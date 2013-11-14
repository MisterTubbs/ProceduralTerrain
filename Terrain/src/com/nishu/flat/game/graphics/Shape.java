package com.nishu.flat.game.graphics;

import static org.lwjgl.opengl.GL11.*;

public class Shape {

	public static void createShape(float x, float y, float z, float size){
		glVertex3f(x, y, z + size);
		glVertex3f(x + size, y, z + size);
		glVertex3f(x + size, y, z);
		glVertex3f(x, y, z);

		// top face
		glVertex3f(x, y + size, z);
		glVertex3f(x + size, y + size, z);
		glVertex3f(x + size, y + size, z + size);
		glVertex3f(x, y + size, z + size);

		// front face
		glVertex3f(x, y, z);
		glVertex3f(x + size, y, z);
		glVertex3f(x + size, y + size, z);
		glVertex3f(x, y + size, z);

		// back face
		glVertex3f(x, y + size, z + size);
		glVertex3f(x + size, y + size, z + size);
		glVertex3f(x + size, y, z + size);
		glVertex3f(x, y, z + size);

		// left face
		glVertex3f(x + size, y, z);
		glVertex3f(x + size, y, z + size);
		glVertex3f(x + size, y + size, z + size);
		glVertex3f(x + size, y + size, z);

		// right face
		glVertex3f(x, y, z + size);
		glVertex3f(x, y, z);
		glVertex3f(x, y + size, z);
		glVertex3f(x, y + size, z + size);
	}
}
