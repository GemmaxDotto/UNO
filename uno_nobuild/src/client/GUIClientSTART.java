import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GUIClientSTART extends JFrame {

    public GUIClientSTART() {
        super("Uno Client");

        // Configura l'interfaccia grafica
        setupUI();
    }

    private void setupUI() {

        // Carica l'immagine di sfondo (assicurati che il percorso sia corretto)
        ImageIcon backgroundImageIcon = new ImageIcon("uno_nobuild\\src\\docs\\images\\unocards3.jpg");

        // Crea un pannello con l'immagine di sfondo
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImageIcon);
        backgroundPanel.setLayout(new GridBagLayout());
        OvalButton startButton = new OvalButton("JOIN A PARTY");
        OvalButton rulesButton = new OvalButton("HOW TO PLAY");
        OvalButton creditsButton = new OvalButton("CREDITS");

        Dimension buttonSize = new Dimension(225, 100);
        Dimension buttonSizeSecond = new Dimension(100, 50);

        startButton.setPreferredSize(buttonSize);
        rulesButton.setPreferredSize(buttonSizeSecond);
        creditsButton.setPreferredSize(buttonSizeSecond);
        startButton.setBackground(Color.RED);
        rulesButton.setBackground(Color.GRAY);
        creditsButton.setBackground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 20, 10); // Spaziatura tra i bottoni

        backgroundPanel.add(startButton, gbc);

        gbc.gridy++;
        backgroundPanel.add(rulesButton, gbc);

        gbc.gridy++;
        backgroundPanel.add(creditsButton, gbc);

        startButton.addActionListener(e -> {
            // Ciò che vuoi che accada quando il pulsante viene premuto
            dispose();

            try {
                new ClientGUI().setVisible(true);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        rulesButton.addActionListener(e -> {
            // Ciò che vuoi che accada quando il pulsante viene premuto
            System.out.println("Il pulsante RULES è stato premuto!");
            // Puoi aggiungere qui il codice per avviare un'altra finestra o eseguire altre
            // azioni.
        });

        creditsButton.addActionListener(e -> {
            // Ciò che vuoi che accada quando il pulsante viene premuto
            System.out.println("Il pulsante CREDITS è stato premuto!");
            // Puoi aggiungere qui il codice per avviare un'altra finestra o eseguire altre
            // azioni.
        });

        // Imposta la grandezza della finestra
        setSize(1200, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra
        add(backgroundPanel);
    }

}

class OvalButton extends JButton {
    public OvalButton(String label) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
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
        g.setColor(getForeground());
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Ellipse2D.Double(0, 0, getSize().width - 1, getSize().height - 1));
    }
}

class BackgroundPanel extends JPanel {
    private ImageIcon backgroundImageIcon;

    public BackgroundPanel(ImageIcon backgroundImageIcon) {
        this.backgroundImageIcon = backgroundImageIcon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImageIcon != null) {
            Image backgroundImage = backgroundImageIcon.getImage();
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
