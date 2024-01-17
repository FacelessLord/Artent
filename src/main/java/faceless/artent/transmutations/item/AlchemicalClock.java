package faceless.artent.transmutations.item;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AlchemicalClock extends ArtentItem {
	public AlchemicalClock() {
		super("alchemy_clock");
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return TypedActionResult.success(user.getEquippedStack(EquipmentSlot.MAINHAND));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.getOrCreateNbt().contains("circlePos");
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos circlePos = context.getBlockPos();
		BlockEntity entity = context.getWorld().getBlockEntity(circlePos);
		var player = context.getPlayer();
		if (player == null)
			return ActionResult.FAIL;

		if (entity instanceof AlchemicalCircleEntity) {
			if (context.getStack().getOrCreateNbt().contains("circlePos")) {
				int[] arr = context.getStack().getOrCreateNbt().getIntArray("circlePos");
				BlockEntity entity2 = context.getWorld().getBlockEntity(new BlockPos(arr[0], arr[1], arr[2]));
				if (entity2 != null) {
					BlockPos pos = entity2.getPos();

					if (pos.isWithinDistance(circlePos, 16)) {
						if (entity2 instanceof AlchemicalCircleEntity) {
							bindEntities((AlchemicalCircleEntity) entity, (AlchemicalCircleEntity) entity2);
							player.sendMessage(Text.translatable("artent.chat.circle.bound.circles"), true);
						} else {
							bindEntities((AlchemicalCircleEntity) entity, entity2, context.getSide());
							player.sendMessage(Text.translatable("artent.chat.circle.bound.circle.and.entity"), true);
						}
						context.getStack().getOrCreateNbt().remove("circlePos");
					} else
						player.sendMessage(Text.translatable("artent.chat.circle.too.far"), true);
				}
			} else {
				context
					.getStack()
					.getOrCreateNbt()
					.putIntArray("circlePos", new int[]{ circlePos.getX(), circlePos.getY(), circlePos.getZ() });
				player.sendMessage(Text.translatable("artent.chat.circle.pos.saved"), true);
			}
		} else {
			if (entity != null) {
				if (context.getStack().getOrCreateNbt().contains("circlePos")) {
					int[] arr = context.getStack().getOrCreateNbt().getIntArray("circlePos");
					BlockEntity entity2 = context.getWorld().getBlockEntity(new BlockPos(arr[0], arr[1], arr[2]));
					if (entity2 == null)
						return ActionResult.FAIL;

					BlockPos pos = entity2.getPos();

					if (pos.isWithinDistance(circlePos, 16)) {
						if (entity2 instanceof AlchemicalCircleEntity) {
							bindEntities((AlchemicalCircleEntity) entity2, entity, context.getSide());
							player.sendMessage(Text.translatable("artent.chat.circle.bound.circle.and.entity"), true);
							context.getStack().getOrCreateNbt().remove("circlePos");
						} else {
							player.sendMessage(Text.translatable("artent.chat.circle.cant.bind.two.entities"), true);
						}
					} else
						player.sendMessage(Text.translatable("artent.chat.circle.too.far"), true);
				} else {
					context
						.getStack()
						.getOrCreateNbt()
						.putIntArray(
							"circlePos",
							new int[]{ circlePos.getX(), circlePos.getY(), circlePos.getZ() });
					player.sendMessage(Text.translatable("artent.chat.circle.pos.saved"), true);
				}
			} else if (player.isSneaking()) {
				player.getEquippedStack(EquipmentSlot.MAINHAND).getOrCreateNbt().remove("circlePos");
			}
		}
		return ActionResult.SUCCESS;
	}

	private void bindEntities(AlchemicalCircleEntity entity, BlockEntity entity2, Direction dir) {
		entity.boundEntities.add(new Pair<>(entity2, dir));
	}

	private void bindEntities(AlchemicalCircleEntity entity, AlchemicalCircleEntity entity2) {
		entity.boundCircles.add(entity2);
		entity2.boundCircles.add(entity);
	}
}
