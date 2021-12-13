package casadmin;

import java.util.*;
import oracle.jdbc.OracleTypes;

public class PaletyRabatyDialog extends javax.swing.JDialog {

     private int getLabelPosition(Vector<Vector> p_vBody, String p_dist) {
        int ret = -1;
        for (int i = 0; i < p_vBody.size(); i ++) {
            Vector vLine = p_vBody.elementAt(i);
            String distance = (String)vLine.elementAt(1);
            if (distance.equals(p_dist)) {
                ret = i;
                break;
            }
        }
        return ret;
    }


    public PaletyRabatyDialog(java.awt.Frame parent,
                                boolean modal, int p_idCas, String p_product, String p_platnik) {
        super(parent, modal);
        initComponents();
        idCas = p_idCas;
        product = p_product;
        platnik = p_platnik;
        showScreen();
    }

    private void showScreen() {
        
        // Rabat liniowy
        if (idCas == 0) { // Dla Standardowego
                YPFunctions.enableContainer(panelLineDiscount, false);
        }
        else {
/*
                Object ob = GlobalData.oraSession.selectObject("select rab_line from dict_pal_rab_line where ID_CUST=? and ID_PRODUCT=? and id_pay=?",
                            new Vector(Arrays.asList(idCas, product, platnik)),
                            0.00);
                double ret = 0.00;
                if (ob != null)
                    ret = Double.parseDouble(ob.toString());
*/
                String s = GlobalData.oraSession.selectString("select rab_line from dict_pal_rab_line where ID_CUST=? and ID_PRODUCT=? and id_pay=?",
                    new Vector(Arrays.asList(idCas, product, platnik)),
                    "0.0");
                double ret = 0.00;
                if (!s.isEmpty())
                    ret = Double.parseDouble(s);


                tfLineDiscount.setText(Double.toString(ret));
                cbLineDiscount.setSelected(ret != 0);
        }

        // Rabat odległościowy

        if (idCas == 0) { // Dla Standardowego
                YPFunctions.FillTableFromOra(
                     "select distance as \"Odległośc\"," +
                        "rab_a as \"Rabat do 200kg\"," +
                        "rab_b as \"do 400kg\"," +
                        "rab_b1 as \"do 600kg\"," +
                        "rab_b2 as \"do 800kg\"," +
                        "rab_c as \"do 1000kg\" " +
                     "from dict_pal_rab_Distance_std " +
                     "where id_product=? and id_pay=? " +
                     "order by distance",
                     new Vector(Arrays.asList(product, platnik)),
                     0,
                     99999,
                     tableDiscountDistance,
                     new Vector(Arrays.asList(10,30,30,30,30,30,30)),
                     new Vector(Arrays.asList(0)),
                     true,
                     false,
                     new Vector(Arrays.asList(0,2,3,4,5,6)),
                     new Vector(Arrays.asList(1)),
                     false);

        } else {
                String s = (String)GlobalData.oraSession.executeFunction(
                           "MGR_DEFINE.f_PREPARE_PAL_RAB_DISTANCE(?,?,?)",
                           new Vector(Arrays.asList(idCas,platnik,product)),
                           OracleTypes.VARCHAR);
                if ((s == null) || s.isEmpty())
                    return;
                Vector<String> vLines = YPFunctions.getTokensAsVector(s, "\n");
                if (!vLines.isEmpty())
                    vLines.removeElementAt(vLines.size()-1);

                Vector<Vector> vBody = new Vector();

                Iterator it = vLines.iterator();
                while(it.hasNext())
                {
                    String line = (String)it.next();
                    Vector vItems = YPFunctions.getTokensAsVector(line, ";");
                    if(vItems.elementAt(0).equals("N"))
                    {
                        Vector vNew = new Vector(Arrays.asList(
                                Boolean.valueOf(false),
                                vItems.elementAt(1),   // odleglosc
                                vItems.elementAt(2).equals("")?"0":vItems.elementAt(2),
                                vItems.elementAt(3).equals("")?"0":vItems.elementAt(3),
                                vItems.elementAt(4).equals("")?"0":vItems.elementAt(4),
                                vItems.elementAt(5).equals("")?"0":vItems.elementAt(5),
                                vItems.elementAt(6).equals("")?"0":vItems.elementAt(6)));
                        vBody.add(vNew);
                    }
                }
                Iterator it1 = vLines.iterator();
                while(it1.hasNext())
                {
                    String line = (String)it1.next();
                    Vector vItems = YPFunctions.getTokensAsVector(line, ";");
                    if(vItems.elementAt(0).equals("T"))
                    {
                        int pos = getLabelPosition(vBody, (String)vItems.elementAt(1));
                        if (pos != -1) {
                           Vector vNew = new Vector(Arrays.asList(
                                Boolean.valueOf(true),
                                vItems.elementAt(1),   // odleglosc
                                vItems.elementAt(2).equals("")?"0":vItems.elementAt(2),
                                vItems.elementAt(3).equals("")?"0":vItems.elementAt(3),
                                vItems.elementAt(4).equals("")?"0":vItems.elementAt(4),
                                vItems.elementAt(5).equals("")?"0":vItems.elementAt(5),
                                vItems.elementAt(6).equals("")?"0":vItems.elementAt(6)));
                            vBody.set(pos, vNew);
                        }
                    }
                }

                YPFunctions.FillTableFromVector(vBody,
                        new Vector(Arrays.asList("Edytuj","Odległość","Rabat do 200kg","do 400kg","do 600kg","do 800kg","do 1000kg")),
                        tableDiscountDistance,
                        new Vector(Arrays.asList(10,30,30,30,30,30,30)),
                        new Vector(Arrays.asList(0)),
                        false,
                        false,
                        new Vector(Arrays.asList(0,2,3,4,5,6)),
                        new Vector(Arrays.asList(1)),
                        false,
                        0);
        }

        // Rabaty ilościowe

        Vector<Vector> vv = new Vector();
        for(int i = 0; i < 10; i++)
        {
            Vector v = new Vector(Arrays.asList("","0"));
            vv.add(v);
        }

        String dictPalRab_Query = "";
        Vector queryParams;
        if (idCas == 0) {
           dictPalRab_Query =
                "select to_char(volume)," +
                       "to_char(Upust) " +
                "from DICT_PAL_RAB_STD " +
                "where " +
                      "id_product=? and " +
                      "id_pay=? " +
                "order by volume";
           queryParams = new Vector(Arrays.asList(product, platnik));
        } else {
           dictPalRab_Query =
                "select to_char(volume)," +
                       "to_char(Upust) " +
                "from DICT_PAL_RAB " +
                "where id_cust=? and " +
                      "id_product=? and " +
                      "id_pay=? " +
                "order by volume";
            queryParams = new Vector(Arrays.asList(idCas, product, platnik));
        }
        Vector vvv = GlobalData.oraSession.selectAllRecordsToVector(dictPalRab_Query, queryParams);
        for (int i = 0; i < vvv.size(); i++)
        {
            Vector v1 = (Vector)vvv.elementAt(i);
            vv.set(i, v1);
        }

        YPFunctions.FillTableFromVector(vv,
                new Vector(Arrays.asList("Sztuk do","Upust")),
                tableDiscountCount,
                new Vector(Arrays.asList(30,30)),
                new Vector(),
                false,
                false,
                new Vector(Arrays.asList(0,1)),
                new Vector(),
                false,
                0);
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
        panelLineDiscount = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbLineDiscount = new javax.swing.JCheckBox();
        tfLineDiscount = new javax.swing.JTextField();
        jbSaveLineDiscount = new javax.swing.JButton();
        panelDistanceDiscount = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDiscountDistance = new javax.swing.JTable();
        jbSaveDistanceDiscount = new javax.swing.JButton();
        panelCountDiscount = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDiscountCount = new javax.swing.JTable();
        jbSaveCountDiscount = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(PaletyRabatyDialog.class);
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
        jPanel1.add(jbCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 480, 190, 30));

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 200, 20));

        panelLineDiscount.setBackground(resourceMap.getColor("panelLineDiscount.background")); // NOI18N
        panelLineDiscount.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelLineDiscount.border.title"))); // NOI18N
        panelLineDiscount.setName("panelLineDiscount"); // NOI18N
        panelLineDiscount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        panelLineDiscount.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 390, 20));

        cbLineDiscount.setBackground(resourceMap.getColor("cbLineDiscount.background")); // NOI18N
        cbLineDiscount.setText(resourceMap.getString("cbLineDiscount.text")); // NOI18N
        cbLineDiscount.setName("cbLineDiscount"); // NOI18N
        panelLineDiscount.add(cbLineDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, -1, -1));

        tfLineDiscount.setText(resourceMap.getString("tfLineDiscount.text")); // NOI18N
        tfLineDiscount.setName("tfLineDiscount"); // NOI18N
        panelLineDiscount.add(tfLineDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 70, -1));

        jbSaveLineDiscount.setText(resourceMap.getString("jbSaveLineDiscount.text")); // NOI18N
        jbSaveLineDiscount.setName("jbSaveLineDiscount"); // NOI18N
        jbSaveLineDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveLineDiscountActionPerformed(evt);
            }
        });
        panelLineDiscount.add(jbSaveLineDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 190, -1));

        jPanel1.add(panelLineDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 600, 80));

        panelDistanceDiscount.setBackground(resourceMap.getColor("panelDistanceDiscount.background")); // NOI18N
        panelDistanceDiscount.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelDistanceDiscount.border.title"))); // NOI18N
        panelDistanceDiscount.setAutoscrolls(true);
        panelDistanceDiscount.setName("panelDistanceDiscount"); // NOI18N
        panelDistanceDiscount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableDiscountDistance.setModel(new javax.swing.table.DefaultTableModel(
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
        tableDiscountDistance.setName("tableDiscountDistance"); // NOI18N
        jScrollPane1.setViewportView(tableDiscountDistance);

        panelDistanceDiscount.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 20, 580, 120));

        jbSaveDistanceDiscount.setText(resourceMap.getString("jbSaveDistanceDiscount.text")); // NOI18N
        jbSaveDistanceDiscount.setName("jbSaveDistanceDiscount"); // NOI18N
        jbSaveDistanceDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveDistanceDiscountActionPerformed(evt);
            }
        });
        panelDistanceDiscount.add(jbSaveDistanceDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 190, 30));

        jPanel1.add(panelDistanceDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 600, 180));

        panelCountDiscount.setBackground(resourceMap.getColor("panelCountDiscount.background")); // NOI18N
        panelCountDiscount.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("panelCountDiscount.border.title"))); // NOI18N
        panelCountDiscount.setName("panelCountDiscount"); // NOI18N
        panelCountDiscount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableDiscountCount.setModel(new javax.swing.table.DefaultTableModel(
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
        tableDiscountCount.setName("tableDiscountCount"); // NOI18N
        jScrollPane2.setViewportView(tableDiscountCount);

        panelCountDiscount.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 580, 110));

        jbSaveCountDiscount.setText(resourceMap.getString("jbSaveCountDiscount.text")); // NOI18N
        jbSaveCountDiscount.setName("jbSaveCountDiscount"); // NOI18N
        jbSaveCountDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveCountDiscountActionPerformed(evt);
            }
        });
        panelCountDiscount.add(jbSaveCountDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 130, 190, 30));

        jPanel1.add(panelCountDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 600, 170));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 630, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        // Wcisniety "Cofnij"
        doClose(RET_CANCEL);
}//GEN-LAST:event_jbCancelActionPerformed

    private void jbSaveDistanceDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveDistanceDiscountActionPerformed
        if (tableDiscountDistance.isEditing()) {
            tableDiscountDistance.getCellEditor().stopCellEditing();
        }
        
        int count = 0;
        String data = "";
        
        try {
            for (int i = 0; i < tableDiscountDistance.getRowCount(); i++)
            {
                String s = YPFunctions.strGet(tableDiscountDistance,i, 0);
                if (!s.equals(""))
                {
                        double sumaRabatow =
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 2)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 3)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 4)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 5)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 6));

                        if ((sumaRabatow > 0) || (idCas == 0)) {
                            data += Integer.parseInt(YPFunctions.strGet(tableDiscountDistance,i, 1)) + ";" +
                                    YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 2)) + ";" +
                                    YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 3)) + ";" +
                                    YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 4)) + ";" +
                                    YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 5)) + ";" +
                                    YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 6)) + ";";
                            count++;
                        }
                }
            }
        } catch (Exception e) {
        }
        
        String s = (String)GlobalData.oraSession.executeFunction(
                   "MGR_DEFINE.F_UPDATE_PAL_RAB_DISTANCE(?,?,?,?,?)",
                   new Vector(Arrays.asList(idCas,
                                            platnik, 
                                            product, 
                                            count, 
                                            data)), 
                   OracleTypes.VARCHAR); 
        if (! s.equals("OK")) {
           this.setCursor(java.awt.Cursor.getDefaultCursor());
           YPFunctions.showErrorMessage("Nie udało się wykonać ustawienie słowników [" + s + "]");
           return;
        } else
           GlobalData.oraSession.SaveLog("Definicja rabatów odległościowych cennika paletowego ID_CUST=" + idCas ,"");
            
        
/*        
        
        

        if (idCas == 0) { // Cennik STD
                GlobalData.oraSession.executeQuery(
                        "Delete from dict_pal_rab_distance_std where id_pay=? and id_product=?",
                        new Vector(Arrays.asList(platnik, product)));

                for (int i = 0; i < tableDiscountDistance.getRowCount(); i++ )
                {
                   GlobalData.oraSession.executeQuery(
                            "INSERT INTO DICT_PAL_RAB_DISTANCE_STD" +
                                  "(id_pay,id_product,distance,rab_a,rab_b,rab_b1,rab_b2,rab_c)" +
                            "VALUES(?,?,?,?,?,?,?,?)",
                            new Vector(Arrays.asList(
                                     platnik,
                                     product,
                                     Integer.parseInt(YPFunctions.strGet(tableDiscountDistance,i, 1)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 2)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 3)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 4)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 5)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 6)))));
                }
        } else {
                GlobalData.oraSession.executeQuery(
                        "Delete from dict_pal_rab_distance where id_cust=? and id_pay=? and id_product=?",
                        new Vector(Arrays.asList(idCas, platnik, product)));

                for (int i = 0; i < tableDiscountDistance.getRowCount(); i++ )
                {
                    double sumaRabatow =
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 2)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 3)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 4)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 5)) +
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 6));
                    if (sumaRabatow > 0) {
                        GlobalData.oraSession.executeQuery(
                            "INSERT INTO DICT_PAL_RAB_DISTANCE" +
                                  "(id_cust,id_pay,id_product,distance,rab_a,rab_b,rab_b1,rab_b2,rab_c)" +
                            "VALUES(?,?,?,?,?,?,?,?,?)",
                            new Vector(Arrays.asList(
                                     idCas,
                                     platnik,
                                     product,
                                     Integer.parseInt(YPFunctions.strGet(tableDiscountDistance,i, 1)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 2)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 3)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 4)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 5)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountDistance,i, 6)))));
                    }
                }
        }
*/
        showScreen();
        YPFunctions.showMessage("Rabaty zostały zapisane i odczytane","");
}//GEN-LAST:event_jbSaveDistanceDiscountActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Dialog został zamknięty
        doClose(RET_CANCEL);
    }//GEN-LAST:event_formWindowClosing

    private void jbSaveCountDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveCountDiscountActionPerformed
        if (tableDiscountCount.isEditing()) {
            tableDiscountCount.getCellEditor().stopCellEditing();
        }
        int count = 0;
        String data = "";
        
        try {
            for (int i = 0; i < tableDiscountCount.getRowCount(); i++)
            {
                String s = YPFunctions.strGet(tableDiscountCount,i, 0);
                if (!s.equals(""))
                {
                        data += Integer.parseInt(YPFunctions.strGet(tableDiscountCount,i, 0)) + ";" +
                                YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountCount,i, 1)) + ";";
                        count++;
                }
            }
        } catch (Exception e) {
        }
        
        String s = (String)GlobalData.oraSession.executeFunction(
                   "MGR_DEFINE.F_UPDATE_PAL_RAB(?,?,?,?,?)",
                   new Vector(Arrays.asList(idCas,
                                            platnik, 
                                            product, 
                                            count, 
                                            data)), 
                   OracleTypes.VARCHAR); 
        if (! s.equals("OK")) {
           this.setCursor(java.awt.Cursor.getDefaultCursor());
           YPFunctions.showErrorMessage("Nie udało się wykonać ustawienie słowników [" + s + "]");
           return;
        }
/*
        
        
        
        
        
        if (tableDiscountCount.isEditing()) {
            tableDiscountCount.getCellEditor().stopCellEditing();
        }
      
        if (idCas == 0) { // STD
                GlobalData.oraSession.executeQuery(
                        "Delete from dict_pal_rab_std where id_pay=? and id_product=?",
                        new Vector(Arrays.asList(platnik, product)));
                try {
                    for (int i = 0; i < tableDiscountCount.getRowCount(); i++)
                    {
                        String s = YPFunctions.strGet(tableDiscountCount,i, 0);
                        if (!s.equals(""))
                        {
                            GlobalData.oraSession.executeQuery(
                            "INSERT INTO DICT_PAL_RAB_std" +
                                  "(volume,upust,id_pay,id_product)" +
                            "VALUES(?,?,?,?)",
                            new Vector(Arrays.asList(
                                     Integer.parseInt(YPFunctions.strGet(tableDiscountCount,i, 0)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountCount,i, 1)),
                                     platnik,
                                     product)));
                        }
                    }
                } catch (Exception e) {
                }
        } else {
                GlobalData.oraSession.executeQuery(
                        "Delete from dict_pal_rab where id_cust=? and id_pay=? and id_product=?",
                        new Vector(Arrays.asList(idCas, platnik, product)));
                try {
                    for (int i = 0; i < tableDiscountCount.getRowCount(); i++)
                    {
                        String s = YPFunctions.strGet(tableDiscountCount,i, 0);
                        if (!s.equals(""))
                        {
                            GlobalData.oraSession.executeQuery(
                            "INSERT INTO DICT_PAL_RAB" +
                                  "(id_cust,volume,upust,id_pay,id_product)" +
                            "VALUES(?,?,?,?,?)",
                            new Vector(Arrays.asList(
                                     idCas,
                                     Integer.parseInt(YPFunctions.strGet(tableDiscountCount,i, 0)),
                                     YPFunctions.nvlParseDouble(YPFunctions.strGet(tableDiscountCount,i, 1)),
                                     platnik,
                                     product)));
                        }
                    }
                } catch (Exception e) {
                }
        }
*/
        showScreen();
        YPFunctions.showMessage("Rabaty zostały zapisane i odczytane","");
    }//GEN-LAST:event_jbSaveCountDiscountActionPerformed

    private void jbSaveLineDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveLineDiscountActionPerformed
        double d = YPFunctions.nvlParseDouble(tfLineDiscount.getText());
        GlobalData.oraSession.executeProcedure(
                "MGR_DEFINE.SET_PAL_RAB_LINE(?,?,?,?)",
                new Vector(Arrays.asList(
                idCas, platnik, product, d)));
        GlobalData.oraSession.SaveLog("Definicja rabatów liniowych cennika paletowego ID_CUST=" + idCas ,"");

        showScreen();
        YPFunctions.showMessage("Rabaty zostały zapisane i odczytane","");
}//GEN-LAST:event_jbSaveLineDiscountActionPerformed

    private void doClose(int retStatus) {
        // Zamykamy dialog
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**  zwraca informacje o przycisku OK czy Cofnij nacisnął użytkownik
    */
    public int getReturnStatus() {
        // zmienna returnStatus jest prywatna, robimy publiczną metodę,
        // żeby zwrócić informację który przycisk (Ok czy Cofnij) nacisnął
        // użytkownik
        return returnStatus;
    }



    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PaletyRabatyDialog dialog = new PaletyRabatyDialog(new javax.swing.JFrame(), true,0,"","");
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbSaveCountDiscount;
    private javax.swing.JButton jbSaveDistanceDiscount;
    private javax.swing.JButton jbSaveLineDiscount;
    private javax.swing.JPanel panelCountDiscount;
    private javax.swing.JPanel panelDistanceDiscount;
    private javax.swing.JPanel panelLineDiscount;
    private javax.swing.JTable tableDiscountCount;
    private javax.swing.JTable tableDiscountDistance;
    private javax.swing.JTextField tfLineDiscount;
    // End of variables declaration//GEN-END:variables
    public int RET_CANCEL = 0;
    public int RET_OK = 1;
    private int returnStatus = RET_CANCEL;
    public String product;
    public String platnik;
    public int idCas;
}
