package faceless.artent.spells.api;

import org.joml.Vector3f;

public class SpellSettings {
    public final int type;
    public final int baseCost;
    public final int prepareCost;
    public final Vector3f color;
    public final float maxActionDistance;
    public final int cooldown;
    public final int prepareTime;
    public final int maxCastTime;

    public AffinityType affinityType;
    public boolean hasGravity;
    public float gravity;
    public float velocity;

    public SpellSettings(
      int type,
      int baseCost,
      int prepareCost,
      Vector3f color,
      float maxActionDistance,
      int cooldown,
      int prepareTime,
      int maxCastTime,
      AffinityType affinityType,
      boolean hasGravity,
      float gravity,
      float velocity
    ) {
        this.type = type;
        this.baseCost = baseCost;
        this.prepareCost = prepareCost;
        this.color = color;
        this.maxActionDistance = maxActionDistance;
        this.cooldown = cooldown;
        this.prepareTime = prepareTime;
        this.maxCastTime = maxCastTime;
        this.affinityType = affinityType;
        this.hasGravity = hasGravity;
        this.gravity = gravity;
        this.velocity = velocity;
    }


    public static SpellSettingsBuilder tick(int baseCost, int prepareCost) {
        return new SpellSettingsBuilder(ActionType.Tick).baseCost(baseCost).prepareCost(prepareCost);
    }

    public static SpellSettingsBuilder action(int baseCost) {
        return new SpellSettingsBuilder(ActionType.SingleCast).baseCost(baseCost);
    }

    public boolean isTickAction() {
        return (type & ActionType.Tick) > 0;
    }

    public boolean isSingleCastAction() {
        return (type & ActionType.SingleCast) > 0;
    }

    public static class SpellSettingsBuilder {
        private final int type;
        private int _baseCost = 10;
        private int _prepareCost;
        private Vector3f _color = new Vector3f(1, 1, 1);
        private float _maxActionDistance = 32;
        private int _cooldown = 20;
        private int _prepareTime = 3 * 20;
        private int _maxCastTime = 10 * 20;
        private AffinityType _affinityType = AffinityType.None;
        private boolean _hasGravity = false;
        private float _gravity = 0;
        private float _velocity = 2;

        private SpellSettingsBuilder(int type) {
            this.type = type;
        }

        public SpellSettings build() {
            return new SpellSettings(
              type,
              _baseCost,
              _prepareCost,
              _color,
              _maxActionDistance,
              _cooldown,
              _prepareTime,
              _maxCastTime,
              _affinityType,
              _hasGravity,
              _gravity,
              _velocity
            );
        }

        public SpellSettingsBuilder copy() {
            var copy = new SpellSettingsBuilder(type);

            copy._baseCost = _baseCost;
            copy._prepareCost = _prepareCost;
            copy._color = _color;
            copy._maxActionDistance = _maxActionDistance;
            copy._cooldown = _cooldown;
            copy._prepareTime = _prepareTime;
            copy._affinityType = _affinityType;
            copy._gravity = _gravity;
            copy._hasGravity = _hasGravity;
            copy._velocity = _velocity;
            return copy;
        }

        public SpellSettingsBuilder baseCost(int baseCost) {
            this._baseCost = baseCost;
            return this;
        }

        public SpellSettingsBuilder prepareCost(int prepareCost) {
            this._prepareCost = prepareCost;
            return this;
        }

        public SpellSettingsBuilder color(Vector3f color) {
            this._color = color;
            return this;
        }

        public SpellSettingsBuilder color(float red, float green, float blue) {
            this._color = new Vector3f(red, green, blue);
            return this;
        }

        public SpellSettingsBuilder maxActionDistance(float maxActionDistance) {
            this._maxActionDistance = maxActionDistance;
            return this;
        }

        public SpellSettingsBuilder cooldown(int cooldown) {
            this._cooldown = cooldown;
            return this;
        }

        public SpellSettingsBuilder prepareTime(int prepareTime) {
            this._prepareTime = prepareTime;
            return this;
        }

        public SpellSettingsBuilder maxCastTime(int maxCastTime) {
            this._maxCastTime = maxCastTime;
            return this;
        }

        public SpellSettingsBuilder affinity(AffinityType affinityType) {
            this._affinityType = affinityType;
            return this;
        }

        public SpellSettingsBuilder hasGravity(boolean hasGravity) {
            this._hasGravity = hasGravity;
            return this;
        }

        public SpellSettingsBuilder gravity(float gravity) {
            this._gravity = gravity;
            this._hasGravity = true;
            return this;
        }

        public SpellSettingsBuilder velocity(float velocity) {
            this._velocity = velocity;
            return this;
        }
    }

    public static class ActionType {
        public static final int Tick = 1;
        public static final int SingleCast = 2;
    }
}
