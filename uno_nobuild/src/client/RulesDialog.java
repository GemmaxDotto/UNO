
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RulesDialog extends JDialog {

    public RulesDialog(JFrame parent) {
        super(parent, "Regole di Uno", true);

        // Testo delle regole di Uno
        String rulesText = "Le regole di Uno sono:\n" +
                "- Il gioco inizia con ogni giocatore che riceve 7 carte.\n" +
                "- I giocatori si alternano nel senso orario, giocando una carta che abbia lo stesso colore o numero della carta in cima alla pila di scarti.\n" +
                "- Se un giocatore non può giocare una carta, deve pescare una carta dal mazzo.\n" +
                "- Le carte speciali come Salta, Inverti , Pesca Due , Pesca Quattro , Cambia Colore , introducono il SALTA TURNO, INVERTI SENSO DEL GIRO,PESCA DUE CARTE, PESCA QUATTRO CARTE E CAMBIA COLORE.\n" +
                "- Il gioco termina quando un giocatore ha esaurito le carte.\n";

                JTextArea rulesTextArea = new JTextArea(rulesText);
                rulesTextArea.setEditable(false);
                rulesTextArea.setLineWrap(true);
                rulesTextArea.setWrapStyleWord(true);
        
                JScrollPane scrollPane = new JScrollPane(rulesTextArea);
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