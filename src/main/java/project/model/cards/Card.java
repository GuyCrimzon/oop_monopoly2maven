package project.model.cards;

import project.model.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Card {
    protected List<Player> onCard = new ArrayList<>();
    protected CardType type;

    public List<Player> getPlayersOnCard() {
        return onCard;
    }

    public void setPlayersOnCard(List<Player> onCard) {
        this.onCard = onCard;
    }

    public int stepOnCard(Player player) {
        onCard.add(player);
        return 0;
    }

    public void stepOfCard(Player player) {
        onCard.remove(player);
    }

    public CardType getCardType() {
        return type;
    }


}
