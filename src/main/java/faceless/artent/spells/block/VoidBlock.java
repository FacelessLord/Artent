package faceless.artent.spells.block;

import com.mojang.serialization.MapCodec;
import faceless.artent.api.item.INamed;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.spells.blockEntity.VoidBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VoidBlock extends BlockWithEntity implements INamed {
	public static final MapCodec<VoidBlock> CODEC = VoidBlock.createCodec(VoidBlock::new);

	public VoidBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}



	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//		return super.getOutlineShape(state, world, pos, context);
        return Block.createCuboidShape(0, 0, 0, 0, 0, 0);
	}

	@Override
	public String getId() {
		return "void";
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new VoidBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, ModBlockEntities.VoidBlock, (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}
}
