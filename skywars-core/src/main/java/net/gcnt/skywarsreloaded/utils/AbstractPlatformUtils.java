package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.utils.centeredText.DefaultFontInfo;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractPlatformUtils implements PlatformUtils {

    @Override
    public boolean isInt(String arg0) {
        try {
            Integer.parseInt(arg0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDouble(String arg0) {
        try {
            Double.parseDouble(arg0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isFloat(String arg0) {
        try {
            Float.parseFloat(arg0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UUID getUUIDFromString(String arg0) {
        try {
            return UUID.fromString(arg0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isBoolean(String arg0) {
        try {
            Boolean.parseBoolean(arg0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String centerMessage(String message) {
        if (message == null || message.trim().isEmpty()) return "";

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '\u00a7') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        int CENTER_PX = 154;
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb + message;
    }

    @Override
    public Item getItemFromMap(Map<String, Object> map) {
        return null;
    }
}
