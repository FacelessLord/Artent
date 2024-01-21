package faceless.artent.api.functions;

import java.util.Objects;

@FunctionalInterface
public interface TetraConsumer<A, B, C, D> {
	void accept(A a, B b, C c, D d);

	default TetraConsumer<A, B, C, D> andThen(TetraConsumer<? super A, ? super B, ? super C, ? super D> var1) {
		Objects.requireNonNull(var1);
		return (a, b, c, d) -> {
			this.accept(a, b, c, d);
			var1.accept(a, b, c, d);
		};
	}
}
