package faceless.artent.api.functions;

@FunctionalInterface
public interface Factory<T> {
	T create();
}
