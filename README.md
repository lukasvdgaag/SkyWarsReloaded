# SkyWarsReloaded

*Created by Rexi666.*

The most popular Skywars plugin ever built for Spigot and Bukkit!

This plugin is a continuation of the original SkyWars plugin created by Walrusone (v4.1.3) and by Devmart.
New features, bug fixes, extensive improvements, and new Minecraft versions support (1.14 - 1.21+) are added to this plugin.

Please note that the old SkyWarsReloaded plugin is no longer being maintained by the original author and that this is the official replacement.
We will continue to maintain this version until further notice, and community contributions are always welcome!

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
    <version>5.6.1</version>
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