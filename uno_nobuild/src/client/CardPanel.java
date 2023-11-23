import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardPanel extends JPanel {

    private GameManaging game;
    ClientGUI GUI;


    public CardPanel(GameManaging game,ClientGUI GUI) {
        
        this.GUI=GUI;
        this.game=game;

        // Imposta il layout e aggiungi il listener del mouse
        setFocusable(true);  // Assicurati che il pannello sia cliccabile
        setLayout(new FlowLayout());
       // addMouseListener(new CardClickListener());
       // updateCards(); // Aggiorna le carte quando viene creato il pannello
    }

    // Metodo per aggiornare la rappresentazione grafica delle carte
    // Nel metodo updateCards di CardPanel

}