package faceless.artent.spells.api;

public class ScrollStack {
    public Spell spell;
    public ScrollType scrollType = ScrollType.Common;

    public ScrollStack(Spell spell, ScrollType scrollType) {
        this.spell = spell;
        this.scrollType = scrollType;
    }

    public ScrollStack(Spell spell) {
        this(spell, ScrollType.Common);
    }
}
