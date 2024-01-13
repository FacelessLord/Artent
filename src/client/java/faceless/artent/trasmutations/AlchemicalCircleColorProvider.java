package faceless.artent.trasmutations;

import faceless.artent.api.Color;
import faceless.artent.transmutations.State;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class AlchemicalCircleColorProvider implements BlockColorProvider {
    @Override
    public int getColor(BlockState state, BlockRenderView view, BlockPos pos, int tintIndex) {
    	var minecraftClient = MinecraftClient.getInstance();
        World world = minecraftClient.world;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AlchemicalCircleEntity) {
            AlchemicalCircleEntity entity = (AlchemicalCircleEntity) blockEntity;
            if (entity.transmutation != null) {
                if (entity.state == State.Preparation) {
                    float percentage = entity.actionTime / ((float) entity.transmutation.getPrepTime());//todo
                    return entity.transmutation.getPreparationColor()
                            .multiply(percentage)
                            .add(new Color().multiply(1 - percentage))
                            .asInt();
                }
                if (entity.state == State.Action) {
                    return entity.transmutation.getPreparationColor().asInt();
                }
            }
        }
    	
        return java.awt.Color.white.getRGB();
    }
}
