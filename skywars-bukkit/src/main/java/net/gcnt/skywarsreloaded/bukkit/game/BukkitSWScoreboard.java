package net.gcnt.skywarsreloaded.bukkit.game;

import me.clip.placeholderapi.PlaceholderAPI;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.utils.scoreboards.AbstractSWBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class BukkitSWScoreboard extends AbstractSWBoard {

    private Scoreboard board;
    private Objective objective;

    private final HashMap<Integer, String> cache = new HashMap<>();

    public BukkitSWScoreboard(SkyWarsReloaded plugin, BukkitSWPlayer player, int lineCount) {
        super(plugin, player, lineCount);
        setup();
    }

    @Override
    public void setup() {
        this.board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        this.objective = this.board.registerNewObjective("sb1", "sb2");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName("...");

        int score = lineCount;
        for (int i = 0; i < lineCount; i++) { // looping through the lines
            Team t = this.board.registerNewTeam("swr-" + i); // creating the team
            t.addEntry(ChatColor.values()[i] + ""); // assigning a color to the team
            this.objective.getScore(ChatColor.values()[i] + "").setScore(score); // sets the score number
            score--;
        }
        apply();
    }

    @Override
    public void apply() {
        ((BukkitSWPlayer) player).getPlayer().setScoreboard(this.board);
    }

    public void setTitle(String arg0) {
        if (arg0 == null) arg0 = ""; // title null, making it empty

        if (cache.containsKey(-1) && cache.get(-1).equals(arg0)) return; // if title is in cache, return
        cache.put(-1, arg0); // changing the title in the cache
        objective.setDisplayName(arg0); // sets the title of the scoreboard
    }

    public void setLine(int arg0, String arg1) {
        Team arg2 = board.getTeam("swr-" + arg0 + ""); // Get the team we need
        if (arg1 == null) arg1 = ""; // Line null, making it empty

        if (arg1.contains("%")) {
            if (arg1.contains("%") && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
                arg1 = PlaceholderAPI.setPlaceholders(((BukkitSWPlayer) player).getPlayer(), arg1); // check if still contains a placeholder
        }

        if (cache.containsKey(arg0) && cache.get(arg0).equals(arg1)) return; // Line hasn't changed
        cache.remove(arg0); // remove the old line
        cache.put(arg0, arg1); // add the new line

        String[] arg3 = convertIntoPieces(arg1);

        arg2.setPrefix(arg3[0]);
        arg2.setSuffix(arg3[1]);
    }

    @Override
    public String[] convertIntoPieces(String string) {
        if (string == null || string.trim().isEmpty()) return new String[]{" ", " "};

        // check if it's a legacy version < 12 for splitting.
        if (plugin.getUtils().getServerVersion() >= 13) {
            return new String[]{string, ""};
        }

        int prefixLength = 16;
        int suffixLength = 16;
        StringBuilder prefix = new StringBuilder(string.substring(0, Math.min(string.length(), prefixLength)));
        StringBuilder suffix = new StringBuilder(string.length() > prefixLength ? string.substring(prefixLength) : "");

        if (prefix.charAt(prefixLength - 1) == '§') {
            // if the string was cut off in the middle of a color code, remove it from the prefix and append to the start of the suffix.
            suffix = suffix.insert(0, '§');
            prefix.deleteCharAt(prefix.length() - 1);
        }
        if (suffix.length() > 0) {
            // if prefix-suffix was cut off in the middle of a color code, copy the last colors from the prefix into the suffix.
            boolean applyColor = true;
            if (suffix.charAt(0) == '§' && suffix.length() >= 2) {
                ChatColor chatColor = ChatColor.getByChar(suffix.charAt(1));
                applyColor = chatColor == null || chatColor.isFormat();
            }
            if (applyColor) {
                String colors = ChatColor.getLastColors(prefix.toString());
                suffix.insert(0, colors);
            }
        }

        if (suffix.length() >= suffixLength) {
            // cut off string if it exceeds the max lengths.
            suffix = new StringBuilder(suffix.substring(0, suffixLength));
            if (suffix.charAt(suffix.length() - 1) == '§') {
                suffix.deleteCharAt(suffix.length() - 1);
            }
        }

        return new String[]{prefix.toString(), suffix.toString()};
    }

}
