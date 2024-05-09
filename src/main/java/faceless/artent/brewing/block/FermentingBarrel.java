package faceless.artent.brewing.block;

import com.mojang.serialization.MapCodec;
import faceless.artent.api.ChatUtils;
import faceless.artent.api.MiscUtils;
import faceless.artent.api.item.INamed;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.brewing.blockEntities.FermentingBarrelBlockEntity;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModItems;
import faceless.artent.registries.AlchemicalPotionRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FermentingBarrel extends BlockWithEntity implements INamed {
    public static final MapCodec<FermentingBarrel> CODEC = FermentingBarrel.createCodec(FermentingBarrel::new);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public FermentingBarrel(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public String getId() {
        return "fermenting_barrel";
    }

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

    @Override
    public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit
    ) {
        var be = (FermentingBarrelBlockEntity) world.getBlockEntity(pos);
        if (be == null) return ActionResult.FAIL;
        var stack = player.getEquippedStack(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        if (stack.getItem() == ModItems.GoldenBucketFilled) {
            if (be.potionKey.equals("")) {
                var potion = AlchemicalPotionUtil.getPotion(stack);
                if (potion != null) {
                    be.setPotionId(potion.id);
                    player.setStackInHand(hand,
                                          ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.GoldenBucket)));
                    ChatUtils.sendMessageToPlayer(world, player, "text.artent.barrel.filled");
                }
            } else
                ChatUtils.sendMessageToPlayer(world, player, "text.artent.barrel.filled.already");
        } else if (stack.getItem() == ModItems.PotionPhial) {
            var potion = AlchemicalPotionUtil.getPotion(stack);
            if (potion == null || !AlchemicalPotionRegistry.fermentedPotionIsRegistered(potion.id)) {
                return ActionResult.FAIL;
            }
            if (be.potionKey.equals("")) {
                be.setPotionId(potion.id, 3);
                if (!player.isCreative()) {
                    stack.decrement(1);
                    MiscUtils.giveOrDropItem(world, pos, player, new ItemStack(ModItems.EmptyPhial));
                }
                ChatUtils.sendMessageToPlayer(world, player, "text.artent.barrel.filled");
            } else if (be.potionKey.equals(potion.id)) {
                if (be.portionsLeft <= 6) {
                    be.setPotionId(potion.id, be.portionsLeft + 3);
                    if (!player.isCreative()) {
                        stack.decrement(1);
                        MiscUtils.giveOrDropItem(world, pos, player, new ItemStack(ModItems.EmptyPhial));
                    }
                    ChatUtils.sendMessageToPlayer(world, player, "text.artent.barrel.filled");
                } else
                    ChatUtils.sendMessageToPlayer(world, player, "text.artent.barrel.filled.already");
            } else
                ChatUtils.sendMessageToPlayer(world, player, "text.artent.barrel.filled.different");
        } else if (be.isFermented()) {
            var potionKey = be.potionKey;
            if (stack.getItem() == ModItems.SmallConcentratePhial) {
                var amount = be.takePotionPortions(1);
                if (amount == 0) return ActionResult.FAIL;
                if (!player.isCreative())
                    stack.decrement(1);
                var smallConcentrate = AlchemicalPotionUtil.setFermentedPotion(new ItemStack(ModItems.SmallConcentrate),
                                                                               potionKey);
                MiscUtils.giveOrDropItem(world, pos, player, smallConcentrate);
            } else if (stack.getItem() == ModItems.MediumConcentratePhial) {
                var amount = be.takePotionPortions(3);
                if (amount == 0) return ActionResult.FAIL;
                var mediumConcentrate = AlchemicalPotionUtil.setFermentedPotion(new ItemStack(ModItems.MediumConcentrate),
                                                                                potionKey,
                                                                                amount);
                if (!player.isCreative())
                    stack.decrement(1);
                MiscUtils.giveOrDropItem(world, pos, player, mediumConcentrate);
            } else if (stack.getItem() == ModItems.MediumConcentrate) {
                var potion = AlchemicalPotionUtil.getFermentedPotion(stack);
                if (potion != null && potion.id.equals(potionKey)) {
                    var concentrateAmount = AlchemicalPotionUtil.getConcentrateAmount(stack);
                    var amount = be.takePotionPortions(Math.min(3 - concentrateAmount, 3));
                    if (amount == 0) return ActionResult.FAIL;
                    AlchemicalPotionUtil.setConcentrateAmount(stack, amount + concentrateAmount);
                }
            } else if (stack.getItem() == ModItems.BigConcentratePhial) {
                var amount = be.takePotionPortions(9);
                if (amount == 0) return ActionResult.FAIL;
                if (!player.isCreative())
                    stack.decrement(1);
                var bigConcentrate = new ItemStack(ModItems.BigConcentrate);
                player.giveItemStack(AlchemicalPotionUtil.setFermentedPotion(bigConcentrate, potionKey, amount));
            } else if (stack.getItem() == ModItems.BigConcentrate) {
                var potion = AlchemicalPotionUtil.getPotion(stack);
                if (potion != null && potion.id.equals(potionKey)) {
                    var concentrateAmount = AlchemicalPotionUtil.getConcentrateAmount(stack);
                    var amount = be.takePotionPortions(Math.min(9 - concentrateAmount, 9));
                    if (amount == 0) return ActionResult.FAIL;
                    AlchemicalPotionUtil.setConcentrateAmount(stack, amount + concentrateAmount);
                }
            }
        }
        if (stack.isEmpty() && world.isClient()) {
            if (be.isFermented()) {
                player.sendMessage(Text
                                     .translatable("text.artent.barrel.fermented." + be.portionsLeft + "/" + 9 + ".of")
                                     .append(Text.translatable(be.potionKey)), false);
            } else {
                if (!be.potionKey.equals(""))
                    player.sendMessage(Text
                                         .translatable("text.artent.barrel.fermenting." + be.portionsLeft + "/" + 9)
                                         .append(Text.translatable(be.potionKey))
                                         .append(Text.translatable("text.artent.fermentation.time.prefix"))
                                         .append(Text.translatable(String.valueOf((FermentingBarrelBlockEntity.FERMENTATION_TIME -
                                                                                   be.fermentedTime) / 20)))
                                         .append(Text.translatable("text.artent.fermentation.time.suffix")), false);
                else
                    player.sendMessage(Text.translatable("text.artent.barrel.empty"), false);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      World world,
      BlockState state,
      BlockEntityType<T> type
    ) {
        return validateTicker(type,
                              ModBlockEntities.FermentingBarrel,
                              (world1, pos, state1, be) -> be.tick(world1, pos, state1));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FermentingBarrelBlockEntity(pos, state);
    }

    // TODO change model based on surrounding - cut upper portion of box when there is no block over barrel
    // add top legs when there is a barrel over it
}