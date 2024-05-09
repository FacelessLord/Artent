package faceless.artent.api.functions;

import java.util.function.Supplier;

public class Lazy<T> {
    private T value;
    private final Supplier<T> factory;

    public Lazy(Supplier<T> factory) {
        this.factory = factory;
    }

    public T get() {
        if (value == null)
            value = this.factory.get();
        return value;
    }
}
