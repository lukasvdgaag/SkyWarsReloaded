package net.gcnt.skywarsreloaded.data.messaging;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.manager.gameinstance.LocalGameInstanceManager;

public class CoreSWMessageCreator implements SWMessageCreator {

    private final SkyWarsReloaded plugin;

    public CoreSWMessageCreator(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public SWMessage createGameCreationRequest(GameTemplate template, String targetServer) {
        JsonObject json = new JsonObject();
        json.addProperty("template", template.getName());

        SWMessage message = new CoreSWMessage(plugin, MessageChannel.GAME_CREATION_REQUEST, json);
        message.setTargetServer(targetServer);

        return message;
    }

    @Override
    public SWMessage createGameCreationResponse(SWMessage message, GameInstance createdGame) {
        JsonObject json = new JsonObject();
        json.add("created_game", createdGame == null ? null : createdGame.toJSON());

        SWMessage response = new CoreSWMessage(plugin, MessageChannel.GAME_CREATION_RESPONSE, json);
        response.setTargetServer(message.getOriginServer());
        response.setReplyToId(message.getId());

        return response;
    }

    @Override
    public SWMessage createGameDeletionRequest(GameInstance instance, String targetServer) {
        JsonObject json = new JsonObject();
        json.addProperty("game_id", instance.getId().toString());
        json.addProperty("template", instance.getTemplate().getName());

        SWMessage message = new CoreSWMessage(plugin, MessageChannel.GAME_DELETION_REQUEST, json);
        message.setTargetServer(targetServer);

        return message;
    }

    @Override
    public SWMessage createGameUpdatePing(LocalGameInstance game) {
        JsonObject json = createGameInfoJson(game);
        return new CoreSWMessage(plugin, MessageChannel.GAME_UPDATE_PING, json);
    }

    @Override
    public SWMessage createServerUpdatePing() {
        JsonObject json = new JsonObject();

        json.addProperty("players", plugin.getServer().getPlayerCount());
        json.addProperty("game_count", plugin.getGameInstanceManager().getGameInstancesList().size());

        if (plugin.getGameInstanceManager() instanceof LocalGameInstanceManager) {
            JsonArray array = new JsonArray();
            LocalGameInstanceManager manager = (LocalGameInstanceManager) plugin.getGameInstanceManager();

            manager.getGameInstancesList().forEach(game -> array.add(createGameInfoJson(game)));
            json.add("games", array);
        }

        return new CoreSWMessage(plugin, MessageChannel.SERVER_UPDATE_PING, json);
    }

    protected JsonObject createGameInfoJson(LocalGameInstance game) {
        JsonObject json = new JsonObject();

        json.addProperty("game_id", game.getId().toString());
        json.addProperty("template", game.getTemplate().getName());
        json.addProperty("status", game.getState().name());
        json.addProperty("players", game.getPlayerCount());
        json.addProperty("timer", game.getTimer());

        return json;
    }

}
