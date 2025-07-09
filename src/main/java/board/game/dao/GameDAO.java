/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package board.game.dao;

import board.game.entity.Game;
import java.util.List;

/**
 *
 * @author LAPTOP LE SON
 */
public interface GameDAO extends CrudDAO<Game, String>{
    
//    // Lấy danh sách game theo trạng thái (hoạt động / ẩn)
//    List<Game> findByTrangThai(String trangThai);
}
