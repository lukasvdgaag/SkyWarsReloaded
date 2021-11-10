package net.gcnt.skywarsreloaded.utils.properties;

public enum FolderProperties {

    TEMPLATE_FOLDER("map_templates"),
    PLAYERDATA_FOLDER("player_data"),
    KITS_FOLDER("kits");

    private final String folderName;

    FolderProperties(String folderNameIn) {
        this.folderName = folderNameIn;
    }

    public String toString() {
        return folderName;
    }
}
