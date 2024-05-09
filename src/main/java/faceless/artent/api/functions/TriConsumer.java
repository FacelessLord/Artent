package faceless.artent.api.functions;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<A, B, C> {
    void accept(A a, B b, C c);

    default TriConsumer<A, B, C> andThen(TriConsumer<? super A, ? super B, ? super C> var1) {
        Objects.requireNonNull(var1);
        return (a, b, c) -> {
            this.accept(a, b, c);
            var1.accept(a, b, c);
        };
    }
}

