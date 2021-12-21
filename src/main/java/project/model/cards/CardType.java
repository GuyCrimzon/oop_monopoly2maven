package project.model.cards;

public enum CardType {
    START_CARD(StartCard.class),
    CARD_WITH_PROPERTY(CardWithProperty.class),
    JAIL_CARD(JailCard.class),
    GO_TO_JAIL_CARD(GoToJailCard.class);

    public Class type;
    CardType(Class type) {
        this.type = type;
    }
}
