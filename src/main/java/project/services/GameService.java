package project.services;

import project.model.Game;
import project.model.Player;
import project.model.PlayerStatus;
import project.model.cards.Card;
import project.model.cards.CardType;
import project.model.cards.CardWithProperty;

import java.util.List;

public class GameService {
    public String nextTurn(Game g) {
        StringBuilder sb = new StringBuilder();
        int currentPlayer = g.getCurrentPlayerId();
        Player p = g.getPlayers().get(currentPlayer);
        sb.append("Player ").append(currentPlayer).append(" turn:\n");
        if (p.getStatus() == PlayerStatus.STOP) {
            p.setStatus(PlayerStatus.NORMAL);
            sb.append("Skips a turn\n");
        } else {
            int diceRoll = Randomizer.randomInt(1, 6, 1);
            sb.append("Dices: ").append(diceRoll).append("\n");
            List<Card> cards = g.getCards();
            int cardIndex = 0;
            for (Card card : cards) {
                if (card.getPlayersOnCard().contains(p)) {
                    card.stepOfCard(p);
                    break;
                }
                cardIndex++;
            }
            int nextCardIndex;
            if (cardIndex + diceRoll < cards.size()) {
                nextCardIndex = cardIndex + diceRoll;
            } else {
                sb.append("New cycle! +").append(g.getMoneyForCycle()).append("\n");
                nextCardIndex = cardIndex + diceRoll - cards.size();
            }
            Card nextCard = cards.get(nextCardIndex);
            sb.append("Step on Card ").append(nextCardIndex).append(":\n").append(printCard(g, nextCard));
            int status = nextCard.stepOnCard(p);
            switch (nextCard.getCardType()) {
                case CARD_WITH_PROPERTY:
                    switch (status) {
                        case 0:
                            sb.append("Can't pay for card/house\n");
                            break;
                        case 1:
                            sb.append("Buy this card\n");
                            break;
                        case 2:
                            sb.append("Buy house\n");
                            break;
                        case 3:
                            sb.append("Pay taxes to Player ").append(((CardWithProperty) nextCard)
                                    .getOwner().getId()).append("\n");
                            break;
                        default:
                            sb.append("Can't pay taxes!\n");
                            int d = status;
                            int i = 0;
                            for (Card card : p.getCards()) {
                                int m;
                                while ((m = ((CardWithProperty) card).sellHouse()) > 0) {
                                    d -= m;
                                    sb.append("Sell house for ").append(m).append("\n");
                                    if (d <= 0) {
                                        break;
                                    }
                                }
                                if (d <= 0) {
                                    break;
                                } else {
                                    d -= ((CardWithProperty) card).getPrice();
                                    ((CardWithProperty) card).setOwner(null);
                                    sb.append("Sell card for ").append(((CardWithProperty) card)
                                            .getPrice()).append("\n");
                                    i++;
                                    if (d <= 0) {
                                        break;
                                    }
                                }
                            }
                            if (d <= 0) {
                                p.addMoney(-d);
                                ((CardWithProperty) nextCard).getOwner().addMoney(status);
                                sb.append("Pay taxes to Player ").append(((CardWithProperty) nextCard)
                                        .getOwner().getId()).append("\n");
                            } else {
                                ((CardWithProperty) nextCard).getOwner().addMoney(status - d);
                                nextCard.getPlayersOnCard().remove(p);
                                sb.append("Pay all that have to Player ").append(((CardWithProperty) nextCard)
                                        .getOwner().getId()).append(" and Lost\n");
                                p.setStatus(PlayerStatus.LOST);

                            }
                            if (i > 0) {
                                p.getCards().subList(0, i).clear();
                            }
                            break;
                    }
                    break;
                case GO_TO_JAIL_CARD:
                    sb.append("Go to jail!\n");
                    nextCard.stepOfCard(p);
                    for (Card card : g.getCards()) {
                        if (card.getCardType() == CardType.JAIL_CARD) {
                            card.stepOnCard(p);
                        }
                    }
                    break;
            }
        }
        Player winner = checkWin(g);
        if (winner != null) {
            sb.append("Player ").append(winner.getId()).append(" win!");
        } else {
            g.endTurn();
        }
        return sb.toString();
    }

    public Player checkWin(Game g) {
        Player winner = null;
        for (Player p : g.getPlayers()) {
            if (p.getStatus() != PlayerStatus.LOST) {
                if (winner == null) {
                    winner = p;
                } else {
                    winner = null;
                    break;
                }
            }
        }
        return winner;
    }


    public String printField(Game g) {
        List<Card> cards = g.getCards();
        List<Player> players = g.getPlayers();
        return ("Field:\n" + printCards(g, cards) +
                "Players:\n" + printPlayers(players));
    }

    private StringBuilder printPlayers(List<Player> players) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Player player : players) {
            sb.append("Player ").append(index++).append(":\n").append(printPlayer(player));
        }
        return sb;
    }

    private StringBuilder printPlayer(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append(player.getStatus()).append("\n");
        if (player.getStatus() != PlayerStatus.LOST) {
            sb.append("Balance: ").append(player.getMoney()).append("\n");
            sb.append("Own cards: ").append(player.getCards().size()).append("\n");
        }
        return sb;
    }

    private StringBuilder printCards(Game g, List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Card card : cards) {
            sb.append("Card ").append(index++).append(":\n").append(printCard(g, card)).append("\n");
        }
        return sb;
    }

    private StringBuilder printCard(Game g, Card card) {
        StringBuilder sb = new StringBuilder();
        sb.append(card.getCardType()).append("\n");
        switch (card.getCardType()) {
            case START_CARD:
                sb.append("Money for cycle: ").append(g.getMoneyForCycle()).append("\n");
                break;
            case CARD_WITH_PROPERTY:
                CardWithProperty c = (CardWithProperty) card;
                if (c.getOwner() != null) {
                    sb.append("Owner: Player ").append(g.getPlayers().indexOf(c.getOwner()));
                } else {
                    sb.append("No owner");
                }
                sb.append("\n");
                sb.append("Price: ").append(c.getPrice()).append("\n");
                sb.append("House price: ").append(c.getHousePrice()).append("\n");
                sb.append("Houses: ").append(c.getHouses()).append("\n");
                int house = 0;
                for (int taxes : c.getTaxes()) {
                    sb.append("With houses: ").append(house++).append("\tTaxes: ").append(taxes).append("\n");
                }
                break;
        }
        if (card.getPlayersOnCard().size() > 0) {
            for (Player player : card.getPlayersOnCard()) {
                sb.append("Player ").append(g.getPlayers().indexOf(player)).append(" on card\n");
            }
        }
        return sb;
    }
}
