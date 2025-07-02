/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author DELL
 */
public class RanSanMoi extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 600, HEIGHT = 600, TILE_SIZE = 20;
    private final Timer timer;
    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private int score = 0;
    private JButton retryButton, startButton;

    public RanSanMoi() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        // Timer
        timer = new Timer(100, this);

        // Nút "Bắt đầu"
        startButton = new JButton("Bắt đầu");
        startButton.setBounds(WIDTH / 2 - 60, HEIGHT / 2 - 20, 120, 40);
        startButton.setFocusable(false);
        startButton.addActionListener(e -> {
            initGame();
            startButton.setVisible(false);
            requestFocusInWindow();
            timer.start();
        });
        add(startButton);

        // Nút "Chơi lại"
        retryButton = new JButton("Chơi lại");
        retryButton.setBounds(WIDTH / 2 - 60, HEIGHT / 2 + 30, 120, 40);
        retryButton.setFocusable(false);
        retryButton.setVisible(false);
        retryButton.addActionListener(e -> {
            initGame();
            retryButton.setVisible(false);
            requestFocusInWindow();
            timer.start();
        });
        add(retryButton);
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        spawnFood();
        direction = 'R';
        score = 0;
        running = true;
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(WIDTH / TILE_SIZE);
            y = rand.nextInt(HEIGHT / TILE_SIZE);
            food = new Point(x, y);
        } while (snake.contains(food));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }

    private void move() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case 'U' -> head.y--;
            case 'D' -> head.y++;
            case 'L' -> head.x--;
            case 'R' -> head.x++;
        }
        snake.add(0, head);
        if (head.equals(food)) {
            score++;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH / TILE_SIZE || head.y < 0 || head.y >= HEIGHT / TILE_SIZE) {
            gameOver();
            return;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {
        running = false;
        timer.stop();
        retryButton.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ lưới (tùy chọn)
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < WIDTH / TILE_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, HEIGHT);
            g.drawLine(0, i * TILE_SIZE, WIDTH, i * TILE_SIZE);
        }

        // Vẽ mồi
        if (food != null) {
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Vẽ rắn
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            g.setColor(i == 0 ? Color.GREEN : Color.LIGHT_GRAY);
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // 👀 Vẽ mắt cho đầu rắn
            if (i == 0) {
                int eyeSize = 4;
                int offsetX = 0, offsetY = 0;

                switch (direction) {
                    case 'U' -> offsetY = -2;
                    case 'D' -> offsetY = 2;
                    case 'L' -> offsetX = -2;
                    case 'R' -> offsetX = 2;
                }

                int px = p.x * TILE_SIZE;
                int py = p.y * TILE_SIZE;

                // Mắt trái
                g.setColor(Color.WHITE);
                g.fillOval(px + 4, py + 4, eyeSize, eyeSize);
                g.setColor(Color.BLACK);
                g.fillOval(px + 4 + offsetX, py + 4 + offsetY, 2, 2);

                // Mắt phải
                g.setColor(Color.WHITE);
                g.fillOval(px + 12, py + 4, eyeSize, eyeSize);
                g.setColor(Color.BLACK);
                g.fillOval(px + 12 + offsetX, py + 4 + offsetY, 2, 2);
            }
        }

        // Vẽ điểm số
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Điểm: " + score, 10, 20);

        // Vẽ chữ Game Over
        if (!running && !startButton.isVisible()) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH / 2 - 120, HEIGHT / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!running) return;

        char newDir = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> 'U';
            case KeyEvent.VK_DOWN -> 'D';
            case KeyEvent.VK_LEFT -> 'L';
            case KeyEvent.VK_RIGHT -> 'R';
            default -> direction;
        };

        if ((direction == 'U' && newDir != 'D') ||
            (direction == 'D' && newDir != 'U') ||
            (direction == 'L' && newDir != 'R') ||
            (direction == 'R' && newDir != 'L')) {
            direction = newDir;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rắn Săn Mồi");
        RanSanMoi gamePanel = new RanSanMoi();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
