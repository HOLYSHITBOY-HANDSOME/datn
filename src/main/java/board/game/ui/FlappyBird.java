package board.game.ui;

import board.game.dao.DiemDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FlappyBird {

    private String userId;
    private BoardGameJFrame parentFrame;

    public FlappyBird(String userId, BoardGameJFrame parentFrame) {
        this.userId = userId;
        this.parentFrame = parentFrame;

        JFrame frame = new JFrame("Flappy Bird - Java");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Close window game");
                GamePanel gamePanel = (GamePanel) frame.getContentPane().getComponent(0);
                gamePanel.stopTimer();
            }
        });

        GamePanel gamePanel = new GamePanel(userId, parentFrame); // 👈 sửa tại đây
        frame.add(gamePanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {

    private Image birdImage;
    private Image pipeTopImage;
    private Image pipeBottomImage;
    private Image background;

    // 🔧 Đã sửa: cập nhật kích thước giao diện
    private final int WIDTH = 500, HEIGHT = 700;

    private int birdY = HEIGHT / 2, birdVelocity = 0;
    private final int BIRD_SIZE = 40;

    private ArrayList<Rectangle> pipes = new ArrayList<>();
    private final int PIPE_WIDTH = 60, PIPE_GAP = 150;

    private Timer timer;
    private boolean running = false;
    private boolean paused = false; // 🟨 Thêm dòng này
    private int score = 0;
    private boolean isRestarting = true;

    private String userId;
    private BoardGameJFrame parentFrame;
    private final String gameId = "game001";

    public GamePanel(String userId, BoardGameJFrame parentFrame) {
        this.userId = userId;
        this.parentFrame = parentFrame;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        birdImage = new ImageIcon(getClass().getResource("/board/game/icons/flappy-bird-sprite.png")).getImage();
        pipeTopImage = new ImageIcon(getClass().getResource("/board/game/icons/ongtren.png")).getImage();
        pipeBottomImage = new ImageIcon(getClass().getResource("/board/game/icons/ongduoi.png")).getImage();
        background = new ImageIcon(getClass().getResource("/board/game/icons/backgroundflappy.png")).getImage();

        timer = new Timer(20, this);
        timer.start();

        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);
    }

    private void addPipe(boolean start) {
        int space = PIPE_GAP;
        int height = 50 + new Random().nextInt(300);
        int x = start ? WIDTH + pipes.size() * 300 : pipes.get(pipes.size() - 1).x + 300;
        pipes.add(new Rectangle(x, 0, PIPE_WIDTH, height));
        pipes.add(new Rectangle(x, height + space, PIPE_WIDTH, HEIGHT - height - space));
    }

    public void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    private void endGame() {
        if (!running) {
            return;
        }

        running = false;
        timer.stop();

        Window win = SwingUtilities.getWindowAncestor(this);
        if (win != null && win.isDisplayable()) {

            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("userId rỗng, không thể lưu điểm.");
                return;
            }

            DiemDAO.DiemService diemService = new DiemDAO.DiemService();
            diemService.capNhatDiemCaoNhat(userId.trim(), gameId, score);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Game Over!\nĐiểm của bạn là: " + score + "\nBạn muốn chơi lại không?",
                    "Kết thúc",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                resetGameState();
                isRestarting = true;
            }
            if (result == JOptionPane.NO_OPTION) {
                if (win != null) {
                    win.dispose();
                }
                parentFrame.setVisible(true);
            }

        }
    }

    private void resetGameState() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        pipes.clear();
        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);
        paused = false;
        running = false; // ✅ chưa bắt đầu lại
        repaint(); // cập nhật giao diện ngay
    }

    private void jump() {
        if (!running) {
            if (isRestarting) {
                birdY = HEIGHT / 2;
                birdVelocity = 0;
                score = 0;
                pipes.clear();

                addPipe(true);
                addPipe(true);
                addPipe(true);
                addPipe(true);

                isRestarting = false; // sau khi reset xong thì không cần nữa
            }

            running = true;
            timer.start();
        }

        birdVelocity = -10;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            birdVelocity += 1;
            birdY += birdVelocity;

            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= 5;

                if (pipe.intersects(new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE))) {
                    endGame();
                }

                if (pipe.y == 0 && pipe.x + PIPE_WIDTH == 100) {
                    score++;
                }
            }

            // Đề xuất sửa: đảm bảo xóa & thêm ống đúng cách
            while (!pipes.isEmpty() && pipes.get(0).x + PIPE_WIDTH < 0) {
                pipes.remove(0);
                pipes.remove(0);
                addPipe(false);
            }

            // Va chạm mép trên/dưới màn hình
            if (birdY > HEIGHT - BIRD_SIZE || birdY < 0) {
                endGame();
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Vẽ ảnh nền (đúng kích thước giao diện)
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, this);

        // Vẽ con chim
        g.drawImage(birdImage, 100, birdY, BIRD_SIZE, BIRD_SIZE, this);

        // Vẽ các ống
        for (Rectangle pipe : pipes) {
            if (pipe.y == 0) {
                g2d.drawImage(pipeTopImage, pipe.x, pipe.y, pipe.width, pipe.height, this);
            } else {
                g2d.drawImage(pipeBottomImage, pipe.x, pipe.y, pipe.width, pipe.height, this);
            }
        }

        // Vẽ điểm số
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Điểm: " + score, 20, 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String pauseHint = "Ấn P để tạm dừng";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(pauseHint);
        g.drawString(pauseHint, WIDTH - textWidth - 20, 40); // 20px từ mép phải

        // Hiển thị thông báo bắt đầu nếu chưa chạy
        if (!running && !paused) {
            g.drawString("Nhấn SPACE để bắt đầu!", WIDTH / 2 - 130, HEIGHT / 2 - 50);
        }

        // 🟡 Thêm vào cuối hàm: hiển thị giao diện pause
        if (paused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("Đang tạm dừng", WIDTH / 2 - 100, HEIGHT / 2 - 20);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Nhấn P để tiếp tục", WIDTH / 2 - 90, HEIGHT / 2 + 10);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Nhấn phím SPACE để nhảy
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }

        // 🟨 Nhấn phím P để tạm dừng hoặc tiếp tục
        if (e.getKeyCode() == KeyEvent.VK_P) {
            paused = !paused;

            if (paused) {
                timer.stop();

                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Bạn đang tạm dừng!\nNhấn OK để tiếp tục chơi.",
                        "Tạm dừng",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    paused = false;
                    running = false;
                    isRestarting = false; // 👈 Thêm dòng này để không reset
                    repaint();
                }

            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
