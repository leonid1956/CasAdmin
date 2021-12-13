unit ActiveDirectoryHelpers;

{$I delphi_versions.inc}

interface

uses
  Classes, ActiveDs_TLB, Contnrs, ADSHLP, SysUtils, Variants, DateUtils;

const
//  TExtendedNameFormat = (
    // Examples for the following formats assume a fictitous company
    // which hooks into the global X.500 and DNS name spaces as follows.
    //
    // Enterprise root domain in DNS is
    //
    //      widget.com
    //
    // Enterprise root domain in X.500 (RFC 1779 format) is
    //
    //      O=Widget, C=US
    //
    // There exists the child domain
    //
    //      engineering.widget.com
    //
    // equivalent to
    //
    //      OU=Engineering, O=Widget, C=US
    //
    // There exists a container within the Engineering domain
    //
    //      OU=Software, OU=Engineering, O=Widget, C=US
    //
    // There exists the user
    //
    //      CN=John Doe, OU=Software, OU=Engineering, O=Widget, C=US
    //
    // And this user's downlevel (pre-ADS) user name is
    //
    //      Engineering\JohnDoe

    // unknown name type
    NameUnknown = 0;

    // CN=John Doe, OU=Software, OU=Engineering, O=Widget, C=US
    NameFullyQualifiedDN = 1;

    // Engineering\JohnDoe
    NameSamCompatible = 2;

    // Probably "John Doe" but could be something else.  I.e. The
    // display name is not necessarily the defining RDN.
    NameDisplay = 3;


    // String-ized GUID as returned by IIDFromString().
    // eg: {4fa050f0-f561-11cf-bdd9-00aa003a77b6}
    NameUniqueId = 6;

    // engineering.widget.com/software/John Doe
    NameCanonical = 7;

    // johndoe@engineering.com
    NameUserPrincipal = 8;

    // Same as NameCanonical except that rightmost '/' is
    // replaced with '\n' - even in domain-only case.
    // eg: engineering.widget.com/software\nJohn Doe
    NameCanonicalEx = 9;

    // www/srv.engineering.com/engineering.com
    NameServicePrincipal = 10;

    // DNS domain name + SAM username
    // eg: engineering.widget.com\JohnDoe
    NameDnsDomain = 12;
//  );

type
  TIADs = class(TObject)
  private
    FADsPath: string;
  protected
    FLastFillResult: Boolean;
  public
    constructor Create; virtual;

    function Fill(AIADs: IADs): Boolean; virtual;
    procedure Clear; virtual;
    function ToString: string; {$IFDEF DELPHI2009_UP} override {$ELSE} virtual {$ENDIF};

    property ADsPath: string read FADsPath;
    property LastFillResult: Boolean read FLastFillResult;
  end;

  TIADsDomain = class(TIADs)
  private
    FMaxPasswordAge: Integer;
  public
    procedure Clear; override;
    function Fill(AIADs: IADs): Boolean; override;
    function FillFromADsPath(AADsPath: string): Boolean;
    function ToString: string; override;

    property MaxPasswordAge: Integer read FMaxPasswordAge;
  end;

  TIADsGroup = class(TIADs)
  private
    FName: string;
    FDescription: string;
    FGroupName: string;
  public
    procedure Clear; override;
    function Fill(AIADs: IADs): Boolean; override;
    function ToString: string; override;

    property GroupName: string read FGroupName;
    property Name: string read FName;
    property Description: string read FDescription;
  end;

  TIADsGroups = class(TObject)
  private
    FGroups: TStringList;
    function GetGroup(AGroupName: string): TIADsGroup;
    procedure AddGroup(AIADsGroup: IADsGroup);
    function GetCount: Integer;
    function GetEmpty: Boolean;
  public
    constructor Create;
    destructor Destroy; override;

    function ToString: string; {$IFDEF DELPHI2009_UP} override; {$ENDIF}
    procedure Clear;
    function Exists(AGroupName: string): Boolean;
    procedure Fill(AIADsMembers: IADsMembers);
    property Group[AGroupName: string]: TIADsGroup read GetGroup; default;
    property Count: Integer read GetCount;
    property Empty: Boolean read GetEmpty;
  end;

  TIADsComputer = class(TIADs)
  private
    FLocation: string;
    FStorageCapacity: string;
    FSite: string;
    FPrimaryUser: string;
    FDepartment: string;
    FOwner: string;
    FModel: string;
    FOperatingSystem: string;
    FNetAddresses: TStrings;
    FDivision: string;
    FRole: string;
    FOperatingSystemVersion: string;
    FDescription: string;
    FProcessor: string;
    FComputerID: string;
    FMemorySize: string;
    FProcessorCount: string;
  public
    constructor Create; override;
    destructor Destroy; override;

    procedure Clear; override;
    function Fill(AIADs: IADs): Boolean; override;
    function FillFromADsPath(AADsPath: string): Boolean;
    function ToString: string; override;

    property ComputerID: string read FComputerID;
    property Department: string read FDepartment;
    property Description: string read FDescription;
    property Division: string read FDivision;
    property Location: string read FLocation;
    property MemorySize: string read FMemorySize;
    property Model: string read FModel;
    property NetAddresses: TStrings read FNetAddresses;
    property OperatingSystem: string read FOperatingSystem;
    property OperatingSystemVersion: string read FOperatingSystemVersion;
    property Owner: string read FOwner;
    property PrimaryUser: string read FPrimaryUser;
    property Processor: string read FProcessor;
    property ProcessorCount: string read FProcessorCount;
    property Role: string read FRole;
    property Site: string read FSite;
    property StorageCapacity: string read FStorageCapacity;
  end;

  TUserNameEx = class(TObject)
  private
    FNameDisplay: string;
    FNameUserPrincipal: string;
    FNameFullyQualifiedDN: string;
    FNameUnknown: string;
    FNameDnsDomain: string;
    FNameCanonicalEx: string;
    FNameCanonical: string;
    FNameSamCompatible: string;
    FNameServicePrincipal: string;
    FNameUniqueId: string;
  public
    procedure Clear;
    procedure Fill;

    function ToString: string; {$IFDEF DELPHI2009_UP} override; {$ENDIF}

    property NameUnknown: string read FNameUnknown;
    property NameFullyQualifiedDN: string read FNameFullyQualifiedDN;
    property NameSamCompatible: string read FNameSamCompatible;
    property NameDisplay: string read FNameDisplay;
    property NameUniqueId: string read FNameUniqueId;
    property NameCanonical: string read FNameCanonical;
    property NameUserPrincipal: string read FNameUserPrincipal;
    property NameCanonicalEx: string read FNameCanonicalEx;
    property NameServicePrincipal: string read FNameServicePrincipal;
    property NameDnsDomain: string read FNameDnsDomain;
  end;
  
  TIADsUser = class(TIADs)
  private
    FTelephoneNumber: string;
    FPostalCodes: string;
    FPostalAddresses: TStrings;
    FNameSuffix: string;
    FGraceLoginsRemaining: Integer;
    FTelephonePager: string;
    FIsAccountLocked: Boolean;
    FBadLoginCount: Integer;
    FRequireUniquePassword: Boolean;
    FGraceLoginsAllowed: Integer;
    FLastName: string;
    FTelephoneHome: string;
    FDepartment: string;
    FPasswordRequired: Boolean;
    FOtherName: string;
    FLoginScript: string;
    FOfficeLocations: string;
    FDivision: string;
    FPasswordMinimumLength: Integer;
    FManager: string;
    FLoginWorkstations: TStrings;
    FTitle: string;
    FBadLoginAddress: string;
    FTelephoneMobile: string;
    FMaxLogins: Integer;
    FLastLogoff: TDateTime;
    FEmployeeID: string;
    FDescription: string;
    FProfile: string;
    FFullName: string;
    FAccountDisabled: Boolean;
    FMaxStorage: Integer;
    FLastFailedLogin: TDateTime;
    FHomePage: string;
    FPasswordLastChanged: TDateTime;
    FLanguages: TStrings;
    FFaxNumber: string;
    FPasswordExpirationDate: TDateTime;
    FFirstName: string;
    FNamePrefix: string;
    FLastLogin: TDateTime;
    FEmailAddress: string;
    FHomeDirectory: string;
    FAccountExpirationDate: TDateTime;
    FSAMAccountName: string;
    FEmployeeNumber: string;
//    FGroups: TIADsGroups;
    FUserPrincipalName: string;
    FPasswordNeverExpires: Boolean;
    function GetAccountExpired: Boolean;
  public
    constructor Create; override;
    destructor Destroy; override;

    procedure Clear; override;
    function Fill(AIADs: IADs): Boolean; override;
    function FillFromADsPath(AADsPath: string): Boolean;
    function ToString: string; override;

    property AccountDisabled: Boolean read FAccountDisabled;
    property AccountExpirationDate: TDateTime read FAccountExpirationDate;
    property AccountExpired: Boolean read GetAccountExpired;
    property BadLoginAddress: string read FBadLoginAddress;
    property BadLoginCount: Integer read FBadLoginCount;
    property Department: string read FDepartment;
    property Description: string read FDescription;
    property Division: string read FDivision;
    property EmailAddress: string read FEmailAddress;
    property EmployeeID: string read FEmployeeID;
    property EmployeeNumber: string read FEmployeeNumber;
    property FaxNumber: string read FFaxNumber;
    property FirstName: string read FFirstName;
    property FullName: string read FFullName;
    property GraceLoginsAllowed: Integer read FGraceLoginsAllowed;
    property GraceLoginsRemaining: Integer read FGraceLoginsRemaining;
//    property Groups: TIADsGroups read FGroups;
    property HomeDirectory: string read FHomeDirectory;
    property HomePage: string read FHomePage;
    property IsAccountLocked: Boolean read FIsAccountLocked;
    property Languages: TStrings read FLanguages;
    property LastFailedLogin: TDateTime read FLastFailedLogin;
    property LastLogin: TDateTime read FLastLogin;
    property LastLogoff: TDateTime read FLastLogoff;
    property LastName: string read FLastName;
    property LoginScript: string read FLoginScript;
    property LoginWorkstations: TStrings read FLoginWorkstations;
    property Manager: string read FManager;
    property MaxLogins: Integer read FMaxLogins;
    property MaxStorage: Integer read FMaxStorage;
    property NamePrefix: string read FNamePrefix;
    property NameSuffix: string read FNameSuffix;
    property OfficeLocations: string read FOfficeLocations;
    property OtherName: string read FOtherName;
    property PasswordExpirationDate: TDateTime read FPasswordExpirationDate;
    property PasswordLastChanged: TDateTime read FPasswordLastChanged;
    property PasswordMinimumLength: Integer read FPasswordMinimumLength;
    property PasswordNeverExpires: Boolean read FPasswordNeverExpires;
    property PasswordRequired: Boolean read FPasswordRequired;
//    property Picture: {octetstring} read FPicture;
    property PostalAddresses: TStrings read FPostalAddresses;
    property PostalCodes: string read FPostalCodes;
    property Profile: string read FProfile;
    property RequireUniquePassword: Boolean read FRequireUniquePassword;
    property SAMAccountName: string read FSAMAccountName;
    property TelephoneHome: string read FTelephoneHome;
    property TelephoneMobile: string read FTelephoneMobile;
    property TelephoneNumber: string read FTelephoneNumber;
    property TelephonePager: string read FTelephonePager;
    property Title: string read FTitle;
    property UserPrincipalName: string read FUserPrincipalName;
  end;

  TIADsADSystemInfo = class(TObject)
  private
    FPDCRoleOwner: string;
    FSiteName: string;
    FComputerName: string;
    FDomainDNSName: string;
    FForestDNSName: string;
    FIsNativeMode: Boolean;
    FSchemaRoleOwner: string;
    FDomainShortName: string;
    FUserName: string;
    FLastFillResult: Boolean;
  public
    procedure Clear;
    function Fill: Boolean;
    function ToString: string; {$IFDEF DELPHI2009_UP} override; {$ENDIF}

    property UserName: string read FUserName;
    property ComputerName: string read FComputerName;
    property SiteName: string read FSiteName;
    property DomainShortName: string read FDomainShortName;
    property DomainDNSName: string read FDomainDNSName;
    property ForestDNSName: string read FForestDNSName;
    property PDCRoleOwner: string read FPDCRoleOwner;
    property SchemaRoleOwner: string read FSchemaRoleOwner;
    property IsNativeMode: Boolean read FIsNativeMode;
    property LastFillResult: Boolean read FLastFillResult;
  end;

  ADException = class(Exception)
  protected
    FErrorCode: Cardinal;
    FOsApiErrorCode: Cardinal;
    FOsApiErrorMsg: string;
  public
    property ErrorCode: Cardinal read FErrorCode write FErrorCode;
    property OsApiErrorCode: Cardinal read FOsApiErrorCode write FOsApiErrorCode;
    property OsApiErrorMsg: string read FOsApiErrorMsg write FOsApiErrorMsg;
  end;

function GetUserNameEx(ANameFormat: Cardinal; out AName: string): Boolean; forward; overload;
function GetDomainName: string;
function GetUserFullyQualifiedDN(out AUserDN: string): Boolean;
function ExtractDomainControllerPart(APathName: string): string;
function GetDomainMaxPasswordAge(APathName: string): Int64;
procedure ADsGetLastErrorString(AErrorCode: Cardinal; out AProvider: string; out AErrorString: string);

procedure RaiseADException(AErrorCode: Cardinal; AErrorMsg: string;
  AOsApiErrorCode: Cardinal; AOsApiErrorMsg: string);
procedure RaiseADExceptionWithLastOsError(AErrorCode: Cardinal = 0;
  AErrorMsg: string = '');

implementation

{$I delphi_versions.inc}

uses
  Windows, ActiveX, comobj {$IFDEF DELPHI7_UP}, DateUtils, Variants {$ENDIF};


function GetUserNameEx(NameFormat: Cardinal; lpNameBuffer: PAnsiChar;//LPTSTR;
  nSize: PULONG): Boolean; stdcall; external 'Secur32' name 'GetUserNameExA'; overload;

function TStringsToString(ASrc: TStrings): string; forward;
function DateTime2Str(ASrc: TDateTime): string; forward;

{$IFDEF DELPHI5}
function Supports(const Instance: IUnknown; const IID: TGUID): Boolean;
var
  Intf: IUnknown;
begin
  Result := (Instance <> nil) and (Instance.QueryInterface(IID, Intf) = 0);
end;

function YearOf(const AValue: TDateTime): Word;
var
  LMonth, LDay: Word;
begin
  DecodeDate(AValue, Result, LMonth, LDay);
end;
{$ENDIF}


{ TIADsUser }

procedure TIADsUser.Clear;
begin
  inherited;
  FTelephoneNumber := '';
  FPostalCodes := '';
  FPostalAddresses.Clear();
  FNameSuffix := '';
  FGraceLoginsRemaining := -1;
  FTelephonePager := '';
  FIsAccountLocked := True;
  FBadLoginCount := -1;
  FRequireUniquePassword := True;
  FGraceLoginsAllowed := -1;
  FLastName := '';
  FTelephoneHome := '';
  FDepartment := '';
  FPasswordRequired := True;
  FOtherName := '';
  FLoginScript := '';
  FOfficeLocations := '';
  FDivision := '';
  FPasswordMinimumLength := -1;
  FManager := '';
  FLoginWorkstations.Clear();
  FTitle := '';
  FBadLoginAddress := '';
  FTelephoneMobile := '';
  FMaxLogins := -1;
  FLastLogoff := 0;
  FEmployeeID := '';
  FDescription := '';
  FProfile := '';
  FFullName := '';
  FAccountDisabled := False;
  FMaxStorage := -1;
  FLastFailedLogin := 0;
  FHomePage := '';
  FPasswordLastChanged := 0;
  FLanguages.Clear();
  FFaxNumber := '';
  FPasswordExpirationDate := 0;
  FFirstName := '';
  FNamePrefix := '';
  FLastLogin := 0;
  FEmailAddress := '';
  FHomeDirectory := '';
  FAccountExpirationDate := 0;
  FSAMAccountName := '';
  FEmployeeNumber := '';
  FUserPrincipalName := '';
  FPasswordNeverExpires := True;
end;

constructor TIADsUser.Create;
begin
  FPostalAddresses := TStringList.Create();
  FLoginWorkstations := TStringList.Create();
  FLanguages := TStringList.Create();
//  FGroups := TIADsGroups.Create();
  inherited;
end;

destructor TIADsUser.Destroy;
begin
//  FGroups.Free();
  FLanguages.Free();
  FLoginWorkstations.Free();
  FPostalAddresses.Free();
  inherited;
end;

function TIADsUser.Fill(AIADs: IADs): Boolean;
const
  UF_DONT_EXPIRE_PASSWD: DWORD = $10000;
var
  user: IADsUser;
  uac: DWORD;
begin
  Result := False;

  if not (inherited Fill(AIADs)) then
    Exit;

  if not Supports(AIADs, IADsUser) then
    Exit;

  user := AIADs as IADsUser;

  try
    FTelephoneNumber := user.TelephoneNumber;
  //  FTelephoneNumber := user.TelephoneNumber[0];
  except
    {do nothing}
  end;

  try
    FPostalCodes := user.PostalCodes;
//    FPostalCodes := user.PostalCodes[0];
  except
    {do nothing}
  end;

  try
    if user.PostalAddresses <> Null then
    begin
      //lowbound := VarArrayLowBound(user.PostalAddresses, 1);
      //highbound := VarArrayHighBound(user.PostalAddresses, 1);
      //for i := lowbound to highbound do
        FPostalAddresses.Add(user.PostalAddresses);//[i]);
    end;
  except
    {do nothing} 
  end;

  {try
    FNameSuffix := user.NameSuffix;
  except
    \\do nothing
  end;          }

  {try
    FGraceLoginsRemaining := user.GraceLoginsRemaining;
  except
    //do nothing
  end;        }
  
  {try
    FTelephonePager := user.TelephonePager;
    //FTelephonePager := user.TelephonePager[0];
  except
    //do nothing
  end;          }

  try
    FIsAccountLocked := user.IsAccountLocked;
  except
    {do nothing}
  end;

  try
    FBadLoginCount := user.BadLoginCount;
  except
    {do nothing}
  end;

  {try
    FRequireUniquePassword := user.RequireUniquePassword;
  except
    //do nothing
  end;          }

  {try
    FGraceLoginsAllowed := user.GraceLoginsAllowed;
  except
    //do nothing
  end;          }

  try
    FLastName := user.LastName;
  except
    {do nothing}
  end;

  {try
    FTelephoneHome := user.TelephoneHome;
    //FTelephoneHome := user.TelephoneHome[0];
  except
    //do nothing
  end;          }

  {try
    FDepartment := user.Department;
  except
    //do nothing
  end;          }

  try
    FPasswordRequired := user.PasswordRequired;
  except
    {do nothing}
  end;

  {try
    FOtherName := user.OtherName;
  except
    //do nothing
  end;          }

  {try
    FLoginScript := user.LoginScript;
  except
    //do nothing
  end;          }

  {try
    FOfficeLocations := user.OfficeLocations;
  except
    \\do nothing
  end;}

  {try
    FDivision := user.Division;
  except
    //do nothing
  end;          }
  
  {try
    FPasswordMinimumLength := user.PasswordMinimumLength;
  except
    //do nothing
  end;        }

  try
    FManager := user.Manager;
  except
    {do nothing}
  end;

  {try
    if user.LoginWorkstations <> Null then
    begin
      lowbound := VarArrayLowBound(user.LoginWorkstations, 1);
      highbound := VarArrayHighBound(user.LoginWorkstations, 1);
      for i := lowbound to highbound do
        FLoginWorkstations.Add(user.LoginWorkstations[i]);
    end;
  except
    //do nothing
  end;          }

  try
    FTitle := user.Title;
  except
    {do nothing}
  end;

  {try
    FBadLoginAddress := user.BadLoginAddress;
  except
    //do nothing
  end;          }

  {try
    FTelephoneMobile := user.TelephoneMobile;
    //FTelephoneMobile := user.TelephoneMobile[0];
  except
    //do nothing
  end;}

  {try
    FMaxLogins := user.MaxLogins;
  except
    //do nothing
  end;          }

  {try
    FLastLogoff := user.LastLogoff;
  except
    //do nothing
  end;          }

  {try
    FEmployeeID := user.EmployeeID;
  except
    //do nothing
  end;          }

  try
    FDescription := user.Description
  except
    {do nothing}
  end;

  {try
    FProfile := user.Profile;
  except
    //do nothing
  end;          }

  try
    FFullName := user.FullName;
  except
    {do nothing}
  end;

  try
    FAccountDisabled := user.AccountDisabled;
  except
    {do nothing}
  end;

  {try
    FMaxStorage := user.MaxStorage;
  except
    //do nothing
  end;          }

  try
    FLastFailedLogin := user.LastFailedLogin;
  except
    {do nothing}
  end;

  {try
    FHomePage := user.HomePage;
  except
    //do nothing
  end;          }

  try
    FPasswordLastChanged := user.PasswordLastChanged;
  except
    {do nothing}
  end;

  {try
    if user.Languages <> Null then
    begin
      lowbound := VarArrayLowBound(user.Languages, 1);
      highbound := VarArrayHighBound(user.Languages, 1);
      for i := lowbound to highbound do
        FLanguages.Add(user.Languages[i]);
    end;
  except
    //do nothing
  end;          }

  {try
    FFaxNumber := user.FaxNumber;
    //FFaxNumber := user.FaxNumber[0];
  except
    //do nothing
  end;          }

  {try
    FPasswordExpirationDate := user.PasswordExpirationDate;
  except
    //do nothing
  end;          }
  
  try
    FFirstName := user.FirstName;
  except
    {do nothing}
  end;

  {try
    FNamePrefix := user.NamePrefix;
  except
    //do nothing
  end;}

  try
    FLastLogin := user.LastLogin;
  except
    {do nothing}
  end;

  {try
    FEmailAddress := user.EmailAddress;
  except
    //do nothing
  end;          }

  {try
    FHomeDirectory := user.HomeDirectory;
  except
    //do nothing
  end;          }

  try
    FAccountExpirationDate := user.AccountExpirationDate;
  except
    {do nothing}
  end;

  try
    FSAMAccountName := user.Get('sAMAccountName');
  except
    {do nothing}
  end;

{  try
    FEmployeeNumber := user.Get('employeeNumber');
  except
    //do nothing
  end;          }

  try
    FUserPrincipalName := user.Get('userPrincipalName');
  except
    {do nothing}
  end;

  try
    uac := user.Get('userAccountControl');
    FPasswordNeverExpires := (uac and UF_DONT_EXPIRE_PASSWD) <> 0;
  except
    {do nothing}
  end;

//  try
//    FGroups.Fill(user.Groups);
//  except
//    {do nothing}
//  end;

  FLastFillResult := True;
  Result := True;
end;

function TIADsUser.FillFromADsPath(AADsPath: string): Boolean;
var
  user: IADsUser;
  ret: HRESULT;
begin
  Result := False;

  try
    ret := ADsGetObject('LDAP://'+AADsPath, IADsUser, user);
    Result := Succeeded(ret);
    if Result then
      Result := Fill(user);
  except
   {do nothing}
  end;
end;

function TIADsUser.GetAccountExpired: Boolean;
var
  year: Word;
begin
  Result := False;
  year := YearOf(AccountExpirationDate);
  if year <= 2000 then
    Exit;

  Result := Now() >= AccountExpirationDate;
end;

function TIADsUser.ToString: string;
const
  BOOL: array[Boolean] of string = ('FALSE', 'TRUE');
  SEPARATOR = ',';
begin
  Result :=
    inherited ToString+SEPARATOR+
    'TelephoneNumber='+FTelephoneNumber+SEPARATOR+
    'PostalCodes='+FPostalCodes+SEPARATOR+
    'PostalAddresses=['+TStringsToString(FPostalAddresses)+']'+SEPARATOR+
    'NameSuffix='+FNameSuffix+SEPARATOR+
    'GraceLoginsRemaining='+IntToStr(FGraceLoginsRemaining)+SEPARATOR+
    'TelephonePager='+FTelephonePager+SEPARATOR+
    'IsAccountLocked='+BOOL[FIsAccountLocked]+SEPARATOR+
    'BadLoginCount='+IntToStr(FBadLoginCount)+SEPARATOR+
    'RequireUniquePassword='+BOOL[FRequireUniquePassword]+SEPARATOR+
    'GraceLoginsAllowed='+IntToStr(FGraceLoginsAllowed)+SEPARATOR+
    'LastName='+FLastName+SEPARATOR+
    'TelephoneHome='+FTelephoneHome+SEPARATOR+
    'Department='+FDepartment+SEPARATOR+
    'PasswordRequired='+BOOL[FPasswordRequired]+SEPARATOR+
    'OtherName='+FOtherName+SEPARATOR+
    'LoginScript='+FLoginScript+SEPARATOR+
    'OfficeLocations='+FOfficeLocations+SEPARATOR+
    'Division='+FDivision+SEPARATOR+
    'PasswordMinimumLength='+IntToStr(FPasswordMinimumLength)+SEPARATOR+
    'Manager='+FManager+SEPARATOR+
    'LoginWorkstations=['+TStringsToString(FLoginWorkstations)+']'+SEPARATOR+
    'Title='+FTitle+SEPARATOR+
    'BadLoginAddress='+FBadLoginAddress+SEPARATOR+
    'TelephoneMobile='+FTelephoneMobile+SEPARATOR+
    'MaxLogins='+IntToStr(FMaxLogins)+SEPARATOR+
    'LastLogoff='+DateTime2Str(FLastLogoff)+SEPARATOR+
    'EmployeeID='+FEmployeeID+SEPARATOR+
    'Description='+FDescription+SEPARATOR+
    'Profile='+FProfile+SEPARATOR+
    'FullName='+FFullName+SEPARATOR+
    'AccountDisabled='+BOOL[FAccountDisabled]+SEPARATOR+
    'MaxStorage='+IntToStr(FMaxStorage)+SEPARATOR+
    'LastFailedLogin='+DateTime2Str(FLastFailedLogin)+SEPARATOR+
    'HomePage='+FHomePage+SEPARATOR+
    'PasswordLastChanged='+DateTime2Str(FPasswordLastChanged)+SEPARATOR+
    'Languages=['+TStringsToString(FLanguages)+']'+SEPARATOR+
    'axNumber='+FFaxNumber+SEPARATOR+
    'PasswordExpirationDate='+DateTime2Str(FPasswordExpirationDate)+SEPARATOR+
    'FirstName='+FFirstName+SEPARATOR+
    'NamePrefix='+FNamePrefix+SEPARATOR+
    'LastLogin='+DateTime2Str(FLastLogin)+SEPARATOR+
    'EmailAddress='+FEmailAddress+SEPARATOR+
    'HomeDirectory='+FHomeDirectory+SEPARATOR+
    'AccountExpirationDate='+DateTime2Str(FAccountExpirationDate)+SEPARATOR+
    'SAMAccountName='+FSAMAccountName+SEPARATOR+
    'EmployeeNumber='+FEmployeeNumber+SEPARATOR+
    'UserPrincipalName='+FUserPrincipalName;
end;

{ TIADsADSystemInfo }

procedure TIADsADSystemInfo.Clear;
begin
  FPDCRoleOwner := '';
  FSiteName := '';
  FComputerName := '';
  FDomainDNSName := '';
  FForestDNSName := '';
  FIsNativeMode := True;
  FSchemaRoleOwner := '';
  FDomainShortName := '';
  FUserName := '';
  FLastFillResult := False;
end;

function TIADsADSystemInfo.Fill: Boolean;
var
  ret: HRESULT;
  sysinfo: IADsADSystemInfo;
begin
  Clear();

  ret := CoCreateInstance(CLASS_ADSystemInfo, nil, CLSCTX_INPROC_SERVER or
    CLSCTX_LOCAL_SERVER, IADsADSystemInfo, sysinfo);

  Result := Succeeded(ret);
  FLastFillResult := Result;
  if not Result then
    Exit;

  try
    FPDCRoleOwner := sysinfo.PDCRoleOwner;
  except
    {do nothing}
  end;

  try
    FSiteName := sysinfo.SiteName;
  except
    {do nothing}
  end;

  try
    FComputerName := sysinfo.ComputerName;
  except
    {do nothing}
  end;

  try
    FDomainDNSName := sysinfo.DomainDNSName;
  except
    {do nothing}
  end;

  try
    FForestDNSName := sysinfo.ForestDNSName;
  except
    {do nothing}
  end;

  try
    FIsNativeMode := sysinfo.IsNativeMode;
  except
    {do nothing}
  end;

  try
    FSchemaRoleOwner := sysinfo.SchemaRoleOwner;
  except
    {do nothing}
  end;

  try
    FDomainShortName := sysinfo.DomainShortName;
  except
    {do nothing}
  end;

  try
    FUserName := sysinfo.UserName;
  except
    {do nothing}
  end;
end;

function TIADsADSystemInfo.ToString: string;
const
  BOOL: array[Boolean] of string = ('FALSE', 'TRUE');
  SEPARATOR = ',';
begin
  Result :=
    'PDCRoleOwner='+FPDCRoleOwner+SEPARATOR+
    'SiteName='+FSiteName+SEPARATOR+
    'ComputerName='+FComputerName+SEPARATOR+
    'DomainDNSName='+FDomainDNSName+SEPARATOR+
    'ForestDNSName='+FForestDNSName+SEPARATOR+
    'IsNativeMode='+BOOL[FIsNativeMode]+SEPARATOR+
    'SchemaRoleOwner='+FSchemaRoleOwner+SEPARATOR+
    'DomainShortName='+FDomainShortName+SEPARATOR+
    'UserName='+FUserName;
end;

function GetUserNameEx(ANameFormat: Cardinal; out AName: string): Boolean;
var
  len: Integer;
  tmp: ansistring;
begin
  SetLength(tmp, 4096);
  len := Length(tmp);
  Result := GetUserNameEx(ANameFormat, PAnsiChar(tmp), @len);
  if Result then
    AName := Copy(string(tmp), 1, len);
end;

{ TUserNameEx }

procedure TUserNameEx.Clear;
begin
  FNameDisplay := '';
  FNameUserPrincipal := '';
  FNameFullyQualifiedDN := '';
  FNameUnknown := '';
  FNameDnsDomain := '';
  FNameCanonicalEx := '';
  FNameCanonical := '';
  FNameSamCompatible := '';
  FNameServicePrincipal := '';
  FNameUniqueId := '';
end;

procedure TUserNameEx.Fill;
var
  tmp: string;
begin
  Clear();

//  if GetUserNameEx(LoginInfo.NameUnknown, tmp) then
//    FNameUnknown := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameFullyQualifiedDN, tmp) then
    FNameFullyQualifiedDN := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameSamCompatible, tmp) then
    FNameSamCompatible := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameDisplay, tmp) then
    FNameDisplay := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameUniqueId, tmp) then
    FNameUniqueId := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameCanonical, tmp) then
    FNameCanonical := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameUserPrincipal, tmp) then
    FNameUserPrincipal := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameCanonicalEx, tmp) then
    FNameCanonicalEx := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameServicePrincipal, tmp) then
    FNameServicePrincipal := tmp;

  if GetUserNameEx(ActiveDirectoryHelpers.NameDnsDomain, tmp) then
    FNameDnsDomain := tmp;
end;

function TUserNameEx.ToString: string;
const
  SEPARATOR = ',';
begin
  Result :=
    'NameDisplay='+FNameDisplay+SEPARATOR+
    'NameUserPrincipal='+FNameUserPrincipal+SEPARATOR+
    'NameFullyQualifiedDN='+FNameFullyQualifiedDN+SEPARATOR+
    'NameUnknown='+FNameUnknown+SEPARATOR+
    'NameDnsDomain='+FNameDnsDomain+SEPARATOR+
    'NameCanonicalEx='+FNameCanonicalEx+SEPARATOR+
    'NameCanonical='+FNameCanonical+SEPARATOR+
    'NameSamCompatible='+FNameSamCompatible+SEPARATOR+
    'NameServicePrincipal='+FNameServicePrincipal+SEPARATOR+
    'NameUniqueId='+FNameUniqueId;
end;

function TStringsToString(ASrc: TStrings): string;
var
  i: Integer;
begin
  Result := '';
  if not Assigned(ASrc) or (ASrc.Count = 0) then
    Exit;

  for i := 0 to ASrc.Count - 2 do
    Result := Result+ASrc[i]+';';

  Result := Result+ASrc[ASrc.Count-1];
end;

{ TIADsComputer }

procedure TIADsComputer.Clear;
begin
  inherited;
  FLocation := '';
  FStorageCapacity := '';
  FSite := '';
  FPrimaryUser := '';
  FDepartment := '';
  FOwner := '';
  FModel := '';
  FOperatingSystem := '';
  FNetAddresses.Clear();
  FDivision := '';
  FRole := '';
  FOperatingSystemVersion := '';
  FDescription := '';
  FProcessor := '';
  FComputerID := '';
  FMemorySize := '';
  FProcessorCount := '';
end;

constructor TIADsComputer.Create;
begin
  FNetAddresses := TStringList.Create();
  inherited;
end;

destructor TIADsComputer.Destroy;
begin
  FNetAddresses.Free();
  inherited;
end;

function TIADsComputer.Fill(AIADs: IADs): Boolean;
var
  i: Integer;
  lowbound: Integer;
  highbound: Integer;
  computer: IADsComputer;
begin
  Result := False;
  if not (inherited Fill(AIADs)) then
    Exit;

  if not Supports(AIADs, IADsComputer) then
    Exit;

  computer := AIADs as IADsComputer;
  
  try
    FComputerID := computer.ComputerID;
  except
    {do nothing}
  end;

  try
    FDepartment := computer.Department;
  except
    {do nothing}
  end;

  try
    FDescription := computer.Description;
  except
    {do nothing}
  end;

  try
    FDivision := computer.Division;
  except
    {do nothing}
  end;

  try
    FLocation := computer.Location;
  except
    {do nothing}
  end;

  try
    FMemorySize := computer.MemorySize;
  except
    {do nothing}
  end;

  try
    FModel := computer.Model;
  except
    {do nothing}
  end;

  try
    lowbound := VarArrayLowBound(computer.NetAddresses, 1);
    highbound := VarArrayHighBound(computer.NetAddresses, 1);
    for i := lowbound to highbound do
      FNetAddresses.Add(computer.NetAddresses[i]);
  except
    {do nothing}
  end;

  try
    FOperatingSystem := computer.OperatingSystem;
  except
    {do nothing}
  end;

  try
    FOperatingSystemVersion := computer.OperatingSystemVersion;
  except
    {do nothing}
  end;

  try
    FOwner := computer.Owner;
  except
    {do nothing}
  end;

  try
    FPrimaryUser := computer.PrimaryUser;
  except
    {do nothing}
  end;

  try
    FProcessor := computer.Processor;
  except
    {do nothing}
  end;

  try
    FProcessorCount := computer.ProcessorCount;
  except
    {do nothing}
  end;

  try
    FRole := computer.Role;
  except
    {do nothing}
  end;

  try
    FSite := computer.Site;
  except
    {do nothing}
  end;

  try
    FStorageCapacity := computer.StorageCapacity;
  except
    {do nothing}
  end;

  FLastFillResult := True;
  Result := FLastFillResult;
end;

function TIADsComputer.FillFromADsPath(AADsPath: string): Boolean;
var
  ret: HRESULT;
  computer: IADsComputer;
begin
  Result := False;
  Clear();
  try
    ret := ADsGetObject(WideString('LDAP://'+AADsPath), IID_IADsComputer, computer);
    Result := Succeeded(ret);
    if Result then
      Result := Fill(computer);
  except
    {do nothing}
  end;
end;

function TIADsComputer.ToString: string;
const
  SEPARATOR = ',';
begin
  Result :=
    inherited ToString+SEPARATOR+
    'Location='+FLocation+SEPARATOR+
    'StorageCapacity='+FStorageCapacity+SEPARATOR+
    'Site='+FSite+SEPARATOR+
    'PrimaryUser='+FPrimaryUser+SEPARATOR+
    'Department='+FDepartment+SEPARATOR+
    'Owner='+FOwner+SEPARATOR+
    'Model='+FModel+SEPARATOR+
    'OperatingSystem='+FOperatingSystem+SEPARATOR+
    'NetAddresses='+TStringsToString(FNetAddresses)+
    'Division='+FDivision+SEPARATOR+
    'Role='+FRole+SEPARATOR+
    'OperatingSystemVersion='+FOperatingSystemVersion+SEPARATOR+
    'Description='+FDescription+SEPARATOR+
    'Processor='+FProcessor+SEPARATOR+
    'ComputerID='+FComputerID+SEPARATOR+
    'MemorySize='+FMemorySize+SEPARATOR+
    'ProcessorCount='+FProcessorCount;
end;

{ TIADsGroup }

procedure TIADsGroup.Clear;
begin
  inherited Clear();
  FName := '';
  FDescription := '';
  FGroupName := '';
end;

function TIADsGroup.Fill(AIADs: IADs): Boolean;
var
  p: Integer;
  grp: IADsGroup;
begin
  Result := False;

  if not (inherited Fill(AIADs)) then
    Exit;

  if not Supports(AIADs, IADsGroup) then
    Exit;

  grp := AIADs as IADsGroup;

  try
    FName := grp.Name;
  except
    {do nothing}
  end;

  p := Pos('=', FName);
  FGroupName := Copy(FName, p+1, Length(FName));

  try
    FDescription := grp.Description;
  except
    {do nothing}
  end;

  FLastFillResult := True;
  Result := FLastFillResult;
end;

function TIADsGroup.ToString: string;
const
  SEPARATOR = ',';
begin
  Result :=
    inherited ToString+SEPARATOR+
    'Name='+FName+SEPARATOR+
    'GroupName='+FGroupName+SEPARATOR+
    'Description='+FDescription;
end;

{ TIADsGroups }

procedure TIADsGroups.Clear;
var
  i: Integer;
begin
  for i := 0 to FGroups.Count - 1 do
    FGroups.Objects[i].Free();
  FGroups.Clear();
end;

constructor TIADsGroups.Create;
begin
  FGroups := TStringList.Create();
end;

destructor TIADsGroups.Destroy;
begin
  Clear();
  FGroups.Free();
  inherited;
end;

function TIADsGroups.Exists(AGroupName: string): Boolean;
begin
  Result := Assigned(Group[AGroupName]);
end;

procedure TIADsGroups.Fill(AIADsMembers: IADsMembers);
var
  e: IEnumVARIANT;
  hr: Integer;
  varArr: OleVariant;
  elementFetched: Cardinal;
  obj: IADs;
begin
  e := AIADsMembers._NewEnum as IEnumVariant;

  while True do
  begin
    hr := e.Next(1, varArr, elementFetched);
    if hr = S_FALSE then
      Break;
    if elementFetched <> 1 then
      Continue;

    obj := IDispatch(varArr) as IADs;
    if Supports(obj, IADsGroup) then
      AddGroup(obj as IADSGroup);
  end;
end;

procedure TIADsGroups.AddGroup(AIADsGroup: IADsGroup);
var
  grp: TIADsGroup;
  idx: Integer;
  grpname: string;
begin
  if not Assigned(AIADsGroup) then
    Exit;
    
  grp := TIADsGroup.Create();
  grp.Fill(AIADsGroup);

  grpname := UpperCase(grp.GroupName);

  idx := FGroups.IndexOf(grpname);
  if idx = -1 then
    FGroups.AddObject(grpname, grp)
  else begin
    FGroups.Objects[idx].Free();
    FGroups.Objects[idx] := grp;
  end;
end;

function TIADsGroups.GetCount: Integer;
begin
  Result := FGroups.Count;
end;

function TIADsGroups.GetEmpty: Boolean;
begin
  Result := Count = 0;
end;

function TIADsGroups.GetGroup(AGroupName: string): TIADsGroup;
var
  index: Integer;
begin
  AGroupName := UpperCase(AGroupName);

  index := FGroups.IndexOf(AGroupName);
  if index = -1 then
    Result := nil
  else
    Result := FGroups.Objects[index] as TIADsGroup;
end;

function TIADsGroups.ToString: string;
var
  i: Integer;
  tmp: string;
begin
  Result := '';
  if Empty then
    Exit;
    
  for i := 0 to FGroups.Count - 2 do
  begin
    tmp := (FGroups.Objects[i] as TIADsGroup).ToString();
    Result := Result+'['+tmp+'],';
  end;

  tmp := (FGroups.Objects[FGroups.Count-1] as TIADsGroup).ToString();
  Result := Result+'['+tmp+']';
end;

{ TIADs }

procedure TIADs.Clear;
begin
  FADsPath := '';
  FLastFillResult := False;
end;

constructor TIADs.Create;
begin
  Clear();
end;

function TIADs.Fill(AIADs: IADs): Boolean;
begin
  Result := False;

  Clear();

  if not Assigned(AIADs) then
    Exit;

  try
    FADsPath := AIADs.ADsPath;
  except
    {do nothing}
  end;

  FLastFillResult := True;
  Result := FLastFillResult;
end;

function TIADs.ToString: string;
begin
  Result :=
    'ADsPath='+FADsPath;
end;

function DateTime2Str(ASrc: TDateTime): string;
begin
  DateTimeToString(Result, 'dd-mm-yyyy hh:mm:ss:zzz', ASrc);
end;

{ TIADsDomain }

procedure TIADsDomain.Clear;
begin
  inherited;
  FMaxPasswordAge := -1;
end;

function TIADsDomain.Fill(AIADs: IADs): Boolean;
var
  dmn: IADsDomain;
begin
  Result := False;

  if not (inherited Fill(AIADs)) then
    Exit;

  if not Supports(AIADs, IADsDomain) then
    Exit;

  dmn := AIADs as IADsDomain;

  try
    FMaxPasswordAge := dmn.MaxPasswordAge;
  except
    {do nothing};
  end;

  FLastFillResult := True;
  Result := FLastFillResult;
end;

function TIADsDomain.FillFromADsPath(AADsPath: string): Boolean;
var
  domain: IADsDomain;
  ret: HRESULT;
begin
  Result := False;

  try
    ret := ADsGetObject(WideString('LDAP://'+AADsPath), IADsDomain, domain);
    Result := Succeeded(ret);
    if Result then
      Result := Fill(domain);
  except

   {do nothing}
  end;
end;

function TIADsDomain.ToString: string;
begin
  Result := ''+
    'MaxPasswordAge='+IntToStr(FMaxPasswordAge);
end;

function GetUserFullyQualifiedDN(out AUserDN: string): Boolean;
begin
  Result := GetUserNameEx(ActiveDirectoryHelpers.NameFullyQualifiedDN, AUserDN);
end;

function ExtractDomainControllerPart(APathName: string): string;
var
  p: Integer;
begin
  p := Pos('DC=', UpperCase(APathName));
  if p = 0 then
    Result := ''
  else
    Result := Copy(APathName, p, Length(APathName));
end;

function GetDomainMaxPasswordAge(APathName: string): Int64;
const
  _100_NANOSECONDS_IN_SECOND = 100/1000000000;
var
  ns: Int64;
  ret: HRESULT;
  ads: IADs;
  mpa: IADsLargeInteger;
begin
  Result := -1;

  try
    ret := ADsGetObject(WideString('LDAP://'+APathName), IADs, ads);
    if Succeeded(ret) then
    begin
      mpa := IDispatch(ads.Get('maxPwdAge')) as IADsLargeInteger;
      if mpa.LowPart > 0 then
      begin
        ns := (Int64(mpa.HighPart) shl 32) + mpa.LowPart;
        Result := Round(Abs(ns*_100_NANOSECONDS_IN_SECOND));
      end;
    end;
  except
   {do nothing}
  end;
end;

function NetGetJoinInformation(lpServer: Pointer; lpNameBuffer: PAnsiChar; var BufferType: Cardinal): DWORD; stdcall; external 'Netapi32' name 'NetGetJoinInformation';
function NetApiBufferFree(ABuffer: Pointer): Cardinal; stdcall; external 'Netapi32';

function GetDomainName: string;
var
  nb: PWideChar;
  bt: Cardinal;
  ret: Cardinal;
begin
  Result := '';
  ret := NetGetJoinInformation(nil, @nb, bt);
  if ret = 0 then
  begin
    if bt = 3 then
      Result := nb;
    NetApiBufferFree(nb);
  end
  else
    RaiseADException(0, 'GetDomainName', ret, '');
end;

procedure ADsGetLastErrorString(AErrorCode: Cardinal; out AProvider: string;
  out AErrorString: string);
var
  errstr: widestring;
  namebuf: widestring;
begin
  SetLength(errstr, 4096);
  SetLength(namebuf, 4096);
  ADsGetLastError(AErrorCode, PWideChar(errstr), Length(errstr),
    PWideChar(namebuf), Length(namebuf));

end;

procedure RaiseADException(AErrorCode: Cardinal; AErrorMsg: string;
  AOsApiErrorCode: Cardinal; AOsApiErrorMsg: string);
var
  e: ADException;
begin
  e := ADException.Create(AErrorMsg);
  e.ErrorCode := AErrorCode;
  e.OsApiErrorCode := AOsApiErrorCode;
  e.OsApiErrorMsg := AOsApiErrorMsg;
  raise e;
end;

procedure RaiseADExceptionWithLastOsError(AErrorCode: Cardinal = 0;
  AErrorMsg: string = '');
var
  err: Cardinal;
begin
  err := GetLastError();
  RaiseADException(AErrorCode, AErrorMsg, err, SysErrorMessage(err));
end;

end.
