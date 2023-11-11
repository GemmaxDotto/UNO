public class App {
    public static void main(String[] args) throws Exception {
         TCPClient client=new TCPClient("localhost",666);
         client.sendMessage("nome;start");
    }
}
