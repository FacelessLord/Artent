package faceless.artent.transmutations.world;

import com.mojang.serialization.MapCodec;

import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModItems;
//import faceless.artent.transmutations.AlchemicalCircleNetworkHook;
import faceless.artent.transmutations.State;
import faceless.artent.transmutations.network.AlchemicalCircleServerHook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AlchemicalCircleBlock extends BlockWithEntity {
	public static final IntProperty circleType = IntProperty.of("type", 0, 54);
    public static final MapCodec<AlchemicalCircleBlock> CODEC = AlchemicalCircleBlock.createCodec(AlchemicalCircleBlock::new);

	public AlchemicalCircleBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(circleType, 0));
	}
	
	public String getBlockId() {
		return "alchemical_circle";
	}

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.01f, 1f);
    }

	@Override
	public BlockEntity createBlockEntity(BlockPos var1, BlockState var2) {
		return new AlchemicalCircleEntity(var1, var2);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(circleType);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof AlchemicalCircleEntity))
			return ActionResult.FAIL;
		
		var circle = (AlchemicalCircleEntity) blockEntity;
		if (player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == ModItems.chalk) {
			AlchemicalCircleServerHook.packetOpenCircleGui(player, circle);
			System.out.println("Open circle");
			return ActionResult.SUCCESS;
		}
		
		var mainHand = player.getEquippedStack(EquipmentSlot.MAINHAND);

		if (!mainHand.isEmpty()) {
			if(mainHand.getItem() == ModItems.alchemicalClock) {
				var tag = new NbtCompound();
				circle.writeNbt(tag);
				player.sendMessage(Text.literal(tag.asString()));
				
				return ActionResult.SUCCESS;
			}
			
			if (circle.inventory.get(0).isEmpty()) {
				circle.inventory.set(0, mainHand);
				player.getInventory().setStack(player.getInventory().selectedSlot, ItemStack.EMPTY);

				return ActionResult.SUCCESS;
			}
		}

		if (player.isSneaking()) {
			if (!circle.inventory.get(0).isEmpty()) {
				if (mainHand.isEmpty()) {
					player.getInventory().setStack(player.getInventory().selectedSlot, circle.inventory.get(0));
					circle.inventory.set(0, ItemStack.EMPTY);

					return ActionResult.SUCCESS;
				}
			}
			if (circle.state != State.Idle) {
				circle.state = State.Idle;
				circle.actionTime = 0;

				return ActionResult.SUCCESS;
			}
		}

		if (circle.transmutation != null) {
			if (circle.state == State.Idle) {
				circle.state = State.Preparation;
				circle.alchemist = player;
			}
		}

		return ActionResult.SUCCESS;
	}

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}
	
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.alchemicalCircleEntity, (world1, pos, state1, be) -> AlchemicalCircleEntity.tick(world1, pos, state1, be));
    }
}
