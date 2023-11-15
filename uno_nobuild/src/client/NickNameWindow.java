import javax.swing.*;

public class NickNameWindow {

    public static String showInputDialog() {
        // Creare un pannello per contenere i componenti
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Aggiungere una etichetta con il testo desiderato
        JLabel label = new JLabel("HI! Game is about to start,choose your nickname!: ");
        panel.add(label);

        // Aggiungere una casella di testo per il nickname
        JTextField textField = new JTextField();
        panel.add(textField);

        // Creare un oggetto array per passare al metodo showOptionDialog
        Object[] options = {"Pronto"};

        // Mostrare la finestra di dialogo modale
        int result = JOptionPane.showOptionDialog(null, panel, "Inserisci il tuo nickname",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        // Se l'utente ha premuto "Pronto", restituisci il testo inserito nella casella di testo
        if (result == JOptionPane.OK_OPTION) {
            return textField.getText();
        } else {
            // Se l'utente ha chiuso la finestra di dialogo o ha premuto Esc, restituisci una stringa vuota
            return "";
        }
    }
}

