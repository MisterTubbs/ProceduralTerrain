package com.nishu.flat.game.graphics;

import static org.lwjgl.opengl.GL11.*;

public class Shape {

	public static void createShape(float x, float y, float z, float size){
		glColor3f(0, 0, 1);
		glVertex3f(x, y, z);
		glColor3f(0, 1, 1);
		glVertex3f(x + size, y, z);
		glColor3f(1, 0, 1);
		glVertex3f(x + size, y, z + size);
		glColor3f(1, 1, 0);
		glVertex3f(x, y, z + size);
	}
}
