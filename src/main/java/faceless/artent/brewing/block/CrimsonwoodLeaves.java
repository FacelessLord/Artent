package faceless.artent.brewing.block;

import faceless.artent.api.item.INamed;
import net.minecraft.block.LeavesBlock;

public class CrimsonwoodLeaves extends LeavesBlock implements INamed {
    public CrimsonwoodLeaves(Settings settings) {
        super(settings);
    }

    @Override
    public String getId() {
        return "crimsonwood_leaves";
    }
}