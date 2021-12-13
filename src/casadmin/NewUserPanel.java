package casadmin;
import java.math.BigDecimal;
import java.util.*;
import oracle.jdbc.OracleTypes;

public class NewUserPanel extends javax.swing.JPanel {

    // Konstruktor z parametrem (numer SAP)
    public NewUserPanel(String p_sap) {
        initComponents();
        // Zapamietany numer SAP;
        sap = p_sap;
        // Panel SAP zrobimy nie dostępną do wpisywania
        YPFunctions.enableContainer(panelSAP, false);
        // Panele Cas i Instalacja, odwrotnie, dostępne dla wpisów
        YPFunctions.enableContainer(panelCas, true);
        YPFunctions.enableContainer(panelInst, true);
        // zabronimy edycję pola IdCas, ponieważ ID_CAS będzie uzyskany 
        // w ciągu procedury utworzenia klienta
        tfIdCas.setEnabled(false);
        // Dolną część ekranu zrobimy na razię nie widoczną, do momentu uzyskania
        // numeru Id CAS.
        panelDefinition.setVisible(false);
        panelNewIdCas.setVisible(false);
        // Wypełniamy niezbędne dane górnej części
        registerNewUser();
    }


    private void registerNewUser() {
        // Kursor oczekiwania
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        // Wypełniamy dane SAP
        String query =
                "SELECT SKROTNAZ," +
                       "NAZWA," +
                       "KOD_P," +
                       "MIASTO," +
                       "ULICA," +
                       "NR_DOMU," +
                       "NIP," +
                       "TELEPHONE " +
                "FROM customers_sap " +
                "where id_sap=?";

        Vector<String> v = GlobalData.oraSession.selectFirstRecordToVector(
                               query,
                               new Vector(Arrays.asList(Integer.parseInt(sap))));
        tfIdSAP.setText(sap);
        tfName.setText(v.elementAt(0));
        tfFullName.setText(v.elementAt(1));
        tfStreet.setText(v.elementAt(4));
        tfHouse.setText(v.elementAt(5));
        tfPostCode.setText(v.elementAt(2));
        tfCity.setText(v.elementAt(3));
        tfNIP.setText(v.elementAt(6));
        tfTelephone.setText(v.elementAt(7));

        // Kopiujemy dane z SAP do Instalacja. Dane Instalacji można modyfikować.
        // Ta możliwość nadaje się dla tego że jeden klient SAP może mieć kilka Id CAS,
        // Na przykład, Klient TESKO ( ma jeden numer SAP ) ma dużo instalacji CAS
        // w różnych miejscach Polski.
        tfInstName.setText(tfName.getText());
        tfInstStreet.setText(tfStreet.getText());
        tfInstHouse.setText(tfHouse.getText());
        tfInstPostCode.setText(tfPostCode.getText());
        tfInstCity.setText(tfCity.getText());
        tfInstTel.setText(tfTelephone.getText());
        tfInstWarnings.setText("");

        // Wypełniamy ComboBox nazwami opiekunów.
        // Opiekun - osoba ze specjalnego działu DHL, który zajmuje się 
        // spółpracą z klientami CAS

        if (GlobalData.v_allUsers.isEmpty())
                 GlobalData.v_allUsers =
                        GlobalData.oraSession.selectOneColumnToVector(
                        "Select user_name from cas_users where active='T' order by user_name",
                        null);

        comboOpiekun.removeAllItems();
        Iterator it = GlobalData.v_allUsers.iterator();
        while (it.hasNext())
               comboOpiekun.addItem((String) it.next());
        comboOpiekun.setSelectedItem(GlobalData.m_sUserLogin);
        if (Integer.parseInt(GlobalData.m_sGrant) > 2) // "USER","REPORT"
            comboOpiekun.setEnabled(false);


        // Wypełniamy ComboBox z nazwami aplikacji
        Vector <String> v_App =
                    GlobalData.oraSession.selectOneColumnToVector(
                    "Select Description from cas_dict_app order by ID_APP",
                     null);
        comboApp.removeAllItems();
        it = v_App.iterator();
        while (it.hasNext())
           comboApp.addItem((String) it.next());
        comboApp.setSelectedItem((String)v_App.elementAt(0));


        // Wypełniamy ComboBox z kodami Depotów (Terminali).
        // Firma DHL na terenie Polski ma > 50 terminali. Klienta CAS trzeba
        // przypisać do terminalu.
        if (GlobalData.v_allDepots.isEmpty())
        {
            GlobalData.v_allDepots =
                    GlobalData.oraSession.selectOneColumnToVector(
                    "Select symbol_pc from dict_depots order by symbol_pc",
                     null);
        }
        comboDepotEX.removeAllItems();
        comboDepotDR.removeAllItems();
        it = GlobalData.v_allDepots.iterator();
        while (it.hasNext()) {
            String next = (String) it.next();
           comboDepotEX.addItem(next);
           comboDepotDR.addItem(next);
        }
        
        Vector<String> vDepotLine = GlobalData.oraSession.selectFirstRecordToVector(
                "select depot, depot_dr from dict_pc where date_active=(select max(date_active) from dict_pc) and post_code=?",
                new Vector(Arrays.asList(tfPostCode.getText())));
        if (! vDepotLine.isEmpty()) {
            comboDepotEX.setSelectedItem((String)vDepotLine.elementAt(0));
            comboDepotDR.setSelectedItem((String)vDepotLine.elementAt(1));
        }

/*
        vs = GlobalData.oraSession.selectAllRecordsToVector(
                               "Select symbol_pc " +
                               "from dict_depots " +
                               "order by symbol_pc",
                          null);

        comboDepot.removeAllItems();
        it = vs.iterator();
        while (it.hasNext()) {
           Vector<String> vTemp = (Vector<String>) it.next();
           comboDepot.addItem(vTemp.elementAt(0));
        }
*/
        this.setCursor(java.awt.Cursor.getDefaultCursor());
        // Program w tym miejscu oczekuje na edycję użytkownikiem pól, dostępnych do edycji
        // i naciśnięcie przycisku "Zatwierdż"
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        upPanel = new javax.swing.JPanel();
        panelCas = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        comboOpiekun = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        comboDepotEX = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        tfKurier = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        comboRangeLP = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        tfProgName = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        comboApp = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        comboDepotDR = new javax.swing.JComboBox();
        panelSAP = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfIdSAP = new javax.swing.JTextField();
        tfFullName = new javax.swing.JTextField();
        tfStreet = new javax.swing.JTextField();
        tfPostCode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tfHouse = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tfCity = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        tfNIP = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tfTelephone = new javax.swing.JTextField();
        panelInst = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        tfInstName = new javax.swing.JTextField();
        tfInstTel = new javax.swing.JTextField();
        tfInstStreet = new javax.swing.JTextField();
        tfInstCity = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        tfInstWarnings = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        tfInstHouse = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        tfInstPostCode = new javax.swing.JTextField();
        jbConfirm = new javax.swing.JButton();
        panelNewIdCas = new javax.swing.JPanel();
        tfIdCas = new javax.swing.JTextField();
        downPanel = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        panelDefinition = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableProducts = new javax.swing.JTable();
        jbDeleteRecord = new javax.swing.JButton();
        jbEditRecord = new javax.swing.JButton();
        jbAddRecord = new javax.swing.JButton();
        panelImport = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        tfSourceIdCust = new javax.swing.JTextField();
        jbImport = new javax.swing.JButton();
        jbStartImport = new javax.swing.JButton();
        cbImportProducts = new javax.swing.JCheckBox();
        cbImportActivation = new javax.swing.JCheckBox();
        cbZleceniodawca = new javax.swing.JCheckBox();
        checkImport = new javax.swing.JCheckBox();
        panelDefault = new javax.swing.JPanel();
        checkDefault = new javax.swing.JCheckBox();
        jbDefault = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        cbServicePoint = new javax.swing.JCheckBox();
        cbPdfBlp = new javax.swing.JCheckBox();
        comboHttpMode = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        panelWOImport = new javax.swing.JPanel();
        checkWOChanges = new javax.swing.JCheckBox();
        jbWoChanges = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(NewUserPanel.class);
        upPanel.setBackground(resourceMap.getColor("upPanel.background")); // NOI18N
        upPanel.setName("upPanel"); // NOI18N
        upPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelCas.setBackground(resourceMap.getColor("panelCas.background")); // NOI18N
        panelCas.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelCas.border.title"))); // NOI18N
        panelCas.setName("panelCas"); // NOI18N
        panelCas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        panelCas.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, 20));

        comboOpiekun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboOpiekun.setName("comboOpiekun"); // NOI18N
        panelCas.add(comboOpiekun, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 160, 20));

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        panelCas.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 20));

        comboDepotEX.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboDepotEX.setName("comboDepotEX"); // NOI18N
        panelCas.add(comboDepotEX, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 50, -1));

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        panelCas.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, 20));

        tfKurier.setText(resourceMap.getString("tfKurier.text")); // NOI18N
        tfKurier.setName("tfKurier"); // NOI18N
        panelCas.add(tfKurier, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 40, 20));

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N
        panelCas.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, 20));

        comboRangeLP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2000", "3000", "5000", "10000", "20000", "30000", "50000", "100000", "200000", "500000", "1000000" }));
        comboRangeLP.setName("comboRangeLP"); // NOI18N
        panelCas.add(comboRangeLP, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 80, -1));

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N
        panelCas.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 94, -1, 30));

        tfProgName.setText(resourceMap.getString("tfProgName.text")); // NOI18N
        tfProgName.setName("tfProgName"); // NOI18N
        panelCas.add(tfProgName, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 160, -1));

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N
        panelCas.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, 20));

        comboApp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboApp.setName("comboApp"); // NOI18N
        panelCas.add(comboApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 160, 20));

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N
        panelCas.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 50, 20));

        comboDepotDR.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboDepotDR.setName("comboDepotDR"); // NOI18N
        panelCas.add(comboDepotDR, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 50, -1));

        upPanel.add(panelCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 230, 130));

        panelSAP.setBackground(resourceMap.getColor("panelSAP.background")); // NOI18N
        panelSAP.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelSAP.border.title"))); // NOI18N
        panelSAP.setEnabled(false);
        panelSAP.setName("panelSAP"); // NOI18N
        panelSAP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        panelSAP.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        panelSAP.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        panelSAP.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        panelSAP.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        tfIdSAP.setName("tfIdSAP"); // NOI18N
        panelSAP.add(tfIdSAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 70, -1));

        tfFullName.setName("tfFullName"); // NOI18N
        panelSAP.add(tfFullName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 240, -1));

        tfStreet.setName("tfStreet"); // NOI18N
        panelSAP.add(tfStreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 150, -1));

        tfPostCode.setName("tfPostCode"); // NOI18N
        panelSAP.add(tfPostCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 70, -1));

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        panelSAP.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 20, 40, -1));

        tfName.setName("tfName"); // NOI18N
        panelSAP.add(tfName, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 130, -1));

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N
        panelSAP.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 60, -1, -1));

        tfHouse.setName("tfHouse"); // NOI18N
        panelSAP.add(tfHouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 60, -1));

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N
        panelSAP.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 80, 40, -1));

        tfCity.setName("tfCity"); // NOI18N
        panelSAP.add(tfCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 130, -1));

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N
        panelSAP.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        tfNIP.setName("tfNIP"); // NOI18N
        panelSAP.add(tfNIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 70, -1));

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N
        panelSAP.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 100, 40, -1));

        tfTelephone.setName("tfTelephone"); // NOI18N
        panelSAP.add(tfTelephone, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 130, -1));

        upPanel.add(panelSAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 130));

        panelInst.setBackground(resourceMap.getColor("panelInst.background")); // NOI18N
        panelInst.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelInst.border.title"))); // NOI18N
        panelInst.setAutoscrolls(true);
        panelInst.setName("panelInst"); // NOI18N
        panelInst.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N
        panelInst.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N
        panelInst.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N
        panelInst.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 80, 30, 20));

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N
        panelInst.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        tfInstName.setName("tfInstName"); // NOI18N
        panelInst.add(tfInstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 180, -1));

        tfInstTel.setName("tfInstTel"); // NOI18N
        panelInst.add(tfInstTel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 180, -1));

        tfInstStreet.setName("tfInstStreet"); // NOI18N
        panelInst.add(tfInstStreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 100, -1));

        tfInstCity.setName("tfInstCity"); // NOI18N
        panelInst.add(tfInstCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 100, -1));

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N
        panelInst.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        tfInstWarnings.setName("tfInstWarnings"); // NOI18N
        panelInst.add(tfInstWarnings, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 180, -1));

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N
        panelInst.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        tfInstHouse.setText(resourceMap.getString("tfInstHouse.text")); // NOI18N
        tfInstHouse.setName("tfInstHouse"); // NOI18N
        panelInst.add(tfInstHouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 50, -1));

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N
        panelInst.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 60, 30, 20));

        tfInstPostCode.setText(resourceMap.getString("tfInstPostCode.text")); // NOI18N
        tfInstPostCode.setName("tfInstPostCode"); // NOI18N
        panelInst.add(tfInstPostCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 50, -1));

        upPanel.add(panelInst, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 240, 130));

        jbConfirm.setText(resourceMap.getString("jbConfirm.text")); // NOI18N
        jbConfirm.setName("jbConfirm"); // NOI18N
        jbConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConfirmActionPerformed(evt);
            }
        });
        upPanel.add(jbConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 100, 150, 40));

        panelNewIdCas.setBackground(resourceMap.getColor("panelNewIdCas.background")); // NOI18N
        panelNewIdCas.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelNewIdCas.border.title"))); // NOI18N
        panelNewIdCas.setName("panelNewIdCas"); // NOI18N
        panelNewIdCas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfIdCas.setText(resourceMap.getString("tfIdCas.text")); // NOI18N
        tfIdCas.setName("tfIdCas"); // NOI18N
        panelNewIdCas.add(tfIdCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, -1));

        upPanel.add(panelNewIdCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 10, 150, 70));

        jSplitPane1.setTopComponent(upPanel);

        downPanel.setBackground(resourceMap.getColor("downPanel.background")); // NOI18N
        downPanel.setName("downPanel"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        panelDefinition.setBackground(resourceMap.getColor("panelDefinition.background")); // NOI18N
        panelDefinition.setName("panelDefinition"); // NOI18N
        panelDefinition.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableProducts.setModel(new javax.swing.table.DefaultTableModel(
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
        tableProducts.setName("tableProducts"); // NOI18N
        jScrollPane2.setViewportView(tableProducts);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 22, 524, 130));

        jbDeleteRecord.setText(resourceMap.getString("jbDeleteRecord.text")); // NOI18N
        jbDeleteRecord.setName("jbDeleteRecord"); // NOI18N
        jbDeleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDeleteRecordActionPerformed(evt);
            }
        });
        jPanel2.add(jbDeleteRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 80, -1));

        jbEditRecord.setText(resourceMap.getString("jbEditRecord.text")); // NOI18N
        jbEditRecord.setName("jbEditRecord"); // NOI18N
        jbEditRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEditRecordActionPerformed(evt);
            }
        });
        jPanel2.add(jbEditRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 160, 70, -1));

        jbAddRecord.setText(resourceMap.getString("jbAddRecord.text")); // NOI18N
        jbAddRecord.setName("jbAddRecord"); // NOI18N
        jbAddRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddRecordActionPerformed(evt);
            }
        });
        jPanel2.add(jbAddRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 160, 70, -1));

        panelDefinition.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 540, 190));

        panelImport.setBackground(resourceMap.getColor("panelImport.background")); // NOI18N
        panelImport.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelImport.border.title"))); // NOI18N
        panelImport.setName("panelImport"); // NOI18N
        panelImport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N
        panelImport.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 90, 20));

        tfSourceIdCust.setName("tfSourceIdCust"); // NOI18N
        panelImport.add(tfSourceIdCust, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 60, -1));

        jbImport.setText(resourceMap.getString("jbImport.text")); // NOI18N
        jbImport.setName("jbImport"); // NOI18N
        jbImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbImportActionPerformed(evt);
            }
        });
        panelImport.add(jbImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 20, 20));

        jbStartImport.setText(resourceMap.getString("jbStartImport.text")); // NOI18N
        jbStartImport.setName("jbStartImport"); // NOI18N
        jbStartImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStartImportActionPerformed(evt);
            }
        });
        panelImport.add(jbStartImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 170, 30));

        cbImportProducts.setBackground(resourceMap.getColor("cbImportProducts.background")); // NOI18N
        cbImportProducts.setText(resourceMap.getString("cbImportProducts.text")); // NOI18N
        cbImportProducts.setName("cbImportProducts"); // NOI18N
        panelImport.add(cbImportProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        cbImportActivation.setBackground(resourceMap.getColor("cbImportActivation.background")); // NOI18N
        cbImportActivation.setText(resourceMap.getString("cbImportActivation.text")); // NOI18N
        cbImportActivation.setName("cbImportActivation"); // NOI18N
        panelImport.add(cbImportActivation, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        cbZleceniodawca.setBackground(resourceMap.getColor("cbZleceniodawca.background")); // NOI18N
        cbZleceniodawca.setText(resourceMap.getString("cbZleceniodawca.text")); // NOI18N
        cbZleceniodawca.setName("cbZleceniodawca"); // NOI18N
        panelImport.add(cbZleceniodawca, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        checkImport.setBackground(resourceMap.getColor("checkImport.background")); // NOI18N
        checkImport.setText(resourceMap.getString("checkImport.text")); // NOI18N
        checkImport.setName("checkImport"); // NOI18N
        checkImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkImportActionPerformed(evt);
            }
        });
        panelImport.add(checkImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 330, -1));

        panelDefinition.add(panelImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 380, 110));

        panelDefault.setBackground(resourceMap.getColor("panelDefault.background")); // NOI18N
        panelDefault.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelDefault.border.title"))); // NOI18N
        panelDefault.setName("panelDefault"); // NOI18N
        panelDefault.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        checkDefault.setBackground(resourceMap.getColor("checkDefault.background")); // NOI18N
        checkDefault.setText(resourceMap.getString("checkDefault.text")); // NOI18N
        checkDefault.setName("checkDefault"); // NOI18N
        checkDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkDefaultActionPerformed(evt);
            }
        });
        panelDefault.add(checkDefault, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 180, -1));

        jbDefault.setText(resourceMap.getString("jbDefault.text")); // NOI18N
        jbDefault.setName("jbDefault"); // NOI18N
        jbDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDefaultActionPerformed(evt);
            }
        });
        panelDefault.add(jbDefault, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 170, 30));

        panelDefinition.add(panelDefault, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 210, 380, 60));

        jPanel5.setBackground(resourceMap.getColor("jPanel5.background")); // NOI18N
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel5.border.title"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbServicePoint.setBackground(resourceMap.getColor("cbServicePoint.background")); // NOI18N
        cbServicePoint.setText(resourceMap.getString("cbServicePoint.text")); // NOI18N
        cbServicePoint.setName("cbServicePoint"); // NOI18N
        jPanel5.add(cbServicePoint, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        cbPdfBlp.setBackground(resourceMap.getColor("cbPdfBlp.background")); // NOI18N
        cbPdfBlp.setText(resourceMap.getString("cbPdfBlp.text")); // NOI18N
        cbPdfBlp.setName("cbPdfBlp"); // NOI18N
        jPanel5.add(cbPdfBlp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        comboHttpMode.setName("comboHttpMode"); // NOI18N
        jPanel5.add(comboHttpMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 350, -1));

        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N
        jPanel5.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        panelDefinition.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 540, 130));

        panelWOImport.setBackground(resourceMap.getColor("panelWOImport.background")); // NOI18N
        panelWOImport.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelWOImport.border.title"))); // NOI18N
        panelWOImport.setName("panelWOImport"); // NOI18N
        panelWOImport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        checkWOChanges.setBackground(resourceMap.getColor("checkWOChanges.background")); // NOI18N
        checkWOChanges.setText(resourceMap.getString("checkWOChanges.text")); // NOI18N
        checkWOChanges.setName("checkWOChanges"); // NOI18N
        checkWOChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkWOChangesActionPerformed(evt);
            }
        });
        panelWOImport.add(checkWOChanges, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jbWoChanges.setText(resourceMap.getString("jbWoChanges.text")); // NOI18N
        jbWoChanges.setName("jbWoChanges"); // NOI18N
        jbWoChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbWoChangesActionPerformed(evt);
            }
        });
        panelWOImport.add(jbWoChanges, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 170, 30));

        panelDefinition.add(panelWOImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 280, 380, 60));

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        panelDefinition.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 320, -1));

        jPanel1.add(panelDefinition);

        downPanel.setViewportView(jPanel1);

        jSplitPane1.setBottomComponent(downPanel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void jbConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConfirmActionPerformed
        // Powinien być wypełniony numer kuriera
        if (tfKurier.getText().trim().equals("")) {
                YPFunctions.showErrorMessage("Nie wpisany kurier");
//                JOptionPane.showMessageDialog(null, "Nie wpisany kurier","Błąd",JOptionPane.ERROR_MESSAGE);
        } else {
                // Robimy nie dostępnymi do edycji wprowadzone dane
                YPFunctions.enableContainer(panelCas, false);
                YPFunctions.enableContainer(panelInst, false);
                // Nie widoczny przycisk "Zatwierdż"
                jbConfirm.setVisible(false);
                // Odczytujemy słownikowane dane do struktur wewnętrznych
                fillProductsAndPayersMaps();
                // Wykonujemy definicję klienta.
                int iNewIdCas = executeDefinitionAndGetNewIdCas();
                if (iNewIdCas != 0)
                    fillMapIdToProduct(Integer.parseInt(tfIdCas.getText()));
                GlobalData.oraSession.SaveLog("Definicja klienta ID_CUST=" + iNewIdCas ,"");

        }
    }//GEN-LAST:event_jbConfirmActionPerformed

    private void jbDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDeleteRecordActionPerformed
        // Użytkownik chce usunąć jeden z istniejących produktów
        int row = tableProducts.getSelectedRow();
        if (row == -1) {
            YPFunctions.showErrorMessage("Nie zaznaczona linijka tablicy");
//            JOptionPane.showMessageDialog(null, "Nie zaznaczona linijka tablicy","Błąd",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String key = YPFunctions.strGet(tableProducts,row, 0) + "," +
                     YPFunctions.strGet(tableProducts,row, 1) + "," +
                     YPFunctions.strGet(tableProducts,row, 2);

        // Zapisujemy do wektora usuniętych ID
        vDeletedProductId.add(mapIdToProduct.get(key));
        // Usuwamy odpowiednia linijkę mapy mapIdToProduct
        mapIdToProduct.remove(key);
        // Korzystamy z wĹ‚asnego modelu tablicy i metody deleteRow
        ((YPTableModel)tableProducts.getModel()).deleteRow(row);
        // Odnawiamy tablicÄ™
        tableProducts.repaint();

    }//GEN-LAST:event_jbDeleteRecordActionPerformed

    private void jbEditRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditRecordActionPerformed
               // UĹĽytkownik chce modyfikowaÄ‡ istniejÄ…cy produkt
        // Sprawdzamy czy uĹĽytkownik zaznaczyĹ‚ jakÄ…Ĺ› linijkÄ™ tablicy
        int row = tableProducts.getSelectedRow();
        if (row == -1) {
            YPFunctions.showErrorMessage("Nie zaznaczona linijka tablicy");
//            JOptionPane.showMessageDialog(null, "Nie zaznaczona linijka tablicy","Błąd",JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Odczytujemy linijkÄ™ tablicy do wektora
        Vector rowVector = new Vector();
        for (int i = 0; i < tableProducts.getColumnCount(); i++)
            rowVector.add(YPFunctions.strGet(tableProducts,row, i));
        // To samo moĹĽna byĹ‚o by zrobiÄ‡ tak :
        // Vector rowVector = ((DefaultTableModel)tableProducts.getModel()).getDataVector().elementAt(row);

        // WywoĹ‚ujemy dialog edycji produktu
        EditProductDialog frame = new EditProductDialog(null, true, rowVector, false);
        frame.setVisible(true);
        // JeĹ›li uĹĽytkownik wycofaĹ‚ sie z procedury edycji
        if (frame.getReturnStatus() == frame.RET_CANCEL) {
                // Wycofujemy sie z procedury
                return;
        }
        // Inaczej podmieniamy linijkÄ™ z numerem row tablicy tableProducts
        // na przygotowany w dialogu EditProductDialog wektor retVector

        // Korzystamy z wĹ‚asnego modelu tablicy i metody updateRow
        ((YPTableModel)tableProducts.getModel()).updateRow(frame.retVector,row);
        // Odnawiamy tablicÄ™
        tableProducts.repaint();
    }//GEN-LAST:event_jbEditRecordActionPerformed

    private void jbAddRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddRecordActionPerformed
            // UĹĽytkownik chce dodaÄ‡ produkt do istniejÄ…cych produktĂłw
        // WywoĹ‚ujemy dialog edycji produktu
        // W konstruktorze przekazujemy w jakoĹ›ci wektora null, to znaczy ĹĽe my
        // chcemy dodaÄ‡ produkt
        EditProductDialog frame = new EditProductDialog(null, true, null, true);
        frame.setVisible(true);
        // JeĹ›li uĹĽytkownik wycofaĹ‚ sie z procedury edycji
        if (frame.getReturnStatus() == frame.RET_CANCEL) {
                // Wycofujemy sie z procedury
                return;
        }
        // Inaczej dodajemy do tablicy przygotowany w dialogu EditProductDialog
        // wektor retVector

        // Dopisujemy do mapy mapIdToProduct odpowiednia klucz z parametrem "0",
        // dla tego że nie wiemy, jaki ID dostanie ten wpis
        // Jeśli ten klucz już istnieje, on się nie dopisze
        String key = frame.retVector.elementAt(0) + "," +
                     frame.retVector.elementAt(1) + "," +
                     frame.retVector.elementAt(2);
        if (!mapIdToProduct.containsKey(key)) {
            mapIdToProduct.put(key, "0");
            // Korzystamy z wł‚asnego modelu tablicy i metody appendRow
            ((YPTableModel)tableProducts.getModel()).appendRow(frame.retVector);
            // Odnawiamy tablicę
            tableProducts.repaint();
        }
    }//GEN-LAST:event_jbAddRecordActionPerformed

    private void jbImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImportActionPerformed
                   // NaciĹ›niÄ™ty przycisk "..."
            // WyĹ›wietlamy dialog dla zaznaczenia pojedynczego ID CUST,
            // ktĂłry bÄ™dzie sĹ‚uĹĽyÄ‡ ĹĽrĂłdĹ‚em importu.
            // Jasne, ĹĽe wĹ›rĂłd wyĹ›wietlonych Id Cust nie powinno byÄ‡
            // docelowego ID CUST
            SelectCustomerDialog frame = new SelectCustomerDialog(null, true, true, "","");
            frame.setTitle("Zaznacz ID_CUST jednego klienta, definicję którego chcesz skopiować");
            frame.setVisible(true);
            if (tfIdCas.getText().equals(((SelectCustomerDialog)frame).selectedID_CUST)){
                YPFunctions.showBigErrorMessage("Nie można wybrać za źródło docelowy ID CAS, wybierz inny");
//                JOptionPane.showMessageDialog(null, "Nie można wybrać za źródło docelowy ID CAS, wybierz inny","Błąd",JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Wyś›wietlamy wybrany ID CUST
            tfSourceIdCust.setText(((SelectCustomerDialog)frame).selectedID_CUST);
            tfSourceIdCust.repaint();
    }//GEN-LAST:event_jbImportActionPerformed

    private void jbStartImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbStartImportActionPerformed
                    
        // Naciśnięty przycisk "Importuj"
            // Sprawdzamy poprawność ID CUST
            String sourceIdCust = tfSourceIdCust.getText();
            if ((sourceIdCust.length() < 4) || (sourceIdCust.length() > 6)) {
                   YPFunctions.showErrorMessage("Nie prawidłowy numer źródłowego Id Cust");
//                   JOptionPane.showMessageDialog(null, "Nie prawidłowy numer źródłowego Id Cust","Błąd",JOptionPane.ERROR_MESSAGE);
                   return;
            }
            // Wykonujemy właśnie import
            YPFunctions.enableContainer(panelImport, false);
            YPFunctions.enableContainer(panelDefault, false);

            executeImport();
            // Pokazujemy klienta jako istniejącego
            showDetail();
    }//GEN-LAST:event_jbStartImportActionPerformed


    private void checkImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkImportActionPerformed
        bImpSelected = ! bImpSelected;
        bDefaultSelected = ! bImpSelected;
        bWOChangesSelected = ! bImpSelected;
        refreshImportDefault();
    }//GEN-LAST:event_checkImportActionPerformed

    private void checkDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkDefaultActionPerformed
        bDefaultSelected = ! bDefaultSelected;
        bImpSelected = ! bDefaultSelected;
        bWOChangesSelected = ! bDefaultSelected;
        refreshImportDefault();
    }//GEN-LAST:event_checkDefaultActionPerformed

    private void jbDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDefaultActionPerformed
            YPFunctions.enableContainer(panelImport, false);
            YPFunctions.enableContainer(panelDefault, false);
            // Wykonujemy ustawienie cenników domyślnych
            executeDefault();
            // Pokazujemy klienta jako istniejącego
            showDetail();

    }//GEN-LAST:event_jbDefaultActionPerformed

    private void checkWOChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkWOChangesActionPerformed
        bWOChangesSelected = ! bWOChangesSelected;
        bImpSelected = ! bWOChangesSelected;
        bDefaultSelected = ! bWOChangesSelected;
        refreshImportDefault();

    }//GEN-LAST:event_checkWOChangesActionPerformed

    private void jbWoChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbWoChangesActionPerformed
            // Pokazujemy klienta
            showDetail();

    }//GEN-LAST:event_jbWoChangesActionPerformed

    private void showDetail()
    {
                 CasAdminApp.getApplication().cav.showPanel(new UserDetailPanel(tfIdCas.getText(),
                   tfIdSAP.getText(),
                   (String)comboDepotEX.getSelectedItem(),
                   new Date()),Constants.gifStyle.read);
    }


    public static void fillProductsAndPayersMaps() {
         // Mamy pięć słownikowanuch tablic - zapisujemy do jednego wektora i
         // czterech map
         vProducts = new Vector<String>();
         mapPrices = new HashMap<String, String>();
         mapPlatnik = new HashMap<String, String>();
         mapPlatnosc = new HashMap<String, String>();
         mapYesNo = new HashMap<String, String>();

         // tablica dict_prices - słownik ma wygląd
         // S     STANDARDOWY    - cennik standardowy
         // X     SPECJALNY      - cennik specjalny
         Vector<Vector<String> > v = 
                 GlobalData.oraSession.selectAllRecordsToVector(
                         "select id_symbol," +
                                "descr " +
                         "from dict_prices", null);
         Iterator it = v.iterator();
         while (it.hasNext()) {
           Vector<String> vTemp = (Vector<String>) it.next();
           mapPrices.put(vTemp.elementAt(1), vTemp.elementAt(0));
         }

         // Tablica DICT_PAY
         // N NADAWCA
         // O ODBIORCA
         // Z ZLECENIODAWCA
         // ...
         v = GlobalData.oraSession.selectAllRecordsToVector(
                            "select id_pay," +
                                    "description " +
                            "from dict_pay", null);
         it = v.iterator();
         while (it.hasNext()) {
           Vector<String> vTemp = (Vector<String>) it.next();
           mapPlatnik.put(vTemp.elementAt(1), vTemp.elementAt(0));
         }

         // Tablica DICT_PAYABLE
         // P  PRZELEW - płatność przelewem
         // G  GOTÓWKA - gotówka
         // K  KARTA   - płatność kartą
         // -  BRAK OPŁATY - bezpłatna, służy dla przesyłania przesyłek między oddziałami samego DHL
         v = GlobalData.oraSession.selectAllRecordsToVector(
                                  "select id_payable," +
                                         "description " +
                                  "from dict_payable", null);
         it = v.iterator();
         while (it.hasNext()) {
           Vector<String> vTemp = (Vector<String>) it.next();
           mapPlatnosc.put(vTemp.elementAt(1), vTemp.elementAt(0));
         }

         // Tablica DICT_YES_NO
         // T JEST
         // N BRAK
         v = GlobalData.oraSession.selectAllRecordsToVector(
                                   "select id_symbol," +
                                           "descr " +
                                   "from dict_yes_no", null);
         it = v.iterator();
         while (it.hasNext()) {
           Vector<String> vTemp = (Vector<String>) it.next();
           mapYesNo.put(vTemp.elementAt(1), vTemp.elementAt(0));
         }
         // Tablica DICT_PRODUCTS
         // Dostępne produkty
         v = GlobalData.oraSession.selectAllRecordsToVector(
                                    "select id_product " +
                                    "from dict_products", null);
         it = v.iterator();
         while (it.hasNext()) {
           String sTemp = (String)((Vector)it.next()).elementAt(0);
           vProducts.add(sTemp);
         }
    }

    public static void fillMapIdToProduct(int iIdCas) {
                 mapIdToProduct = new HashMap<String, String>();
                 vDeletedProductId = new Vector<String>();
                 // Dla już zdefiniowanych klientów wypełniamy mapę relacji
                 // Mapa relacji Id zdefinoowanego produktu do nazwy produktu,
                 // będzie używać się przy operacjach Update, Insert i Delete
                 Vector<Vector<String> > v = GlobalData.oraSession.selectAllRecordsToVector(
                        "select S.ID_SERVICES," +
                               "v.ID_PRODUCT || ',' || v.des_pay || ',' || v.des_payable " +
                        "from v_dict_services v, dict_services s " +
                        "where v.id_cust=? " +
                              "and v.id_cust=s.id_cust " +
                              "and v.id_product=s.id_product " +
                              "and v.id_pay=s.id_pay",
                              new Vector(Arrays.asList(iIdCas)));
                 Iterator it = v.iterator();
                 while (it.hasNext()) {
                   Vector<String> vTemp = (Vector<String>) it.next();
                   mapIdToProduct.put(vTemp.elementAt(1), vTemp.elementAt(0));
                 }
    }



    private void executeImport() {
        // Kursor oczekiwania
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        // Najpierw zapisujemy definicję produktów - zdefiniowanych lub importowanych z innego ID CAS
        if (cbImportProducts.isSelected()) {
                // Wykonujemy proceduru IMPORT_OTHERS
                       GlobalData.oraSession.executeProcedure(
                             "MGR_DEFINE.IMPORT_OTHERS(?,?,?)",
                             new Vector(Arrays.asList(
                                           Integer.parseInt(tfIdCas.getText()),
                                           Integer.parseInt(tfSourceIdCust.getText()),
                                           "S")));
        } else
        {
            if (!getDefinedProductsAndWriteToDataBase(tableProducts, tfIdCas.getText())) {
                 this.setCursor(java.awt.Cursor.getDefaultCursor());
                 YPFunctions.showErrorMessage("Nie przedwidziany błąd.\nZgłoś błąd do autora aplikacji");
                 return;
            }
        }
        // Wektor płatników
        Vector<String> vPlatnik =
                 GlobalData.oraSession.selectOneColumnToVector(
                      "select distinct id_pay from dict_services where id_cust=?",
                      new Vector(Arrays.asList(Integer.parseInt(tfIdCas.getText()))));

        // Dla każdego płatnika wykonujemy ciąg procedur
        for (int i=0; i<vPlatnik.size(); i++) {
            // Parametry jednakowe dla wszystkich procedur
            Vector pars = new Vector(Arrays.asList(
                                   Integer.parseInt(tfIdCas.getText()),
                                   Integer.parseInt(tfSourceIdCust.getText()),
                                   vPlatnik.elementAt(i)));
            // Wykonujemy procdury
            GlobalData.oraSession.executeProcedure(
                     "MGR_DEFINE.IMPORT_EX(?,?,?)", pars);
            GlobalData.oraSession.executeProcedure(
                     "MGR_DEFINE.IMPORT_EX_RAB(?,?,?)", pars);
            GlobalData.oraSession.executeProcedure(
                     "MGR_DEFINE.IMPORT_DR(?,?,?)", pars);
            GlobalData.oraSession.executeProcedure(
                     "MGR_DEFINE.IMPORT_PAL(?,?,?)", pars);
            GlobalData.oraSession.executeProcedure(
                     "MGR_DEFINE.IMPORT_PAL_RAB(?,?,?)", pars);
        }

        // Tworzymy ciąg z zaznaczonymi opcjami dodatkowymi, tak jak jest
        // to wymagane dla uruchomienia procedury IMPORT_OTHERS
        String parameter = "";
        if (cbImportActivation.isSelected())
            parameter += "A";
        if (cbZleceniodawca.isSelected())
            parameter += "Z";
        // Wykonujemy proceduru IMPORT_OTHERS
        if (!parameter.isEmpty()) {
               GlobalData.oraSession.executeProcedure(
                     "MGR_DEFINE.IMPORT_OTHERS(?,?,?)",
                     new Vector(Arrays.asList(
                                   Integer.parseInt(tfIdCas.getText()),
                                   Integer.parseInt(tfSourceIdCust.getText()),
                                   parameter)));
        }


        setOptions();
        // Przywracamy domyślny kursor
        this.setCursor(java.awt.Cursor.getDefaultCursor());
        // Uwaga : Nie wiemy czy procedury były wykonane prawidłowo,
        // tak jak nie ma w procedurach parametrów wyjściowych, które
        // sygnalizowały by o błędach, i zostały zrealizowane jako procedury,
        // a nie jako funkcję, gdzie też można było by zwrócić wynik.
        // Nie możemy na danym etapie modyfikować procedur PL/SQL,
        // w przyszłości będą wykonane zmiany.

        // Powiadomienie dla użytkownika
        YPFunctions.showMessage("Import został wykonany","Import wykonany");
    }

    public static boolean getDefinedProductsAndWriteToDataBase(javax.swing.JTable p_table, String p_id_cust) {
//            public static Map<String, String> mapIdToProduct;
//    public static Vector<String> vDeletedProductId;

        int iIdCas = Integer.parseInt(p_id_cust);
        // Usuwamy zdefiniowane prodykty, wcześniej usunięte z tablicy
        // Lista ID usuniętych produktów zapisana w wektorze vDeletedProductId
        Iterator it = vDeletedProductId.iterator();
        while (it.hasNext()) {
            int id = Integer.parseInt((String)it.next());
            GlobalData.oraSession.executeProcedure("MGR_DEFINE.DELETE_SERVICES(?,?)",
                    new Vector(Arrays.asList(iIdCas,id)));
        }
        // W mapie mapIdToProduct znachodzą się produkty modyfikowane lub dopisane.
        // Dla modyfikowanych prodyktów ID != 0 - zastosujemy Update,
        // a dla dodanych produktów ID=0 - wykonujemy INSERT

        it = mapIdToProduct.keySet().iterator();
        while (it.hasNext()) {
             String key = (String) it.next();
             int id = Integer.parseInt(mapIdToProduct.get(key));
             // Odszukujemy linijkę tablicy tableProducts dla odzyskania dodatkowych parametrów
             int i;
             for (i = 0; i < p_table.getRowCount(); i++) {
                 if (key.equals(YPFunctions.strGet(p_table,i, 0) + "," +
                                YPFunctions.strGet(p_table,i, 1) + "," +
                                YPFunctions.strGet(p_table,i, 2)))
                     break;
             }
             // Zabezpieczamy się przed nie prawidłowym działaniem algorytmu,
             // t.zn. jeśli nie możemy znaleść odpowiednika w tablica dla mapy
             if (i == p_table.getRowCount()) {
                    return false;
             }
             if (id == 0) {
                    // INSERT_SERVICES(nIDCust NUMBER,
                    //        vIDPay VARCHAR2, vIDProduct VARCHAR2, vIDPayAble VARCHAR2,
                    //         vTypeEX VARCHAR2, vTypeDR VARCHAR2, vTypePal VARCHAR2)
                    GlobalData.oraSession.executeProcedure("MGR_DEFINE.INSERT_SERVICES(?,?,?,?,?,?,?)",
                            new Vector(Arrays.asList(iIdCas,
                                                     mapPlatnik.get(YPFunctions.strGet(p_table,i, 1)),
                                                     YPFunctions.strGet(p_table,i, 0),
                                                     mapPlatnosc.get(YPFunctions.strGet(p_table,i, 2)),
                                                     mapPrices.get(YPFunctions.strGet(p_table,i, 3)),
                                                     mapPrices.get(YPFunctions.strGet(p_table,i, 4)),
                                                     mapYesNo.get(YPFunctions.strGet(p_table,i, 5)))));
             } else {
                    //PROCEDURE UPDATE_SERVICES(nIDCust NUMBER, nIDServices NUMBER,
                    //        vIDPay VARCHAR2, vIDProduct VARCHAR2, vIDPayAble VARCHAR2,
                    //         vTypeEX VARCHAR2, vTypeDR VARCHAR2, vTypePal VARCHAR2) IS

                    GlobalData.oraSession.executeProcedure("MGR_DEFINE.UPDATE_SERVICES(?,?,?,?,?,?,?,?)",
                            new Vector(Arrays.asList(iIdCas,
                                                     id,
                                                     mapPlatnik.get(YPFunctions.strGet(p_table,i, 1)),
                                                     YPFunctions.strGet(p_table,i, 0),
                                                     mapPlatnosc.get(YPFunctions.strGet(p_table,i, 2)),
                                                     mapPrices.get(YPFunctions.strGet(p_table,i, 3)),
                                                     mapPrices.get(YPFunctions.strGet(p_table,i, 4)),
                                                     mapYesNo.get(YPFunctions.strGet(p_table,i, 5)))));
             }


        }
        return true;
    }
    private void executeDefault() {
        // Kursor oczekiwania
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

        if (!getDefinedProductsAndWriteToDataBase(tableProducts, tfIdCas.getText())) {
            this.setCursor(java.awt.Cursor.getDefaultCursor());
            YPFunctions.showErrorMessage("Nie przedwidziany błąd.\nZgłoś błąd do autora aplikacji");
            return;
        }

        // Wykomujemy zapisywanie słowników domyślnych
        String s = (String)GlobalData.oraSession.executeFunction(
                   "MGR_DEFINE.f_insertNewCustomer(?)",
                   new Vector(Arrays.asList(Integer.parseInt(tfIdCas.getText()))),
                   OracleTypes.VARCHAR);
        if (! s.equals("OK")) {
           this.setCursor(java.awt.Cursor.getDefaultCursor());
           YPFunctions.showErrorMessage("Nie udało się wykonać ustawienie słowników [" + s + "]");
//           JOptionPane.showMessageDialog(null, "Nie udało się wykonać ustawienie słowników [" + s + "]" ,"Błąd",JOptionPane.ERROR_MESSAGE);
           return;
        }
        // Dodatkowe opcję
        setOptions();
        // Przywracamy domyślny kursor
        this.setCursor(java.awt.Cursor.getDefaultCursor());

        // Powiadomienie dla użytkownika
        YPFunctions.showMessage("Ustawienie słowników zostało wykonane","");
    }

    private void setOptions() {
        String queryActivation = "Update customers set " +
                  "type_serv_point=?,type_pdf=?,type_comm=? where id_cust=?";
        Vector pars = new Vector(Arrays.asList(
                YPFunctions.bool2str((Boolean)cbServicePoint.isSelected()),
                YPFunctions.bool2str((Boolean)cbPdfBlp.isSelected()),
                comboHttpMode.getSelectedIndex() + 1,
                tfIdCas.getText()));
        GlobalData.oraSession.executeQuery(queryActivation,pars);
    }


    private int executeDefinitionAndGetNewIdCas() {
        // Kursor oczekiwania
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

        // Odczytujemy numer wybranego opiekuna
        int opiekun = Integer.parseInt(
                     GlobalData.oraSession.selectString(
                                     "SELECT id_user " +
                                     "from cas_users " +
                                     "where user_name=?",
                new Vector(Arrays.asList((String)comboOpiekun.getSelectedItem())),
                "0"));
        
        int app = Integer.parseInt(GlobalData.oraSession.selectString(
                                     "SELECT id_app " +
                                     "from cas_Dict_app " +
                                     "where description=?",
                new Vector(Arrays.asList((String)comboApp.getSelectedItem())),
                "0"));

//  FUNCTION F_INSERT_CUSTOMER(nIDUser NUMBER,
//                        nIDSAP NUMBER, vDepotEX VARCHAR2, vDepotDR VARCHAR2, nIDApp NUMBER, vAppVersion VARCHAR2,
//                          nKurier NUMBER, vProgramName VARCHAR2, nRangeLp NUMBER) return NUMBER;
        
        
        int id_cust = ((BigDecimal) GlobalData.oraSession.executeFunction(
            "MGR_DEFINE.F_INSERT_CUSTOMER(?,?,?,?,?,?,?,?,?)",
                            new Vector(Arrays.asList(
                           opiekun,
                           Integer.parseInt(sap),
                           (String)comboDepotEX.getSelectedItem(),
                           (String)comboDepotDR.getSelectedItem(),
                           app,
                           "",
                           Integer.parseInt(tfKurier.getText()),
                           tfProgName.getText(),
                           Integer.parseInt((String)comboRangeLP.getSelectedItem())
                           )),
            OracleTypes.NUMBER)).intValue();
        if (id_cust == 0) { // nie udała się próba założenia klienta
            YPFunctions.showErrorMessage("Nie udało się założyć nowego klienta.\nSkontaktuj się z autorem aplikacji");
            return 0;
        }
/*




        // Wykonujemy procedurę Oracle INSERT_CUST założenia nowego klienta Cas.
        // MGR_DEFINE - nazwa pakietu w bazie danych Oracle.
        boolean executeProcedure =
             GlobalData.oraSession.executeProcedure(
                "MGR_DEFINE.INSERT_CUST(?,?,?,?,?,?,?,?)",
                new Vector(Arrays.asList(
                           opiekun,
                           Integer.parseInt(sap),
                           (String)comboDepot.getSelectedItem(),
                           app,
                           "",
                           Integer.parseInt(tfKurier.getText()),
                           tfProgName.getText(),
                           Integer.parseInt((String)comboRangeLP.getSelectedItem())
                           )));

        // Jeśli nie udało się wykonać procedury
        if (!executeProcedure) {
                // Przywracamy domyślny kursor
                this.setCursor(java.awt.Cursor.getDefaultCursor());
                YPFunctions.showErrorMessage("Nie udało się utworzyć nowego klienta");
//                JOptionPane.showMessageDialog(null, "Nie udało się utworzyć nowego klienta","Błąd",JOptionPane.ERROR_MESSAGE);
                this.setVisible(false);
                return 0;
        }

        // Procedura wykonała się, nowy ID CAS został założony.
        // Szkoda, że procedura nie napisana jako funkcja, zwracająca numer założonego Id Cust.
        // Żeby uzyskać założony Id Cust, musimy wykonać zapytanie SQL.
        int id_cust = Integer.parseInt(GlobalData.oraSession.selectString(
                "SELECT max(id_cust) " +
                "from customers where id_cust <> 15556", null, "0"));
*/
        // Wyświetlamy nowy ID CUST
        panelNewIdCas.setVisible(true);
        tfIdCas.setText(""+id_cust);

/*
        // Zapisujemy zawartość paneli Instalacja do odpowiedniej tablicy Oracle
        String  query = "Insert into customers_install(ID_CUST,NAME,STREET,STREETNO,ZIPCODE,CITY,PHONE) values(?,?,?,?,?,?,?)";
        Vector pars = new Vector(Arrays.asList(
                        Integer.parseInt(tfIdCas.getText()),
                        tfInstName.getText(),
                        tfInstStreet.getText(),
                        tfInstHouse.getText(),
                        tfInstPostCode.getText(),
                        tfInstCity.getText(),
                        tfInstTel.getText()));
        GlobalData.oraSession.executeQuery(query,pars);

        // Zapisujemy zawartość pola uwagi do tablicy Oracle Customers_memo
        query = "Insert into customers_memo(ID_CUST,MEMO) values(?,?)";
        pars = new Vector(Arrays.asList(
                        Integer.parseInt(tfIdCas.getText()),
                        tfInstWarnings.getText()));
        GlobalData.oraSession.executeQuery(query,pars);
*/        
        // Zapisujemy zawartość paneli Instalacja do odpowiedniej tablicy Oracle
        // Zapisujemy zawartość pola uwagi do tablicy Oracle Customers_memo
        String s = (String)GlobalData.oraSession.executeFunction(
                   "MGR_DEFINE.F_INSERT_CUSTOMERS_INSTALL(?,?,?,?,?,?,?,?)",
                   new Vector(Arrays.asList(
                        Integer.parseInt(tfIdCas.getText()),
                        tfInstName.getText(),
                        tfInstStreet.getText(),
                        tfInstHouse.getText(),
                        tfInstPostCode.getText(),
                        tfInstCity.getText(),
                        tfInstTel.getText(),
                        tfInstWarnings.getText())), 
                   OracleTypes.VARCHAR); 


        // Robimy widoczną dolną część ekranu
        panelDefinition.setVisible(true);
        
        // Wypełniamy tablicę zdefiniowanych produktów i serwisów
        String query = "select ID_PRODUCT as \"Produkt\"," +
                       "des_pay as \"Platnik\"," +
                       "des_payable as \"Platnosc\"," +
                       "descr_ex as \"Ekspres\"," +
                       "descr_dr as \"Drobnica\"," +
                       "descr_pal as \"Palety\" "+
                "from v_dict_services " +
                "where id_cust=?";
        Vector pars = new Vector(Arrays.asList(Integer.parseInt(tfIdCas.getText())));

        YPFunctions.FillTableFromOra(query,
                                     pars,
                                     0,
                                     99999,
                                     tableProducts,
                                     new Vector(),
                                     new Vector(),
                                     false,
                                     false,
                                     new Vector(),
                                     null,
                                     false);

        Vector<Vector<String> > v =
                    GlobalData.oraSession.selectAllRecordsToVector(
                      "Select descr || ' ' || addr from cas_dict_web order by id",
                      null);
        comboHttpMode.removeAllItems();
        Iterator it = v.iterator();
        while (it.hasNext()) {
                Vector<String> vTemp = (Vector<String>) it.next();
                comboHttpMode.addItem(vTemp.elementAt(0));
        }
        if (!v.isEmpty())
            comboHttpMode.setSelectedIndex(0);
        bImpSelected = false;
        bDefaultSelected = false;
        bWOChangesSelected = false;
        refreshImportDefault();

        // Przywracamy domyślny kursor
        this.setCursor(java.awt.Cursor.getDefaultCursor());
        // Program oczekuje na edycję dostępnych produktów i serwisów za pomocą
        // przycisków "Dodaj", "Usuń", "Edytuj".
        // Także wymagany jest wybór istniejącego klienta CAS w jakości żrodłowego -
        // trzeba wcisnąć przycisk "...", a póżniej - nacisnięcie "Importuj".
        // Dodatkowo możma zaznaczyć dodatkowe opcję, wyświetlone jako CheckBoxy
        return id_cust;
    }

    private void refreshImportDefault() {

        YPFunctions.enableContainer(panelImport, bImpSelected);
        YPFunctions.enableContainer(panelDefault, bDefaultSelected);
        YPFunctions.enableContainer(panelWOImport, bWOChangesSelected);

        checkImport.setEnabled(true);
        checkImport.setSelected(bImpSelected);
        checkDefault.setEnabled(true);
        checkDefault.setSelected(bDefaultSelected);
        checkWOChanges.setEnabled(true);
        checkWOChanges.setSelected(bWOChangesSelected);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbImportActivation;
    private javax.swing.JCheckBox cbImportProducts;
    private javax.swing.JCheckBox cbPdfBlp;
    private javax.swing.JCheckBox cbServicePoint;
    private javax.swing.JCheckBox cbZleceniodawca;
    private javax.swing.JCheckBox checkDefault;
    private javax.swing.JCheckBox checkImport;
    private javax.swing.JCheckBox checkWOChanges;
    private javax.swing.JComboBox comboApp;
    private javax.swing.JComboBox comboDepotDR;
    private javax.swing.JComboBox comboDepotEX;
    private javax.swing.JComboBox comboHttpMode;
    private javax.swing.JComboBox comboOpiekun;
    private javax.swing.JComboBox comboRangeLP;
    private javax.swing.JScrollPane downPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton jbAddRecord;
    private javax.swing.JButton jbConfirm;
    private javax.swing.JButton jbDefault;
    private javax.swing.JButton jbDeleteRecord;
    private javax.swing.JButton jbEditRecord;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbStartImport;
    private javax.swing.JButton jbWoChanges;
    private javax.swing.JPanel panelCas;
    private javax.swing.JPanel panelDefault;
    private javax.swing.JPanel panelDefinition;
    private javax.swing.JPanel panelImport;
    private javax.swing.JPanel panelInst;
    private javax.swing.JPanel panelNewIdCas;
    private javax.swing.JPanel panelSAP;
    private javax.swing.JPanel panelWOImport;
    private javax.swing.JTable tableProducts;
    private javax.swing.JTextField tfCity;
    private javax.swing.JTextField tfFullName;
    private javax.swing.JTextField tfHouse;
    private javax.swing.JTextField tfIdCas;
    private javax.swing.JTextField tfIdSAP;
    private javax.swing.JTextField tfInstCity;
    private javax.swing.JTextField tfInstHouse;
    private javax.swing.JTextField tfInstName;
    private javax.swing.JTextField tfInstPostCode;
    private javax.swing.JTextField tfInstStreet;
    private javax.swing.JTextField tfInstTel;
    private javax.swing.JTextField tfInstWarnings;
    private javax.swing.JTextField tfKurier;
    private javax.swing.JTextField tfNIP;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfPostCode;
    private javax.swing.JTextField tfProgName;
    private javax.swing.JTextField tfSourceIdCust;
    private javax.swing.JTextField tfStreet;
    private javax.swing.JTextField tfTelephone;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables
    public String sap;

    public static Vector<String> vProducts;
    public static Map<String, String> mapPlatnik;
    public static Map<String, String> mapPlatnosc;
    public static Map<String, String> mapPrices;
    public static Map<String, String> mapYesNo;
    public static Map<String, String> mapIdToProduct;
    public static Vector<String> vDeletedProductId;
    boolean bImpSelected;
    boolean bDefaultSelected;
    boolean bWOChangesSelected;
}
