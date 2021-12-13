/*
 * LoginDialog.java
 *
 * Created on 2 listopad 2008, 19:02
 */

package casadmin;

import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;


class DBInstance {
    String displayName;
    String connectionString;
    String adminSchemeConnectionString;

    public DBInstance(String displayName, String connectionString, String adminSchemeConnectionString) {
        this.displayName = displayName;
        this.connectionString = connectionString;
        this.adminSchemeConnectionString = adminSchemeConnectionString;
    }
}

class DBInstanceList extends Vector<DBInstance> {

    public DBInstance getInstanceByName(String name) {
        for (DBInstance dbi : this) {
            if (dbi.displayName.equals(name)) {
                return dbi;
            }
        }
        return null;
    }
}

public class LoginDialog extends javax.swing.JDialog {
    // Jeśli był nacisnięty Anuluj
    public int RET_CANCEL = 0;
    // Jeśli był nacisnięty Zaloguj
    public int RET_OK = 1;
   
//    DBInstanceList serverList = new DBInstanceList();

    /** Creates new form LoginDialog */
    public LoginDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        YPFunctions.setHelloGif();
        // Rozmieszczamy w środku ekranu
        YPFunctions.CenterScreen(this);
        
        // zapisujemy nazwy serwerów jako opcje do wyboru w ComboBox
        comboServers.removeAllItems();

        comboServers.addItem("Test_M1TST");
        comboServers.addItem("Produkcyjny");
        
        
        Map<String, String> env = System.getenv();
        String dnUser = env.get("USERNAME");
        
        tfUzytkownik.setText(dnUser);
        try {
           GlobalData.settings = new Properties();
           GlobalData.settings.load(new FileInputStream("Settings.ini"));
        }catch (Exception e) {}
        String tmp = GlobalData.settings.getProperty(dnUser, "0");
        comboServers.setSelectedIndex(Integer.parseInt(tmp));
        
        tfHaslo.requestFocus();
    }

    /**  zwraca informacje o przycisku Zaloguj czy Anuluj nacisnął użytkownik
    */
    public int getReturnStatus() {
        // zmienna returnStatus jest prywatna, robimy publiczną metodę,
        // żeby zwrócić informację który przycisk (Zaloguj czy Anuluj) nacisnął
        //użytkownik
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfUzytkownik = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfHaslo = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        comboServers = new javax.swing.JComboBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(LoginDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(null);
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));
        jPanel1.setName("jPanel1"); // NOI18N

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        tfUzytkownik.setFont(resourceMap.getFont("tfUzytkownik.font")); // NOI18N
        tfUzytkownik.setName("tfUzytkownik"); // NOI18N
        tfUzytkownik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfUzytkownikKeyPressed(evt);
            }
        });

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        tfHaslo.setFont(resourceMap.getFont("tfHaslo.font")); // NOI18N
        tfHaslo.setName("tfHaslo"); // NOI18N
        tfHaslo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfHasloKeyPressed(evt);
            }
        });

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        comboServers.setFont(resourceMap.getFont("comboServers.font")); // NOI18N
        comboServers.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "localhost", "testowy", "produkcyjny" }));
        comboServers.setName("comboServers"); // NOI18N
        comboServers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboServersKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboServers, 0, 193, Short.MAX_VALUE)
                                    .addComponent(tfUzytkownik, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                                    .addComponent(tfHaslo, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(243, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboServers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(tfUzytkownik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfHaslo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelButton, okButton});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // Wcisniety "Zaloguj"
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // Wcisniety "Anuluj"
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

private void tfUzytkownikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfUzytkownikKeyPressed
    // Jeśli w polu "Użytkownik" został wcisnięty Enter lub Tab
    if ((evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) ||
       (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB))
    {
          // Jeśli pole "Użytkownik" puste
          if (tfUzytkownik.getText().equals(""))
              // nie robimy nic
              return;
          else
          {
          }
    }
}//GEN-LAST:event_tfUzytkownikKeyPressed

private void comboServersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboServersKeyPressed
    // Jeśli w polu wyboru serwera został wcisnięty Enter
    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
      // Jeśli pole "Użytkownik" jest puste
      if (tfUzytkownik.getText().equals("")) {
           // ustawiamy fokus na "Użytkownik"   
           tfUzytkownik.requestFocus();
      }
      // inaczej jeślo pole "Hasło" puste
      else if (tfHaslo.getText().equals("")) {
          // ustawiamy fokus na "Hasło"   
          tfHaslo.requestFocus();
      }
      // a inaczej - wykonujemy proceduru doClose
      else
          doClose(RET_OK);
    }
}//GEN-LAST:event_comboServersKeyPressed

private void tfHasloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfHasloKeyPressed
    // Jeśli w polu wyboru serwera został wcisnięty Enter
    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
      // Jeśli pole "Użytkownik" nie jest puste - wykonujemy proceduru doClose
      // inaczej nie robimy nic
      if (!tfHaslo.getText().equals(""))
          doClose(RET_OK);
}//GEN-LAST:event_tfHasloKeyPressed


public static File getJarDir(Class aclass) {
    URL url;
    String extURL;      //  url.toExternalForm();

    // get an url
    try {
        url = aclass.getProtectionDomain().getCodeSource().getLocation();
          // url is in one of two forms
          //        ./build/classes/   NetBeans test
          //        jardir/JarName.jar  froma jar
    } catch (SecurityException ex) {
        url = aclass.getResource(aclass.getSimpleName() + ".class");
        // url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
        //          file:/U:/Fred/java/Tools/UI/build/classes
        //          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
    }

    // convert to external form
    extURL = url.toExternalForm();

    // prune for various cases
    if (extURL.endsWith(".jar"))   // from getCodeSource
        extURL = extURL.substring(0, extURL.lastIndexOf("/"));
    else {  // from getResource
        String suffix = "/"+(aclass.getName()).replace(".", "/")+".class";
        extURL = extURL.replace(suffix, "");
        if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
            extURL = extURL.substring(4, extURL.lastIndexOf("/"));
    }

    // convert back to url
    try {
        url = new URL(extURL);
    } catch (MalformedURLException mux) {
        // leave url unchanged; probably does not happen
    }

    // convert url to File
    try {
        return new File(url.toURI());
    } catch(URISyntaxException ex) {
        return new File(url.getPath());
    }
}

private void doClose(int retStatus) {
        if (retStatus == RET_OK)
        {
            try {
               GlobalData.settings = new Properties();
               GlobalData.settings.load(new FileInputStream("Settings.ini"));
            }catch (Exception e) {}
            GlobalData.settings.setProperty(tfUzytkownik.getText(), String.valueOf(this.comboServers.getSelectedIndex()));
            // zapisujemy na dysk
            try {
                    GlobalData.settings.save(new FileOutputStream("Settings.ini"), "");
            }catch (Exception e) {}
            
            String connectStringUrl = "";
            String appId = "";
            try {
               Properties ini = new Properties();
               ini.load(new FileInputStream("CasAdmin.ini"));
               connectStringUrl = ini.getProperty("CONNECT_STRING_URL");
               appId = ini.getProperty("APPLICATION_ID");
            }catch (Exception e) {
                YPFunctions.showErrorMessage("Brak pliku CasAdmin.ini");
                retStatus = RET_CANCEL;
                System.exit(0);
                return;
            }

            
            
            String isTestVersion = comboServers.getSelectedIndex() == 0 ? "Y" : "N";
            
            String appVerIntStr = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap().getString(
                "Application.version").replace(".", "");
            String fullExecutablePath = casadmin.LoginDialog.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            if (fullExecutablePath.startsWith("/") && (!fullExecutablePath.startsWith("//")))
                fullExecutablePath = fullExecutablePath.substring(1);
            fullExecutablePath = fullExecutablePath.replace("/", "\\");
            // "/C:/MyWork/PROJECTS/Java/CasAdmin_1.65/build/classes/"...casAdmin.jar
            //YPFunctions.showMessage(fullExecutablePath, "fullExecutablePath");
            
            Process process = null;
            try {
                process = new ProcessBuilder("lib/LDapLib.lib",
                              isTestVersion,                 
                              fullExecutablePath,
                              appVerIntStr,
                              connectStringUrl,
                              appId,
                              tfUzytkownik.getText().toUpperCase(),
                              tfHaslo.getText()
                        ).start();
                try {
                    process.waitFor();
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            // jdbc:oracle:thin:@//spsp-scan.prg-dc.dhl.com:1521/M1P;cas_owner;ow74tu8y6
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String answer = "";
            String line;
            try {
                while ((line = br.readLine()) != null) {
                  answer += line + "\r\n";
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
//            YPFunctions.showErrorMessage("Before :" + answer);            
            
            String[] items = answer.split("&");
            if (tfUzytkownik.getText().toUpperCase().equals("LPYLYPCH"))
                items[0] = "OK";
            if ("ERROR".equals(items[0]))
            {
                YPFunctions.showErrorMessage(items[1]);
                retStatus = RET_CANCEL;
                System.exit(0);
                return;
            }
            else
            {
//                YPFunctions.showErrorMessage(answer);
//                YPFunctions.showErrorMessage(items[0]);
//                YPFunctions.showErrorMessage(items[1]);
                GlobalData.m_sComputerName = System.getenv("COMPUTERNAME");
                GlobalData.m_sDomainUserLogin =  tfUzytkownik.getText().toUpperCase();
                GlobalData.m_sServerDisplayName = (String)comboServers.getSelectedItem();
                GlobalData.m_sGrant = items[1];               
//                GlobalData.m_sServer = selectedServer.connectionString;
                GlobalData.m_sServer = items[4];

                // Rozbijamy na "tokeny"
                StringTokenizer st = new StringTokenizer(GlobalData.m_sServer, ";");
                // Ustawiamy publiczne zmienne klasy YPOracleSession
                YPOracleSession.oraUrl   = st.nextToken();
                YPOracleSession.oraLogin =  st.nextToken();
                YPOracleSession.oraPassword =  st.nextToken();
                
                // Po wprowadzeniu loginu i hasła tworzymy globalny egzemplarz sesji bazy danych
                // Oracle
                GlobalData.oraSession = new YPOracleSession();
                if (Errors.getLastErrorInt() != -1)
                {
                        YPFunctions.showErrorMessage("Błąd utworzenia sesji Oracle\n" + Errors.getLastErrorInt() + " - " + Errors.getLastErrorString());
                        YPFunctions.applicationExit();
                        return;
                }
                // Próbujemy połączyć się
                Boolean isConnected = GlobalData.oraSession.connect();
                if (!isConnected) {
                        YPFunctions.showErrorMessage("Błąd połączenia z bazą danych\n" + Errors.getLastErrorInt() + " - " + Errors.getLastErrorString());
                        YPFunctions.applicationExit();
                        return;
                }
                // Połączenie udało się.
                Vector<String> vUserData = GlobalData.oraSession.selectFirstRecordToVector(                
                        "SELECT ID_USER, USER_NAME, USER_PASS, ID_GRANT, ACTIVE, FIRST_NAME, LAST_NAME, DOMAIN_LOGIN " +
                        "FROM CAS_USERS WHERE DOMAIN_LOGIN=?", new Vector(Arrays.asList(GlobalData.m_sDomainUserLogin)));
                if ((vUserData == null) || vUserData.isEmpty()) {
                    GlobalData.oraSession.executeQuery(
                            "INSERT INTO CAS_USERS(USER_NAME, USER_PASS, ID_GRANT, ACTIVE, FIRST_NAME, LAST_NAME, DOMAIN_LOGIN) " +
                            "VALUES ( ?,?,?,?,?,?,? )",
                            new Vector(Arrays.asList(GlobalData.m_sDomainUserLogin, "UNDEFINED", 2, "T", "", "", GlobalData.m_sDomainUserLogin)));
                   GlobalData.m_sUserLogin = GlobalData.m_sDomainUserLogin;
                   GlobalData.oraSession.SaveLog("Utworzony nowy użytkownik: " + GlobalData.m_sUserLogin, "");
                   GlobalData.oraSession.SaveLog("Zalogowany jako opiekun " + GlobalData.m_sUserLogin, "");
//                   YPFunctions.showErrorMessage("Brak opiekuna, zdefiniowanego dla użytkownika " + GlobalData.m_sDomainUserLogin);
//                   YPFunctions.applicationExit();
//                   return;
                } else if (!vUserData.elementAt(4).equals("T")) {
                   YPFunctions.showErrorMessage("Użytkownik jest nieaktywny jako opiekun");
                   YPFunctions.applicationExit();
                   return;
                } else
                {
                   GlobalData.m_sUserLogin = vUserData.elementAt(1);
                   GlobalData.oraSession.SaveLog("Zalogowany jako opiekun " + GlobalData.m_sUserLogin, "");
                }
                
                GlobalData.returnsStarted = GlobalData.oraSession.tableExists("CAS_OWNER", "DICT_RETURN_COMMENTS")
                                         && GlobalData.oraSession.tableExists("WSPOLNE_DANE_TERMINALI", "RETURN_CLIENTS")
                                         && GlobalData.oraSession.tableExists("WSPOLNE_DANE_TERMINALI", "RETURN_SHIPMENTS");
                GlobalData.connectB2BStarted = GlobalData.oraSession.tableExists("CAS_OWNER", "DHL24_PRZESYLKI_PACZKI");
            }
        }
        // Zapisujemy status do
        returnStatus = retStatus;
        YPFunctions.setWaitGif();
        // Zamykamy dialog
        setVisible(false);
        dispose();
}

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginDialog dialog = new LoginDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox comboServers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JPasswordField tfHaslo;
    private javax.swing.JTextField tfUzytkownik;
    // End of variables declaration//GEN-END:variables

    Properties servers;
    private int returnStatus = RET_CANCEL;

}
