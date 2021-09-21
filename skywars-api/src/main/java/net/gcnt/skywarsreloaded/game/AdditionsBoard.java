package net.gcnt.skywarsreloaded.game;

import java.util.ArrayList;

public interface AdditionsBoard {

    void setTitle(String title);

    void setLine(int number, String value);

    ArrayList<String> convertIntoPieces(String input, int charsCount);

    int getLineCount();

    String getLine(int number);

}
