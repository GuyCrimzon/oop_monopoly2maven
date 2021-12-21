package project.model;

import project.model.cards.CardWithProperty;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private int money;
    private PlayerStatus status = PlayerStatus.NORMAL;
    transient private List<CardWithProperty> cards = new ArrayList<>();

    public Player(int id, int money) {
        this.id = id;
        this.money = money;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public int payMoney(int money) {
        if (this.money < money) {
            int temp = this.money;
            this.money = 0;
            return temp;
        } else {
            this.money -= money;
            return money;
        }
    }

    public void addCard(CardWithProperty card) {
        cards.add(card);
    }

    public void setCards(List<CardWithProperty> cards) {
        this.cards = cards;
    }

    public boolean removeCard(CardWithProperty card) {
        return cards.remove(card);
    }

    public int getMoney() {
        return money;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public List<CardWithProperty> getCards() {
        return cards;
    }

    public int getId() {
        return id;
    }
}
