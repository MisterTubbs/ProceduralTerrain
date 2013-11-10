package com.nishu.flat.game.level;

import static org.lwjgl.opengl.GL11.*;

import com.nishu.flat.game.graphics.Shape;

public class Level {
	
	public Level(){
		init();
	}

	public void init(){
	}

	public void update(){
	}

	public void render(){
		glBegin(GL_QUADS);
		Shape.createShape(0, 0, 0, 512);
		glEnd();
	}

	public void dispose(){
	}
	
}
