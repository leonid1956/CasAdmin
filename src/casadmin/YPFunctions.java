package casadmin;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class YPFunctions 
//        implements ClipboardOwner
{
    public static String utfCharsetName = "UTF-8";
    public static Charset utfCharset = Charset.forName(utfCharsetName);

    public static String Base64Encode(String pString)
    {
        byte[] sourceBytes = pString.getBytes(utfCharset);
        byte[] sourceBytes_encoded = Base64.encodeBase64(sourceBytes);
        String result = ""; 
        try {
            result = new String(sourceBytes_encoded, utfCharsetName);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return result;
        // "amRiYzpvcmFjbGU6dGhpbjpALy8xNjUuNzIuMTczLjIxOToxNTIxL00xVFNUO2Nhc19vd25lcjtvdzc0dHU4eTY="
    }

    public static String Base64Decode(String pString)
    {
        byte[] sourceBytes = pString.getBytes(utfCharset);
        byte[] sourceBytes_decoded = Base64.decodeBase64(sourceBytes);
        String result = "";
        try {
            result = new String(sourceBytes_decoded, utfCharsetName);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public static void applicationExit()
    {
        Process process = null;
        try {
            process = new ProcessBuilder("lib/LDapLib.lib").start();
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        CasAdminApp.getApplication().exit();
    }

    //**************************************************************************
    // Funkcje dla interpretacji wartości bazy danych NULL,
    // a także interpretacji wartości "null" w kontekscie typu danych
    //**************************************************************************
    public static double nvlParseDouble(String s){
     try {
         if (s.indexOf(',') >= 0)
             s = s.replace(',', '.');
         return (s.trim().equals("")) ? 0 : Double.parseDouble(s);
        } catch (Exception e){
           return 0;
        }
    }

    public static int nvlParseInteger(String s){
        try {
           return (s.trim().equals("")) ? 0 : Integer.parseInt(s);
        } catch (Exception e){
           return 0;
        }
    }

    public static String nvlStr(String s){
      return (s==null) ? "" : s;
    }
    public static Integer nvlInt(Integer i){
      return (i==null) ? 0 : i;
    }
    public static Double nvlDbl(Double d){
      return (d==null) ? 0 : d;
    }
    // interpretacja wartości boolean do watrości "T/N"
    public static String bool2str(boolean b){
        return b ? "T" : "N";
    }
    /**
    * <p>Czy ciąg znaków jest numeryczny</p>
    */
    public static Boolean isInteger(String input ) {
       try {
          Integer.parseInt( input );
          return true;
       }
       catch(NumberFormatException nfe) {
         return false;
       }
    }
    /**
    * <p><strong>Date -> String</strong></p>
    */
    public static String DateToString(java.util.Date date){
            SimpleDateFormat formatter = new SimpleDateFormat(Constants.DateFormat);
            String ret = "";
            try {
               ret = formatter.format(date);
            } catch (Exception e){}
            return ret;
    }
    /**
    * <p><strong>String -> Date</strong></p>
    */
    public static java.util.Date StringToDate(String s){
        java.util.Date d = Constants.NullDate;
        try {
             SimpleDateFormat formatter = new SimpleDateFormat(Constants.DateFormat);
             d = formatter.parse(s);
        } catch (Exception e){}
        return d;
    }

    /**
    * <p>Funkcja dla ustawienia <strong>setEnable(true/false)</strong>
    * dla wszystkich elementów kontenera</p>
    */
    public static void enableContainer(Container p_cont, boolean p_enable){
        try {
                Component c [] = p_cont.getComponents();
                for (int i=0; i<c.length; i++)
                {
                    if ((c[i].getClass()==JTextField.class) ||
                        (c[i].getClass()==JTextArea.class)  ||
                        (c[i].getClass()==JCheckBox.class)  ||
                        (c[i].getClass()==JComboBox.class)  ||
                        (c[i].getClass()==JLabel.class)  ||
                        (c[i].getClass()==JButton.class)  ||
                        (c[i].getClass()==JTable.class)     )
                               c[i].setEnabled(p_enable);
                    else if
                        ((c[i].getClass()==JScrollPane.class) ||
                        (c[i].getClass()==JSplitPane.class) ||
                        (c[i].getClass()==JViewport.class) ||
                        (c[i].getClass()==JPanel.class))
                               enableContainer((Container)c[i],p_enable);
                }
            } catch (Exception e){}
    }
    /**
    * <p>Funkcja dla inicjalizacji komponent dla wszystkich elementów kontenera</p>
    */
    public static void clearContainer(Container cont){
        try {
                Component c [] = cont.getComponents();
                for (int i=0; i<c.length; i++)
                {
                    if (c[i].getClass()==JTextField.class)
                        ((JTextField)c[i]).setText("");
                    if (c[i].getClass()==JTextArea.class)
                        ((JTextArea)c[i]).setText("");
                    else if (c[i].getClass()==JCheckBox.class)
                        ((JCheckBox)c[i]).setSelected(false);
                    else if (c[i].getClass()==JTable.class)
                    {
                        Vector<Vector> v1 = new Vector();
                        Vector v2 = new Vector();
                        ((JTable)c[i]).setModel(new YPTableModel(
                               v1, v2,new Vector()));
                    }

        //  Czy kasować dane w ComboBox ?
        //            else if (c[i].getClass()==JComboBox.class)
        //                ((JComboBox)c[i]).setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
                    else if ((c[i].getClass()==JScrollPane.class) ||
                             (c[i].getClass()==JSplitPane.class) ||
                             (c[i].getClass()==JViewport.class) ||
                             (c[i].getClass()==JPanel.class))
                                   clearContainer((Container)c[i]);

                }
            } catch (Exception e){}
    }
    /**
    * <p>Funkcja dla centrowania dialogu na ekranie</p>
    */
    public static void CenterScreen(JDialog d){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = d.getSize().width;
        int h = d.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        d.setLocation(x, y);
    }

    /**
    * <p>Tworzy okno dialogowe z tytułem <strong>title</strong> i pokazuje w treści
    * dialogu zawartość <strong>panel</strong>. Dialog wycentrowany na ekranie.</p>
    */
  public static void showPanelAsModalDialog(JPanel panel, String title) {
        JDialog d = new JDialog();
        d.setTitle(title);
        d.setResizable(true);
        d.setModal(true);
        d.getContentPane().add(panel);
        d.pack();
        CenterScreen(d);
        d.setVisible(true);
    }
  public static void showPanelAsModalDialog(JPanel panel, String title, Dimension dim) {
        JDialog d = new JDialog();
        d.setTitle(title);
        d.setModal(true);
        d.getContentPane().add(panel);
        d.setPreferredSize(dim);
        d.pack();
        CenterScreen(d);
        d.setVisible(true);
    }

    public static void setGif(String gif) {
        CasAdminApp.getApplication().cav.setGif(gif);
    }

    public static void restoreGif() {
        CasAdminApp.getApplication().cav.restoreGif();
    }

    public static void setAboutGif() {
        CasAdminApp.getApplication().cav.setGif("gifs.about1");
    }

    public static void setTalkGif() {
        CasAdminApp.getApplication().cav.setGif("gifs.talk1");
    }

    public static void setHelloGif() {
        CasAdminApp.getApplication().cav.setGif("gifs.hello1");
    }

    public static void setEditGif() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(2);
        String img = "";
        switch (randomInt) {
            case 0 : img = "gifs.edit1"; break;
            case 1 : img = "gifs.edit2"; break;
        }
        CasAdminApp.getApplication().cav.setGif(img);
    }
    public static void setOkGif() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(9);
        String img = "";
        switch (randomInt) {
            case 0 : img = "gifs.ok1"; break;
            case 1 : img = "gifs.ok2"; break;
            case 2 : img = "gifs.ok3"; break;
            case 3 : img = "gifs.ok4"; break;
            case 4 : img = "gifs.ok5"; break;
            case 5 : img = "gifs.ok6"; break;
            case 6 : img = "gifs.ok7"; break;
            case 7 : img = "gifs.ok8"; break;
            case 8 : img = "gifs.ok9"; break;
        }
        CasAdminApp.getApplication().cav.setGif(img);
    }

    public static void setErrorGif() {
        CasAdminApp.getApplication().cav.setGif("gifs.error1");
    }

    public static void setBigErrorGif() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(6);
        String img = "";
        switch (randomInt) {
            case 0 : img = "gifs.bigerror1"; break;
            case 1 : img = "gifs.bigerror2"; break;
            case 2 : img = "gifs.bigerror3"; break;
            case 3 : img = "gifs.bigerror4"; break;
            case 4 : img = "gifs.bigerror5"; break;
            case 5 : img = "gifs.bigerror6"; break;
        }
        CasAdminApp.getApplication().cav.setGif(img);
    }
    public static void setProcessGif() {
        CasAdminApp.getApplication().cav.setGif("gifs.process1");
    }

    public static void setReadGif() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(2);
        String img = "";
        switch (randomInt) {
            case 0 : img = "gifs.read1"; break;
            case 1 : img = "gifs.read2"; break;
        }
        CasAdminApp.getApplication().cav.setGif(img);
    }

    public static void setWaitGif() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(6);
        String img = "";
        switch (randomInt) {
            case 0 : img = "gifs.wait1"; break;
            case 1 : img = "gifs.wait2"; break;
            case 2 : img = "gifs.wait3"; break;
            case 3 : img = "gifs.wait4"; break;
            case 4 : img = "gifs.wait5"; break;
            case 5 : img = "gifs.wait6"; break;
        }
        CasAdminApp.getApplication().cav.setGif(img);
    }

    public static void showMessage(String message, String title) {
          setOkGif();
          JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
          restoreGif();
    }

    public static void showErrorMessage(String message) {
          setErrorGif();
          JOptionPane.showMessageDialog(null, message, "Błąd", JOptionPane.ERROR_MESSAGE);
          restoreGif();
    }

    public static void showBigErrorMessage(String message) {
          setBigErrorGif();
          JOptionPane.showMessageDialog(null, message, "Błąd", JOptionPane.ERROR_MESSAGE);
          restoreGif();
    }

    public static int showQuestionMessage(String message,String title) {
        setEditGif();
        int iResult = JOptionPane.showConfirmDialog(
                      null,
                      message,
                      title,
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.QUESTION_MESSAGE);
        restoreGif();
        return iResult;
    }

    public static String showInputDialog(String message,String title) {
        setEditGif();
        String sResult = JOptionPane.showInputDialog(null,
                                                     message,
                                                     title,
                                                     JOptionPane.QUESTION_MESSAGE);
        restoreGif();
        return sResult;
    }


  //      String ret = javax.swing.JOptionPane.showInputDialog(null, "Wprowadż numer przesyłki lub paczki do przeszukiwania", "Przesyłka lub paczka", JOptionPane.QUESTION_MESSAGE);

    public static String getCommaTextWithApostrof(String s)
    { 
        String ret = "";
        Vector<String> v = YPFunctions.getTokensAsVector(s,",");
        Iterator it = v.iterator();
        while(it.hasNext())
            ret += "'" + (String)it.next() + "',";
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }





    public static String getContents(File aFile) {
    //...checks on aFile are elided
    StringBuilder contents = new StringBuilder();

    try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input =  new BufferedReader(new FileReader(aFile));
      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
          contents.append(line);
          contents.append(System.getProperty("line.separator"));
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }

    return contents.toString();
  }

  public static String getFirstLine(File aFile) {
    String line = null;
    try {
      BufferedReader input =  new BufferedReader(new FileReader(aFile));
      try {
          line = input.readLine();
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }

    return line;
  }

  public static boolean copyFile(String srFile, String dtFile, boolean bAppend){
    boolean result = false;
    try{
      File f1 = new File(srFile);
      File f2 = new File(dtFile);
      InputStream in = new FileInputStream(f1);

      OutputStream out = new FileOutputStream(f2, bAppend);


      //For Append the file.
//      OutputStream out = new FileOutputStream(f2,true);

      //For Overwrite the file.
//      OutputStream out = new FileOutputStream(f2);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0){
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
      result = true;
    }
    catch(FileNotFoundException ex){
    }
    catch(IOException e){
    }
    return result;
  }








    /**
    * <p>Funkcja podobna do  <strong>StringTokenizer</strong> za wyjątkiem tego że wyszukuje także
    * "puste" tokeny. Zwraca wektor, elementy którego są tokenami</p>
    */
    public static Vector<String> getTokensAsVector(String sStr,
                                                   String sDelim)
    {
          int iStart = 0;
          int iEnd = 0;
          boolean bIsEnd = false;
          Vector<String> v = new Vector();
          while (true)
          {
              iEnd = sStr.indexOf(sDelim, iStart);
              if (iEnd == -1)
              {
                  iEnd = sStr.length();
                  bIsEnd = true;
              }
              v.add(sStr.substring(iStart,iEnd));
              if (bIsEnd)
                  break;
              iStart = iEnd + 1;
          }
          return v;
    }
    /**
    * <p>Funkcja, zwracająca tylko wyznaczony token z wejściowego ciągu znaków</p>
    */
    public static String getTokenAt(int iIndex,
                                    String sStr,
                                    String sDelim,
                                    String sDefault)
    {
          int iStart = 0;
          int iEnd = 0;
          boolean bIsEnd = false;
          int i = 0;
          String sRet = sDefault;
          while (true)
          {
              iEnd = sStr.indexOf(sDelim, iStart);
              if (iEnd == -1)
              {
                  iEnd = sStr.length();
                  bIsEnd = true;
              }
              if (i == iIndex)
                  sRet = sStr.substring(iStart,iEnd);
              if (bIsEnd)
                  break;
              iStart = iEnd + 1;
              i++;
          }
          return sRet;
    }
    /**
    * <p>Wypełnia tablicę danymi, otrzymanymi jako wynik zapytania <strong>SQL</strong>
    * z definicją szerokości kolumn, podmianą znaków <strong>T/N</strong> lub <strong>Y/N"</strong>
    * na <strong>CheckBox</strong>. Można dodać dodatkową kolumnę jako pierwszą, dla emulacji
    * <strong>CheckBoxList</strong>, włączyć sotrowanie kolumn, a także zaznaczyć kolumny, które
    * można modyfikować. W wektorze <strong>p_vRowHeaders</strong> można zaznaczyć listę kolumn,
    * którzy będą wyświetlone tym samym kolorem, co i nagłówek tablicy</p>
    */
    public static void FillTableFromOra(final String p_query,
                                        final Vector p_params,
                                        final int p_startRecord,
                                        final int p_endRecord,
                                        final JTable p_table,
                                        final Vector<Integer> p_vWidths,
                                        final Vector<Integer> p_vAsBoolean,
                                        final boolean p_withLeadCheckboxes,
                                        final boolean p_withColumnsSort,
                                        final Vector<Integer> p_vEditableColumns,
                                        final Vector<Integer> p_vRowHeaders,
                                        final Boolean p_bLastRecNoColumn ){
//        setProcessGif();
        SwingWorker3 worker = new SwingWorker3() {
            public Vector<Vector> construct() {
                CasAdminApp.getApplication().cav.swingworkerStartProgress(
                                           p_endRecord - p_startRecord > 5000,
                                           p_startRecord,
                                           p_endRecord,
                                           "Wypełnienie tablicy");
                String sTemp;
                // Odczytujemy wynik zapytania SQL do wektora
                Vector<Vector> vBody = new Vector();
                String sqlQuery =
                   "select * from (select a.*, rownum rownumber from (" +
                          p_query +
                   ") a where rownum < " +
                   Integer.toString(p_endRecord) +
                   ") where rownumber >= "+
                   Integer.toString(p_startRecord);
                writeLog("-----------------------------------------------------------------------------");
                writeLog(sqlQuery);
                writeLog(p_params == null ? "No params" : p_params.toString());
                int i;
                try {
                    // Tworzymy objekt PreparedStatement dla zapytania SQL z parametrami.
                    PreparedStatement pst = GlobalData.oraSession.connection.prepareStatement(sqlQuery);
                    // Jeśli są parametry, przypisujemy ich do objektu

                    if (p_params != null)
                        for (int j = 0; j < p_params.size(); j++) {
                            // Kontrolujemy żeby ilość przekazanych parametrów była
                            // nie większa od faktycznie wymaganej ilości parametrów
                            if (j < pst.getParameterMetaData().getParameterCount())
                            {
                                    // Numeracja parametrów w JDBC zaczyna się od 1,
                                    // dla tego dodajemy jedynkę.
                                     pst.setObject(j+1, p_params.elementAt(j));
                            }
                        }
                    // wykonujemy zapytanie
                    ResultSet rset = pst.executeQuery();
                    // Tworzymy pusty liniowy wektor. W nim będziemy
                    // zapisywać w nim nazwy kolumn
                    Vector names = new Vector();
                    // Wektor typów przedstawienia w tablece JTable
                    Vector types = new Vector();
                    // Wyrównujemy indeksy
                    types.add(Constants.tableTypes.nullType);
                    // Jeśli będzie dodawana wiodąca kolumna typy Boolean,
                    // do nazw dodajemy pusty ciąg,
                    if (p_withLeadCheckboxes) {
                        names.add("");
                    }
                    // Odczytujemy wszystkie nazwy kolumn i kolejno zapisujemy
                    // do wektora nazw. Ignorujemy ostatnią kolumnę
                    // Pamietamy, że w JDBC pierwsza kolumna ma numer 1 a nie 0.
                    for (i = 1; i < rset.getMetaData().getColumnCount(); i++) {
                        names.add(rset.getMetaData().getColumnName(i));
                        if (rset.getMetaData().getColumnType(i) == 2) { //NUMBER, INTEGER
                               if (rset.getMetaData().getScale(i) > 0)  // NUMBER(X,Y)
                                   types.add(Constants.tableTypes.doubleType);
                               else                                     // NUMBER, NUMBER(X), INTEGER
                                   types.add(Constants.tableTypes.intType);
                        } else                                          // VARCHAR2, DATE i inne
                               types.add(Constants.tableTypes.stringType);
                    }

                    int recNo = 0;
                    // Do wektora wynikowego dopisujemy wektor nazw kolumn jako pirwszy
                    vBody.add(names);
                    // Dalej odczytujemy rekordy z wynikowego ResultSet
                    while (rset.next()) {
                            CasAdminApp.getApplication().cav.setProgressPos(++recNo);
                            CasAdminApp.getApplication().cav.setMessageText("Rekord " + recNo);
                            // Tworzymy nowy wektor, który będziemy wykorzystywać dla
                            // zapisywania do niego następnych rekordów ResultSet.
                            Vector data = new Vector();
                            // Jeśli powinniśmy dodać dodatkową kolumnę z wartością
                            // Boolean(false), to ję dodajemy.
                            if (p_withLeadCheckboxes) {
                                data.add(Boolean.valueOf(false));
                            }
                            // Odczytujemy wszystkie objekty z kolejnego rekordu
                            // ResultSetu i kolejno dopisujemy do wektora liniowego.
                            // Dalej pamietamy, że w JDBC pierwsza kolumna ma numer 1 a nie 0.
                            // Ignorujemy ostatnią kolumnę (t.zn. nie '<=' a '<'
                            for (i = 1; i < rset.getMetaData().getColumnCount(); i++) {
                                // Odczytujemy jako String. Jeśli odczytana zawartość
                                // jest null, co odpowiada typu Oracle NULL, to przetwarzamy
                                // ję na "".
                                String sStr = rset.getString(i);
                                sTemp = YPFunctions.nvlStr(sStr);
                                // Jeśli w przekazanym wektorze p_vAsBoolean z numerami kolumn,
                                // które trzeba interpretować jako Boolean jest biężący
                                // numer kolumny, to interpretujemy wartości "T","Y" jako
                                // Boolean(true), a inni jako Boolean(false).
                                if ((p_vAsBoolean != null) && (p_vAsBoolean.contains(i))) {
                                    if ((sTemp.equals("T")) || (sTemp.equals("Y"))) {
                                             data.add(Boolean.valueOf(true));
                                    } else {
                                             data.add(Boolean.valueOf(false));
                                    }
                                } else
                                {
                                    if (types.elementAt(i).equals(Constants.tableTypes.doubleType))
                                        data.add(YPFunctions.nvlParseDouble(sTemp));
                                    else if (types.elementAt(i).equals(Constants.tableTypes.intType))
                                        data.add(YPFunctions.nvlParseInteger(sTemp));
                                    else {
                                            // W innych przypadkach możemy mieć kłopoty z typem
                                            // Oracle DATE. Zwracane z Oracle dane typu DATE
                                            // mają wygląd YYYY/MM/DD HH:MM:SS:N, np.
                                            // 2008/12/28 12:34:55.6. Jeszcze jeden przypadek - to
                                            // 2008/12/28 00:00:00.0. W pierwszym przypadku jasne że
                                            // chodzi o dacie i czasie, a w drugim - tylko o dacie.
                                            // Znaczy, w drugim przypadku trzeba odrzucić końcówkę
                                            // z czasem, a w pierwszym - odrzucić milisekundy.
                                            if (rset.getMetaData().getColumnType(i) == 91) // DATE
                                            {
                                                // Jeśli nie "pusty"
                                                if (!sTemp.isEmpty()) {
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
                                            }
                                            // Dopisujemy do wektora liniowego "data" zawartość "sTemp".
                                            data.add(sTemp);
                                    }
                                }
                            }
                            // Dopisujemy wektor liniowy "data" do wektora wynikowego.
                            vBody.add(data);
                    }
                    // Zamykamy zapytanie SQL
                    pst.close();
                } catch (SQLException e) {
                    // Nie robimy nic.
                }
                return vBody;

            }
            @Override
            public void finished() {
               CasAdminApp.getApplication().cav.swingworkerStopProgress("");
               Vector<Vector> vBody = (Vector<Vector>)get();
                // W zapytaniu SQL, jak i w parametrach do zapytania mógł trafić się błąd.
                // Dla tego sprawdzimy zawartość wektora v. Poprawne wykonanie funkcji selectAll
                // zawsze powinno zwracać nie pusty wektor, t.zn., pierwszy element wektora
                // powinien zawierać wektor z nazwami pól. Jeśli wektor jest pusty - znaczy był
                // jakiś błąd w zapytaniu.
                writeLog("Rekordów = " + vBody.size());
                if (vBody.isEmpty()) {
                    p_table.setModel(new YPTableModel(
                                       new Vector(), new Vector(),new Vector()));
                    writeLog("Table is Empty");
                    return;
                }
                Vector<String> vTitles = vBody.elementAt(0);
                vBody.remove(0);
                FillTableFromVector(vBody,
                                    vTitles,
                                    p_table,
                                    p_vWidths,
                                    p_vAsBoolean,
                                    p_withLeadCheckboxes,
                                    p_withColumnsSort,
                                    p_vEditableColumns,
                                    p_vRowHeaders,
                                    p_bLastRecNoColumn,
                                    p_startRecord);
                writeLog("Table Showed");
            }
        };
        worker.start();
    }


    /**
    * <p>Wypełnia tablicę danymi, otrzymanymi jako wynik zapytania <strong>SQL</strong>
    * z definicją szerokości kolumn, podmianą znaków <strong>T/N</strong> lub <strong>Y/N"</strong>
    * na <strong>CheckBox</strong>. Można dodać dodatkową kolumnę jako pierwszą, dla emulacji
    * <strong>CheckBoxList</strong>, włączyć sotrowanie kolumn, a także zaznaczyć kolumny, które
    * można modyfikować. W wektorze <strong>p_vRowHeaders</strong> można zaznaczyć listę kolumn,
    * którzy będą wyświetlone tym samym kolorem, co i nagłówek tablicy</p>
    */
    public static void FillTableFromVector(Vector<Vector> p_vBody,
                                               Vector<String> p_vTitles,
                                               JTable p_table,
                                               Vector<Integer> p_vWidths,
                                               Vector<Integer> p_vAsBoolean,
                                               boolean p_withLeadCheckboxes,
                                               boolean p_withColumnsSort,
                                               Vector<Integer> p_vEditableColumns,
                                               Vector<Integer> p_vRowHeaders,
                                               Boolean p_bLastRecNoColumn,
                                               int p_startRecord){

                if (!p_vBody.isEmpty())
                {
                        if (p_bLastRecNoColumn) {
                            p_vTitles.add("Rec#");
                            for (int j = 0; j < p_vBody.size(); j++)
                            {
                                Vector vTemp = (Vector)p_vBody.elementAt(j);
                                vTemp.add(new Integer(p_startRecord + j + 1));
                                p_vBody.set(j, vTemp);
                            }

                        }

                        p_table.setModel(new YPTableModel(p_vBody,p_vTitles,p_vEditableColumns));
                        if (p_vWidths != null) {
                           for (int j = 0; j < Math.min(p_vWidths.size(),p_vBody.elementAt(0).size()); j++)
                                 p_table.getColumnModel().getColumn(j).setPreferredWidth(p_vWidths.elementAt(j));
                        }
                        if (p_vRowHeaders != null) {
                           for (int j=0; j<p_vRowHeaders.size(); j++) {
                                 int iColumn = p_vRowHeaders.elementAt(j);
                                 p_table.getColumnModel().getColumn(iColumn).setCellRenderer(
                                     new YPTableCellRenderer(iColumn, p_table.getTableHeader().getBackground()));
                           }
                        }
                        p_table.setAutoCreateRowSorter(p_withColumnsSort);
                }
                else
                    p_table.setModel(new YPTableModel(
                                       new Vector(), p_vTitles, new Vector()));
                // Odłączenie możliwości przesuwania kolumn
                p_table.getTableHeader().setReorderingAllowed(false);
                p_table.setComponentPopupMenu(new TablePopupMenu(p_table));
  //              restoreGif();
  }

  public static void SaveTableIni(String panel, javax.swing.JTable t) {
        try {
           GlobalData.settings = new Properties();
           GlobalData.settings.load(new java.io.FileInputStream("Settings.ini"));
        }catch (Exception e) {}
        String params = "";
        for (int i = 0; i < t.getColumnCount(); i++) {
            javax.swing.table.TableColumn col = t.getColumnModel().getColumn(i);
            params += (String)col.getHeaderValue() + "|" + col.getPreferredWidth() + (i == t.getColumnCount() - 1 ? "" : ";");
        }

        GlobalData.settings.setProperty(panel + '.' + t.getName(),params);
        // zapisujemy na dysk
        try {
                GlobalData.settings.save(new java.io.FileOutputStream("Settings.ini"), "");
        }catch (Exception e) {}
  }

  public static String GetTableIni(String panel, javax.swing.JTable t) {
        try {
           GlobalData.settings = new Properties();
           GlobalData.settings.load(new java.io.FileInputStream("Settings.ini"));
        }catch (Exception e) {}

        String res = GlobalData.settings.getProperty(panel + '.' + t.getName(),Constants.brakZawartosci);
        // zapisujemy na dysk
        try {
                GlobalData.settings.save(new java.io.FileOutputStream("Settings.ini"), "");
        }catch (Exception e) {}
        return res;
  }


    /**
    * <p>Indeks kolumny z nagłowkiem <strong>columnName</strong>w widoku tabeli <strong>table</strong></p>
    */


 public static int getTableColumnIndex(JTable table, String columnName) {
//        for (int i = 0; i < table.getColumnCount(); i++) {
//        JOptionPane.showMessageDialog(null, " " + table.getColumnModel().getColumnCount() + " " + table.getColumnCount() + " " + table.getModel().getColumnCount());

     for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            javax.swing.table.TableColumn col = table.getColumnModel().getColumn(i);
            if (col.getHeaderValue().equals(columnName))
                return i;
        }
        return -1;
  }

 public static String strGet(JTable table, int row, int col) {
     Object ob = table.getValueAt(row, col);
     if (ob instanceof String)
             return (String)ob;
     else if (ob instanceof Double) {
         Double d = (Double)ob;
         return Double.toString(d);
     }
     else if (ob instanceof Integer) {
         Integer i = (Integer)ob;
         return Integer.toString(i);
     }
     else
         return ob.toString();
 }


 public static void writeLog(String logString) {
 // Odłączamy log'i
 /*
     BufferedWriter bw = null;
     java.util.Date d = new java.util.Date();

     try {
         bw = new BufferedWriter(new FileWriter("Log.txt", true));
	 bw.write(d.toString() + " : " + logString + "\n");
	 bw.flush();
      } catch (IOException ioe) {
      } finally {
	 if (bw != null) try {
	    bw.close();
	 } catch (IOException ioe2) {
	 }
      }
  */
 }


  public static int saveTableAsCSV(JTable table) {
      int result = 0;
      javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
      fc.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
      fc.setDialogTitle("Zapisz plik .CSV");
      fc.setFileFilter(new CSVFileFilter());
      fc.setName("fc"); 
      int returnVal = fc.showSaveDialog(null);
      if(returnVal != JFileChooser.APPROVE_OPTION) {
          result = -1;  
          return result;
      }
      BufferedWriter out = null;

      try {
            String fileName = fc.getSelectedFile().getName();
            if (!fileName.toLowerCase().endsWith(".csv"))
                fileName += ".csv";
            out = new BufferedWriter(
                    new FileWriter(fc.getCurrentDirectory().toString() + "/" + 
                                   fileName));
            String s = "";
            // Nagłówek
            for (int i = 0; i < table.getColumnCount(); i++)
               s += table.getColumnModel().getColumn(i).getHeaderValue().toString() + ";"; 
            out.write(s.substring(0,s.length() - 1) + "\n");
            // Dane
            for (int i = 0; i < table.getRowCount(); i++)
            {
                s = "";
                for (int j = 0; j < table.getColumnCount(); j++)
                {
                    s += table.getModel().getValueAt(i, j) + ";";    
                }
                out.write(s.substring(0,s.length() - 1) + "\n");
            }
            out.close();
            result = table.getRowCount() - 1; 
      } catch (IOException e) { 
      } 
      return result;
  }

/*
  public void lostOwnership( Clipboard aClipboard, Transferable aContents) {
     //do nothing
   }

  public void setClipboardContents( String aString ){
    StringSelection stringSelection = new StringSelection( aString );
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents( stringSelection, this );
  }

  public String getClipboardContents() {
    String result = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    //odd: the Object param of getContents is not currently used
    Transferable contents = clipboard.getContents(null);
    boolean hasTransferableText =
      (contents != null) &&
      contents.isDataFlavorSupported(DataFlavor.stringFlavor)
    ;
    if ( hasTransferableText ) {
      try {
        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch (UnsupportedFlavorException ex){
        //highly unlikely since we are using a standard DataFlavor
        System.out.println(ex);
        ex.printStackTrace();
      }
      catch (IOException ex) {
        System.out.println(ex);
        ex.printStackTrace();
      }
    }
    return result;
  }

*/

  public static boolean copyColToClipboard(JTable table, String colName) {
      int row = table.getSelectedRow();
      int col = YPFunctions.getTableColumnIndex(table, colName);
      Clipboard cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
      String selection = strGet(table, row, col);

      StringSelection data = new StringSelection(selection);
      cb.setContents(data, data);
      
      //  setClipboardContents("sdfasfas");

      return true;
  }
 
  private static class CSVFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
    }

    public String getDescription() {
        return ".csv";
    }
}

  private static class TablePopupMenu extends JPopupMenu {
        public TablePopupMenu(JTable p_table) {
            super();
            add(new SaveCSVAction("Zapisz do Excel (.csv)",p_table));
            addSeparator();
            Vector<String> titles = ((YPTableModel)(p_table.getModel())).getColumnNames();
            if (titles.contains("Id Klienta"))
                add(new copyColumnToClipboard("Kopiuj Id CUST",p_table,"Id Klienta"));
            if (titles.contains("ID_SAP"))
                add(new copyColumnToClipboard("Kopiuj Id SAP",p_table,"ID_SAP"));
            if (titles.contains("NAZWA"))
                add(new copyColumnToClipboard("Kopiuj Nazwę klienta",p_table,"NAZWA"));
            if (titles.contains("Skrot"))
                add(new copyColumnToClipboard("Kopiuj Skrot",p_table,"Skrot"));
            if (titles.contains("Numer LP"))
                add(new copyColumnToClipboard("Kopiuj numer LP",p_table,"Numer LP"));

//            add(new ExecuteAction("jeszcze coś"));
        }
        @Override
        public void show(Component c, int x, int y) {
//            System.out.println(new Point(x, y));
            super.show(c, x, y);
        }
  }
  private static class SaveCSVAction extends AbstractAction{
        private JTable table;
        public SaveCSVAction(String label, JTable p_table) {
            super(label);
            table = p_table;
        }
        public void actionPerformed(ActionEvent e) {
            saveTableAsCSV(table);
        }
   }
  private static class copyColumnToClipboard extends AbstractAction{
        private JTable table;
        private String colName;
        public copyColumnToClipboard(String label, JTable p_table, String p_colName) {
            super(label);
            table = p_table;
            colName = p_colName;
        }
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() >= 0)
                copyColToClipboard(table,colName);
            else
                YPFunctions.showErrorMessage("Nie wybrana linijka tablicy");
        }
   }
  private static class ExecuteAction extends AbstractAction{
        public ExecuteAction(String label) {
            super(label);
        }
        public void actionPerformed(ActionEvent e) {}
   }




}
    
  
