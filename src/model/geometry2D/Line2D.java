package model.geometry2D;

import model.basis.Matrix;

public class Line2D {
	private Vector2D a, r;
	
	public Line2D(Vector2D a, Vector2D r) {
		if (a == null || r == null || 
			r.norm() == 0.0) {
			throw new IllegalArgumentException();
		}
		
		this.a = a;
		this.r = r;
	}
	
	public Vector2D a() {
		return a;
	}
	
	public Vector2D r() {
		return r;
	}
	
	public Vector2D p(double t) {
		if (Double.isNaN(t)) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.add(a, Vector2D.multiply(t, r));
	}
	
	public double t(Vector2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.projectionFactor(Vector2D.subtract(p, a), r);
	}
	
	public boolean contains(Vector2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.collinear(Vector2D.subtract(p, a), r);
	}
	
	public double distance(Vector2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		
		Vector2D n = new Vector2D(r.x2(), -r.x1()).unit();
		
		return Math.abs(Vector2D.projectionFactor(Vector2D.subtract(p, a), n));
	}
	
	public static boolean parallel(Line2D a, Line2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.collinear(a.r(), b.r());
	}
	
	public static boolean orthogonal(Line2D a, Line2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.orthogonal(a.r(), b.r());
	}
	
	public static boolean equal(Line2D a, Line2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.collinear(a.r(), b.r()) && a.contains(b.a());
	}
	
	public static Vector2D intersection(Line2D a, Line2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		if (Line2D.parallel(a, b)) {
			throw new IllegalArgumentException();
		}
		
		Matrix m1 = Vector2D.matrixWithColumnVectors(a.r(), b.r().neg()).inverse();
		Matrix m2 = Vector2D.matrixWithColumnVector(Vector2D.add(a.a().neg(), b.a()));
		Matrix m3 = Matrix.multiply(m1, m2);
		
		double t = m3.entry(0, 0);
		
		return a.p(t);
	}
	
}
