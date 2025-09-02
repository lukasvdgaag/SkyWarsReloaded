# SkyWarsReloaded

*Created by [Devmart](https://devmart.net).*

The most popular Skywars plugin ever built for Spigot and Bukkit!

This plugin is a continuation of the original SkyWars plugin created by Walrusone (v4.1.3).
New features, bug fixes, extensive improvements, and new Minecraft versions support (1.14 - 1.21+) are added to this plugin.

Please note that the old SkyWarsReloaded plugin is no longer being maintained by the original author and that this is the official replacement.
We will continue to maintain this version until further notice, and community contributions are always welcome!

> [!TIP]  
> **Want to support our work and help us maintain the plugin?**
>
> Donate to our PayPal account: [devmart.net/donate](https://devmart.net/donate)  
> Or buy the [SkyWarsReloaded Extension](https://devmart.net/swre) to get access to more features for a small price!

*Want to see what we are working on or planning to add? Check out the [SkyWarsReloaded project board](https://github.com/users/lukasvdgaag/projects/2)!*

## Development setup

### Maven dependency

**Repository**

```xml

<repositories>
    <repository>
        <id>gcnt</id>
        <url>https://nexuslite.gcnt.net/repos/gcnt</url>
    </repository>
</repositories>
```

**Dependency**

```xml

<dependency>
    <groupId>net.gcnt</groupId>
    <artifactId>skywarsreloaded</artifactId>
    <version>5.6.33</version>
</dependency>
```

### Toolchains

In the latest versions of the plugin, we require toolchains to be set up in your `~/.m2/toolchains.xml` file.
This is because we compile the plugin core in Java 8, while some of the NMS handlers require higher Java versions to be compiled.

If your `~/.m2` directory does not yet contain a `toolchains.xml` file, create one.  
Make sure that you have the following JDK versions installed: `1.8`, `16`, `17`.

**Example `toolchains.xml` file**  
*The target paths may differ depending on your operating system and Java installation.*

```xml

<toolchains>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>1.8</version>
        </provides>
        <configuration>
            <jdkHome>/Library/Java/JavaVirtualMachines/jdk-1.8.jdk/Contents/Home</jdkHome>
        </configuration>
    </toolchain>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>16</version>
        </provides>
        <configuration>
            <jdkHome>/Library/Java/JavaVirtualMachines/jdk-16.jdk/Contents/Home</jdkHome>
        </configuration>
    </toolchain>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>17</version>
        </provides>
        <configuration>
            <jdkHome>/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home</jdkHome>
        </configuration>
    </toolchain>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>21</version>
        </provides>
        <configuration>
            <jdkHome>/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home</jdkHome>
        </configuration>
    </toolchain>
</toolchains>
```

## Installation

1. Head over to the download page.
2. Click the "Download Now" button.
3. ~~Select the correct version for your Minecraft server.~~ (As of version 5.3.0, the plugin works across all versions)
4. Download the required dependencies
5. Copy the files to your "plugins" folder.
6. Start the Minecraft server
7. Done! (Make sure to follow the Setup Steps)

## Requirements

**Required Plugins**: *Skywars requires the following plugins*

* WorldEdit

**Optional Plugins**: *Skywars can use the following plugins but does not require them for the basic functions*

* Skywars-Extention
* Vault
* HolographicDisplays
* PlaceholderAPI
* Multiverse-Core
* MVdWPlaceholderAPI
* PerWorldInventory
* SlimeWorldManager
* PartyAndFriends (for bungeecord installations)

## Commands & Permissions

* List of available commands [*[here]*](https://github.com/TechnicallyCoded/SkywarsReloadedWiki/wiki/commands)
* List of permissions [*[here]*](https://github.com/TechnicallyCoded/SkywarsReloadedWiki/wiki/permissions)

## Support

Are you having trouble running the plugin?

**Get help in our Discord server!**
When asking for help, provide the necessary context required for people to help you. This includes one or more of the following:

1. Logs (most important part usually). We recommend that you use our own "paste" website to submit your logs
2. Partial logs are harder to work with than your entire log file, we need the most information about what's happening
3. An image or recording of your problem in-game, if possible.
4. A description of what you have already tried.. We don't want to waste your time.

<a href="https://devmart.net/discord">
<picture>
    <source media="(prefers-color-scheme: dark)" srcset=" https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/653714c18aeaa62dfe96cd3f_636e0b5493894cf60b300587_full_logo_white_RGB.svg">
    <source media="(prefers-color-scheme: light)" srcset="https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/653714c1c624c0d37cd2d328_636e0b5061df290f5892d944_full_logo_black_RGB.svg">
    <img alt="Discord logo" src="https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/653714c1c2d8d50382c7df8a_636e0b5061df29d55a92d945_full_logo_blurple_RGB.svg">
</picture>
</a>