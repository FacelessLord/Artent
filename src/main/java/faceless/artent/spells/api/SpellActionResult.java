package faceless.artent.spells.api;

public class SpellActionResult {
    private SpellActionResult(SpellActionResultType type, int manaToConsume) {
        this.type = type;
        this.manaToConsume = manaToConsume;
    }

    public SpellActionResultType type;
    public int manaToConsume;
    public int recoilLevel;

    public static SpellActionResult Continue(int manaToConsume) {
        return new SpellActionResult(SpellActionResultType.Continue, manaToConsume);
    }

    public static SpellActionResult Stop(int manaToConsume) {
        return new SpellActionResult(SpellActionResultType.Stop, manaToConsume);
    }

    public static SpellActionResult Recoil(int manaToConsume, int level) {
        var result = new SpellActionResult(SpellActionResultType.Recoil, manaToConsume);
        result.recoilLevel = level;
        return result;
    }

}
