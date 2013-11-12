package com.nishu.flat.game.level;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_POINT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_MODE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;

import com.nishu.flat.game.graphics.HeightMap;
import com.nishu.flat.game.graphics.utilities.Texture;
import com.nishu.shaderutils.Shader;
import com.nishu.shaderutils.ShaderProgram;

public class Level {

	private Texture mapLookup;
	private ShaderProgram geomProgram;

	private int mapList;

	private float[][] data;

	private boolean flatten = false;
	private boolean typeMap = false;

	public Level() {
		init();
	}

	public void init() {
		glPointSize(2);
		setupShaders();
		if (typeMap) {
			setupHeightMapProcedural();
		}
		if (!typeMap) {
			setupHeightMapFromImage();
		}
	}

	private void setupShaders() {
		Shader geomShaders = new Shader("/landscape.vert", "/landscape.frag");
		geomProgram = new ShaderProgram(geomShaders.getvShader(), geomShaders.getfShader());

		glUniform1i(glGetUniformLocation(geomProgram.getProgram(), "lookup"), 0);
	}
	
	private void setupHeightMapProcedural(){
		BufferedImage image = HeightMap.generate(1024, 1024, 2049, 255);
		data = new float[image.getHeight()][image.getWidth()];
		
		Color c;
		
		for (int z = 0; z < data.length; z++) {
			for (int x = 0; x < data[z].length; x++) {
				c = new Color(image.getRGB(z, x));
				data[z][x] = c.getRed();
			}
		}
		genGeom();
	}

	private void setupHeightMapFromImage() {
		BufferedImage image;
		try {
			image = ImageIO.read(Level.class.getClassLoader().getResourceAsStream("heightmap.bmp"));
			data = new float[image.getWidth()][image.getHeight()];

			Color c;

			for (int z = 0; z < data.length; z++) {
				for (int x = 0; x < data[z].length; x++) {
					c = new Color(image.getRGB(z, x));
					data[z][x] = c.getRed();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		genGeom();
	}
	
	private void genGeom(){
		mapLookup = Texture.loadTexture("heightmap_lookup.png");
		mapLookup.bind();

		mapList = glGenLists(1);

		glNewList(mapList, GL_COMPILE);
		glScalef(1f, 1f, 1f);

		for (int z = 0; z < data.length - 1; z++) {
			glBegin(GL_TRIANGLE_STRIP);
			for (int x = 0; x < data[z].length; x++) {
				glVertex3f(x, data[z][x], z);
				glVertex3f(x, data[z + 1][x], z + 1);
			}
			glEnd();
		}
		glEndList();
	}

	public void update() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
					flatten = !flatten;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_R)){
					typeMap = true;
					setupHeightMapProcedural();
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_H)){
					typeMap = false;
					setupHeightMapFromImage();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_P) {
					int mode = glGetInteger(GL_POLYGON_MODE);
					if (mode == GL_LINE) {
						glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
					} else if (mode == GL_FILL) {
						glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
					} else if (mode == GL_POINT) {
						glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
					}
				}
			}
		}
	}

	public void render() {
		if (flatten) {
			glScalef(1, 0, 1);
		}
		geomProgram.use();
		glCallList(mapList);
	}

	public void dispose() {
	}

}
