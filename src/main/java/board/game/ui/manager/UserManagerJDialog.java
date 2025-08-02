/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package board.game.ui.manager;

import board.game.dao.UserDAO;
import board.game.dao.ipml.UserDAOimpl;
import board.game.entity.User;
import board.game.util.Auth;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LAPTOP LE SON
 */
public class UserManagerJDialog extends javax.swing.JDialog {

    UserDAO userdao = new UserDAOimpl();
    DefaultTableModel model;
    int currentIndex = -1;

    /**
     * Creates new form UserManagerJDialog
     */
    private JFrame parentFrame;

    private void setControlsEnabled(boolean enabled) {
        rdbManager.setEnabled(enabled);
        rdbPlayer.setEnabled(enabled);
        rdbActive.setEnabled(enabled);
        rdbUnactive.setEnabled(enabled);
        btnUpdate.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
    }

    public UserManagerJDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        this.parentFrame = parent;
        initComponents();
        fillTable();
        txtId.setEditable(false);

        handleRoleVisibilityOnStartup(); // 👈 Gọi xử lý ẩn
    }

    public UserManagerJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fillTable();
        txtId.setEditable(false);
    }

    boolean validateForm() {
        String id = txtId.getText().trim();

        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ID người dùng");
            txtId.requestFocus();
            return false;
        }

        if (!rdbActive.isSelected() && !rdbUnactive.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái");
            return false;
        }

        if (!rdbManager.isSelected() && !rdbPlayer.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vai trò");
            return false;
        }
        return true;
    }

    void handleRoleVisibilityOnStartup() {
        int role = Auth.user.getVaiTro();
        if (role == 2) { // Nếu Admin đăng nhập
            rdbManager.setVisible(false);
            rdbPlayer.setVisible(false);
            jLabel5.setVisible(false);
            // lblVaiTro.setVisible(false); // nếu có label đi kèm
        } else {
            rdbManager.setVisible(true);
            rdbPlayer.setVisible(true);
        }
    }

    void fillTable() {
        model = (DefaultTableModel) tblUsers.getModel();
        model.setRowCount(0);

        try {
            List<User> list = userdao.findAll();
            int currentRole = Auth.user.getVaiTro(); // Vai trò người đăng nhập

            for (User u : list) {
                int userRole = u.getVaiTro();

                // Nếu Admin đăng nhập → ẩn cả Dev (3) và Admin (2)
                if (currentRole == 2 && (userRole == 2 || userRole == 3)) {
                    continue;
                }

                // Nếu Dev đăng nhập → ẩn Dev, nhưng vẫn hiển thị Admin
                if (currentRole == 3 && userRole == 3) {
                    continue;
                }

                String vaiTroStr = switch (userRole) {
                    case 1 ->
                        "Người chơi";
                    case 2 ->
                        "Admin";
                    case 3 ->
                        "Dev";
                    default ->
                        "Không xác định";
                };

                model.addRow(new Object[]{
                    u.getIdNguoiDung(),
                    u.isTrangThai() ? "Còn hoạt động" : "Ngừng hoạt động",
                    vaiTroStr,
                    false
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi " + e.getMessage());
        }
    }

    User getForm() {
        User u = new User();
        u.setIdNguoiDung(txtId.getText());
        u.setTrangThai(rdbActive.isSelected());
        u.setVaiTro(rdbManager.isSelected() ? 2 : 1); // Không có radio Dev nên chỉ 2 hoặc 1
        return u;
    }

    void setForm(User u) {
        txtId.setText(u.getIdNguoiDung());
        rdbManager.setSelected(u.getVaiTro() == 2); // Admin
        rdbPlayer.setSelected(u.getVaiTro() == 1); // Player
        rdbActive.setSelected(u.isTrangThai());
        rdbUnactive.setSelected(!u.isTrangThai());

        rdbActive.setEnabled(true);
        rdbUnactive.setEnabled(true);

        // Xử lý phân quyền chỉnh vai trò
        int role = Auth.user.getVaiTro();
        boolean canEditRole = role == 3 && u.getVaiTro() != 3;

        if (!canEditRole) {
            rdbManager.setEnabled(false);
            rdbPlayer.setEnabled(false);

            for (MouseListener ml : rdbManager.getMouseListeners()) {
                rdbManager.removeMouseListener(ml);
            }
            for (MouseListener ml : rdbPlayer.getMouseListeners()) {
                rdbPlayer.removeMouseListener(ml);
            }
        }

        // Nếu người đăng nhập là Admin thì ẩn luôn radio
        if (role == 2) { // 2 = Admin
            rdbManager.setVisible(false);
            rdbPlayer.setVisible(false);
            // Nếu có label "Vai trò" thì ẩn luôn:
            // lblVaiTro.setVisible(false);
        } else {
            rdbManager.setVisible(true);
            rdbPlayer.setVisible(true);
            // lblVaiTro.setVisible(true);
        }
    }

    void insert() {
        if (!validateForm()) {
            return;
        }

        if (rdbUnactive.isSelected()) {
            JOptionPane.showMessageDialog(this, "Không thể tạo tài khoản với trạng thái 'Ngừng hoạt động'.");
            return;
        }
        User u = getForm();
        u.setMatKhau("pass999");
        try {
            userdao.create(u);
            fillTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    void update() {
        if (!validateForm()) {
            return;
        }

        User u = getForm(); // Lấy dữ liệu từ form (bao gồm cả trạng thái đã chọn)

        try {
            userdao.update(u); // Cập nhật xuống cơ sở dữ liệu

            fillTable(); // Cập nhật lại bảng

            if (!u.isTrangThai()) {
                JOptionPane.showMessageDialog(this, "Tài khoản đã được chuyển sang trạng thái dừng hoạt động.");

                // Vô hiệu hóa các thành phần giao diện
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
                rdbManager.setEnabled(false);
                rdbPlayer.setEnabled(false);
                rdbActive.setEnabled(false);
                rdbUnactive.setEnabled(false);

                return;
            }

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void delete() {
        String id = txtId.getText();
        if (JOptionPane.showConfirmDialog(this, "Xóa người dùng: " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                userdao.deleteById(id);
                fillTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    void edit(int index) {
        try {
            String id = (String) tblUsers.getValueAt(index, 0);
            User u = userdao.findById(id);
            currentIndex = index;

            // Gọi setForm xử lý UI + quyền
            setForm(u);

            // Luôn cho phép chỉnh trạng thái và cập nhật
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);

            // Nếu tài khoản bị khóa, cảnh báo
            if (!u.isTrangThai()) {
                JOptionPane.showMessageDialog(this, "Tài khoản đang ngừng hoạt động.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi hiển thị: " + e.getMessage());
        }
    }

    void clearForm() {
        txtId.setText("");
        rdbManager.setSelected(true);
        rdbActive.setSelected(true);
        currentIndex = -1;
    }

    void moveFirst() {
        edit(0);
    }

    void movePrevious() {
        if (currentIndex < tblUsers.getRowCount() - 1) {
            edit(++currentIndex);
        }
    }

    void moveNext() {
        if (currentIndex > 0) {
            edit(--currentIndex);
        }
    }

    void moveLast() {
        edit(tblUsers.getRowCount() - 1);
    }

    void checkAll() {
        for (int i = 0; i < tblUsers.getRowCount(); i++) {
            tblUsers.setValueAt(true, i, 3);
        }
    }

    void uncheckAll() {
        for (int i = 0; i < tblUsers.getRowCount(); i++) {
            tblUsers.setValueAt(false, i, 3);
        }
    }

    void deleteCheckedItems() {
        if (JOptionPane.showConfirmDialog(this, "Xóa các mục đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for (int i = tblUsers.getRowCount() - 1; i >= 0; i--) {
                Boolean checked = (Boolean) tblUsers.getValueAt(i, 3);
                if (checked != null && checked) {
                    String id = (String) tblUsers.getValueAt(i, 0);
                    userdao.deleteById(id);
                    ((DefaultTableModel) tblUsers.getModel()).removeRow(i);
                }
            }
            JOptionPane.showMessageDialog(this, "Đã xóa các mục đã chọn!");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        chontatca = new javax.swing.JButton();
        bochontatca = new javax.swing.JButton();
        xoacacmuc = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdbManager = new javax.swing.JRadioButton();
        rdbPlayer = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        rdbActive = new javax.swing.JRadioButton();
        rdbUnactive = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnMoveFirst = new javax.swing.JButton();
        btnMoveNext = new javax.swing.JButton();
        btnMovePrevious = new javax.swing.JButton();
        btnMoveLast = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Trạng thái", "Vai trò", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsers);

        chontatca.setText("Chọn tất cả");
        chontatca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chontatcaActionPerformed(evt);
            }
        });

        bochontatca.setText("Bỏ chọn tất cả");
        bochontatca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bochontatcaActionPerformed(evt);
            }
        });

        xoacacmuc.setText("Xóa các mục đã chọn");
        xoacacmuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoacacmucActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(chontatca, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bochontatca, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(xoacacmuc)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xoacacmuc)
                    .addComponent(chontatca)
                    .addComponent(bochontatca))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(293, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabs.addTab("Danh sách", jPanel1);

        jLabel1.setText("ID");

        jLabel5.setText("Vai trò");

        buttonGroup1.add(rdbManager);
        rdbManager.setText("Admin");
        rdbManager.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdbManagerMouseClicked(evt);
            }
        });
        rdbManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbManagerActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdbPlayer);
        rdbPlayer.setText("Người chơi");
        rdbPlayer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdbPlayerMouseClicked(evt);
            }
        });
        rdbPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbPlayerActionPerformed(evt);
            }
        });

        jLabel6.setText("Trạng thái");

        buttonGroup2.add(rdbActive);
        rdbActive.setText("Còn hoạt động");

        buttonGroup2.add(rdbUnactive);
        rdbUnactive.setText("Ngừng hoạt động");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(rdbActive)
                                .addGap(18, 18, 18)
                                .addComponent(rdbUnactive)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(rdbManager)
                                .addGap(18, 18, 18)
                                .addComponent(rdbPlayer)))))
                .addGap(272, 272, 272))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdbManager)
                            .addComponent(rdbPlayer)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(jLabel6)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdbActive)
                            .addComponent(rdbUnactive))))
                .addGap(54, 54, 54))
        );

        btnCreate.setText("Tạo mới");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setText("Nhập mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnMoveFirst.setText("|<<");
        btnMoveFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveFirstActionPerformed(evt);
            }
        });

        btnMoveNext.setText("<<");
        btnMoveNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveNextActionPerformed(evt);
            }
        });

        btnMovePrevious.setText(">>");
        btnMovePrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovePreviousActionPerformed(evt);
            }
        });

        btnMoveLast.setText(">>|");
        btnMoveLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMoveFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveNext, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMovePrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveLast, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoveLast, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMovePrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoveNext, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoveFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        tabs.addTab("Biền mẫu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chontatcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chontatcaActionPerformed
        // TODO add your handling code here:
        checkAll();
    }//GEN-LAST:event_chontatcaActionPerformed

    private void bochontatcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bochontatcaActionPerformed
        // TODO add your handling code here:
        uncheckAll();
    }//GEN-LAST:event_bochontatcaActionPerformed

    private void xoacacmucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoacacmucActionPerformed
        // TODO add your handling code here:
        deleteCheckedItems();
    }//GEN-LAST:event_xoacacmucActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnMoveFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveFirstActionPerformed
        // TODO add your handling code here:
        moveFirst();
    }//GEN-LAST:event_btnMoveFirstActionPerformed

    private void btnMoveNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveNextActionPerformed
        // TODO add your handling code here:
        moveNext();
    }//GEN-LAST:event_btnMoveNextActionPerformed

    private void btnMovePreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovePreviousActionPerformed
        // TODO add your handling code here:
        movePrevious();
    }//GEN-LAST:event_btnMovePreviousActionPerformed

    private void btnMoveLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveLastActionPerformed
        // TODO add your handling code here:
        moveLast();
    }//GEN-LAST:event_btnMoveLastActionPerformed

    private void tblUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsersMouseClicked
        // TODO add your handling code here:
        int row = tblUsers.getSelectedRow();
        if (row >= 0) {
            edit(row);
        }
    }//GEN-LAST:event_tblUsersMouseClicked

    private void rdbManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbManagerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbManagerActionPerformed

    private void rdbPlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbPlayerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbPlayerActionPerformed

    private void rdbManagerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdbManagerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbManagerMouseClicked

    private void rdbPlayerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdbPlayerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbPlayerMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.htmtabs*/
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UserManagerJDialog dialog = new UserManagerJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton bochontatca;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnMoveFirst;
    private javax.swing.JButton btnMoveLast;
    private javax.swing.JButton btnMoveNext;
    private javax.swing.JButton btnMovePrevious;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton chontatca;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdbActive;
    private javax.swing.JRadioButton rdbManager;
    private javax.swing.JRadioButton rdbPlayer;
    private javax.swing.JRadioButton rdbUnactive;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTextField txtId;
    private javax.swing.JButton xoacacmuc;
    // End of variables declaration//GEN-END:variables
}
