import java.awt.*;
import java.awt.geom.Ellipse2D;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * La classe GUIClientSTART rappresenta la finestra principale dell'applicazione
 * client per il gioco Uno.
 * Estende JFrame e gestisce l'interfaccia grafica per avviare il gioco,
 * visualizzare le regole e i crediti.
 */
public class GUIClientSTART extends JFrame {

    /**
     * Costruttore per la classe GUIClientSTART.
     * Crea e configura l'interfaccia grafica dell'applicazione client.
     */
    public GUIClientSTART() {
        super("Uno Client");

        // Configura l'interfaccia grafica
        setupUI();
    }

    /**
     * Configura l'interfaccia grafica dell'applicazione client.
     */
    private void setupUI() {
        // Creazione di un'immagine di sfondo per il pannello
        ImageIcon backgroundImageIcon = new ImageIcon("uno_nobuild\\src\\client\\images\\unocards3.jpg");

        // Creazione del pannello di sfondo con l'immagine
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImageIcon);
        backgroundPanel.setLayout(new GridBagLayout());

        // Creazione dei pulsanti ovali per "Join a Party", "How to Play" e "Credits"
        OvalButton startButton = new OvalButton("JOIN A PARTY");
        OvalButton rulesButton = new OvalButton("HOW TO PLAY");
        OvalButton creditsButton = new OvalButton("CREDITS");

        // Impostazione delle dimensioni e del colore dei pulsanti
        Dimension buttonSize = new Dimension(225, 100);
        Dimension buttonSizeSecond = new Dimension(100, 50);
        startButton.setPreferredSize(buttonSize);
        rulesButton.setPreferredSize(buttonSizeSecond);
        creditsButton.setPreferredSize(buttonSizeSecond);
        startButton.setBackground(Color.RED);
        rulesButton.setBackground(Color.GRAY);
        creditsButton.setBackground(Color.GRAY);

        // Configurazione del layout del pannello
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 20, 10); // Spaziatura tra i bottoni
        backgroundPanel.add(startButton, gbc);

        gbc.gridy++;
        backgroundPanel.add(rulesButton, gbc);

        gbc.gridy++;
        backgroundPanel.add(creditsButton, gbc);

        // Azioni dei pulsanti
        startButton.addActionListener(e -> {
            dispose();
            try {
                new ClientGUI().setVisible(true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        rulesButton.addActionListener(e -> {
            RulesDialog rulesDialog = new RulesDialog(this);
            rulesDialog.setVisible(true);
        });

        creditsButton.addActionListener(e -> {
            CreditsDialog creditsDialog = new CreditsDialog(this);
            creditsDialog.setVisible(true);
        });

        // Configurazione della finestra principale
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(backgroundPanel);
    }
}

/**
 * La classe OvalButton rappresenta un JButton con forma ovale.
 * Estende JButton e personalizza l'aspetto del pulsante.
 */
class OvalButton extends JButton {
    public OvalButton(String label) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Personalizzazione dell'aspetto del pulsante
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(new Ellipse2D.Double(0, 0, getSize().width - 1, getSize().height - 1));
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Disegno del bordo del pulsante
        g.setColor(getForeground());
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Ellipse2D.Double(0, 0, getSize().width - 1, getSize().height - 1));
    }
}

/**
 * La classe BackgroundPanel rappresenta un JPanel con un'immagine di sfondo.
 * Estende JPanel e permette di visualizzare un'immagine di sfondo nel pannello.
 */
class BackgroundPanel extends JPanel {
    private ImageIcon backgroundImageIcon;

    public BackgroundPanel(ImageIcon backgroundImageIcon) {
        this.backgroundImageIcon = backgroundImageIcon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Disegna l'immagine di sfondo nel pannello
        super.paintComponent(g);
        if (backgroundImageIcon != null) {
            Image backgroundImage = backgroundImageIcon.getImage();
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
