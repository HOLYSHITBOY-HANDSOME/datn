/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package board.game.dao;

import board.game.entity.Diem;
import java.util.List;

/**
 *
 * @author LAPTOP LE SON
 */
public interface DiemDAO extends CrudDAO<Diem, Integer>{
    // Lấy danh sách điểm theo người chơi
    List<Diem> findByUserId(String userId);

    // Lấy danh sách điểm theo game
    List<Diem> findByGameId(String gameId);

//    // Lấy top điểm cao nhất của 1 game
//    List<Diem> findTopByGameId(String gameId, int limit);
}
