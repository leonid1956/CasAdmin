package casadmin;

import java.util.Arrays;
import java.util.Vector;

public class SqlQueriesPanel extends javax.swing.JPanel {

    public SqlQueriesPanel() {
        initComponents();
        FillQueriesTable();
        YPFunctions.clearContainer(tabbedResults);
    }
    
    private void FillQueriesTable()
    {
        YPFunctions.FillTableFromOra(
                "select insertt, username, queryname, querytext from CAS_QUERIES order by insertt",
                new Vector(),
                1,
                99999,
                tableQueries,
                new Vector(Arrays.asList(20,20,30,300)),
                new Vector(),
                false,
                true,
                new Vector(),
                new Vector(),
                false);                 
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSplitPane1 = new javax.swing.JSplitPane();
        upPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableQueries = new javax.swing.JTable();
        downPanel = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jbExecute = new javax.swing.JButton();
        jbAddToHistory = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taQueryText = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        tabbedResults = new javax.swing.JTabbedPane();
        panelResultTable = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableResults = new javax.swing.JTable();

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane3.setViewportView(jTable1);

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(SqlQueriesPanel.class);
        upPanel.setBackground(resourceMap.getColor("upPanel.background")); // NOI18N
        upPanel.setName("upPanel"); // NOI18N
        upPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableQueries.setModel(new javax.swing.table.DefaultTableModel(
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
        tableQueries.setName("tableQueries"); // NOI18N
        tableQueries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableQueriesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableQueries);

        upPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setTopComponent(upPanel);

        downPanel.setBackground(resourceMap.getColor("downPanel.background")); // NOI18N
        downPanel.setName("downPanel"); // NOI18N
        downPanel.setLayout(new java.awt.GridLayout());

        jSplitPane2.setDividerLocation(150);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout());

        jSplitPane3.setDividerLocation(681);
        jSplitPane3.setName("jSplitPane3"); // NOI18N

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbExecute.setText(resourceMap.getString("jbExecute.text")); // NOI18N
        jbExecute.setName("jbExecute"); // NOI18N
        jbExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExecuteActionPerformed(evt);
            }
        });
        jPanel3.add(jbExecute, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 30));

        jbAddToHistory.setText(resourceMap.getString("jbAddToHistory.text")); // NOI18N
        jbAddToHistory.setName("jbAddToHistory"); // NOI18N
        jbAddToHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddToHistoryActionPerformed(evt);
            }
        });
        jPanel3.add(jbAddToHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 30));

        jSplitPane3.setRightComponent(jPanel3);

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        taQueryText.setColumns(20);
        taQueryText.setFont(resourceMap.getFont("taQueryText.font")); // NOI18N
        taQueryText.setRows(5);
        taQueryText.setName("taQueryText"); // NOI18N
        jScrollPane2.setViewportView(taQueryText);

        jPanel4.add(jScrollPane2);

        jSplitPane3.setLeftComponent(jPanel4);

        jPanel1.add(jSplitPane3);

        jSplitPane2.setTopComponent(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        tabbedResults.setName("tabbedResults"); // NOI18N

        panelResultTable.setName("panelResultTable"); // NOI18N
        panelResultTable.setLayout(new javax.swing.BoxLayout(panelResultTable, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        tableResults.setModel(new javax.swing.table.DefaultTableModel(
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
        tableResults.setName("tableResults"); // NOI18N
        jScrollPane4.setViewportView(tableResults);

        panelResultTable.add(jScrollPane4);

        tabbedResults.addTab(resourceMap.getString("panelResultTable.TabConstraints.tabTitle"), panelResultTable); // NOI18N

        jPanel2.add(tabbedResults);

        jSplitPane2.setRightComponent(jPanel2);

        downPanel.add(jSplitPane2);

        jSplitPane1.setRightComponent(downPanel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void tableQueriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableQueriesMouseClicked
         if (evt.getClickCount() == 2) {
            int i = tableQueries.getSelectedRow();
            taQueryText.setText(YPFunctions.strGet(tableQueries, i, 3));
         }
    }//GEN-LAST:event_tableQueriesMouseClicked

    private void jbExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExecuteActionPerformed
        String s = taQueryText.getText().trim();
        if (s.equals(""))
            return;
        YPFunctions.clearContainer(tabbedResults);
        if (s.substring(0, 6).toUpperCase().equals("SELECT"))
        {
            YPFunctions.FillTableFromOra(
                s,
                new Vector(),
                1,
                99999,
                tableResults,
                new Vector(),
                new Vector(),
                false,
                true,
                new Vector(),
                new Vector(),
                true);
        }
        else
        {
            Boolean b = GlobalData.oraSession.executeQuery(s, new Vector());
            if (b)
                YPFunctions.showMessage("Polecenie SQL zosta??o wykonane", "OK");
            else
                YPFunctions.showErrorMessage(Errors.getLastErrorString());
        }
    }//GEN-LAST:event_jbExecuteActionPerformed

    private void jbAddToHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddToHistoryActionPerformed
       String sqlTitle = YPFunctions.showInputDialog("Wprowad?? nazw?? polecenia SQL", "Nazwa polecenia SQL");
       GlobalData.oraSession.executeQuery(
               "Insert into CAS_QUERIES(USERNAME,QUERYNAME,QUERYTEXT) values (?,?,?)",
               new Vector(Arrays.asList(GlobalData.m_sUserLogin, sqlTitle, taQueryText.getText())));
       FillQueriesTable();
    }//GEN-LAST:event_jbAddToHistoryActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel downPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbAddToHistory;
    private javax.swing.JButton jbExecute;
    private javax.swing.JPanel panelResultTable;
    private javax.swing.JTextArea taQueryText;
    private javax.swing.JTabbedPane tabbedResults;
    private javax.swing.JTable tableQueries;
    private javax.swing.JTable tableResults;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables

}
