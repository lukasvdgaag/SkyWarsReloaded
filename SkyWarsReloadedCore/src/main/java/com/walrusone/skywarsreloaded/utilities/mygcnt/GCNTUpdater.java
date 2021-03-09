package com.walrusone.skywarsreloaded.utilities.mygcnt;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;

public class GCNTUpdater {

    private static final String currentVersion = SkyWarsReloaded.get().getDescription().getVersion();
    private static final String pluginName = "SkyWarsReloaded";
    private static String lastVersion = "";
    private static String lastOfficial = "";
    private static String updateVersion = "";
    private static String updateURL = "https://my.gaagjescraft.net/plugins/SkyWarsReloaded";

    public String getUpdateURL() {
        return updateURL;
    }

    public boolean checkForUpdate() {
        try {
            URL url = new URL("https://my.gaagjescraft.net/inc/php/pluginChecker.php?plugin=" + pluginName);
            URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] ab = line.trim().split("<br>");
                if (ab[0].startsWith("last_version= ")) {
                    lastVersion = ab[0].replace("last_version= ", "");
                }
                if (ab[1].startsWith("last_official= ")) {
                    lastOfficial = ab[1].replace("last_official= ", "");
                }
                if (ab[2].startsWith("update_url= ")) {
                    updateURL = ab[2].replace("update_url= ", "");
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getUpdateStatus() {
        // returns -1 if error, returns 0 if latest version, returns 1 if update needed
        String last = SkyWarsReloaded.getCfg().isCheckForBetaVersion() ? lastVersion : lastOfficial;

        if (last.equals("")) return -1;

        String current = currentVersion.replaceAll("[^0-9]+", " ");
        last = last.replaceAll("[^0-9]+", " ");

        List<String> currentVersionNums = Arrays.asList(current.trim().split(" "));
        List<String> latestVersionNums = Arrays.asList(last.trim().split(" "));

        for (int i = 0; i < max(currentVersionNums.size(), latestVersionNums.size()); i++) {
            int x = i < currentVersionNums.size() ? Integer.parseInt(currentVersionNums.get(i)) : 0;
            int y = i < latestVersionNums.size() ? Integer.parseInt(latestVersionNums.get(i)) : 0;

            if (x < y) {
                updateVersion = SkyWarsReloaded.getCfg().isCheckForBetaVersion() ? lastVersion : lastOfficial;
                return 1;
            }
        }

        return 0;
    }

    public String getLatestVersion() {
        return updateVersion;
    }

    public String getLatestOfficialVersion() {
        return lastOfficial;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }
}
