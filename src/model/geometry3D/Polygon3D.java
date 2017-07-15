package model.geometry3D;

import java.util.Arrays;

import model.basis.*;

public class Polygon3D {
	private Vector3D[] points;
	private Color color;
	
	public Polygon3D(Vector3D a, Vector3D b, Vector3D c, Color color) {
		if (a == null || b == null || c == null || color == null) {
			throw new IllegalArgumentException();
		}
		
		this.points = new Vector3D[] {a, b, c};
		this.color = color;
	}
	
	public Polygon3D(Vector3D[] points, Color color) {
		if (points == null || points.length < 3 || color == null) {
			throw new IllegalArgumentException();
		}
		
		if (points.length > 3) {
			Vector3D v0 = points[0];
			
			Vector3D[] s = new Vector3D[points.length - 1];
			
			for (int i = 1; i < points.length; i++) {
				Vector3D vi = points[i];
				
				s[i - 1] = Vector3D.subtract(vi, v0);
			}
			
			Matrix m = Vector3D.matrixWithColumnVectors(s);
			
			if (m.rank() > 2) {
				throw new IllegalArgumentException();
			}
		}
		
		this.points = points;
		this.color = color;
	}
	
	public int getNumberOfPoints() {
		return points.length;
	}
	
	public Vector3D getPointAtIndex(int index) {
		if (index < 0 || index >= points.length) {
			throw new IllegalArgumentException();
		}
		
		return points[index];
	}
	
	public Vector3D[] getPoints() {
		return points;
	}
	
	public Vector3D getCenter() {
		double minX = Double.NaN;
		double maxX = Double.NaN;
		double minY = Double.NaN;
		double maxY = Double.NaN;
		double minZ = Double.NaN;
		double maxZ = Double.NaN;
		
		for (Vector3D v : points) {
			double x = v.x1();
			double y = v.x2();
			double z = v.x3();
			
			if (Double.isNaN(minX) || 
				minX > x) {
				minX = x;
			}
			if (Double.isNaN(maxX) || 
				maxX < x) {
				maxX = x;
			}
			
			if (Double.isNaN(minY) || 
				minY > y) {
				minY = y;
			}
			if (Double.isNaN(maxY) || 
				maxY < y) {
				maxY = y;
			}
				
			if (Double.isNaN(minZ) || 
				minZ > z) {
				minZ = z;
			}
			if (Double.isNaN(maxZ) || 
				maxZ < z) {
				maxZ = z;
			}
		}
		
		double cx = minX + (maxX - minX) / 2;
		double cy = minY + (maxY - minY) / 2;
		double cz = minZ + (maxZ - minZ) / 2;
		
		return new Vector3D(cx, cy, cz);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Vector3D getNormalVector() {
		Vector3D v0 = points[0];
		
		double[][] mv = new double[points.length][3];
		
		for (int i = 1; i < points.length; i++) {
			Vector3D vi = points[i];
			
			mv[i] = Vector3D.subtract(vi, v0).x();
		}
		
		Matrix m = new Matrix(mv);
		
		double[][] bv = m.basisForRowSpace();
		
		if (bv.length == 2) {
			Vector3D a = new Vector3D(bv[0]);
			Vector3D b = new Vector3D(bv[1]);
			
			return Vector3D.crossProduct(a, b);
			
		}else{
			Vector3D a = new Vector3D(bv[0]);
			Vector3D b = !Vector3D.collinear(a, Vector3D.i) ? Vector3D.i : Vector3D.j;
			
			return Vector3D.crossProduct(a, b);
		}
	}
	
	public Plane3D getPlane() {
		Vector3D v0 = points[0];
		Vector3D n = getNormalVector();
		
		return new Plane3D(v0, n);
	}
	
	public Triangle3D[] getTriangles() {
		int p = points.length;
		Vector3D[] ps = points.clone();
		
		int t = 0;
		Triangle3D[] ts = new Triangle3D[0];
		
		while (ps.length > 3) {
			for (int i = 0; i < p; i++) {
				Vector3D a = ps[i];
				Vector3D b = ps[(i + 1) % p];
				Vector3D c = ps[(i + 2) % p];
				
				Triangle3D e = new Triangle3D(a, b, c, color);
				
				for (int j = 0; j < p; j++) {
					Vector3D d = ps[j];
					
					if (d == a || d == b || d == c) {
						continue;
					}
					
					if (e.contains(d)) {
						e = null;
						break;
					}
				}
				
				if (e != null) {
					t++;
					ts = Arrays.copyOf(ts, t);
					ts[t - 1] = e;
					
					if (i + 2 < p) {
						System.arraycopy(ps, i + 2, 
						                 ps, i + 1, 
						                 p - i - 2);
					}else if (i + 1 == p) {
						System.arraycopy(ps, 1, 
						                 ps, 0, p - 1);
					}
					p--;
					
					break;
				}
			}
		}
		
		t++;
		ts = Arrays.copyOf(ts, t);
		ts[t - 1] = new Triangle3D(ps, color);
		
		return ts;
	}
	
	public static Triangle3D[] triangles(Polygon3D[] ps) {
		Triangle3D[] ts = new Triangle3D[0];
		
		for (Polygon3D p : ps) {
			Triangle3D[] t = p.getTriangles();
			
			int n = ts.length;
			int m = t.length;
			
			ts = Arrays.copyOf(ts, n + m);
			System.arraycopy(t, 0, ts, n, m);
		}
		
		return ts;
	}
	
	public static Polygon3D[] prism(Polygon3D base, Vector3D extension) {
		int n = base.getNumberOfPoints();
		
		Vector3D[] basePoints = base.getPoints();
		Vector3D[] coverPoints = new Vector3D[n];
		
		for (int i = 0; i < n; i++) {
			coverPoints[i] = Vector3D.add(basePoints[i], extension);
		}
		
		Polygon3D[] prism = new Polygon3D[2 + n];
		
		prism[0] = base;
		
		for (int i = 0; i < n; i++) {
			Vector3D b1 = basePoints[i];
			Vector3D b2 = basePoints[(i + 1) % n];
			
			Vector3D c1 = coverPoints[i];
			Vector3D c2 = coverPoints[(i + 1) % n];
			
			Polygon3D side = new Polygon3D(new Vector3D[] {b1, b2, c2, c1}, 
			                               new Color(255, 127, 0));
			
			prism[i + 1] = side;
		}
		
		prism[n + 1] = new Polygon3D(coverPoints, new Color(0, 255, 127));
		
		return prism;
	}
	
}
