package faceless.artent.spells.block;

import com.mojang.serialization.MapCodec;
import faceless.artent.api.item.INamed;
import faceless.artent.objects.ModBlocks;
import faceless.artent.spells.blockEntity.InscriptionTableBlockEntity;
import faceless.artent.spells.screenhandlers.InscriptionTableScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InscriptionTable extends BlockWithEntity implements INamed {
    public static final MapCodec<InscriptionTable> CODEC = InscriptionTable.createCodec(InscriptionTable::new);

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public InscriptionTable(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public String getId() {
        return "inscription_table";
    }

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    protected void openScreen(World world, BlockState state, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity cast to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
        }
    }

    @Override
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof InscriptionTableBlockEntity table)
            return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
                                                         new InscriptionTableScreenHandler(
                                                           syncId,
                                                           inventory,
                                                           table.inventory,
                                                           ScreenHandlerContext.create(world, pos)),
                                                       Text.translatable("gui.artent.inscription_table"));
        return null;
    }

    @Override
    public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit
    ) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        this.openScreen(world, state, pos, player);
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InscriptionTableBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      World world,
      BlockState state,
      BlockEntityType<T> type
    ) {
        return null;//validateTicker(type, ModBlockEntities.SHARPENING_ANVIL, (world1, pos, state1, be) -> be.tick(world1, pos, state1));
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        var facing = getFacing(state);
        var directionToPt2 = facing.rotateCounterclockwise(Direction.Axis.Y);
        if (!world.isClient())
            world.breakBlock(pos.offset(directionToPt2), false);
    }

    //This method will drop all items onto the ground when the block is broken
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof InscriptionTableBlockEntity table) {
                var dropStacks = DefaultedList.ofSize(3, ItemStack.EMPTY);
                dropStacks.set(0, table.inventory.getStack(0));
                dropStacks.set(1, table.inventory.getStack(1));
                dropStacks.set(2, table.inventory.getStack(2));
                ItemScatterer.spawn(world, pos, dropStacks);
                // update comparators
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
            var facing = getFacing(state);
            var directionToPt2 = facing.rotateCounterclockwise(Direction.Axis.Y);
            world.setBlockState(pos.offset(directionToPt2), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public void onPlaced(
      World world,
      BlockPos pos,
      BlockState state,
      @Nullable LivingEntity placer,
      ItemStack itemStack
    ) {
        super.onPlaced(world, pos, state, placer, itemStack);

        var facing = getFacing(state);
        var directionToPt2 = facing.rotateCounterclockwise(Direction.Axis.Y);
        var pt2Pos = pos.offset(directionToPt2);
        if (world.getBlockState(pt2Pos).isAir())
            world.setBlockState(pt2Pos,
                                ModBlocks.InscriptionTable2.getDefaultState().with(FACING, facing),
                                Block.NOTIFY_ALL_AND_REDRAW);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var facing = getFacing(state);
        var directionToPt2 = facing.rotateCounterclockwise(Direction.Axis.Y);
        var pt2Pos = pos.offset(directionToPt2);
        return world.getBlockState(pt2Pos).isReplaceable() && super.canPlaceAt(state, world, pos);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        var drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(ModBlocks.InscriptionTableItem, 1));
        return drops;
    }

    public Direction getFacing(BlockState state) {
        return state.get(FACING);
    }
}