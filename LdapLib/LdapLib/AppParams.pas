unit AppParams;

interface
uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, ScHttp, ScSSLTypes, StdCtrls;

  type
  TAppParams = record
    public
      ConnStrAppLogowanieSDS,
      ConnStrAppLogowanieSPS,
      ConnStrCasOwnerM1,
      ConnStrCasOwnerM1TST         : string;
//      IsTestVersion             : boolean;
      Code                      : Integer;
      exceptionText : string;
      class function getParams(pConnectStringUrl : string; pAppId : string) : TAppParams; static;
  end;


implementation

uses uLkJSON;

//------------------------------------------------------------------------------
// APPL_SB/ADMIN@SDS - lista aplikacji pobrac ID     CASADMIN  529
// generator https://www.guidgenerator.com/online-guid-generator.aspx
// Wygenerowac id_APP a88ffaa6-6b9f-4b3e-9e98-d41fb737f62d
// APP_TO_APPL_MAP/LOGIN_API@SDS - wpisac APPL_ID i Wygenerowane id_APP



class function TAppParams.getParams(pConnectStringUrl : string; pAppId : string) : TAppParams;
var tmp : string;

    Request : TScHttpWebRequest;
    Response : TScHttpWebResponse;
    Buf : TBytes;
    l_js : TlkJSONbase;
    i : Integer;
    function normalize(pConnStr : string; pDb : string) : string;
    begin
       if not pConnStr.Contains('@') then
           result := pConnStr.Trim() + '@' + pDb
       else
           result := pConnStr.Trim();
    end;

begin
      result.ConnStrAppLogowanieSDS := '';
      result.ConnStrAppLogowanieSPS := '';
      result.ConnStrCasOwnerM1 := '';
      result.ConnStrCasOwnerM1TST := '';

      result.code := 0;
      result.exceptionText := '';
      buf := TEncoding.UTF8.GetBytes('{ "app_id": "' + pAppId + '" }');
      Request := TScHttpWebRequest.Create(nil);
      try
        Request.Method := rmPOST;
        Request.SSLOptions.Protocols := [spTls12];
        Request.ReadWriteTimeout := 5;
        Request.RequestUri := pConnectStringUrl;
        Request.ContentType  := 'application/json;charset=UTF-8';
        Request.Accept := 'application/json';
        Request.ContentLength := Length(Buf);
        Request.WriteBuffer(Buf);

        try
          response := Request.GetResponse;
          tmp := response.ReadAsString;

          l_js := TlkJSON.ParseText(tmp);
          for i := 0 to Pred(l_js.Count) do
            try
              if (VarToStr(l_js.Child[I].Field['db_name'].Value).ToUpper()= 'SPS')
                 and
                 (VarToStr(l_js.Child[I].Field['db_user'].Value).ToUpper() = 'APP_LOGOWANIE')
               then
                  Result.ConnStrAppLogowanieSPS :=
                     normalize(VarToStr(l_js.Child[I].Field['connection_string'].Value), 'SPS')
              else
              if (VarToStr(l_js.Child[I].Field['db_name'].Value).ToUpper()= 'SDS')
                 and
                 (VarToStr(l_js.Child[I].Field['db_user'].Value).ToUpper() = 'APP_LOGOWANIE')
               then
                  Result.ConnStrAppLogowanieSDS :=
                     normalize(VarToStr(l_js.Child[I].Field['connection_string'].Value), 'SDS')
              else
              if (VarToStr(l_js.Child[I].Field['db_name'].Value).ToUpper()= 'M1')
                 and
                 (VarToStr(l_js.Child[I].Field['db_user'].Value).ToUpper() = 'CAS_OWNER')
               then
                  Result.ConnStrCasOwnerM1 :=
                     normalize(VarToStr(l_js.Child[I].Field['connection_string'].Value), 'M1')
              else
              if (VarToStr(l_js.Child[I].Field['db_name'].Value).ToUpper()= 'M1TST')
                 and
                 (VarToStr(l_js.Child[I].Field['db_user'].Value).ToUpper() = 'CAS_OWNER')
               then
                  Result.ConnStrCasOwnerM1Tst :=
                     normalize(VarToStr(l_js.Child[I].Field['connection_string'].Value), 'M1TST');

              result.Code := 0;
            except end;
        except
          on E: HttpException do
          begin
               Result.Code := 100 + Integer(HttpException(E).StatusCode);
               result.exceptionText := e.Message;
          end;
          on e:exception do
          begin
              Result.Code := 1000;
              result.exceptionText := e.Message;
          end;
        end;

      finally
        Request.Free;
        if Assigned(Response) then
          Response.Free;
      end;

      if result.Code = 0  then begin
            if (result.ConnStrAppLogowanieSDS = '') or (result.ConnStrCasOwnerM1TST = '') then
               result.Code := 10;
            if (result.ConnStrAppLogowanieSPS = '') or (result.ConnStrCasOwnerM1 = '') then
               result.Code := 20;
      end;
end;


end.
