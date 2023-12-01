import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseColorDialog extends JDialog {
    private String selectedColor;

    public ChooseColorDialog(Frame parent) {
        super(parent, "Scegli il colore", true);
        setLayout(new GridLayout(4, 1));

        JButton redButton = new JButton("Rosso");
        JButton blueButton = new JButton("Blu");
        JButton greenButton = new JButton("Verde");
        JButton yellowButton = new JButton("Giallo");

        redButton.addActionListener(new ColorButtonListener("Rosso"));
        blueButton.addActionListener(new ColorButtonListener("Blu"));
        greenButton.addActionListener(new ColorButtonListener("Verde"));
        yellowButton.addActionListener(new ColorButtonListener("Giallo"));

        add(redButton);
        add(blueButton);
        add(greenButton);
        add(yellowButton);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    private class ColorButtonListener implements ActionListener {
        private String color;

        public ColorButtonListener(String color) {
            this.color = color;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedColor = color;
            dispose(); // Chiudi la finestra
        }
    }
}