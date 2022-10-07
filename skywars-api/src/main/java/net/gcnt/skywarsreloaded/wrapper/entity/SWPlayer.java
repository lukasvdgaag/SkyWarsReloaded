package net.gcnt.skywarsreloaded.wrapper.entity;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerData;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.party.SWParty;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;
import org.jetbrains.annotations.Nullable;

/**
 * General data about a player that is independent of any running state of games or teams
 */
public interface SWPlayer extends SWCommandSender, SWOfflinePlayer, SWEntity {

    SWPlayerData getPlayerData();

    void setPlayerData(SWPlayerData playerData);

    GameInstance getGameWorld();

    void setGameWorld(GameInstance world);

    Item getItemInHand(boolean offHand);

    SWInventory getInventory();

    void setSlot(int slot, Item item);

    Item getSlot(int slot);

    Item getHelmet();

    void setHelmet(Item helmet);

    Item getChestplate();

    void setChestplate(Item chestplate);

    Item getLeggings();

    void setLeggings(Item leggings);

    Item getBoots();

    void setBoots(Item boots);

    void clearInventory();

    void setExp(int level, float exp);

    void playSound(SWCoord coord, String sound, float volume, float pitch);

    void sendTitle(String title, String subtitle);

    void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

    void setFlying(boolean flying);

    boolean isFlying();

    void setAllowFlight(boolean allowFlight);

    boolean isAllowFlight();

    void setFoodLevel(int foodLevel);

    int getFoodLevel();

    void clearBodyArrows();

    /**
     * Change the game mode of the player.
     *
     * @param gamemode <p>
     *                 0 - survival
     *                 1 - creative
     *                 2 - adventure
     *                 3 - spectate
     *                 </p>
     */
    void setGameMode(int gamemode);

    /**
     * Get the current party of the player
     *
     * @return The player's party
     */
    @Nullable
    SWParty getParty();

    /**
     * Set the player's party
     *
     * @param party The party to assign the player to
     */
    void setParty(@Nullable SWParty party);

    /**
     * Freeze the player in place until unfrozen.
     * This will allow the player to fly to avoid being kicked, but will restrict all movement.
     */
    void freeze();

    /**
     * Unfreeze the player.
     */
    void unfreeze();

    /**
     * Get if the player is frozen
     *
     * @return true if the player is frozen
     */
    boolean isFrozen();

    /**
     * Update the internal parent player object. This uses the UUID stored in the {@link SWPlayer} object to
     * find the parent player from the platform.
     */
    void fetchParentPlayer();

    void openInventory(SWInventory inventory);

    void closeInventory();

    SkyWarsReloaded getPlugin();

}
