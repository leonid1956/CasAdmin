/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ShowSpecialSettingsDialog.java
 *
 * Created on 2011-01-20, 15:21:59
 */

package casadmin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author lpylypch
 */
public class ShowSpecialSettingsDialog extends javax.swing.JDialog {

    private Vector<Vector> bodyTable = null;
    private String parameters = "";
    private Map<String, Integer> mapNames = null;
    private Map<String, String> mapResults = null;
    private Integer iIdCas;

    private int nextNumber = 0;
    /** Creates new form ShowSpecialSettingsDialog */
    public ShowSpecialSettingsDialog(java.awt.Frame parent, boolean modal, String p_parameters, Integer p_idCas) {
        super(parent, modal);
        initComponents();
        iIdCas = p_idCas;
        parameters = p_parameters;
        fillTableBody();
    }


    private boolean s2b(String s)
    {
         return (!s.equals("0"));
    }

    private void put(String p_line, int p_pos, String p_result)
    {
        int pos = p_pos;
        mapNames.put(p_line, pos);
        mapResults.put(p_line, p_result);
        boolean checked = false;
        if (pos == 0)
            checked = true;
        else
        {
            if (pos > 1000)
            {
                int p = (int) (pos/100);
                String v = parameters.substring(p - 1, p);
                String s = Integer.toString(pos - p * 100);
                if (
                        ((s.equals("1")) && Arrays.asList("1","3","5","7","9","B","D","F").contains(v)) ||
                        ((s.equals("2")) && Arrays.asList("2","3","6","7","A","B","E","F").contains(v)) ||
                        ((s.equals("3")) && Arrays.asList("4","5","6","7","C","D","E","F").contains(v)) ||
                        ((s.equals("4")) && Arrays.asList("8","9","A","B","C","D","E","F").contains(v))
                   )
                   checked = true;
                else
                    checked = false;
            }
            else
            {
                String value = parameters.substring(pos - 1, pos);
                checked = s2b(value);
            }
        }
        bodyTable.add(new Vector(Arrays.asList(
                    p_line,
                    checked,
                    false,
                    checked,
                    nextNumber)));
        nextNumber++;
    }


    private void fillTableBody()
    {
        bodyTable = new Vector();
        mapNames = new HashMap<String, Integer>();
        mapResults = new HashMap<String, String>();

        put("Odczyt danych z plików XML (Numer LP generowany)",6, "GET_XML_NOLP");
        put("Odczyt danych z plików XML (Numer LP pobierany)",13, "GET_XML_WITHLP");
        put("Odczyt danych z plików TXT (Numer LP generowany)",11, "GET_TXT_NOLP");
        put("Odczyt danych z plików TXT (Numer LP pobierany)",10, "GET_TXT_WITHLP");
        put("Nie pokazywać informacji o cenach",7,"NO_PRICES");
        put("Nowy standard wydruku etykiet ( Label && Identifier )",0, "COMMON_LABEL");          //5
        put("Emulacja programu LP9ORA",18,"LP9ORA");                                             //6
        put("Weryfikacja danych dla trybu LP9ORA",24,"LP9ORA_CHECK");                            //7
        put("Pozwolenie na pracę z BLP",19,"BLP");
        put("Modyfikacja dla APOLLO ( Uwagi -> faktury )",20,"APOLLO");
        put("Pozwolenie na pracę batczową z XML",21,"BATCH_XML");
        put("TnT for ROD",23,"TNT_ROD");
        put("Zapis danych do XML",25,"WRITE_TO_XML");
        put("Zapis danych przesyłki z późniejszym wydrukiem LP",26,"WRITE_LP_PRINT_LATER");
        put("Zawiadomienie nadawcy o doręczaniu przesyłki poprzez EMail",2701,"NOTIF_EMAIL");
        put("Zawiadomienie nadawcy o doręczaniu przesyłki poprzez SMS",2702,"NOTIF_SMS");
        put("Zawiadomienie odbiorcy o doręczaniu przesyłki poprzez EMail",2703,"PREAWI_EMAIL");
        put("Zawiadomienie odbiorcy o doręczaniu przesyłki poprzez SMS",2704,"PREAWI_SMS");
        put("Wersja dla Punktów Przyjęć",28,"SP_VERSION");
        put("Wprowadzanie Nadawcy i Odbiorcy przy stałym Zleceniodawcę",29,"THIRD_CONST");
        put("Wprowadzanie referencji Nadawcy na poziomie przesyłki",30,"SHIP_REF");
        put("Płaci Odbiorca za palety po cenach Nadawcy",31,"PAL_ODB");
        put("Wprowadzanie DWP z pliku PDF",32,"DWP_FROM_PDF");
        put("Pozwolenie na długość Uwag do 250 znaków",33,"WARNINGS_250");
        put("Wprowadzanie referencji Nadawcy na poziomie paczki",34,"ITEM_REF");
        put("Odczyt danych z plików XML do bufora (Numer LP generowany)",35,"BUF_XML_NOLP");
        put("Odczyt danych z plików TXT do bufora (Numer LP generowany)",36,"BUF_TXT_NOLP");
        put("Odczyt danych z plików XML do bufora (Numer LP pobierany)",37,"BUF_XML_WITHLP");
        put("Odczyt danych z plików TXT do bufora (Numer LP pobierany)",38,"BUF_TXT_WITHLP");
        put("Kontrolowanie zmiany daty nadania przy zmianie daty systemowej",39,"CHECK_PICKUP_DATE");
        put("Wymiana XML przez FTP",40,"XML_BY_FTP");
        put("Pozwolenie usunięcia przesyłek z dni poprzednich",41,"ALLOW_DEL_SHIP");
        put("Możliwość wydruku listów przewozowych w trybie manualnym na dodatek do trybów z XML i TXT",42,"PRINT_LP_MANUAL");
        put("Możliwość importu danych dla preawizacji ( SMS && Mail ) przy imporcie odbiorców",43,"IMPORT_PREAWI");
        put("Możliwość zaznaczenia zwrotu palet dla DR bez palet",44,"ALLOW_RET_PAL");
        put("Możliwość zamawiania kuriera On Line",45,"ORDERING");
        put("Możliwość nadawania przesyłek do ServicePoint ( DPST )",46,"DPST");               //36
        put("Drukowanie uwag w raporcie rozbieżności",47,"PRINT_WARNINGS");
        put("Przypisanie domyślnego MPK na poziomie operatora",48,"DEFAULT_MPK");
        put("Archiwizacja danych do katalogu ...\\DB\\ARCHIVE",49,"ARCHIVE");
        put("Wydruk BLP z kodem 2D (PDF)",50,"PDF2D");                                        //40
        put("Bez wieloelementowości DR",51,"ONE_ITEM_DR");
        put("Odczyt danych nadawcy z pliku .TXT (zaznaczone 3 lub 4 CheckBox na pierwszej zakładce)",52,"GET_SENDER_TXT");
        put("Wymuszać wprowadzanie MPK",12,"MUST_BE_MPK");
        put("Zapisywanie i odczytywanie Uwag jako domyślnych dla każdego odbiorcy",53,"DEFAULT_WARNINGS");
        put("Po wybraniu odbiorcy ustawić fokus na \"Uwagi\"",54,"FOCUS_WARNINGS");
        put("Status usługi PDI",55,"PDI");                                                    //46
        put("Wywołanie formy poprawiania kodu pocztowego dla plików wejściowych(TXT,CSV,XML,Profan...)",56,"CORRECT_POSTCODE");
        put("Przycisk \"Zagranica\" skieruj na IntraShip(zaznaczono) lub ShipNow(odznaczono)",57,"INTRASHIP_SHIPNOW");
        put("Wymuszanie wprowadzenia numeru telefonu Odbiorcy",58,"RECEIVER_TEL");            // 49
        put("Wymuszanie nadania przesyłek tylko i wyłącznie do ServicePoint ( DPST )",59,"ONLY_TO_SP");
        put("Status usługi VIP",60,"VIP");                                                    //51
        put("Wydruk LP(BLP) po naciśnięciu <ENTER> w polu ID ODBIORCY",61,"PRINT_ON_ENTER_IN_ID");
        put("Możliwość zaznachenia <Płaci odbiorca> dla wybranych klientów",62,"OG_FOR_RECEIVER");
        put("Możliwość usuwania wydruku dla powtórnego wydruku LP",63,"DELETE_PRINT");
        put("Wprowadzanie MPK dwuelementowego (w stylu CASTORAMA)",64,"CASTORAMA_MPK");
        put("Edycja odbiorcy <Ad-Hoc>",65,"EDIT_CLIENT_ADHOC");
        put("Odczyt do bufora przez przycisk \"Odśwież\"",66,"READ_BUFFER_ODSWIEZ");
        put("NIE drukować 'X' na etykiecie w przypadku nie jednoznacznej definicji trasy(projekt DataQuality)",67,"SET_X_TRACE");
        put("Nie zaznaczać OWL przy COD>5500zł.",68,"CHECK_COD5500");
        
        

        YPFunctions.FillTableFromVector(bodyTable,
                new Vector(Arrays.asList("Ustawienie","Jest u klienta", "Czy modyfikować?", "Zamienić na wartość","#")),
                tableSpecSettings,
                new Vector(Arrays.asList(150,10,10,10,5)),
                new Vector(Arrays.asList(0,1,2,3)),
                false,
                false,
                new Vector(Arrays.asList(2,3)),
                new Vector(Arrays.asList(0)),
                false,
                0);

        final Set<Integer> set = new HashSet<Integer>();
        set.addAll(Arrays.asList(5,6,7,36,40,46,49,51));
        tableSpecSettings.getColumnModel().getColumn(0).setCellRenderer(
                new YPTableCellRenderer(set, 4, java.awt.Color.RED));

        tableSpecSettings.repaint();

        
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSpecSettings = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jbCancel = new javax.swing.JButton();
        jbSaveSynchro = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(ShowSpecialSettingsDialog.class);
        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 130, -1));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableSpecSettings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableSpecSettings.setName("tableSpecSettings"); // NOI18N
        jScrollPane1.setViewportView(tableSpecSettings);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 940, 390));

        jLabel2.setBackground(resourceMap.getColor("jLabel2.background")); // NOI18N
        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 430, 270, 100));

        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jbCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 490, 100, 40));

        jbSaveSynchro.setText(resourceMap.getString("jbSaveSynchro.text")); // NOI18N
        jbSaveSynchro.setName("jbSaveSynchro"); // NOI18N
        jbSaveSynchro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveSynchroActionPerformed(evt);
            }
        });
        jPanel1.add(jbSaveSynchro, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 490, 160, 40));

        jLabel3.setBackground(resourceMap.getColor("jLabel3.background")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel3.setName("jLabel3"); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 390, 100));

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jbSaveSynchroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveSynchroActionPerformed
        // Sprawdzamy zaznaczenie choć by jednej linijki z numerami 14,15,16,17
        boolean b14 = (Boolean) tableSpecSettings.getValueAt(14, 2);
        boolean b15 = (Boolean) tableSpecSettings.getValueAt(15, 2);
        boolean b16 = (Boolean) tableSpecSettings.getValueAt(16, 2);
        boolean b17 = (Boolean) tableSpecSettings.getValueAt(17, 2);
        // Zaznaczone do synchronizacji mają być wszystkie lub ani jeden
        if ((b14 || b15 || b16 || b17) != (b14 && b15 && b16 && b17))
        {
            YPFunctions.showErrorMessage("Dane w preawizacji i notyfikacji\r\n" +
                                         "t.zn. linijki 14, 15, 16, 17\r\n" +
                                         "powinni być zaznaczane lub odznaczane\r\n" +
                                         "za jeden raz.\r\n\r\n" +
                                         "Popraw i nacisnij \"Zapisz do synchronizacji\"" +
                                         "jeszcze raz");
            YPFunctions.showMessage("<html>Dane do synchronizacji <b>NIE ZOSTAŁY</b> zapisane</html>", "");
            return;
        }



        String result = "";
        for (int i = 0; i < tableSpecSettings.getRowCount(); i++)
        {
            if (! Arrays.asList(5,6,7,36,40,46,49,51).contains(i))
            {
                 if ((Boolean) tableSpecSettings.getValueAt(i, 2)) // Zaznaczono do synchronizacji
                 {
                     String text = YPFunctions.strGet(tableSpecSettings, i, 0);
                     String value = (Boolean)tableSpecSettings.getValueAt(i, 3) ? "1" : "0";
                     result += mapResults.get(text) + "=" + value + "\r\n";
                 }
            }
        }
        if (!result.equals(""))
        {
            String s = (String)GlobalData.oraSession.executeFunction(
                       "MGR_DEFINE.F_SET_SPEC_SYNCHRO(?,?)",
                       new Vector(Arrays.asList(iIdCas,
                                                result)), 
                       OracleTypes.VARCHAR); 
            YPFunctions.showMessage("Dane do synchronizacji zostały zapisane", "");
        }
        else
        {
            YPFunctions.showErrorMessage("Nie zaznaczono żadnych danych do synchronizacji");
        }
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jbSaveSynchroActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ShowSpecialSettingsDialog dialog = new ShowSpecialSettingsDialog(new javax.swing.JFrame(), true, "", 0);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbSaveSynchro;
    private javax.swing.JTable tableSpecSettings;
    // End of variables declaration//GEN-END:variables

}
