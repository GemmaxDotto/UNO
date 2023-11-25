public class Condivisa {
    public GameManaging Game;
    public UnoCard tempCard;

    
    public Condivisa(ClientGUI GUI){
        this.Game= new GameManaging(GUI,this);
        System.out.println("Game");
    }

    public void startGameManaging(){
        this.Game.startGame();
    }

}
