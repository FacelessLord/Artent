package faceless.artent.spells.block;

import faceless.artent.api.block.ArtentBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

public class InscriptionTablePt2 extends ArtentBlock {
    public InscriptionTablePt2(Settings settings) {
        super("inscription_table2", settings);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        var facing = getFacing(state);
        var directionToMain = facing.rotateClockwise(Direction.Axis.Y);
        world.breakBlock(pos.offset(directionToMain), true);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return new ArrayList<>(0);
    }

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
