package project.services.serialize;

import com.google.gson.*;
import project.model.cards.*;

import java.lang.reflect.Type;

public class CardAdapter implements JsonDeserializer<Card>, JsonSerializer<Card> {
    @Override
    public Card deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonElement element = jsonElement.getAsJsonObject().get("Card");
        JsonObject object = element.getAsJsonObject();
        String cardType = jsonDeserializationContext
                .deserialize(object.getAsJsonPrimitive("type"), String.class);
        return jsonDeserializationContext.deserialize(element, CardType.valueOf(cardType).type);
    }

    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        JsonElement element = null;
        switch (card.getCardType()) {
            case START_CARD:
                StartCard startCard = (StartCard) card;
                element = jsonSerializationContext.serialize(startCard);
                break;
            case CARD_WITH_PROPERTY:
                CardWithProperty cardWithProperty = (CardWithProperty) card;
                element = jsonSerializationContext.serialize(cardWithProperty);
                break;
            case JAIL_CARD:
                JailCard jailCard = (JailCard) card;
                element = jsonSerializationContext.serialize(jailCard);
                break;
            case GO_TO_JAIL_CARD:
                GoToJailCard goToJailCard = (GoToJailCard) card;
                element = jsonSerializationContext.serialize(goToJailCard);
                break;
        }
        object.add("Card", element);
        return object;
    }
}
