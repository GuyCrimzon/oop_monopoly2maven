package project.model;

import project.model.cards.*;
import project.services.Randomizer;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int SIDES = 4;
    private static final int CARDS_WITH_PROPERTY_NUM_BY_SIDE = 3;

    private static final int PLAYERS_NUM = 2;
    private static final int START_MONEY = 0;
    private static final int MONEY_FOR_CYCLE = 300;
    private static final int MAX_HOUSES = 2;

    private List<Player> players = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private int currentPlayerId;

    public Game() {
        newGame(PLAYERS_NUM);
    }

    public void newGame(int playersNum) {
        for (int i = 0; i < playersNum; i++) {
            players.add(new Player(i, START_MONEY));
        }
        for (int i = 0; i < SIDES; i++) {
            switch (i) {
                case 0:
                    cards.add(new StartCard(MONEY_FOR_CYCLE));
                    for (Player player : players) {
                        cards.get(0).stepOnCard(player);
                    }
                    break;
                case 1:
                case 3:
                    cards.add(new GoToJailCard());
                    break;
                case 2:
                    cards.add(new JailCard());
                    break;
            }
            for (int j = 0; j < CARDS_WITH_PROPERTY_NUM_BY_SIDE; j++) {
                int price = Randomizer.randomInt(100, 200, 10);
                int housePrice = Randomizer.randomInt(30, 80, 5);
                int[] taxes = new int[MAX_HOUSES + 1];
                for (int l = 0; l < taxes.length; l++) {
                    taxes[l] = housePrice * (l + 1);
                    if (l == taxes.length - 1) {
                        taxes[l] *= 2;
                    }
                }
                cards.add(new CardWithProperty(price, housePrice, taxes));
            }
        }
        this.currentPlayerId = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getStartMoney() {
        return START_MONEY;
    }

    public int getMoneyForCycle() {
        return MONEY_FOR_CYCLE;
    }

    public int getMaxHouses() {
        return MAX_HOUSES;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void endTurn() {
        currentPlayerId += 1;
        if (currentPlayerId >= PLAYERS_NUM) {
            currentPlayerId = 0;
        }
        if (players.get(currentPlayerId).getStatus() == PlayerStatus.LOST) {
            endTurn();
        }
    }
}
