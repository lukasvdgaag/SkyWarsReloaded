package net.gcnt.skywarsreloaded.bukkit.game;

import me.clip.placeholderapi.PlaceholderAPI;
import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.game.AbstractSWScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class BukkitSWScoreboard extends AbstractSWScoreboard {

    private final BukkitSWPlayer player;
    private final Objective objective;
    private final int linecount;
    private final HashMap<Integer, String> cache = new HashMap<>();
    public Scoreboard board;

    public BukkitSWScoreboard(BukkitSWPlayer player, int linecount) {
        this.player = player;
        this.linecount = linecount;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.board.registerNewObjective("sb1", "sb2");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName("...");

        int score = linecount;
        for (int i = 0; i < linecount; i++) { // looping through the lines
            Team t = this.board.registerNewTeam("plus-" + i); // creating the team
            t.addEntry(ChatColor.values()[i] + ""); // assigning a color to the team
            this.objective.getScore(ChatColor.values()[i] + "").setScore(score); // sets the score number
            score--;
        }
        this.player.getPlayer().setScoreboard(this.board); // sets the player scoreboard
    }

    @Override
    public void setTitle(String arg0) {
        if (arg0 == null) arg0 = ""; // title null, making it empty

        if (cache.containsKey(-1) && cache.get(-1).equals(arg0)) return; // if title is in cache, return
        cache.remove(-1); // removing the title from the cache
        cache.put(-1, arg0); // changing the title in the cache
        objective.setDisplayName(arg0); // sets the title of the scoreboard
    }

    @Override
    public void setLine(int arg0, String arg1) {
        Team arg2 = board.getTeam("plus-" + arg0 + ""); // Get the team we need
        if (arg1 == null) arg1 = ""; // Line null, making it empty

        if (arg1.contains("%")) {
            //arg1 = Utils.get().parseStatics(arg1, player);
            if (arg1.contains("%") && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
                arg1 = PlaceholderAPI.setPlaceholders(player.getPlayer(), arg1); // check if still contains a placeholder
        }
        if (arg1.contains("{") && arg1.contains("}") && Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            arg1 = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player.getPlayer(), arg1);
        }

        if (cache.containsKey(arg0) && cache.get(arg0).equals(arg1)) return; // Line hasn't changed
        cache.remove(arg0); // remove the old line
        cache.put(arg0, arg1); // add the new line

        ArrayList<String> arg3;
        // todo check if new version (1.13+)
        if (true) arg3 = convertIntoPieces(arg1, 64);
        else arg3 = convertIntoPieces(arg1, 16);

        arg2.setPrefix((arg3.get(0)));
        arg2.setSuffix((arg3.get(1)));
    }

    @Override
    public ArrayList<String> convertIntoPieces(String arg0, int arg1) {
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

    @Override
    public int getLineCount() {
        return linecount;
    }

    @Override
    public String getLine(int number) {
        return cache.getOrDefault(number, "");
    }


}
