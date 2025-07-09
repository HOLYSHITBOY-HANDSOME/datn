/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.entity;

import lombok.*;
import java.util.Date;

/**
 *
 * @author LAPTOP LE SON
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diem {

    private int idDiem;            // PK, auto-increment
    private String idNguoiDung;    // FK → Users
    private String idGame;         // FK → Game
    private int diemSo;
    private Date ngayChoi;
}
