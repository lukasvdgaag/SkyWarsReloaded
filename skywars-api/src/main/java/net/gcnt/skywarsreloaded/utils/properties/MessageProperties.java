package net.gcnt.skywarsreloaded.utils.properties;

public enum MessageProperties {

    CHAT_HEADER("chat.header");

    private final String value;

    MessageProperties(String valueIn) {
        this.value = valueIn;
    }

    public String toString() {
        return value;
    }
}
