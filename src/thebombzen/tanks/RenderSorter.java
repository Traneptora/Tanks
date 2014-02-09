package thebombzen.tanks;

import java.util.Comparator;

import thebombzen.tanks.object.property.Renderable;

public class RenderSorter implements Comparator<Renderable> {

	private static final RenderSorter renderSorter = new RenderSorter();

	public static RenderSorter getRenderSorter() {
		return renderSorter;
	}

	private RenderSorter() {

	}

	@Override
	public int compare(Renderable renderable1, Renderable renderable2) {
		return Integer.valueOf(renderable1.getRenderLayer()).compareTo(
				renderable2.getRenderLayer());
	}

}
