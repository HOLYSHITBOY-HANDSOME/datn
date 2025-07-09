/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.util;

import board.game.entity.User;

/**
 *
 * @author LAPTOP LE SON
 */
public class XAuth {
    // Người dùng hiện tại đã đăng nhập
    public static User user = null;

    // Đăng xuất
    public static void logout() {
        user = null;
    }

    // Kiểm tra đã đăng nhập chưa
    public static boolean isLogin() {
        return user != null;
    }

    // Kiểm tra có phải Admin không
    public static boolean isAdmin() {
        return isLogin() && user.isVaiTro();
    }

    // Kiểm tra trạng thái tài khoản
    public static boolean isActive() {
        return isLogin() && user.isTrangThai();
    }
}
