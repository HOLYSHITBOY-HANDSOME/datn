/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package board.game.dao;

import board.game.entity.User;
import java.util.List;

/**
 *
 * @author LAPTOP LE SON
 */
public interface UserDAO extends CrudDAO<User, String>{
//    // Dùng để đăng nhập
//    User findByUsernameAndPassword(String username, String password);
//
//    // Lọc người dùng theo trạng thái (đang hoạt động, bị khóa,...)
//    List<User> findByTrangThai(String trangThai);
//
//    // Lọc người dùng theo vai trò (admin/user)
//    List<User> findByVaiTro(String vaiTro);
    
    void updatePassword(String username, String newPassword);
    
}
