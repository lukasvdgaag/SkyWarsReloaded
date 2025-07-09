# SkyWarsReloaded Fork

*Forked by Rexi666.*

The most popular Skywars plugin ever built for Spigot and Bukkit!

This plugin is a continuation of the original SkyWars plugin created by Walrusone (v4.1.3) and by Devmart.
New features, bug fixes, extensive improvements, and new Minecraft versions support (1.14 - 1.21+) are added to this plugin.

Please note that the old SkyWarsReloaded plugin is no longer being maintained by the original author and that this is the official replacement.
We will continue to maintain this version until further notice, and community contributions are always welcome!

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

## CHANGED:
- added default-kit on config, you can add a default kit so if a new player joins without picking a kit, it will be automatically assinged
- Messages added to config
- New command `/sw joinmenu <solo/team>`. permission: `sw.joinmenu` (default). Players can open join menu
- New Placeholders `%swr_players_playing%` (players ingame) `%swr_players_waiting%` (players waiting)
- Added those placeholders by game type `%swr_players_playing_solo%`, `%swr_players_waiting_solo%`, `%swr_players_playing_team%`, `%swr_players_waiting_team%`
- Spectators can now teleport to players
- Error "Chat validation error" fixed
- Losses count fixed