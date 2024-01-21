package faceless.artent.brewing.api;

import net.minecraft.item.Item;

import java.util.Objects;

public record BrewingIngredient(Item item, int meta) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrewingIngredient that = (BrewingIngredient) o;
        return meta == that.meta && item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, meta);
    }
}