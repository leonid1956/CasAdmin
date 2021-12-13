unit FileVersion;

interface

type
  FileVersionNumber = record
    Major: Cardinal;
    Minor: Cardinal;
    Release: Cardinal;
    Build: Cardinal;
  end;

  FileVersionRec = record
    VersionNumber: FileVersionNumber;
    FileType: string;
    CompanyName: string;
    FileDescription: string;
    InternalName: string;
    LegalCopyRight: string;
    LegalTradeMarks: string;
    OriginalFileName: string;
    ProductName: string;
    ProductVersion: string;
    Comments: string;
    SpecialBuildStr: string;
    PrivateBuildStr: string;
    FileFunction: string;
    OracleModuleInfo: string;
    DebugBuild: Boolean;
    PreRelease: Boolean;
    SpecialBuild: Boolean;
    PrivateBuild: Boolean;
    Patched: Boolean;
    InfoInferred: Boolean;
  end;

procedure Clear(var AVersion: FileVersionNumber); overload;
function GetFileVersionNumberString4(AVersion: FileVersionNumber): string; overload;
function GetFileVersionNumberString3(AVersion: FileVersionNumber): string; overload;
function GetFileVersionNumberString2(AVersion: FileVersionNumber): string; overload;
function GetFileVersionNumberString3rc(AVersion: FileVersionNumber): string; overload;

procedure Clear(var AVersion: FileVersionRec); overload;
function GetFileVersionNumberString4(AVersion: FileVersionRec): string; overload;
function GetFileVersionNumberString3(AVersion: FileVersionRec): string; overload;
function GetFileVersionNumberString2(AVersion: FileVersionRec): string; overload;
function GetFileVersionNumberString3rc(AVersion: FileVersionRec): string; overload;

function GetFileVersionRec(out AFileVersionRec: FileVersionRec): Boolean; overload;
function GetFileVersionRec(AFilename: string;
  out AFileVersionRec: FileVersionRec): Boolean; overload;

function GetFileVersionString2: string; overload;
function GetFileVersionString2(AFilename: string): string; overload;

function GetFileVersionString3: string; overload;
function GetFileVersionString3(AFilename: string): string; overload;

function GetFileVersionString4: string; overload;
function GetFileVersionString4(AFilename: string): string; overload;

implementation

uses
  SysUtils, Windows, ShellApi;

{$I DisableWarnings.inc}

{==============================================================================}
function GetFileVersionRec(out AFileVersionRec: FileVersionRec): Boolean;
var
  size: Cardinal;
  fn: string;
  buffer: array[0..MAX_PATH-1] of Char;
begin
  Result := False;

  size := GetModuleFileName(0, buffer, Length(buffer));
  if size = 0 then Exit;
  SetString(fn, buffer, size);

  Result := GetFileVersionRec(fn, AFileVersionRec);
end;

{==============================================================================}
function GetFileVersionRec(AFilename: string;
  out AFileVersionRec: FileVersionRec): Boolean; overload;
var
  hdl, size, buflen: Cardinal;
  buf, bufvalue: Pointer;
  fixedfileinfo: PVSFixedFileInfo;
  key: string;
  shfi: TSHFileInfo;

// dwFileType, dwFileSubtype
  function GetFileSubType(FixedFileInfo: PVSFixedFileInfo): string;
  begin
    case FixedFileInfo.dwFileType of
      VFT_UNKNOWN: Result    := 'Unknown';
      VFT_APP: Result        := 'Application';
      VFT_DLL: Result        := 'DLL';
      VFT_STATIC_LIB: Result := 'Static-link Library';
      VFT_DRV:
        case
          FixedFileInfo.dwFileSubtype of
          VFT2_UNKNOWN: Result         := 'Unknown Driver';
          VFT2_DRV_COMM: Result        := 'Communications Driver';
          VFT2_DRV_PRINTER: Result     := 'Printer Driver';
          VFT2_DRV_KEYBOARD: Result    := 'Keyboard Driver';
          VFT2_DRV_LANGUAGE: Result    := 'Language Driver';
          VFT2_DRV_DISPLAY: Result     := 'Display Driver';
          VFT2_DRV_MOUSE: Result       := 'Mouse Driver';
          VFT2_DRV_NETWORK: Result     := 'Network Driver';
          VFT2_DRV_SYSTEM: Result      := 'System Driver';
          VFT2_DRV_INSTALLABLE: Result := 'InstallableDriver';
          VFT2_DRV_SOUND: Result       := 'Sound Driver';
        end;
      VFT_FONT:
        case FixedFileInfo.dwFileSubtype of
          VFT2_UNKNOWN: Result       := 'Unknown Font';
          VFT2_FONT_RASTER: Result   := 'Raster Font';
          VFT2_FONT_VECTOR: Result   := 'Vector Font';
          VFT2_FONT_TRUETYPE: Result := 'Truetype Font';
          else;
        end;
      VFT_VXD: Result := 'Virtual Defice Identifier = ' +
          IntToHex(FixedFileInfo.dwFileSubtype, 8);
    end;
  end;
  function HasdwFileFlags(FixedFileInfo: PVSFixedFileInfo; Flag: Word): Boolean;
  begin
    Result := (FixedFileInfo.dwFileFlagsMask and
      FixedFileInfo.dwFileFlags and
      Flag) = Flag;
  end;
  function GetFixedFileInfo: PVSFixedFileInfo;
  begin
    if not VerQueryValue(buf, '', Pointer(Result), buflen) then
      Result := nil
  end;
  function GetInfo(const AKey: string): string;
  begin
    Result := '';
    key := Format('\StringFileInfo\%.4x%.4x\%s',
      [LoWord(Integer(bufvalue^)), HiWord(Integer(bufvalue^)), aKey]);
    if VerQueryValue(buf, PChar(key), bufvalue, buflen) then
      Result := PWideChar(bufvalue);
  end;
  function QueryValue(const AValue: string): string;
  begin
    Result := '';
    // obtain version information about the specified file
    if VerQueryValue(buf, '\VarFileInfo\Translation', bufvalue, buflen) then
      Result := GetInfo(AValue);
  end;
begin
  Result := False;

  Clear(AFileVersionRec);

  if SHGetFileInfo(PChar(AFilename), 0, shfi, SizeOf(shfi),
    SHGFI_TYPENAME) <> 0 then
  begin
    AFileVersionRec.FileType := shfi.szTypeName;
  end;

  if 0 = SHGetFileInfo(PChar(AFilename), 0, shfi, SizeOf(shfi), SHGFI_EXETYPE) then
    Exit;

  size := GetFileVersionInfoSize(PChar(AFilename), hdl);
  if size = 0 then Exit;

  GetMem(buf, size);
  try
    if not GetFileVersionInfo(PChar(AFilename), hdl, size, buf) then Exit;
    if not VerQueryValue(buf, '\', Pointer(fixedfileinfo), buflen) then Exit;

    with AFileVersionRec do
    begin
      VersionNumber.Major := fixedfileinfo.dwFileVersionMS shr 16;
      VersionNumber.Minor := fixedfileinfo.dwFileVersionMS shl 16 shr 16;
      VersionNumber.Release := fixedfileinfo.dwFileVersionLS shr 16;
      VersionNumber.Build := fixedfileinfo.dwFileVersionLS shl 16 shr 16;

      CompanyName      := QueryValue('CompanyName');
      FileDescription  := QueryValue('FileDescription');
      InternalName     := QueryValue('InternalName');
      LegalCopyRight   := QueryValue('LegalCopyRight');
      LegalTradeMarks  := QueryValue('LegalTradeMarks');
      OriginalFileName := QueryValue('OriginalFileName');
      ProductName      := QueryValue('ProductName');
      ProductVersion   := QueryValue('ProductVersion');
      Comments         := QueryValue('Comments');
      SpecialBuildStr  := QueryValue('SpecialBuild');
      PrivateBuildStr  := QueryValue('PrivateBuild');
      OracleModuleInfo := QueryValue('OracleModuleInfo');
      // Fill the VS_FIXEDFILEINFO structure
      fixedfileinfo := GetFixedFileInfo();
      DebugBuild    := HasdwFileFlags(fixedfileinfo, VS_FF_DEBUG);
      PreRelease    := HasdwFileFlags(fixedfileinfo, VS_FF_PRERELEASE);
      PrivateBuild  := HasdwFileFlags(fixedfileinfo, VS_FF_PRIVATEBUILD);
      SpecialBuild  := HasdwFileFlags(fixedfileinfo, VS_FF_SPECIALBUILD);
      Patched       := HasdwFileFlags(fixedfileinfo, VS_FF_PATCHED);
      InfoInferred  := HasdwFileFlags(fixedfileinfo, VS_FF_INFOINFERRED);
      FileFunction  := GetFileSubType(fixedfileinfo);
    end;
    Result := True;
  finally
    FreeMem(buf);
  end;
end;

{==============================================================================}
function GetFileVersionString3: string;
var
  v: FileVersionRec;
begin
  if GetFileVersionRec(v) then
    Result := GetFileVersionNumberString3(v.VersionNumber)
  else
    Result := '';
end;

{==============================================================================}
function GetFileVersionString3(AFilename: string): string;
var
  v: FileVersionRec;
begin
  if GetFileVersionRec(AFilename, v) then
    Result := GetFileVersionNumberString3(v.VersionNumber)
  else
    Result := '';
end;

{==============================================================================}
function CDLGetFileVersionString3rc: string;
var
  v: FileVersionRec;
begin
  if GetFileVersionRec(v) then
    Result := GetFileVersionNumberString3rc(v.VersionNumber)
  else
    Result := '';
end;

{==============================================================================}
function GetFileVersionString4: string;
var
  v: FileVersionRec;
begin
  if GetFileVersionRec(v) then
    Result := GetFileVersionNumberString4(v.VersionNumber)
  else
    Result := '';
end;

{==============================================================================}
function GetFileVersionString4(AFilename: string): string;
var
  v: FileVersionRec;
begin
  if GetFileVersionRec(AFilename, v) then
    Result := GetFileVersionNumberString4(v.VersionNumber)
  else
    Result := '';
end;

{==============================================================================}
function GetFileVersionString2: string;
var
  v: FileVersionRec;
begin
  if GetFileVersionRec(v) then
    Result := GetFileVersionNumberString2(v.VersionNumber)
  else
    Result := '';
end;

{==============================================================================}
function GetFileVersionString2(AFilename: string): string;
var
  v: FileVersionRec;
begin
  if GetFileVersionRec(AFilename, v) then
    Result := GetFileVersionNumberString2(v.VersionNumber)
  else
    Result := '';
end;

{ FileVersionNumber }

{==============================================================================}
procedure Clear(var AVersion: FileVersionNumber);
begin
  AVersion.Major := 0;
  AVersion.Minor := 0;
  AVersion.Release := 0;
  AVersion.Build := 0;
end;

{==============================================================================}
function GetFileVersionNumberString2(AVersion: FileVersionNumber): string;
begin
  Result := Format('%d.%d', [AVersion.Major, AVersion.Minor]);
end;

{==============================================================================}
function GetFileVersionNumberString3(AVersion: FileVersionNumber): string;
begin
  Result := Format('%d.%d.%d', [AVersion.Major, AVersion.Minor,
    AVersion.Release]);
end;

{==============================================================================}
function GetFileVersionNumberString3rc(AVersion: FileVersionNumber): string;
begin
  if AVersion.Build = 0 then
    Result := Format('%d.%d.%d', [AVersion.Major, AVersion.Minor,
      AVersion.Release])
  else
    Result := Format('%d.%d.%drc%d', [AVersion.Major, AVersion.Minor,
      AVersion.Release, AVersion.Build]);
end;

{==============================================================================}
function GetFileVersionNumberString4(AVersion: FileVersionNumber): string;
begin
  Result := Format('%d.%d.%d.%d', [AVersion.Major, AVersion.Minor,
    AVersion.Release, AVersion.Build]);
end;

{ FileVersionRec }

{==============================================================================}
procedure Clear(var AVersion: FileVersionRec);
begin
  with AVersion do
  begin
    Clear(VersionNumber);
    FileType         := '';
    CompanyName      := '';
    FileDescription  := '';
    InternalName     := '';
    LegalCopyRight   := '';
    LegalTradeMarks  := '';
    OriginalFileName := '';
    ProductName      := '';
    ProductVersion   := '';
    Comments         := '';
    SpecialBuildStr  := '';
    PrivateBuildStr  := '';
    FileFunction     := '';
    OracleModuleInfo := '';
    DebugBuild       := False;
    Patched          := False;
    PreRelease       := False;
    SpecialBuild     := False;
    PrivateBuild     := False;
    InfoInferred     := False;
  end;
end;

{==============================================================================}
function GetFileVersionNumberString2(AVersion: FileVersionRec): string;
begin
  Result := GetFileVersionNumberString2(AVersion.VersionNumber);
end;

{==============================================================================}
function GetFileVersionNumberString3(AVersion: FileVersionRec): string;
begin
  Result := GetFileVersionNumberString3(AVersion.VersionNumber);
end;

{==============================================================================}
function GetFileVersionNumberString3rc(AVersion: FileVersionRec): string;
begin
  Result := GetFileVersionNumberString3rc(AVersion.VersionNumber);
end;

{==============================================================================}
function GetFileVersionNumberString4(AVersion: FileVersionRec): string;
begin
  Result := GetFileVersionNumberString4(AVersion.VersionNumber);
end;

{$I EnableWarnings.inc}

end.
