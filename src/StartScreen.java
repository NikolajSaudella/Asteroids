import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class StartScreen extends JPanel {


    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;


    public StartScreen() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);

    setLayout(new GridBagLayout());

    JLabel title = new JLabel("ASTEROIDS");
    title.setForeground(Color.WHITE);
    title.setFont(new Font("Monospaced", Font.BOLD, 64));

    JButton startButton = new JButton("START");
    startButton.setFont(new Font("Monospaced", Font.BOLD, 24));
    startButton.setFocusable(false);
    
    startButton.addActionListener(e -> {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        GameArea game = new GameArea();
        frame.setContentPane(game);

        frame.revalidate();
        frame.repaint();

        game.requestFocusInWindow();
    });

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 40, 0);

    add(title, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 0, 0);

    add(startButton, gbc);
}


}
