package com.walrusone.skywarsreloaded.api.command;

import com.walrusone.skywarsreloaded.commands.BaseCmd;

public interface SWRCmdManager {

    void registerCommand(BaseCmd commandIn);

    void unregisterCommand(BaseCmd commandIn);

    BaseCmd getCommand(String name);

}
