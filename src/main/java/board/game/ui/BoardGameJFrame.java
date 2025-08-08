/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package board.game.ui;

import board.game.dao.DiemDAO.DiemService;
import board.game.util.Auth;
import board.game.util.XIcon;
import board.game.util.XJdbc;
import java.sql.ResultSet;
import javax.swing.JLabel;

/**
 *
 * @author LAPTOP LE SON
 */
public class BoardGameJFrame extends javax.swing.JFrame implements BoardGameController {

    public static JLabel lbtitleGlobal;
    private String userId;
    private String currentUserId;

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String id) {
        this.currentUserId = id;
    }

    /**
     * Creates new form boardgameJframe
     */
    public BoardGameJFrame(String userId, int role) {
        initComponents();
        this.userId = userId.trim();
        this.setCurrentUserId(this.userId);

        lbtitleGlobal = this.lbtitle;
        loadUserTitle();
        applyUserTitleColor(userId);
        lbtitleGlobal = this.lbtitle;

        if (Auth.user.getVaiTro() == 2) { // Admin
            btnUserManager.setText("Quản lý người chơi");
        } else if (Auth.user.getVaiTro() == 3) { // Dev
            btnUserManager.setText("Quản lý người dùng");
        }

        this.role = role;
        lblUserID.setText("User ID: " + userId);

        btnUserManager.setVisible(role == 2 || role == 3);
        btnChangePassword.setVisible(true);

        // Gán nhãn vai trò
        String roleName = switch (role) {
            case 1 ->
                "Player";
            case 2 ->
                "Admin";
            case 3 ->
                "Dev";
            default ->
                "Unknown";
        };
        lblName.setText(roleName);

        setLocationRelativeTo(null);
        loadScores(userId);
    }

    private void loadUserTitle() {
        String sql = "SELECT title, colorHex FROM UserTitleColors WHERE idNguoiDung = ? AND dangChon = 1";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, userId);
            if (rs.next()) {
                String title = rs.getString("title");
                String colorHex = rs.getString("colorHex");

                lbtitleGlobal.setText(title);

                if (colorHex != null && !colorHex.trim().isEmpty()) {
                    try {
                        java.awt.Color color = java.awt.Color.decode(colorHex.trim());
                        lbtitleGlobal.setForeground(color);
                    } catch (NumberFormatException ex) {
                        lbtitleGlobal.setForeground(java.awt.Color.BLACK);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void applyUserTitleColor(String userId) {
        try {
            String sql = "SELECT title, colorHex FROM UserTitleColors WHERE idNguoiDung = ? AND dangChon = 1";
            ResultSet rs = XJdbc.executeQuery(sql, userId);
            if (rs.next()) {
                String title = rs.getString("title");
                String colorHex = rs.getString("colorHex");

                lbtitleGlobal.setText(title);
                if (colorHex != null && !colorHex.trim().isEmpty()) {
                    try {
                        java.awt.Color color = java.awt.Color.decode(colorHex.trim());
                        lbtitleGlobal.setForeground(color);
                    } catch (NumberFormatException ex) {
                        lbtitleGlobal.setForeground(java.awt.Color.BLACK); // hoặc màu mặc định
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadScores(String userId) {
        DiemService diemService = new DiemService();

        int flappyScore = diemService.getScore(userId, "game001"); // Flappy Bird
        int meteorScore = diemService.getScore(userId, "game002"); // Bắn Gà

        lbbird.setText("Điểm Flappy Bird: " + flappyScore);
        lbmetoer.setText("Điểm Meteor: " + meteorScore);
    }

    private int role;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        lblUser = new javax.swing.JLabel();
        btnFlappy = new javax.swing.JButton();
        btnShot = new javax.swing.JButton();
        btnChangePassword = new javax.swing.JButton();
        btnUserManager = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblUserID = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lbbird = new javax.swing.JLabel();
        lbmetoer = new javax.swing.JLabel();
        btnnew = new javax.swing.JButton();
        lbtitle = new javax.swing.JLabel();
        btnreward = new javax.swing.JButton();
        btntitle = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblName.setFont(new java.awt.Font("Serif", 0, 28)); // NOI18N
        lblName.setForeground(new java.awt.Color(102, 102, 102));
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblName.setText("User");
        jPanel5.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 290, 30));

        jButton6.setBackground(new java.awt.Color(244, 241, 233));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 51, 51));
        jButton6.setText("Thoát");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 520, 137, -1));

        lblUser.setBackground(new java.awt.Color(255, 255, 255));
        lblUser.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblUser.setForeground(new java.awt.Color(51, 51, 51));
        lblUser.setText("Chào mừng bạn đến GameHub");
        jPanel5.add(lblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 58, -1, 60));

        btnFlappy.setBackground(new java.awt.Color(51, 91, 160));
        btnFlappy.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnFlappy.setForeground(new java.awt.Color(255, 255, 255));
        btnFlappy.setText("Flappy Bird");
        btnFlappy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFlappyActionPerformed(evt);
            }
        });
        jPanel5.add(btnFlappy, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 230, 190, 40));

        btnShot.setBackground(new java.awt.Color(51, 91, 160));
        btnShot.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnShot.setForeground(new java.awt.Color(255, 255, 255));
        btnShot.setText("Meteor");
        btnShot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShotActionPerformed(evt);
            }
        });
        jPanel5.add(btnShot, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 340, 190, 40));

        btnChangePassword.setBackground(new java.awt.Color(42, 67, 136));
        btnChangePassword.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnChangePassword.setForeground(new java.awt.Color(51, 204, 255));
        btnChangePassword.setText("Đổi mật khẩu");
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });
        jPanel5.add(btnChangePassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 470, 150, 40));

        btnUserManager.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnUserManager.setText("Quản lý người dùng");
        btnUserManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserManagerActionPerformed(evt);
            }
        });
        jPanel5.add(btnUserManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 160, 210, 40));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/board/game/icons/flappy-bird-sprite.png"))); // NOI18N
        jLabel7.setToolTipText("");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 230, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/board/game/icons/spaceship.png"))); // NOI18N
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 340, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/board/game/icons/avatar_resized_150x150.jpg"))); // NOI18N
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, -1, -1));

        lblUserID.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        lblUserID.setForeground(new java.awt.Color(102, 102, 102));
        lblUserID.setText("User ID:");
        jPanel5.add(lblUserID, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, -1));

        jButton1.setBackground(new java.awt.Color(244, 241, 233));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 51, 51));
        jButton1.setText("Đăng xuất");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 460, 140, 30));

        lbbird.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbbird.setForeground(new java.awt.Color(0, 102, 255));
        lbbird.setText("Điểm:");
        jPanel5.add(lbbird, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 240, 500, 30));

        lbmetoer.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbmetoer.setForeground(new java.awt.Color(0, 102, 255));
        lbmetoer.setText("Điểm:");
        jPanel5.add(lbmetoer, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 350, 500, 30));

        btnnew.setBackground(new java.awt.Color(42, 67, 136));
        btnnew.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnnew.setForeground(new java.awt.Color(51, 204, 255));
        btnnew.setText("Làm mới điểm");
        btnnew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnewActionPerformed(evt);
            }
        });
        jPanel5.add(btnnew, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 470, 150, 40));

        lbtitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbtitle.setText("Title");
        jPanel5.add(lbtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 290, 40));

        btnreward.setBackground(new java.awt.Color(51, 91, 160));
        btnreward.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnreward.setForeground(new java.awt.Color(153, 255, 255));
        btnreward.setText("Reward");
        btnreward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrewardActionPerformed(evt);
            }
        });
        jPanel5.add(btnreward, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 560, 150, 40));

        btntitle.setBackground(new java.awt.Color(51, 91, 160));
        btntitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btntitle.setForeground(new java.awt.Color(153, 255, 255));
        btntitle.setText("Title");
        btntitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntitleActionPerformed(evt);
            }
        });
        jPanel5.add(btntitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 560, 150, 40));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/board/game/icons/background homeplayer.png"))); // NOI18N
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 720));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        // TODO add your handling code here:
        this.showChangePasswordJDilog(this);
    }//GEN-LAST:event_btnChangePasswordActionPerformed

    private void btnUserManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserManagerActionPerformed
        // TODO add your handling code here:
        this.showUserManagerJDialog(this);
    }//GEN-LAST:event_btnUserManagerActionPerformed

    private void btnFlappyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFlappyActionPerformed
        // TODO add your handling code here:
        new FlappyBird(userId.trim(), this);
    }//GEN-LAST:event_btnFlappyActionPerformed

    private void btnShotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShotActionPerformed
        // TODO add your handling code here:
        BanGa.showGame2(userId, "BanGa");
    }//GEN-LAST:event_btnShotActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);

        LoginJDialog login = new LoginJDialog(this, true);
        login.setLocationRelativeTo(null);
        login.setVisible(true);

        if (login.loginSuccessful) {
            new BoardGameJFrame(login.UserId(), login.role()).setVisible(true);
            this.dispose();
        } else {
            System.exit(0);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnnewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnewActionPerformed
        // TODO add your handling code here:
        loadScores(getCurrentUserId());
    }//GEN-LAST:event_btnnewActionPerformed

    private void btnrewardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrewardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnrewardActionPerformed

    private void btntitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntitleActionPerformed
        // TODO add your handling code here:
        TitleRewardJFrame frame = new TitleRewardJFrame(getCurrentUserId());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//GEN-LAST:event_btntitleActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
            LoginJDialog dialog = new LoginJDialog(null, true); // mở form login
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            if (dialog.loginSuccessful) {
                // gọi constructor có tham số đầy đủ
                new BoardGameJFrame(dialog.UserId(), dialog.role()).setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnFlappy;
    private javax.swing.JButton btnShot;
    private javax.swing.JButton btnUserManager;
    private javax.swing.JButton btnnew;
    private javax.swing.JButton btnreward;
    private javax.swing.JButton btntitle;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lbbird;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUserID;
    private javax.swing.JLabel lbmetoer;
    private javax.swing.JLabel lbtitle;
    // End of variables declaration//GEN-END:variables

    @Override
    public void init() {
        this.setIconImage(XIcon.getIcon("backgroundflappy.png").getImage());
        //để hiện thị icon trên góc của view khi chạy
        this.setLocationRelativeTo(null);
        this.showLoginJDialog(this);
        this.showWelcomeJDialog(this);
    }
}
