package casadmin;



import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 /**
 * <p>Klasa dostepu do bazy danych Oracle za pomocą <strong>JDBC</strong>.
 * Zawiera następne metody :
 * Boolean <strong>connect()</strong> - podłączenie do bazy danych;
 * Object<strong>selectObject(String p_query, Vector p_params, Object p_default)</strong> - 
 *       dla odzytywania <strong>pojedynczego</strong> objektu z <strong>pierwszego</strong> rekordu <strong>ResultSetu</strong>
 *       Jeśli <strong>ResultSet</strong> nie zawiera rekordu, zwracaza <strong>p_default</strong>.
 * Vector<Vector> <strong>selectAll(String p_query,
 *                                       Vector p_params,
 *                                       int p_iMinNumber,
 *                                       int p_iMaxNumber,
 *                                       Vector<Integer> p_vAsBoolean,
 *                                       boolean p_withBooleanColumn)</strong> -
 *       Wypełnia wektor danymi, otrzymanymi jako wynik zapytania <strong>p_query</strong>
 *       z parametrami, zawartymi w wektorze <strong>p_params</strong>, zaczynając od rekordu
 *       z numerem <strong>p_iMinNumber</strong> i do rekordu z numerem<strong>p_iMaxNumber - 1</strong>.
 *       W związku z brakiem w ORACLE typu BOOLEAN można zaznaczyć interpretację danych w kolumnach
 *       z numerami, zawartymi w wektorze <strong>p_vAsBoolean</strong> jako typ Java <strong>Boolean</strong>.
 *       Dodatkowa fukcjonalność - to możliwość dodania do wektora wynikowego jako pierwsza kolumna
 *       zmienną Boolean(false).
 * Vector<Vector> <strong>selectFirstRecordVertically(String p_query,
 *                                                    Vector p_params,
 *                                                    boolean bAsBoolean)</strong> -
 *       Trzeci parameter wskazuje na to czy przedstawiać kolumnę danych jako <b>Boolean</b?
 *       Wynik - dwowymiarowy wektor z dwoma kolumnami.
 *       Pierwsza kolumna - nazwy pól zapytania <strong>p_query</strong>,
 *       druga kolumna - zawartość pierwszego rekordu wynikowego <strong>ResultSet</strong>.
 * Vector<Integer> <strong> selectTypesToVector(String p_query,
 *                                             Vector p_params)</strong> -
 *       Wynik - wektor typów kolumn wynikowego <strong>ResultSet</strong>.
 * Vector<String> <strong>selectFirstRecordToVector(String p_query,
 *                                                         Vector p_params)</strong> -
 *       Wynik - wektor, zawierający pierwszy rekord wynikowego <strong>ResultSet</strong>.
 * Vector<Vector<String> > <strong>selectAllRecordsToVector(String p_query,
 *                                                         Vector p_params)</strong> -
 *       Wynik - wszzystkie rekordy wynikowego <strong>ResultSet</strong>.
 * boolean <strong>executeQuery(String p_query,Vector p_params)</strong> -
 *       Wykonuje polecenie SQL <strong>p_query</strong> z parametrami,
 *       zawartymi w wektorze <strong>p_params</strong>;
 * boolean <strong>executeProcedure(String p_execProc,Vector p_params)</strong> -
 *       Wykonuje procedurę PL/SQL <strong>p_execProc</strong> z parametrami,
 *       zawartymi w wektorze <strong>p_params</strong>
 * Object <strong>executeFunction(String p_execFunc,Vector p_params, Integer p_resultType)</strong> -
 *       Wykonuje procedurę PL/SQL <strong>p_execFunc</strong> z parametrami,
 *       zawartymi w wektorze <strong>p_params</strong>. Typ rezultatu przekazywany w
 *       parametrze <strong>p_resultType</strong>.(Np. <strong>oracle.jdbc.OracleTypes.VARCHAR,
 *       oracle.jdbc.OracleTypes.NUMBER,...</strong>)</p>
*/


public class YPOracleSession {

    public static String oraUrl;
    public static String oraLogin;
    public static String oraPassword;
    public Connection connection;
//    public ResultSet resultSet;
      public static final String OraDriver = "oracle.jdbc.driver.OracleDriver";
//    public static final String OraDriver = "oracle.jdbc.OracleDriver";

    public YPOracleSession() {
/*
        try {
            Class.forName(OraDriver);
            Errors.setLastError(-1, "");
        } catch (ClassNotFoundException e) {
            Errors.setLastError(0, e.getMessage());
        }
*/
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Errors.setLastError(-1, "");
        } catch (SQLException ex) {
            Errors.setLastError(0, ex.getMessage());
        }
        
        
    }
    
   /**
   * <p>Podłączenie do bazy danych</p>
   */
   public Boolean connect() {
        Boolean ret = true;
        connection = null;
        Errors.setLastError(-1, "");
        try {
            // próba połączenia
            DriverManager.setLoginTimeout(10);
            connection = DriverManager.getConnection(oraUrl, oraLogin, oraPassword);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            // połączenie nie powiodło się
            Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
            System.out.println(e.getErrorCode() + " - " + e.getMessage() + " State : " + e.getSQLState());
            ret = false;
        }
        return ret;
    }
    public String selectString(String p_query, Vector p_params, String p_default) {
        // Przypisujemy do objektu wyniku domyślną wartość.
        // Jeśli zapytanie SQL nie wykona się z któregoś powodu,
        // lub wynikowy ResultSet nie będzie miał rekordów, funkcja
        // zwróci nam tą wartość.
        Errors.setLastError(-1, "");

        String result = p_default;
        try {
            // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
            PreparedStatement pst = connection.prepareStatement(p_query);
            // Jeśli są parametry, przypisujemy ich do objektu
            if (p_params != null)
                for (int i = 0; i < p_params.size(); i++) {
                    // Kontrolujemy żeby ilość przekazanych parametrów była
                    // nie większa od faktycznie wymaganej ilości parametrów
                    if (i < pst.getParameterMetaData().getParameterCount())
                    {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                             pst.setObject(i+1, p_params.elementAt(i));
                    }
                }
            // wykonujemy zapytanie
            int count = pst.executeUpdate();
            // Jeśli są zwrocone rekordy
            if (count > 0) {
                // odczytujemy pierwszy rekord
                pst.getResultSet().next();
                result = YPFunctions.nvlStr(pst.getResultSet().getString(1));
            }
            // Zamykamy zapytanie
            pst.close();
        } catch (SQLException e) {
              Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
        }
        // zwracamy wynik
        return result;
    }

   /////////////////////////////////////////////////////////////////////////////
   /**
   * <p>
   *       dla odzytywania <strong>pojedynczego</strong> objektu z <strong>pierwszego</strong> rekordu <strong>ResultSetu</strong>
   *       Jeśli <strong>ResultSet</strong> nie zawiera rekordu, zwracaza <strong>p_default</strong>.
   * </p>
   */

/*
    public Object selectObject(String p_query, Vector p_params, Object p_default) {
        // Przypisujemy do objektu wyniku domyślną wartość.
        // Jeśli zapytanie SQL nie wykona się z któregoś powodu,
        // lub wynikowy ResultSet nie będzie miał rekordów, funkcja
        // zwróci nam tą wartość.
        Errors.setLastError(-1, "");

        Object result = p_default;
        try {
            // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
            PreparedStatement pst = connection.prepareStatement(p_query);
            // Jeśli są parametry, przypisujemy ich do objektu
            if (p_params != null)
                for (int i = 0; i < p_params.size(); i++) {
                    // Kontrolujemy żeby ilość przekazanych parametrów była
                    // nie większa od faktycznie wymaganej ilości parametrów
                    if (i < pst.getParameterMetaData().getParameterCount())
                    {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                             pst.setObject(i+1, p_params.elementAt(i));
                    }
                }
            // wykonujemy zapytanie
            int count = pst.executeUpdate();
            // Jeśli są zwrocone rekordy
            if (count > 0) {
                // odczytujemy pierwszy rekord
                pst.getResultSet().next();
                result = pst.getResultSet().getObject(1);
                // Oracle może zwrócić niektóre wyniki jako typ Java BigDecimal
                // lub typ BigInteger. W takim razie przetwarzamy do typu Integer.
                if      (result.getClass() == BigDecimal.class)
                    result = (Double)((BigDecimal)result).doubleValue();
                else if (result.getClass() == BigInteger.class)
                    result = (Integer)((BigInteger)result).intValue();
            }
            // Zamykamy zapytanie
            pst.close();
        } catch (SQLException e) {
              Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
        }
        // zwracamy wynik
        return result;
    }
*/
   /////////////////////////////////////////////////////////////////////////////
   /**
   * <p>   Odczytuje nazwy pól tablicy lub widoku z tablicy <strong>p_table</strong> do wektora.</p>
   */
    public Vector<String> selectFieldNames (String p_table) {
        Errors.setLastError(-1, "");
        // Wektor wyniku
        Vector<String> result = new Vector();
        // Odczytujemy tylko nagłowki pól, nie trzeba nam całej tabeli
        String sqlQuery =
           "select * from " + p_table + " where 1 = 2";
        try {
            // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
            PreparedStatement pst = connection.prepareStatement(sqlQuery);
            // wykonujemy zapytanie
            ResultSet rset = pst.executeQuery();
            // Odczytujemy wszystkie nazwy kolumn i kolejno zapisujemy
            // do wektora nazw. Ignorujemy ostatnią kolumnę
            // Pamietamy, że w JDBC pierwsza kolumna ma numer 1 a nie 0.
            for (int i = 1; i <= rset.getMetaData().getColumnCount(); i++) {
                result.add(rset.getMetaData().getColumnName(i).toUpperCase());
            }
            // Zamykamy zapytanie SQL
            pst.close();
        } catch (SQLException e) {
              Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
            // Nie robimy nic.
        }
        // Zwracamy wynik
        return result;
}





   /////////////////////////////////////////////////////////////////////////////
   /**
   * <p> Wynik - dwowymiarowy wektor z dwoma kolumnami.
   *       Pierwsza kolumna - nazwy pól zapytania <strong>p_query</strong>,
   *       druga kolumna - zawartość pierwszego rekordu wynikowego <strong>ResultSet</strong>.</p>
   */
    public Vector<Vector> selectFirstRecordVertically(String p_query,
                                                      Vector p_params,
                                                      boolean bAsBoolean) {
        Errors.setLastError(-1, "");
        // Wektor wyniku
        Vector<Vector> result = new Vector();
        // Zbiór odpowiedników typu Boolean
        Set booleanSet = new HashSet(Arrays.asList("T","Y","N"));
        try {
                // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
                PreparedStatement pst = connection.prepareStatement(p_query);
                // Jeśli są parametry, przypisujemy ich do objektu
                if (p_params != null)
                    for (int j = 0; j < p_params.size(); j++) {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                            pst.setObject(j+1, p_params.elementAt(j));
                    }
                // wykonujemy zapytanie
                ResultSet rset = pst.executeQuery();
                // wybieramy pierwszy rekord
                rset.next();
                for (int i = 1; i <= rset.getMetaData().getColumnCount(); i++) {
                    // Tworzymy nowy liniowy wektor
                    Vector v = new Vector();
                    // dodajemy do wektora nazwę kolumny
                    v.add(rset.getMetaData().getColumnName(i));
                    // odczytujemy zawartość ResultSet w tej samej kolumnie
                    String sTemp = YPFunctions.nvlStr(rset.getString(i));
                    if (bAsBoolean)
                    {
                            // Jeśli odczytana zawatrość jest typu Boolean
                            if (booleanSet.contains(sTemp))
                            {
                                // Dodajemy do wektora odpowiednik Boolean
                                if ((sTemp.equals("T")) ||
                                    (sTemp.equals("Y"))) {
                                        v.add(Boolean.valueOf(true));
                                } else {
                                        v.add(Boolean.valueOf(false));
                                }
                            }
                            // inaczej dodajemy do wektora zawartość
                            else
                                 v.add(sTemp);
                    }
                    else
                        v.add(sTemp);
                    // do wyniku dodajemy zawartość wektora
                    result.add(v);
                }
                // Zamykamy zapytanie SQL
                pst.close();
        } catch (SQLException e) {
             Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
        }
        // wynik
        return result;
    }
    /////////////////////////////////////////////////////////////////////////////
   /**
   * <p>Wynik - wektor typów kolumn wynikowego <strong>ResultSet</strong>.</p>
   */
    public Vector<Integer> selectTypesToVector(String p_query,
                                               Vector p_params) {
        Errors.setLastError(-1, "");
        // Wektor wyniku
        Vector<Integer> result = new Vector();
        try {
                // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
                PreparedStatement pst = connection.prepareStatement(p_query);
                // Jeśli są parametry, przypisujemy ich do objektu
                if (p_params != null)
                    for (int j = 0; j < p_params.size(); j++) {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                            pst.setObject(j+1, p_params.elementAt(j));
                    }
                // wykonujemy zapytanie
                ResultSet rset = pst.executeQuery();
                // Zapisujemy do wektora typy kolumn
                for (int i = 1; i <= rset.getMetaData().getColumnCount(); i++) {
                        result.add(rset.getMetaData().getColumnType(i));
                }
                // Zamykamy zapytanie SQL
                pst.close();
                } catch (SQLException e) {
                     Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
                }
        // wynik
        return result;
    }
    /////////////////////////////////////////////////////////////////////////////
   /**
   * <p>Wynik - wektor, zawierający pierwszy rekord wynikowego <strong>ResultSet</strong>.</p>
   */
    public Vector<String> selectFirstRecordToVector(String p_query,
                                                           Vector p_params) {
         Errors.setLastError(-1, "");
         // Wektor wyniku
          Vector<String> res = new Vector();
          try {
                // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
                PreparedStatement pst = connection.prepareStatement(p_query);
                // Jeśli są parametry, przypisujemy ich do objektu
                if (p_params != null)
                    for (int j = 0; j < p_params.size(); j++) {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                            pst.setObject(j+1, p_params.elementAt(j));
                    }
                // wykonujemy zapytanie
                ResultSet rset = pst.executeQuery();
                // Wybieramy pierwszy rekord
                rset.next();
                // Zapisulemy kolejno wszystkie pola rekordu ResultSet do wektora
                for (int i = 1; i <= rset.getMetaData().getColumnCount(); i++) {
                    res.add(YPFunctions.nvlStr(rset.getString(i)));
                }
                // Zamykamy zapytanie SQL
                pst.close();
          } catch (SQLException e) {
                Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
          }
          // Wynik
          return res;
    }
    /////////////////////////////////////////////////////////////////////////////
   /**
   * <p>Wynik - wszzystkie rekordy wynikowego <strong>ResultSet</strong>.</p>
   */
    public Vector<Vector<String> > selectAllRecordsToVector(String p_query,
                                                           Vector p_params) {
         Errors.setLastError(-1, "");
       // Wektor wyniku
        Vector<Vector<String> > result = new Vector();
        try {
                // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
                PreparedStatement pst = connection.prepareStatement(p_query);
                // Jeśli są parametry, przypisujemy ich do objektu
                if (p_params != null)
                    for (int j = 0; j < p_params.size(); j++) {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                            pst.setObject(j+1, p_params.elementAt(j));
                    }
                // wykonujemy zapytanie
                ResultSet rset = pst.executeQuery();
                while (rset.next()) {
                        // Tworzymy nowy liniowy wektor
                        Vector<String> v = new Vector();
                        // Odczytujemy wszystkie objekty z kolejnego rekordu
                        // ResultSetu i kolejno dopisujemy do wektora liniowego.
                        // Dalej pamietamy, że w JDBC pierwsza kolumna ma numer 1 a nie 0.
                        for (int i = 1; i <= rset.getMetaData().getColumnCount(); i++) {
                            // Postepujemy analogicznie do metody selectAll
                            String sTemp = YPFunctions.nvlStr(rset.getString(i));
                            // Zwracane z Oracle dane typu DATE
                            // mają wygląd YYYY/MM/DD HH:MM:SS:N, np.
                            // 2008/12/28 12:34:55.6. Jeszcze jeden przypadek - to
                            // 2008/12/28 00:00:00.0. W pierwszym przypadku jasne że
                            // chodzi o dacie i czasie, a w drugim - tylko o dacie.
                            // Znaczy, w drugim przypadku trzeba odrzucić końcówkę
                            // z czasem, a w pierwszym - odrzucić milisekundy.

                            if (rset.getMetaData().getColumnType(i) == 91) // DATE
                            {
                                 // Odrzucamy milisekundy
                                 if (sTemp.length() >= 19)
                                    sTemp = sTemp.substring(0, 19);
                                 // Czy drugi przypadek ?
                                 if (sTemp.length() >= 11) {
                                     if (sTemp.substring(11).equals("00:00:00")) {
                                        // Zostawiamy tylko datę
                                        sTemp = sTemp.substring(0, 10);
                                     }
                                 }
                            }
                            v.add(sTemp);
                        }
                        result.add(v);
               }
               pst.close();
        } catch (SQLException e) {
              Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
        }
        return result;
    }
    /////////////////////////////////////////////////////////////////////////////
   /**
   * <p>Wynik - Zawartość kolumny wynikowego <strong>ResultSet</strong>.</p>
   */
    public Vector<String> selectOneColumnToVector(String p_query,
                                                  Vector p_params) {
         Errors.setLastError(-1, "");
       // Wektor wyniku
        Vector<String> result = new Vector();
        try {
                // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
                PreparedStatement pst = connection.prepareStatement(p_query);
                // Jeśli są parametry, przypisujemy ich do objektu
                if (p_params != null)
                    for (int j = 0; j < p_params.size(); j++) {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                            pst.setObject(j+1, p_params.elementAt(j));
                    }
                // wykonujemy zapytanie
                ResultSet rset = pst.executeQuery();
                while (rset.next()) {
                    String sTemp = YPFunctions.nvlStr(rset.getString(1));
                    if (rset.getMetaData().getColumnType(1) == 91) // DATE
                    {
                         // Odrzucamy milisekundy
                         if (sTemp.length() >= 19)
                            sTemp = sTemp.substring(0, 19);
                         // Czy drugi przypadek ?
                         if (sTemp.length() >= 11) {
                             if (sTemp.substring(11).equals("00:00:00")) {
                                // Zostawiamy tylko datę
                                sTemp = sTemp.substring(0, 10);
                             }
                         }
                     }
                     result.add(sTemp);
                }
               pst.close();
        } catch (SQLException e) {
              Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
        }
        return result;
    }
    /////////////////////////////////////////////////////////////////////////////
   /**
   * <p> Wykonuje polecenie SQL <strong>p_query</strong> z parametrami,
   *     zawartymi w wektorze <strong>p_params</strong>* </p>
   */
    public boolean executeQuery(String p_query,Vector p_params) {
        Errors.setLastError(-1, "");
        boolean result = false;
        try {
            // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
            PreparedStatement pst = connection.prepareStatement(p_query);
            // Jeśli są parametry, przypisujemy ich do objektu
            if (p_params != null)
                for (int j = 0; j < p_params.size(); j++) {
                        // Numeracja parametrów w JDBC zaczyna się od 1,
                        // dla tego dodajemy jedynkę.
                        pst.setObject(j+1, p_params.elementAt(j));
                }
            // wykonujemy zapytanie
            int r = pst.executeUpdate();
            //boolean bExecute = pst.execute();
            // Zamykamy zapytanie
            pst.close();
            // Zatwierdzamy zmiany
            connection.commit();
            result = true;
        } catch (SQLException e) {
              Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
        }
        // wynik
        return result;
    }
    /////////////////////////////////////////////////////////////////////////////
   /**
   * <p> Wykonuje procedurę PL/SQL <strong>p_execProc</strong> z parametrami,
   *     zawartymi w wektorze <strong>p_params</strong></p>
   */
    public boolean executeProcedure(String p_execProc,Vector p_params) {
      Errors.setLastError(-1, "");
      boolean result = false;
        try {
                 // Tworzymy objekt CallableStatement dla wykonania procedury PL/SQL z parametrami.
                 CallableStatement proc = connection.prepareCall("begin " + p_execProc + "; end;");
                 // Jeśli są parametry, przypisujemy ich do objektu
                 if (p_params != null)
                    for (int j = 0; j < p_params.size(); j++) {
                            // Numeracja parametrów w JDBC zaczyna się od 1,
                            // dla tego dodajemy jedynkę.
                            proc.setObject(j+1, p_params.elementAt(j));
                    }
                 // wykonujemy procedurę
                 proc.execute();
                 // Zamykamy połączenie
                 proc.close();
                 // Zatwierdzamy zmiany
                 connection.commit();
                 result = true;
        } catch (SQLException e) {
              Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
        }
        return result;
    }

   /**
   * <p>Wykonuje procedurę PL/SQL <strong>p_execFunc</strong> z parametrami,
   *    zawartymi w wektorze <strong>p_params</strong>. Typ rezultatu przekazywany w
   *    parametrze <strong>p_resultType</strong>.(Np. <strong>oracle.jdbc.OracleTypes.VARCHAR,
   *    oracle.jdbc.OracleTypes.NUMBER,...</strong>)</p>
   */

    public Object executeFunction(String p_execFunc,Vector p_params, Integer p_resultType  ) {
       Errors.setLastError(-1, "");
       Object result = null;
        try {
                 // Tworzymy objekt CallableStatement dla wykonania funkcji PL/SQL z parametrami.
//                 CallableStatement func = connection.prepareCall("begin ? := " + p_execFunc + "; end;");
                 CallableStatement func = connection.prepareCall("{ ? = call " + p_execFunc + " }");

                 // Wykonujemy rejestrację wyjściowego parametru, przypisując mu typ
                 func.registerOutParameter(1, p_resultType);
                 // Jeśli są parametry do funkcji, przypisujemy ich do objektu
                 if (p_params != null)
                    for (int j = 0; j < p_params.size(); j++) {
                            // Tak jak pierwszy parameter to jest wynik, a numeracja
                            // parametrów w JDBC zaczyna się od 1, dla tego dodajemy 2.
                            func.setObject(j+2, p_params.elementAt(j));
                    }
                 // wykonujemy procedurę
                 func.execute();
                 // przypisujemy wynik
                 result = func.getObject(1);
                 // Zamykamy objekt
                 func.close();
                 // Zatwierdzamy zmiany
                 connection.commit();
        } catch (SQLException e) {
            Errors.setLastError(e.getErrorCode(), e.getMessage() + " State : " + e.getSQLState());
            javax.swing.JOptionPane.showMessageDialog(null, " " + e.getErrorCode() + e.getMessage());
        }
        // Zwracamy wynik
        return result;
    }
    
    public boolean tableExists(String pSchemaName, String pTableName) {
        return selectString(
                   "select table_name from SYS.ALL_ALL_TABLES where owner = ? and table_name=?",
                   new Vector(Arrays.asList(pSchemaName, pTableName)),
                   "") != "";
    }

    public boolean SaveLog(String pOperationDescription, String pOperationTarget) {
            
        return executeQuery("INSERT INTO CAS_OWNER.LOG_CASADMIN (" +
                            "USERNAME, COMPUTERNAME, OPERATIONDESCR, OPERATIONTARGET) " + 
                            "VALUES (?, ?, ?, ? )",
                            new Vector(Arrays.asList(
                                GlobalData.m_sDomainUserLogin,
                                GlobalData.m_sComputerName,
                                pOperationDescription, 
                                pOperationTarget)));

    }
}
        