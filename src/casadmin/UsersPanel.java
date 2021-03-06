/*
 * UsersPanel.java
 *
 * Created on 31 październik 2008, 09:48
 */
package casadmin;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.*;


// Klasa UsersPanel jest przyznaczona dla filtrowania, wyszukiwania
// i wyświetlenia danych zarejestrowanych klientów CAS
public class UsersPanel extends javax.swing.JPanel {

    /** Creates new form UsersPanel */
    // Konstruktor
    public UsersPanel() {
        initComponents();
        // iStartRecord - numer rekordu od którego zaczyna się przeglądanie listy
        // użytkowników. Jest dla tego, że istnieje możliwość przeglądania danych
        // częściami (po 50, 100 czy inną ilość na stronie).
        iStartRecord = 0;
          // Ustawiamy początkowe zawartości
//        dcOdDaty.setDate(GlobalData.startShipmentsDate);

        Date d = new Date();
        d.setYear(d.getYear());
        d.setMonth(d.getMonth()-2);
        d.setDate(1);  
        dcOdDaty.setDate(d);

//        Date d = new Date();
//        d.setYear(d.getYear()-3);
//        d.setMonth(Calendar.JANUARY);
//        d.setDate(1);  // 3 lata temu od początku roku
//        dcOdDaty.setDate(d);
        tfTerminal.setText("");
        tfIdCas.setText("");
        tfSeek.setText("");
        // Wypełniamy ComboBox nazwami opiekunów.
        // Opiekun - osoba ze specjalnego działu DHL, który zajmuje się
        // spółpracą z klientami CAS

        if (GlobalData.v_allUsers.isEmpty())
                 GlobalData.v_allUsers =
                        GlobalData.oraSession.selectOneColumnToVector(
                        "Select user_name from cas_users where active='T' order by user_name",
                        null);

        comboOpiekun.removeAllItems();
        comboOpiekun.addItem("*.*");
        Iterator it = GlobalData.v_allUsers.iterator();
        while (it.hasNext())
               comboOpiekun.addItem((String) it.next());
        comboOpiekun.setSelectedItem("*.*");

//        comboOpiekun.setSelectedItem(GlobalData.m_sUserLogin);

        // "Zerujemy" dolną część ekranu
        YPFunctions.clearContainer(downPanel);
        table.setRowSelectionAllowed(true);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        tfIdCas = new javax.swing.JTextField();
        jbShowCustomers = new javax.swing.JButton();
        jLabel67 = new javax.swing.JLabel();
        tfTerminal = new javax.swing.JTextField();
        jbShow = new javax.swing.JButton();
        jbPrevRecords = new javax.swing.JButton();
        jbNextRecords = new javax.swing.JButton();
        cbRecCount = new javax.swing.JComboBox();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listFilter = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        dcOdDaty = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        comboOpiekun = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        checkShowAddress = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jbSeek = new javax.swing.JButton();
        tfSeek = new javax.swing.JTextField();
        jbFilter = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        checkFromStart = new javax.swing.JCheckBox();
        downPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(UsersPanel.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setName("Form"); // NOI18N
        setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        upPanel.setBackground(resourceMap.getColor("upPanel.background")); // NOI18N
        upPanel.setName("upPanel"); // NOI18N
        upPanel.setPreferredSize(new java.awt.Dimension(800, 52));
        upPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel66.setText(resourceMap.getString("jLabel66.text")); // NOI18N
        jLabel66.setName("jLabel66"); // NOI18N
        jPanel2.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 60, 20));

        tfIdCas.setName("tfIdCas"); // NOI18N
        tfIdCas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfIdCasKeyReleased(evt);
            }
        });
        jPanel2.add(tfIdCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 100, 20));

        jbShowCustomers.setText(resourceMap.getString("jbShowCustomers.text")); // NOI18N
        jbShowCustomers.setName("jbShowCustomers"); // NOI18N
        jbShowCustomers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbShowCustomersActionPerformed(evt);
            }
        });
        jPanel2.add(jbShowCustomers, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 40, -1));

        jLabel67.setText(resourceMap.getString("jLabel67.text")); // NOI18N
        jLabel67.setName("jLabel67"); // NOI18N
        jPanel2.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 50, 20));

        tfTerminal.setName("tfTerminal"); // NOI18N
        tfTerminal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTerminalMouseClicked(evt);
            }
        });
        jPanel2.add(tfTerminal, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 100, -1));

        jbShow.setText(resourceMap.getString("jbShow.text")); // NOI18N
        jbShow.setName("jbShow"); // NOI18N
        jbShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbShowActionPerformed(evt);
            }
        });
        jPanel2.add(jbShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 43, 110, 30));

        jbPrevRecords.setText(resourceMap.getString("jbPrevRecords.text")); // NOI18N
        jbPrevRecords.setName("jbPrevRecords"); // NOI18N
        jbPrevRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPrevRecordsActionPerformed(evt);
            }
        });
        jPanel2.add(jbPrevRecords, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, 40, -1));

        jbNextRecords.setText(resourceMap.getString("jbNextRecords.text")); // NOI18N
        jbNextRecords.setName("jbNextRecords"); // NOI18N
        jbNextRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNextRecordsActionPerformed(evt);
            }
        });
        jPanel2.add(jbNextRecords, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 40, -1));

        cbRecCount.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50", "100", "250", "500", "ALL" }));
        cbRecCount.setName("cbRecCount"); // NOI18N
        jPanel2.add(cbRecCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, 50, -1));

        jLabel71.setText(resourceMap.getString("jLabel71.text")); // NOI18N
        jLabel71.setName("jLabel71"); // NOI18N
        jPanel2.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 50, 20));

        jLabel72.setText(resourceMap.getString("jLabel72.text")); // NOI18N
        jLabel72.setName("jLabel72"); // NOI18N
        jPanel2.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 40, 20));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        listFilter.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Wszystkie", "Aktywne", "On Line", "Test", "Skan", "Zablokowane", "BLP", "T&T ROD", "Laser", "Service Point", "PDF", "Apache", "IIS" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listFilter.setName("listFilter"); // NOI18N
        jScrollPane2.setViewportView(listFilter);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 90, 50));

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, -1, -1));

        dcOdDaty.setName("dcOdDaty"); // NOI18N
        jPanel2.add(dcOdDaty, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 90, -1));

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        comboOpiekun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboOpiekun.setName("comboOpiekun"); // NOI18N
        jPanel2.add(comboOpiekun, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 140, -1));

        upPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 680, 80));

        jPanel4.setBackground(resourceMap.getColor("jPanel4.background")); // NOI18N
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel4.border.title"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        checkShowAddress.setBackground(resourceMap.getColor("checkShowAddress.background")); // NOI18N
        checkShowAddress.setText(resourceMap.getString("checkShowAddress.text")); // NOI18N
        checkShowAddress.setName("checkShowAddress"); // NOI18N
        jPanel4.add(checkShowAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, -1));

        upPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 10, 140, 80));

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbSeek.setText(resourceMap.getString("jbSeek.text")); // NOI18N
        jbSeek.setName("jbSeek"); // NOI18N
        jbSeek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSeekActionPerformed(evt);
            }
        });
        jPanel3.add(jbSeek, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 80, -1));

        tfSeek.setFont(resourceMap.getFont("tfSeek.font")); // NOI18N
        tfSeek.setName("tfSeek"); // NOI18N
        tfSeek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfSeekKeyReleased(evt);
            }
        });
        jPanel3.add(tfSeek, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 150, 20));

        jbFilter.setText(resourceMap.getString("jbFilter.text")); // NOI18N
        jbFilter.setName("jbFilter"); // NOI18N
        jbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFilterActionPerformed(evt);
            }
        });
        jPanel3.add(jbFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 80, -1));

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        checkFromStart.setBackground(resourceMap.getColor("checkFromStart.background")); // NOI18N
        checkFromStart.setText(resourceMap.getString("checkFromStart.text")); // NOI18N
        checkFromStart.setName("checkFromStart"); // NOI18N
        jPanel3.add(checkFromStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 220, -1));

        upPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, 330, 80));

        jSplitPane1.setTopComponent(upPanel);

        downPanel.setBackground(resourceMap.getColor("downPanel.background")); // NOI18N
        downPanel.setName("downPanel"); // NOI18N
        downPanel.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setBackground(resourceMap.getColor("jScrollPane1.background")); // NOI18N
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setBackground(resourceMap.getColor("table.background")); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
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
        table.setName("table"); // NOI18N
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tableMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("table.columnModel.title0")); // NOI18N
        table.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("table.columnModel.title1")); // NOI18N
        table.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("table.columnModel.title2")); // NOI18N
        table.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("table.columnModel.title3")); // NOI18N

        downPanel.add(jScrollPane1);

        jSplitPane1.setRightComponent(downPanel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void showDetail() {
        int i = table.getSelectedRow();
        // wyświetlamy szczegóły klienta Cas
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        d.height -= 100;
        YPFunctions.showPanelAsModalDialog(
                new UserDetailPanel(YPFunctions.strGet(table, i, 0),
                                    YPFunctions.strGet(table, i, 1),
                                    YPFunctions.strGet(table, i, 3),
                                    dcOdDaty.getDate()),
                   "Szczegóły klienta Cas ID=" + YPFunctions.strGet(table, i, 0),
                   d);
    }

    private void jbShowCustomersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbShowCustomersActionPerformed
        // Naciśnięty przycisk "?" w polu "ID CAS"
        // Proponujemy zaznaczyć klientów do przegłądania
        String opiekun = (String)comboOpiekun.getSelectedItem();
        if (opiekun.equals("*.*"))
            opiekun = "";


        SelectCustomerDialog frame = new SelectCustomerDialog(null, true, false, tfTerminal.getText().toUpperCase(),opiekun);
        frame.setTitle("Zaznacz ID_CUST klientów, którzycz chcesz obejrzeć");
        frame.setVisible(true);
        // Wyświetlamy Id Cust, które chcemy przeglądać
        tfIdCas.setText(((SelectCustomerDialog)frame).selectedID_CUST);
        tfIdCas.repaint();
}//GEN-LAST:event_jbShowCustomersActionPerformed

    private void prepareAndShowMainQuery() {

        Vector<String> sqlOptions = new Vector(Arrays.asList(
                             " and v.active_cust='T'",
                             " and v.type_online='T'",
                             " and v.type_test='T'",
                             " and v.use_scan='T'",
                             " and v.type_block='T'",
                             " and v.type_blp='T'",
                             " and v.type_rod='T'",
                             " and v.type_laser='T'",
                             " and v.type_serv_point='T'",
                             " and v.type_pdf='T'",
                             " and v.type_comm=1",
                             " and v.type_comm=2"
                             ));


        // Usuwamy dane z tablicy
        YPFunctions.clearContainer(downPanel);
        // Przygotowujemy szablon zapytania SQL
        String subQuery = " where 1=1 ";

        String sCasSap = tfIdCas.getText();
        // Jeśli wybrane ID CUST lub ID SAP
        if (! sCasSap.equals("")) {
            String firstToken = YPFunctions.getTokenAt(0, sCasSap, ",", "");
            if (firstToken.length()==7)
                subQuery += " and v.id_cust in (select id_cust from customers where id_sap in (" +
                              tfIdCas.getText() + "))";
            else
                subQuery += " and v.id_cust in (" + tfIdCas.getText() + ")";
        }



        // Dodajemy zawartość pola Terminal
        if (!tfTerminal.getText().equals("")) {
//            subQuery += " and depot='" + tfTerminal.getText() + "'";
            subQuery += " and v.depot in (" +
                          YPFunctions.getCommaTextWithApostrof(tfTerminal.getText()) +
                          ") ";
        }

        // Dodajemy filtrowanie po opiekunu
        String selectedOpiekun = (String)comboOpiekun.getSelectedItem();
        if ( ! selectedOpiekun.equals("*.*")) {
            subQuery += " and v.USER_NAME = '" + selectedOpiekun + "'";
        }



        // Dodajemy informację z filtru
        boolean bDelete11_12 = listFilter.isSelectedIndex(11) && listFilter.isSelectedIndex(12);

        int[] selected = listFilter.getSelectedIndices();
        // Jeśli mamy zaznaczenia
        if (selected.length > 0)
        {
            // Jakie pierwsze zaznaczenie ?
            int firstSelIx = listFilter.getSelectedIndex();
            if (firstSelIx != 0) { // Nie wybrano pierwszy element - "Wszystkie"
               for (int i = 0; i < selected.length; i++) {
                   if (bDelete11_12 && ((selected[i] == 11) ||
                                        (selected[i] == 12)
                                       )
                      )
                       ; // Nie dodawać warunków, jeśli jednocześnie zaznaczone 11 i 12 pozycje
                   else
                       subQuery +=  sqlOptions.elementAt(selected[i]-1);

               }
            }
        }

        // Tworzymy zapytanie SQL
        String mainQuery =
                selectQuery +
                subQuery + " order by v.id_cust";

        String s = (String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex());
        if (s.equals("ALL")) {
            // Jeśli w polu "Pokaż rekordów" jest "ALL" - tzeba pokazać wszystkie rekordy
            ShowMainQuery(mainQuery, 0, 999999);
        } else {
            // inaczej rekordy od iStartRecord do iStartRecord+n
            ShowMainQuery(mainQuery, iStartRecord,
                    iStartRecord +
                    Integer.parseInt((String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex())));
        }
    }

    private void ShowMainQuery(String p_query, int p_start, int p_end) {
        YPFunctions.clearContainer(downPanel);
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        YPFunctions.FillTableFromOra(
                p_query,
                null,
                p_start,
                p_end,
                table,
                new Vector(Arrays.asList(45, 50, 300, 60, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40)),
                new Vector(Arrays.asList(6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30)),
                false,
                true,
                new Vector(),
                null,
                true);
/*
        table.repaint();
        String tableSettings = YPFunctions.GetTableIni("UsersPanel", table);
        if (!tableSettings.equals(Constants.brakZawartosci)) {
            try {
                 Vector<String> v = YPFunctions.getTokensAsVector(tableSettings, ";");
                 for (int i = 0; i < v.size(); i++) {
                      String line = v.elementAt(i);
                      String colName = YPFunctions.getTokenAt(0, line, "|", "");
                      int width = Integer.parseInt(YPFunctions.getTokenAt(1, line, "|", ""));
                      int srcPos = YPFunctions.getTableColumnIndex(table, colName);
                      if (srcPos > -1) {
                          table.getColumnModel().moveColumn(srcPos, i);
                          table.getColumnModel().getColumn(i).setPreferredWidth(width);
                      }
                 }
            }catch (Exception e) {}
        }

        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }
            public void columnMoved(TableColumnModelEvent e) {
                YPFunctions.SaveTableIni("UsersPanel", table);
            }
            public void columnRemoved(TableColumnModelEvent e) {
            }
            public void columnMarginChanged(ChangeEvent e) {
                YPFunctions.SaveTableIni("UsersPanel", table);
            }
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });
*/
        this.setCursor(java.awt.Cursor.getDefaultCursor());
    }



  /*          try {
           // Otwieramy dostęp do pliku
           GlobalData.settings = new Properties();
           GlobalData.settings.load(new FileInputStream("Settings.ini"));
        }catch (Exception e) {}
        // Odczytujemy ostatnio używany serwer
        String tmp = GlobalData.settings.getProperty("UsersPanel.startShipmentsDate","2006/01/01");

        GlobalData.startShipmentsDate = YPFunctions.StringToDate(tmp);

        GlobalData.settings.setProperty("UsersPanel.startShipmentsDate","2006/01/01");
        // zapisujemy na dysk
        try {
                GlobalData.settings.save(new FileOutputStream("Settings.ini"), "");
        }catch (Exception e) {}
*/

    private void jbShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbShowActionPerformed
        // Naciśnięty przycisk "Pokaż"
        // Zaczynamy od pierwszego rekordu
        iStartRecord = 0;
        // Wyświetlamy zawartość tablicy zgodnie z wybranymi opcjami
        prepareAndShowMainQuery();
}//GEN-LAST:event_jbShowActionPerformed

    private void jbPrevRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPrevRecordsActionPerformed
        // odczytujemy ilość rekordów do przegłądania na jednej stronie
        String s = (String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex());
        // Jeśli wszystkie - wyjście z metody
        if (s.equals("ALL")) {
            return;
        }

        // zmniejszamy początkowy rekord do przegłądania o wybraną ilość
        int defCount = Integer.parseInt(s);
        if (iStartRecord >= defCount) {
            iStartRecord -= defCount;
        } else {
            iStartRecord = 0;
        }
        // Pokazujemy rekordy klientów CAS
        prepareAndShowMainQuery();
}//GEN-LAST:event_jbPrevRecordsActionPerformed

    private void jbNextRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNextRecordsActionPerformed
        // odczytujemy ilość rekordów do przegłądania na jednej stronie
        String s = (String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex());
        // Jeśli wszystkie - wyjście z metody
        if (s.equals("ALL")) {
            return;
        }
        // zwiększamy początkowy rekord do przegłądania o wybraną ilość
        iStartRecord += Integer.parseInt(s);
        // Pokazujemy rekordy klientów CAS
        prepareAndShowMainQuery();
}//GEN-LAST:event_jbNextRecordsActionPerformed

    private void jbSeekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSeekActionPerformed
        // Naciśnięty przycisk "Szukaj"
        // Odczytujemy treść wyszukiwania
        String seek = tfSeek.getText().toUpperCase();
        // Jeśli treść wyszukiwania jest mała - nie robimy nic
        if (seek.length() < 3) {
            return;
        }
        // tworzymy zbiór, do którego będziemy dopisywać Id CUST, które
        // trzeba podświetlić innym kolorem
        final Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < table.getRowCount() - 1; i++) {
            // Jeśli w nazwie istnieje ciąg przeszukiwania
            if ((YPFunctions.strGet(table,i, 2)).toUpperCase().indexOf(seek) >= 0) {
                // Do zbioru dopisujemy ID Cas
                set.add(Integer.parseInt(YPFunctions.strGet(table,i, 0)));
            }
        }
        // Wywołujemy renderer YPTableCellRenderer, który wyświetla żółtym kolorem
        // znalezione linijki tablicy
        table.getColumnModel().getColumn(2).setCellRenderer(
                new YPTableCellRenderer(set, 0, java.awt.Color.YELLOW));

        table.repaint();
    }//GEN-LAST:event_jbSeekActionPerformed


    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // Podwójne kliknięcie na linijce powoduje wyświetlenie szczegółów
        if (evt.getClickCount() == 2) {
           showDetail();
//           prepareAndShowMainQuery();
        }
    }//GEN-LAST:event_tableMouseClicked

    private void tfTerminalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTerminalMouseClicked
           SelectDepotDialog frame = new SelectDepotDialog(CasAdminApp.getApplication().getMainFrame(), true);
           Point p = tfTerminal.getLocationOnScreen();
           p.x += tfTerminal.getWidth();
           frame.setLocation(p);
           frame.setVisible(true);
           // Wyświetlamy listę depotów, które chcemy przeglądać
           if (((SelectDepotDialog) frame).getReturnStatus() != ((SelectDepotDialog) frame).RET_CANCEL)
           {
              tfTerminal.setText(((SelectDepotDialog) frame).retDepot);
              tfTerminal.repaint();
           }
    }//GEN-LAST:event_tfTerminalMouseClicked

    private void jbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFilterActionPerformed
        // Odczytujemy treść wyszukiwania
        String seek = tfSeek.getText().toUpperCase();
        // Jeśli treść wyszukiwania jest mała - nie robimy nic
        if (seek.length() < 3) {
            return;
        }

        String where = "";
        // Dodajemy zawartość pola Terminal
        if (!tfTerminal.getText().equals("")) {
            where += " and v.depot in (" +
                          YPFunctions.getCommaTextWithApostrof(tfTerminal.getText()) +
                          ") ";
        }

        // Dodajemy filtrowanie po opiekunu
        String selectedOpiekun = (String)comboOpiekun.getSelectedItem();
        if ( ! selectedOpiekun.equals("*.*")) {
            where += " and v.USER_NAME = '" + selectedOpiekun + "'";
        }




        // Usuwamy dane z tablicy
        YPFunctions.clearContainer(downPanel);
        // Przygotowujemy szablon zapytania SQL
        // Tworzymy zapytanie SQL
        String percent = checkFromStart.isSelected() ? "" : "%";
        String mainQuery =
                selectQuery +
                " where (v.nazwa like '" + percent + seek + "%'" +
                " or v.id_sap like '" + percent + seek + "%')" +
                where +
                " order by v.id_cust";

        ShowMainQuery(mainQuery, 0, 999999);

    }//GEN-LAST:event_jbFilterActionPerformed

    private void tfIdCasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIdCasKeyReleased
          if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
               jbShowActionPerformed(null);
    }//GEN-LAST:event_tfIdCasKeyReleased

    private void tfSeekKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSeekKeyReleased
          if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
               jbFilterActionPerformed(null);
    }//GEN-LAST:event_tfSeekKeyReleased

private void tableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseMoved
    if (checkShowAddress.isSelected())
    {
        String toolTipText;
        int row = table.rowAtPoint(evt.getPoint());
        //int column = table.columnAtPoint(evt.getPoint());

        if (row >= 0) {
            String idCas = table.getValueAt(row, 0).toString();

            String address = GlobalData.oraSession.selectString("Select '<html>'||c.kod_p||' '||c.miasto||'<br>'||c.Ulica||' '||c.nr_domu||"
                    + "'<br>'||'<b>Instalacja:</b>'||'<br>'||i.NAME||'<br>'||i.ZIPCODE||' '||i.city||'<br>'||i.street||' '||i.STREETNO||'</html>'"
                    + " from v_customers c left outer join CUSTOMERS_INSTALL i on c.id_cust=i.id_cust where c.id_cust=?",
                            new Vector(Arrays.asList(idCas)),
                            "");

            table.setToolTipText(address); 
        }
    
    } 
}//GEN-LAST:event_tableMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbRecCount;
    private javax.swing.JCheckBox checkFromStart;
    private javax.swing.JCheckBox checkShowAddress;
    private javax.swing.JComboBox comboOpiekun;
    private com.toedter.calendar.JDateChooser dcOdDaty;
    private javax.swing.JPanel downPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton jbFilter;
    private javax.swing.JButton jbNextRecords;
    private javax.swing.JButton jbPrevRecords;
    private javax.swing.JButton jbSeek;
    private javax.swing.JButton jbShow;
    private javax.swing.JButton jbShowCustomers;
    private javax.swing.JList listFilter;
    private javax.swing.JTable table;
    private javax.swing.JTextField tfIdCas;
    private javax.swing.JTextField tfSeek;
    private javax.swing.JTextField tfTerminal;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables
    private int iStartRecord;
    private String selectQuery =
                "SELECT " +
                "v.id_cust as \"Id Klienta\"" +
                ",v.id_sap as \"ID_SAP\"" +
                ",v.nazwa as \"NAZWA\"" +
                ",v.user_name as \"Opiekun\"" +
                ",v.depot as \"Depot\"" +
                ",v.active_cust as \"Aktywny\"" +
                ",v.type_online as \"OnLine\"" +
                ",v.cim_mobile as \"CIM/Mobile\"" +
                ",v.type_test as \"Test\"" +
                ",v.use_scan as \"Skan\"" +
                ",v.type_block as \"Blokada\"" +
                ",v.type_blp as \"BLP\"" +
                ",v.type_rod as \"ROD\"" +
                ",v.stop_ora as \"Stop migr.ORACLE\"" +
                ",v.type_serv_point as \"SP\"" +
                ",v.type_pdf as \"Pdf\"" +
                ",v.dr24 as \"Dr24\"" +
                ",v.vip as \"Vip\"" +
                ",v.multi_sap as \"MultiSAP\"" +
                ",v.fill_Phone as \"FillPhone\"" +
                ",v.app_name as \"App\"" +
                ",v.app_version as \"Wersja\"" +
                ",v.date_install as \"Instalacja\"" +
                ",v.Addr as \"Link\"" +
                ",(select da.date_synchro from data_activity da where da.id_cust=v.id_cust) \"LastDate\"" +
                ",v.type_db as \"DB\"" +
                ",v.type_neighbour as \"Sąsiad\"" +
                ",v.phone_to_preawi as \"TelPreawi\"" +
                ",v.must_phone as \"Must_phone\"" +
                ",v.must_email as \"Must_Email\"" +
                " from v_customers v ";

}

