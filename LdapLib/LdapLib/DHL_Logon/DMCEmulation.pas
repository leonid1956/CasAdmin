(***
@abstract (Emulacja obiektu DMC)
@author   (Oleksandr Pylypchuk)
@created  (12.10.2010)
@lastmod  (12.10.2010)
@version  (1.00)
Lekki modul emulujacy dzialanie DMC dedykowany dla modulu DHLLogon. Daje mozliwosc odlaczenia sie od bibliotek SB/MS.
*)

unit DMCEmulation;

interface

uses SysUtils, Ora, variants, Db, FileVersion;

type
  TSessionAfterConnectAdditional = procedure (Sender: TObject) of object;

  TDMCEmulator = class
  private
    class var SingleInstance : TDMCEmulator;
    fSession : TOraSession;
    fOraSql : TOraSQL;
    fErrCode : integer;
    fErrText : string;
    fSessionAfterConnectAdditional : TSessionAfterConnectAdditional;
    function getSqlText(s: array of string; plsql : boolean = true): string;
    procedure AssignParamsToOraParams(pars: array of Variant; oraParams : TOraParams);

  public
    procedure SetSession(aOraSession : TOraSession; aSessionAfterConnectAdditional : TSessionAfterConnectAdditional = nil);
    function Connect : integer;
    function SQL(s : array of string; pars : array of Variant) : integer;
    function Query(s : array of string; pars : array of Variant; query : TOraQuery) : integer;
    function ErrText : string;
    function ErrCode : integer;
    function Params : TOraParams;
    function pInt(aParamName : string) : integer;
    function pStr(aParamName : string) : string;
    class function getInstance : TDMCEmulator;
    class procedure freeClassInstance;
    constructor Create;
    destructor Destroy; override;

    procedure EnabledSessionAfterConnect;
    procedure SessionAfterConnect(Sender : TObject);
  end;

  function DMC : TDMCEmulator;

implementation

uses DHLLogon;

{ TDMCEmulator }

procedure TDMCEmulator.SessionAfterConnect(Sender: TObject);
var
  oraSQL : TOraSQL;
  info   : FileVersionRec;

  vModuleInfo : string;
  vModuleAction : string;

  fvinfo : FileVersionRec;

begin
  oraSQL := TOraSQL.Create(nil);

  try
    oraSQL.Session := fSession;
    if GetFileVersionRec(info) then
    begin
      vModuleInfo := info.ProductName + ' ' + GetFileVersionNumberString4(info);

      if info.OracleModuleInfo <> '' then
        vModuleInfo := vModuleInfo + ' ' + info.OracleModuleInfo;

      vModuleAction := 'Nawi¹zanie po³¹czenia z baz¹';
    end else
    begin
      vModuleInfo := info.ProductName;
      if info.OracleModuleInfo <> '' then
        vModuleInfo := vModuleInfo + ' ' + info.OracleModuleInfo;
      vModuleAction := '';
    end;

    oraSQL.Text :=
    'begin '+
      'ADMIN.dhl_user_env_pkg.set_context_and_app_info('+
         ':p_user,'+
         ':p_user_firstname,'+
         ':p_user_lastname,'+
         ':p_user_id,'+
         ':p_user_sap_id,'+
         ':p_terminal,'+
         ':p_app_nick,'+
         ':p_app_internal_version,'+
         ':p_app_std_version,'+
         ':p_privileges,'+
         ':p_test_mode,'+
         ':p_client_id,'+
         ':p_module_name,'+
         ':p_action_name,'+
         ':p_client_info'+
       '); '+
    'end;';


    oraSQL.ParamByName('p_user').AsString := g_UserParam.Nick;
    oraSQL.ParamByName('p_user_firstname').AsString := g_UserParam.firstname;
    oraSQL.ParamByName('p_user_lastname').AsString := g_UserParam.lastname;
    oraSQL.ParamByName('p_user_id').AsInteger := g_UserParam.userID;
    if strToIntDef(g_UserParam.UserSapId, -1) = -1 then
      oraSQL.ParamByName('p_user_sap_id').Value := null
    else
      oraSQL.ParamByName('p_user_sap_id').AsInteger := strToInt(g_UserParam.UserSapId);

    oraSQL.ParamByName('p_terminal').AsString := g_UserParam.UserTerminal;
    oraSQL.ParamByName('p_app_nick').AsString := g_UserParam.ApplicationNick;
    oraSQL.ParamByName('p_app_internal_version').AsInteger := g_UserParam.ApplicationVersion;

    if GetFileVersionRec(fvinfo) then
    begin
      oraSQL.ParamByName('p_app_std_version').AsString := GetFileVersionNumberString4(fvinfo);
    end else
    begin
      oraSQL.ParamByName('p_app_std_version').AsString := '';
    end;

    oraSQL.ParamByName('p_privileges').AsString := inttostr(g_UserParam.ApplicationOpLevel);

    {$IFDEF TEST_MODE}
    oraSQL.ParamByName('p_test_mode').AsString := 'TRUE';
    {$ESLE}
    oraSQL.ParamByName('p_test_mode').Value := null;
    {$ENDIF}
    oraSQL.ParamByName('p_client_id').Value := null;
    oraSQL.ParamByName('p_module_name').AsString := vModuleInfo;
    oraSQL.ParamByName('p_action_name').AsString := vModuleAction;
    oraSQL.ParamByName('p_client_info').Value := null;

    oraSQL.Execute();

  finally
    oraSQL.Free();
  end;

  if @fSessionAfterConnectAdditional <> nil then
    fSessionAfterConnectAdditional(Sender);
end;

procedure TDMCEmulator.EnabledSessionAfterConnect;
begin
  fSession.AfterConnect := SessionAfterConnect;
end;

procedure TDMCEmulator.SetSession(aOraSession: TOraSession; aSessionAfterConnectAdditional : TSessionAfterConnectAdditional = nil);
begin
  fSession := aOraSession;
  fSessionAfterConnectAdditional := aSessionAfterConnectAdditional;
  fOraSql.Session := fSession;
end;

function TDMCEmulator.Connect : integer;
begin
  result := 0;

  try
    if not fSession.Connected then
      fSession.Connect;
  except
    on e : exception do
    begin
      fErrCode := 1;
      fErrText := e.Message;
      Result := 2;
    end;
  end;
end;

constructor TDMCEmulator.Create;
begin
  inherited;
  fOraSql := TOraSQL.Create(nil);
  fOraSql.AutoCommit := false;
  fOraSql.ParamCheck := true;
  fSessionAfterConnectAdditional := nil;
end;

destructor TDMCEmulator.Destroy;
begin
  fOraSql.Free;
  inherited;
end;

function TDMCEmulator.ErrCode: integer;
begin
  result := fErrCode;
end;

function TDMCEmulator.ErrText: string;
begin
  result := fErrText;
end;

class procedure TDMCEmulator.freeClassInstance;
begin
  if SingleInstance <> nil then
    SingleInstance.Destroy;
end;

class function TDMCEmulator.getInstance: TDMCEmulator;
begin
  if SingleInstance = nil then
    SingleInstance := TDMCEmulator.Create;

  result := SingleInstance;
end;

function TDMCEmulator.Params: TOraParams;
begin
  result := fOraSQL.Params;
end;

function TDMCEmulator.pInt(aParamName: string): integer;
begin
  if fOraSQL.ParamByName(aParamName).IsNull then
    Result := 0
  else
    Result := fOraSQL.ParamByName(aParamName).AsInteger;
end;

function TDMCEmulator.pStr(aParamName: string): string;
begin
  result := fOraSQL.ParamByName(aParamName).AsString;
end;

function TDMCEmulator.Query(s: array of string; pars: array of Variant;
  query: TOraQuery): integer;
begin
  result := 0;

  query.SQL.Text := getSqlText(s);
  AssignParamsToOraParams(pars, query.Params);
  try
    query.Open;
  except
    on e : Exception do
    begin
      fErrCode := 4;
      fErrText := e.Message;
      Result := 3;
    end;
  end;
end;

function TDMCEmulator.getSqlText(s: array of string; plsql : boolean = true) : string;
var
  i : integer;
  lSqlText : string;

begin
  for i := 0 to length(s) - 1 do
  begin
    if i > 0 then
      lSqlText := lSqlText + #13#10;

    lSqlText := lSqlText + s[i];
  end;
  lSqlText := Trim(UpperCase(lSqlText));

  if not plsql then
  begin
    result := lSqlText;
    exit;
  end;

  if (Copy(lSqlText,1,8) <> 'DECLARE ') and
     (Copy(lSqlText,1,8) <> 'DECLARE'#13) and
     (Copy(lSqlText,1,6) <> 'BEGIN ') and
     (Copy(lSqlText,1,6) <> 'BEGIN'#13) then
  begin
    lSqlText := 'begin '+lSqlText;
  end;

  if copy(lSqlText, length(lSqlText), 1) <> ';' then
    lSqlText := lSqlText + ';';

  if (Copy(lSqlText, length(lSqlText) - 4, 5) <> ' END;') and
     (Copy(lSqlText, length(lSqlText) - 4, 5) <> ';END;') and
     (Copy(lSqlText, length(lSqlText) - 4, 5) <> #10'END;') then
  begin
    lSqlText := lSqlText + ' end;';
  end;

  result := lSqlText;
end;

procedure TDMCEmulator.AssignParamsToOraParams(pars: array of Variant; oraParams : TOraParams);
var
  i : integer;
  lVarType : integer;
  lParamNameIndex : integer;
  lParamValueIndex : integer;
  lOraParam : TOraParam;
begin
  for i := 0 to (length(pars) div 2) - 1 do
  begin
    lParamNameIndex  := i * 2;
    lParamValueIndex := i * 2 + 1;
    lVarType := VarType(pars[lParamValueIndex]);

    lOraParam := oraParams.ParamByName(pars[lParamNameIndex]);

    if lVarType = varNull then
      lOraParam.DataType := ftString
    else if lVarType = varBoolean then
      lOraParam.DataType := ftBoolean
    else if lVarType = varDate then
      lOraParam.DataType := ftDateTime
    else if lVarType = varDouble then
      lOraParam.DataType := ftFloat
    else if lVarType in [varByte,varInteger] then
      lOraParam.DataType := ftInteger
    else
      lOraParam.DataType := ftString;

    lOraParam.Value := pars[lParamValueIndex];
    lOraParam.ParamType := ptInputOutput;
  end;
end;

function TDMCEmulator.SQL(s: array of string; pars: array of Variant): integer;
begin
  result := 0;

  fOraSql.SQL.Text := getSqlText(s);
  AssignParamsToOraParams(pars, fOraSql.Params);
  try
    fOraSql.Execute;
  except
    on e : Exception do
    begin
      fErrCode := 2;
      fErrText := e.Message;
      Result := 3;
    end;
  end;
end;

function DMC : TDMCEmulator;
begin
  result := TDMCEmulator.getInstance();
end;

initialization
  TDMCEmulator.SingleInstance := nil;

finalization
  TDMCEmulator.freeClassInstance;

end.
