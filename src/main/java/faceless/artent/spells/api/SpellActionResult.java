package faceless.artent.spells.api;

public class SpellActionResult {
    private SpellActionResult(SpellActionResultType type) {
        this.type = type;
    }

    public SpellActionResultType type;
    public int recoilLevel;

    public static SpellActionResult Continue() {
        return new SpellActionResult(SpellActionResultType.Continue);
    }

    public static SpellActionResult Stop() {
        return new SpellActionResult(SpellActionResultType.Stop);
    }

    public static SpellActionResult Recoil(int level) {
        var result = new SpellActionResult(SpellActionResultType.Recoil);
        result.recoilLevel = level;
        return result;
    }

}
