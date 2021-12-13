package casadmin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

public class ImportPcPanel extends javax.swing.JPanel {

    public ImportPcPanel() {
        initComponents();
        Date d = new Date();
        dcDateActive.setDate(d);
        labelApshubs.setText("") ;
        labelArea.setText("") ;
        labelDepots.setText("") ;
        labelDistances.setText("") ;
        labelPC.setText("") ;
        labelPcs.setText("") ;
        labelRangetrm.setText("") ;
        labelDirectPC.setText("");
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
        jLabel2 = new javax.swing.JLabel();
        dcDateActive = new com.toedter.calendar.JDateChooser();
        downPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cbImportRangetrm = new javax.swing.JCheckBox();
        cbImportApshubs = new javax.swing.JCheckBox();
        cbImportPc = new javax.swing.JCheckBox();
        cbImportDistances = new javax.swing.JCheckBox();
        cbImportArea = new javax.swing.JCheckBox();
        cbImportDepots = new javax.swing.JCheckBox();
        cbGenerateDictPcs = new javax.swing.JCheckBox();
        labelPcs = new javax.swing.JLabel();
        labelRangetrm = new javax.swing.JLabel();
        labelApshubs = new javax.swing.JLabel();
        labelPC = new javax.swing.JLabel();
        labelDistances = new javax.swing.JLabel();
        labelArea = new javax.swing.JLabel();
        labelDepots = new javax.swing.JLabel();
        jbExecute = new javax.swing.JButton();
        cbGenerateDirectPC = new javax.swing.JCheckBox();
        labelDirectPC = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(ImportPcPanel.class);
        upPanel.setBackground(resourceMap.getColor("upPanel.background")); // NOI18N
        upPanel.setName("upPanel"); // NOI18N
        upPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 20));

        dcDateActive.setDateFormatString(resourceMap.getString("dcDateActive.dateFormatString")); // NOI18N
        dcDateActive.setName("dcDateActive"); // NOI18N
        jPanel2.add(dcDateActive, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 100, 20));

        upPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 790, 80));

        jSplitPane1.setTopComponent(upPanel);

        downPanel.setBackground(resourceMap.getColor("downPanel.background")); // NOI18N
        downPanel.setName("downPanel"); // NOI18N
        downPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbImportRangetrm.setBackground(resourceMap.getColor("cbImportRangetrm.background")); // NOI18N
        cbImportRangetrm.setText(resourceMap.getString("cbImportRangetrm.text")); // NOI18N
        cbImportRangetrm.setName("cbImportRangetrm"); // NOI18N
        jPanel3.add(cbImportRangetrm, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 160, -1));

        cbImportApshubs.setBackground(resourceMap.getColor("cbImportApshubs.background")); // NOI18N
        cbImportApshubs.setText(resourceMap.getString("cbImportApshubs.text")); // NOI18N
        cbImportApshubs.setName("cbImportApshubs"); // NOI18N
        jPanel3.add(cbImportApshubs, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        cbImportPc.setBackground(resourceMap.getColor("cbImportPc.background")); // NOI18N
        cbImportPc.setText(resourceMap.getString("cbImportPc.text")); // NOI18N
        cbImportPc.setName("cbImportPc"); // NOI18N
        jPanel3.add(cbImportPc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        cbImportDistances.setBackground(resourceMap.getColor("cbImportDistances.background")); // NOI18N
        cbImportDistances.setText(resourceMap.getString("cbImportDistances.text")); // NOI18N
        cbImportDistances.setName("cbImportDistances"); // NOI18N
        jPanel3.add(cbImportDistances, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        cbImportArea.setBackground(resourceMap.getColor("cbImportArea.background")); // NOI18N
        cbImportArea.setText(resourceMap.getString("cbImportArea.text")); // NOI18N
        cbImportArea.setName("cbImportArea"); // NOI18N
        jPanel3.add(cbImportArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        cbImportDepots.setBackground(resourceMap.getColor("cbImportDepots.background")); // NOI18N
        cbImportDepots.setText(resourceMap.getString("cbImportDepots.text")); // NOI18N
        cbImportDepots.setName("cbImportDepots"); // NOI18N
        jPanel3.add(cbImportDepots, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        cbGenerateDictPcs.setBackground(resourceMap.getColor("cbGenerateDictPcs.background")); // NOI18N
        cbGenerateDictPcs.setText(resourceMap.getString("cbGenerateDictPcs.text")); // NOI18N
        cbGenerateDictPcs.setName("cbGenerateDictPcs"); // NOI18N
        jPanel3.add(cbGenerateDictPcs, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        labelPcs.setText(resourceMap.getString("labelPcs.text")); // NOI18N
        labelPcs.setName("labelPcs"); // NOI18N
        jPanel3.add(labelPcs, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 210, 90, 20));

        labelRangetrm.setText(resourceMap.getString("labelRangetrm.text")); // NOI18N
        labelRangetrm.setName("labelRangetrm"); // NOI18N
        jPanel3.add(labelRangetrm, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 90, 20));

        labelApshubs.setText(resourceMap.getString("labelApshubs.text")); // NOI18N
        labelApshubs.setName("labelApshubs"); // NOI18N
        jPanel3.add(labelApshubs, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 90, 20));

        labelPC.setText(resourceMap.getString("labelPC.text")); // NOI18N
        labelPC.setName("labelPC"); // NOI18N
        jPanel3.add(labelPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 100, 20));

        labelDistances.setText(resourceMap.getString("labelDistances.text")); // NOI18N
        labelDistances.setName("labelDistances"); // NOI18N
        jPanel3.add(labelDistances, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 90, 20));

        labelArea.setText(resourceMap.getString("labelArea.text")); // NOI18N
        labelArea.setName("labelArea"); // NOI18N
        jPanel3.add(labelArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 70, 20));

        labelDepots.setText(resourceMap.getString("labelDepots.text")); // NOI18N
        labelDepots.setName("labelDepots"); // NOI18N
        jPanel3.add(labelDepots, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 180, 70, 20));

        jbExecute.setText(resourceMap.getString("jbExecute.text")); // NOI18N
        jbExecute.setName("jbExecute"); // NOI18N
        jbExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExecuteActionPerformed(evt);
            }
        });
        jPanel3.add(jbExecute, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 150, 30));

        cbGenerateDirectPC.setBackground(resourceMap.getColor("cbGenerateDirectPC.background")); // NOI18N
        cbGenerateDirectPC.setText(resourceMap.getString("cbGenerateDirectPC.text")); // NOI18N
        cbGenerateDirectPC.setName("cbGenerateDirectPC"); // NOI18N
        jPanel3.add(cbGenerateDirectPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        labelDirectPC.setText(resourceMap.getString("labelDirectPC.text")); // NOI18N
        labelDirectPC.setName("labelDirectPC"); // NOI18N
        jPanel3.add(labelDirectPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, 80, 20));

        downPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 790, 360));

        jSplitPane1.setRightComponent(downPanel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void jbExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExecuteActionPerformed
            if (cbImportRangetrm.isSelected() || 
                cbImportApshubs.isSelected() ||
                cbImportPc.isSelected() ||
                cbImportDistances.isSelected() ||
                cbImportArea.isSelected() ||
                cbImportDepots.isSelected())
            {
                cbGenerateDictPcs.setSelected(true);
                cbGenerateDirectPC.setSelected(true);
                this.repaint();
            }
            jbExecute.setEnabled(false);
            java.sql.Date sqlDate = new java.sql.Date(0);
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            long dateTime = 0;
            Date setDate = new Date(0);
            try 
            {        
               setDate = (Date) df.parse(YPFunctions.DateToString(dcDateActive.getDate()));
            } catch (ParseException ex) {
               Logger.getLogger(ImportPcPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            dateTime = setDate.getTime();
            sqlDate = new java.sql.Date(dateTime);
            
            
            if (cbImportRangetrm.isSelected()) {
                labelRangetrm.setText("Wykonanie ...");
                labelRangetrm.repaint();
                GlobalData.oraSession.executeFunction("MGR_CDB.IMP_RANGETRM", new Vector(), OracleTypes.NUMBER);
                GlobalData.oraSession.SaveLog("Import RANGETRM","");

                labelRangetrm.setText("Wykonano");
                labelRangetrm.repaint();
            }
            if (cbImportApshubs.isSelected()) {
                labelApshubs.setText("Wykonanie ...");
                labelApshubs.repaint();
                GlobalData.oraSession.executeFunction("MGR_CDB.IMP_APSHUBS(?)",
                        new Vector(Arrays.asList(sqlDate)),
                        OracleTypes.NUMBER);
                GlobalData.oraSession.SaveLog("Import APSHUBS","");
                labelApshubs.setText("Wykonano");
                labelApshubs.repaint();
            }
            if (cbImportPc.isSelected()) {
                labelPC.setText("Wykonanie ...");
                labelPC.repaint();
                GlobalData.oraSession.executeFunction("MGR_CDB.IMP_PC(?)",
                        new Vector(Arrays.asList(sqlDate)),
                        OracleTypes.NUMBER);
                GlobalData.oraSession.SaveLog("Import PC","");
                labelPC.setText("Wykonano");
                labelPC.repaint();
            }
            if (cbImportDistances.isSelected()) {
                labelDistances.setText("Wykonanie ...");
                labelDistances.repaint();
                GlobalData.oraSession.executeFunction("MGR_CDB.IMP_DISTANCES(?)",
                        new Vector(Arrays.asList(sqlDate)),
                        OracleTypes.NUMBER);
                GlobalData.oraSession.SaveLog("Import DISTANCES","");
                labelDistances.setText("Wykonano");
                labelDistances.repaint();
            }
            if (cbImportArea.isSelected()) {
                labelArea.setText("Wykonanie ...");
                labelArea.repaint();
                GlobalData.oraSession.executeFunction("MGR_CDB.IMP_AREA",
                        new Vector(),
                        OracleTypes.NUMBER);
                GlobalData.oraSession.SaveLog("Import AREA","");
                labelArea.setText("Wykonano");
                labelArea.repaint();
            }
            if (cbImportDepots.isSelected()) {
                labelDepots.setText("Wykonanie ...");
                labelDepots.repaint();
                GlobalData.oraSession.executeFunction("MGR_CDB.IMP_DEPOTS",
                        new Vector(),
                        OracleTypes.NUMBER);
                GlobalData.oraSession.SaveLog("Import DEPOTS","");
                labelDepots.setText("Wykonano");
                labelDepots.repaint();
            }
            if (cbGenerateDictPcs.isSelected()) {
                labelPcs.setText("Wykonanie ...");
                labelPcs.repaint();
                GlobalData.oraSession.executeFunction("MGR_CDB.CREATE_PCS(?,?)",
                        new Vector(Arrays.asList(sqlDate, sqlDate)),
                        OracleTypes.NUMBER);
                GlobalData.oraSession.SaveLog("Import + create DICT_PCS","");
                labelPcs.setText("Wykonano");
                labelPcs.repaint();
            }
            if (cbGenerateDirectPC.isSelected()) {
                labelDirectPC.setText("Wykonanie ...");
                labelDirectPC.repaint();
                GlobalData.oraSession.executeFunction("DIRECTCAS.f_generateDirectPC",
                        new Vector(),
                        OracleTypes.VARCHAR);
                GlobalData.oraSession.SaveLog("Import + generate DirectPc","");
                labelDirectPC.setText("Wykonano");
                labelDirectPC.repaint();
            }

            
            
            jbExecute.setEnabled(true);



    }//GEN-LAST:event_jbExecuteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbGenerateDictPcs;
    private javax.swing.JCheckBox cbGenerateDirectPC;
    private javax.swing.JCheckBox cbImportApshubs;
    private javax.swing.JCheckBox cbImportArea;
    private javax.swing.JCheckBox cbImportDepots;
    private javax.swing.JCheckBox cbImportDistances;
    private javax.swing.JCheckBox cbImportPc;
    private javax.swing.JCheckBox cbImportRangetrm;
    private com.toedter.calendar.JDateChooser dcDateActive;
    private javax.swing.JPanel downPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton jbExecute;
    private javax.swing.JLabel labelApshubs;
    private javax.swing.JLabel labelArea;
    private javax.swing.JLabel labelDepots;
    private javax.swing.JLabel labelDirectPC;
    private javax.swing.JLabel labelDistances;
    private javax.swing.JLabel labelPC;
    private javax.swing.JLabel labelPcs;
    private javax.swing.JLabel labelRangetrm;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables

}
