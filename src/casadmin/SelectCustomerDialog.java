/*
 * SelectCustomerDialog.java
 *
 * Created on 21 listopad 2008, 12:21
 */

package casadmin;

import java.util.*;


public class SelectCustomerDialog extends javax.swing.JDialog {

    /** Creates new form SelectCustomerDialog */
    public SelectCustomerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SelectCustomerDialog(java.awt.Frame parent, boolean modal, boolean p_selectOnlyOne,String p_forTerminals, String p_forOpiekun){
        super(parent, modal);
        initComponents();
        // Czy wynikiem wyboru ID CUST ma być tylko jeden ID CUST
        selectOnlyOne = p_selectOnlyOne;
        
        vectorSelectedIdCas = new Vector<String>();
        // Dla ID Cust tylko z wyznaczonych terminałów
        forTerminals = p_forTerminals;
        forOpiekun = p_forOpiekun;

        bCheckAll = false;

//        cbCheckAll.setEnabled(!(forTerminals+forOpiekun).equals(""));
        // "Centrowanie"
        YPFunctions.CenterScreen(this);
        // selectedID_CUST - publiczna, zapisuje się wynik wybranych ID CUST, zapisanych
        // przes ","
        selectedID_CUST = "";
        // Wypełniamy tablicę
        fillTable("");
        // Włączamy zaznaczenie całych linijek tablicy
        tableCustomers.setRowSelectionAllowed(true);
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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tfSeek = new javax.swing.JTextField();
        jbSeek = new javax.swing.JButton();
        jbSelectChecked = new javax.swing.JButton();
        cbCheckAll = new javax.swing.JCheckBox();
        jbFilter = new javax.swing.JButton();
        checkFromStart = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCustomers = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(null);
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(70);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(SelectCustomerDialog.class);
        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 20));

        tfSeek.setText(resourceMap.getString("tfSeek.text")); // NOI18N
        tfSeek.setName("tfSeek"); // NOI18N
        jPanel1.add(tfSeek, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 190, -1));

        jbSeek.setText(resourceMap.getString("jbSeek.text")); // NOI18N
        jbSeek.setName("jbSeek"); // NOI18N
        jbSeek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSeekActionPerformed(evt);
            }
        });
        jPanel1.add(jbSeek, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 80, -1));

        jbSelectChecked.setText(resourceMap.getString("jbSelectChecked.text")); // NOI18N
        jbSelectChecked.setName("jbSelectChecked"); // NOI18N
        jbSelectChecked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelectCheckedActionPerformed(evt);
            }
        });
        jPanel1.add(jbSelectChecked, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, 140, 30));

        cbCheckAll.setBackground(resourceMap.getColor("cbCheckAll.background")); // NOI18N
        cbCheckAll.setText(resourceMap.getString("cbCheckAll.text")); // NOI18N
        cbCheckAll.setName("cbCheckAll"); // NOI18N
        cbCheckAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCheckAllActionPerformed(evt);
            }
        });
        jPanel1.add(cbCheckAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, 170, 40));

        jbFilter.setText(resourceMap.getString("jbFilter.text")); // NOI18N
        jbFilter.setName("jbFilter"); // NOI18N
        jbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFilterActionPerformed(evt);
            }
        });
        jPanel1.add(jbFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 80, -1));

        checkFromStart.setBackground(resourceMap.getColor("checkFromStart.background")); // NOI18N
        checkFromStart.setText(resourceMap.getString("checkFromStart.text")); // NOI18N
        checkFromStart.setName("checkFromStart"); // NOI18N
        jPanel1.add(checkFromStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, -1, -1));

        jSplitPane1.setTopComponent(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "null", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        tableCustomers.setName("tableCustomers"); // NOI18N
        tableCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCustomersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCustomers);

        jPanel2.add(jScrollPane1);

        jSplitPane1.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jbSeekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSeekActionPerformed
    // Naciśnięty przycisk "Szukaj"
    // Przeszukujemy tablicę w polach "Skrot" i "Nazwa" i zaznaczamy
    // znalezione linijku żółtym kolorem
    seekAndSelect(tfSeek.getText().toUpperCase());
}//GEN-LAST:event_jbSeekActionPerformed

private void jbSelectCheckedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelectCheckedActionPerformed
      // nacisnięty przycisk "Wybierz zaznaczone"
      // zapisujemy wynik wybranych ID CUST do zmiennej selectedID_CUST
      // i zamykamy dialog
      doClose(RET_OK);
}//GEN-LAST:event_jbSelectCheckedActionPerformed

private void tableCustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCustomersMouseClicked
    if (evt.getClickCount() == 2)
         doClose(RET_DBLCLICK);
}//GEN-LAST:event_tableCustomersMouseClicked

private void cbCheckAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCheckAllActionPerformed
        bCheckAll = !bCheckAll;
        YPTableModel tm = (YPTableModel)tableCustomers.getModel();
        for (int i = 0; i < tm.getRowCount(); i++)
        {
            Boolean b = (Boolean)(tm.getValueAt(i, 0)) ;
            tm.setValueAt(!b, i, 0);
       }
        tableCustomers.repaint();
}//GEN-LAST:event_cbCheckAllActionPerformed

private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
           // Zamykamy dialog
       doClose(RET_CANCEL);
}//GEN-LAST:event_closeDialog

private void jbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFilterActionPerformed
    fillTable(tfSeek.getText().toUpperCase());
}//GEN-LAST:event_jbFilterActionPerformed

private Vector<Vector> fromCacheToLocal() {
    boolean forTerminalsIsEmpty = forTerminals.isEmpty();
    if (forTerminalsIsEmpty)
        return GlobalData.v_allCustomers;
    else {
        Vector<Vector> res = new Vector();
        Iterator it = GlobalData.v_allCustomers.iterator();
        while (it.hasNext()) {
            Vector v = (Vector)it.next();
            if (forTerminalsIsEmpty || forTerminals.contains((String)v.elementAt(3)))
                    res.add(v);
        }
        return res;
    }
}


private void fillTable(String p_filter ){
        bSaveToCache = false;
        boolean fillFromOra = true;
        if (! GlobalData.v_allCustomers.isEmpty())
                fillFromOra = false;
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        this.setCursor(java.awt.Cursor.getDefaultCursor());
        if (fillFromOra)
        {
                // Zapytanie SQL
                String  query = "select c.id_cust as \"Id Klienta\"," +
                                       "c.id_sap as \"ID_SAP\","+
                                       "c.depot as \"Terminal\"," +
                                       "s.skrotnaz as \"Skrot\"," +
                                       "s.nazwa as \"NAZWA\"," +
                                       "c.app_version as \"Wersja\" " +
                                "from customers c," +
                                     "customers_sap s " +
                                "where c.id_sap=s.id_sap";
                if (!forTerminals.equals(""))
                    query += " and c.depot in (" + YPFunctions.getCommaTextWithApostrof(forTerminals) + ")";

                if (!forOpiekun.equals(""))
                    query += " and c.id_cust in (select id_cust from customers where id_user = (select id_user from cas_users where user_name='"+
                                    forOpiekun + "')) ";
                if (!p_filter.equals("")) {
                    String percent = checkFromStart.isSelected() ? "" : "%";

                    query += " and ((Upper(s.skrotnaz) like '" + percent + p_filter + "%' or Upper(s.nazwa) like '" + percent + p_filter + "%')" +
                                   " or (c.id_sap like '" + percent + p_filter + "%'))" ;
                }



                // Pokazujemy tablicę
                YPFunctions.FillTableFromOra(query,  // zapytanie
                                             null,   // brak parametrów
                                             0,      // wszystkie od 0
                                             99999,  // do 99999
                                             tableCustomers, // tablica
                                             new Vector(Arrays.asList(20,70,70,30,120,400,20,10)), // szerokości kolumn
                                             new Vector(),  // brak kolumn, interpretowanych jako Boolean
                                             true,   // Dodatkowa pierwsza kolumna z checkbox'ami
                                             true,   // sortowanie po kolumnach
                                             new Vector(Arrays.asList(0)), // edytować tylko pierwszą kolumnę, t.zn. zaznaczać checkbox'y
                                             null,    // bez nagłówków linijek
                                             true);
                if (forTerminals.equals("") && p_filter.equals("") && forOpiekun.equals(""))
                    bSaveToCache = true;
        }
        else
        {
                Vector<Vector> vBody = fromCacheToLocal();
                if (!p_filter.equals("")) {
                    for (int i = vBody.size()-1; i >=0; i--) {
                        Vector vTemp = (Vector)vBody.elementAt(i);
                        String skrot = ((String)vTemp.elementAt(4)).toUpperCase();
                        String nazwa = ((String)vTemp.elementAt(5)).toUpperCase();
                        if ((skrot.indexOf(p_filter) >= 0) || (nazwa.indexOf(p_filter) >= 0))
                            ;
                        else
                            vBody.remove(i);
                    }
                }
                YPFunctions.FillTableFromVector(
                                             vBody,
                                             new Vector(Arrays.asList("","Id Klienta","ID_SAP","Terminal","Skrot","NAZWA","Wersja")),
                                             tableCustomers, // tablica
                                             new Vector(Arrays.asList(20,70,70,30,120,400,20,10)), // szerokości kolumn
                                             new Vector(),  // brak kolumn, interpretowanych jako Boolean
                                             true,   // Dodatkowa pierwsza kolumna z checkbox'ami
                                             true,   // sortowanie po kolumnach
                                             new Vector(Arrays.asList(0)), // edytować tylko pierwszą kolumnę, t.zn. zaznaczać checkbox'y
                                             null,    // bez nagłówków linijek
                                             true,
                                             0);
        }
        tableCustomers.repaint();
        this.setCursor(java.awt.Cursor.getDefaultCursor());
}
    
private void seekAndSelect(String p_seekStr){
        // nie szukamy, jeśli długość szablonu wyszukiwania <3
        if (p_seekStr.length()<3) 
            return;

        // Robimy zbiór, do którego będziemy dopisywać ID_CUST, które trzeba zaznaczać
        // żółtym kolorem
        final Set<Integer> set = new HashSet<Integer>();
        // przeszukujemy wszystkie linijki tablicy
        for (int i=0; i < tableCustomers.getRowCount() - 1; i++)
            // Szukamy w poly "Skrot" i "Nazwa"
            if ((YPFunctions.strGet(tableCustomers,i, 4)).toUpperCase().indexOf(p_seekStr)>=0 ||
                (YPFunctions.strGet(tableCustomers,i, 5)).toUpperCase().indexOf(p_seekStr)>=0  )
            {
                // Dopisujemy do zbioru Id Cust z drugiej kolumny ( 1 koumna - checkbox'y)
                set.add(Integer.parseInt(YPFunctions.strGet(tableCustomers,i, 1)));
            }
        // Wywołujemy renderer dla wszystkich kolumn, za wyjątkiem 1 kolumny
        for (int i=1; i<tableCustomers.getColumnCount();i++)
             tableCustomers.getColumnModel().getColumn(i).setCellRenderer(
                     new YPTableCellRenderer(set,1,java.awt.Color.YELLOW));
        // Przerysowanie tablicy
        tableCustomers.repaint();
}
            
private void doClose(int retStatus) {
    // Jeśli był podwójny click na tablice
    if (retStatus == RET_DBLCLICK)
        selectedID_CUST = YPFunctions.strGet(tableCustomers,tableCustomers.getSelectedRow(), 1);
    // Jeśli był nacisnięty przycisk "Wybierz zaznaczone"
    else if (retStatus == RET_OK)
    {
        // Licznik zaznaczonych linijek
        int selectedCount = 0;
        // Do sRet zapisujemy ID CUST, rozdzielając znakiem ","
        String sRet = "";
        String sTemp = "";
        for (int i=0; i<tableCustomers.getModel().getRowCount(); i++) {
           if ((Boolean)tableCustomers.getValueAt(i, 0)) {
               // zwiększamy licznik
               selectedCount++;
               // Dodajemy ID CUST
               sRet += YPFunctions.strGet(tableCustomers, i, 1) + ",";

               sTemp += YPFunctions.strGet(tableCustomers, i, 1) + ",";

               if (sTemp.length() > 1000) {
                   sTemp = sTemp.substring(0, sTemp.length() - 1);
                   vectorSelectedIdCas.add(sTemp);
                   sTemp = "";
               }
           }
        }
        // usuwamy ostatni znak ","
        if (sRet.length()>0)
                sRet = sRet.substring(0, sRet.length() - 1);
        
        if (sTemp.length()>0) {
                sTemp = sTemp.substring(0, sTemp.length() - 1);
                vectorSelectedIdCas.add(sTemp);
        }
        
        // Sprawdzamy czy spełniony warunek, że ma być zwrócono tylko jeden ID CUST
        if (selectOnlyOne && (selectedCount != 1)) {
            // Warunek był i został nie spełniony.
            // Sygnalizujemy błąd
            YPFunctions.showErrorMessage("Musi być wybrany jeden i tylko jeden Id Cust");
//            JOptionPane.showMessageDialog(null, "Musi być wybrany jeden i tylko jeden Id Cust","Błąd",JOptionPane.ERROR_MESSAGE);
            // nie zwracamy nic. Użytkownik powinien jeszcze raz wykonać tą procedure.
            selectedID_CUST = "";
        } else
        {
            // Zwracamy wynik
            selectedID_CUST = sRet;
        }
    }
    else
        selectedID_CUST = "";

    if (bSaveToCache)
    {
          YPTableModel tm = (YPTableModel)tableCustomers.getModel();
          GlobalData.v_allCustomers = tm.getBody();
          for (int i = 0; i < GlobalData.v_allCustomers.size(); i++)
          {
                if ((Boolean)(GlobalData.v_allCustomers.elementAt(i).elementAt(0)) == true)
                    GlobalData.v_allCustomers.elementAt(i).setElementAt(Boolean.valueOf(false), 0);
          }
    }

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
                SelectCustomerDialog dialog = new SelectCustomerDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JCheckBox cbCheckAll;
    private javax.swing.JCheckBox checkFromStart;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton jbFilter;
    private javax.swing.JButton jbSeek;
    private javax.swing.JButton jbSelectChecked;
    private javax.swing.JTable tableCustomers;
    private javax.swing.JTextField tfSeek;
    // End of variables declaration//GEN-END:variables
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    public static final int RET_DBLCLICK = 2;
    private boolean selectOnlyOne;
    private String forTerminals;
    private String forOpiekun;
    private boolean bCheckAll;
    private boolean bSaveToCache;
    public String selectedID_CUST;
    public Vector<String> vectorSelectedIdCas;
}