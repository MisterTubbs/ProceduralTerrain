package com.nishu.flat.game.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class HeightMap {

	public static Random rand = new Random();

	public static BufferedImage generate(int width, int height, int DATA_SIZE, int h) {
		final double SEED = rand.nextInt(100) + 1;
		double[][] data = new double[DATA_SIZE][DATA_SIZE];
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		data[0][0] = data[0][DATA_SIZE - 1] = data[DATA_SIZE - 1][0] = data[DATA_SIZE - 1][DATA_SIZE - 1] = SEED;

		for (int sideLength = DATA_SIZE - 1; sideLength >= 2; sideLength /= 2, h /= 2.0) {
			int halfSide = sideLength / 2;

			for (int x = 0; x < DATA_SIZE - 1; x += sideLength) {
				for (int z = 0; z < DATA_SIZE - 1; z += sideLength) {
					double avg = data[x][z] + data[x + sideLength][z] + data[x][z + sideLength] + data[x + sideLength][z + sideLength];
					avg /= 4.0;

					data[x + halfSide][z + halfSide] = avg + (rand.nextDouble() * 2 * h) - h;
					if (data[x + halfSide][z + halfSide] > 255)
						data[x + halfSide][z + halfSide] = 255;
					if (data[x + halfSide][z + halfSide] < 0)
						data[x + halfSide][z + halfSide] = 0;
				}
			}

			for (int x = 0; x < DATA_SIZE - 1; x += halfSide) {
				for (int z = (x + halfSide) % sideLength; z < DATA_SIZE - 1; z += sideLength) {
					double avg = data[(x - halfSide + DATA_SIZE - 1) % (DATA_SIZE - 1)][z] + data[(x + halfSide) % (DATA_SIZE - 1)][z] + data[x][(z + halfSide) % (DATA_SIZE - 1)] + data[x][(z - halfSide + DATA_SIZE - 1) % (DATA_SIZE - 1)];
					avg /= 4.0;

					avg = avg + (rand.nextDouble() * 2 * h) - h;

					data[x][z] = avg;

					if (x == 0)
						data[DATA_SIZE - 1][z] = avg;
					if (z == 0)
						data[x][DATA_SIZE - 1] = avg;
					if (data[x][z] > 255)
						data[x][z] = 255;
					if (data[x][z] < 0)
						data[x][z] = 0;
				}
			}
		}
		
		for(int x = 0; x < width; x++){
			for(int z = 0; z < height; z++){
				image.setRGB(x, z, new Color((int) data[x][z], (int) data[x][z], (int) data[x][z]).getRGB());
			}
		}

		try {
			ImageIO.write(image, "bmp", new File("image.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

}
