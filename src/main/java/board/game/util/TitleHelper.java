package board.game.util;

import java.sql.ResultSet;

public class TitleHelper {

    public class DiemHelper {

        public static void loadDiemData() {
            String sql = "SELECT iddiem, idnguoidung, idgame, diemso FROM Diem";

            try {
                ResultSet rs = XJdbc.executeQuery(sql);
                while (rs.next()) {
                    int idDiem = rs.getInt("iddiem"); // ✅ đúng rồi nè
                    String idNguoiDung = rs.getString("idnguoidung");
                    String idGame = rs.getString("idgame");
                    int diemSo = rs.getInt("diemso");

                    System.out.println("ID Điểm: " + idDiem + " | Người dùng: " + idNguoiDung + " | Game: " + idGame + " | Điểm: " + diemSo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
