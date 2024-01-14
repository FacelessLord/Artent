package faceless.artent.transmutations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CircleHelper {
    public static CirclePart getCirclePart(String circleString) {
        PartType type = PartType.fromCode(circleString.substring(0, 2));
        boolean reversed = circleString.charAt(2) == 'r';
        return new CirclePart(type, reversed);
    }

    public static String createCircleFormula(List<CirclePart> parts) {
        StringBuilder builder = new StringBuilder();
        parts.stream().sorted(Comparator.comparingInt(a -> a.part.ordinal() * 2 + (a.reverse ? 1 : 0))).forEach(p -> builder.append(p.toString()));
        return builder.toString();
    }

    public static List<CirclePart> getCircles(String formula) {
        List<CirclePart> parts = new ArrayList<>(formula.length() / 3);
        for (int i = 0; i < formula.length() / 3; i++) {
            parts.add(getCirclePart(formula.substring(i * 3, i * 3 + 3)));
        }
        return parts;
    }
}
