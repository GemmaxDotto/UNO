
/*
 *  thread per la ricezione dei messaggi client
 * 
 */
public class threadMsg extends Thread{
    Condivisa cond ;
    public threadMsg(Condivisa cond){
        this.cond = cond;
    }

    @Override
    public void run() {
       //ricezione msg per tutti i client
       while (true) {
            String receString=cond.Game.client.receiveMessage().strip();

            switch (receString.split(";")[0]) {
                //game,first,speciali 
                //msg = "speciale;"+nickGiocatoreSuccessivo+";salta_turno"
            
                case "speciale":
                    
                    break;
            }
       }
                
    }
}
