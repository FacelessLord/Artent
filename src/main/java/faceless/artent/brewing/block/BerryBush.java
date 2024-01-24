package faceless.artent.brewing.block;

import faceless.artent.api.item.INamed;
import faceless.artent.brewing.ingridients.Ingredients;
import faceless.artent.objects.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BerryBush extends Block implements INamed {
	public final int type;
	public static final IntProperty AGE = IntProperty.of("age", 0, 2);

	public BerryBush(int type, Settings settings) {
		super(settings);
		this.type = type;
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
	}

	@Override
	public String getId() {
		return Ingredients.GetBerryName(type) + "_bush";
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		if (world.getBaseLightLevel(pos, 0) >= 9 && state.get(AGE) < 2) {
			world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), Block.NOTIFY_LISTENERS);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

		var stack = player.getEquippedStack(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
		if (stack.getItem() == Items.BONE_MEAL) {
			if (!world.isClient()) {
				if (world.getBaseLightLevel(pos, 0) >= 9 && state.get(AGE) < 2 && world.random.nextFloat() < 0.80) {
					world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), Block.NOTIFY_LISTENERS);
				}
				var newStack = stack.copy();
				newStack.setCount(stack.getCount() - 1);
				player.setStackInHand(hand, newStack);
			}

			return ActionResult.success(world.isClient());
		}

		if (state.get(AGE) == 2) {
			if (!world.isClient()) {
				player.giveItemStack(new ItemStack(ModItems.berries[type], world.random.nextInt(3) + 1));
				state = state.with(AGE, 0);
				world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
			}

			return ActionResult.success(world.isClient());
		}
		return ActionResult.FAIL;
	}

	protected static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 15, 14);

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}