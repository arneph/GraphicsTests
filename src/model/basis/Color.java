package model.basis;

public class Color {
	private int r, g, b;
	
	public Color(int r, int g, int b) {
		if (r < 0) r = 0;
		if (r > 255) r = 255;
		if (g < 0) g = 0;
		if (g > 255) g = 255;
		if (b < 0) b = 0;
		if (b > 255) b = 255;
		
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int r() {
		return r;
	}
	
	public int g() {
		return g;
	}
	
	public int b() {
		return b;
	}
	
	public static Color average(Color a, Color b) {
		return new Color((a.r() + b.r()) / 2, 
		                 (a.g() + b.g()) / 2, 
		                 (a.b() + b.b()) / 2);
	}
	
}
