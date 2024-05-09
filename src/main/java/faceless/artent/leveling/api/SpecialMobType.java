package faceless.artent.leveling.api;

public enum SpecialMobType {
    Common,
    Cursed,
    Demonic,
    Eldritch,
    Wounded;

    public static SpecialMobType fromInt(int ord) {
        return switch (ord) {
            case 2 -> Cursed;
            case 3 -> Demonic;
            case 4 -> Eldritch;
            case 5 -> Wounded;
            default -> Common;
        };
    }

    public static SpecialMobType fromName(String name) {
        return switch (name) {
            case "common" -> Common;
            case "cursed" -> Cursed;
            case "demonic" -> Demonic;
            case "eldritch" -> Eldritch;
            case "wounded" -> Wounded;
            default -> null;
        };
    }
}
