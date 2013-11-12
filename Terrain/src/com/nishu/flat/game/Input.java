package com.nishu.flat.game;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.nishu.flat.Time;

public class Input {

	private float yrot, xrot, lookSpeed = 0.09f, strafeSpeed = 80f;
	private Vector2f mouseD;
	private Vector3f rot, pos;

	public Input(float x, float y, float z) {
		this.pos = new Vector3f(x, y, z);
		this.rot = new Vector3f(0, 0, 0);
		this.mouseD = new Vector2f(0, 0);
	}

	public void update() {
		if (Mouse.isGrabbed()) {
			mouseD.x = Mouse.getDX();
			mouseD.y = -Mouse.getDY();
		}

		rot.set(rot.x += (float) mouseD.y * lookSpeed, rot.y += (float) mouseD.x * lookSpeed, 0);

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			yrot = (float) (rot.y / 180 * 3.141592654f);
			pos.set(pos.x -= ((float) (Math.cos(yrot)) * strafeSpeed) * Time.getDelta(), pos.y, pos.z -= ((float) (Math.sin(yrot) * strafeSpeed) * Time.getDelta()));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			yrot = (float) (rot.y / 180 * 3.141592654f);
			pos.set(pos.x += ((float) (Math.cos(yrot)) * strafeSpeed) * Time.getDelta(), pos.y, pos.z += ((float) (Math.sin(yrot) * strafeSpeed) * Time.getDelta()));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			yrot = (float) (rot.y / 180 * 3.141592654f);
			xrot = ((float) (rot.x / 180 * 3.141592654f));
			pos.set(pos.x += ((float) (Math.sin(yrot)) * strafeSpeed) * Time.getDelta(), pos.y -= ((float) (Math.sin(xrot)) * strafeSpeed) * Time.getDelta(), pos.z -= ((float) (Math.cos(yrot) * strafeSpeed) * Time.getDelta()));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			yrot = (float) (rot.y / 180 * 3.141592654f);
			xrot = (float) (rot.x / 180 * 3.141592654f);
			pos.set(pos.x -= ((float) (Math.sin(yrot)) * strafeSpeed) * Time.getDelta(), pos.y += ((float) (Math.sin(xrot)) * strafeSpeed) * Time.getDelta(), pos.z += ((float) (Math.cos(yrot) * strafeSpeed) * Time.getDelta()));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			pos.setY(pos.y -= strafeSpeed * Time.getDelta());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			pos.setY(pos.y += strafeSpeed * Time.getDelta());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}

	public void render() {
		if (rot.x < -45) {
			rot.setX(-45);
		}
		if (rot.x > 90) {
			rot.setX(90);
		}
		
		glRotatef(rot.x, 1, 0, 0);
		glRotatef(rot.y, 0, 1, 0);
		glTranslatef(pos.x, pos.y, pos.z);
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

}
