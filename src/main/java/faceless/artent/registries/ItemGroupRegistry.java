package faceless.artent.registries;

import faceless.artent.objects.ModItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry implements IRegistry {

    @Override
    public void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier("artent", "main"), ModItemGroups.Main.build());
        Registry.register(Registries.ITEM_GROUP, new Identifier("artent", "potions"), ModItemGroups.Potions.build());
    }

}
