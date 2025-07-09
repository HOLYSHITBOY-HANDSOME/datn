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
public class Game {

    private String idGame;     // PK
    private String tenGame;
    private String moTa;
    private String trangThai;
}
