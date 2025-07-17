/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *
 * @author DELL
 */
public class Pacman extends JPanel implements ActionListener, KeyListener {

    private final int TILE_SIZE = 24;
    private final int ROWS = 21;
    private final int COLS = 21;
    private final int[][] map = new int[ROWS][COLS];
    private int pacX = 10, pacY = 11;
    private int dx = 0, dy = 0;
    private int ghostX = 10, ghostY = 9; // ghost bắt đầu ở xa hơn
    private int gdx = 0, gdy = 1;
    private final int DELAY = 200;
    private Timer timer;
    private final Random rand = new Random();
    private boolean gameOver = false;
    private long startTime;

    public Pacman() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initMap();
        timer = new Timer(DELAY, this);
        startTime = System.currentTimeMillis();
        timer.start();
    }

    private void initMap() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == 0 || r == ROWS - 1 || c == 0 || c == COLS - 1) {
                    map[r][c] = 1;
                } else {
                    map[r][c] = 2;
                }
            }
        }
        for (int r = 5; r < 16; r++) {
            map[r][5] = 1;
            map[r][15] = 1;
        }
        for (int r = 8; r <= 12; r++) {
            for (int c = 8; c <= 12; c++) {
                map[r][c] = 0;
            }
        }
        for (int i = 8; i <= 12; i++) {
            map[8][i] = 1;
            map[12][i] = 1;
            map[i][8] = 1;
            map[i][12] = 1;
        }
        map[12][10] = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        int newX = pacX + dx;
        int newY = pacY + dy;
        if (map[newY][newX] != 1) {
            pacX = newX;
            pacY = newY;
            if (map[pacY][pacX] == 2) {
                map[pacY][pacX] = 0;
                score += 1;
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed > 5000) {
            moveGhost();
        }

        if (elapsed > 1000 && pacX == ghostX && pacY == ghostY) {
            gameOver = true;
            timer.stop();
            int choice = JOptionPane.showConfirmDialog(this, "Bạn thua! Chơi lại?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                SwingUtilities.getWindowAncestor(this).dispose(); // Đóng cửa sổ game
            }
        }

        if (checkWin()) {
            gameOver = true;
            timer.stop();
            int choice = JOptionPane.showConfirmDialog(this, "Bạn thắng! Chơi lại?", "Victory", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }

        repaint();
    }

    private void moveGhost() {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int bestDist = Integer.MAX_VALUE;
        int[] bestDir = {0, 0};

        for (int[] dir : directions) {
            int nx = ghostX + dir[0];
            int ny = ghostY + dir[1];
            if (map[ny][nx] != 1) {
                int dist = Math.abs(nx - pacX) + Math.abs(ny - pacY);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestDir = dir;
                }
            }
        }

        ghostX += bestDir[0];
        ghostY += bestDir[1];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;
                if (map[r][c] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                } else if (map[r][c] == 2) {
                    g.setColor(Color.WHITE);
                    g.fillOval(x + TILE_SIZE / 2 - 3, y + TILE_SIZE / 2 - 3, 6, 6);
                }
            }
        }
        g.setColor(Color.YELLOW);
        g.fillOval(pacX * TILE_SIZE, pacY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.PINK);
        g.fillOval(ghostX * TILE_SIZE, ghostY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score, 10, 20);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> {
                dx = -1;
                dy = 0;
            }
            case KeyEvent.VK_RIGHT -> {
                dx = 1;
                dy = 0;
            }
            case KeyEvent.VK_UP -> {
                dx = 0;
                dy = -1;
            }
            case KeyEvent.VK_DOWN -> {
                dx = 0;
                dy = 1;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac-Man Java Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Pacman());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void resetGame() {
        pacX = 10;
        pacY = 11;
        dx = 0;
        dy = 0;
        ghostX = 10;
        ghostY = 9;
        gdx = 0;
        gdy = 1;
        gameOver = false;
        startTime = System.currentTimeMillis();
        initMap();
        timer.start();
    }

    private boolean checkWin() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (map[r][c] == 2) {
                    return false;
                }
            }
        }
        return true;
    }

    private int score = 0;

    public static void showGame1() {
        JFrame frame = new JFrame("Pacman");
        Pacman gamePanel = new Pacman();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng cửa sổ nhưng không tắt app
        frame.add(gamePanel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
