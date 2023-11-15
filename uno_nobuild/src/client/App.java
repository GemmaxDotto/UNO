import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIClientSTART().setVisible(true);
            }
        });
        //ClientGUI unoClient = new ClientGUI();
         //while(!unoClient.client.receiveMessage().equals("end"))
         //client.sendMessage("nome;start");
        //client.receiveMessage();
        
    }
}
