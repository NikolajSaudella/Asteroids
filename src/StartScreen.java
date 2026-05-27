import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class StartScreen extends JPanel {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private SoundPlayer soundPlayer;

    public StartScreen() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        setLayout(new GridBagLayout());

        soundPlayer = new SoundPlayer();

        // suono quando si apre la schermata home
        soundPlayer.play("/sounds/start_screen.wav");

        JLabel title = new JLabel("ASTEROIDS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Monospaced", Font.BOLD, 64));

        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        startButton.setFocusable(false);

        JButton settingsButton = new JButton("SETTINGS");
        settingsButton.setFont(new Font("Monospaced", Font.BOLD, 18));
        settingsButton.setFocusable(false);

        JButton infoButton = new JButton("INFO COMANDI");
        infoButton.setFont(new Font("Monospaced", Font.BOLD, 18));
        infoButton.setFocusable(false);

        // --- azione START ---
        startButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            GameArea game = new GameArea();
            frame.setContentPane(game);
            frame.revalidate();
            frame.repaint();
            game.requestFocusInWindow();
        });

        // --- azione SETTINGS ---
        settingsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this,
                "Nessuna impostazione disponibile per ora.",
                "Impostazioni",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        // --- azione INFO COMANDI ---
        infoButton.addActionListener(e -> {
            String comandi =
                "╔══════════════════════════════╗\n" +
                "║       COMANDI DI GIOCO       ║\n" +
                "╠══════════════════════════════╣\n" +
                "║  ← / A     Ruota a sinistra  ║\n" +
                "║  → / D     Ruota a destra    ║\n" +
                "║  ↑ / W     Accelera          ║\n" +
                "║  SPAZIO    Spara             ║\n" +
                "║  P / ESC   Pausa             ║\n" +
                "║  R         Ricomincia        ║\n" +
                "╚══════════════════════════════╝";

            JOptionPane.showMessageDialog(
                this,
                comandi,
                "Info Comandi",
                JOptionPane.PLAIN_MESSAGE
            );
        });

        // dimensione uguale per tutti i bottoni
        Dimension btnSize = new Dimension(220, 45);
        startButton.setPreferredSize(btnSize);
        settingsButton.setPreferredSize(btnSize);
        infoButton.setPreferredSize(btnSize);

        // --- layout GridBag ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;

        // titolo
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(title, gbc);

        // START
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        add(startButton, gbc);

        // SETTINGS
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 12, 0);
        add(settingsButton, gbc);

        // INFO COMANDI
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(infoButton, gbc);
    }
}
