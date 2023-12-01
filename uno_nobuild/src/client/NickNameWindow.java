import javax.swing.*;

/**
 * La classe NickNameWindow fornisce un metodo statico per visualizzare una
 * finestra di dialogo
 * per l'inserimento del nickname del giocatore.
 */
public class NickNameWindow {

    /**
     * Metodo statico per visualizzare una finestra di dialogo modale per
     * l'inserimento del nickname del giocatore.
     * 
     * @return Il nickname inserito dall'utente o una stringa vuota se l'utente ha
     *         chiuso la finestra di dialogo.
     */
    public static String showInputDialog() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("HI! Game is about to start, choose your nickname!: ");
        panel.add(label);

        JTextField textField = new JTextField();
        panel.add(textField);

        // Creare un array di oggetti per passare al metodo showOptionDialog
        Object[] options = { "Pronto" };

        // Mostrare la finestra di dialogo modale
        int result = JOptionPane.showOptionDialog(null, panel, "Inserisci il tuo nickname",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        // Se l'utente ha premuto "Pronto", restituisci il testo inserito nella casella
        // di testo
        if (result == JOptionPane.OK_OPTION) {
            return textField.getText();
        } else {
            // Se l'utente ha chiuso la finestra di dialogo o ha premuto Esc, restituisci
            // una stringa vuota
            return "";
        }
    }
}
