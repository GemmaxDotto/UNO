import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe ChooseColorDialog estende JDialog e fornisce un dialogo per la
 * selezione del colore.
 */
public class ChooseColorDialog extends JDialog {
    private String selectedColor;

    /**
     * Costruisce un dialogo per la selezione del colore.
     * 
     * @param parent Frame genitore che ospiterà il dialogo di scelta del colore.
     */
    public ChooseColorDialog(Frame parent) {
        super(parent, "Scegli il colore", true);
        setLayout(new GridLayout(4, 1));

        // Creazione dei bottoni per i colori
        JButton redButton = new JButton("Rosso");
        JButton blueButton = new JButton("Blu");
        JButton greenButton = new JButton("Verde");
        JButton yellowButton = new JButton("Giallo");

        // Aggiunta dei listener per i bottoni dei colori
        redButton.addActionListener(new ColorButtonListener("Rosso"));
        blueButton.addActionListener(new ColorButtonListener("Blu"));
        greenButton.addActionListener(new ColorButtonListener("Verde"));
        yellowButton.addActionListener(new ColorButtonListener("Giallo"));

        // Aggiunta dei bottoni al layout
        add(redButton);
        add(blueButton);
        add(greenButton);
        add(yellowButton);

        pack(); // Adatta le dimensioni del dialogo in base ai suoi componenti
        setLocationRelativeTo(parent); // Imposta la posizione del dialogo al centro del Frame genitore
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Chiude il dialogo quando viene cliccato il pulsante di chiusura
    }

    /**
     * Restituisce il colore selezionato dall'utente.
     * 
     * @return Il colore selezionato.
     */
    public String getSelectedColor() {
        return selectedColor;
    }

    /**
     * Inner class che funge da listener per i bottoni dei colori.
     */
    private class ColorButtonListener implements ActionListener {
        private String color;

        /**
         * Costruisce un listener per il pulsante di un determinato colore.
         * 
         * @param color Il colore associato al listener.
         */
        public ColorButtonListener(String color) {
            this.color = color;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedColor = color; // Imposta il colore selezionato
            dispose(); // Chiudi la finestra
        }
    }
}
