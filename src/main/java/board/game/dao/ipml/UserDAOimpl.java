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

    String createSql = "INSERT INTO Users VALUES (?, ?, ?, ?)";
    String updateSql = "UPDATE Users SET trangthai=?, vaitro=? WHERE idnguoidung=?";
    String deleteSql = "DELETE FROM Users WHERE idnguoidung=?";
    String findAllSql = "SELECT * FROM Users";
    String findByIdSql = "SELECT * FROM Users WHERE idnguoidung=?";

    @Override
    public User create(User entity) {
        Object[] args = {
            entity.getIdNguoiDung(),
            entity.getMatKhau(),
            entity.isTrangThai(),
            entity.getVaiTro()
        };
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public void update(User entity) {
        Object[] args = {
            entity.isTrangThai(),
            entity.getVaiTro(),
            entity.getIdNguoiDung()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(String id) {
        // Xóa điểm trước (nếu có), tránh lỗi ràng buộc khóa ngoại
        XJdbc.executeUpdate("DELETE FROM Diem WHERE idnguoidung = ?", id);

        // Sau đó xóa người dùng
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

    @Override
    public boolean isUsernameExists(String IdNguoiDung) {
        try {
            String sql = "SELECT COUNT(*) FROM Users WHERE idnguoidung = ?";
            ResultSet rs = XJdbc.executeQuery(sql, IdNguoiDung);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
