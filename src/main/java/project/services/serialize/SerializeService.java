package project.services.serialize;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import project.model.Game;
import project.model.Player;
import project.model.cards.Card;
import project.model.cards.CardType;
import project.model.cards.CardWithProperty;
import project.services.GameService;

public class SerializeService {
    private Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Card.class, new CardAdapter())
            .setPrettyPrinting()
            .create();

    public void serialize(Game game) {
        try {
            FileWriter fw = new FileWriter("json/serialized.json");
            gson.toJson(game, fw);
            fw.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public Game deserialize() {
        Game game = null;
        try {
            FileReader fr = new FileReader("json/serialized.json");
            game = gson.fromJson(fr, new TypeToken<Game>(){}.getType());
        } catch (IOException e) {
//            e.printStackTrace();
        }
        setCardForPlayers(game);
        return game;
    }

    private void setCardForPlayers(Game game) {
        List<Card> cards = game.getCards();
        List<Player> players = game.getPlayers();
        for (Player p : players) {
            p.setCards(new ArrayList<>());
        }
        for (Card card : cards) {
            if (card.getPlayersOnCard().size() > 0) {
                List<Player> playersOnCard = new ArrayList<>();
                for (Player p : card.getPlayersOnCard()) {
                    playersOnCard.add(players.get(p.getId()));
                }
                card.setPlayersOnCard(playersOnCard);
            }
            if (card.getCardType() == CardType.CARD_WITH_PROPERTY) {
                CardWithProperty c = (CardWithProperty) card;
                if (c.getOwner() != null) {
                    int id = c.getOwner().getId();
                    players.get(id).addCard(c);
                    c.setOwner(players.get(id));
                }
            }
        }
    }
}