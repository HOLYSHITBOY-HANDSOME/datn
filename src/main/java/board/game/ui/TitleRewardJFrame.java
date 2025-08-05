/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package board.game.ui;

import board.game.util.XJdbc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author XPS 9380
 */
public class TitleRewardJFrame extends javax.swing.JFrame {

    Map<String, Integer> titleConditions = new HashMap<>();
    private String userId;

    /**
     * Creates new form TitleRewardJFrame
     */
    public TitleRewardJFrame(String userId) {
        this.userId = userId;
        initComponents();
        loadRewardData();
        initTitleConditions();
        updateRewardDisplay();
        btnfb1.setActionCommand("Wobby Wings");
        btnfb2.setActionCommand("Feather Master");
        btnfb3.setActionCommand("Pipe Doger");
        btnfb4.setActionCommand("Sky Ruler");
        btnmt1.setActionCommand("Rookie Pilot");
        btnmt2.setActionCommand("First Feather Down");
        btnmt3.setActionCommand("Asteroid Slayer");
        btnmt4.setActionCommand("Galactic Legend");
        jButton1.setActionCommand("NewBie");

        ActionListener titleListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTitle = e.getActionCommand();
                BoardGameJFrame.lbtitleGlobal.setText(selectedTitle);

                java.util.Random rand = new java.util.Random();
                int r = rand.nextInt(256);
                int g = rand.nextInt(256);
                int b = rand.nextInt(256);
                BoardGameJFrame.lbtitleGlobal.setForeground(new java.awt.Color(r, g, b));

                String sql = "UPDATE Users SET title = ? WHERE idNguoiDung = ?";
                XJdbc.executeUpdate(sql, selectedTitle, userId);
            }
        };

        btnfb1.addActionListener(titleListener);
        btnfb2.addActionListener(titleListener);
        btnfb3.addActionListener(titleListener);
        btnfb4.addActionListener(titleListener);
        btnmt1.addActionListener(titleListener);
        btnmt2.addActionListener(titleListener);
        btnmt3.addActionListener(titleListener);
        btnmt4.addActionListener(titleListener);
        jButton1.addActionListener(titleListener);

    }

    private void updateRewardDisplay() {
        Map<String, Integer> scoreMap = new HashMap<>();
        String sql = "SELECT Diem.diemso, Game.idgame FROM Diem JOIN Game ON Diem.idgame = Game.idgame WHERE Diem.idnguoidung = ?";

        try {
            ResultSet rs = XJdbc.executeQuery(sql, userId);
            while (rs.next()) {
                String gameId = rs.getString("idgame").trim();
                int score = rs.getInt("diemso");
                scoreMap.put(gameId, score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateButtonStates("game001", scoreMap.getOrDefault("game001", 0)); // Flappy Bird
        updateButtonStates("game002", scoreMap.getOrDefault("game002", 0)); // Meteor
    }

    private void initTitleConditions() {
        titleConditions.put("btnfb1", 5);
        titleConditions.put("btnfb2", 10);
        titleConditions.put("btnfb3", 20);
        titleConditions.put("btnfb4", 30);
        titleConditions.put("btnmt1", 5);
        titleConditions.put("btnmt2", 10);
        titleConditions.put("btnmt3", 20);
        titleConditions.put("btnmt4", 30);
    }

    private void updateButtonStates(String currentGame, int currentScore) {
        for (Map.Entry<String, Integer> entry : titleConditions.entrySet()) {
            String btnName = entry.getKey();
            int requiredScore = entry.getValue();

            JButton btn = getButtonByName(btnName); // bạn tạo hàm này để lấy JButton theo tên
            String titleText;

            if (currentScore >= requiredScore && isMatchingGame(btnName, currentGame)) {
                titleText = "Đã nhận";
                btn.setEnabled(true);
            } else {
                titleText = "Đạt " + requiredScore + " điểm để nhận";
                btn.setEnabled(true);
            }

            btn.setText(titleText);
            btn.addActionListener(e -> handleButtonClick(currentScore, requiredScore, currentGame, btnName));
        }
        jButton1.setText("Đã nhận");
        jButton1.setEnabled(true);
        jButton1.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chào mừng bạn đến với thế giới trò chơi!");
        });

    }

    private void handleButtonClick(int score, int required, String game, String btnName) {
        if (score >= required && isMatchingGame(btnName, game)) {
            JOptionPane.showMessageDialog(this, "Bạn đã nhận được danh hiệu này rồi!");
        } else {
            JOptionPane.showMessageDialog(this, "Bạn chưa đủ điểm để nhận danh hiệu này!");
        }
    }

    private JButton getButtonByName(String name) {
        switch (name) {
            case "btnfb1":
                return btnfb1;
            case "btnfb2":
                return btnfb2;
            case "btnfb3":
                return btnfb3;
            case "btnfb4":
                return btnfb4;
            case "btnmt1":
                return btnmt1;
            case "btnmt2":
                return btnmt2;
            case "btnmt3":
                return btnmt3;
            case "btnmt4":
                return btnmt4;
            default:
                return null;
        }
    }

    private boolean isMatchingGame(String btnName, String game) {
        return (btnName.startsWith("btnfb") && game.equals("game001"))
                || (btnName.startsWith("btnmt") && game.equals("game002"));
    }

    private void loadRewardData() {
        String sql = "SELECT Diem.diemso, Game.tengame FROM Diem JOIN Game ON Diem.idgame = Game.idgame WHERE Diem.idnguoidung = ?";

        try {
            ResultSet rs = XJdbc.executeQuery(sql, userId); // chỉ khai báo rs ở đây
            while (rs.next()) {
                String gameName = rs.getString("tengame");
                int score = rs.getInt("diemso");
                System.out.println(gameName + ": " + score);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xem lỗi thật sự là gì
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnmt4 = new javax.swing.JButton();
        btnmt3 = new javax.swing.JButton();
        btnmt2 = new javax.swing.JButton();
        btnmt1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnfb1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnfb2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnfb3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnfb4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel2.setText("Danh Hiệu");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        jScrollPane2.setBackground(new java.awt.Color(235, 232, 216));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setBackground(new java.awt.Color(235, 232, 216));

        jLabel7.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel7.setText("Danh hiệu: Rookie Pilot");

        jLabel8.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel8.setText("Danh hiệu: First Feather Down");

        jLabel9.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel9.setText("Danh hiệu: Asteroid Slayer");

        jLabel10.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel10.setText("Danh hiệu: Galactic Legend");

        btnmt4.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnmt4.setForeground(new java.awt.Color(153, 0, 153));
        btnmt4.setText("30 điểm Meteor");

        btnmt3.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnmt3.setForeground(new java.awt.Color(204, 153, 0));
        btnmt3.setText("20 điểm Meteor");

        btnmt2.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnmt2.setForeground(new java.awt.Color(255, 153, 51));
        btnmt2.setText("10 điểm Meteor");

        btnmt1.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnmt1.setForeground(new java.awt.Color(255, 0, 0));
        btnmt1.setText("5 điểm Meteor");

        jButton1.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 102, 0));
        jButton1.setText("Người mới");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel11.setText("Danh hiệu: NewBie");

        jLabel6.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel6.setText("Danh hiệu: Wobbly Wings");

        btnfb1.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnfb1.setForeground(new java.awt.Color(51, 51, 255));
        btnfb1.setText("5 điểm FlappyBird");
        btnfb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfb1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel3.setText("Danh hiệu: Feather Master");

        btnfb2.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnfb2.setForeground(new java.awt.Color(102, 255, 204));
        btnfb2.setText("10 điểm FlappyBird");

        jLabel4.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel4.setText("Danh hiệu: Pipe Dodger");

        btnfb3.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnfb3.setForeground(new java.awt.Color(102, 102, 255));
        btnfb3.setText("20 điểm FlappyBird");

        jLabel5.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel5.setText("Danh hiệu: Sky Ruler");

        btnfb4.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btnfb4.setForeground(new java.awt.Color(0, 204, 204));
        btnfb4.setText("30 điểm FlappyBird");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel7))
                .addGap(55, 55, 55)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnmt4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnmt2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnmt3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnmt1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(604, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnfb1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnfb2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnfb3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnfb4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnfb1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnfb2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnfb3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnfb4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnmt1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnmt2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnmt3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnmt4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(77, 77, 77))
        );

        jScrollPane2.setViewportView(jPanel2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 610, 610));

        jButton2.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 102, 102));
        jButton2.setText("Thoát");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 0, 100, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/board/game/icons/BackgroundTitle.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 790));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnfb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfb1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnfb1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
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
            java.util.logging.Logger.getLogger(TitleRewardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TitleRewardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TitleRewardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TitleRewardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new TitleRewardJFrame().setVisible(true);
                new TitleRewardJFrame("user002").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnfb1;
    private javax.swing.JButton btnfb2;
    private javax.swing.JButton btnfb3;
    private javax.swing.JButton btnfb4;
    private javax.swing.JButton btnmt1;
    private javax.swing.JButton btnmt2;
    private javax.swing.JButton btnmt3;
    private javax.swing.JButton btnmt4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
