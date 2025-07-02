/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *
 * @author DELL
 */
public class CaroGame extends JFrame implements MouseListener {
    private final int SIZE = 30; // Kích thước 1 ô caro
    private final int ROWS = 25; // Số dòng
    private final int COLS = 40; // Số cột
    private final int WIDTH = COLS * SIZE;
    private final int HEIGHT = ROWS * SIZE;

    private int[][] board = new int[ROWS][COLS]; // 0: rỗng, 1: X, 2: O
    private boolean xTurn = true; // Luợt người chơi

    public CaroGame() {
        setTitle("Caro Game - 5 in a Row");
        setSize(1200, 800); // ← thêm dòng này
        setLocationRelativeTo(null); // ← đặt giữa màn hình
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(this);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Image offScreen = createImage(getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) offScreen.getGraphics();

        // Vẽ bàn cờ
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.BLACK);
        for (int i = 0; i <= ROWS; i++)
            g2.drawLine(0, i * SIZE, COLS * SIZE, i * SIZE);
        for (int j = 0; j <= COLS; j++)
            g2.drawLine(j * SIZE, 0, j * SIZE, ROWS * SIZE);

        // Vẽ các quân cờ
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++) {
                int val = board[i][j];
                if (val == 1) { // X
                    g2.setColor(Color.BLUE);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(j * SIZE + 5, i * SIZE + 5, (j + 1) * SIZE - 5, (i + 1) * SIZE - 5);
                    g2.drawLine((j + 1) * SIZE - 5, i * SIZE + 5, j * SIZE + 5, (i + 1) * SIZE - 5);
                } else if (val == 2) { // O
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(j * SIZE + 5, i * SIZE + 5, SIZE - 10, SIZE - 10);
                }
            }

        g.drawImage(offScreen, 0, 0, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / SIZE;
        int y = e.getY() / SIZE;

        if (x >= COLS || y >= ROWS || board[y][x] != 0)
            return;

        board[y][x] = xTurn ? 1 : 2;
        repaint();

        if (checkWin(y, x)) {
            String winner = xTurn ? "Người chơi X" : "Người chơi O";
            int option = JOptionPane.showConfirmDialog(this, winner + " thắng!\nBạn có muốn chơi lại?", "Kết thúc", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                board = new int[ROWS][COLS];
                xTurn = true;
                repaint();
            } else {
                System.exit(0);
            }
        }

        xTurn = !xTurn;
    }

    private boolean checkWin(int row, int col) {
        int player = board[row][col];
        return count(row, col, 0, 1, player) + count(row, col, 0, -1, player) - 1 >= 5 || // Ngang
               count(row, col, 1, 0, player) + count(row, col, -1, 0, player) - 1 >= 5 || // Dọc
               count(row, col, 1, 1, player) + count(row, col, -1, -1, player) - 1 >= 5 || // Chéo \
               count(row, col, 1, -1, player) + count(row, col, -1, 1, player) - 1 >= 5;   // Chéo /
    }

    private int count(int row, int col, int dx, int dy, int player) {
        int cnt = 0;
        while (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == player) {
            cnt++;
            row += dx;
            col += dy;
        }
        return cnt;
    }

    // Không dùng các sự kiện này
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        new CaroGame();
    }
}
