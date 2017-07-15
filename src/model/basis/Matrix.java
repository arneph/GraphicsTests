package model.basis;

import java.util.*;

public class Matrix {
	private double[][] entries;
	
	public Matrix(int n) {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		
		entries = new double[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					entries[i][j] = 1.0;
				}else{
					entries[i][j] = 0.0;
				}
			}
		}
	}
	
	public Matrix(double[] entries) {
		if (entries == null) {
			throw new IllegalArgumentException();
		}
		
		int n = entries.length;
		
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		
		this.entries = new double[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					double entry = entries[i];
					
					if (Double.isNaN(entry)) {
						throw new IllegalArgumentException();
					}
					
					this.entries[i][j] = Geometry.round(entry);
				}else{
					this.entries[i][j] = 0.0;
				}
			}
		}
	}
	
	public Matrix(double[][] entries) {
		if (entries == null) {
			throw new IllegalArgumentException();
		}
		
		int m = entries.length;
		
		if (m < 1) {
			throw new IllegalArgumentException();
		}
		
		int n = entries[0].length;
		
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		
		this.entries = new double[m][n];
		
		for (int i = 0; i < m; i++) {
			double[] row = entries[i];
			
			if (row.length != n) {
				throw new IllegalArgumentException();
			}
			
			for (int j = 0; j < n; j++) {
				double entry = row[j];
				
				if (Double.isNaN(entry)) {
					throw new IllegalArgumentException();
				}
				
				this.entries[i][j] = Geometry.round(entry);
			}
		}
	}
	
	public int m() {
		return entries.length;
	}
	
	public int n() {
		return entries[0].length;
	}
	
	public boolean isSquare() {
		int m = entries.length;
		int n = entries[0].length;
		
		return m == n;
	}
	
	public double entry(int i, int j) {
		if (i < 0 || i >= entries.length || 
			j < 0 || j >= entries[0].length) {
			throw new IllegalArgumentException();
		}
		
		return entries[i][j];
	}
	
	public double[] rowVector(int i) {
		if (i < 0 || i >= entries.length) {
			throw  new IllegalArgumentException();
		}
		
		return entries[i];
	}
	
	public double[] columnVector(int j) {
		if (j < 0 || j >= entries[0].length) {
			throw new IllegalArgumentException();
		}
		
		double[] v = new double[entries.length];
		
		for (int i = 0; i < entries.length; i++) {
			v[i] = entries[i][j];
		}
		
		return v;
	}
	
	public Matrix submatrix(int i, int j) {
		int m = entries.length;
		int n = entries[0].length;
		
		if (i < 0 || i >= m || 
			j < 0 || j >= n) {
			throw new IllegalArgumentException();
		}else if (m == 1 || n == 1) {
			return null;
		}
		
		double[][] v = new double[m - 1][n - 1];
		
		for (int k = 0; k < m - 1; k++) {
			for (int l = 0; l < n - 1; l++) {
				if (k < i) {
					if (l < j) {
						v[k][l] = entries[k][l];
					}else{
						v[k][l] = entries[k][l + 1];
					}
				}else{
					if (l < j) {
						v[k][l] = entries[k + 1][l];
					}else{
						v[k][l] = entries[k + 1][l + 1];
					}
				}
			}
		}
		
		return new Matrix(v);
	}
	
	public Matrix transpose() {
		int m = entries.length;
		int n = entries[0].length;
		
		double[][] v = new double[n][m];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				v[j][i] = entries[i][j];
			}
		}
		
		return new Matrix(v);
	}
	
	public int numberOfLeadingOnes() {
		int m = entries.length;
		int n = entries[0].length;
		
		int num = 0;
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (entries[i][j] == 1.0) {
					num++;
					break;
				}
			}
		}
		
		return num;
	}
	
	public int[] rowIndicesOfLeadingOnes() {
		int m = entries.length;
		int n = entries[0].length;
		
		int[] indices = new int[0];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (entries[i][j] == 1.0) {
					int t = indices.length;
					
					indices = Arrays.copyOf(indices, t + 1);
					indices[t] = i;
					
					continue;
				}
			}
		}
		
		return indices;
	}
	
	public int[] columnIndicesOfLeadingOnes() {
		int m = entries.length;
		int n = entries[0].length;
		
		int[] indices = new int[0];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (entries[i][j] == 1.0) {
					int t = indices.length;
					
					indices = Arrays.copyOf(indices, t + 1);
					indices[t] = j;
					
					continue;
				}
			}
		}
		
		return indices;
	}
	
	public double determinant() {
		int m = entries.length;
		int n = entries[0].length;
		
		if (m != n) {
			return Double.NaN;
		}
		
		if (m == 1) {
			return entries[0][0];
			
		}else if (m == 2) {
			double a = entries[0][0];
			double b = entries[0][1];
			double c = entries[1][0];
			double d = entries[1][1];
			
			return Geometry.round(a*d - b*c);
			
		}else if (m == 3) {
			double a = entries[0][0];
			double b = entries[0][1];
			double c = entries[0][2];
			double d = entries[1][0];
			double e = entries[1][1];
			double f = entries[1][2];
			double g = entries[2][0];
			double h = entries[2][1];
			double i = entries[2][2];
			
			return Geometry.round(a*e*i + b*f*g + c*d*h - a*f*g - b*d*i - c*e*g);
			
		}else{
			double d = 0.0;
			
			for (int i = 0; i < n; i++) {
				double v = entries[i][0];
				
				if (v == 0.0) continue;
				
				double ds = submatrix(i, 0).determinant();
				
				if (i % 2 == 0) {
					d += v * ds;
				}else{
					d -= v * ds;
				}
			}
			
			return Geometry.round(d);
		}
	}
	
	public double trace() {
		int m = entries.length;
		int n = entries[0].length;
		
		if (m != n) {
			return Double.NaN;
		}
		
		double t = 0.0;
		
		for (int i = 0; i < m; i++) {
			t += entries[i][i];
		}
		
		return Geometry.round(t);
	}
	
	public int rank() {
		return ref().numberOfLeadingOnes();
	}
	
	public int nullity() {
		int n = entries[0].length;
		
		return n - ref().numberOfLeadingOnes();
	}
	
	public double minor(int i, int j) {
		int m = entries.length;
		int n = entries[0].length;
		
		if (i < 0 || i >= m || 
			j < 0 || j >= n) {
			throw new IllegalArgumentException();
		}else if (m == 1 || n == 1) {
			return 1.0;
		}
		
		return submatrix(i, j).determinant();
	}
	
	public double cofactor(int i, int j) {
		int m = entries.length;
		int n = entries[0].length;
		
		if (i < 0 || i >= m || 
			j < 0 || j >= n) {
			throw new IllegalArgumentException();
		}else if (m == 1 || n == 1) {
			return 1.0;
		}
		
		if ((i + j) % 2 == 0) {
			return minor(i, j);
		}else{
			return -minor(i, j);
		}
	}
	
	public double[][] basisForRowSpace() {
		return transpose().basisForColumnSpace();
	}
	
	public double[][] basisForColumnSpace() {
		int[] indices = ref().columnIndicesOfLeadingOnes();
		
		double[][] basis = new double[indices.length][];
		
		for (int i = 0; i < indices.length; i++) {
			basis[i] = columnVector(indices[i]);
		}
		
		return basis;
	}
	
	public Matrix adjoint() {
		int m = entries.length;
		int n = entries[0].length;
		
		double v[][] = new double[n][m];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				v[j][i] = cofactor(i, j);
			}
		}
		
		return new Matrix(v);
	}
	
	public Matrix inverse() {
		int m = entries.length;
		int n = entries[0].length;
		
		if (m != n) {
			return null;
		}
		
		double d = determinant();
		
		if (d == 0.0) {
			return null;
		}
		
		return Matrix.multiply(1.0 / d, adjoint());
	}
	
	public Matrix ref() {
		int m = entries.length;
		int n = entries[0].length;
		
		double[][] v = new double [m][n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				v[i][j] = entries[i][j];
			}
		}
		
		int i = 0;
		int j = 0;
		
		while (i < m && j < n) {
			//Sorting:
			int nzeros = 0;
			
			for (int k = i; k < m - nzeros; k++) {
				if (v[k][j] == 0.0) {
					nzeros++;
					
					double[] r1 = v[k];
					double[] r2 = v[m - nzeros];
					
					v[k] = r2;
					v[m - nzeros] = r1;
				}
			}
			
			
			double e = v[i][j];
			
			if (e == 0.0) {
				j++;
				continue;
			}
			
			//Reducing rows below [i][j]:
			for (int k = i + 1; k < m; k++) {
				double f = v[k][j];
				
				v[k][j] = 0.0;
				
				for (int l = j + 1; l < n; l++) {
					v[k][l] = v[k][l] - f / e * v[i][l];
				}
			}
			
			//Reducing row i:
			v[i][j] = 1.0;
			
			for (int l = j + 1; l < n; l++) {
				v[i][l] = 1.0 / e * v[i][l];
			}
			
			
			i++;
			j++;
		}
		
		return new Matrix(v);
	}
	
	public Matrix rref() {
		int m = entries.length;
		int n = entries[0].length;
		
		double[][] v = new double [m][n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				v[i][j] = entries[i][j];
			}
		}
		
		int i = 0;
		int j = 0;
		
		while (i < m && j < n) {
			//Sorting:
			int nzeros = 0;
			
			for (int k = i; k < m - nzeros; k++) {
				if (v[k][j] == 0.0) {
					nzeros++;
					
					double[] r1 = v[k];
					double[] r2 = v[m - nzeros];
					
					v[k] = r2;
					v[m - nzeros] = r1;
				}
			}
			
			
			double e = v[i][j];
			
			if (e == 0.0) {
				j++;
				continue;
			}
			
			//Reducing rows above and below [i][j]:
			for (int k = 0; k < m; k++) {
				if (k == i) continue;
				
				double f = v[k][j];
				
				v[k][j] = 0.0;
				
				for (int l = j + 1; l < n; l++) {
					v[k][l] = v[k][l] - f / e * v[i][l];
				}
			}
			
			//Reducing row i:
			v[i][j] = 1.0;
			
			for (int l = j + 1; l < n; l++) {
				v[i][l] = 1.0 / e * v[i][l];
			}
			
			
			i++;
			j++;
		}
		
		return new Matrix(v);
	}
	
	public static Matrix add(Matrix a, Matrix b) {
		if (a == null || b == null || 
			a.m() != b.m() || 
			a.n() != b.n()) {
			throw new IllegalArgumentException();
		}
		
		int m = a.m();
		int n = a.n();
		
		double v[][] = new double[m][n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				v[i][j] = a.entry(i, j) + b.entry(i, j);
			}
		}
		
		return new Matrix(v);
	}
	
	public static Matrix subtract(Matrix a, Matrix b) {
		if (a == null || b == null || 
			a.m() != b.m() || 
			a.n() != b.n()) {
			throw new IllegalArgumentException();
		}
		
		int m = a.m();
		int n = a.n();
		
		double v[][] = new double[m][n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				v[i][j] = a.entry(i, j) - b.entry(i, j);
			}
		}
		
		return new Matrix(v);
	}

	public static Matrix multiply(double k, Matrix a) {
		if (Double.isNaN(k) || a == null) {
			throw new IllegalArgumentException();
		}
		int m = a.m();
		int n = a.n();
		
		double v[][] = new double[m][n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				v[i][j] = k * a.entry(i, j);
			}
		}
		
		return new Matrix(v);
	}
	
	public static Matrix multiply(Matrix a, Matrix b) {
		if (a == null || b == null || 
			a.n() != b.m()) {
			throw new IllegalArgumentException();
		}
		
		int m = a.m();
		int r = a.n();
		int n = b.n();
		
		double v[][] = new double[m][n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				double e = 0.0;
				
				for (int k = 0; k < r; k++) {
					e += a.entry(i, k) * b.entry(k, j);
				}
				
				v[i][j] = e;
			}
		}
		
		return new Matrix(v);
	}
	
	public static Matrix rotation2D(double angle) {
		if (Double.isNaN(angle)) {
			throw new IllegalArgumentException();
		}
		
		double c = Math.cos(Math.toRadians(angle));
		double s = Math.sin(Math.toRadians(angle));
		
		double[][] e = new double[][] {new double[] {+c, -s}, 
									   new double[] {+s, +c}};
		
		return new Matrix(e);
	}
	
	public static Matrix rotation3D_x_axis(double angle) {
		if (Double.isNaN(angle)) {
			throw new IllegalArgumentException();
		}
		
		double c = Math.cos(Math.toRadians(angle));
		double s = Math.sin(Math.toRadians(angle));
		
		double[][] e = new double[][] {new double[] {1.0, 0.0, 0.0}, 
									   new double[] {0.0, +c,  -s }, 
									   new double[] {0.0, +s,  -c }};
		
		return new Matrix(e);
	}
	
	public static Matrix rotation3D_y_axis(double angle) {
		if (Double.isNaN(angle)) {
			throw new IllegalArgumentException();
		}
		
		double c = Math.cos(Math.toRadians(angle));
		double s = Math.sin(Math.toRadians(angle));
		
		double[][] e = new double[][] {new double[] {+c,  0.0, -s }, 
									   new double[] {0.0, 1.0, 0.0}, 
									   new double[] {+s,  0.0, -c }};
		
		return new Matrix(e);
	}
	
	public static Matrix rotation3D_z_axis(double angle) {
		if (Double.isNaN(angle)) {
			throw new IllegalArgumentException();
		}
		
		double c = Math.cos(Math.toRadians(angle));
		double s = Math.sin(Math.toRadians(angle));
		
		double[][] e = new double[][] {new double[] {+c,  -s,  0.0}, 
									   new double[] {+s,  +c,  0.0}, 
									   new double[] {0.0, 0.0, 1.0}};
		
		return new Matrix(e);
	}
	
}
