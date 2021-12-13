package casadmin;

import java.util.*;
public class ShowTableRecordDetailDialog extends javax.swing.JDialog {

    /** Creates new form ShowShipmentDetailsDialog */
    public ShowTableRecordDetailDialog(java.awt.Frame parent, boolean modal, Vector<Vector> p_body) {
         super(parent, modal);
         initComponents();
         // "Centrujemy" dialog
         YPFunctions.CenterScreen(this);
         // Wykorzystujemy własny "tokenizer"
         // Wektow nagłówków tablicy
         Vector titles = new Vector(Arrays.asList("Nazwa pola","Zawartość"));
         // Wyświetlamy tablicę
         pdfTable.setModel(new YPTableModel(p_body,titles,new Vector()));
         // Ustawiamy szerokości kolumn
         pdfTable.getColumnModel().getColumn(0).setPreferredWidth(120);
         pdfTable.getColumnModel().getColumn(1).setPreferredWidth(500);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        pdfTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(ShowTableRecordDetailDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        pdfTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pdfTable.setName("pdfTable"); // NOI18N
        pdfTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(pdfTable);
        pdfTable.getColumnModel().getColumn(0).setResizable(false);
        pdfTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        pdfTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("pdfTable.columnModel.title0")); // NOI18N
        pdfTable.getColumnModel().getColumn(1).setResizable(false);
        pdfTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        pdfTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("pdfTable.columnModel.title1")); // NOI18N
        pdfTable.getColumnModel().getColumn(2).setResizable(false);
        pdfTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        pdfTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("pdfTable.columnModel.title2")); // NOI18N

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

  
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ShowTableRecordDetailDialog dialog = new ShowTableRecordDetailDialog(new javax.swing.JFrame(), true,null);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable pdfTable;
    // End of variables declaration//GEN-END:variables
}