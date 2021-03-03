package com.walrusone.skywarsreloaded.managers;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class SkywarsBoard {

    private final Player player;
    private final Objective objective;
    private final int lineCount;
    private final HashMap<Integer, String> cache = new HashMap<>();
    public Scoreboard board;

    public SkywarsBoard(Player player, int lineCount) {
        this.player = player;
        this.lineCount = lineCount;
        this.board = SkyWarsReloaded.get().getServer().getScoreboardManager().getNewScoreboard();
        this.objective = this.board.registerNewObjective("sb1", "sb2");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName("...");

        int score = lineCount;
        for (int i = 0; i < lineCount; i++) {
            Team t = this.board.registerNewTeam(i + "");
            t.addEntry(ChatColor.values()[i] + "");
            this.objective.getScore(ChatColor.values()[i] + "").setScore(score);
            score--;
        }
        this.player.setScoreboard(this.board);
    }

    public void setTitle(String arg0) {
        if (arg0 == null) arg0 = "";

        if (cache.containsKey(-1) && cache.get(-1).equals(arg0)) return;
        cache.remove(-1);
        cache.put(-1, arg0);
        objective.setDisplayName(arg0);
    }

    public void setLine(int arg0, String arg1) {
        Team arg2 = board.getTeam(arg0 + "");
        if (arg1 == null) arg1 = "";

        if (arg1.contains("%") && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            arg1 = PlaceholderAPI.setPlaceholders(player, arg1);
        }

        if (cache.containsKey(arg0) && cache.get(arg0).equals(arg1)) return;
        cache.remove(arg0);
        cache.put(arg0, arg1);

        ArrayList<String> arg3;
        if (SkyWarsReloaded.getNMS().getVersion() > 12) arg3 = convertIntoPieces(arg1, 64);
        else arg3 = convertIntoPieces(arg1, 16);

        arg2.setPrefix(fixIssues(arg3.get(0)));
        arg2.setSuffix(fixIssues(arg3.get(1)));
    }

    private String fixIssues(String arg0) {
        return arg0;
    }

    private ArrayList<String> convertIntoPieces(String arg0, int arg1) {
        ArrayList<String> arg2 = new ArrayList<>();

        if (arg0.length() <= arg1) {
            arg2.add(arg0);
            arg2.add("");
        } else {
            if (!ChatColor.getLastColors(arg0.substring(arg1 - 2, arg1)).equals("")) {
                arg2.add(arg0.substring(0, arg1 - 2));
                arg2.add(arg0.substring(arg1 - 2));
            } else if (!ChatColor.getLastColors(arg0.substring(arg1 - 1, arg1 + 1)).equals("")) {
                arg2.add(arg0.substring(0, arg1 - 1));
                arg2.add(arg0.substring(arg1 - 1));
            } else {
                arg2.add(arg0.substring(0, arg1));
                String arg3 = ChatColor.getLastColors(arg2.get(0));
                arg2.add(arg3 + arg0.substring(arg1));
            }

            if (arg2.get(1).length() > arg1) {
                arg2.set(1, arg2.get(1).substring(0, arg1));
            }
        }

        return arg2;
    }

    public int getLinecount() {
        return lineCount;
    }

    public String getLine(int number) {
        return cache.getOrDefault(number, "");
    }
}
