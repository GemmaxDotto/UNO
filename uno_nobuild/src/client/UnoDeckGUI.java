import javax.swing.JFrame;

class UnoDeckGUI extends JFrame {
    private UnoDeck unoDeck;

    public UnoDeckGUI(UnoDeck unoDeck) {
        this.unoDeck = unoDeck;

        // Impostazioni della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("UNO Deck");

        // Pannello per disegnare le carte
        CardPanel cardPanel = new CardPanel();
        add(cardPanel);

        pack();
        setVisible(true);
    }
}