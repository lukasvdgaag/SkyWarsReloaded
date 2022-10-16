package net.gcnt.skywarsreloaded.data.messaging;

import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import org.jetbrains.annotations.Nullable;

public interface SWMessageCreator {

    SWMessage createGameCreationRequest(GameTemplate template, String targetServer);

    SWMessage createGameCreationResponse(SWMessage message, @Nullable GameInstance createdGame);

    SWMessage createGameDeletionRequest(GameInstance instance, String targetServer);

    SWMessage createGameUpdatePing(LocalGameInstance game);

    SWMessage createServerUpdatePing();

    enum MessageChannel {
        GAME_CREATION_REQUEST("creategame"),
        GAME_CREATION_RESPONSE("gamecreated"),
        GAME_DELETION_REQUEST("deletegame"),
        GAME_UPDATE_PING("gameupdate"),
        SERVER_UPDATE_PING("serverupdate");

        private final String id;

        MessageChannel(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }

        public String getId() {
            return id;
        }
    }

}
