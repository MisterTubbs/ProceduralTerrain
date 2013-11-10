package com.nishu.flat;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	
	public static void creatWindow(int width, int height){
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Flat Terrain");
			Mouse.setGrabbed(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isClosed(){
		return Display.isCloseRequested();
	}
	
	public static void update(){
		Display.update();
	}
	
	public static void dispose(){
		Display.destroy();
	}

}
