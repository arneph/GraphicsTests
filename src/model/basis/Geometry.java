package model.basis;

public class Geometry {
	
	public static double round(double d) {
		return Math.round(d * 1000000000.0) / 1000000000.0;
	}
	
	public static double round(double d, int r) {
		return Math.round(d * Math.pow(10.0, r)) / Math.pow(10.0, r);
	}
	
}
