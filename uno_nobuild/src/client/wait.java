import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class wait {

    private JDialog waitDialog;

    public void showWaitWindow() {
        SwingUtilities.invokeLater(() -> {
            // Crea la finestra di attesa
            waitDialog = new JDialog((Frame) null, "Attendi...", true);
            waitDialog.setSize(300, 200);
            waitDialog.setLocationRelativeTo(null);
            waitDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            // Aggiungi un'immagine di attesa (sostituisci con il tuo percorso dell'immagine)
            ImageIcon loadingIcon = new ImageIcon("uno_nobuild\\src\\docs\\images\\waiting.gif");
            JLabel loadingLabel = new JLabel(loadingIcon);

            // Configura il layout
            waitDialog.setLayout(new BorderLayout());
            waitDialog.add(loadingLabel, BorderLayout.CENTER);

            // Aggiungi un ascoltatore per gestire la chiusura della finestra di attesa
            waitDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Puoi gestire l'evento di chiusura se necessario
                    // Qui non sto eseguendo alcuna azione per impedire la chiusura manuale
                }
            });

            // Mostra la finestra di attesa
            waitDialog.setVisible(true);
        });
    }

    public void closeWaitWindow(Runnable onClose) {
        SwingUtilities.invokeLater(() -> {
            if (waitDialog != null && waitDialog.isVisible()) {
                waitDialog.dispose();
            }

            // Esegui il Runnable fornito (potrebbe essere onClose)
            if (onClose != null) {
                onClose.run();
            }
        });
    }
}
