package faceless.artent.api;

import java.util.Hashtable;
import java.util.function.Supplier;

public class DefaultedDict<K, V> {
	private final Hashtable<K, V> table;
	private final Supplier<V> defaultValueGenerator;

	public DefaultedDict(V defaultValue) {
		this(() -> defaultValue);
	}

	public DefaultedDict(Supplier<V> defaultValueGenerator) {
		this.table = new Hashtable<>();
		this.defaultValueGenerator = defaultValueGenerator;
	}

	public V get(K key) {
		if (table.containsKey(key))
			return table.get(key);
		var newList = defaultValueGenerator.get();
		table.put(key, newList);
		return newList;
	}

	public void put(K key, V value) {
		table.put(key, value);
	}
}