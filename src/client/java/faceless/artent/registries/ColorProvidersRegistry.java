package faceless.artent.registries;

import faceless.artent.api.math.Color;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModItems;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;


public class ColorProvidersRegistry implements IRegistry {
    @Override
    public void register() {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            if (view == null || view.getBlockEntityRenderData(pos) == null)
                return new Color().asInt();
            //noinspection DataFlowIssue
            return (int) view.getBlockEntityRenderData(pos);
        }, ModBlocks.AlchemicalCircle);

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            if (view == null)
                return 0;
            var be = view.getBlockEntity(pos);
            if (be instanceof BrewingCauldronBlockEntity cauldron) {
                return cauldron.color.toHex();
            }
            return 0;
        }, ModBlocks.CauldronFluid);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                                                if (tintIndex == 0) return -1;
                                                return AlchemicalPotionUtil.getColor(stack);
                                            },
                                            ModItems.PotionPhial,
                                            ModItems.PotionPhialExplosive,
                                            ModItems.GoldenBucketFilled,
                                            ModItems.SmallConcentrate,
                                            ModItems.MediumConcentrate,
                                            ModItems.BigConcentrate);

    }
}
