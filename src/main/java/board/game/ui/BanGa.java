/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author DELL
 */
public class BanGa extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 800, HEIGHT = 600;
    private final int PLAYER_WIDTH = 40, PLAYER_HEIGHT = 40;
    private final int CHICKEN_WIDTH = 40, CHICKEN_HEIGHT = 40;
    private final int BULLET_WIDTH = 5, BULLET_HEIGHT = 10;

    private int playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
    private int playerY = HEIGHT - 70;
    private boolean left, right, shooting;
    private int score = 0;
    private boolean gameOver = false;

    private ArrayList<Rectangle> bullets = new ArrayList<>();
    private ArrayList<Rectangle> chickens = new ArrayList<>();
    private Timer gameTimer, chickenSpawner;

    private JButton retryButton;

    public BanGa() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        gameTimer = new Timer(15, this);
        gameTimer.start();

        chickenSpawner = new Timer(1000, e -> spawnChicken());
        chickenSpawner.start();

        retryButton = new JButton("Chơi lại");
        retryButton.setFocusable(false);
        retryButton.addActionListener(e -> resetGame());
        retryButton.setVisible(false);
        this.setLayout(null);
        retryButton.setBounds(WIDTH / 2 - 60, HEIGHT / 2 + 30, 120, 30);
        this.add(retryButton);
    }

    private void spawnChicken() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH - CHICKEN_WIDTH);
        chickens.add(new Rectangle(x, 0, CHICKEN_WIDTH, CHICKEN_HEIGHT));
    }

    private void shoot() {
        bullets.add(new Rectangle(playerX + PLAYER_WIDTH / 2 - BULLET_WIDTH / 2, playerY, BULLET_WIDTH, BULLET_HEIGHT));
    }
    
    private void tryShoot() {
        long now = System.currentTimeMillis();
        if (now - lastShootTime >= SHOOT_DELAY) {
            shoot();
            lastShootTime = now;
        }
    }

    private void resetGame() {
        gameOver = false;
        score = 0;
        chickens.clear();
        bullets.clear();
        playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
        retryButton.setVisible(false);
        gameTimer.start();
        chickenSpawner.start();
        requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (left && playerX > 0) playerX -= 6;
            if (right && playerX < WIDTH - PLAYER_WIDTH) playerX += 6;
            if (shooting) tryShoot();

            Iterator<Rectangle> bulletIter = bullets.iterator();
            while (bulletIter.hasNext()) {
                Rectangle bullet = bulletIter.next();
                bullet.y -= 10;
                if (bullet.y < 0) bulletIter.remove();
            }

            Iterator<Rectangle> chickenIter = chickens.iterator();
            while (chickenIter.hasNext()) {
                Rectangle chicken = chickenIter.next();
                chicken.y += 2;

                Rectangle playerRect = new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);
                if (chicken.intersects(playerRect)) {
                    gameOver = true;
                    gameTimer.stop();
                    chickenSpawner.stop();
                    retryButton.setVisible(true);
                }

                if (chicken.y > HEIGHT) {
                    gameOver = true;
                    gameTimer.stop();
                    chickenSpawner.stop();
                    retryButton.setVisible(true);
                    break; // thoát vòng lặp
                }                                    
            }

            bulletIter = bullets.iterator();
            while (bulletIter.hasNext()) {
                Rectangle bullet = bulletIter.next();
                chickenIter = chickens.iterator();
                while (chickenIter.hasNext()) {
                    Rectangle chicken = chickenIter.next();
                    if (bullet.intersects(chicken)) {
                        bulletIter.remove();
                        chickenIter.remove();
                        score++;
                        break;
                    }
                }
            }

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.CYAN);
        g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);

        g.setColor(Color.YELLOW);
        for (Rectangle bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        g.setColor(Color.WHITE);
        for (Rectangle chicken : chickens) {
            g.fillOval(chicken.x, chicken.y, chicken.width, chicken.height);
        }

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Điểm: " + score, 10, 25);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", WIDTH / 2 - 130, HEIGHT / 2 - 20);

            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Điểm của bạn: " + score, WIDTH / 2 - 100, HEIGHT / 2 + 10);
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = true;
    }
    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chicken Invaders - Bắn Gà");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(new BanGa());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    private long lastShootTime = 0;
    private final int SHOOT_DELAY = 300; // milliseconds

}
