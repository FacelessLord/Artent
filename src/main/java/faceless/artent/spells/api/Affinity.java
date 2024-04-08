package faceless.artent.spells.api;

public class Affinity {
    public AffinityType type;
    public float value;

    public Affinity(AffinityType type, float value) {
        this.type = type;
        this.value = value;
    }

    public static Affinity of(AffinityType type) {
        return new Affinity(type, 1);
    }
}
