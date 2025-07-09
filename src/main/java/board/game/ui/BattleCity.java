/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

/**
 *
 * @author DELL
 */
public class BattleCity extends JPanel implements KeyListener, ActionListener {

    Timer gameLoop = new Timer(50, this); // 20 FPS
    Timer spawnTimer;

    final int TILE_SIZE = 32;
    final int ROWS = 13;
    final int COLS = 13;

    int[][] map = new int[ROWS][COLS];

    int playerX = 6 * TILE_SIZE;
    int playerY = 12 * TILE_SIZE;
    int playerDir = 0;
    boolean gameOver = false;
    boolean gameWin = false;

    JButton retryButton;
    JButton spawnEnemyButton;
    JFrame frame;

    java.util.List<Bullet> bullets = new ArrayList<>();
    java.util.List<Bullet> enemyBullets = new ArrayList<>();

    class Enemy {

        int x, y, dir;
        int stepCounter = 0;
        int fireCooldown = 0;

        Enemy(int x, int y) {
            this.x = x;
            this.y = y;
            this.dir = new Random().nextInt(4);
        }

        void move() {
            int dx = 0, dy = 0;
            switch (dir) {
                case 0 ->
                    dy = -TILE_SIZE / 4;
                case 1 ->
                    dx = TILE_SIZE / 4;
                case 2 ->
                    dy = TILE_SIZE / 4;
                case 3 ->
                    dx = -TILE_SIZE / 4;
            }
            int nextX = x + dx;
            int nextY = y + dy;
            if (!collides(nextX, nextY)) {
                x = nextX;
                y = nextY;
            } else {
                dir = new Random().nextInt(4);
            }

            stepCounter++;
            if (stepCounter > 20) {
                dir = new Random().nextInt(4);
                stepCounter = 0;
            }

            if (fireCooldown-- <= 0) {
                fireCooldown = 60 + new Random().nextInt(40);
                fire();
            }
        }

        void fire() {
            int bx = x + TILE_SIZE / 2;
            int by = y + TILE_SIZE / 2;
            enemyBullets.add(new Bullet(bx, by, dir, false));
        }

        void draw(Graphics g) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        }
    }

    java.util.List<Enemy> enemies = new ArrayList<>();

    public BattleCity() {
        retryButton = new JButton("Retry");
        retryButton.setBounds(110, 250, 100, 40);
        retryButton.addActionListener(e -> resetGame());
        retryButton.setVisible(false);

        spawnEnemyButton = new JButton("+ Enemy");
        spawnEnemyButton.setBounds(220, 250, 100, 40);
        spawnEnemyButton.addActionListener(e -> {
            spawnEnemies(1);
            requestFocusInWindow();
        });

        this.setLayout(null);
        this.add(retryButton);
        this.add(spawnEnemyButton);

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);

        initMap();
        gameLoop.start();

        spawnTimer = new Timer(10000, e -> {
            if (!gameOver && !gameWin) {
                spawnEnemies(2);
            }
        });
        spawnTimer.start();

        spawnEnemies(2);
    }

    void resetGame() {
        playerX = 6 * TILE_SIZE;
        playerY = 12 * TILE_SIZE;
        playerDir = 0;
        gameOver = false;
        gameWin = false;
        bullets.clear();
        enemyBullets.clear();
        enemies.clear();
        initMap();
        spawnEnemies(2);
        retryButton.setVisible(false);
        this.setFocusable(true);
        this.requestFocusInWindow();
        spawnTimer.restart();
        repaint();
    }

    void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            int x = (i % 2 == 0) ? 0 : 12 * TILE_SIZE;
            enemies.add(new Enemy(x, 0));
        }
    }

    void initMap() {
        for (int i = 0; i < ROWS; i++) {
            map[i][0] = map[i][COLS - 1] = 1;
        }
        for (int j = 0; j < COLS; j++) {
            map[0][j] = map[ROWS - 1][j] = 1;
        }
        map[6][6] = 2;
        map[12][6] = 3;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int tile = map[i][j];
                switch (tile) {
                    case 1 ->
                        g.setColor(new Color(188, 66, 66));
                    case 2 ->
                        g.setColor(Color.GRAY);
                    case 3 ->
                        g.setColor(Color.BLACK);
                    default ->
                        g.setColor(Color.BLACK);
                }
                if (tile != 0) {
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        if (!gameOver && !gameWin) {
            g.setColor(Color.YELLOW);
            g.fillRect(playerX, playerY, TILE_SIZE, TILE_SIZE);

            g.setColor(Color.ORANGE);
            int midX = playerX + TILE_SIZE / 2;
            int midY = playerY + TILE_SIZE / 2;
            switch (playerDir) {
                case 0 ->
                    g.fillRect(midX - 2, playerY - 6, 4, 6);
                case 1 ->
                    g.fillRect(playerX + TILE_SIZE, midY - 2, 6, 4);
                case 2 ->
                    g.fillRect(midX - 2, playerY + TILE_SIZE, 4, 6);
                case 3 ->
                    g.fillRect(playerX - 6, midY - 2, 6, 4);
            }
        }

        g.setColor(Color.WHITE);
        for (Bullet b : bullets) {
            b.draw(g);
        }
        g.setColor(Color.RED);
        for (Bullet b : enemyBullets) {
            b.draw(g);
        }
        for (Enemy e : enemies) {
            e.draw(g);
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("GAME OVER", 120, 200);
            retryButton.setVisible(true);
        }

        if (gameWin) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("YOU WIN!", 130, 200);
            retryButton.setVisible(true);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (gameOver || gameWin) {
            return;
        }

        int code = e.getKeyCode();
        int dx = 0, dy = 0;

        switch (code) {
            case KeyEvent.VK_UP -> {
                playerDir = 0;
                dy = -TILE_SIZE;
            }
            case KeyEvent.VK_RIGHT -> {
                playerDir = 1;
                dx = TILE_SIZE;
            }
            case KeyEvent.VK_DOWN -> {
                playerDir = 2;
                dy = TILE_SIZE;
            }
            case KeyEvent.VK_LEFT -> {
                playerDir = 3;
                dx = -TILE_SIZE;
            }
            case KeyEvent.VK_SPACE ->
                fire();
        }

        int nextX = playerX + dx;
        int nextY = playerY + dy;

        if (!collides(nextX, nextY)) {
            playerX = nextX;
            playerY = nextY;
        }

        repaint();
    }

    void fire() {
        int bx = playerX + TILE_SIZE / 2;
        int by = playerY + TILE_SIZE / 2;
        bullets.add(new Bullet(bx, by, playerDir, true));
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver || gameWin) {
            return;
        }

        for (Bullet b : bullets) {
            b.move();
        }
        bullets.removeIf(b -> b.outOfBounds() || b.hitWall() || b.hitEnemy());

        for (Bullet b : enemyBullets) {
            b.move();
        }
        enemyBullets.removeIf(b -> b.outOfBounds() || b.hitWall() || b.hitPlayer());

        for (Enemy en : enemies) {
            en.move();
        }

        if (enemies.isEmpty()) {
            gameWin = true;
        }

        repaint();
    }

    boolean collides(int x, int y) {
        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        if (row < 0 || col < 0 || row >= ROWS || col >= COLS) {
            return true;
        }

        return map[row][col] == 1 || map[row][col] == 2;
    }

    class Bullet {

        int x, y, dir;
        int speed = 8;
        boolean isPlayer;

        Bullet(int x, int y, int dir, boolean isPlayer) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.isPlayer = isPlayer;
        }

        void move() {
            switch (dir) {
                case 0 ->
                    y -= speed;
                case 1 ->
                    x += speed;
                case 2 ->
                    y += speed;
                case 3 ->
                    x -= speed;
            }
        }

        boolean outOfBounds() {
            return x < 0 || y < 0 || x >= COLS * TILE_SIZE || y >= ROWS * TILE_SIZE;
        }

        boolean hitWall() {
            int row = y / TILE_SIZE;
            int col = x / TILE_SIZE;
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                if (map[row][col] == 1) {
                    map[row][col] = 0;
                    return true;
                }
                if (map[row][col] == 2) {
                    return true;
                }
                if (map[row][col] == 3) {
                    gameOver = true;
                    return true;
                }
            }
            return false;
        }

        boolean hitPlayer() {
            if (!isPlayer && x >= playerX && x <= playerX + TILE_SIZE
                    && y >= playerY && y <= playerY + TILE_SIZE) {
                gameOver = true;
                return true;
            }
            return false;
        }

        boolean hitEnemy() {
            if (isPlayer) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy en = enemies.get(i);
                    if (x >= en.x && x <= en.x + TILE_SIZE
                            && y >= en.y && y <= en.y + TILE_SIZE) {
                        enemies.remove(i);
                        return true;
                    }
                }
            }
            return false;
        }

        void draw(Graphics g) {
            g.fillOval(x - 2, y - 2, 4, 4);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        new BattleCity();
    }

    public static void showGame3() {
        JFrame frame = new JFrame("Battle City - Java Swing");
        BattleCity gamePanel = new BattleCity();

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(gamePanel);
        frame.setSize(450, 500); // hoặc COLS * TILE_SIZE + padding
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
