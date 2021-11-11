package net.gcnt.skywarsreloaded.utils;

public abstract class AbstractUtilities implements Utilities {

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
    public boolean isBoolean(String arg0) {
        try {
            Boolean.parseBoolean(arg0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
