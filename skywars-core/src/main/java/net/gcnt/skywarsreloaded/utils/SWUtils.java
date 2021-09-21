package net.gcnt.skywarsreloaded.utils;

public final class SWUtils {

    public static boolean isInt(String arg0) {
        try {
            Integer.parseInt(arg0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
