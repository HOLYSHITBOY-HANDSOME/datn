/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.util;

/**
 *
 * @author LAPTOP LE SON
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("thu sql");
        them();
    }
    
    public static void them(){
        System.out.println("thu sql");
        String sql = "INSERT INTO Diem(idnguoidung, idgame, diemso, ngaychoi) VALUES (?, ?, ?, ?)";
        XJdbc.executeUpdate(sql, "user001", "game004", 15, "2025-06-14");
        XJdbc.executeUpdate(sql, "user002", "game002", 27, "2025-06-14");
        XJdbc.executeUpdate(sql, "user009", "game002", 100, "2025-06-14");
        
    }
}
