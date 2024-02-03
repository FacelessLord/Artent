package faceless.artent.registries;

import faceless.artent.api.functions.Factory;
import faceless.artent.trading.api.IItemStackPriceDeterminator;
import faceless.artent.trading.api.IPriceDeterminatorContext;
import faceless.artent.trading.priceDeterminators.ConstantItemStackPriceDeterminator;

import java.util.HashMap;
import java.util.Map;

public class ItemPriceDeterminatorRegistry implements IRegistry {
	public final Map<String, Factory<? extends IPriceDeterminatorContext>> factories = new HashMap<>();
	public final Map<String, IItemStackPriceDeterminator> determinators = new HashMap<>();

	@Override
	public void register() {
		register(ConstantItemStackPriceDeterminator.NAME, new ConstantItemStackPriceDeterminator(), ConstantItemStackPriceDeterminator.ConstantPriceDeterminatorContext::new);
	}

	public <T extends IPriceDeterminatorContext> void register(String registryName, IItemStackPriceDeterminator determinator, Factory<T> contextFactory) {
		determinators.put(registryName, determinator);
		factories.put(registryName, contextFactory);
	}
}
