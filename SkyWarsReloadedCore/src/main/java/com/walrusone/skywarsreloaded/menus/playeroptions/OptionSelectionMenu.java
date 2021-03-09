package com.walrusone.skywarsreloaded.menus.playeroptions;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.enums.PlayerOptions;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OptionSelectionMenu {

    public OptionSelectionMenu(final Player player, PlayerOptions p, boolean commandOpen) {
        List<PlayerOption> availableItems;
        switch (p) {
            case GLASSCOLOR:
                availableItems = GlassColorOption.getPlayerOptions();
                break;
            case PARTICLEEFFECT:
                availableItems = ParticleEffectOption.getPlayerOptions();
                break;
            case PROJECTILEEFFECT:
                availableItems = ProjectileEffectOption.getPlayerOptions();
                break;
            case WINSOUND:
                availableItems = WinSoundOption.getPlayerOptions();
                break;
            case KILLSOUND:
                availableItems = KillSoundOption.getPlayerOptions();
                break;
            case TAUNT:
                availableItems = TauntOption.getPlayerOptions();
                break;
            default:
                availableItems = GlassColorOption.getPlayerOptions();
        }

        String menuName = new Messaging.MessageFormatter().format(availableItems.get(0).getMenuName());
        int menuSize = availableItems.get(0).getMenuSize();

        ArrayList<Inventory> invs = new ArrayList<>();

        int level = Util.get().getPlayerLevel(player);

        for (PlayerOption option : availableItems) {
            int page = option.getPage() - 1;
            int position = option.getPosition();

            if (page < 0) {
                option.setPage(1);
            }

            if (position < 0 || position >= menuSize) {
                position = 1;
            }

            if (invs.isEmpty() || invs.size() < page + 1) {
                while (invs.size() < page + 1) {
                    invs.add(Bukkit.createInventory(null, menuSize + 9, menuName));
                }
            }


            List<String> loreList = Lists.newLinkedList();
            ItemStack item = SkyWarsReloaded.getIM().getItem("nopermission");

            if (level >= option.getLevel() || player.hasPermission(option.getPermission())) {
                if (SkyWarsReloaded.getCfg().economyEnabled()) {
                    if (player.hasPermission(option.getPermission()) || option.getCost() == 0) {
                        if (option instanceof TauntOption) {
                            loreList.addAll(((TauntOption) option).getLore());
                        }
                        loreList.add(new Messaging.MessageFormatter().format(option.getUseLore()));
                        item = option.getItem().clone();
                    } else {
                        loreList.add(new Messaging.MessageFormatter().setVariable("cost", "" + option.getCost()).format("menu.cost"));
                        item = option.getItem().clone();
                    }
                } else {
                    if (option instanceof TauntOption) {
                        for (String lore : ((TauntOption) option).getLore()) {
                            loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                        }
                    }
                    loreList.add(new Messaging.MessageFormatter().format(option.getUseLore()));
                    item = option.getItem().clone();
                }
            } else {
                loreList.add(new Messaging.MessageFormatter().setVariable("level", "" + option.getLevel()).format("menu.no-use"));
            }

            invs.get(page).setItem(position, SkyWarsReloaded.getNMS().getItemStack(item, loreList, ChatColor.translateAlternateColorCodes('&', option.getName())));
        }
        SkyWarsReloaded.getIC().create(player, invs, event -> {
            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWarsReloaded.getNMS().getItemName(SkyWarsReloaded.getIM().getItem("exitMenuItem")))) {
                if (commandOpen) {
                    player.closeInventory();
                    return;
                }
                new OptionsSelectionMenu(player);
                return;
            }
            PlayerOption option;
            switch (p) {
                case GLASSCOLOR:
                    option = GlassColorOption.getPlayerOptionByName(name);
                    break;
                case PARTICLEEFFECT:
                    option = ParticleEffectOption.getPlayerOptionByName(name);
                    break;
                case PROJECTILEEFFECT:
                    option = ProjectileEffectOption.getPlayerOptionByName(name);
                    break;
                case WINSOUND:
                    option = WinSoundOption.getPlayerOptionByName(name);
                    break;
                case KILLSOUND:
                    option = KillSoundOption.getPlayerOptionByName(name);
                    break;
                case TAUNT:
                    option = TauntOption.getPlayerOptionByName(name);
                    break;
                default:
                    option = null;
            }

            if (option == null) {
                return;
            }

            if (SkyWarsReloaded.getCfg().economyEnabled()) {
                if (level < option.getLevel() && !player.hasPermission(option.getPermission())) {
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getErrorSound(), 1, 1);
                    return;
                } else if (level >= option.getLevel() && !player.hasPermission(option.getPermission()) && !VaultUtils.get().canBuy(player, option.getCost())) {
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getErrorSound(), 1, 1);
                    player.sendMessage(new Messaging.MessageFormatter().format("menu.insufficientfunds"));
                    player.closeInventory();
                    return;
                }
            } else {
                if (level < option.getLevel() && !player.hasPermission(option.getPermission())) {
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getErrorSound(), 1, 1);
                    return;
                }
            }

            if (SkyWarsReloaded.getCfg().economyEnabled() && !player.hasPermission(option.getPermission())) {
                boolean result = VaultUtils.get().payCost(player, option.getCost());
                if (!result) {
                    return;
                } else {
                    PlayerStat ps = PlayerStat.getPlayerStats(player);
                    if (ps != null) {
                        ps.addPerm(option.getPermission(), true);
                        player.sendMessage(option.getPurchaseMessage());
                    }
                }
            }

            PlayerStat ps = PlayerStat.getPlayerStats(player);
            option.setEffect(ps);
            DataStorage.get().saveStats(ps);
            Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getConfirmeSelctionSound(), 1, 1);
            player.sendMessage(option.getUseMessage());
            new OptionsSelectionMenu(player);
        });

        if (player != null) {
            SkyWarsReloaded.getIC().show(player, null);
        }
    }
}