package model.geometry2D;

import java.util.*;

import model.basis.*;

public class Polygon2D {
	private Vector2D[] points;
	private Triangle2D[] triangles;
	
	public Polygon2D(Vector2D[] points) {
		if (points == null || points.length < 3) {
			throw new IllegalArgumentException();
		}
		
		int n = points.length;
		
		for (int i = 0; i < n; i++) {
			int j = (i + 1) % points.length;
			
			Vector2D a = points[i];
			Vector2D b = points[j];
			
			if (!Vector2D.equal(a, b)) {
				continue;
			}
			
			if (j > 0) {
				System.arraycopy(points, j, 
				                 points, i, 
				                 n - j);
			}
			points = Arrays.copyOf(points, n - 1);
			n--;
			i--;
			
			if (n < 3) {
				throw new IllegalArgumentException();
			}
		}
		
		for (int i = 0; i < points.length; i++) {
			int j = (i + 1) % points.length;
			int k = (i + 2) % points.length;
			
			Vector2D a = points[i];
			Vector2D b = points[j];
			Vector2D c = points[k];
			
			if (!Vector2D.collinear(Vector2D.subtract(b, a), 
				                       Vector2D.subtract(c, a))) {
				continue;
			}
			
			if (k > 0) {
				System.arraycopy(points, k, 
				                 points, j, 
				                 n - k);
			}
			points = Arrays.copyOf(points, n - 1);
			n--;
			i--;
			
			if (n < 3) {
				throw new IllegalArgumentException();
			}
		}
		
		this.points = points;
		this.triangles = null;
	}
	
	public int n() {
		return points.length;
	}
	
	public Vector2D point(int i) {
		if (i < 0 || i >= points.length) {
			throw new IllegalArgumentException();
		}
		
		return points[i];
	}

	public Vector2D[] points() {
		return points;
	}
	
	public Vector2D center() {
		return Vector2D.average(points);
	}
	
	public Vector2D edge(int i) {
		if (i < 0 || i >= points.length) {
			throw new IllegalArgumentException();
		}
		
		int j = (i + 1) % points.length;
		
		return Vector2D.subtract(points[j], points[i]);
	}
	
	public Vector2D[] edges() {
		Vector2D[] edges = new Vector2D[points.length];
		
		for (int i = 0; i < points.length; i++) {
			int j = (i + 1) % points.length;
			
			Vector2D a = points[i];
			Vector2D b = points[j];
			
			edges[i] = Vector2D.subtract(b, a);
		}
		
		return edges;
	}
	
	public double angle(int i) {
		if (i < 0 || i >= points.length) {
			throw new IllegalArgumentException();
		}
		
		int n = points.length;
		int h = (i + n - 1) % n;
		int j = (i + 1) % n;
		
		Vector2D a = Vector2D.subtract(points[h], points[i]);
		Vector2D b = Vector2D.subtract(points[j], points[i]);
		
		return Vector2D.angle(a, b);
	}
	
	public double[] angles() {
		int n = points.length;
		double[] angles = new double[n];
		
		for (int i = 0; i < n; i++) {
			int h = (i + n - 1) % n;
			int j = (i + 1) % n;
			
			Vector2D a = Vector2D.subtract(points[h], points[i]);
			Vector2D b = Vector2D.subtract(points[j], points[i]);
			
			angles[i] = Vector2D.angle(a, b);
		}
		
		return angles;
	}
	
	public double area() {
		double s = 0.0;
		
		for (int i = 0; i < points.length; i++) {
			int j = (i + 1) % points.length;
			
			Vector2D a = points[i];
			Vector2D b = points[j];
			
			s += a.x1() * b.x2() - a.x2() * b.x1();
		}
		
		return Geometry.round(Math.abs(s / 2.0));
	}
	
	public int orientation() {
		double s = 0.0;
		
		for (int i = 0; i < points.length; i++) {
			int j = (i + 1) % points.length;
			
			Vector2D a = points[i];
			Vector2D b = points[j];
			
			s += a.x1() * b.x2() - a.x2() * b.x1();
		}
		
		double signedArea = Geometry.round(s / 2.0);
		
		if (signedArea < 0.0) {
			return -1;
		}else if (signedArea > 0.0) {
			return +1;
		}else{
			return 0;
		}
	}
	
	public boolean contains(Vector2D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		prepareTriangles();
		
		for (Triangle2D t : triangles) {
			if (t.contains(r)) {
				return true;
			}
		}
		
		return false;
	}
	
	public double distance(Vector2D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		prepareTriangles();
		
		for (Triangle2D t : triangles) {
			if (t.contains(r)) return 0.0;
		}
		
		double d = Double.NaN;
		
		for (int i = 0; i < points.length; i++) {
			int j = (i + 1) % points.length;
			
			Line2D l = new Line2D(points[i], points[j]);
			
			double t = l.t(r);
			
			if (t < 0.0) t = 0.0;
			if (t > 1.0) t = 1.0;
			
			double e = Vector2D.distance(l.p(t), r);
			
			if (Double.isNaN(d) || 
				d > e) {
				d = e;
			}
		}
		
		return d;
	}
	
	public Triangle2D[] triangles() {
		prepareTriangles();
		
		return triangles;
	}
	
	private void prepareTriangles() {
		if (triangles != null) {
			return;
		}
		
		triangles = new Triangle2D[0];
		
		int n = points.length;
		Vector2D[] pts = Arrays.copyOf(points, n);
		
		while (n > 3) {
			for (int i = 0; i < n; i++) {
				int j = (i + 1) % n;
				int k = (i + 2) % n;
				
				Vector2D a = pts[i];
				Vector2D b = pts[j];
				Vector2D c = pts[k];
				
				Triangle2D t = new Triangle2D(a, b, c);
				
				if (t.orientation() != orientation()) {
					continue;
				}
				
				for (int l = 0; l < n; l++) {
					if (l == i || l == j || l == k) continue;
					
					Vector2D d = pts[l];
					
					if (t.contains(d)) {
						t = null;
						break;
					}
				}
				
				if (t == null) {
					continue;
				}
				
				if (k > 0) {
					System.arraycopy(pts, k, 
					                 pts, j, 
					                 n - k);
				}
				points = Arrays.copyOf(pts, n - 1);
				n--;
				i--;
				
				triangles = Arrays.copyOf(triangles, triangles.length + 1);
				triangles[triangles.length - 1] = t;
				
				break;
			}
		}
		
		triangles = Arrays.copyOf(triangles, triangles.length + 1);
		triangles[triangles.length - 1] = new Triangle2D(pts);
	}
	
	public static boolean equal(Polygon2D a, Polygon2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		if (a.n() != b.n()) {
			return false;
		}
		
		int n = a.n();
		int o = -1;
		
		Vector2D p = a.point(0);
		for (int i = 0; i < n; i++) {
			Vector2D q = b.point(i);
			
			if (Vector2D.equal(p, q)) {
				o = i;
				break;
			}
		}
		
		if (o == -1) return false;
		
		boolean d1 = true;
		boolean d2 = true;
		
		for (int i = 1; i < n; i++) {
			Vector2D q = a.point(i);
			Vector2D r = b.point((i + o) % n);
			Vector2D s = b.point((n + o - i) % n);
			
			if (!Vector2D.equal(q, r)) d1 = false;
			if (!Vector2D.equal(q, s)) d2 = false;
		}
		
		return d1 || d2;
	}
	
	public static Triangle2D[] intersection(Polygon2D a, Polygon2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Triangle2D.intersection(a.triangles(), b.triangles());
	}
	
	public static Polygon2D polygonFromTriangle(Triangle2D t) {
		if (t == null) {
			throw new IllegalArgumentException();
		}
		
		return new Polygon2D(new Vector2D[] {t.a(), t.b(), t.c()});
	}
	
}
