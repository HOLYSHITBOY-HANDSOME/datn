/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package board.game.ui;

import board.game.ui.manager.HistoryManagerJDialog;
import board.game.ui.manager.RankManagerJDialog;
import board.game.ui.manager.ThongkeManagerJDialog;
import board.game.ui.manager.UserManagerJDialog;
import board.game.util.XDialog;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author LAPTOP LE SON
 */
public interface BoardGameController {

    /**
     * Hiển thị cửa sổ chào Hiển thị cửa sổ đăng nhập Hiển thị thông tin user
     * đăng nhập Disable/Enable các thành phần tùy thuộc vào vai trò đăng nhập
     */
    void init();

    default void exit() {
        if (XDialog.confirm("Bạn muốn kết thúc?")) {
            System.exit(0);
        }
    }

    default void showJDialog(JDialog dialog) {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    default void showWelcomeJDialog(JFrame frame) {
        this.showJDialog(new WelcomeJDialog(frame, true));
    }

    default void showLoginJDialog(JFrame frame) {
        this.showJDialog(new LoginJDialog(frame, true));
    }

    default void showSignUpJDialog(JFrame frame) {
        this.showJDialog(new SignUpJDialog(frame, true));
    }

    default void showChangePasswordJDilog(JFrame frame) {
        this.showJDialog(new ChangePasswordJDialog(frame, true));
    }

    default void showHistoryJDialog(JFrame frame) {
        this.showJDialog(new HistoryJDialog(frame, true));
    }

    default void showRankJDialog(JFrame frame) {
        this.showJDialog(new RankJDialog(frame, true));
    }

    default void showHistoryManagerJDialog(JFrame frame) {
        this.showJDialog(new HistoryManagerJDialog(frame, true));
    }

    default void showRankManagerJDialog(JFrame frame) {
        this.showJDialog(new RankManagerJDialog(frame, true));
    }

    default void showThongKeManagerJDialog(JFrame frame) {
        this.showJDialog(new ThongkeManagerJDialog(frame, true));
    }

    default void showUserManagerJDialog(JFrame frame) {
        this.showJDialog(new UserManagerJDialog(frame, true));
    }

    default void showFlappyBirdGame() {
        new FlappyBird(); // Gọi constructor bạn vừa thêm
    }

    default void showRanSanMoiGame() {
        RanSanMoi.showGame();
    }
    
    default void showPacManGame(){
        Pacman.showGame1();
    }
    
    default void showBanGaGame(){
        BanGa.showGame2();
    }
    
    default void showTank(){
        BattleCity.showGame3();
    }
    
    default void showDoMin(){
        DoMin.showGame4();
    }

}
