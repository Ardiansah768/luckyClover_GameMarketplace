/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package marketplace;
 
/**
 *
 * @author Administrator
 */
public class AdminDashboardFrame extends javax.swing.JFrame {
 
    private int selectedGameId = -1;
    private static final String[] CATEGORIES = {"RPG", "Action", "Sandbox", "Sports", "Strategy", "Simulation"};
    private static final String[] TYPES = {"Game", "Featured", "Bundle"};
 
    public AdminDashboardFrame(String adminUsername) {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Admin Dashboard - Game Marketplace");
        lblHi.setText("Hi, " + adminUsername + " (Admin)");
        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(CATEGORIES));
        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(TYPES));
        setupTable();
        loadTable();
        tblGames.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillForm();
        });
        btnAdd.addActionListener(e -> doAdd());
        btnUpdate.addActionListener(e -> doUpdate());
        btnDelete.addActionListener(e -> doDelete());
btnClear.addActionListener(e -> clearForm());
btnLogout.addActionListener(e -> {
    new LoginFrame().setVisible(true);
    dispose();
});
    }
 
    private void setupTable() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new String[]{"ID", "Nama Game", "Harga", "Kategori", "Tipe"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblGames.setModel(model);
        tblGames.getColumnModel().getColumn(0).setMinWidth(0);
        tblGames.getColumnModel().getColumn(0).setMaxWidth(0);
        tblGames.getColumnModel().getColumn(0).setWidth(0);
        tblGames.setRowHeight(28);
        tblGames.getTableHeader().setReorderingAllowed(false);
    }
 
    private void loadTable() {
        javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) tblGames.getModel();
        model.setRowCount(0);
        for (AbstractProduct p : DatabaseManager.getAllGames()) {
            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                String.format("Rp %,.0f", p.getPrice()),
                p.getCategory(),
                p instanceof BundleGame ? "Bundle"
                    : p instanceof FeaturedGame ? "Featured" : "Game"
            });
        }
        selectedGameId = -1;
    }
 
    private void fillForm() {
        int row = tblGames.getSelectedRow();
        if (row < 0) return;
        int id = (int) tblGames.getModel().getValueAt(row, 0);
        AbstractProduct p = DatabaseManager.getGameById(id);
        if (p == null) return;
        selectedGameId = id;
        txtName.setText(p.getName());
        txtPrice.setText(String.valueOf((int) p.getPrice()));
        txtDeveloper.setText(p instanceof Game ? ((Game) p).getDeveloper() : "");
        txtRating.setText(String.valueOf(p.getRating()));
        txtImageUrl.setText(p.getImageUrl());
        txtDescription.setText(p.getDescription());
        for (int i = 0; i < CATEGORIES.length; i++)
            if (CATEGORIES[i].equals(p.getCategory())) { cbCategory.setSelectedIndex(i); break; }
        String type = p instanceof BundleGame ? "Bundle"
                    : p instanceof FeaturedGame ? "Featured" : "Game";
        for (int i = 0; i < TYPES.length; i++)
            if (TYPES[i].equals(type)) { cbType.setSelectedIndex(i); break; }
    }
 
    private void clearForm() {
        selectedGameId = -1;
        txtName.setText("");
        txtPrice.setText("");
        txtDeveloper.setText("");
        txtRating.setText("");
        txtImageUrl.setText("");
        txtDescription.setText("");
        cbCategory.setSelectedIndex(0);
        cbType.setSelectedIndex(0);
        tblGames.clearSelection();
    }
 
    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            showMsg("Nama game tidak boleh kosong!"); return false;
        }
        try {
            double price = Double.parseDouble(txtPrice.getText().trim());
            if (price < 0) { showMsg("Harga tidak boleh negatif!"); return false; }
        } catch (NumberFormatException e) {
            showMsg("Harga harus berupa angka!"); return false;
        }
        try {
            double rating = Double.parseDouble(txtRating.getText().trim());
            if (rating < 0 || rating > 5) {
                showMsg("Rating harus antara 0 sampai 5!"); return false;
            }
        } catch (NumberFormatException e) {
            showMsg("Rating harus berupa angka (contoh: 4.5)!"); return false;
        }
        if (txtDeveloper.getText().trim().isEmpty()) {
            showMsg("Developer tidak boleh kosong!"); return false;
        }
        return true;
    }
 
    private void doAdd() {
        if (!validateForm()) return;
        boolean ok = DatabaseManager.insertGame(
            txtName.getText().trim(),
            Double.parseDouble(txtPrice.getText().trim()),
            (String) cbCategory.getSelectedItem(),
            txtDescription.getText().trim(),
            txtImageUrl.getText().trim(),
            Double.parseDouble(txtRating.getText().trim()),
            txtDeveloper.getText().trim(),
            (String) cbType.getSelectedItem()
        );
        if (ok) { showMsg("Game berhasil ditambahkan!"); loadTable(); clearForm(); }
        else showMsg("Gagal menambahkan game. Cek koneksi database.");
    }
 
    private void doUpdate() {
        if (selectedGameId < 0) { showMsg("Pilih game dari tabel terlebih dahulu!"); return; }
        if (!validateForm()) return;
        boolean ok = DatabaseManager.updateGameFull(
            selectedGameId,
            txtName.getText().trim(),
            Double.parseDouble(txtPrice.getText().trim()),
            (String) cbCategory.getSelectedItem(),
            txtDescription.getText().trim(),
            txtImageUrl.getText().trim(),
            Double.parseDouble(txtRating.getText().trim()),
            txtDeveloper.getText().trim(),
            (String) cbType.getSelectedItem()
        );
        if (ok) { showMsg("Game berhasil diupdate!"); loadTable(); clearForm(); }
        else showMsg("Gagal mengupdate game.");
    }
 
    private void doDelete() {
        if (selectedGameId < 0) { showMsg("Pilih game dari tabel terlebih dahulu!"); return; }
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus game \"" + txtName.getText().trim() + "\"?",
            "Konfirmasi Hapus",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            boolean ok = DatabaseManager.deleteGame(selectedGameId);
            if (ok) { showMsg("Game berhasil dihapus!"); loadTable(); clearForm(); }
            else showMsg("Gagal menghapus game.");
        }
    }
 
    private void showMsg(String msg) {
        javax.swing.JOptionPane.showMessageDialog(this, msg, "Info",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblHi = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        scrollGames = new javax.swing.JScrollPane();
        tblGames = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbCategory = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtDeveloper = new javax.swing.JTextField();
        txtRating = new javax.swing.JTextField();
        txtImageUrl = new javax.swing.JTextField();
        scrollDesc = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));

        lblHi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHi.setForeground(new java.awt.Color(242, 242, 242));
        lblHi.setText("Hi,Admin");

        btnLogout.setBackground(new java.awt.Color(255, 51, 51));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(242, 242, 242));
        btnLogout.setText("LogOut");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(lblHi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        scrollGames.setForeground(new java.awt.Color(102, 102, 0));

        tblGames.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollGames.setViewportView(tblGames);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(242, 242, 242));
        jLabel1.setText("Nama");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(242, 242, 242));
        jLabel2.setText("Price");

        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(242, 242, 242));
        jLabel3.setText("Kategory");

        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(242, 242, 242));
        jLabel4.setText("Type");

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        scrollDesc.setViewportView(txtDescription);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(242, 242, 242));
        jLabel5.setText("Developer");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(242, 242, 242));
        jLabel6.setText("Rating");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(242, 242, 242));
        jLabel7.setText("ImageUrl");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(242, 242, 242));
        jLabel8.setText("Description");

        btnAdd.setText("Tambah");

        btnUpdate.setText("Update");

        btnDelete.setText("Delete");

        btnClear.setText("Clear");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(37, 37, 37)
                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4))
                                        .addGap(13, 13, 13)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDeveloper, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRating, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImageUrl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scrollDesc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(scrollGames, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollGames, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDeveloper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtImageUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(scrollDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        new LoginFrame().setVisible(true);
        dispose();
    }
 
    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) { }
    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) { }
    private void txtDeveloperActionPerformed(java.awt.event.ActionEvent evt) { }
    private void txtRatingActionPerformed(java.awt.event.ActionEvent evt) { }
    private void txtImageUrlActionPerformed(java.awt.event.ActionEvent evt) { }
 
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        doDelete();
    }
 
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {
        clearForm();
    }
    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JComboBox<String> cbType;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblHi;
    private javax.swing.JScrollPane scrollDesc;
    private javax.swing.JScrollPane scrollGames;
    private javax.swing.JTable tblGames;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtDeveloper;
    private javax.swing.JTextField txtImageUrl;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtRating;
    // End of variables declaration//GEN-END:variables
}
