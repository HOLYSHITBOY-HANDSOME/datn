/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package board.game.dao;

import board.game.entity.Diem;
import board.game.util.XJdbc;
import java.util.List;
import java.sql.*;

/**
 *
 * @author LAPTOP LE SON
 */
public interface DiemDAO extends CrudDAO<Diem, Integer> {

    // Lấy danh sách điểm theo người chơi
    List<Diem> findByUserId(String userId);

    // Lấy danh sách điểm theo game
    List<Diem> findByGameId(String gameId);

//    // Lấy top điểm cao nhất của 1 game
//    List<Diem> findTopByGameId(String gameId, int limit);
    public class DiemService {

        public int getScore(String userId, String gameId) {
            String sql = "SELECT diemso FROM Diem WHERE idnguoidung = ? AND idgame = ?";
            Integer score = XJdbc.getValue(sql, userId, gameId);
            return score != null ? score : 0;
        }
    }
}
