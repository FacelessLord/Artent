package faceless.artent.registries;

import faceless.artent.objects.ModItems;
import faceless.artent.sharpening.api.IEnhancer;

import java.util.Hashtable;

public class EnhancerRegistry implements IRegistry {
	public static int nextId = 1;

	public static Hashtable<IEnhancer, Integer> EnhancerToIds = new Hashtable<>();
	public static Hashtable<Integer, IEnhancer> IdsToEnhancer = new Hashtable<>();

	public static void register(IEnhancer enhancer) {
		var id = nextId++;
		EnhancerToIds.put(enhancer, id);
		IdsToEnhancer.put(id, enhancer);
	}

	@Override
	public void register() {
		register(ModItems.EnderInversionUpgrade);
		register(ModItems.NetherFireStone);
		register(ModItems.GoldenCrossUpgrade);
		register(ModItems.PoisonBottleUpgrade);
		register(ModItems.VampiresFangUpgrade);
	}
}
