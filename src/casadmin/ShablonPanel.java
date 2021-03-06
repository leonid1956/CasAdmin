package casadmin;
public class ShablonPanel extends javax.swing.JPanel {

    public ShablonPanel() {
        initComponents();
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
        downPanel = new javax.swing.JPanel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(ShablonPanel.class);
        upPanel.setBackground(resourceMap.getColor("upPanel.background")); // NOI18N
        upPanel.setName("upPanel"); // NOI18N
        upPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jSplitPane1.setTopComponent(upPanel);

        downPanel.setBackground(resourceMap.getColor("downPanel.background")); // NOI18N
        downPanel.setName("downPanel"); // NOI18N
        downPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jSplitPane1.setRightComponent(downPanel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel downPanel;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables

}
