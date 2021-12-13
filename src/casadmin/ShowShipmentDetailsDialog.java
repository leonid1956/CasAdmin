/*
 * ShowShipmentDetailsDialog.java
 *
 * Created on 15 listopad 2008, 22:34
 */

package casadmin;

import java.util.*;
public class ShowShipmentDetailsDialog extends javax.swing.JDialog {

    /** Creates new form ShowShipmentDetailsDialog */
    public ShowShipmentDetailsDialog(java.awt.Frame parent, boolean modal, String p_pdfSource) {
         super(parent, modal);
         initComponents();
         // "Centrujemy" dialog
         YPFunctions.CenterScreen(this);
         // Wykorzystujemy własny "tokenizer"
         Vector<String> tokens = YPFunctions.getTokensAsVector(p_pdfSource,";");
         //DHL0211000
         boolean bIsEncoded = tokens.elementAt(0).charAt(7) != '0';
         // "Ciało" tablicy
         Vector<Vector> body = new Vector();
         // Licznik
         int i = 0;
         Enumeration en = tokens.elements();
         while(en.hasMoreElements()){
             // Zwiększamy licznik
             i++;
             // Tworzymy nowy wektor
             Vector v = new Vector();
             // w pierwszy element wektora zapisujemy licznik
             v.add(Integer.toString(i));
             try{
                 // w drugi element - nazwę pola
                 v.add(Constants.PdfItemsNames[i-1]);
             }catch (Exception e) {
                break;
             }
             // Trzeci element - kolejny "token"
             String coded = (String)en.nextElement();
             v.add(coded);
             if (bIsEncoded)
             {
                 // Dekodujemy zakodowany ciąg
                 v.add(Decode(coded,i));
             }
             else
                 v.add(coded);

             // Dopisujemy wektor do "ciała" tablicy
             body.add(v);
         }
         // Wektow nagłówków tablicy
         Vector titles = new Vector(Arrays.asList("Numer","Opis","Zawartość","Dekodowana zawartość"));
         // Wyświetlamy tablicę
         YPFunctions.FillTableFromVector(body,
                             titles,
                             pdfTable,
                             new Vector(Arrays.asList(50,120,500,500)),
                             new Vector(),
                             false,
                             false,
                             new Vector(),
                             new Vector(Arrays.asList(0,1,2,3)),
                             false,
                             0);
/*
         pdfTable.setModel(new YPTableModel(body,titles,new Vector()));
         // Ustawiamy szerokości kolumn
         pdfTable.getColumnModel().getColumn(0).setPreferredWidth(50);
         pdfTable.getColumnModel().getColumn(1).setPreferredWidth(120);
         pdfTable.getColumnModel().getColumn(2).setPreferredWidth(500);
         pdfTable.getColumnModel().getColumn(3).setPreferredWidth(500);
*/
 }

    private int chars(String c){
         if (c.equals("."))
             return 10;
         else
             return Integer.parseInt(c);
    }

    private String DecodeAlgoN2(String s){
        String[] digits = {"0","1","2","3","4","5","6","7","8","9","."};
        String temp = "";
        try {
            for (int i = 1; i <= s.length(); i++ ){
                String c = s.substring(i-1,i);
                temp += digits[(chars(c) -i + 11 * (i / 11 + 1)) % 11];
            }
        }catch (Exception e) {
             temp = "Błąd dekodowania!!!";
        }
        return temp;
    }

    private String Decode(String s, int n){
             if (((n >= 49) && (n <= 67)) || (n == 75) || (n == 105) || (n == 107))
                return DecodeAlgoN2(s);
             else
                return s;
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
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(ShowShipmentDetailsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
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
                ShowShipmentDetailsDialog dialog = new ShowShipmentDetailsDialog(new javax.swing.JFrame(), true,"");
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
