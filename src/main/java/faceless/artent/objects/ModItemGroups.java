package faceless.artent.objects;

import faceless.artent.api.item.group.ArtentItemGroupBuilder;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

    public static final ArtentItemGroupBuilder Main = new ArtentItemGroupBuilder(
      () -> new ItemStack(ModItems.PhiloStone),
      "main");
    public static final ArtentItemGroupBuilder Potions = new ArtentItemGroupBuilder(
      () -> {
          var stack = new ItemStack(ModItems.PotionPhialExplosive);
          AlchemicalPotionUtil.setPotion(stack, AlchemicalPotions.FLIGHT);
          return stack;
      },
      "potions");
}
