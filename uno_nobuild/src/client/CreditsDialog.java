import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreditsDialog extends JDialog {

    public CreditsDialog(JFrame parent) {
        super(parent, "Crediti", true);

        String creditsText = "Uno Project by Provenzi & Dotto\n" +
                "Progetto TPSIT\n" +
                "Sviluppato con Java e Python\n" +
                "© 2023 Provenzi & Dotto. Tutti i diritti riservati.";

        JTextArea creditsTextArea = new JTextArea(creditsText);
        creditsTextArea.setEditable(false);
        creditsTextArea.setLineWrap(true);
        creditsTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(creditsTextArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiudi la finestra di dialogo quando viene premuto il pulsante "OK"
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }
}