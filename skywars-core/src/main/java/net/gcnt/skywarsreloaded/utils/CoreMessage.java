package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.HashMap;
import java.util.List;

public class CoreMessage implements Message {

    private final SkyWarsReloaded plugin;
    private final List<String> lines;
    private final HashMap<String, String> replacements;
    private boolean colors;

    public CoreMessage(SkyWarsReloaded plugin, boolean colors, String... lines) {
        this.plugin = plugin;
        this.lines = List.of(lines);
        this.colors = colors;
        this.replacements = new HashMap<>();
    }

    public CoreMessage(SkyWarsReloaded plugin, String... lines) {
        this(plugin, true, lines);
    }

    @Override
    public Message colors(boolean colors) {
        this.colors = colors;
        return this;
    }

    @Override
    public Message replace(String search, String replace) {
        this.replacements.put(search, replace);
        return this;
    }

    private String parse(String s) {
        if (colors) s = plugin.getUtils().colorize(s);
        for (String key : replacements.keySet()) {
            s = s.replace(key, replacements.get(key));
        }
        return s;
    }

    @Override
    public void send(SWCommandSender... senders) {
        lines.forEach(s -> {
            s = parse(s);

            for (SWCommandSender sender : senders) {
                sender.sendMessage(s);
            }
        });
    }

    @Override
    public void sendTitle(SWCommandSender... senders) {
        sendTitle(20, 50, 20, senders);
    }

    @Override
    public void sendTitle(int in, int stay, int out, SWCommandSender... senders) {
        if (lines.size() == 0) return;
        String s = parse(lines.get(0));
        String[] split = s.split("\\n");

        String first = split.length == 2 ? split[0] : s;
        String second = split.length == 2 ? split[1] : "";

        for (SWCommandSender sender : senders) {
            if (sender instanceof SWPlayer player) player.sendTitle(first, second, in, stay, out);
        }
    }
}
