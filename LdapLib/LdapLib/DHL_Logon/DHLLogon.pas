unit DHLLogon;

{$HINTS OFF}
{$WARNINGS OFF}
{$WRITEABLECONST ON}

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, Buttons, DBAccess, Ora, Db, MemDS, ExtCtrls, UserSapParam,
  ActiveDirectoryHelpers, DMCEmulation;

type
  TDhlLoginResult = (
    dlrOK {$IFNDEF VER130} = 0 {$ENDIF},
    dlrSystemFunctionCallFailed {$IFNDEF VER130} = -100 {$ENDIF},
    dlrAuthenticationCanceled {$IFNDEF VER130} = -101 {$ENDIF},
    dlrImpersonationFailed {$IFNDEF VER130} = -102 {$ENDIF},
    dlrComputerNotConnectedToDomain {$IFNDEF VER130} = -103 {$ENDIF},
    dlrLocalUserAccount {$IFNDEF VER130} = -104 {$ENDIF},
    dlrGettingUserInfoFailed {$IFNDEF VER130} = -105 {$ENDIF},
    dlrAccountDisabled {$IFNDEF VER130} = -106 {$ENDIF},
    dlrPasswordReseted {$IFNDEF VER130} = -107 {$ENDIF},
    dlrAccountExpired {$IFNDEF VER130} = -108 {$ENDIF},
    dlrInvalidApplicationVersion {$IFNDEF VER130} = -109 {$ENDIF},
    dlrInsufficientPrivileges {$IFNDEF VER130} = -110 {$ENDIF},
    dlrUserNotFounded {$IFNDEF VER130} = -111 {$ENDIF},
    dlrError {$IFNDEF VER130} = -112 {$ENDIF},
    dlrAccountLocked {$IFNDEF VER130} = -113 {$ENDIF},
    dlrGettingSystemInfoFailed {$IFNDEF VER130} = -114 {$ENDIF},
    dlrPasswordMustBeChanged {$IFNDEF VER130} = -115 {$ENDIF},
    dlrDMCConnectError {$IFNDEF VER130} = -116 {$ENDIF},
    dlrDMCOperationError {$IFNDEF VER130} = -117 {$ENDIF}
  );

  TUserParam = class(TObject)
  private
    FUserSapId: string;
    FApplicationUncPath: string;
    FLastname: string;
    FAppointmentDescription: string;
    FUserId: Integer;
    FSessionId: string;
    FComputerName: string;
    FNick: string;
    FApplicationTime: string;
    FApplicationFullPath: string;
    FApplicationSize: string;
    FPrivilegesLevel: string;
    FUserTerminal: string;
    FPassword: string;
    FLogonServer: string;
    FFirstname: string;
    FApplicationPath: string;
    FApplicationName: string;
    FUserName: string;
    FApplicationDate: string;
    FUserTypeDescription: string;
    FApplicationVersion: Integer;
    FApplicationOpLevel: Cardinal;
    FApplicationId: Integer;
    FApplicationNick: string;
  public
    constructor Create;
    procedure Clear;

    property Firstname: string read FFirstname write FFirstname;
    property Lastname: string read FLastname write FLastname;
    property Nick: string read FNick write FNick;
    property Password: string read FPassword write FPassword;
    // opis stanowiska
    property AppointmentDescription: string read FAppointmentDescription write FAppointmentDescription;
    // opis typu u¿ytkownika
    property UserTypeDescription: string read FUserTypeDescription write FUserTypeDescription;
    property ComputerName: string read FComputerName write FComputerName;
    property UserName: string read FUserName write FUserName;
    property LogonServer: string read FLogonServer write FLogonServer;
    property ApplicationName: string read FApplicationName write FApplicationName;
    property ApplicationNick: string read FApplicationNick write FApplicationNick;
    property ApplicationPath: string read FApplicationPath write FApplicationPath;
    property ApplicationUncPath: string read FApplicationUncPath write FApplicationUncPath;
    property ApplicationDate: string read FApplicationDate write FApplicationDate;
    property ApplicationSize: string read FApplicationSize write FApplicationSize;
    property ApplicationFullPath: string read FApplicationFullPath write FApplicationFullPath;
    property ApplicationTime: string read FApplicationTime write FApplicationTime;
    // id aplikacji wg bazy logowania
    property SessionId: string read FSessionId write FSessionId;
    property ApplicationVersion: Integer read FApplicationVersion write FApplicationVersion;
    property ApplicationOpLevel: Cardinal read FApplicationOpLevel write FApplicationOpLevel;
    property ApplicationId: Integer read FApplicationId write FApplicationId;
    property UserId: Integer read FUserId write FUserId;
    // poziom uprawnien
    property PrivilegesLevel: string read FPrivilegesLevel write FPrivilegesLevel;
    property UserTerminal: string read FUserTerminal write FUserTerminal;
    property UserSapId: string read FUserSapId write FUserSapId;
  end;

var
  g_UserSapParam: TUserSapParam;
  g_adsUser: TIADsUser;
  g_UserParam: TUserParam;
  g_applName : string;

function DhlLogin(AApplName: string;
                  AApplVrs: Integer;
                  ASession : TOraSession;
                  username: string;
                  password: string;
                  var errStr : string;
                  ASessionAfterConnect : DMCEmulation.TSessionAfterConnectAdditional = nil
                  ): TDhlLoginResult;
function DHLLogout: Integer;


(***
@abstract (Tworzenie instancji TDataModule na ktorym umieszczony jest TOraSession)
@param    (InstanceClass typ instancji)
@param    (Reference referencja)
Procedura wykonuje to samo co Application.CreateForm.
Powod stworzenia tej procedury - w pliku dpr Delphi instrukcje Application.CreateForm nie moga byc przerwane innymi instrukcjami.
*)
procedure CreateDataModule(InstanceClass: TComponentClass; var Reference);

implementation

uses
  ActiveX;

type
  TInternalLoginResult = (
    ilrOK,
    ilrError,
    ilrInvalidApplicationVersion,
    ilrUserNotFounded,
    ilrInsufficientPrivileges,
    ilrDMCConnectError,
    ilrDMCOperationError
  );

  TCreateInfoResult = (
    cirOK,
    cirError,
    cirUserNotFounded,
    cirInsufficientPrivileges,
    cirDMCOperationError
  );

  DHLLoginError = class(Exception)
  private
    FResult: TDhlLoginResult;
  public
    constructor Create(AResult: TDhlLoginResult; AMsg: string);
    property Result: TDhlLoginResult read FResult;
  end;

{$IFDEF VER130}
function GetEnvironmentVariable(const Name: string): string;
const
  BufSize = 1024;
var
  Len: Integer;
  Buffer: array[0..BufSize - 1] of Char;
begin
  Result := '';
  Len := Windows.GetEnvironmentVariable(PChar(Name), @Buffer, BufSize);
  if Len < BufSize then
    SetString(Result, PChar(@Buffer), Len)
  else
  begin
    SetLength(Result, Len - 1);
    Windows.GetEnvironmentVariable(PChar(Name), PChar(Result), Len);
  end;
end;
{$ENDIF}

procedure RaiseDHLLoginError(AResult: TDhlLoginResult; AMsg: string = ''); forward;
procedure LoadParamUser(AParamUser: string); forward;
procedure LoadSapParamUser(ASapId: string); forward;
function CheckAppl(AMode: string; out aDmcOperationError : boolean): Integer; forward;
function PutInfoToBase(AMode: string): TCreateInfoResult; forward;
function InternalLogin(AApplicationName: string; AMode: string = ' ';
  AApplicationVersion: Integer = 1): TInternalLoginResult; forward;
procedure GetCurrentUserInfo(out AUserName: string; out ADomain: string); forward;
function ImpersonateUser(AUserName, APassword, ADomain: string): Cardinal; forward;
procedure ZeroStr(var AStr: string); forward;
function TDhlLoginResultToStr(AResult: TDhlLoginResult): string; forward;
function SilentMode: Boolean; forward;
function CreateSimpleThread(AStartAddress: TThreadFunc; AParameter: Pointer): Boolean; forward;
function GetSystemInfoThd(AParam: Pointer): Integer; forward;
function GetUserThd(AParam: Pointer): Integer; forward;
procedure ShowErrorMsg(AText: string); forward;
procedure ShowDmcErrorMsg(AText: string; AConnectError : boolean = false); forward;

function ApplAllowedForUser(AExeName, AExeNick: string; AExeVersion: integer;
  AUserLogon,  AUserPrincipalName, ADomain: string;
  var AOplevel: LongWord): TCreateInfoResult;
var
  res: Integer;
begin
  AOplevel := 0;

  res := DMC.Sql([
    ':out := admin.applcd.appl_sb_allowed(',
    '   :exeName, :exenick, :exeVersion, :userLogon, :upn, :userDomain, :opLevel)'],
    ['out', 0,
    'oplevel', 0,
    'exeName', AExeNick, //AExeName,
    'exeNick', AExeNick,
    'exeVersion', AExeVersion,
    'userLogon', AUserLogon,
    'upn', AUserPrincipalName,
    'userDomain', ADomain]);

  if res <> 0 then
  begin
    result := cirDMCOperationError;
    ShowDmcErrorMsg(dmc.ErrText);
    exit;
  end;

  if (DMC.pInt('out') = 1) then
  begin
    AOplevel := DMC.pInt('oplevel');
    result := cirOK;
  end
  else
    result := cirInsufficientPrivileges;
end;

procedure FSplit(pname : string; var d : string; var n : string; var e : string);
var
  p : integer;
begin
  d := ExtractFileDir(pname);
  n := ExtractFileName(pname);
  e := ExtractFileExt(pname);

  p := Pos(e,n);
  if p > 0 then
    n := Copy(n,1,p-1);
end;

function ExtractMainName ( pname : string ) : string;
var
  d : string;
  n : string;
  e : string;
begin
  FSplit(pname,d,n,e);
  Result := n;
end;

function CreateInfo(exeName : string): TCreateInfoResult;
const
  LOGON = 'IN';
var
  applOpLevel: Cardinal;
  odp: Integer;
  outstr: string;
  domain: string;
begin
  //sprawdzenie czy uzytkownik istnieje w bazodanowej czesci systemu logowania
  domain := GetDomainName();
  odp := DMC.SQL([':out := admin.checkuser5(:ulogon, :upn, :domain)'],
    ['out','',
     'ulogon', g_UserParam.UserName,
     'upn', '',
     'domain', domain]);
  if odp = 0 then
  begin
    outstr := DMC.pStr('out');
    if outstr <> '' then
    begin
      LoadParamUser(outstr);
      { TODO: odblokowaæ gdy potrzebne z SAP-a dane o u¿ytkowniku
        LoadSapParamUser(UserParamMS[20]); }
    end
    else begin
      Result := cirUserNotFounded;
      Exit;
    end;
  end
  else begin
    Result := cirDMCOperationError;
    ShowDmcErrorMsg(dmc.ErrText);
    Exit;
  end;


  Result := ApplAllowedForUser(
              exename, g_UserParam.ApplicationNick,
               g_UserParam.ApplicationVersion, g_UserParam.UserName,
              '', domain,
              applOpLevel);
  if Result = cirOK then
  begin
    g_UserParam.ApplicationOpLevel := applOpLevel;
    Result := PutInfoToBase(LOGON);
  end;
end;

function InternalLogin(AApplicationName: string; AMode: string = ' ';
  AApplicationVersion: Integer = 1): TInternalLoginResult;
var
  ApplID: Integer;
  ApplName: string;
  lDmcOperationError : boolean;
begin
  Result := ilrError;

//  if length(AApplicationName) > 30 then
//    ApplName := Copy(AApplicationName, 1, 30)
//  else
  ApplName := AApplicationName.SubString(AApplicationName.LastDelimiter(PathDelim + DriveDelim) + 1);

  g_UserParam.ApplicationNick := ApplName.Split(['.'])[0].ToUpper();
  g_UserParam.ApplicationVersion := AApplicationVersion;

  g_UserParam.ComputerName := Uppercase(Trim(GetEnvironmentVariable('COMPUTERNAME')));
  //g_UserParam.UserName := Uppercase(AllTrim(GetEnvVariable('USERNAME')));
  g_UserParam.LogonServer := Uppercase(Trim(GetEnvironmentVariable('LOGONSERVER')));

  if AMode <> 'ADMIN' then
    AMode := ' ';

  DMC.Connect;
  if DMC.ErrCode > 0 then
  begin
    Result := ilrDMCConnectError;
    ShowDmcErrorMsg(DMC.ErrText, true); // drugi parametr oznacza b³¹d po³¹czenia
    Exit;
  end;

  ApplID := CheckAppl(AMode, lDmcOperationError);

  if lDmcOperationError then
  begin
    Result := ilrDMCOperationError;
    exit;
  end;

  if ApplID <> 0 then
  begin
    g_UserParam.ApplicationId := ApplID;
    case CreateInfo(ApplName) of
      cirOK: Result := ilrOK;
      cirError: Result := ilrError;
      cirUserNotFounded: Result := ilrUserNotFounded;
      cirInsufficientPrivileges: Result := ilrInsufficientPrivileges;
      cirDMCOperationError: Result := ilrDMCOperationError;
    end;

    if Result = ilrOK then // jesli zalogowany to ustawiamy kontekst sessionModule, sessionAction i ADMIN.dhl_user_env_pkg
    begin
      DMC.SessionAfterConnect(nil); // wywolujemy sztucznie zdarzenie afterConnect
      DMC.EnabledSessionAfterConnect(); // ustawiamy zdarzenie afterConnect dla sesji
    end;
  end
  else
    Result := ilrInvalidApplicationVersion;
end;

// wpisanie informacji do tabeli admin.LOGACTIVE
// mozliwe wartosci zwrotne: cirOk, cirError, cirDmcOperationError
function PutInfoToBase_Login : TCreateInfoResult;
var
  lDmcResult : integer;
begin
  lDmcResult := DMC.SQL(
    [':out := admin.loginuser4('+
        ':pUserFirstname,'+
        ':pUserLastname,'+
        ':pUserNick,'+
        ':pMode,'+
        ':pApplicationNick,'+
        ':pComputerName,'+
        ':pSAMAccountName,'+
        ':pUserPrincipalName,'+
        ':pDomainName,'+
        ':pLogonServer,'+
        ':pApplicationId);'],
    ['out',0,
     'pUserFirstname', g_UserParam.Firstname,
     'pUserLastname', g_UserParam.Lastname,
     'pUserNick', g_UserParam.Nick,
     'pMode', 'IN',
     'pApplicationNick', g_UserParam.ApplicationNick,
     'pComputerName', g_UserParam.ComputerName,
     'pSAMAccountName', g_adsUser.SAMAccountName,
     'pUserPrincipalName', g_adsUser.UserPrincipalName,
     'pDomainName', GetDomainName(),
     'pLogonServer', g_UserParam.LogonServer,
     'pApplicationId', IntToStr(g_UserParam.ApplicationId)]);

    if lDmcResult = 0 then
    begin
      g_UserParam.SessionId := DMC.Params.ParamByName('out').asString;

      if g_UserParam.SessionId <> '' then
        result := cirOK
      else
        result := cirError;
    end else
    begin
      result := cirDMCOperationError;
      ShowDmcErrorMsg(dmc.ErrText);
      Exit;
    end;
end;

// update informacji o wylogowaniu w tabeli admin.logactive
// mozliwe wartosci zwrotne: cirOk, cirDmcOperationError
function PutInfoToBase_Logout : TCreateInfoResult;
var
  lDmcResult : integer;
begin
  lDmcResult := DMC.SQL(
    [':out := admin.logout(:pSessionId);'],
    ['out',0,
     'pSessionId', g_UserParam.SessionId]);

    if lDmcResult = 0 then
      result := cirOK
    else
    begin
      result := cirDMCOperationError;
      ShowDmcErrorMsg(dmc.ErrText);
      Exit;
    end;
end;

// mozliwe wartosci zwrotne: cirOk, cirError, cirDmcOperationError
function PutInfoToBase(AMode: string): TCreateInfoResult;
begin
  if AMode = 'IN' then
  begin
    result := PutInfoToBase_Login();
  end
  else
    result := PutInfoToBase_Logout();
end;

// z SB\Terminal.pas
function ItemSection ( s : string; i : word; c : char ) : string;
var j, k, l, m : word;
begin s := s+c; l := Length(s); j := 1; k := 1;
      while (k <= l) and (j < i) do
      begin if s[k] = c then Inc(j);
            Inc(k);
      end;
      m := k;
      while (m <= l) and (s[m] <> c) do Inc(m);
      ItemSection := Copy(s,k,m-k);
end;

procedure LoadParamUser(AParamUser: string);
begin
  g_UserParam.Firstname := ItemSection(AParamUser,1,'|');
  g_UserParam.Lastname := ItemSection(AParamUser,2,'|');
  g_UserParam.Nick := ItemSection(AParamUser,3,'|');
  g_UserParam.Password := ItemSection(AParamUser,4,'|');
  g_UserParam.AppointmentDescription := ItemSection(AParamUser,5,'|');
  g_UserParam.UserTypeDescription := ItemSection(AParamUser,6,'|');

  g_UserParam.UserId := StrToIntDef(trim(ItemSection(AParamUser,7,'|')), 0);
  g_UserParam.PrivilegesLevel := ItemSection(AParamUser,8,'|');
  g_UserParam.UserTerminal := ItemSection(AParamUser,9,'|');
  g_UserParam.UserSapId := ItemSection(AParamUser,10,'|');
end;

procedure LoadSapParamUser(ASapId: string);
var
  id: Integer;
begin
  try
    id := StrToInt(ASapId);
    g_UserSapParam := TUserSapParam.Create();
    g_UserSapParam.GetDataFromDatabase(id);
  except
    //nothing
  end;
end;

function PadL(const s: string; len: integer; c: char) : string;
var
  i: integer;
begin
  i:=length(s);
	if i<len then
    result := stringofchar(c, len-i)+s
  else
    result := s;
end;

function CheckAppl(AMode: string; out aDmcOperationError : boolean): Integer;
var
  sr: TSearchRec;
  v: Integer;
  year, month, day: Word;
  hour, min, sec, msec: Word;
  nameappl: string;
  pathappl: string;
  fullname: string;
  pathunc: string;
  dateappl: string;
  volappl: string;
  timeappl: string;
  lDmcResult : integer;
begin
  Result := 0;
  v := 0;
  aDmcOperationError := false;

  fullname := ExpandFileName(g_applName); // "/C:/MyWork/PROJECTS/Java/CasAdmin_1.65/build/classes/"
  nameappl := fullName.SubString(fullName.LastDelimiter(PathDelim + DriveDelim) + 1);
//  nameappl := ExtractFileName(ParamStr(0));// CasAdmin.jar

  pathappl := fullName.SubString(0, fullName.LastDelimiter(PathDelim + DriveDelim) + 1);

//  pathappl := ExtractFilePath(ParamStr(0)); //"/C:/MyWork/PROJECTS/Java/CasAdmin_1.65/build/classes/"
  pathunc := ExpandUNCFileName(fullname);


  if FindFirst(fullname, v, sr) = 0 then
  begin
    DecodeDate(FileDateToDateTime(sr.time),year, month, day);
    DecodeTime(FileDateToDateTime(sr.time),hour, min, sec, msec);
    dateappl := IntToStr(year)+'-'+padl(IntToStr(month),2,'0')+
      '-'+padl(IntToStr(day),2,'0');
    volappl := IntToStr(sr.size);
    timeappl := Padl(IntToStr(hour),2,'0')+':'+Padl(IntToStr(min),2,'0')+
      ':'+Padl(IntToStr(sec),2,'0');
  end;
  FindClose(sr);

  g_UserParam.ApplicationName  := uppercase(nameappl);
  g_UserParam.ApplicationPath := uppercase(pathappl);
  g_UserParam.ApplicationUncPath := uppercase(pathunc);
  g_UserParam.ApplicationDate := uppercase(dateappl);
  g_UserParam.ApplicationSize := uppercase(volappl);
  g_UserParam.ApplicationFullPath := uppercase(fullname);
  g_UserParam.ApplicationTime := uppercase(timeappl);

  lDmcResult := DMC.SQL([
    ':out := admin.checkappl3('+
               ':pApplicationNick,'+
               ':pApplicationName,'+
               ':pApplicationPath,'+
               ':pApplicationUncPath,'+
               ':pApplicationDate,'+
               ':pApplicationTime,'+
               ':pApplicationSize,'+
               ':pMode);'],
    ['out',0,
    'pApplicationNick', g_UserParam.ApplicationNick,
    'pApplicationName', g_UserParam.ApplicationNick, //g_UserParam.ApplicationName,
    'pApplicationPath', g_UserParam.ApplicationPath,
    'pApplicationUncPath', g_UserParam.ApplicationUncPath,
    'pApplicationDate', g_UserParam.ApplicationDate,
    'pApplicationTime', g_UserParam.ApplicationTime,
    'pApplicationSize', g_UserParam.ApplicationSize,
    'pMode', AMode]);

  if lDmcResult = 0 then
  begin
    Result := DMC.pInt('out');
  end else
  begin
    aDmcOperationError := true;
    ShowDmcErrorMsg(dmc.ErrText);
    Exit;
  end;
end;

procedure GetCurrentUserInfo(out AUserName: string; out ADomain: string);
begin
  AUserName := '';
  ADomain := '';

  AUserName := GetEnvironmentVariable('USERNAME');
  ADomain := GetEnvironmentVariable('USERDOMAIN');
  {adsi := TIADsADSystemInfo.Create();
  try
    if adsi.Fill() then
    begin
      ADomain := adsi.DomainShortName;
      usr := adsi.UserName;
    end;
  finally
    adsi.Free();
  end;

  if usr = '' then
    Exit;

  adu := TIADsUser.Create();
  try
    if adu.FillFromADsPath(usr) then
      AUserName := adu.SAMAccountName;
  finally
    adu.Free();
  end;}
end;

function ImpersonateUser(AUserName, APassword, ADomain: string): Cardinal;
var
  token: NativeUInt;
  res: Boolean;
begin
  Result := ERROR_SUCCESS;
  
  res := LogonUser(PChar(AUserName), PChar(ADomain),
                   PChar(APassword), LOGON32_LOGON_INTERACTIVE, LOGON32_PROVIDER_DEFAULT,
                   token);
  if not res then
  begin
    Result := GetLastError;
    Exit;
  end;

  try
    if not ImpersonateLoggedOnUser(token) then
      Result := GetLastError;
  finally
    CloseHandle(token);
  end;
end;

procedure ZeroStr(var AStr: string);
begin
  AStr := StringOfChar(' ', Length(AStr))
end;

function DhlLogin(AApplName: string;
                  AApplVrs: Integer;
                  ASession : TOraSession;
                  username: string;
                  password: string;
                  var errStr : string;
                  ASessionAfterConnect : TSessionAfterConnectAdditional = nil
                  ): TDhlLoginResult;
const
  SECONDS_IN_DAY = 24*60*60;
var
  domainname: string;
  ilresult: TInternalLoginResult;
  iuresult: Cardinal;
  udn: string;
  dcstr: string;
  elapsedseconds: Extended;
  maxpwdage: Int64;

  function NoPermission: string;
  begin
    Result := Format('brak pozwolenia na uruchomienie aplikacji'#13#10+
      'exe: %s'#13#10'nick: %s'#13#10'vrs: %d'#13#10'user: %s'#13#10+
      'Skontaktuj siê z dzia³em IT',
      [AApplName, g_UserParam.ApplicationNick,
      g_UserParam.ApplicationVersion, g_adsUser.SAMAccountName]);
  end;

begin
  Result := dlrError;
  errStr := '';
  domainname := '';
  g_applName := AApplName;
//  username := Uppercase(Trim(GetEnvironmentVariable('USERNAME')));

  if not g_applName.EndsWith('.jar') then begin
     Result := dlrOK;
  end else begin
      DMC.SetSession(ASession, ASessionAfterConnect);

      try
        try
          //poczatek uwierzytelniania
          try
            GetCurrentUserInfo(username, domainname);
            iuresult := ImpersonateUser(username, password, domainname);
            if iuresult <> ERROR_SUCCESS then
              RaiseDHLLoginError(dlrImpersonationFailed, SysErrorMessage(iuresult));
          finally
            ZeroStr(password);
          end;

          Application.ProcessMessages();

          //sprawdzam czy komputer jest podlaczony do sieci i jednoczenie do domeny
          if GetDomainName() = '' then
            RaiseDHLLoginError(dlrComputerNotConnectedToDomain);

          //sprawdzam czy uzytkownik nie jest uzytkownikem lokalnym
          if not GetUserFullyQualifiedDN(udn) or (udn = '') then
            RaiseDHLLoginError(dlrLocalUserAccount);

          Application.ProcessMessages();

          g_adsUser := TIADsUser.Create();
          if not g_adsUser.FillFromADsPath(udn) then
            RaiseDHLLoginError(dlrGettingUserInfoFailed);

          Application.ProcessMessages();

          //sprawdzam czy has³o nie wygas³o
          if not g_adsUser.PasswordNeverExpires and (g_adsUser.PasswordLastChanged <> 0) then
          begin
            maxpwdage := GetDomainMaxPasswordAge(ExtractDomainControllerPart(udn));
            elapsedseconds := SECONDS_IN_DAY*(Now() - g_adsUser.PasswordLastChanged);
            if (maxpwdage > 0) and (elapsedseconds > 0) and (elapsedseconds >= maxpwdage) then
              RaiseDHLLoginError(dlrPasswordMustBeChanged);
          end;

          //sprawdzam czy konto nie zostalo zablokowane przez administratorow domeny
          if g_adsUser.AccountDisabled then
            RaiseDHLLoginError(dlrAccountDisabled);

          //sprawdzam czy konto nie zosta³o zablokowane
          if g_adsUser.IsAccountLocked then
            RaiseDHLLoginError(dlrAccountLocked);

          //sprawdzam czy konto nadal jest wazne, tzn. czy uzytkownik moze logowac sie
          if g_adsUser.AccountExpired then
            RaiseDHLLoginError(dlrAccountExpired);

          RevertToSelf;

          //koniec uwierzytelniania

          Application.ProcessMessages();

          g_UserParam.UserName := username;

          //autoryzacja
          ilresult := InternalLogin(AApplName, 'ADMIN', AApplVrs);
          case ilresult of
            ilrInvalidApplicationVersion: Result := dlrInvalidApplicationVersion;
            ilrUserNotFounded: Result := dlrUserNotFounded;
            ilrInsufficientPrivileges: Result := dlrInsufficientPrivileges;
            ilrError: Result := dlrError;
            ilrDMCConnectError: Result := dlrDMCConnectError;
            ilrDMCOperationError: Result := dlrDMCOperationError;
          else
            Result := dlrOK;
          end;
        except
          on e: DHLLoginError do
          begin
            Result := e.Result;
            errstr := e.Message;
          end;
          on e: ADException do
          begin
            Result := dlrSystemFunctionCallFailed;
            errstr := Format('%s (%d) : %s (%x)', [e.Message, e.ErrorCode,
              e.OsApiErrorMsg, e.OsApiErrorCode]);
          end;
        end;
      finally
        if not SilentMode() and
          (Result <> dlrOk) and (Result <> dlrAuthenticationCanceled) then
        begin
          if (Result = dlrUserNotFounded) or (Result = dlrInsufficientPrivileges) or
            (Result = dlrError) then
            errstr := NoPermission()
          else if Result = dlrError then
            errstr := 'nieoczekiwany b³¹d'
          else if (Result <> dlrImpersonationFailed) and
            (Result <> dlrSystemFunctionCallFailed) then
            errstr := TDhlLoginResultToStr(Result);

          // dlrDMCOperationError, dlrDMCConnectError nie sa brane pod uwage, bo
          // komunikat przy tego typu bledzie jest wyswietlany natychmiast - patrz wystapienia procedury ShowDmcErrorMsg
          if (Result <> dlrDMCOperationError) and (result <> dlrDMCConnectError) then
            ShowErrorMsg(errstr);
        end;
        ExitCode := Integer(Result);
      end;
  end;
end;

procedure ShowDmcErrorMsg(AText: string; AConnectError : boolean = false);
var
  lErrorTypeMsg : string;
begin
  if not SilentMode then
  begin
    if AConnectError then
      lErrorTypeMsg := 'b³¹d przy po³¹czeniu z baz¹ centraln¹'
    else
      lErrorTypeMsg := 'b³¹d przy wykonaniu operacji na bazie centralnej';

    ShowErrorMsg(lErrorTypeMsg+#13#10#13#10+
                 'szczegó³y b³êdu: '#13#10+
                 AText);
  end;
end;

procedure ShowErrorMsg(AText: string);
const
  TYP: Cardinal = MB_ICONERROR or MB_OK or MB_SYSTEMMODAL;
var
  hdl: THandle;
begin
{$IFDEF VER130}
  hdl := 0;
{$ELSE}
  hdl := Application.ActiveFormHandle;
{$ENDIF}
  MessageBox(hdl, PChar(AText), 'B³¹d', TYP);
end;

function DHLLogout: Integer;
begin
  if g_applName.EndsWith('.jar') then
     Result := integer(PutInfoToBase('OUT'))
  else result := 0;
end;

function TDhlLoginResultToStr(AResult: TDhlLoginResult): string;
begin
  case AResult of
    dlrOK:
      Result := 'ok';
    dlrSystemFunctionCallFailed:
      Result := 'wywo³anie funkcji systemowej siê nie powiod³o';
    dlrAuthenticationCanceled:
      Result := 'sprawdzenie uprawnieñ do aplikacji przerwane przez u¿ytkownika';
    dlrImpersonationFailed:
      Result := 'nieznana nazwa u¿ytkownika lub nieprawid³owe has³o';
    dlrComputerNotConnectedToDomain:
      Result := 'komputer nie jest pod³¹czony do sieci lub domeny';
    dlrLocalUserAccount:
      Result := 'aplikacjê mo¿na uruchomiæ tylko z konta u¿ytkownika domenowego';
    dlrGettingUserInfoFailed:
      Result := 'nie uda³o siê pobraæ danych u¿ytkownika z domeny';//' konto u¿ytkownika nie istnieje lub jest zablokowane lub has³o wymaga zmiany';
    dlrAccountDisabled:
      Result := 'twoje konto zosta³o wy³¹czone';
    dlrPasswordReseted:
      Result := 'musisz zmieniæ has³o swojego konta';
    dlrAccountExpired:
      Result := 'twoje konto wygas³o';
    dlrInvalidApplicationVersion:
      Result := 'nieprawid³owa wersja aplikacji';
    dlrInsufficientPrivileges:
      Result := 'nie masz wystarczaj¹cych uprawnieñ by uruchomiæ aplikacjê';
    dlrUserNotFounded:
      Result := 'nie znaleziono u¿ytkownika';
    dlrError:
      Result := 'inny b³¹d';
    dlrAccountLocked:
      Result := 'twoje konto jest obecnie zablokowane i nie mo¿na logowaæ siê za jego pomoc¹';
    dlrGettingSystemInfoFailed:
      Result := 'nie uda³o siê pobraæ informacji o komputerzez z domeny';
    dlrPasswordMustBeChanged:
      Result := 'musisz zmieniæ has³o swojego konta';
    dlrDMCConnectError: // komunikat o tym bledzie pokazuje sie odrazu w nastepnych linijkach kodu za DMC.SQL (patrz procedure ShowDmcErrorMsg)
      Result := '';
    dlrDMCOperationError: // komunikat o tym bledzie pokazuje sie odrazu w nastepnych linijkach kodu za DMC.SQL (patrz procedure ShowDmcErrorMsg)
      Result := ''
  else
    Result := 'to nie powinno siê pojawiæ';
  end;
end;

function SilentMode: Boolean;
var
  i: Integer;
begin
  Result := False;
  for i := 1 to ParamCount() do
  begin
    Result := LowerCase(ParamStr(i)) = '-sm';
    if Result then
      Exit;
  end;
end;

function CreateSimpleThread(AStartAddress: TThreadFunc; AParameter: Pointer): Boolean;
var
  tid: Cardinal;
  hdl: array[1..1] of THandle;
begin
  Result := False;
  hdl[1] := BeginThread(nil, 0, AStartAddress, AParameter, 0, tid);
  if hdl[1] = 0 then
    Exit;

  try
    while True do
      case MsgWaitForMultipleObjects(1, hdl, False, INFINITE, QS_ALLINPUT) of
        WAIT_OBJECT_0: Break;
      else
        Application.ProcessMessages();
      end;
  finally
    CloseHandle(hdl[1]);
  end;

  Result := True;
end;

function GetSystemInfoThd(AParam: Pointer): Integer;
begin
  try
    CoInitialize(nil);
    try
//      PBoolean(AParam)^ := g_adsSystemInfo.Fill();
    finally
      CoUninitialize();
    end;
  except
  end;
  Result := 0;
end;

function GetUserThd(AParam: Pointer): Integer;
begin
  CoInitialize(nil);
  try
    try
//      PBoolean(AParam)^ := g_adsUser.FillFromADsPath(g_adsSystemInfo.UserName);
    finally
      CoUninitialize();
    end;
  except
  end;
  Result := 0;
end;

{ TUserParam }

procedure TUserParam.Clear;
begin
  FUserSapId := '';
  FApplicationUncPath := '';
  FLastname := '';
  FAppointmentDescription := '';
  FUserId := -1;
  FSessionId := '';
  FComputerName := '';
  FNick := '';
  FApplicationTime := '';
  FApplicationFullPath := '';
  FApplicationSize := '';
  FPrivilegesLevel := '';
  FUserTerminal := '';
  FPassword := '';
  FLogonServer := '';
  FFirstname := '';
  FApplicationPath := '';
  FApplicationName := '';
  FUserName := '';
  FApplicationDate := '';
  FUserTypeDescription := '';
  FApplicationVersion := 0;
  FApplicationOpLevel := 0;
  FApplicationId := -1;
  FApplicationNick := '';
end;

constructor TUserParam.Create;
begin
  Clear();
end;

{ DHLLoginException }

constructor DHLLoginError.Create(AResult: TDhlLoginResult; AMsg: string);
begin
  inherited Create(AMsg);
  FResult := AResult;
end;

procedure RaiseDHLLoginError(AResult: TDhlLoginResult; AMsg: string = '');
begin
  raise DHLLoginError.Create(AResult, AMsg);
end;

procedure CreateDataModule(InstanceClass: TComponentClass; var Reference);
begin
  Application.CreateForm(InstanceClass, Reference);
end;


initialization
  CoInitialize(nil);
  g_UserParam := TUserParam.Create;

finalization
  g_UserParam.Free();
  g_adsUser.Free();
  g_UserSapParam.Free();
  CoUninitialize();



//------------------------------------------------------------------------------
// Przyklad wykorzystania modulu logowania bez DMC (w pliku .dpr projektu)
//
//
//
//program HelloWorld;
//
//uses
//  Forms,
//  Main in 'Main.pas' {formMain},
//  DataModuleOraSession in 'DataModuleOraSession.pas' {dmOraSession: TDataModule}, // TDataModule gdzie znajduje sie komponent TOraSession
//  DHLLogon;
//
//{$R *.res}
//
//begin
//  Application.Initialize;
//  CreateDataModule(TdmOraSession, dmOraSession);
//
//  if DhlLogin('HelloWorld', // nazwa aplikacji
//              1, // wersja aplikacji uzyta przy logowaniu
//              true, // true - pytac uzytkownika o haslo, false - uzyc biezacego konta domenowego
//              dmOraSession.oraSessionMain, // obiekt TOraSession
//              dmOraSession.sessionAfterConnect // opcjonalna procedura ktora wykonuje dodatkowe czynnosci po nawiazaniu polaczenia z baza
//              )<> dlrOK then
//    exit;
//
//  Application.CreateForm(TformMain, formMain);
//  Application.Run;
//
//  DHLLogout();
//end.
//
//
// dla uzytkownika, na ktorego loguje sie sesja dodac GRANT na Execute dla:
// - admin.applcd
// - admin.checkuser5
// - admin.checkappl3
// - admin.loginuser4
// - admin.logout
//------------------------------------------------------------------------------

end.


