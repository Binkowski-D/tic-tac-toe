package tic_tac_toe;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.*;

/**
 * @author Damian Binkowski
 */
public class Tic_tac_toe extends JFrame {

    public Tic_tac_toe() {
        super("Tic-tac-toe");
        this.setSize(500, 500);
        ImageIcon icon = new ImageIcon("icon.png");
        this.setIconImage(icon.getImage());

        JPanel backgroundPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image image = new ImageIcon("background.jpg").getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        this.setContentPane(backgroundPanel);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New game");
        newGameItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Czy chcesz zagrać jeszcze raz?", "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                clearBoard();
            }
        });

        gameMenu.add(newGameItem);
        menuBar.add(gameMenu);
        this.setJMenuBar(menuBar);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents();
    }

    public void initComponents() {
        startPanel = new JPanel();
        startPanel.setPreferredSize(new Dimension(500, 85));
        startPanel.setLayout(new BorderLayout());
        startPanel.setBackground(Color.WHITE);

        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(Color.WHITE);

        leftLabel = new JLabel();
        leftLabel.setFont(FONT);

        rightLabel = new JLabel();
        rightLabel.setFont(FONT);

        xButton = new JButton("X");
        xButton.addActionListener(e -> {
            player = xButton.getText();
            leftLabel.setText("Gracz 1");
            rightLabel.setText("Gracz 2");
            xButton.setEnabled(false);
            oButton.setEnabled(false);
            if (currentPlayer == 1) {
                player = "X";
                label.setText("Twój ruch " + leftLabel.getText() + " : X");
            } else {
                player = "O";
                label.setText("Twój ruch " + rightLabel.getText() + " : O");
            }
        });

        oButton = new JButton("O");
        oButton.addActionListener(e -> {
            player = oButton.getText();
            leftLabel.setText("Gracz 2");
            rightLabel.setText("Gracz 1");
            xButton.setEnabled(false);
            oButton.setEnabled(false);
            if (currentPlayer == 2) {
                player = "O";
                label.setText("Twój ruch " + rightLabel.getText() + " : O");
            } else {
                player = "X";
                label.setText("Twój ruch " + leftLabel.getText() + " : X");
            }
        });

        topPanel.add(leftLabel);
        topPanel.add(xButton);
        topPanel.add(oButton);
        topPanel.add(rightLabel);

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);

        label = new JLabel("Wybierz symbol pierwszego gracza");
        label.setFont(FONT);

        bottomPanel.add(label);

        startPanel.add(topPanel, BorderLayout.CENTER);
        startPanel.add(bottomPanel, BorderLayout.SOUTH);

        gamePanel = new JPanel(new GridLayout(3, 3)) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2D = (Graphics2D) g;
                g2D.setColor(Color.BLACK);
                g2D.setStroke(new BasicStroke(5));

                for (int i = 0; i < 3; i++) {
                    if (!gameLabels[i][0].getText().isEmpty() && gameLabels[i][0].getText().equals(gameLabels[i][1].getText()) && gameLabels[i][0].getText().equals(gameLabels[i][2].getText())) {
                        int y = gameLabels[i][0].getLocation().y + gameLabels[i][0].getHeight() / 2;
                        g2D.drawLine(0, y, getWidth(), y);
                    }

                    if (!gameLabels[0][i].getText().isEmpty() && gameLabels[0][i].getText().equals(gameLabels[1][i].getText()) && gameLabels[0][i].getText().equals(gameLabels[2][i].getText())) {
                        int x = gameLabels[0][i].getLocation().x + gameLabels[0][i].getWidth() / 2;
                        g2D.drawLine(x, 0, x, getHeight());
                    }
                }

                if (!gameLabels[0][0].getText().isEmpty() && gameLabels[0][0].getText().equals(gameLabels[1][1].getText()) && gameLabels[0][0].getText().equals(gameLabels[2][2].getText())) {
                    g2D.drawLine(0, 0, getWidth(), getHeight());
                }

                if (!gameLabels[2][0].getText().isEmpty() && gameLabels[2][0].getText().equals(gameLabels[1][1].getText()) && gameLabels[2][0].getText().equals(gameLabels[0][2].getText())) {
                    g2D.drawLine(0, getHeight(), getWidth(), 0);
                }
            }
        };
        gamePanel.setOpaque(false);

        for (JLabel[] gameLabel : gameLabels) {
            for (int j = 0; j < 3; j++) {
                gameLabel[j] = new JLabel();
                gameLabel[j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gameLabel[j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (gameEnd) {
                            int result = JOptionPane.showConfirmDialog(null, "Czy chcesz zagrać jeszcze raz?", "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (result == JOptionPane.YES_OPTION) {
                                clearBoard();
                            }
                        }

                        if (player.isEmpty() || clickCount >= 9) {
                            return;
                        }

                        JLabel clicked = (JLabel) e.getSource();

                        if (clickedLabels.contains(clicked)) {
                            return;
                        }

                        clicked.setHorizontalAlignment(SwingConstants.CENTER);
                        clicked.setVerticalAlignment(SwingConstants.CENTER);
                        clicked.setFont(new Font("Roboto", Font.PLAIN, 30));

                        if ("X".equals(player)) {
                            clicked.setText("X");
                            label.setText("Twój ruch " + rightLabel.getText() + " : O");
                            player = "O";
                        } else {
                            clicked.setText("O");
                            label.setText("Twój ruch " + leftLabel.getText() + " : X");
                            player = "X";
                        }

                        clickedLabels.add(clicked);
                        clickCount++;

                        if (checkWinCondition()) {
                            gamePanel.repaint();
                            gameEnd = true;

                            label.setText((player.equals("O") ? leftLabel.getText() + " : X" : rightLabel.getText() + " : O") + " wygrał. Gratulacje!");
                        } else if (clickCount >= 9) {
                            gameEnd = true;
                            label.setText("Remis. Nikt nie wygrał");
                        }
                    }
                });
                gamePanel.add(gameLabel[j]);
            }
        }

        endPanel = new JPanel();
        endPanel.setPreferredSize(new Dimension(500, 85));
        endPanel.setBackground(Color.WHITE);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjść z gry?", "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        endPanel.add(exitButton);

        this.add(startPanel, BorderLayout.PAGE_START);
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(endPanel, BorderLayout.PAGE_END);
    }

    private JPanel startPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel gamePanel;
    private JPanel endPanel;
    private JButton exitButton;
    final static Font FONT = new Font("Roboto", Font.PLAIN, 15);
    private String player = "";
    private final Random random = new Random();
    private final int currentPlayer = random.nextInt(2) + 1;
    private JLabel label;
    private JLabel leftLabel;
    private JLabel rightLabel;
    private JButton xButton;
    private JButton oButton;
    private final JLabel[][] gameLabels = new JLabel[3][3];
    private int clickCount = 0;
    private final Set<JLabel> clickedLabels = new HashSet<>();
    private boolean gameEnd = false;

    public void clearBoard() {
        for (JLabel[] gameLabel : gameLabels) {
            for (int j = 0; j < gameLabel.length; j++) {
                gameLabel[j].setText("");
            }
        }

        xButton.setEnabled(true);
        oButton.setEnabled(true);
        leftLabel.setText("");
        rightLabel.setText("");
        label.setText("Wybierz symbol pierwszego gracza");
        gameEnd = false;
        clickCount = 0;
        player = "";
        clickedLabels.clear();
    }

    public boolean checkWinCondition() {
        for (int i = 0; i < 3; i++) {
            if (!gameLabels[i][0].getText().isEmpty() && gameLabels[i][0].getText().equals(gameLabels[i][1].getText()) && gameLabels[i][0].getText().equals(gameLabels[i][2].getText())) {
                return true;
            }

            if (!gameLabels[0][i].getText().isEmpty() && gameLabels[0][i].getText().equals(gameLabels[1][i].getText()) && gameLabels[0][i].getText().equals(gameLabels[2][i].getText())) {
                return true;
            }
        }

        return !gameLabels[0][0].getText().isEmpty() && gameLabels[0][0].getText().equals(gameLabels[1][1].getText()) && gameLabels[0][0].getText().equals(gameLabels[2][2].getText())
                || !gameLabels[2][0].getText().isEmpty() && gameLabels[2][0].getText().equals(gameLabels[1][1].getText()) && gameLabels[2][0].getText().equals(gameLabels[0][2].getText());
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Tic_tac_toe().setVisible(true));
    }
}
