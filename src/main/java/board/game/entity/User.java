/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.entity;

import lombok.*;

/**
 *
 * @author LAPTOP LE SON
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String idNguoiDung;    // PK
    private String matKhau;
    private boolean trangThai;
    private boolean vaiTro;         // admin / user

}
