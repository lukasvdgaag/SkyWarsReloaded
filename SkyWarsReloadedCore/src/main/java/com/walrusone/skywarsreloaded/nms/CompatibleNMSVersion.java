package com.walrusone.skywarsreloaded.nms;

public enum CompatibleNMSVersion {

    // 1.8
    v1_8_R3(8, "v1_8_R3"),

    // 1.9
    v1_9_R1(9, "v1_9_R1"),
    v1_9_R2(9, "v1_9_R2"),

    // 1.10
    v1_10_R1(10, "v1_10_R1"),

    // 1.11
    v1_11_R1(11, "v1_11_R1"),

    // 1.12
    v1_12_R1(12, "v1_12_R1"),

    // 1.13
    v1_13_R2(13, "v1_13_R2"),

    // 1.14
    v1_14_R1(14, "v1_14_R1"),

    // 1.15
    v1_15_R1(15, "v1_15_R1"),

    // 1.16
    v1_16_R1(16, "v1_16_R1"),
    v1_16_R2(16, "v1_16_R2"),
    v1_16_R3(16, "v1_16_R3"),

    // 1.17
    v1_17_R1(17, "v1_17_R1"),

    // 1.18
    v1_18_R2(18, "v1_18_R2"),

    // 1.19
    v1_19_R1(19, "v1_19_R1"),
    v1_19_R2(19, "v1_19_R1"),
    v1_19_R3(19, "v1_19_R1"),

    // 1.20
    v1_20_R1(20, "v1_20_R1"),
    v1_20_R2(20, "v1_20_R1"),

    // 1.21
    v1_21_R1(21, "v1_21_R1"),
    v1_21_R2(21, "v1_21_R1"),
    v1_21_R3(21, "v1_21_R1"),
    ;

    private final int featureVersion;
    private final String nmsImplVersion;

    CompatibleNMSVersion(int featureVersion, String nmsImplVersion) {
        this.featureVersion = featureVersion;
        this.nmsImplVersion = nmsImplVersion;
    }

    public String getNmsImplVersion() {
        return nmsImplVersion;
    }

    static CompatibleNMSVersion getLatestSupported(Integer currentFeatureVersion) {
        // iterate over the available NMS versions and get the latest one that matches the current feature version (not higher than the current version)
        if (currentFeatureVersion != null) {
            for (int i = CompatibleNMSVersion.values().length - 1; i >= 0; i--) {
                CompatibleNMSVersion version = CompatibleNMSVersion.values()[i];
                if (version.getFeatureVersion() <= currentFeatureVersion) {
                    return version;
                }
            }
        }

        return CompatibleNMSVersion.values()[CompatibleNMSVersion.values().length - 1];
    }

    public int getFeatureVersion() {
        return this.featureVersion;
    }
}
