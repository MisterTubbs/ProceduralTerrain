package com.nishu.flat.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.opengl.Display;

import com.nishu.flat.Main;
import com.nishu.flat.game.level.Level;

public class Game {
	
	private Level level;
	private Input input;
	
	public Game(){
	}

	public void initGL(){
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		gluPerspective((float) 67, Main.WIDTH / Main.HEIGHT, 0.001f, 10000f);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public void init(){
		input = new Input(-70, -13, -50);
		level = new Level();
		level.init();
	}

	public void update(){
		input.update();
		level.update();
	}

	public void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0, 0.75f, 1, 1);
		input.render();
		level.render();
		
		glLoadIdentity();
	}

	public void dispose(){
	}
	
}
