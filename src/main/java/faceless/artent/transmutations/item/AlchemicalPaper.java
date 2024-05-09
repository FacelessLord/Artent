package faceless.artent.transmutations.item;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.objects.ModBlocks;
import faceless.artent.registries.TransmutationRegistry;
import faceless.artent.transmutations.api.CircleHelper;
import faceless.artent.transmutations.block.AlchemicalCircleBlock;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlchemicalPaper extends ArtentItem {
    public AlchemicalPaper() {
        super("alchemical_paper");
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var blockSide = context.getSide();
        var blockPos = context.getBlockPos();
        var blockState = context.getWorld().getBlockState(blockPos);
        if (blockState.getBlock() == ModBlocks.AlchemicalCircle && context.shouldCancelInteraction()) {
            useOnCircle(context);
            return ActionResult.SUCCESS;
        }

        if (stack == null || !stack.hasNbt() || !stack.getOrCreateNbt().contains("circleFormula"))
            return ActionResult.PASS;

        BlockPos circlePos = blockPos.add(blockSide.getVector());

        BlockState circleState = ModBlocks.AlchemicalCircle
          .getDefaultState()
          .with(AlchemicalCircleBlock.FACING, blockSide);
        context.getWorld().setBlockState(circlePos, circleState, Block.NOTIFY_ALL);

        var blockEntity = context.getWorld().getBlockEntity(circlePos);
        if (blockEntity == null)
            return ActionResult.FAIL;
        var circle = (AlchemicalCircleEntity) blockEntity;

        var circleString = stack.getOrCreateNbt().getString("circleFormula");

        circle.parts = CircleHelper.getCircles(circleString);
        circle.transmutation = TransmutationRegistry.registry.getOrDefault(circleString, null);

        return super.useOnBlock(context);
    }

    private void useOnCircle(ItemUsageContext context) {
        var stack = context.getStack();
        var blockPos = context.getBlockPos();
        var blockEntity = context.getWorld().getBlockEntity(blockPos);
        if (blockEntity == null)
            return;

        var circle = (AlchemicalCircleEntity) blockEntity;

        var formula = CircleHelper.createCircleFormula(circle.parts);
        var handTag = stack.getOrCreateNbt();
        handTag.putString("circleFormula", formula);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt() && stack.getOrCreateNbt().contains("circleFormula");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (!hasGlint(stack))
            return;
        var formula = stack.getOrCreateNbt().getString("circleFormula");
        tooltip.add(Text.literal(formula));
    }
}
