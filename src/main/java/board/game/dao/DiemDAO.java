package board.game.dao;

import board.game.entity.Diem;
import board.game.util.XJdbc;
import java.util.List;

public interface DiemDAO extends CrudDAO<Diem, Integer> {

    List<Diem> findByUserId(String userId);

    List<Diem> findByGameId(String gameId);

    // Lớp nội bộ xử lý điểm
    public class DiemService {

    private boolean isUserExists(String userId) {
        String sql = "SELECT COUNT(*) FROM Users WHERE idnguoidung = ?";
        Integer count = (Integer) XJdbc.getValue(sql, userId);
        return count != null && count > 0;
    }

    // Trả về điểm hiện tại hoặc null nếu chưa có
    private Integer getScoreRaw(String userId, String gameId) {
        String sql = "SELECT diemso FROM Diem WHERE idnguoidung = ? AND idgame = ?";
        return (Integer) XJdbc.getValue(sql, userId, gameId); // null nếu chưa có
    }

    // Trả về điểm (0 nếu không có)
    public int getScore(String userId, String gameId) {
        Integer score = getScoreRaw(userId, gameId);
        return score != null ? score : 0;
    }

    // Cập nhật điểm nếu cao hơn, hoặc thêm mới nếu chưa có
    public void capNhatDiemCaoNhat(String userId, String gameId, int diemMoi) {
        userId = userId.trim();

        if (!isUserExists(userId)) {
            System.out.println("UserId không tồn tại: " + userId);
            return;
        }

        Integer diemCu = getScoreRaw(userId, gameId);

        try {
            if (diemCu == null) {
                String insertSql = "INSERT INTO Diem (idnguoidung, idgame, diemso) VALUES (?, ?, ?)";
                XJdbc.executeUpdate(insertSql, userId, gameId, diemMoi);
                //System.out.println("Thêm điểm mới lần đầu: " + diemMoi + " cho user " + userId);
            } else if (diemMoi > diemCu) {
                String updateSql = "UPDATE Diem SET diemso = ? WHERE idnguoidung = ? AND idgame = ?";
                XJdbc.executeUpdate(updateSql, diemMoi, userId, gameId);
                //System.out.println("Cập nhật điểm cao mới: " + diemMoi + " cho user " + userId);
            } else {
                //System.out.println("Điểm chưa vượt, giữ nguyên: " + diemCu + " cho user " + userId);
            }
        } catch (Exception e) {
            System.err.println(" Lỗi khi lưu điểm: " + e.getMessage());
        }
    }
    }

}
