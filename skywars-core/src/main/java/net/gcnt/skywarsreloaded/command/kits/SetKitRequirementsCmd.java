package net.gcnt.skywarsreloaded.command.kits;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.data.player.PlayerStat;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.game.kits.Unlockable;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetKitRequirementsCmd extends Cmd {

    public SetKitRequirementsCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "requirements", "skywars.command.kit.requirements", false,
                "<kit> <requirement> <value>", "Manage the kit's requirements.", "rq");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        final StringBuilder statOptions = new StringBuilder();
        for (int i = 0; i < PlayerStat.values().length; i++) {
            statOptions.append("&7").append(PlayerStat.values()[i].getProperty());
            if (i != PlayerStat.values().length - 1) {
                statOptions.append("&f, ");
            }
        }
        final String statOptionVal = statOptions.toString();

        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_NAME.toString()).send(sender);
            return true;
        } else if (args.length == 1) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_REQUIREMENT.toString()).send(sender);
            return true;
        } else if (args.length == 2) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_REQUIREMENT_VALUE.toString()).send(sender);
            return true;
        }

        String requirement = args[1].toLowerCase();
        String value;
        boolean checkInt = false;
        switch (requirement) {
            case "cost":
                // value entered, checking if it's an int.
                value = args[2];
                checkInt = true;
                break;
            case "permission":
                value = args[2].toLowerCase();
                if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_REQUIREMENT_VALUE_BOOLEAN.toString()).send(sender);
                    return true;
                }
                break;
            case "stats":
                if (args.length == 3) {
                    plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_REQUIREMENT_VALUE.toString()).send(sender);
                    return true;
                } else {
                    requirement = args[2];
                    value = args[3];
                    checkInt = true;

                    PlayerStat stat = PlayerStat.fromString(requirement);
                    if (stat == null) {
                        plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_STAT_REQUIREMENT.toString())
                                .replace("%options%", statOptionVal)
                                .send(sender);
                        return true;
                    }
                }
                break;
            default:
                plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_REQUIREMENT.toString()).send(sender);
                return true;
        }

        if (checkInt) {
            if (!plugin.getUtils().isInt(value)) {
                plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_REQUIREMENT_VALUE_NUMBER.toString()).send(sender);
                return true;
            }
            int index = Integer.parseInt(value);
            if (index < 0) {
                plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_KIT_REQUIREMENT_VALUE_GREATER.toString()).send(sender);
                return true;
            }
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            plugin.getMessages().getMessage(MessageProperties.KITS_DOESNT_EXIST.toString()).replace("%kit%", kitName).send(sender);
            return true;
        }

        Unlockable unlockable = (Unlockable) kit;

        switch (requirement) {
            case "cost":
                unlockable.setCost(Integer.parseInt(value));
                break;
            case "permission":
                unlockable.setRequirePermission(Boolean.parseBoolean(value));
                break;
            default:
                unlockable.addMinimumStat(PlayerStat.fromString(requirement), Integer.parseInt(value));
                break;
        }

        kit.saveData();
        plugin.getMessages().getMessage(MessageProperties.KITS_SET_REQUIREMENT.toString())
                .replace("%kit%", kitName)
                .replace("%requirement%", requirement)
                .replace("%value%", value)
                .send(sender);
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> kits = new ArrayList<>();
            plugin.getKitManager().getKits().forEach(kit -> kits.add(kit.getId()));
            return kits;
        } else if (args.length == 2) {
            return Lists.newArrayList("permission", "cost", "stats");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("permission")) {
                return Lists.newArrayList("true", "false");
            } else if (args[1].equalsIgnoreCase("stats")) {
                List<String> options = new ArrayList<>();
                for (PlayerStat stat : PlayerStat.values()) {
                    options.add(stat.getProperty());
                }
                return options;
            }
        }
        return new ArrayList<>();
    }

}
