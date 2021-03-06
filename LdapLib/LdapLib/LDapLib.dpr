program LDapLib;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.SysUtils,
  System.IOUtils,
  Ora,
  ActiveDirectoryHelpers in 'DHL_Logon\ActiveDirectoryHelpers.pas',
  ActiveDs_TLB in 'DHL_Logon\ActiveDs_TLB.pas',
  ADSHLP in 'DHL_Logon\ADSHLP.PAS',
  DHLLogon in 'DHL_Logon\DHLLogon.pas',
  DMCEmulation in 'DHL_Logon\DMCEmulation.pas',
  FileVersion in 'DHL_Logon\FileVersion.pas',
  UserSapParam in 'DHL_Logon\UserSapParam.pas',
  AppParams in 'AppParams.pas',
  uLkJSON in 'uLkJSON.pas';

var
  connectStringAppLogowanie,
  connectStringCasOwner,
  appName,
  appVersion,
  login,
  password : string;
  testVersion : string;
  oraSession : TOraSession;
  logResult : TDhlLoginResult;
  errStr : string;
  appPars : AppParams.TAppParams;

  connectStringUrl : string;
  appId : string;

  casOwnerItems : TArray<string>;
  serverName : string;


begin
  logResult := dlrError;
  try
    if ParamCount() = 0 then begin
       if Assigned(oraSession) then begin
          if oraSession.Connected then begin
             LogResult := TDhlLoginResult(DhlLogOut());
             oraSession.Connected := false;
             oraSession.Free();
          end;
       end;
    end else begin
       testVersion := ParamStr(1);
       appName := ParamStr(2);
       appVersion := ParamStr(3);
       connectStringUrl := paramStr(4);
       appId := paramStr(5);
       login := paramStr(6);
       password := paramStr(7);
//       TFile.WriteAllText('pars.txt',
//           'testVersion=' + testVersion + #13#10 +
//           'appName=' + appName + #13#10 +
//           'appVersion=' + appVersion + #13#10 +
//           'connectStringUrl=' + connectStringUrl + #13#10 +
//           'appId=' + appId + #13#10 +
//           'login=' + login + #13#10 +
//           'password=' + password + #13#10);

       appPars := AppParams.TAppParams.getParams(connectStringUrl, appId);
        if appPars.Code <> 0 then begin
            writeln('ERROR' + '&'  +
                    'B??d pobierania parametr?w aplikacji - ' +
                    appPars.Code.ToString()  + ' - ' +
                    appPars.exceptionText +

                    '&' )
        end else begin
            if testVersion = 'Y' then begin
              connectStringCasOwner := appPars.ConnStrCasOwnerM1TST;
              connectStringAppLogowanie := appPars.ConnStrAppLogowanieSDS;
              serverName := 'czcholsint2548';
            end else begin
              connectStringCasOwner := appPars.ConnStrCasOwnerM1;
              connectStringAppLogowanie := appPars.ConnStrAppLogowanieSPS;
              serverName := 'spsp-scan';
            end;

            casOwnerItems := connectStringCasOwner.Split(['/', '@']);
            if casOwnerItems[2] = 'M1' then
               casOwnerItems[2] := 'M1P';


            connectStringCasOwner := 'jdbc:oracle:thin:@//' + serverName + '.prg-dc.dhl.com:1521/' +
                                     casOwnerItems[2] + ';' +
                                     casOwnerItems[0] + ';' +
                                     casOwnerItems[1];

            oraSession := TOraSession.Create(nil);
            oraSession.ConnectPrompt := False;
            oraSession.ConnectString := connectStringAppLogowanie;
            oraSession.Options.Net := False;
            oraSession.AutoCommit := True;
            oraSession.Connected := True;
            errStr := '';
            logResult := DhlLogin(appName, strToIntDef(appVersion, 999), oraSession, login, password, errStr);
            if errStr <> '' then
                writeln('ERROR' + '&'  +
                        errStr + '&' )
            else if logResult = dlrOK then begin
                writeln('OK' + '&' +
                        g_UserParam.ApplicationOpLevel.ToString() + '&' +
                        g_UserParam.PrivilegesLevel + '&'  +
                        g_UserParam.UserTerminal + '&' +
                        connectStringCasOwner + '&');
            end else begin
                writeln('ERROR' + '&'  +
                        errStr + '&' );
            end;
        end;
    end;


  except
    on E: Exception do
      Writeln(E.ClassName, ': ', E.Message);
  end;
end.
