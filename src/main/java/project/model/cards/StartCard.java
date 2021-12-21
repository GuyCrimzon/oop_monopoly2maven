package project.model.cards;

import project.model.Player;

public class StartCard extends Card {
    private int moneyForCycle;

    public StartCard(int moneyForCycle) {
        this.moneyForCycle = moneyForCycle;
        this.type = CardType.START_CARD;
    }

    @Override
    public int stepOnCard(Player player) {
        onCard.add(player);
        player.addMoney(this.moneyForCycle);
        return this.moneyForCycle;
    }
}
