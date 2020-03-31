/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p_18090090;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Notebook
 */
public class JavaMySQL extends javax.swing.JFrame {
    int idBaris=0;
    String role;
    DefaultTableModel model;
    /**
     * Creates new form JavaMySQL
     */
    public JavaMySQL() {
        initComponents();
        KoneksiDB.sambungDB();
        aturModelTabel();
        pangkat();
        jafung();
        showForm(false);
        showData("");
    }
    
    private void aturModelTabel(){
        Object[] kolom = {"No","NIDN","NAMA","JAFUNG","Golongan/Pangkat","No HP"};
        model = new DefaultTableModel(kolom,0) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        tabel.setModel(model);
        tabel.setRowHeight(20);
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
    }
    private void showForm(boolean b){
        areaSplit.setDividerLocation(0.3);
        areaSplit.getLeftComponent().setVisible(b);
    }
    
    private void resetForm(){
        tabel.clearSelection();
        txt_NIDN.setText("");
        txt_nama.setText("");
        cmb_jafung.setSelectedIndex(0);
        cmb_pangkat.setSelectedIndex(0);
        txt_nohp.setText("");
        txt_NIDN.requestFocus();
    }
    
    private void pangkat(){
        cmb_pangkat.removeAllItems();
        cmb_pangkat.addItem("Pilih Golongan/Pangkat");
        cmb_pangkat.addItem("Belum ada");
        cmb_pangkat.addItem("II/c (Pengatur)");
        cmb_pangkat.addItem("II/d (Pengatur Tk. I)");
        cmb_pangkat.addItem("III/a (Penata Muda)");
        cmb_pangkat.addItem("III/b (Penata Muda Tk. I)");
        cmb_pangkat.addItem("III/c (Penata)");
        cmb_pangkat.addItem("III/d (Penata Tk. I)");
        cmb_pangkat.addItem("IV/a (Pembina)");
        cmb_pangkat.addItem("IV/b (Pembina Tk. I)");
        cmb_pangkat.addItem("IV/c (Pembina Utama Muda)");
        cmb_pangkat.addItem("IV/d (Pembina Utama Madya)");
        cmb_pangkat.addItem("IV/e (Pembina Utama)");
    }
    
    private void jafung(){
        cmb_jafung.removeAllItems();
        cmb_jafung.addItem("Pilih Golongan/Pangkat");
        cmb_jafung.addItem("Belum ada");
        cmb_jafung.addItem("Asisten Ahli (100)");
        cmb_jafung.addItem("Asisten Ahli (150)");
        cmb_jafung.addItem("Lektor (200)");
        cmb_jafung.addItem("Lektor (300)");
        cmb_jafung.addItem("Lektor Kepala (400)");
        cmb_jafung.addItem("Lektor Kepala (550)");
        cmb_jafung.addItem("Lektor Kepala (700)");
        cmb_jafung.addItem("Profesor (850)");
        cmb_jafung.addItem("Profesor (1050)");
    }
    
    private void showData(String key){
        model.getDataVector().removeAllElements();         
        String where = "";         
        if(!key.isEmpty()){
            where += "WHERE nidn LIKE '%"+key+"%' "                     
                    + "OR nama LIKE '%"+key+"%' "                    
                    + "OR jabatan_fungsional LIKE '%"+key+"%' "                     
                    + "OR pangkat_golongan LIKE '%"+key+"%' "                    
                    + "OR no_hp LIKE '%"+key+"%'"; 
        }
        String sql = "SELECT * FROM data_dosen "+where;                 
        Connection con;         
        Statement st;        
        ResultSet rs;         
        int baris = 0;         
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Object id = rs.getInt(1);
                Object nidn = rs.getString(2);
                Object nama = rs.getString(3);
                Object jafung = rs.getString(4);
                Object pangkat = rs.getString(5);
                Object no_hp = rs.getString(6);
                Object[] data = {id,nidn,nama,jafung,pangkat,no_hp};
                model.insertRow(baris, data);
                baris++;
            }
            st.close();
            con.close();
            tabel.revalidate();
            tabel.repaint();
        } catch (SQLException e) {
            System.err.println("showData(): "+e.getMessage());
        }
    }
    
    private void resetView(){
        resetForm();
        showForm(false);
        showData("");
        btn_hapus.setEnabled(false);
        idBaris = 0;
    }
    
    private void pilihData(String n){
        btn_hapus.setEnabled(true);
        String sql = "SELECT * FROM data_dosen WHERE id='"+n+"'";
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String nidn = rs.getString(2);
                String nama = rs.getString(3);
                Object jafung = rs.getString(4);
                Object pangkat = rs.getString(5);
                String no_hp = rs.getString(6);
                idBaris = id;
                txt_NIDN.setText(nidn);
                txt_nama.setText(nama);
                cmb_jafung.setSelectedItem(jafung);
                cmb_jafung.setSelectedItem(pangkat);
                txt_nohp.setText(no_hp);
            }
            st.close();
            con.close();
            showForm(true);
        } catch (SQLException e) {
            System.err.println("pilihData(): "+e.getMessage());
        }
    }
    
    private void simpanData(){
        String nidn = txt_NIDN.getText();
        String nama = txt_nama.getText();
        int jafung = cmb_jafung.getSelectedIndex();
        int pangkat = cmb_pangkat.getSelectedIndex();
        String nohp = txt_nohp.getText();
        if(nidn.isEmpty() || nama.isEmpty() || jafung==0 || pangkat==0 ||
                nohp.isEmpty()){
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
        }else{
            String jafung_isi = cmb_jafung.getSelectedItem().toString();
            String pangkat_isi = cmb_pangkat.getSelectedItem().toString();
            String sql =
                    "INSERT INTO data_dosen (nidn,nama,jabatan_fungsional,"
                    + "pangkat_golongan,no_hp) "
                    + "VALUES (\""+nidn+"\",\""+nama+"\","
                    + "\""+jafung_isi+"\",\""+pangkat_isi+"\",\"" +nohp+ "\")";
            Connection con;
            Statement st;
            try {
                con = KoneksiDB.sambungDB();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this,"Data telah disimpan!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    
    private void ubahData(){
        String nidn = txt_NIDN.getText();
        String nama = txt_nama.getText();
        int jafung = cmb_jafung.getSelectedIndex();
        int pangkat = cmb_pangkat.getSelectedIndex();
        String nohp = txt_nohp.getText();
        if(nidn.isEmpty() || nama.isEmpty() || jafung==0 || pangkat==0 ||
                nohp.isEmpty()){
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
        }else{
            String jafung_isi = cmb_jafung.getSelectedItem().toString();
            String pangkat_isi = cmb_pangkat.getSelectedItem().toString();
            String sql = "UPDATE data_dosen "
                    + "SET nidn=\""+nidn+"\","
                    + "nama=\""+nama+"\","
                    + "jabatan_fungsional=\""+jafung_isi+"\","
                    + "pangkat_golongan=\""+pangkat_isi+"\","
                    + "no_hp=\""+nohp+"\" WHERE id=\""+idBaris+"\"";
            Connection con;
            Statement st;
            try {
                con = KoneksiDB.sambungDB();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this,"Data telah diubah!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    
    private void hapusData(int baris){
        Connection con;
        Statement st;
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            st.executeUpdate("DELETE FROM data_dosen WHERE id="+baris);
            st.close();
            con.close();
            resetView();
            JOptionPane.showMessageDialog(this, "Data telah dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        areaSplit = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        lbl_jabatan = new javax.swing.JLabel();
        cmb_jafung = new javax.swing.JComboBox<String>();
        txt_NIDN = new javax.swing.JTextField();
        txt_nama = new javax.swing.JTextField();
        cmb_pangkat = new javax.swing.JComboBox<String>();
        lbl_nidn = new javax.swing.JLabel();
        lbl_nama = new javax.swing.JLabel();
        lbl_pangkat = new javax.swing.JLabel();
        lbl_nohp = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btn_simpan = new javax.swing.JButton();
        btn_tutup = new javax.swing.JButton();
        txt_nohp = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 60));

        btn_cari.setText("Cari");
        btn_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_cariKeyReleased(evt);
            }
        });

        btn_tambah.setText("Tambah Data");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_hapus.setText("Hapus Data");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_tambah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cari)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cari)
                    .addComponent(btn_tambah)
                    .addComponent(btn_hapus))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        areaSplit.setDividerLocation(300);
        areaSplit.setDividerSize(20);
        areaSplit.setOneTouchExpandable(true);

        lbl_jabatan.setText("JABATAN FUNGSIONAL : ");

        cmb_jafung.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmb_pangkat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbl_nidn.setText("NIDN :");

        lbl_nama.setText("NAMA :");

        lbl_pangkat.setText("PANGKAT/GOLONGAN :");

        lbl_nohp.setText("NO HP :");

        btn_simpan.setText("SIMPAN");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_tutup.setText("TUTUP");
        btn_tutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tutupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbl_jabatan)
                                .addComponent(lbl_nama)
                                .addComponent(lbl_nidn))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbl_nohp)
                                .addComponent(lbl_pangkat)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_NIDN)
                            .addComponent(cmb_jafung, 0, 1, Short.MAX_VALUE)
                            .addComponent(txt_nama)
                            .addComponent(cmb_pangkat, 0, 1, Short.MAX_VALUE)
                            .addComponent(txt_nohp))
                        .addContainerGap())
                    .addComponent(jSeparator1)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tutup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_simpan)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_NIDN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_nidn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_nama))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_jafung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_jabatan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_pangkat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pangkat))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_nohp)
                    .addComponent(txt_nohp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_simpan)
                    .addComponent(btn_tutup))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        areaSplit.setLeftComponent(jPanel3);

        tabel.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel);

        areaSplit.setRightComponent(jScrollPane1);

        jPanel2.add(areaSplit, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        role = "Tambah";
        btn_simpan.setText("SIMPAN");
        idBaris = 0;
        resetForm();
        showForm(true);
        btn_hapus.setEnabled(false);
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        if(role.equals("Tambah")){
            simpanData();
        } else if (role.equals("Ubah")){
            ubahData();
        }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_tutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tutupActionPerformed
        resetForm();
        showForm(false);
        btn_hapus.setEnabled(false);
        idBaris = 0;
    }//GEN-LAST:event_btn_tutupActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        if(idBaris == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan"
            + "dihapus");
        } else{
            hapusData(idBaris);
        }
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cariKeyReleased
        String key = txt_cari.getText();
        showData(key);
    }//GEN-LAST:event_btn_cariKeyReleased

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        areaSplit.setDividerLocation(0.3);
    }//GEN-LAST:event_formComponentResized

    private void tabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMouseClicked
        role = "Ubah";
        int row = tabel.getRowCount();
        if(row > 0) {
            int sel = tabel.getSelectedRow();
            if(sel != -1){
                pilihData(tabel.getValueAt(sel, 0).toString());
                btn_simpan.setText("UBAH DATA");
            }
        }
    }//GEN-LAST:event_tabelMouseClicked

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
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JavaMySQL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane areaSplit;
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_tutup;
    private javax.swing.JComboBox<String> cmb_jafung;
    private javax.swing.JComboBox<String> cmb_pangkat;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_jabatan;
    private javax.swing.JLabel lbl_nama;
    private javax.swing.JLabel lbl_nidn;
    private javax.swing.JLabel lbl_nohp;
    private javax.swing.JLabel lbl_pangkat;
    private javax.swing.JTable tabel;
    private javax.swing.JTextField txt_NIDN;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JTextField txt_nohp;
    // End of variables declaration//GEN-END:variables
}
