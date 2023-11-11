public class App {
    public static void main(String[] args) throws Exception {
         TCPClient client=new TCPClient("localhost",12346);
         client.sendMessage("nome;start");
        client.receiveMessage();
    }
}
