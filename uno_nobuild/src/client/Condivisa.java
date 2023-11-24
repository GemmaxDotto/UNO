public class Condivisa {
    public static GameManaging Game;
    public static UnoCard tempCard;

    
    public Condivisa(){
    }

    public void createGameManaging(Condivisa cond){
        this.Game= new GameManaging(cond);
    }

}
