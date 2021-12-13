package casadmin;

import java.util.*;

public class CasTablesPanel extends javax.swing.JPanel {

    public CasTablesPanel() {
        initComponents();
        // wypełniamy comboBox cbDictionary nazwami tabel i widoków serwera CAS
        fillComboWithOneField(
                   "select table_name tn " +
                   "from SYS.ALL_ALL_TABLES " +
                   "where owner = 'CAS_OWNER' " +
                         "and temporary = 'N' " +
                   "union " +
                   "select view_name tn " +
                   "from SYS.ALL_VIEWS " +
                   "where owner = 'CAS_OWNER' " +
                   "order by tn",
                   cbDictionary,
                   true,
                   false);
        // Wyznaczamy wygląd górnego panelu
        refreshUpPanel();
    }

    private void fillComboWithOneField(String query, javax.swing.JComboBox combo, boolean bAddEmpty, boolean bTrimToDateWhenDateActive) {
                 if (bAddEmpty)
                     combo.addItem(" ");

                 Vector<Vector<String> > v =
                    GlobalData.oraSession.selectAllRecordsToVector(query, null);
                 // Wypełniamy comboBox wartościami z wektora
                 Iterator it = v.iterator();
                 while (it.hasNext()) {
                     // Odczytujemy następny element wektora
                     Vector<String> vLine = (Vector<String>)it.next();
                     // z otrzymanego wektowa wybieramy pierwszy element
                     String item = vLine.elementAt(0);
                     if (bTrimToDateWhenDateActive && !item.isEmpty()) 
                         item = item.substring(0, 10);
                     // dodajemy element do comboBox
                     combo.addItem(item);
                 }
    }

    private void refreshUpPanel() {
        // Czy ma byc dostępne: comboBox z datami aktywności,
        // Id Cust i Sort
        Boolean bDateActiveEnabled = false;
        Boolean bIdCustEnabled = false;

        // Czy udostępniać panel "Opcję"
        Boolean optionsEnabled = false;
        tfSort.setText("");
        cbAscDesc.setSelected(false);
        // Wypełniamy combobox z datami aktywności pustą wartością
        cbDateActive.removeAllItems();
        cbDateActive.addItem(" ");
        // odczytujemy wybrany tekst z comboBox "Słownik".
        // Pierwszym z elementów modelu tego combobox jest " " dla tego żeby
        // na początku pokazywac w tym ComboBox pustą wartośc
        String sTable = (String)cbDictionary.getSelectedItem();
        // Jeśli wybrany tekst zawiera nazwę tablicy lub widoku
        if (!sTable.trim().equals("")) {
            vFieldNames = GlobalData.oraSession.selectFieldNames(sTable);
            if (vFieldNames.indexOf("DATE_ACTIVE") >= 0) {
                 // Odczytujemy wszystkie możliwe daty aktywności słownika (jeśli
                 // pole "DATE_ACTIVE" istnieje
                 fillComboWithOneField(
                        "select distinct date_active from "+sTable,
                        cbDateActive,
                        false,
                        true);
                 // Udostępniamy comboBox "Data aktywacji"
                 bDateActiveEnabled = true;
            }

            // Jeśli wybrany tekst zawiera pole ID_CUST
            if (vFieldNames.indexOf("ID_CUST") >= 0) {
                 // Udostępniamy pole ID_CUST do edycji, a także przycisk
                 // przeglądania dostępnych ID CUST
                 bIdCustEnabled = true;
            }
        }

        optionsEnabled = true;
        if (sTable.trim().equals(""))
             optionsEnabled = false;
        else if (bDateActiveEnabled && (cbDateActive.getSelectedIndex() == 0 ))
             optionsEnabled = false;
//        else if (bIdCustEnabled && (tfIdCust.getText().length() < 4))
//             optionsEnabled = false;

        if (optionsEnabled) {
                // iStartRecord - numer rekordu, od którego zaczyna się przeglądanie
                // słownika. Jest dla tego, że istnieje możliwość przeglądania danych
                // częściami (po 500, 1000 czy inną ilość na stronie).
                iStartRecord = 0;
        }

       // Robimy udostępnienie comboBox'a "Data aktywacji" w zależności od wartości
        // dateActiveEnabled
        cbDateActive.setEnabled(bDateActiveEnabled);
        tfIdCust.setEnabled(bIdCustEnabled);
        butShowIdCust.setEnabled(bIdCustEnabled);
        tfSort.setEnabled(optionsEnabled);
        cbAscDesc.setEnabled(optionsEnabled);
        butShowSelectFields.setEnabled(optionsEnabled);

        // Ustawiamy index na pierwszy element - on zawsze jest " "
        cbDateActive.setSelectedIndex(0);
        // panel "Opcję" udostępniamy w zależności od wartości zmiennej optionsEnabled
        YPFunctions.enableContainer(panelOptions, optionsEnabled);
        // usuwamy informację z dolnego panelu
        YPFunctions.clearContainer(downPanel);
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
        panelOptions = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        cbRecCount = new javax.swing.JComboBox();
        jbPrevRecords = new javax.swing.JButton();
        jbNextRecords = new javax.swing.JButton();
        jbShow = new javax.swing.JButton();
        panelTables = new javax.swing.JPanel();
        cbDictionary = new javax.swing.JComboBox();
        cbDateActive = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfIdCust = new javax.swing.JTextField();
        butShowSelectFields = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tfSort = new javax.swing.JTextField();
        butShowIdCust = new javax.swing.JButton();
        cbAscDesc = new javax.swing.JCheckBox();
        downPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDictionary = new javax.swing.JTable();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(110);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(CasTablesPanel.class);
        upPanel.setBackground(resourceMap.getColor("upPanel.background")); // NOI18N
        upPanel.setName("upPanel"); // NOI18N
        upPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelOptions.setBackground(resourceMap.getColor("panelOptions.background")); // NOI18N
        panelOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Opcję"));
        panelOptions.setEnabled(false);
        panelOptions.setName("panelOptions"); // NOI18N
        panelOptions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel71.setText(resourceMap.getString("jLabel71.text")); // NOI18N
        jLabel71.setName("jLabel71"); // NOI18N
        panelOptions.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 20));

        cbRecCount.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "500", "1000", "2000", "5000" }));
        cbRecCount.setToolTipText(resourceMap.getString("cbRecCount.toolTipText")); // NOI18N
        cbRecCount.setName("cbRecCount"); // NOI18N
        panelOptions.add(cbRecCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 50, -1));

        jbPrevRecords.setText(resourceMap.getString("jbPrevRecords.text")); // NOI18N
        jbPrevRecords.setToolTipText(resourceMap.getString("jbPrevRecords.toolTipText")); // NOI18N
        jbPrevRecords.setName("jbPrevRecords"); // NOI18N
        jbPrevRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPrevRecordsActionPerformed(evt);
            }
        });
        panelOptions.add(jbPrevRecords, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 50, -1));

        jbNextRecords.setText(resourceMap.getString("jbNextRecords.text")); // NOI18N
        jbNextRecords.setToolTipText(resourceMap.getString("jbNextRecords.toolTipText")); // NOI18N
        jbNextRecords.setName("jbNextRecords"); // NOI18N
        jbNextRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNextRecordsActionPerformed(evt);
            }
        });
        panelOptions.add(jbNextRecords, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 50, -1));

        jbShow.setText(resourceMap.getString("jbShow.text")); // NOI18N
        jbShow.setToolTipText(resourceMap.getString("jbShow.toolTipText")); // NOI18N
        jbShow.setName("jbShow"); // NOI18N
        jbShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbShowActionPerformed(evt);
            }
        });
        panelOptions.add(jbShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 70, -1));

        upPanel.add(panelOptions, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 260, 90));

        panelTables.setBackground(resourceMap.getColor("panelTables.background")); // NOI18N
        panelTables.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelTables.border.title"))); // NOI18N
        panelTables.setEnabled(false);
        panelTables.setName("panelTables"); // NOI18N
        panelTables.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbDictionary.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        cbDictionary.setToolTipText(resourceMap.getString("cbDictionary.toolTipText")); // NOI18N
        cbDictionary.setName("cbDictionary"); // NOI18N
        cbDictionary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDictionaryActionPerformed(evt);
            }
        });
        panelTables.add(cbDictionary, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 150, -1));

        cbDateActive.setToolTipText(resourceMap.getString("cbDateActive.toolTipText")); // NOI18N
        cbDateActive.setName("cbDateActive"); // NOI18N
        cbDateActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDateActiveActionPerformed(evt);
            }
        });
        panelTables.add(cbDateActive, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 150, -1));

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        panelTables.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 20));

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        panelTables.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 20));

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        panelTables.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, -1, 20));

        tfIdCust.setText(resourceMap.getString("tfIdCust.text")); // NOI18N
        tfIdCust.setToolTipText(resourceMap.getString("tfIdCust.toolTipText")); // NOI18N
        tfIdCust.setName("tfIdCust"); // NOI18N
        panelTables.add(tfIdCust, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 130, -1));

        butShowSelectFields.setText(resourceMap.getString("butShowSelectFields.text")); // NOI18N
        butShowSelectFields.setToolTipText(resourceMap.getString("butShowSelectFields.toolTipText")); // NOI18N
        butShowSelectFields.setName("butShowSelectFields"); // NOI18N
        butShowSelectFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butShowSelectFieldsActionPerformed(evt);
            }
        });
        panelTables.add(butShowSelectFields, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 40, -1));

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        panelTables.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, -1, -1));

        tfSort.setText(resourceMap.getString("tfSort.text")); // NOI18N
        tfSort.setToolTipText(resourceMap.getString("tfSort.toolTipText")); // NOI18N
        tfSort.setName("tfSort"); // NOI18N
        panelTables.add(tfSort, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 130, -1));

        butShowIdCust.setText(resourceMap.getString("butShowIdCust.text")); // NOI18N
        butShowIdCust.setToolTipText(resourceMap.getString("butShowIdCust.toolTipText")); // NOI18N
        butShowIdCust.setName("butShowIdCust"); // NOI18N
        butShowIdCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butShowIdCustActionPerformed(evt);
            }
        });
        panelTables.add(butShowIdCust, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, 40, -1));

        cbAscDesc.setBackground(resourceMap.getColor("cbAscDesc.background")); // NOI18N
        cbAscDesc.setText(resourceMap.getString("cbAscDesc.text")); // NOI18N
        cbAscDesc.setToolTipText(resourceMap.getString("cbAscDesc.toolTipText")); // NOI18N
        cbAscDesc.setName("cbAscDesc"); // NOI18N
        panelTables.add(cbAscDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        upPanel.add(panelTables, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 540, 90));

        jSplitPane1.setTopComponent(upPanel);

        downPanel.setBackground(resourceMap.getColor("downPanel.background")); // NOI18N
        downPanel.setName("downPanel"); // NOI18N
        downPanel.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableDictionary.setModel(new javax.swing.table.DefaultTableModel(
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
        tableDictionary.setName("tableDictionary"); // NOI18N
        tableDictionary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDictionaryMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableDictionary);

        downPanel.add(jScrollPane1);

        jSplitPane1.setRightComponent(downPanel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void cbDictionaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDictionaryActionPerformed
        // Wybrany element z comboBox "Słownik"
        // Wyznaczamy wygląd górnego panelu
        refreshUpPanel();
    }//GEN-LAST:event_cbDictionaryActionPerformed

    private void jbNextRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNextRecordsActionPerformed
        if (tfIdCust.isEnabled()&& (tfIdCust.getText().length() < 4))
        {
            YPFunctions.showErrorMessage("Nie poprawny ID CUST");
//            JOptionPane.showMessageDialog(null, "Nie poprawny ID CUST","Błąd",JOptionPane.ERROR_MESSAGE);
            return;
        }
        // odczytujemy ilość rekordów do przegłądania na jednej stronie
        String s = (String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex());
        // zwiększamy początkowy rekord do przegłądania o wybraną ilość
        iStartRecord += Integer.parseInt(s);
        // Pokazujemy rekordy tablicy słownika
        prepareAndShowMainQuery();
    }//GEN-LAST:event_jbNextRecordsActionPerformed

    private void jbPrevRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPrevRecordsActionPerformed
        if (tfIdCust.isEnabled()&& (tfIdCust.getText().length() < 4))
        {
            YPFunctions.showErrorMessage("Nie poprawny ID CUST");
//            JOptionPane.showMessageDialog(null, "Nie poprawny ID CUST","Błąd",JOptionPane.ERROR_MESSAGE);
            return;
        }
        // odczytujemy ilość rekordów do przegłądania na jednej stronie
        String s = (String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex());
        // zmniejszamy początkowy rekord do przegłądania o wybraną ilość
        int defCount = Integer.parseInt(s);
        if (iStartRecord >= defCount) {
            iStartRecord -= defCount;
        } else {
            iStartRecord = 0;
        }
        // Pokazujemy rekordy tablicy słownika
        prepareAndShowMainQuery();
    }//GEN-LAST:event_jbPrevRecordsActionPerformed

    private void jbShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbShowActionPerformed
            // Naciśnięty przycisk "Pokaż"
            // Wyświetlamy zawartość tablicy zgodnie z wybranymi opcjami
            if (tfIdCust.isEnabled()&& (tfIdCust.getText().length() < 4))
            {
                YPFunctions.showErrorMessage("Nie poprawny ID CUST");
//                JOptionPane.showMessageDialog(null, "Nie poprawny ID CUST","Błąd",JOptionPane.ERROR_MESSAGE);
                return;
            }
//             optionsEnabled = false;

            prepareAndShowMainQuery();
    }//GEN-LAST:event_jbShowActionPerformed

    private void cbDateActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDateActiveActionPerformed
        // Wybrana wartość z comboBox "Data aktywacji"
        // Odczytujemy wybraną wartość
        String s = (String)cbDateActive.getSelectedItem();
        // Jeśli wybrana wartość zawiera datę aktywacji, to udostępniamy
        // panel "Opcję", inaczej blokujemy go.
        Boolean optionsEnabled = ! YPFunctions.nvlStr(s).trim().equals("");
        YPFunctions.enableContainer(panelOptions, optionsEnabled);
        // Usuwamy informację z dolnego panelu
        YPFunctions.clearContainer(downPanel);
        // Ustawiamy początek pokazu rekordów słownika
        iStartRecord = 0;
    }//GEN-LAST:event_cbDateActiveActionPerformed

    private void butShowIdCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butShowIdCustActionPerformed
        // Naciśnięty przycisk "?" w polu "ID CAS"
        // Proponujemy zaznaczyć klientów do przegłądania
        SelectCustomerDialog frame = new SelectCustomerDialog(null, true, false, "","");
        frame.setTitle("Zaznacz ID_CUST klientów, którzycz chcesz obejrzeć");
        frame.setVisible(true);
        // Wyświetlamy Id Cust, które chcemy przeglądać
        tfIdCust.setText(((SelectCustomerDialog)frame).selectedID_CUST);
        tfIdCust.repaint();
        refreshUpPanel();
    }//GEN-LAST:event_butShowIdCustActionPerformed

    private void butShowSelectFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butShowSelectFieldsActionPerformed
        // Naciśnięty przycisk "?" w polu "ID CAS"
        // Proponujemy zaznaczyć klientów do przegłądania
        SelectSortFieldsDialog frame = new SelectSortFieldsDialog(
                CasAdminApp.getApplication().getMainFrame(),
                true,
                (String)cbDictionary.getSelectedItem(),
                vFieldNames);
        frame.setTitle("Zaznacz ID_CUST klientów, którzycz chcesz obejrzeć");
        frame.setVisible(true);
        // Wyświetlamy Id Cust, które chcemy przeglądać
        String sort = frame.vSelectedFields.toString();
        sort = sort.substring(1, sort.length()-1);
        tfSort.setText(sort);

        tfSort.repaint();

    }//GEN-LAST:event_butShowSelectFieldsActionPerformed

    private void tableDictionaryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDictionaryMouseClicked
        Vector<Vector> body = new Vector();

        for (int i = 0; i < tableDictionary.getColumnCount(); i++)
        {
            Vector line = new Vector();
            line.add(((YPTableModel)tableDictionary.getModel()).getColumnName(i));
            line.add(YPFunctions.strGet(tableDictionary,tableDictionary.getSelectedRow(), i));
            body.add(line);
        }
        // Wyświetlamy okno dialogowe dla pokazywania rekordu tablicy
        // w pionowej formie
        ShowTableRecordDetailDialog frame = new ShowTableRecordDetailDialog(
                                                  CasAdminApp.getApplication().getMainFrame(),
                                                  true,
                                                  body);
        frame.setVisible(true);
    }//GEN-LAST:event_tableDictionaryMouseClicked

    private void prepareAndShowMainQuery() {
        // Przygotowujemy warunek dla zapytania SQL
        String subQuery = "";
        // Jeśli słownik nie zawiera pola "date_active", to comboBox cbDateActive
        // jest niedostępny i wybrana wartość jest pusta, inaczej
        String da = (String)cbDateActive.getSelectedItem();
        da = da.trim();
        if (!da.isEmpty())
            subQuery = subQuery + " and date_active=to_date('"+da+"','yyyy-mm-dd hh:mi:ss')";

        // Zawartość pola ID CUST specjalnie zostawiamy nie skasowanej, żeby za każdym razem nie
        // wpisywać ję od nowa, dla tego zawsze sprawdzamy, czy jest to pole dostępne do edycji
        if (tfIdCust.isEnabled())
        {
           String idcust = tfIdCust.getText().trim();
           if (!idcust.isEmpty())
               subQuery = subQuery + " and ID_CUST=" + idcust;
        }
        String orderBy = tfSort.getText().trim();
        if (!orderBy.isEmpty()) {
            orderBy = " order by " + orderBy;
            if (cbAscDesc.isSelected())
                orderBy += " desc";
        }
        // Budujemy zapytanie SQL
        String mainQuery = "select * from " + 
                           (String)cbDictionary.getSelectedItem() +
                           " where 1=1 " +
                           subQuery +
                           orderBy;
        // Odczytujemy ilość rekordów, które trzeba odczytać
        String s = (String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex());
        // rekordy od iStartRecord do iStartRecord+n
        int iEndRecord = iStartRecord +
                    Integer.parseInt((String) cbRecCount.getItemAt(cbRecCount.getSelectedIndex()));
        // Wyświetlamy tablicę
        YPFunctions.FillTableFromOra(mainQuery,
                                     null,
                                     iStartRecord,
                                     iEndRecord,
                                     tableDictionary,
                                     new Vector(),
                                     new Vector(),
                                     false,
                                     true,
                                     new Vector(),
                                     null,
                                     true);
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butShowIdCust;
    private javax.swing.JButton butShowSelectFields;
    private javax.swing.JCheckBox cbAscDesc;
    private javax.swing.JComboBox cbDateActive;
    private javax.swing.JComboBox cbDictionary;
    private javax.swing.JComboBox cbRecCount;
    private javax.swing.JPanel downPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton jbNextRecords;
    private javax.swing.JButton jbPrevRecords;
    private javax.swing.JButton jbShow;
    private javax.swing.JPanel panelOptions;
    private javax.swing.JPanel panelTables;
    private javax.swing.JTable tableDictionary;
    private javax.swing.JTextField tfIdCust;
    private javax.swing.JTextField tfSort;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables
    private Integer iStartRecord;
    private Vector<String> vFieldNames;

}
