package com.nishu.flat.game.level;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import com.nishu.flat.game.Game;
import com.nishu.flat.game.graphics.HeightMap;
import com.nishu.flat.game.graphics.MidpointMap;
import com.nishu.flat.game.graphics.Shape;
import com.nishu.flat.game.graphics.utilities.Texture;
import com.nishu.shaderutils.Shader;
import com.nishu.shaderutils.ShaderProgram;

public class Level {

	private Texture mapLookup;
	private ShaderProgram geomProgram;
	private Game game;

	private int mapList;

	private float[][] data;
	private List<MidpointMap.Line> lines;

	private boolean flatten = false;

	public Level(Game game) {
		this.game = game;
		init();
	}

	public void init() {
		glPointSize(2);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		setupShaders();
		setupHeightMapFromImage();
	}

	private void setupShaders() {
		if (!game.render2D) {
			Shader geomShaders = new Shader("/landscape.vert", "/landscape.frag");
			geomProgram = new ShaderProgram(geomShaders.getvShader(), geomShaders.getfShader());

			glUniform1i(glGetUniformLocation(geomProgram.getProgram(), "lookup"), 0);
		}
	}

	private void setupHeightMapProcedural() {
		BufferedImage image = HeightMap.generate(1024, 1024, 1025, 255);
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

	private void setupVoxel() {
		BufferedImage image = HeightMap.generate(512, 512, 513, 255);
		data = new float[image.getHeight()][image.getWidth()];

		Color c;

		for (int z = 0; z < data.length; z++) {
			for (int x = 0; x < data[z].length; x++) {
				c = new Color(image.getRGB(z, x));
				data[z][x] = c.getRed();
			}
		}
		genVoxelGeom();
	}
	
	private void setupMidpoint(){
		lines = MidpointMap.newMap(1, 1, 1, 0);
		gen2DGeom();
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

	private void genGeom() {
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

	private void gen2DGeom() {
		mapList = glGenLists(1);
		
		glNewList(mapList, GL_COMPILE);
		
		glBegin(GL_LINE);
		for(int i = 0; i < lines.size(); i++){
			glVertex2f(lines.get(i).start.x, lines.get(i).start.y);
			glVertex2f(lines.get(i).end.x, lines.get(i).end.y);
		}
		glEnd();
		glEndList();
	}

	private void genVoxelGeom() {
		mapLookup = Texture.loadTexture("heightmap_lookup.png");
		mapLookup.bind();

		mapList = glGenLists(1);

		glNewList(mapList, GL_COMPILE);

		for (int z = 0; z < data.length - 1; z++) {
			glBegin(GL_QUADS);
			for (int x = 0; x < data[z].length; x++) {
				Shape.createShape(x, data[z][x], z, 1);
				Shape.createShape(x, data[z + 1][x], z + 1, 1);
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
				if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
					if(game.render2D) {
						game.render2D = false;
						game.initGL3D();
					}
					setupHeightMapProcedural();
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
					if(game.render2D) {
						game.render2D = false;
						game.initGL3D();
					}
					setupHeightMapFromImage();
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
					if(game.render2D) {
						game.render2D = false;
						game.initGL3D();
					}
					setupVoxel();
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
					if(!game.render2D) {
						//game.render2D = true;
						//game.initGL2D();
					}
					//setupMidpoint();
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
				if(Keyboard.isKeyDown(Keyboard.KEY_L)){
					glDisable(GL_LIGHTING);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_K)){
					glEnable(GL_LIGHTING);
				}
			}
		}
	}

	public void render() {
		if (flatten) {
			glScalef(1, 0, 1);
		}
		if (!game.render2D) {
			geomProgram.use();
		}
		glCallList(mapList);
		if (!game.render2D) {
			geomProgram.release();
		}
	}

	public void dispose() {
	}

}
