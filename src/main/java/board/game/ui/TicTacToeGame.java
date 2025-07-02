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
public class TicTacToeGame extends JPanel implements KeyListener, MouseListener {
    private final int TILE_SIZE = 100;
    private final int ROWS = 3;
    private final int COLS = 3;
    private final int[][] board = new int[ROWS][COLS]; // 0 = trống, 1 = X, 2 = O
    private boolean xTurn = true;
    private boolean gameOver = false;

    public TicTacToeGame() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setBackground(Color.WHITE);
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ lưới
        g.setColor(Color.BLACK);
        for (int i = 1; i < ROWS; i++) {
            g.drawLine(0, i * TILE_SIZE, COLS * TILE_SIZE, i * TILE_SIZE);
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, ROWS * TILE_SIZE);
        }

        // Vẽ X hoặc O
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;
                if (board[r][c] == 1) {
                    g.setColor(Color.RED);
                    g.drawLine(x + 10, y + 10, x + TILE_SIZE - 10, y + TILE_SIZE - 10);
                    g.drawLine(x + TILE_SIZE - 10, y + 10, x + 10, y + TILE_SIZE - 10);
                } else if (board[r][c] == 2) {
                    g.setColor(Color.BLUE);
                    g.drawOval(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
                }
            }
        }

        // Kiểm tra game over
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.MAGENTA);
            String winner = checkWin() == 1 ? "X thắng!" : (checkWin() == 2 ? "O thắng!" : "Hòa!");
            g.drawString(winner, 70, 330);
        }
    }

    private int checkWin() {
        // Kiểm tra hàng, cột, chéo
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 &&
                board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0];
            if (board[0][i] != 0 &&
                board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i];
        }
        if (board[0][0] != 0 &&
            board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0];
        if (board[0][2] != 0 &&
            board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2];
        return 0;
    }

    private boolean isDraw() {
        for (int[] row : board)
            for (int cell : row)
                if (cell == 0) return false;
        return true;
    }

    private void resetGame() {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                board[r][c] = 0;
        xTurn = true;
        gameOver = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    if (gameOver) {
        resetGame();
        return;
    }

    int c = e.getX() / TILE_SIZE;
    int r = e.getY() / TILE_SIZE;

    if (c < 0 || c >= COLS || r < 0 || r >= ROWS) return;

    if (board[r][c] == 0) {
        board[r][c] = xTurn ? 1 : 2;
        xTurn = !xTurn;

        int winner = checkWin();
        if (winner != 0) {
            gameOver = true;
            repaint();
            String message = "Người chơi " + (winner == 1 ? "X" : "O") + " thắng!";
            JOptionPane.showMessageDialog(this, message, "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        } else if (isDraw()) {
            gameOver = true;
            repaint();
            JOptionPane.showMessageDialog(this, "Hòa!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        }

        repaint();
    }
}

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe - Java Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new TicTacToeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
