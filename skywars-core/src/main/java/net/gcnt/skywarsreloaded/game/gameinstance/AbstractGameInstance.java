package net.gcnt.skywarsreloaded.game.gameinstance;

import com.google.gson.JsonObject;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.types.GameState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractGameInstance implements GameInstance {

    // Final
    protected final SkyWarsReloaded plugin;

    // Static Properties
    protected final UUID id;
    protected GameTemplate gameTemplate;

    // Instance runtime data
    private GameState state;

    public AbstractGameInstance(SkyWarsReloaded plugin, UUID id, @Nullable GameTemplate gameTemplate) {
        this.plugin = plugin;
        this.id = id;
        this.gameTemplate = gameTemplate;
        this.state = GameState.DISABLED;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public GameTemplate getTemplate() {
        return this.gameTemplate;
    }

    @Override
    public void setTemplate(GameTemplate template) {
        this.gameTemplate = template;
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public CompletableFuture<Void> requestEditSession() {
        return null;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id.toString());
        json.addProperty("template", this.gameTemplate.getName());
        json.addProperty("state", this.state.name());
        json.addProperty("players", this.getPlayerCount());
        json.addProperty("editing", this.isEditing());
        return json;
    }
}
