import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WaitingWindow extends JDialog {
    private GameManaging game;

    public WaitingWindow(GameManaging game) {
        super((Frame) null, "Attendere...", true);
        this.game = game;

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JLabel labelMessage = new JLabel("Attendere...", JLabel.CENTER);
        labelMessage.setFont(new Font("Helvetica", Font.PLAIN, 16));
        add(labelMessage, BorderLayout.CENTER);

        ImageIcon icon = new ImageIcon("uno_nobuild\\src\\client\\images\\waiting.gif");
        JLabel labelImage = new JLabel(icon);
        add(labelImage, BorderLayout.SOUTH);

        // Chiudi la finestra quando Game.on diventa true
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                new Thread(() -> {
                    while (!game.isOn()) {
                        try {

                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        dispose(); // Chiudi la finestra di attesa quando Game.on diventa true
                    });
                }).start();
            }
        });
    }
}
