package faceless.artent.transmutations;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.util.math.random.Random;

import faceless.artent.api.Color;

public class Transmutation {
	public TransAction action;
	private String name;
	private TransTickAction tickAction;
	private RenderTickAction renderAction;
	private int prepTime = 40;
	private Color prepCol = new Color(60, 40, 255);
	private Color actCol = new Color(160, 140, 255);

	public Transmutation(String name, TransAction action) {
		this.name = name;
		this.action = action;
		this.setTickAction((entity, playerEntity, integer) -> true);
		this.setRenderAction((entity, playerEntity, integer) -> {
		});
	}

	public int getPrepTime() {
		return prepTime;
	}

	public Transmutation setPrepTime(int prepTime) {
		this.prepTime = prepTime;
		return this;
	}

	public Color getPreparationColor() {
		return prepCol;
	}

	public Transmutation setPrepCol(Color prepCol) {
		this.prepCol = prepCol;
		return this;
	}

	public Color getActionColor() {
		return actCol;
	}

	public Transmutation setActCol(Color actCol) {
		this.actCol = actCol;
		return this;
	}

	public RenderTickAction getRenderAction() {
		return renderAction;
	}

	public Transmutation setRenderAction(RenderTickAction renderAction) {
		this.renderAction = renderAction;
		return this;
	}

	public String getName() {
		return name;
	}

	public Transmutation setName(String name) {
		this.name = name;
		return this;
	}

	public TransTickAction getTickAction() {
		return tickAction;
	}

	public void setTickAction(TransTickAction tickAction) {
		this.tickAction = tickAction;
	}

	public boolean randomPoints(World world, int count, BlockPos center, int widthRange, int heightRange,
			BiFunction<BlockPos, BlockState, Boolean> action, Behaviour behaviour) {
		for (int i = 0; i < count; i++) {
			if (randomPoint(world, center, widthRange, heightRange, action) && behaviour == Behaviour.StopOnTrue)
				return true;
			else if (behaviour == Behaviour.StopOnFalse)
				return false;
		}
		return false;
	}

	public boolean randomPoint(World world, BlockPos center, int widthRange, int heightRange,
			BiFunction<BlockPos, BlockState, Boolean> action) {
		BlockPos pos = getNextPos(world.random, center, widthRange, heightRange);
		BlockState state = world.getBlockState(pos);
		return action.apply(pos, state);
	}

	public void randomPoints(World world, int count, BlockPos center, int widthRange, int heightRange,
			BiConsumer<BlockPos, BlockState> action) {
		for (int i = 0; i < count; i++) {
			randomPoint(world, center, widthRange, heightRange, action);
		}
	}

	public void randomPoint(World world, BlockPos center, int widthRange, int heightRange,
			BiConsumer<BlockPos, BlockState> action) {
		BlockPos pos = getNextPos(world.random, center, widthRange, heightRange);
		BlockState state = world.getBlockState(pos);
		action.accept(pos, state);
	}

	private BlockPos getNextPos(Random random, BlockPos center, int widthRange, int heightRange) {
		int k = random.nextInt(2 * heightRange + 1) - heightRange;
		int j = random.nextInt(2 * widthRange + 1) - widthRange;
		int i = random.nextInt(2 * widthRange + 1) - widthRange;

		return center.add(i, k, j);
	}

	public enum Behaviour {
		DoAll, StopOnTrue, StopOnFalse
	}
}
