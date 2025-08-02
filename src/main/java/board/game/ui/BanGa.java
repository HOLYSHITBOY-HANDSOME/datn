package board.game.ui;

import board.game.dao.DiemDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import board.game.ui.BoardGameJFrame;

class Explosion {

    private int x, y;
    private int width, height;
    private int timeVisible = 15;
    private Image image;

    public Explosion(int x, int y, Image img, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = img;
    }

    public void draw(Graphics g) {
        if (timeVisible > 0) {
            g.drawImage(image, x, y, width, height, null);
            timeVisible--;
        }
    }

    public boolean isDone() {
        return timeVisible <= 0;
    }
}

public class BanGa extends JPanel implements ActionListener, KeyListener {

    private BoardGameJFrame parentFrame;
    private String userId;
    private DiemDAO.DiemService diemService;
    private String gameId = "BanGa"; // Giữ nguyên hoặc tùy hệ thống

    private final int WIDTH = 1000, HEIGHT = 800;
    private final int PLAYER_WIDTH = 60, PLAYER_HEIGHT = 60;
    private final int CHICKEN_WIDTH = 50, CHICKEN_HEIGHT = 50;
    private final int BULLET_WIDTH = 20, BULLET_HEIGHT = 30;

    private int playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
    private int playerY = HEIGHT - 80;
    private boolean left, right, up, down, shooting;
    private int score = 0;
    private int hp = 10;
    private boolean gameOver = false;
    private boolean showMinusOne = false;
    private long minusOneTimer = 0;

    private ArrayList<Rectangle> bullets = new ArrayList<>();
    private ArrayList<Rectangle> chickens = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();

    private Timer gameTimer;
    private Timer chickenSpawner;
    private int spawnDelay = 1000;
    private boolean paused = false;
    private JButton retryButton;

    private Image spaceshipImg;
    private Image meteorImg;
    private Image backgroundImg;
    private Image bulletImg;
    private Image explosionImg;

    private long lastShootTime = 0;
    private final int SHOOT_DELAY = 300;

    public BanGa(String userId, String gameId) {
        this(userId, gameId, null);
    }

    public BanGa(String userId, String gameId, BoardGameJFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.userId = (userId == null) ? "" : userId.trim();
        this.gameId = gameId;
        this.diemService = new DiemDAO.DiemService();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        spaceshipImg = new ImageIcon(getClass().getResource("/board/game/icons/spaceship.png")).getImage();
        meteorImg = new ImageIcon(getClass().getResource("/board/game/icons/meteor.png")).getImage();
        backgroundImg = new ImageIcon(getClass().getResource("/board/game/icons/anhbanga.png")).getImage();
        bulletImg = new ImageIcon(getClass().getResource("/board/game/icons/beambeam.png")).getImage();
        explosionImg = new ImageIcon(getClass().getResource("/board/game/icons/explosion.png")).getImage();

        gameTimer = new Timer(15, this);
        gameTimer.start();

        startChickenSpawner(spawnDelay);

        retryButton = new JButton("Chơi lại");
        retryButton.setFocusable(false);
        retryButton.addActionListener(e -> resetGame());
        retryButton.setVisible(false);
        this.setLayout(null);
        retryButton.setBounds(WIDTH / 2 - 60, HEIGHT / 2 + 40, 120, 30);
        this.add(retryButton);
    }

    private void startChickenSpawner(int delay) {
        if (chickenSpawner != null) {
            chickenSpawner.stop();
        }
        chickenSpawner = new Timer(delay, e -> spawnChicken());
        chickenSpawner.start();
    }

    private void spawnChicken() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH - CHICKEN_WIDTH);
        chickens.add(new Rectangle(x, 0, CHICKEN_WIDTH, CHICKEN_HEIGHT));
    }

    private void shoot() {
        int hitboxWidth = 30;
        int hitboxHeight = 80;
        int hitboxX = playerX + PLAYER_WIDTH / 2 - hitboxWidth / 2;
        int hitboxY = playerY + (BULLET_HEIGHT - hitboxHeight) / 2;
        Rectangle bullet = new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
        bullets.add(bullet);
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
        hp = 10;
        spawnDelay = 1000;
        showMinusOne = false;
        bullets.clear();
        chickens.clear();
        explosions.clear();
        playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
        playerY = HEIGHT - 80;
        retryButton.setVisible(false);
        gameTimer.start();
        startChickenSpawner(spawnDelay);
        requestFocusInWindow();
        left = false;
        right = false;
        up = false;
        down = false;
        shooting = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (left && playerX > 0) {
                playerX -= 8;
            }
            if (right && playerX < WIDTH - PLAYER_WIDTH) {
                playerX += 8;
            }
            if (up && playerY > 0) {
                playerY -= 8;
            }
            if (down && playerY < HEIGHT - PLAYER_HEIGHT) {
                playerY += 8;
            }
            if (shooting) {
                tryShoot();
            }

            Iterator<Rectangle> bulletIter = bullets.iterator();
            while (bulletIter.hasNext()) {
                Rectangle bullet = bulletIter.next();
                bullet.y -= 10;
                if (bullet.y < 0) {
                    bulletIter.remove();
                }
            }

            Iterator<Rectangle> chickenIter = chickens.iterator();
            while (chickenIter.hasNext()) {
                Rectangle chicken = chickenIter.next();
                chicken.y += 2;

                Rectangle playerRect = new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);
                if (chicken.intersects(playerRect)) {
                    chickenIter.remove();
                    explosions.add(new Explosion(playerX, playerY, explosionImg, PLAYER_WIDTH, PLAYER_HEIGHT));
                    loseHp();
                }

                if (chicken.y > HEIGHT) {
                    chickenIter.remove();
                    explosions.add(new Explosion(chicken.x, HEIGHT - CHICKEN_HEIGHT, explosionImg, PLAYER_WIDTH, PLAYER_HEIGHT));
                    loseHp();
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
                        explosions.add(new Explosion(chicken.x, chicken.y, explosionImg, PLAYER_WIDTH, PLAYER_HEIGHT));
                        score++;
                        if (score % 25 == 0 && spawnDelay > 200) {
                            spawnDelay -= 100;
                            startChickenSpawner(spawnDelay);
                        }
                        break;
                    }
                }
            }

            repaint();
        }
    }

    private void loseHp() {
        hp--;
        showMinusOne = true;
        minusOneTimer = System.currentTimeMillis();
        Window win = SwingUtilities.getWindowAncestor(this);

        if (hp <= 0) {
            gameOver = true;
            gameTimer.stop();
            chickenSpawner.stop();
            retryButton.setVisible(true);

            diemService.capNhatDiemCaoNhat(userId, "game002", score);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Game Over!\nĐiểm của bạn là: " + score + "\nBạn muốn chơi lại không?",
                    "Kết thúc",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (result == JOptionPane.NO_OPTION) {
                if (win != null) {
                    win.dispose();
                } else {
                    // Nếu không lấy được cửa sổ hiện tại, thì thoát toàn chương trình
                    System.exit(0);
                }

                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImg, 0, 0, WIDTH, HEIGHT, this);
        g.drawImage(spaceshipImg, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);

        for (Rectangle bullet : bullets) {
            g.drawImage(bulletImg, bullet.x, bullet.y, bullet.width, bullet.height, this);
        }

        for (Rectangle chicken : chickens) {
            g.drawImage(meteorImg, chicken.x, chicken.y, CHICKEN_WIDTH, CHICKEN_HEIGHT, this);
        }

        for (int i = 0; i < explosions.size(); i++) {
            Explosion exp = explosions.get(i);
            exp.draw(g);
            if (exp.isDone()) {
                explosions.remove(i);
                i--;
            }
        }

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Điểm: " + score, 10, 25);

        g.setColor(Color.ORANGE);
        g.drawString("Máu: " + hp + "/10", WIDTH - 140, 25);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        String hintCenter = "SPACE để bắn • P để tạm dừng";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(hintCenter);
        int x = WIDTH / 2 - textWidth / 2;
        int y = 70;
        g.drawString(hintCenter, x, y);

        if (showMinusOne) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("-1", playerX + PLAYER_WIDTH / 2 - 10, playerY - 10);
            if (System.currentTimeMillis() - minusOneTimer > 1000) {
                showMinusOne = false;
            }
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", WIDTH / 2 - 140, HEIGHT / 2 - 30);
            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Điểm của bạn: " + score, WIDTH / 2 - 100, HEIGHT / 2 + 10);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A ->
                left = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
                right = true;
            case KeyEvent.VK_UP, KeyEvent.VK_W ->
                up = true;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
                down = true;
            case KeyEvent.VK_SPACE ->
                shooting = true;

            case KeyEvent.VK_P -> {
                if (!paused && !gameOver) {
                    paused = true;
                    gameTimer.stop();
                    chickenSpawner.stop();

                    int result = JOptionPane.showConfirmDialog(
                            this,
                            "Bạn đang tạm dừng!\nNhấn OK để tiếp tục chơi.",
                            "Tạm dừng",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    if (result == JOptionPane.OK_OPTION) {
                        paused = false;
                        gameTimer.start();
                        chickenSpawner.start();
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A ->
                left = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
                right = false;
            case KeyEvent.VK_UP, KeyEvent.VK_W ->
                up = false;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
                down = false;
            case KeyEvent.VK_SPACE ->
                shooting = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        String userId = "user001";
        String gameId = "BanGa";

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chicken Invaders - Bắn Gà");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            BanGa gamePanel = new BanGa(userId, gameId);
            frame.setContentPane(gamePanel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void showGame2(String userId, String gameId) {
        BanGa gamePanel = new BanGa(userId, gameId);
        JFrame frame = new JFrame("Bắn Gà");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(gamePanel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
