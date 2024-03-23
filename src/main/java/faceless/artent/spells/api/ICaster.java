package faceless.artent.spells.api;

import java.util.UUID;

public interface ICaster {
	boolean consumeMana(int mana);

	UUID getCasterUuid();
}
