package model.geometry3D;

import model.basis.*;

public class Plane3D {
	private Vector3D r0, n;
	
	public Plane3D(Vector3D r0, Vector3D n) {
		if (r0 == null || n == null || n.norm() == 0.0) {
			throw new IllegalArgumentException();
		}
		
		this.r0 = r0;
		this.n = n;
	}
	
	public Plane3D(Vector3D r0, Vector3D r1, Vector3D r2) {
		if (r0 == null || r1 == null || r2 == null) {
			throw new IllegalArgumentException();
		}
		
		Vector3D s1 = Vector3D.subtract(r1, r0); 
		Vector3D s2 = Vector3D.subtract(r2, r0);
		
		if (Vector3D.collinear(s1, s2)) {
			throw new IllegalArgumentException();
		}
		
		this.r0 = r0;
		this.n = Vector3D.crossProduct(s1, s2);
	}
	
	public Vector3D r0() {
		return r0;
	}
	
	public Vector3D n() {
		return n;
	}
	
	public Vector3D s1() {
		Vector3D v1 = Vector3D.crossProduct(n, Vector3D.i);
		Vector3D v2 = Vector3D.crossProduct(n, Vector3D.j);
		
		if (v1.norm() != 0.0) {
			return v1;
		}else{
			return v2;
		}
	}
	
	public Vector3D s2() {
		return Vector3D.crossProduct(n, s1());
	}
	
	public Vector3D vector(double t1, double t2) {
		if (Double.isNaN(t1) || Double.isNaN(t2)) {
			throw new IllegalArgumentException();
		}
		
		return Vector3D.add(r0, 
		                    Vector3D.multiply(t1, s1()), 
		                    Vector3D.multiply(t2, s2()));
	}
	
	public boolean contains(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		if (Vector3D.equal(r, r0)) {
			return true;
		}
		
		return Vector3D.orthogonal(Vector3D.subtract(r, r0), n);
	}
	
	public boolean contains(Line3D l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector3D.orthogonal(l.s(), n) && contains(l.r0());
	}
	
	public double parameter1(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		Vector3D[] s = new Vector3D[4];
		s[0] = s1();
		s[1] = s2();
		s[2] = n;
		s[3] = Vector3D.subtract(r, r0);
		
		
		Matrix m = Vector3D.matrixWithColumnVectors(s);
		
		m = m.rref();
		
		double t1 = m.entry(0, 3);
		
		return t1;
	}
	
	public double parameter2(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}

		Vector3D[] s = new Vector3D[4];
		s[0] = s1();
		s[1] = s2();
		s[2] = n;
		s[3] = Vector3D.subtract(r, r0);
		
		
		Matrix m = Vector3D.matrixWithColumnVectors(s);
		
		m = m.rref();
		
		double t2 = m.entry(1, 3);
		
		return t2;
	}
	
	public Vector3D projection(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		Vector3D[] s = new Vector3D[4];
		s[0] = s1();
		s[1] = s2();
		s[2] = n;
		s[3] = Vector3D.subtract(r, r0);
		
		
		Matrix m = Vector3D.matrixWithColumnVectors(s);
		
		m = m.rref();
		
		double t1 = m.entry(0, 3);
		double t2 = m.entry(1, 3);
		
		return Vector3D.add(r0, 
		                    Vector3D.multiply(t1, s1()), 
		                    Vector3D.multiply(t2, s2()));
	}
	
	public static boolean parallel(Plane3D a, Plane3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector3D.collinear(a.n(), b.n());
	}
	
	public static boolean orthogonal(Plane3D a, Plane3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector3D.orthogonal(a.n(), b.n());
	}
	
	public static boolean equal(Plane3D a, Plane3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return parallel(a, b) && a.contains(b.r0());
	}
	
	public static double distance(Vector3D v, Plane3D p) {
		if (v == null || p == null) {
			throw new IllegalArgumentException();
		}
		
		Vector3D[] s = new Vector3D[4];
		s[0] = p.s1();
		s[1] = p.s2();
		s[2] = p.n();
		s[3] = Vector3D.subtract(v, p.r0());
		
		Matrix m = Vector3D.matrixWithColumnVectors(s);
		
		m = m.rref();
		
		double n = m.entry(2, 3);
		
		return n * p.n().norm();
	}
	
	public static Vector3D intersection(Line3D l, Plane3D p) {
		if (l == null || p == null || p.contains(l)) {
			throw new IllegalArgumentException();
		}
		
		if (Vector3D.orthogonal(l.s(), p.n())) {
			return null;
		}
		
		Matrix m = Vector3D.matrixWithColumnVectors(new Vector3D[]{Vector3D.multiply(+1.0, l.s()),
																   Vector3D.multiply(-1.0, p.s1()),
																   Vector3D.multiply(-1.0, p.s2()),
																   Vector3D.subtract(p.r0(), l.r0())});
		m = m.rref();
		
		double t1 = m.entry(0, 3);
		
		return l.vector(t1);
	}
	
	public static Line3D intersection(Plane3D a, Plane3D b) {
		if (a == null || b == null || equal(a, b)) {
			throw new IllegalArgumentException();
		}
		
		if (parallel(a, b)) {
			return null;
		}
		
		Vector3D s = Vector3D.crossProduct(a.n(), b.n());
		
		Matrix m = Vector3D.matrixWithColumnVectors(new Vector3D[]{Vector3D.multiply(+1.0, a.s1()),
																   Vector3D.multiply(+1.0, a.s2()),
																   Vector3D.multiply(-1.0, b.s1()),
																   Vector3D.multiply(-1.0, b.s2()),
																   Vector3D.subtract(b.r0(), a.r0())});
		m = m.rref();
		
		//f1 * t3 + f2 * t4 = c
		double f1 = m.entry(2, 2);
		double f2 = m.entry(2, 3);
		double c = m.entry(2, 4);
		
		Vector3D r0;
		
		if (f1 != 0.0) {
			r0 = b.vector(c / f1, 0.0);
		}else{
			r0 = b.vector(0.0, c / f2);
		}
		
		return new Line3D(r0, s);
	}
	
}
