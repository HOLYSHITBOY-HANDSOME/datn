package board.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FlappyBird {

    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird - Java");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new GamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {

    private Image birdImage;
    private Image pipeTopImage;
    private Image pipeBottomImage;

    private final int WIDTH = 400, HEIGHT = 600;
    private int birdY = HEIGHT / 2, birdVelocity = 0;
    private final int BIRD_SIZE = 40;

    private ArrayList<Rectangle> pipes = new ArrayList<>();
    private final int PIPE_WIDTH = 60, PIPE_GAP = 150;

    private Timer timer;
    private boolean running = false;
    private int score = 0;

    GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        // Load ảnh từ thư mục resources (src/board/game/icons/)
        birdImage = new ImageIcon(getClass().getResource("/board/game/icons/uia.png")).getImage();
        pipeTopImage = new ImageIcon(getClass().getResource("/board/game/icons/ongtren.png")).getImage();
        pipeBottomImage = new ImageIcon(getClass().getResource("/board/game/icons/ongduoi.png")).getImage();

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
        int x = start ? WIDTH + pipes.size() * 200 : pipes.get(pipes.size() - 1).x + 200;
        pipes.add(new Rectangle(x, 0, PIPE_WIDTH, height));
        pipes.add(new Rectangle(x, height + space, PIPE_WIDTH, HEIGHT - height - space));
    }

    private void jump() {
        if (!running) {
            birdY = HEIGHT / 2;
            birdVelocity = 0;
            score = 0;
            pipes.clear();
            addPipe(true);
            addPipe(true);
            addPipe(true);
            addPipe(true);
            running = true;
        }
        birdVelocity = -10;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            birdVelocity += 1;
            birdY += birdVelocity;

            Iterator<Rectangle> it = pipes.iterator();
            while (it.hasNext()) {
                Rectangle pipe = it.next();
                pipe.x -= 5;

                if (pipe.intersects(new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE))) {
                    running = false;
                }

                if (pipe.y == 0 && pipe.x + PIPE_WIDTH == 100) {
                    score++;
                }
            }

            if (!pipes.isEmpty() && pipes.get(0).x + PIPE_WIDTH < 0) {
                pipes.remove(0);
                pipes.remove(0);
                addPipe(false);
            }

            if (birdY > HEIGHT - BIRD_SIZE || birdY < 0) {
                running = false;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Vẽ nền trời
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Vẽ nền đất
        g.setColor(Color.green.darker());
        g.fillRect(0, HEIGHT - 100, WIDTH, 100);

        // Vẽ chim
        g.drawImage(birdImage, 100, birdY, BIRD_SIZE, BIRD_SIZE, this);

        // Vẽ ống
        for (Rectangle pipe : pipes) {
            if (pipe.y == 0) {
                // ======== ỐNG TRÊN ========
                // Vẽ ống trên (ảnh đã đầy đủ: gồm đầu + thân)
                g2d.drawImage(pipeTopImage,
                        pipe.x, pipe.y,
                        pipe.width, pipe.height,
                        this);
            } else {
                // ======== ỐNG DƯỚI ========
                // Vẽ ống dưới (ảnh đã đầy đủ: gồm đầu + thân)
                g2d.drawImage(pipeBottomImage,
                        pipe.x, pipe.y,
                        pipe.width, pipe.height,
                        this);
            }
        }

        // Vẽ điểm
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        if (!running) {
            g.drawString("Nhấn SPACE để bắt đầu!", 70, HEIGHT / 2 - 50);
        }
        g.drawString("Điểm: " + score, 20, 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
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
