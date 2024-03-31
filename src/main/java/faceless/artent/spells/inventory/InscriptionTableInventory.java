package faceless.artent.spells.inventory;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.spells.blockEntity.InscriptionTableBlockEntity;

public class InscriptionTableInventory extends ArtentInventory {
    public InscriptionTableBlockEntity table;

    public InscriptionTableInventory(InscriptionTableBlockEntity table) {
        this.table = table;
    }

    @Override
    public int size() {
        return 3;
    }
}
