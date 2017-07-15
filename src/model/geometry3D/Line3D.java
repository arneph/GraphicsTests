package model.geometry3D;

import model.basis.*;

public class Line3D {
	private Vector3D r0, s;
	
	public Line3D(Vector3D r0, Vector3D s) {
		if (r0 == null || s == null || s.norm() == 0.0) {
			throw new IllegalArgumentException();
		}
		
		this.r0 = r0;
		this.s = s;
	}
	
	public Vector3D r0() {
		return r0;
	}
	
	public Vector3D s() {
		return s;
	}
	
	public Vector3D vector(double t) {
		if (Double.isNaN(t)) {
			throw new IllegalArgumentException();
		}
		
		return Vector3D.add(r0, Vector3D.multiply(t, s));
	}
	
	public boolean contains(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		if (Vector3D.equal(r, r0)) {
			return true;
		}else{
			return Vector3D.collinear(Vector3D.subtract(r, r0), 
			                          s);
		}
	}
	
	public double parameter(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		if (Vector3D.equal(r, r0)) {
			return 0.0;
		}
		
		double t = Vector3D.projectionFactor(Vector3D.subtract(r, r0), 
		                                     s);
		
		return t;
	}
	
	public Vector3D projection(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		if (Vector3D.equal(r, r0)) {
			return r;
		}
		
		double t = Vector3D.projectionFactor(Vector3D.subtract(r, r0), 
		                                     s);
		
		return Vector3D.add(r0, Vector3D.multiply(t, s));
	}
	
	public static boolean parallel(Line3D a, Line3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector3D.collinear(a.s(), b.s());
	}
	
	public static boolean orthogonal(Line3D a, Line3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector3D.orthogonal(a.s(), b.s());
	}
	
	public static boolean equal(Line3D a, Line3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return parallel(a, b) && a.contains(b.r0());
	}
	
	public static double distance(Vector3D v, Line3D l) {
		if (v == null || l == null) {
			throw new IllegalArgumentException();
		}
		
		Vector3D w = l.projection(v);
		
		return Vector3D.subtract(v, w).norm();
	}
	
	public static Vector3D intersection(Line3D a, Line3D b) {
		if (a == null || b == null || equal(a, b)) {
			throw new IllegalArgumentException();
		}
		
		if (Line3D.parallel(a, b)) {
			return null;
		}
		
		Matrix m = Vector3D.matrixWithColumnVectors(Vector3D.multiply(+1.0, a.s()), 
		                                            Vector3D.multiply(-1.0, b.s()),
		                                            Vector3D.subtract(b.r0(), a.r0()));
		m = m.rref();
		
		if (m.entry(2, 2) == 1.0) {
			return null;
		}
		
		double t1 = m.entry(0, 2);
		
		return a.vector(t1);
	}
	
}
