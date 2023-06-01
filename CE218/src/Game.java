import javax.swing.*;

public class Game extends JFrame {

    public Game() {
        initializeUI();
    }

    private void initializeUI() {

        add(new View());

        setTitle("HEINZ: Curse of Doofenshmirtz 1902672");
        setSize( 700, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
