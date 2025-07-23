/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.dao.ipml;

import board.game.dao.UserDAO;
import board.game.entity.User;
import board.game.util.XJdbc;
import board.game.util.XQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.naming.spi.DirStateFactory;

/**
 *
 * @author LAPTOP LE SON
 */
public class UserDAOimpl implements UserDAO {

    String createSql = "INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE Users SET tennguoidung=?, trangthai=?, vaitro=?, email=?, sdt=? WHERE idnguoidung=?";
    String deleteSql = "DELETE FROM Users WHERE idnguoidung=?";
    String findAllSql = "SELECT * FROM Users";
    String findByIdSql = "SELECT * FROM Users WHERE idnguoidung=?";
    String findByUsernameSql = "SELECT * FROM Users WHERE tennguoidung=?";

    @Override
    public User create(User entity) {
        Object[] args = {
            entity.getIdNguoiDung(),
            entity.getTenNguoiDung(),
            entity.getMatKhau(),
            entity.isTrangThai(),
            entity.isVaiTro(),
            entity.getEmail(),
            entity.getSdt()
        };
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public void update(User entity) {
        Object[] args = {
            entity.getTenNguoiDung(),
            entity.isTrangThai(),
            entity.isVaiTro(),
            entity.getEmail(),
            entity.getSdt(),
            entity.getIdNguoiDung()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<User> findAll() {
        return XQuery.getBeanList(User.class, findAllSql);
    }

    @Override
    public User findById(String id) {
        return XQuery.getSingleBean(User.class, findByIdSql, id);
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        String sql = "UPDATE Users SET matkhau=? WHERE idnguoidung=?";
        XJdbc.executeUpdate(sql, newPassword, username);
    }

    public String generateNewUserId() {
        try {
            String sql = "SELECT MAX(idnguoidung) FROM USERS";
            ResultSet rs = XJdbc.executeQuery(sql);
            if (rs.next()) {
                String last = rs.getString(1);
                if (last != null) {
                    String numberPart = last.substring(4);
                    int number = Integer.parseInt(numberPart) + 1;
                    return String.format("user%03d", number);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "user001";
    }

}
