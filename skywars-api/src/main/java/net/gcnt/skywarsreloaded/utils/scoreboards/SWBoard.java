package net.gcnt.skywarsreloaded.utils.scoreboards;

import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public interface SWBoard {

    void setup();

    void setTitle(String title);

    void setLine(int line, String text);

    String[] convertIntoPieces(String text);

    int getLineCount();

    String getLine(int line);

    SWPlayer getPlayer();

    void apply();

}
