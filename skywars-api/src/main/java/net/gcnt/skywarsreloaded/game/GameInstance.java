package net.gcnt.skywarsreloaded.game;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GameInstance {

    String getId();

    GameTemplate getTemplate();

    List<GameTeam> getTeams();

    boolean isEditing();

    CompletableFuture<Void> requestEditSession();
}
