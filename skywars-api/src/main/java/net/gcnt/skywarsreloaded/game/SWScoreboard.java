package net.gcnt.skywarsreloaded.game;

import java.util.ArrayList;

public interface SWScoreboard {

    void setTitle(String title);

    void setLine(int number, String value);

    String[] convertIntoPieces(String input);

    int getLineCount();

    String getLine(int number);

}
