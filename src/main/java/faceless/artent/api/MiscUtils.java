package faceless.artent.api;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class MiscUtils {
	public static boolean isInRange(int a, int min, int max) {
		return a >= min && a <= max;
	}

	public static int clamp(int a, int min, int max) {
		return Math.max(min, Math.min(a, max));
	}

	public static int[] applyDirection(int[] src, Direction direction) {
		return switch (direction) {
			case DOWN -> new int[]{ src[0], -src[1], -src[2] };
			case NORTH -> new int[]{ src[0], src[2], -src[1] };
			case SOUTH -> new int[]{ src[0], -src[2], src[1] };
			case EAST -> new int[]{ src[1], -src[0], src[2] };
			case WEST -> new int[]{ -src[1], src[0], src[2] };
			default -> src;
		};
	}

	public static float[] applyDirection(float[] src, Direction direction) {
		return switch (direction) {
			case DOWN -> new float[]{ src[0], -src[1], -src[2] };
			case NORTH -> new float[]{ src[0], src[2], -src[1] };
			case SOUTH -> new float[]{ src[0], -src[2], src[1] };
			case EAST -> new float[]{ src[1], -src[0], src[2] };
			case WEST -> new float[]{ -src[1], src[0], src[2] };
			default -> src;
		};
	}

	public static OrderedBox orderBox(Box box, Direction direction) {
		return switch (direction) {
			case UP, SOUTH, EAST ->
				new OrderedBox((int) box.minX, (int) box.maxX, 1, (int) box.minY, (int) box.maxY, 1, (int) box.minZ, (int) box.maxZ, 1);
			case DOWN ->
				new OrderedBox((int) box.minX, (int) box.maxX, 1, (int) box.maxY, (int) box.minY, -1, (int) box.minZ, (int) box.maxZ, 1);
			case NORTH ->
				new OrderedBox((int) box.minX, (int) box.maxX, 1, (int) box.minY, (int) box.maxY, 1, (int) box.maxZ, (int) box.minZ, -1);
			case WEST ->
				new OrderedBox((int) box.maxX, (int) box.minX, -1, (int) box.minY, (int) box.maxY, 1, (int) box.minZ, (int) box.maxZ, 1);
		};
	}
}
