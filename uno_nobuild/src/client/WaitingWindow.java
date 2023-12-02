import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe WaitingWindow rappresenta una finestra di attesa che si apre in
 * attesa di un evento specifico.
 * Estende JDialog per visualizzare un messaggio e un'immagine durante l'attesa.
 */
public class WaitingWindow extends JDialog {
    private Condivisa cond; // Oggetto condiviso che contiene informazioni sull'evento di attesa

    /**
     * Costruttore per la classe WaitingWindow.
     * 
     * @param cond Oggetto Condivisa contenente informazioni sull'evento di attesa.
     */
    public WaitingWindow(Condivisa cond) {
        super((Frame) null, "Attendere...", true);
        this.cond = cond;

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JLabel labelMessage = new JLabel("Attendere...", JLabel.CENTER);
        labelMessage.setFont(new Font("Helvetica", Font.PLAIN, 16));
        add(labelMessage, BorderLayout.CENTER);

        // Aggiunta di un'immagine di attesa
        ImageIcon icon = new ImageIcon("uno_nobuild\\src\\client\\images\\waiting.gif");
        JLabel labelImage = new JLabel(icon);
        add(labelImage, BorderLayout.SOUTH);

        // Chiudi la finestra quando Game.on diventa true
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                new Thread(() -> {
                    // Attende finché Game.on non diventa true
                    while (!cond.Game.isOn()) {
                        try {
                            setVisible(true);
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                    // Chiudi la finestra di attesa quando Game.on diventa true
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                    });
                }).start();
            }
        });
    }
}
