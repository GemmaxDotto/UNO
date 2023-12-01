import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe CreditsDialog estende JDialog per visualizzare i crediti del
 * progetto.
 */
public class CreditsDialog extends JDialog {

    /**
     * Costruisce un dialogo per visualizzare i crediti del progetto.
     * 
     * @param parent JFrame genitore che ospiterà il dialogo dei crediti.
     */
    public CreditsDialog(JFrame parent) {
        super(parent, "Crediti", true);

        // Testo dei crediti del progetto
        String creditsText = "Uno Project by Provenzi & Dotto\n" +
                "Progetto TPSIT\n" +
                "Sviluppato con Java e Python\n" +
                "© 2023 Provenzi & Dotto. Tutti i diritti riservati.";

        // TextArea per visualizzare il testo dei crediti
        JTextArea creditsTextArea = new JTextArea(creditsText);
        creditsTextArea.setEditable(false);
        creditsTextArea.setLineWrap(true);
        creditsTextArea.setWrapStyleWord(true);

        // JScrollPane per aggiungere la barra di scorrimento al testo dei crediti
        JScrollPane scrollPane = new JScrollPane(creditsTextArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        // JButton "OK" per chiudere il dialogo
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiude la finestra di dialogo quando viene premuto il pulsante "OK"
            }
        });

        // JPanel per contenere il pulsante "OK"
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        // Imposta il layout del dialogo e aggiunge i componenti al BorderLayout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER); // Aggiunge il pannello scrollabile al centro
        add(buttonPanel, BorderLayout.SOUTH); // Aggiunge il pannello dei pulsanti nella parte inferiore

        pack(); // Adatta le dimensioni del dialogo in base ai suoi componenti
        setLocationRelativeTo(parent); // Imposta la posizione del dialogo al centro del JFrame genitore
    }

}