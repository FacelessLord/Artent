package faceless.artent.spells.api;

import net.minecraft.world.World;

import java.util.Hashtable;
import java.util.UUID;

public class CasterStorage {
    private final Hashtable<UUID, ICaster> CasterMap = new Hashtable<>();

    public ICaster getCasterById(UUID uuid, World world) {
        return CasterMap.getOrDefault(uuid, null);
    }

    public void putCaster(UUID uuid, ICaster caster) {
        CasterMap.put(uuid, caster);
    }

    public void removeCaster(UUID uuid) {
        CasterMap.remove(uuid);
    }

    public static ICaster getCasterById(World world, UUID uuid) {
        return ((IContainsCasterStorage) world).getCasterStorage().getCasterById(uuid, world);
    }

    public static void putCaster(World world, ICaster caster) {
        ((IContainsCasterStorage) world).getCasterStorage().putCaster(caster.getCasterUuid(), caster);
    }

    public static void removeCaster(World world, ICaster caster) {
        ((IContainsCasterStorage) world).getCasterStorage().removeCaster(caster.getCasterUuid());
    }
}
