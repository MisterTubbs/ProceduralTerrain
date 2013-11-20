package com.nishu.flat;

import org.lwjgl.input.Keyboard;

import com.nishu.flat.game.Game;

public class Main {

	private Game game;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final double FRAME_CAP = 60.0;

	private boolean isRunning = false;

	public Main() {
		isRunning = false;
		game = new Game();

		init();
	}

	private void init() {
		if (!game.render2D) {
			game.initGL3D();
		}else if(game.render2D){
			game.initGL2D();
		}
		game.init();
	}

	private void start() {
		if (isRunning)
			return;
		isRunning = true;
		run();
	}

	private void run() {
		isRunning = true;

		int frames = 0;
		int frameCounter = 0;

		final double frameTime = 1.0 / FRAME_CAP;

		long lastTime = Time.getTime();
		double unprocessed = 0;

		while (isRunning) {
			boolean render = false;
			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessed += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;

			while (unprocessed > frameTime) {
				render = true;
				unprocessed -= frameTime;

				if (Window.isClosed() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
					stop();
				}

				Time.setDelta(frameTime);

				update();

				if (frameCounter >= Time.SECOND) {
					System.out.println("FPS: " + frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		dispose();
	}

	public void update() {
		Window.update();
		game.update();
	}

	public void render() {
		game.render();
	}

	private void stop() {
		if (!isRunning)
			return;
		isRunning = false;
	}

	private void dispose() {
		Window.dispose();
		game.dispose();
	}

	public static void main(String[] args) {
		Window.creatWindow(WIDTH, HEIGHT);
		Main main = new Main();
		main.start();
	}
}
