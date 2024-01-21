package faceless.artent.api.math;


public class Vec3 {
	public final float X;
	public final float Y;
	public final float Z;

	public static final Vec3 UP = new Vec3(0, 1, 0);

	public Vec3(float x, float y, float z) {
		X = x;
		Y = y;
		Z = z;
	}

	public float length() {
		return (float) Math.sqrt(X * X + Y * Y + Z * Z);
	}

	public Vec3 normalize() {
		var len = length();
		return new Vec3(X / len, Y / len, Z / len);
	}

	public float[] asArray() {
		return new float[]{ X, Y, Z };
	}

	public Vec3 rotate90AtCoord(int i) {
		return switch (i) {
			case 0 -> rotateX90();
			case 1 -> rotateY90();
			case 2 -> rotateZ90();
			default -> this;
		};
	}

	public Vec3 rotateX(float angle) {
		return new Vec3(
			X, (float) (Y * Math.cos(angle) - Z * Math.sin(angle)), (float) (Y * Math.sin(angle) + Z * Math.cos(angle))
		);
	}

	public Vec3 rotateX90() {
		return new Vec3(X, -Z, Y);
	}

	public Vec3 rotateY(float angle) {
		return new Vec3(
			(float) (X * Math.cos(angle) - Z * Math.sin(angle)), Y, (float) (X * Math.sin(angle) + Z * Math.cos(angle))
		);
	}

	public Vec3 rotateY90() {
		return new Vec3(-Z, Y, X);
	}

	public Vec3 rotateZ(float angle) {
		return new Vec3(
			(float) (X * Math.cos(angle) - Y * Math.sin(angle)), (float) (X * Math.sin(angle) + Y * Math.cos(angle)), Z
		);
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public Vec3 rotateZ90() {
		return new Vec3(-Y, X, Z);
	}
}
