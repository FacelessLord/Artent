package faceless.artent.brewing.ingridients;

public class Ingredients {
	public static String GetBerryName(int type) {
		return switch (type) {
			case 0 -> "blackberry";
			case 1 -> "blueberry";
			case 2 -> "cloudberry";
			case 3 -> "raspberry";
			case 4 -> "crimsonwood_berry";
			default -> null;
		};
	}
}