package project.model.cards;

import project.model.Player;
import project.model.PlayerStatus;

public class GoToJailCard extends Card {
    public GoToJailCard() {
        this.type = CardType.GO_TO_JAIL_CARD;
    }

    @Override
    public int stepOnCard(Player player) {
        onCard.add(player);
        player.setStatus(PlayerStatus.STOP);
        return 0;
    }
}
