/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClientShowDialog.java
 *
 * Created on 2015-01-05, 11:26:38
 */
package casadmin;

import javax.swing.BorderFactory;

/**
 *
 * @author lpylypch
 */
public class ClientShowDialog extends javax.swing.JDialog {

    /** Creates new form ClientShowDialog */
    public ClientShowDialog(java.awt.Frame parent, boolean modal,
            String p_title,
            String p_nrSap, String p_name, String p_zip, String p_city,
            String p_street, String p_house, String p_tel) {
        super(parent, modal);
        initComponents();
        this.setTitle(p_title);
        this.ClientPanel.setBorder(BorderFactory.createTitledBorder(p_title));
        this.SapCli.setText(p_nrSap);
        this.NazwaCli.setText(p_name);
        this.KodPocztCli.setText(p_zip);
        this.MiastoCli.setText(p_city);
        this.UlicaCli.setText(p_street);
        this.DomCli.setText(p_house);
        this.TelCli.setText(p_tel);
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
        ClientPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        UlicaCli = new javax.swing.JTextField();
        KodPocztCli = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        MiastoCli = new javax.swing.JTextField();
        DomCli = new javax.swing.JTextField();
        SapCli = new javax.swing.JTextField();
        NazwaCli = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        TelCli = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(ClientShowDialog.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setName("Form"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        ClientPanel.setBackground(resourceMap.getColor("ClientPanel.background")); // NOI18N
        ClientPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("ClientPanel.border.title"))); // NOI18N
        ClientPanel.setName("ClientPanel"); // NOI18N
        ClientPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N
        ClientPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 10));

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N
        ClientPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, -1, 20));

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N
        ClientPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 20));

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N
        ClientPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

        UlicaCli.setName("UlicaCli"); // NOI18N
        ClientPanel.add(UlicaCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 140, -1));

        KodPocztCli.setName("KodPocztCli"); // NOI18N
        ClientPanel.add(KodPocztCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 90, -1));

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N
        ClientPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, -1, 20));

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N
        ClientPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, -1, 20));

        MiastoCli.setName("MiastoCli"); // NOI18N
        ClientPanel.add(MiastoCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 240, -1));

        DomCli.setName("DomCli"); // NOI18N
        ClientPanel.add(DomCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 50, -1));

        SapCli.setName("SapCli"); // NOI18N
        ClientPanel.add(SapCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 90, -1));

        NazwaCli.setName("NazwaCli"); // NOI18N
        ClientPanel.add(NazwaCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 240, -1));

        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N
        ClientPanel.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 20, 20));

        TelCli.setName("TelCli"); // NOI18N
        ClientPanel.add(TelCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 100, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(ClientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ClientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientShowDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientShowDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientShowDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientShowDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ClientShowDialog dialog = new ClientShowDialog(new javax.swing.JFrame(), true,"","","","","","","","");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ClientPanel;
    private javax.swing.JTextField DomCli;
    private javax.swing.JTextField KodPocztCli;
    private javax.swing.JTextField MiastoCli;
    private javax.swing.JTextField NazwaCli;
    private javax.swing.JTextField SapCli;
    private javax.swing.JTextField TelCli;
    private javax.swing.JTextField UlicaCli;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
