package faceless.artent.transmutations.api;

import faceless.artent.api.DirectionUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

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
		this.setTickAction((facing, entity, playerEntity, integer) -> true);
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

	public void setPrepCol(Color prepCol) {
		this.prepCol = prepCol;
	}

	public Color getActionColor() {
		return actCol;
	}

	public void setActCol(Color actCol) {
		this.actCol = actCol;
	}

	public RenderTickAction getRenderAction() {
		return renderAction;
	}

	public void setRenderAction(RenderTickAction renderAction) {
		this.renderAction = renderAction;
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

	public boolean randomPoints(World world, int count, BlockPos center, Direction facing, int widthRange, int heightRange,
								BiFunction<BlockPos, BlockState, Boolean> action, Behaviour behaviour) {
		for (int i = 0; i < count; i++) {
			var pointResult = randomPoint(world, center, facing, widthRange, heightRange, action);
			if (pointResult && behaviour == Behaviour.StopOnTrue)
				return true;
			else if (!pointResult && behaviour == Behaviour.StopOnFalse)
				return false;
		}
		return false;
	}

	public boolean randomPoint(World world, BlockPos center, Direction facing, int widthRange, int heightRange,
							   BiFunction<BlockPos, BlockState, Boolean> action) {
		BlockPos pos = getNextPos(world.random, center, facing, widthRange, heightRange);
		BlockState state = world.getBlockState(pos);
		return action.apply(pos, state);
	}

	private BlockPos getNextPos(Random random, BlockPos center, Direction facing, int widthRange, int heightRange) {
		int k = random.nextInt(2 * heightRange + 1) - heightRange;
		int j = random.nextInt(2 * widthRange + 1) - widthRange;
		int i = random.nextInt(2 * widthRange + 1) - widthRange;
		var rotated = DirectionUtils.applyDirection(new int[]{ i, k, j }, facing);
		return center.add(rotated[0], rotated[1], rotated[2]);
	}

	public enum Behaviour {
		DoAll, StopOnTrue, StopOnFalse
	}
}
