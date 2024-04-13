package faceless.artent.leveling.api;

public enum SpecialMobType {
    Common,
    Cursed,
    Demonic,
    Eldritch,
    Weakened;

    public static SpecialMobType fromInt(int ord) {
        return switch (ord) {
            case 2 -> Cursed;
            case 3 -> Demonic;
            case 4 -> Eldritch;
            case 5 -> Weakened;
            default -> Common;
        };
    }
}
