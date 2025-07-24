/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.util;

import board.game.entity.User;


public class Auth {
    public static User user = null; // người dùng đăng nhập

    public static void clear() {
        user = null;
    }

    public static boolean isLogin() {
        return user != null;
    }

    public static boolean isManager() {
        return user != null && user.isVaiTro(); // hoặc getVaiTro() nếu dùng kiểu getter
    }
}
