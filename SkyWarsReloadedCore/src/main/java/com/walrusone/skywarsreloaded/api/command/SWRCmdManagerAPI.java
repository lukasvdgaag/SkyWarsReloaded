package com.walrusone.skywarsreloaded.api.command;

import com.walrusone.skywarsreloaded.commands.BaseCmd;

public interface SWRCmdManagerAPI {

    void registerCommand(BaseCmd commandIn);

    void unregisterCommand(BaseCmd commandIn);

    BaseCmd getSubCommand(String name);

}
