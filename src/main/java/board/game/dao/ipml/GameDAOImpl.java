/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.dao.ipml;

import board.game.dao.GameDAO;
import board.game.entity.Game;
import board.game.util.XJdbc;
import board.game.util.XQuery;
import java.util.List;

/**
 *
 * @author LAPTOP LE SON
 */
public class GameDAOImpl implements GameDAO{
    String createSql = "INSERT INTO Game VALUES (?, ?, ?, ?)";
    String updateSql = "UPDATE Game SET tengame=?, mota=?, trangthai=? WHERE idgame=?";
    String deleteSql = "DELETE FROM Game WHERE idgame=?";
    String findAllSql = "SELECT * FROM Game";
    String findByIdSql = "SELECT * FROM Game WHERE idgame=?";

    @Override
    public Game create(Game entity) {
        Object[] args = {
            entity.getIdGame(),
            entity.getTenGame(),
            entity.getMoTa(),
            entity.getTrangThai()
        };
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public void update(Game entity) {
        Object[] args = {
            entity.getTenGame(),
            entity.getMoTa(),
            entity.getTrangThai(),
            entity.getIdGame()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Game> findAll() {
        return XQuery.getBeanList(Game.class, findAllSql);
    }

    @Override
    public Game findById(String id) {
       return XQuery.getSingleBean(Game.class, findByIdSql, id);
    }
}
