package thebombzen.tanks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class RenderHelper {

	private static float nextHue = -1F;
	private static final float ONE_OVER_PHI = 0.618033988749895F;

	public static void drawCircle(Graphics2D g2, Vector center, int radius,
			Color color) {
		int x = center.getIntegerX();
		int y = center.getIntegerY();
		g2.setColor(color);
		g2.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}

	public static Color generateRandomColor(float saturation, float value) {
		if (nextHue == -1) {
			nextHue = Tanks.random.nextFloat();
		} else {
			nextHue += ONE_OVER_PHI;
			nextHue %= 1F;
		}
		return Color.getHSBColor(nextHue, saturation, value);
	}

	public static Color getInverse(Color color) {
		return new Color(color.getBlue(), color.getGreen(), color.getRed());
	}

	public static void setAntialias(Graphics2D g2, boolean enabled) {
		if (enabled) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	private RenderHelper() {

	}
}
