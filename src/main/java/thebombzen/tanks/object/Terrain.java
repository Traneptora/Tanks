package thebombzen.tanks.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import thebombzen.tanks.Constants;
import thebombzen.tanks.PerlinNoiseGenerator;
import thebombzen.tanks.RenderHelper;
import thebombzen.tanks.Tanks;
import thebombzen.tanks.Vector;
import thebombzen.tanks.object.property.Renderable;

public class Terrain implements Renderable {

	private static final Terrain terrain = new Terrain();

	public static Terrain getTerrain() {
		return terrain;
	}

	private int[] originalHeightMap = new int[Constants.WIDTH];

	private PerlinNoiseGenerator terrainGenerator;
	private boolean[][] terrainMap = new boolean[Constants.WIDTH][Constants.HEIGHT];
	private BufferedImage terrainImage = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration()
			.createCompatibleImage(Constants.WIDTH, Constants.HEIGHT);
	private Graphics2D terrainGraphics = terrainImage.createGraphics();

	private Terrain() {

	}

	public boolean doesTerrainExistAt(Vector position) {
		return position.getIntegerX() >= 0
				&& position.getIntegerX() < Constants.WIDTH
				&& position.getIntegerY() >= 0
				&& position.getIntegerY() < Constants.HEIGHT
				&& terrainMap[position.getIntegerX()][position.getIntegerY()];
	}

	public Vector getInnerImpactLocation(Vector insidePosition,
			Vector outsidePosition) {
		Vector displacement = insidePosition.subtract(outsidePosition);
		if (doesTerrainExistAt(insidePosition)
				&& doesTerrainExistAt(outsidePosition)) {
			return insidePosition;
		}
		if (!doesTerrainExistAt(insidePosition)
				&& !doesTerrainExistAt(outsidePosition)) {
			return outsidePosition;
		}

		double distance = displacement.getNorm();
		Vector finalPosition = insidePosition;

		for (double partialDistance = 0D; partialDistance <= distance; partialDistance++) {
			Vector testPosition = insidePosition.subtract(displacement
					.setLength(partialDistance));
			if (!doesTerrainExistAt(testPosition)) {
				break;
			} else {
				finalPosition = testPosition;
			}
		}

		return finalPosition;

	}

	public int getOriginalHeightValue(int x) {
		return originalHeightMap[x];
	}

	public Vector getOuterImpactLocation(Vector insidePosition,
			Vector outsidePosition) {
		Vector displacement = insidePosition.subtract(outsidePosition);
		if (doesTerrainExistAt(insidePosition)
				&& doesTerrainExistAt(outsidePosition)) {
			return insidePosition;
		}
		if (!doesTerrainExistAt(insidePosition)
				&& !doesTerrainExistAt(outsidePosition)) {
			return outsidePosition;
		}

		double distance = displacement.getNorm();
		Vector finalPosition = insidePosition;

		for (double partialDistance = 0D; partialDistance <= distance; partialDistance++) {
			finalPosition = insidePosition.subtract(displacement
					.setLength(partialDistance));
			if (!doesTerrainExistAt(finalPosition)) {
				break;
			}
		}

		return finalPosition;

	}

	@Override
	public int getRenderLayer() {
		return -1000;
	}

	public Vector getSurfaceNormalVector(Vector position) {
		return getSurfaceNormalVector(position, 10);
	}

	public Vector getSurfaceNormalVector(Vector position, int searchDistance) {

		Vector normal = Vector.ZERO;

		for (int diffX = -searchDistance; diffX <= searchDistance; diffX++) {
			for (int diffY = -searchDistance; diffY <= searchDistance; diffY++) {
				Vector diff = new Vector(diffX, diffY);
				Vector fin = position.add(diff);
				if (doesTerrainExistAt(fin)) {
					normal = normal.subtract(diff);
				}
			}
		}

		return normal;
	}

	@Override
	public void render(Graphics2D g2) {
		g2.drawImage(terrainImage, 0, 0, null);
	}

	public void reset() {
		Arrays.fill(originalHeightMap, 0);
		terrainGenerator = new PerlinNoiseGenerator();
		terrainGenerator.generateNoise(originalHeightMap, Tanks.random, 0.4D);
		for (int x = 0; x < Constants.WIDTH; x++) {
			Arrays.fill(terrainMap[x], 0, getOriginalHeightValue(x), false);
			Arrays.fill(terrainMap[x], getOriginalHeightValue(x),
					Constants.HEIGHT, true);
		}
		RenderHelper.setAntialias(terrainGraphics, false);
		terrainGraphics.setBackground(Color.WHITE);
		terrainGraphics.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		terrainGraphics.setColor(Color.BLACK);
		for (int x = 0; x < Constants.WIDTH; x++) {
			terrainGraphics.drawLine(x, getOriginalHeightValue(x), x,
					Constants.HEIGHT);
		}
		RenderHelper.setAntialias(terrainGraphics, true);
		for (int x = 0; x < Constants.WIDTH - 1; x++) {
			terrainGraphics.drawLine(x, getOriginalHeightValue(x), x + 1,
					getOriginalHeightValue(x + 1));
		}
	}

	public void setTerrainAroundRadius(Vector position, double radius,
			boolean value) {
		for (double i = -radius; i <= radius; i++) {
			for (double j = -radius; j <= radius; j++) {
				Vector diff = new Vector(i, j);
				if (diff.getNormSquared() <= radius * radius) {
					setTerrainExistsAt(position.add(diff), value);
				}
			}
		}
		RenderHelper.setAntialias(terrainGraphics, true);
		terrainGraphics.setColor(value ? Color.BLACK : Color.WHITE);
		terrainGraphics.fillOval((int) (position.getX() - radius),
				(int) (position.getY() - radius), (int) (radius * 2D),
				(int) (radius * 2D));

	}

	public void setTerrainExistsAt(Vector position, boolean terrainExists) {
		int x = position.getIntegerX();
		int y = position.getIntegerY();
		if (x < 0 || x >= Constants.WIDTH || y < 0 || y >= Constants.HEIGHT) {
			return;
		}
		terrainMap[x][y] = terrainExists;
	}

}
