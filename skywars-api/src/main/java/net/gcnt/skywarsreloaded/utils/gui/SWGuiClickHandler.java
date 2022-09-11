package net.gcnt.skywarsreloaded.utils.gui;

public interface SWGuiClickHandler {

    ClickResult onClick(SWGui gui, int slot, ClickType clickType, boolean isShift);

    enum ClickType {
        PRIMARY,
        SECONDARY,
        MIDDLE,
    }

    enum ClickResult {
        CANCELLED,
        ALLOWED,
        IGNORED
    }

}
