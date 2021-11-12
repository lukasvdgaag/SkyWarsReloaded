package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.general.MainCmd;
import net.gcnt.skywarsreloaded.command.kits.*;
import net.gcnt.skywarsreloaded.command.maps.*;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class CoreSWCommandManager implements SWCommandManager {

    private final SkyWarsReloaded main;

    private final LinkedHashMap<Class<? extends SWCommand>, SWCommand> commands;

    public CoreSWCommandManager(SkyWarsReloaded mainIn) {
        this.main = mainIn;
        this.commands = new LinkedHashMap<>();
    }

    public void registerBaseCommands() {
        this.registerCommand(new MainCmd(this.main));
    }

    @Override
    public void registerMapCommands() {
        this.registerCommand(new MainMapCmd(this.main));
        this.registerCommand(new ListMapsCmd(this.main));
        this.registerCommand(new CreateMapCmd(this.main));
        this.registerCommand(new SaveMapCmd(this.main));
        this.registerCommand(new SetMapTeamsizeCmd(this.main));
        this.registerCommand(new SetMapCreatorCmd(this.main));
        this.registerCommand(new SetSpawnCmd(this.main));
        this.registerCommand(new EnableCmd(this.main));
        this.registerCommand(new DisableCmd(this.main));
    }

    @Override
    public void registerKitCommands() {
        this.registerCommand(new MainKitCmd(this.main));
        this.registerCommand(new CreateKitCmd(this.main));
        this.registerCommand(new PreviewKitCmd(this.main));
        this.registerCommand(new SetKitContentsCmd(this.main));
        this.registerCommand(new SetKitIconCmd(this.main));
        this.registerCommand(new SetKitUnavailableIconCmd(this.main));
        this.registerCommand(new SetKitSlotCmd(this.main));
        this.registerCommand(new SetKitDisplayNameCmd(this.main));
        this.registerCommand(new SetKitDescriptionCmd(this.main));
        this.registerCommand(new SetKitLoreCmd(this.main));
        this.registerCommand(new SetKitRequirementsCmd(this.main));
    }

    @Override
    public List<SWCommand> getCommands(String baseCmd) {
        return this.commands.values().stream().filter(swCommand -> swCommand.getParentCommand().equals(baseCmd)).collect(Collectors.toList());
    }

    public Collection<SWCommand> getCommands() {
        return this.commands.values();
    }

    public void registerCommand(SWCommand cmd) {
        this.commands.put(cmd.getClass(), cmd);
    }

    public <T extends SWCommand> T getCommand(Class<T> clazz) {
        return (T) commands.get(clazz);
    }

    public void runCommand(SWCommandSender sender, String command, String subCommand, String[] args) {
        for (SWCommand cmd : commands.values()) {
            if (cmd.getParentCommand().equalsIgnoreCase(command) && cmd.getName().equalsIgnoreCase(subCommand)) {
                cmd.processCommand(sender, args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);
            }
        }
    }
}
