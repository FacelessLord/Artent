package faceless.artent.brewing.block;

public class BrewingCauldronCopper extends BrewingCauldron {
    public BrewingCauldronCopper(Settings settings) {
        super(settings);
    }

    @Override
    public String getId() {
        return "cauldron_copper";
    }
}