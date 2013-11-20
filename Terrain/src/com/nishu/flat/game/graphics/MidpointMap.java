package com.nishu.flat.game.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

public class MidpointMap {

	public static List<Line> newMap(int size, int passes, float topLimit, float bottomLimit) {
		List<Line> lines = new ArrayList<Line>();
		Random rand = new Random();

		float tL = topLimit;
		float bL = bottomLimit;

		lines.add(new Line(0, 0, size, 0));
		System.out.println(lines.size());

		for (int i = 0; i < lines.size(); i++) {
			float midPoint = lines.get(i).getMidPoint();
			float h = bL + (rand.nextFloat() * ((1 + tL) - bL));

			lines.add(new Line(lines.get(i).start.x, lines.get(i).start.y, lines.get(i).end.x + (midPoint + h), lines.get(i).end.y + (midPoint + h)));

			float reduceAmount = rand.nextFloat();
			if (reduceAmount > tL)
				reduceAmount = tL;
			if (reduceAmount < bL)
				reduceAmount = bL;
			tL -= reduceAmount;
			bL += reduceAmount;
		}
		return lines;
	}

	public static class Line {
		public Vector2f start, end, newVector;

		public Line(float startx, float starty, float endx, float endy) {
			this.start = new Vector2f(startx, starty);
			this.end = new Vector2f(endx, endy);
		}

		public Vector2f add() {
			newVector = new Vector2f();
			return Vector2f.add(start, end, newVector);
		}

		public float getLength() {
			newVector = add();
			return (float) Math.abs(Math.sqrt((newVector.x * newVector.x) + (newVector.y * newVector.y)));
		}

		public float getMidPoint() {
			return getLength() / 2;
		}
	}

}
