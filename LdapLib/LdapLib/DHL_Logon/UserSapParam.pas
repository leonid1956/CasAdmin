unit UserSapParam;

interface

type
  TLocation = class(TObject)
  private
    FName: string;
    FStreet: string;
    FPostalCode: string;
    FProvince: string;
    FStreetNumber: string;
    FCity: string;
    FId: string;
  public
    constructor Create;
    procedure Clear;
    function GetDataFromDatabase(ALocationId: string): Boolean;
    property Id: string read FId;
    property Name: string read FName;
    property PostalCode: string read FPostalCode;
    property City: string read FCity;
    property Street: string read FStreet;
    property StreetNumber: string read FStreetNumber;
    property Province: string read FProvince;
  end;

  TUserSapParam = class(TObject)
  private
    FLastname: string;
    FSapId: Integer;
    FFirstname: string;
    FLocation: TLocation;
    FSectionId: Integer;
    FBossSapId: Integer;
    FDepartment: string;
    FSection: string;
    FDepartmentId: Integer;
    FFaxCountryCode: string;
    FBirthDate: TDateTime;
    FPhoneNumber: string;
    FEmail: string;
    FFaxCityCode: string;
    FPhoneInternal: string;
    FPhoneCountryCode: string;
    FJob: string;
    FPhoneCityCode: string;
    FPesel: string;
    FMotherLastname: string;
    FJobId: Integer;
    FMobileNumber: string;
    FFaxNumber: string;
    FBirthPlace: string;
    FNip: string;
    FEmploymentDate: TDateTime;
  public
    constructor Create;
    destructor Destroy; override;
    procedure Clear;
    function GetDataFromDatabase(AUserSapId: Integer): Boolean;
    property SapId: Integer read FSapId;
    property Firstname: string read FFirstname;
    property Lastname: string read FLastname;
    property BossSapId: Integer read FBossSapId;
    property Department: string read FDepartment;
    property DepartmentId: Integer read FDepartmentId;
    property Location: TLocation read FLocation;
    property Section: string read FSection;
    property SectionId: Integer read FSectionId;
    property EmploymentDate: TDateTime read FEmploymentDate;
    property MotherLastname: string read FMotherLastname;
    property BirthDate: TDateTime read FBirthDate;
    property BirthPlace: string read FBirthPlace;
    property Pesel: string read FPesel;
    property Nip: string read FNip;
    property PhoneCountryCode: string read FPhoneCountryCode;
    property PhoneCityCode: string read FPhoneCityCode;
    property PhoneNumber: string read FPhoneNumber;
    property PhoneInternal: string read FPhoneInternal;
    property FaxCountryCode: string read FFaxCountryCode;
    property FaxCityCode: string read FFaxCityCode;
    property FaxNumber: string read FFaxNumber;
    property MobileNumber: string read FMobileNumber;
    property Email: string read FEmail;
    property JobId: Integer read FJobId;
    property Job: string read FJob;
  end;

implementation

uses
  Ora, SysUtils, DMCEmulation;

function GetDateTimeFromQuery(AQuery: TOraQuery; AField: string;
  ADefault: TDateTime = 0): TDateTime;
begin
  Assert(Assigned(AQuery));
  Assert(not AQuery.Eof);

  if AQuery.FieldByName(AField).IsNull then
    Result := ADefault
  else
    Result := AQuery.FieldByName(AField).AsDateTime;
end;

function GetIntegerFromQuery(AQuery: TOraQuery; AField: string;
  ADefault: Integer = 0): Integer;
begin
  Assert(Assigned(AQuery));
  Assert(not AQuery.Eof);

  if AQuery.FieldByName(AField).IsNull then
    Result := ADefault
  else
    Result := AQuery.FieldByName(AField).AsInteger;
end;

function GetStringFromQuery(AQuery: TOraQuery; AField: string;
  ADefault: string = ''): string;
begin
  Assert(Assigned(AQuery));
  Assert(not AQuery.Eof);

  if AQuery.FieldByName(AField).IsNull then
    Result := ADefault
  else
    Result := AQuery.FieldByName(AField).AsString;
end;

{ TLocation }

constructor TLocation.Create;
begin
  Clear();
end;

function TLocation.GetDataFromDatabase(ALocationId: string): Boolean;
var
  ret: Integer;
  query: TOraQuery;
begin
  Clear();
  FId := Trim(ALocationId);

  Result := False;
  try
    query := TOraQuery.Create(nil);
    try
      ret := DMC.Query(
        ['SELECT * FROM sap.locations WHERE location_id = :loc'],
        ['loc', FId], query);
      if (ret <> 0) then
        Exit;

      if query.Eof then
      begin
        Result := True;
        Exit;
      end;

      FName := GetStringFromQuery(query, 'name');
      FStreet := GetStringFromQuery(query, 'street');
      FPostalCode := GetStringFromQuery(query, 'postal_code');
      FProvince := GetStringFromQuery(query, 'province');
      FStreetNumber := GetStringFromQuery(query, 'street_number');
      FCity := GetStringFromQuery(query, 'city');

      Result := True;
    finally
      query.Free();
    end;
  except
    // nothing
  end;
end;

procedure TLocation.Clear;
begin
  FName := '';
  FStreet := '';
  FPostalCode := '';
  FProvince := '';
  FStreetNumber := '';
  FCity := '';
  FId := '';
end;

{ TUserSapParam }

constructor TUserSapParam.Create;
begin
  FLocation := TLocation.Create();
  Clear();
end;

function TUserSapParam.GetDataFromDatabase(AUserSapId: Integer): Boolean;
var
  ret: Integer;
  query: TOraQuery;
begin
  Assert(AUserSapId > 0);

  Clear();
  FSapId := AUserSapId;

  Result := False;
  try
    query := TOraQuery.Create(nil);
    try
      ret := DMC.Query(
        ['SELECT * FROM sap.v_allpersons WHERE sap_id = :sap'],
        ['sap', FSapId], query);
      if (ret <> 0) then
        Exit;

      if query.Eof then
      begin
        Result := True;
        Exit;
      end;

      FLastname := GetStringFromQuery(query, 'lastname');
      FFirstname := GetStringFromQuery(query, 'firstname');
      FMotherLastname := GetStringFromQuery(query, 'mother_lastname');

      FLocation.GetDataFromDatabase(GetStringFromQuery(query, 'location_sap_id'));

      FSectionId := GetIntegerFromQuery(query, 'section_id', -1);
      FSection := GetStringFromQuery(query, 'section');

      FBossSapId := GetIntegerFromQuery(query, 'boss_sap_id', -1);

      FDepartment := GetStringFromQuery(query, 'department');
      FDepartmentId := GetIntegerFromQuery(query, 'department_id', -1);

      FFaxCountryCode := GetStringFromQuery(query, 'fax_country_code');
      FFaxNumber := GetStringFromQuery(query, 'fax_number');
      FFaxCityCode := GetStringFromQuery(query, 'fax_city_code');

      FBirthPlace := GetStringFromQuery(query, 'birth_place');
      FBirthDate := GetDateTimeFromQuery(query, 'birth_date');

      FEmail := GetStringFromQuery(query, 'email');

      FPhoneInternal := GetStringFromQuery(query, 'phone_internal');
      FPhoneCountryCode := GetStringFromQuery(query, 'phone_country_code');
      FPhoneNumber := GetStringFromQuery(query, 'phone_number');
      FPhoneCityCode := GetStringFromQuery(query, 'phone_city_code');

      FMobileNumber := GetStringFromQuery(query, 'mobile_number');

      FJob := GetStringFromQuery(query, 'job');
      FJobId := GetIntegerFromQuery(query, 'job_id', -1);

      FPesel := GetStringFromQuery(query, 'pesel');

      FNip := GetStringFromQuery(query, 'nip');

      FEmploymentDate := GetDateTimeFromQuery(query, 'employment_date');

      Result := True;
    finally
      query.Free();
    end;
  except
    // nothing
  end;
end;

procedure TUserSapParam.Clear;
begin
  FLocation.Clear();
  FLastname := '';
  FSapId := -1;
  FFirstname := '';
  FSectionId := -1;
  FBossSapId := -1;
  FDepartment := '';
  FSection := '';
  FDepartmentId := -1;
  FFaxCountryCode := '';
  FBirthDate := 0;
  FPhoneNumber := '';
  FEmail := '';
  FFaxCityCode := '';
  FPhoneInternal := '';
  FPhoneCountryCode := '';
  FJob := '';
  FPhoneCityCode := '';
  FPesel := '';
  FMotherLastname := '';
  FJobId := -1;
  FMobileNumber := '';
  FFaxNumber := '';
  FBirthPlace := '';
  FNip := '';
  FEmploymentDate := 0;
end;

destructor TUserSapParam.Destroy;
begin
  FLocation.Free();
  inherited;
end;

end.