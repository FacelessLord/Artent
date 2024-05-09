package faceless.artent.api.functions;

@FunctionalInterface
public interface TetraFunction<A, B, C, D, F> {
    F accept(A a, B b, C c, D d);
}
