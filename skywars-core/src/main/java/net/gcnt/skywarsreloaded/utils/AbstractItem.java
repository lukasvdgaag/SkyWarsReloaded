package net.gcnt.skywarsreloaded.utils;

public abstract class AbstractItem implements Item {

    @Override
    public Item withMessages(Item item) {
        if (item == null) return this;

        if (item.getDisplayName() != null) {
            setDisplayName(item.getDisplayName());
        }
        if (item.getLore() != null) {
            setLore(item.getLore());
        }
        return this;
    }
}
