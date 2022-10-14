package com.walrusone.skywarsreloaded.utilities.mygcnt;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.max;

public class GCNTUpdater {

    private static final String currentVersion = SkyWarsReloaded.get().getDescription().getVersion();
    private static final String pluginName = "SkyWarsReloaded";
    private static final AtomicReference<String> latestVersion = new AtomicReference<>("");
    private static final AtomicReference<String> lastRelease = new AtomicReference<>("");
    private static final AtomicReference<String> updateVersion = new AtomicReference<>("");
    private static final AtomicReference<String> updateURL = new AtomicReference<>("https://my.gcnt.net/plugins/SkyWarsReloaded");

    public String getUpdateURL() {
        return updateURL.get();
    }

    public void checkForUpdate() {
        try {
            URL url = new URL("https://my.gcnt.net/inc/php/pluginChecker.php?plugin=" + pluginName);
            URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] ab = line.trim().split("<br>");
                if (ab[0].startsWith("last_version= ")) {
                    latestVersion.set(ab[0].replace("last_version= ", ""));
                }
                if (ab[1].startsWith("last_official= ")) {
                    lastRelease.set(ab[1].replace("last_official= ", ""));
                }
                if (ab[2].startsWith("update_url= ")) {
                    updateURL.set(ab[2].replace("update_url= ", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Thread safe update checker
     * @return The update status (-1 if the latest version on MyGCNT is empty, 0 if there is no update, 1 if there is an update)
     */
    public int getUpdateStatus() {
        // returns -1 if error, returns 0 if latest version, returns 1 if update needed
        String last = SkyWarsReloaded.getCfg().isCheckForBetaVersion() ? latestVersion.get() : lastRelease.get();

        if (last.equals("")) return -1;

        String current = currentVersion.replaceAll("[^0-9]+", " ");
        last = last.replaceAll("[^0-9]+", " ");

        List<String> currentVersionNums = Arrays.asList(current.trim().split(" "));
        List<String> latestVersionNums = Arrays.asList(last.trim().split(" "));

        for (int i = 0; i < max(currentVersionNums.size(), latestVersionNums.size()); i++) {
            int currSubVer = i < currentVersionNums.size() ? Integer.parseInt(currentVersionNums.get(i)) : 0;
            int latestSubVer = i < latestVersionNums.size() ? Integer.parseInt(latestVersionNums.get(i)) : 0;

            if (currSubVer < latestSubVer) {
                updateVersion.set(SkyWarsReloaded.getCfg().isCheckForBetaVersion() ? latestVersion.get() : lastRelease.get());
                return 1;
            }
        }

        return 0;
    }

    public String getLatestVersion() {
        return updateVersion.get();
    }

    /*public String getLatestOfficialVersion() {
        return lastRelease.get();
    }

    public String getPluginName() {
        return pluginName;
    }*/

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void handleJoiningPlayer(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SkyWarsReloaded.get().getUpdater().getUpdateStatus() == 1 && (player.isOp() || player.hasPermission("sw.admin"))) {
                    BaseComponent base = new TextComponent(
                            "§d§l[SkyWarsReloaded] §aA new update has been found: §b" +
                            SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!"
                    );
                    base.setClickEvent(
                            new ClickEvent(ClickEvent.Action.OPEN_URL, SkyWarsReloaded.get().getUpdater().getUpdateURL())
                    );
                    base.setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new BaseComponent[] {
                                            new TextComponent("§7Click here to update to the latest version!")})
                    );
                    SkyWarsReloaded.getNMS().sendJSON(player,
                            "[\"\",{\"text\":\"§d§l[SkyWarsReloaded] §aA new update has been found: §b" +
                                    SkyWarsReloaded.get().getUpdater().getLatestVersion() +
                                    "§a. Click here to update!\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" +
                                    SkyWarsReloaded.get().getUpdater().getUpdateURL() +
                                    "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§7Click here to update to the latest version!\"}]}}}]"
                    );
                }
            }
        }.runTaskAsynchronously(SkyWarsReloaded.get());
    }
}
