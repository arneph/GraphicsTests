package model.geometry3D;

import model.basis.*;

public class Vector3D {
	private double x1, x2, x3;
	
	public static final Vector3D zeroVector = new Vector3D(0.0, 0.0, 0.0);
	public static final Vector3D origin 	= new Vector3D(0.0, 0.0, 0.0);
	public static final Vector3D i 			= new Vector3D(1.0, 0.0, 0.0);
	public static final Vector3D j 			= new Vector3D(0.0, 1.0, 0.0);
	public static final Vector3D k 			= new Vector3D(0.0, 0.0, 1.0);
	
	public Vector3D(double x1, double x2, double x3) {
		if (Double.isNaN(x1) || 
			Double.isNaN(x2) || 
			Double.isNaN(x3)) {
			throw new IllegalArgumentException();
		}
		
		this.x1 = Geometry.round(x1);
		this.x2 = Geometry.round(x2);
		this.x3 = Geometry.round(x3);
	}
	
	public Vector3D(double[] x) {
		if (x == null || 
			x.length != 3 || 
			Double.isNaN(x[0]) || 
			Double.isNaN(x[1]) || 
			Double.isNaN(x[2])) {
			throw new IllegalArgumentException();
		}
		
		this.x1 = Geometry.round(x[0]);
		this.x2 = Geometry.round(x[1]);
		this.x3 = Geometry.round(x[2]);
	}
	
	public double x1() {
		return x1;
	}
	
	public double x2() {
		return x2;
	}
	
	public double x3() {
		return x3;
	}
	
	public double[] x() {
		return new double[] {x1, x2, x3};
	}
	
	public Vector3D neg() {
		return new Vector3D(-x1, -x2, -x3);
	}
	
	public double norm() {
		double n = Math.sqrt(Math.pow(x1, 2.0) + Math.pow(x2, 2.0) + Math.pow(x3, 2.0));
		
		return Geometry.round(n);
	}
	
	public Vector3D unit() {
		double n = norm();
		
		if (n == 0.0) {
			return null;
		}else{
			return new Vector3D(x1 / n, x2 / n, x3 / n);
		}
	}
	
	public static Vector3D add(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D(a.x1() + b.x1(), 
		                    a.x2() + b.x2(), 
		                    a.x3() + b.x3());
	}
	
	public static Vector3D add(Vector3D a, Vector3D b, Vector3D c) {
		if (a == null || b == null || c == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D(a.x1() + b.x1() + c.x1(), 
		                    a.x2() + b.x2() + c.x2(), 
		                    a.x3() + b.x3() + c.x3());
	}
	
	public static Vector3D add(Vector3D[] a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		
		double x1 = 0.0;
		double x2 = 0.0;
		double x3 = 0.0;
		
		for (Vector3D v : a) {
			x1 += v.x1();
			x2 += v.x2();
			x3 += v.x3();
		}
		
		return new Vector3D(x1, x2, x3);
	}
	
	public static Vector3D subtract(Vector3D m, Vector3D s) {
		if (m == null || s == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D(m.x1() - s.x1(), 
		                    m.x2() - s.x2(), 
		                    m.x3() - s.x3());
	}
	
	public static Vector3D multiply(double k, Vector3D a) {
		if (Double.isNaN(k) || a == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D(k * a.x1(), 
		                    k * a.x2(), 
		                    k * a.x3());
	}
	
	public static Vector3D average(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D((a.x1() + b.x1()) / 2.0, 
		                    (a.x2() + b.x2()) / 2.0, 
		                    (a.x3() + b.x3()) / 2.0);
	}
	
	public static Vector3D average(Vector3D a, Vector3D b, Vector3D c) {
		if (a == null || b == null || c == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D((a.x1() + b.x1() + c.x1()) / 3.0, 
		                    (a.x2() + b.x2() + c.x2()) / 3.0, 
		                    (a.x3() + b.x3() + c.x3()) / 3.0);
	}
	
	public static Vector3D average(Vector3D[] a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		
		double n = a.length;
		double x1 = 0.0;
		double x2 = 0.0;
		double x3 = 0.0;
		
		for (Vector3D v : a) {
			x1 += v.x1();
			x2 += v.x2();
			x3 += v.x3();
		}
		
		return new Vector3D(x1 / n, x2 / n, x3 / n);
	}
	
	public static double dotProduct(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Geometry.round(a.x1() * b.x1() + a.x2() * b.x2() + a.x3() * b.x3());
	}
	
	public static Vector3D crossProduct(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D(a.x2() * b.x3() - a.x3() * b.x2(), 
		                    a.x3() * b.x1() - a.x1() * b.x3(), 
		                    a.x1() * b.x2() - a.x2() * b.x1());
	}
	
	public static double angle(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		double ab = dotProduct(a, b);
		double an = a.norm();
		double bn = b.norm();
		
		double angle = Math.toDegrees(Math.acos(ab / (an * bn)));
		
		return Geometry.round(angle);
	}
	
	public static double projectionFactor(Vector3D a, Vector3D v) {
		if (a == null || v == null) {
			throw new IllegalArgumentException();
		}
		
		double f = Vector3D.dotProduct(a, v);
		double g = Vector3D.dotProduct(v, v);
		
		if (g == 0.0) {
			return Double.NaN;
		}else{
			double pf = f / g;
			
			return Geometry.round(pf);
		}
	}
	
	public static Vector3D projection(Vector3D a, Vector3D v) {
		if (a == null || v == null) {
			throw new IllegalArgumentException();
		}
		
		double f = Vector3D.dotProduct(a, v);
		double g = Vector3D.dotProduct(v, v);
		
		if (g == 0.0) {
			return null;
		}else{
			return Vector3D.multiply(f / g, v);
		}
	}
	
	public static boolean collinear(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		double ab = dotProduct(a, b);
		double an = a.norm();
		double bn = b.norm();
		
		if (an == 0.0 || bn == 0.0) {
			return false;
		}else{
			return Geometry.round(ab - an * bn) == 0.0;
		}
	}
	
	public static boolean orthogonal(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		double ab = dotProduct(a, b);
		
		return ab == 0.0;
	}
	
	public static boolean equal(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		if (a.x1() != b.x1()) return false;
		if (a.x2() != b.x2()) return false;
		if (a.x3() != b.x3()) return false;
		
		return true;
	}
	
	public static Matrix matrixWithRowVector(Vector3D v) {
		if (v == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][]{new double[] {v.x1(), v.x2(), v.x3()}});
	}
	
	public static Matrix matrixWithRowVectors(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][]{new double[] {a.x1(), a.x2(), a.x3()}, 
										 new double[] {b.x1(), b.x2(), b.x3()}});
	}
	
	public static Matrix matrixWithRowVectors(Vector3D a, Vector3D b, Vector3D c) {
		if (a == null || b == null || c == null) {
			throw new IllegalArgumentException();
		}

		return new Matrix(new double[][]{new double[] {a.x1(), a.x2(), a.x3()}, 
										 new double[] {b.x1(), b.x2(), b.x3()}, 
										 new double[] {c.x1(), c.x2(), c.x3()}});
	}
	
	public static Matrix matrixWithRowVectors(Vector3D[] a) {
		if (a == null || a.length < 1) {
			throw new IllegalArgumentException();
		}
		
		int m = a.length;
		
		double[][] v = new double[m][3];
		
		for (int i = 0; i < m; i++) {
			v[i][0] = a[i].x1();
			v[i][1] = a[i].x2();
			v[i][2] = a[i].x3();
		}
		
		return new Matrix(v);
	}
	
	public static Vector3D rowVectorFromMatrix(Matrix m) {
		if (m == null || 
			m.m() != 1 || 
			m.n() != 3) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D(m.rowVector(0));
	}
	
	public static Vector3D[] rowVectorsFromMatrix(Matrix m) {
		if (m == null || 
			m.n() != 3) {
			throw new IllegalArgumentException();
		}
		
		int n = m.m();
		
		Vector3D[] rowVectors = new Vector3D[n];
		
		for (int i = 0; i < n; i++) {
			rowVectors[i] = new Vector3D(m.rowVector(i));
		}
		
		return rowVectors;
	}
	
	public static Matrix matrixWithColumnVector(Vector3D a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][]{new double[] {a.x1()},
										 new double[] {a.x2()},
										 new double[] {a.x3()}});
	}
	
	public static Matrix matrixWithColumnVectors(Vector3D a, Vector3D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][]{new double[] {a.x1(), b.x1()},
										 new double[] {a.x2(), b.x2()},
										 new double[] {a.x3(), b.x3()}});
	}
	
	public static Matrix matrixWithColumnVectors(Vector3D a, Vector3D b, Vector3D c) {
		if (a == null || b == null || c == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][]{new double[] {a.x1(), b.x1(), c.x1()},
										 new double[] {a.x2(), b.x2(), c.x2()},
										 new double[] {a.x3(), b.x3(), c.x3()}});
	}
	
	public static Matrix matrixWithColumnVectors(Vector3D[] a) {
		if (a == null || a.length < 1) {
			throw new IllegalArgumentException();
		}
		
		int n = a.length;
		
		double[][] v = new double[3][n];
		
		for (int i = 0; i < n; i++) {
			v[0][i] = a[i].x1();
			v[1][i] = a[i].x2();
			v[2][i] = a[i].x3();
		}
		
		return new Matrix(v);
	}
	
	public static Vector3D columnVectorFromMatrix(Matrix m) {
		if (m == null || 
			m.m() != 3 || 
			m.n() != 1) {
			throw new IllegalArgumentException();
		}
		
		return new Vector3D(m.columnVector(0));
	}
	
	public static Vector3D[] columnVectorsFromMatrix(Matrix m) {
		if (m == null || 
			m.m() != 3) {
			throw new IllegalArgumentException();
		}
		
		int n = m.n();
		
		Vector3D[] columnVectors = new Vector3D[n];
		
		for (int i = 0; i < n; i++) {
			columnVectors[i] = new Vector3D(m.columnVector(i));
		}
		
		return columnVectors;
	}
	
}
