package casadmin;

import java.util.*;

public class DrRabatyDialog extends javax.swing.JDialog {


    public DrRabatyDialog(java.awt.Frame parent,
                                boolean modal, int p_idCas, String p_product, String p_platnik) {
        super(parent, modal);
        initComponents();
        idCas = p_idCas;
        product = p_product;
        platnik = p_platnik;
        showScreen();
    }

    private void showScreen() {
/*
        Object ob = GlobalData.oraSession.selectObject("select rab_line from dict_dr_rab_line where ID_CUST=? and ID_PRODUCT=? and id_pay=?",
                    new Vector(Arrays.asList(idCas, product, platnik)),
                    0);
        double ret = 0.00;
        if (ob != null)
            ret = Double.parseDouble(ob.toString());
*/
        String s = GlobalData.oraSession.selectString("select rab_line from dict_dr_rab_line where ID_CUST=? and ID_PRODUCT=? and id_pay=?",
                    new Vector(Arrays.asList(idCas, product, platnik)),
                    "0.0");
        double ret = 0.00;
        if (!s.isEmpty())
            ret = Double.parseDouble(s);


        tfLineDiscount.setText(Double.toString(ret));
        cbLineDiscount.setSelected(ret != 0);
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
        jbCancel = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbLineDiscount = new javax.swing.JCheckBox();
        tfLineDiscount = new javax.swing.JTextField();
        jbSaveLineDiscount = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(DrRabatyDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jbCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 190, 30));

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 200, 20));

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 390, 20));

        cbLineDiscount.setBackground(resourceMap.getColor("cbLineDiscount.background")); // NOI18N
        cbLineDiscount.setText(resourceMap.getString("cbLineDiscount.text")); // NOI18N
        cbLineDiscount.setName("cbLineDiscount"); // NOI18N
        jPanel2.add(cbLineDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, -1, -1));

        tfLineDiscount.setText(resourceMap.getString("tfLineDiscount.text")); // NOI18N
        tfLineDiscount.setName("tfLineDiscount"); // NOI18N
        jPanel2.add(tfLineDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 70, -1));

        jbSaveLineDiscount.setText(resourceMap.getString("jbSaveLineDiscount.text")); // NOI18N
        jbSaveLineDiscount.setName("jbSaveLineDiscount"); // NOI18N
        jbSaveLineDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveLineDiscountActionPerformed(evt);
            }
        });
        jPanel2.add(jbSaveLineDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 190, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 600, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 150));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        // Wcisniety "Cofnij"
        doClose(RET_CANCEL);
}//GEN-LAST:event_jbCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Dialog zosta?? zamkni??ty
        doClose(RET_CANCEL);
    }//GEN-LAST:event_formWindowClosing

    private void jbSaveLineDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveLineDiscountActionPerformed
        Double d = YPFunctions.nvlParseDouble(tfLineDiscount.getText());
        GlobalData.oraSession.executeProcedure(
                "MGR_DEFINE.SET_DR_RAB_LINE(?,?,?,?)",
                new Vector(Arrays.asList(
                idCas, platnik, product, d)));
        GlobalData.oraSession.SaveLog("Zmiana rabatu liniowego dla Cust_Id=" + idCas, 
                 "platnik=" + platnik + ",product=" + product + "," + d.toString() );

        showScreen();
        YPFunctions.showMessage("Rabaty zosta??y zapisane i odczytane","");
}//GEN-LAST:event_jbSaveLineDiscountActionPerformed

    private void doClose(int retStatus) {
        // Zamykamy dialog
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**  zwraca informacje o przycisku OK czy Cofnij nacisn???? u??ytkownik
    */
    public int getReturnStatus() {
        // zmienna returnStatus jest prywatna, robimy publiczn?? metod??,
        // ??eby zwr??ci?? informacj?? kt??ry przycisk (Ok czy Cofnij) nacisn????
        // u??ytkownik
        return returnStatus;
    }



    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DrRabatyDialog dialog = new DrRabatyDialog(new javax.swing.JFrame(), true,0,"","");
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
    private javax.swing.JCheckBox cbLineDiscount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbSaveLineDiscount;
    private javax.swing.JTextField tfLineDiscount;
    // End of variables declaration//GEN-END:variables
    public int RET_CANCEL = 0;
    public int RET_OK = 1;
    private int returnStatus = RET_CANCEL;
    public String product;
    public String platnik;
    public int idCas;
}
