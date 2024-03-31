package faceless.artent.spells.item;
import faceless.artent.api.item.ArtentItem;
import faceless.artent.spells.api.ISpellInventoryItem;

public class SpellBook extends ArtentItem implements ISpellInventoryItem {
    public final int level;

    public SpellBook(int size, Settings settings) {
        super(settings, "spell_book_" + size);
        this.level = size;
    }

    @Override
    public int getLevel() {
        return level * 3;
    }
}
