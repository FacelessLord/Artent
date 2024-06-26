package faceless.artent.spells.blockEntity;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.spells.inventory.InscriptionTableInventory;
import faceless.artent.spells.screenhandlers.InscriptionTableScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class InscriptionTableBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    public ArtentInventory inventory;

    public InscriptionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.InscriptionTable, pos, state);
        inventory = new InscriptionTableInventory(this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        inventory.writeNbt(nbt);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        //noinspection DataFlowIssue
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("inscription_table");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new InscriptionTableScreenHandler(syncId,
                                                 playerInventory,
                                                 this.inventory,
                                                 ScreenHandlerContext.create(world, pos));
    }
}