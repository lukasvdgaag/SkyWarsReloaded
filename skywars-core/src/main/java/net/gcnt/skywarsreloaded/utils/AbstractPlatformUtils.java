package net.gcnt.skywarsreloaded.utils;

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
    public boolean isBoolean(String arg0) {
        try {
            Boolean.parseBoolean(arg0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
