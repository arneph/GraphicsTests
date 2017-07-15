package model.geometry2D;

import model.basis.*;

public class Vector2D {
	private double x1, x2;
	
	public static final Vector2D zeroVector = new Vector2D(0.0, 0.0);
	public static final Vector2D origin 	= new Vector2D(0.0, 0.0);
	public static final Vector2D i			= new Vector2D(1.0, 0.0);
	public static final Vector2D j			= new Vector2D(0.0, 1.0);
	
	public Vector2D(double x1, double x2) {
		if (Double.isNaN(x1) || 
			Double.isNaN(x2)) {
			throw new IllegalArgumentException();
		}
		
		this.x1 = Geometry.round(x1);
		this.x2 = Geometry.round(x2);
	}
	
	public Vector2D(double[] x) {
		if (x == null || 
			x.length != 2 || 
			Double.isNaN(x[0]) || 
			Double.isNaN(x[1])) {
			throw new IllegalArgumentException();
		}
		
		this.x1 = Geometry.round(x[0]);
		this.x2 = Geometry.round(x[1]);
	}
	
	public double x1() {
		return x1;
	}
	
	public double x2() {
		return x2;
	}
	
	public double[] x() {
		return new double[] {x1, x2};
	}
	
	public Vector2D neg() {
		return new Vector2D(-x1, -x2);
	}
	
	public double norm() {
		double n = Math.sqrt(Math.pow(x1, 2.0) + Math.pow(x2, 2.0));
		
		return Geometry.round(n);
	}
	
	public Vector2D unit() {
		double n = norm();
		
		if (n == 0.0) {
			return null;
		}else{
			return new Vector2D(x1 / n, x2 / n);			
		}
	}
	
	public static Vector2D add(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D(a.x1() + b.x1(), 
		                    a.x2() + b.x2());
	}
	

	public static Vector2D add(Vector2D a, Vector2D b, Vector2D c) {
		if (a == null || b == null || c == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D(a.x1() + b.x1() + c.x1(), 
		                    a.x2() + b.x2() + c.x2());
	}
	
	public static Vector2D add(Vector2D[] a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		
		double x1 = 0.0;
		double x2 = 0.0;
		
		for (Vector2D v : a) {
			x1 += v.x1();
			x2 += v.x2();
		}
		
		return new Vector2D(x1, x2);
	}
	
	public static Vector2D subtract(Vector2D m, Vector2D s) {
		if (m == null || s == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D(m.x1() - s.x1(), 
		                    m.x2() - s.x2());
	}
	
	public static Vector2D multiply(double k, Vector2D a) {
		if (Double.isNaN(k) || a == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D(k * a.x1(), 
		                    k * a.x2());
	}
	
	public static Vector2D average(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D((a.x1() + b.x1()) / 2.0, 
		                    (a.x2() + b.x2()) / 2.0);
	}
	
	public static Vector2D average(Vector2D a, Vector2D b, Vector2D c) {
		if (a == null || b == null || c == null) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D((a.x1() + b.x1() + c.x1()) / 3.0, 
		                    (a.x2() + b.x2() + c.x2()) / 3.0);
	}
	
	public static Vector2D average(Vector2D[] a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		
		double n = a.length;
		double x1 = 0.0;
		double x2 = 0.0;
		
		for (Vector2D v : a) {
			x1 += v.x1();
			x2 += v.x2();
		}
		
		return new Vector2D(x1 / n, x2 / n);
	}
	
	public static double distance(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		double d = Math.sqrt(Math.pow(a.x1() - b.x1(), 2.0) + Math.pow(a.x2() - b.x2(), 2.0));
		
		return Geometry.round(d);
	}
	
	public static double dotProcuct(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return Geometry.round(a.x1() * b.x1() + a.x2() * b.x2());
	}
	
	public static double angle(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		double ab = dotProcuct(a, b);
		double an = a.norm();
		double bn = b.norm();
		
		double angle = Math.toDegrees(Math.acos(ab / (an * bn)));
		
		return Geometry.round(angle);
	}
	
	public static double projectionFactor(Vector2D a, Vector2D v) {
		if (a == null || v == null) {
			throw new IllegalArgumentException();
		}
		
		double f = Vector2D.dotProcuct(a, v);
		double g = Vector2D.dotProcuct(v, v);
		
		if (g == 0.0) {
			return Double.NaN;
		}else{
			double pf = f / g;
			
			return Geometry.round(pf);
		}
	}
	
	public static Vector2D projection(Vector2D a, Vector2D v) {
		if (a == null || v == null) {
			throw new IllegalArgumentException();
		}
		
		double f = Vector2D.dotProcuct(a, v);
		double g = Vector2D.dotProcuct(v, v);
		
		if (g == 0.0) {
			return null;
		}else{
			return Vector2D.multiply(f / g, v);
		}
	}
	
	public static boolean collinear(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		Matrix m = Vector2D.matrixWithColumnVectors(a, b);
		
		return m.determinant() == 0.0;
	}
	
	public static boolean orthogonal(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		double ab = dotProcuct(a, b);
		
		return ab == 0.0;
	}
	
	public static boolean equal(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		if (a.x1() != b.x1()) return false;
		if (a.x2() != b.x2()) return false;
		
		return true;
	}
	
	public static Matrix matrixWithRowVector(Vector2D a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][] {new double[] {a.x1(), a.x2()}});
	}
	
	public static Matrix matrixWithRowVectors(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][] {new double[] {a.x1(), a.x2()}, 
										  new double[] {b.x1(), b.x2()}});
	}
	public static Matrix matrixWithRowVectors(Vector2D[] a) {
		if (a == null || a.length < 1) {
			throw new IllegalArgumentException();
		}
		
		int m = a.length;
		
		double[][] v = new double[m][2];
		
		for (int i = 0; i < m; i++) {
			v[i][0] = a[i].x1();
			v[i][1] = a[i].x2();
		}
		
		return new Matrix(v);
	}
	
	public static Vector2D rowVectorFromMatrix(Matrix m) {
		if (m == null || 
			m.m() != 1 || 
			m.n() != 2) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D(m.rowVector(0));
	}
	
	public static Vector2D[] rowVectorsFromMatrix(Matrix m) {
		if (m == null || 
			m.n() != 2) {
			throw new IllegalArgumentException();
		}
		
		int n = m.m();
		
		Vector2D[] rowVectors = new Vector2D[n];
		
		for (int i = 0; i < n; i++) {
			rowVectors[i] = new Vector2D(m.rowVector(i));
		}
		
		return rowVectors;
	}
	
	public static Matrix matrixWithColumnVector(Vector2D a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][] {new double[] {a.x1()}, 
										  new double[] {a.x2()}});
	}
	
	public static Matrix matrixWithColumnVectors(Vector2D a, Vector2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		return new Matrix(new double[][] {new double[] {a.x1(), b.x1()}, 
										  new double[] {a.x2(), b.x2()}});
	}
	public static Matrix matrixWithColumnVectors(Vector2D[] a) {
		if (a == null || a.length < 1) {
			throw new IllegalArgumentException();
		}
		
		int n = a.length;
		
		double[][] v = new double[2][n];
		
		for (int i = 0; i < n; i++) {
			v[0][i] = a[i].x1();
			v[1][i] = a[i].x2();
		}
		
		return new Matrix(v);
	}
	
	public static Vector2D columnVectorFromMatrix(Matrix m) {
		if (m == null || 
			m.m() != 2 || 
			m.n() != 1) {
			throw new IllegalArgumentException();
		}
		
		return new Vector2D(m.columnVector(0));
	}
	
	public static Vector2D[] columnVectorsFromMatrix(Matrix m) {
		if (m == null || 
			m.m() != 2) {
			throw new IllegalArgumentException();
		}
		
		int n = m.n();
		
		Vector2D[] columnVectors = new Vector2D[n];
		
		for (int i = 0; i < n; i++) {
			columnVectors[i] = new Vector2D(m.columnVector(i));
		}
		
		return columnVectors;
	}
	
}
