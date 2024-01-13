package faceless.artent.transmutations;

import faceless.artent.transmutations.gui.PartType;

public class CirclePart {
    public final PartType part;
    public final boolean reverse;

    public CirclePart(PartType part, boolean reverse) {
        this.part = part;
        this.reverse = reverse;
    }

    @Override
    public String toString() {
        return part + (reverse ? "r" : "n");
    }
}
