/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.dao.ipml;

import board.game.dao.DiemDAO;
import board.game.entity.Diem;
import board.game.util.XJdbc;
import board.game.util.XQuery;
import java.util.List;

/**
 *
 * @author LAPTOP LE SON
 */
public class DiemDAOImpl implements DiemDAO{
    String createSql = "INSERT INTO Diem(idnguoidung, idgame, diemso, ngaychoi) VALUES (?, ?, ?, ?)";
    String updateSql = "UPDATE Diem SET idnguoidung=?, idgame=?, diemso=?, ngaychoi=? WHERE iddiem=?";
    String deleteSql = "DELETE FROM Diem WHERE iddiem=?";
    String findAllSql = "SELECT * FROM Diem";
    String findByIdSql = "SELECT * FROM Diem WHERE iddiem=?";
    String findByUserSql = "SELECT * FROM Diem WHERE idnguoidung=?";
    String findByGameSql = "SELECT * FROM Diem WHERE idgame=?";

    @Override
    public List<Diem> findByUserId(String userId) {
       return XQuery.getBeanList(Diem.class, findByUserSql, userId);
    }

    @Override
    public List<Diem> findByGameId(String gameId) {
       return XQuery.getBeanList(Diem.class, findByGameSql, gameId);
    }

    @Override
    public Diem create(Diem entity) {
       Object[] args = {
            entity.getIdNguoiDung(),
            entity.getIdGame(),
            entity.getDiemSo(),
            entity.getNgayChoi()
        };
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public void update(Diem entity) {
        Object[] args = {
            entity.getIdNguoiDung(),
            entity.getIdGame(),
            entity.getDiemSo(),
            entity.getNgayChoi(),
            entity.getIdDiem()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(Integer id) {
      XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Diem> findAll() {
      return XQuery.getBeanList(Diem.class, findAllSql);
    }

    @Override
    public Diem findById(Integer id) {
       return XQuery.getSingleBean(Diem.class, findByIdSql, id);
    }
}
