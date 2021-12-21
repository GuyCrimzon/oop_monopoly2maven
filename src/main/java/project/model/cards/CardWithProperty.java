package project.model.cards;

import project.model.Player;
import project.model.PlayerStatus;

public class CardWithProperty extends Card {
    private Player owner = null;

    private int price;
    private int housePrice;
    private int[] taxes;
    private int houses = 0;

    public CardWithProperty(int price, int housePrice, int[] taxes) {
        this.price = price;
        this.housePrice = housePrice;
        this.taxes = taxes;
        this.type = CardType.CARD_WITH_PROPERTY;
    }

    public boolean buyHouse() {
        if (houses + 1 < taxes.length) {
            houses += 1;
            return true;
        } else {
            return false;
        }
    }

    public int sellHouse() {
        if (houses > 0) {
            houses -= 1;
            return housePrice;
        } else {
            return 0;
        }
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getPrice() {
        return price;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public int getCurrentTaxes() {
        return taxes[houses];
    }

    public int[] getTaxes() {
        return taxes;
    }

    public int getHouses() {
        return houses;
    }

    @Override
    public int stepOnCard(Player player) {
        onCard.add(player);
        if (this.owner == null) {
            if (player.getMoney() > this.price) {
                this.owner = player;
                player.payMoney(this.price);
                player.getCards().add(this);
                return 1;
            }
        } else {
            if (player == this.owner) {
                if (player.getMoney() > this.housePrice && this.houses + 1 < taxes.length) {
                    this.houses += 1;
                    player.payMoney(this.housePrice);
                    return 2;
                }
            } else {
                int pay = player.payMoney(getCurrentTaxes());
                if (pay < getCurrentTaxes()) {
                    return getCurrentTaxes() - pay;
                }
                this.owner.addMoney(pay);
                return 3;
            }
        }
        return 0;
    }
}
