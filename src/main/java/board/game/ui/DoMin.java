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
public class DoMin extends JPanel {

    private final int ROWS = 40;
    private final int COLS = 75;
    private final int MINES = 300;

    private JButton[][] buttons = new JButton[ROWS][COLS];
    private boolean[][] mines = new boolean[ROWS][COLS];
    private boolean[][] revealed = new boolean[ROWS][COLS];
    private boolean[][] flagged = new boolean[ROWS][COLS];
    private boolean gameOver = false;

    private JPanel boardPanel;
    private JButton replayButton;

    public DoMin() {
        setLayout(new BorderLayout());

        boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        add(boardPanel, BorderLayout.CENTER);

        replayButton = new JButton("Replay");
        replayButton.addActionListener(e -> resetGame());
        add(replayButton, BorderLayout.SOUTH);

        initGame();
    }

    private void initGame() {
        boardPanel.removeAll();
        gameOver = false;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 12));
                buttons[row][col].setMargin(new Insets(0, 0, 0, 0));

                final int r = row, c = col;
                buttons[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }

                        if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlag(r, c);
                        } else {
                            if (!flagged[r][c]) {
                                handleClick(r, c);
                            }
                        }
                    }
                });

                boardPanel.add(buttons[row][col]);
                mines[row][col] = false;
                revealed[row][col] = false;
                flagged[row][col] = false;
            }
        }

        placeMines();
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void resetGame() {
        initGame();
    }

    private void toggleFlag(int r, int c) {
        if (revealed[r][c]) {
            return;
        }

        flagged[r][c] = !flagged[r][c];
        buttons[r][c].setText(flagged[r][c] ? "🚩" : "");
    }

    private void handleClick(int r, int c) {
        if (mines[r][c]) {
            buttons[r][c].setText("💥");
            revealAllMines();
            JOptionPane.showMessageDialog(this, "Bạn đã thua!");
            gameOver = true;
        } else {
            reveal(r, c);
            if (checkWin()) {
                revealAllMines();
                JOptionPane.showMessageDialog(this, "🏆 Bạn đã thắng!");
                gameOver = true;
            }
        }
    }

    private void placeMines() {
        int placed = 0;
        while (placed < MINES) {
            int r = (int) (Math.random() * ROWS);
            int c = (int) (Math.random() * COLS);
            if (!mines[r][c]) {
                mines[r][c] = true;
                placed++;
            }
        }
    }

    private int countAdjacentMines(int r, int c) {
        int count = 0;
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < ROWS && j >= 0 && j < COLS && mines[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void reveal(int r, int c) {
        if (r < 0 || r >= ROWS || c < 0 || c >= COLS || revealed[r][c] || flagged[r][c]) {
            return;
        }

        revealed[r][c] = true;
        int count = countAdjacentMines(r, c);
        buttons[r][c].setText(count == 0 ? "" : String.valueOf(count));
        buttons[r][c].setEnabled(false);

        if (count == 0) {
            for (int i = r - 1; i <= r + 1; i++) {
                for (int j = c - 1; j <= c + 1; j++) {
                    if (i != r || j != c) {
                        reveal(i, j);
                    }
                }
            }
        }
    }

    private void revealAllMines() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (mines[r][c]) {
                    buttons[r][c].setText("BOOM");
                    buttons[r][c].setEnabled(false);
                    if (!revealed[r][c]) {
                        buttons[r][c].setBackground(Color.RED);
                    }
                }
            }
        }
    }

    private boolean checkWin() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!mines[r][c] && !revealed[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoMin::new);
    }

    public static void showGame4() {
        JFrame frame = new JFrame("Dò Mìn");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.add(new DoMin()); // Thêm JPanel vào JFrame
    frame.pack(); // Tự động fit size
    frame.setSize(950, 600); // Hoặc giữ nguyên size nếu bạn muốn cố định
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    }
}
