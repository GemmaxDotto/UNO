
import java.util.ArrayList;
import java.util.List;

class UnoDeck {
    private List<UnoCard> cards;

    public UnoDeck() {
        cards = new ArrayList<>();
        
        // Aggiungi tutte le carte al mazzo (questo è un esempio, aggiungi tutte le carte necessarie)
        /* cards.add(new UnoCard("Red", "1"));
        cards.add(new UnoCard("Green", "2")); */
        // Aggiungi altre carte...

        // Puoi mescolare il mazzo se necessario
        // Collections.shuffle(cards);
    }

    public List<UnoCard> getCards() {
        return cards;
    }

    public void addCards(UnoCard card)
    {
        cards.add(card);
    }
}