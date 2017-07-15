package model.geometry2D;

import java.util.*;

import model.basis.*;

public class Triangle2D {
	private Vector2D a, b, c;
	
	public Triangle2D(Vector2D a, Vector2D b, Vector2D c) {
		if (a == null || b == null || c == null || 
			Vector2D.equal(a, b) || 
			Vector2D.equal(a, c) || 
			Vector2D.equal(b, c) || 
			Vector2D.collinear(Vector2D.subtract(b, a), 
			                   Vector2D.subtract(c, a))) {
			throw new IllegalArgumentException();
		}
		
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public Triangle2D(Vector2D[] p) {
		if (p == null || p.length != 3 || 
			Vector2D.equal(p[0], p[1]) || 
			Vector2D.equal(p[0], p[2]) ||
			Vector2D.equal(p[1], p[2]) || 
			Vector2D.collinear(Vector2D.subtract(p[1], p[0]), 
			                   Vector2D.subtract(p[2], p[0]))) {
			throw new IllegalArgumentException();
		}
		
		this.a = p[0];
		this.b = p[1];
		this.c = p[2];
	}
	
	public Vector2D a() {
		return a;
	}
	
	public Vector2D b() {
		return b;
	}
	
	public Vector2D c() {
		return c;
	}
	
	public Vector2D ab() {
		return Vector2D.subtract(b, a);
	}
	
	public Vector2D ac() {
		return Vector2D.subtract(c, a);
	}
	
	public Vector2D ar(Vector2D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.subtract(r, a);
	}
	
	public Vector2D ba() {
		return Vector2D.subtract(a, b);
	}
	
	public Vector2D bc() {
		return Vector2D.subtract(c, b);
	}
	
	public Vector2D br(Vector2D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.subtract(r, b);
	}
	
	public Vector2D ca() {
		return Vector2D.subtract(a, c);
	}
	
	public Vector2D cb() {
		return Vector2D.subtract(b, c);
	}
	
	public Vector2D cr(Vector2D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		return Vector2D.subtract(r, c);
	}
	
	public Vector2D center() {
		return Vector2D.average(a, b, c);
	}
	
	public double alpha() {
		return Vector2D.angle(ab(), ac());
	}
	
	public double beta() {
		return Vector2D.angle(ba(), bc());
	}
	
	public double gamma() {
		return Vector2D.angle(ca(), cb());
	}
	
	public double area() {
		return Math.abs(Geometry.round(0.5 * Vector2D.dotProcuct(ab(), ac())));
	}
	
	public int orientation() {
		double a1 = a.x1();
		double a2 = a.x2();
		double b1 = b.x1();
		double b2 = b.x2();
		double c1 = c.x1();
		double c2 = c.x2();
		
		double d1 = a1 * b2 - a2 * b1;
		double d2 = b1 * c2 - b2 * c1;
		double d3 = c1 * a2 - c2 * a1;
		
		double signedArea = Geometry.round(0.5 * (d1 + d2 + d3));
		
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
		
		Matrix m1 = Vector2D.matrixWithColumnVectors(ab(), ac()).inverse();
		Matrix m2 = Vector2D.matrixWithColumnVector(ar(r));
		Matrix m3 = Matrix.multiply(m1, m2);
		
		double t1 = Geometry.round(m3.entry(0, 0), 6);
		double t2 = Geometry.round(m3.entry(1, 0), 6);
		
		if (t1 < 0.0 || t2 < 0.0) {
			return false;
		}else{
			return t1 + t2 <= 1.0;
		}
	}
	
	public double distance(Vector2D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		if (contains(r)) {
			return 0.0;
		}
		
		Line2D ab = new Line2D(a, ab());
		Line2D ac = new Line2D(a, ac());
		Line2D bc = new Line2D(b, bc());
		
		double t1 = ab.t(r);
		double t2 = ac.t(r);
		double t3 = bc.t(r);
		
		t1 = Math.max(0.0, t1);
		t1 = Math.min(t1, 1.0);
		t2 = Math.max(0.0, t2);
		t2 = Math.min(t2, 1.0);
		t3 = Math.max(0.0, t3);
		t3 = Math.min(t3, 1.0);
		
		Vector2D a = ab.p(t1);
		Vector2D b = ac.p(t2);
		Vector2D c = bc.p(t3);
		
		double d = Math.min(Vector2D.distance(r, a), 
		                    Vector2D.distance(r, b));
		d = Math.min(d, Vector2D.distance(r, c));
		
		return d;
	}
	
	public static boolean equal(Triangle2D a, Triangle2D b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		
		Vector2D a1 = a.a();
		Vector2D a2 = a.b();
		Vector2D a3 = a.c();
		Vector2D b1 = b.a();
		Vector2D b2 = b.b();
		Vector2D b3 = b.c();
		
		if (Vector2D.equal(a1, b1)) {
			if (Vector2D.equal(a2, b2) && 
				Vector2D.equal(a3, b3)) {
				return true;
			}else if (Vector2D.equal(a2, b3) && 
					  Vector2D.equal(a3, b2)) {
				return true;
			}else{
				return false;
			}
		}else if (Vector2D.equal(a1, b2)) {
			if (Vector2D.equal(a2, b1) && 
				Vector2D.equal(a3, b3)) {
				return true;
			}else if (Vector2D.equal(a2, b3) && 
					  Vector2D.equal(a3, b1)) {
				return true;
			}else{
				return false;
			}
		}else if (Vector2D.equal(a1, b3)) {
			if (Vector2D.equal(a2, b1) && 
				Vector2D.equal(a3, b2)) {
				return true;
			}else if (Vector2D.equal(a2, b2) && 
					  Vector2D.equal(a3, b1)) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public static Triangle2D[] split(Line2D l, Triangle2D t) {
		if (l == null || t == null) {
			return null;
		}
		
		Line2D ab = new Line2D(t.a(), t.ab());
		Line2D ac = new Line2D(t.a(), t.ac());
		Line2D bc = new Line2D(t.b(), t.bc());
		
		if (Line2D.equal(l, ab) || 
			Line2D.equal(l, ac) || 
			Line2D.equal(l, bc)) {
			return new Triangle2D[] {t};
		}
		
		Vector2D p1 = Line2D.parallel(l, ab) ? null : Line2D.intersection(l, ab);
		Vector2D p2 = Line2D.parallel(l, ac) ? null : Line2D.intersection(l, ac);
		Vector2D p3 = Line2D.parallel(l, bc) ? null : Line2D.intersection(l, bc);
		
		if (l.contains(t.a())) {
			if (p3 != null && t.contains(p3)) {
				return new Triangle2D[] {new Triangle2D(t.a(), t.b(), p3), 
										 new Triangle2D(t.a(), p3, t.c())};
			}else{
				return new Triangle2D[] {t};
			}
			
		}else if (l.contains(t.b())) {
			if (p2 != null && t.contains(p2)) {
				return new Triangle2D[] {new Triangle2D(t.a(), t.b(), p2), 
										 new Triangle2D(t.b(), t.c(), p2)};
			}else{
				return new Triangle2D[] {t};
			}
			
		}else if (l.contains(t.c())) {
			if (p1 != null && t.contains(p1)) {
				return new Triangle2D[] {new Triangle2D(t.a(), p1, t.c()), 
										 new Triangle2D(t.b(), t.c(), p1)};
			}else{
				return new Triangle2D[] {t};
			}
		}
		
		double t1 = (p1 == null) ? Double.NaN : ab.t(p1);
		double t2 = (p2 == null) ? Double.NaN : ac.t(p2);
		double t3 = (p3 == null) ? Double.NaN : bc.t(p3);
		
		boolean p1c = (p1 == null) ? false : (0.0 <= t1 && t1 <= 1.0);
		boolean p2c = (p2 == null) ? false : (0.0 <= t2 && t2 <= 1.0);
		boolean p3c = (p3 == null) ? false : (0.0 <= t3 && t3 <= 1.0);
		
		if (p1c && p2c) {
			return new Triangle2D[] {new Triangle2D(t.a(), p1, p2), 
									 new Triangle2D(p1, t.b(), t.c()), 
									 new Triangle2D(p1, t.c(), p2)};
		}else if (p1c && p3c) {
			return new Triangle2D[] {new Triangle2D(t.b(), p3, p1), 
					 				 new Triangle2D(t.a(), p1, p3), 
					 				 new Triangle2D(t.a(), p3, t.c())};
		}else if (p2c && p3c) {
			return new Triangle2D[] {new Triangle2D(t.c(), p2, p3), 
									 new Triangle2D(t.a(), p3, p2), 
									 new Triangle2D(t.a(), t.b(), p3)};
		}
		
		return new Triangle2D[] {t};
	}
	
	public static Triangle2D[] split(Line2D[] l, Triangle2D t) {
		if (l == null || t == null) {
			throw new IllegalArgumentException();
		}
		
		Triangle2D[] u = new Triangle2D[] {t};
		
		for (Line2D m : l) {
			Triangle2D[] v = new Triangle2D[0];
			
			for (int i = 0; i < u.length; i++) {
				Triangle2D[] w = split(m, u[i]);
				
				int a = v.length;
				int b = w.length;
				
				v = Arrays.copyOf(v, a + b);
				System.arraycopy(w, 0, v, a, b);
			}
			
			u = v;
		}
		
		return u;
	}
	
	public static Triangle2D[] intersection(Triangle2D a, Triangle2D b) {
		if (a == null || b == null) {
			return null;
		}
		
		Line2D ab = new Line2D(a.a(), a.ab());
		Line2D bc = new Line2D(a.b(), a.bc());
		Line2D ca = new Line2D(a.c(), a.ca());
		
		Triangle2D[] t = split(new Line2D[] {ab, bc, ca}, b);
		Triangle2D[] u = new Triangle2D[0];
		
		for (int i = 0; i < t.length; i++) {
			if (a.contains(t[i].center()) == false) {
				continue;
			}
			
			int n = u.length;
			
			u = Arrays.copyOf(u, n + 1);
			u[n] = t[i];
		}
		
		return u;
	}
	
	public static Triangle2D[] intersection(Triangle2D[] a, Triangle2D[] b) {
		if (a == null || a.length == 0 || 
			b == null || b.length == 0) {
			throw new IllegalArgumentException();
		}
		
		Triangle2D[] t = new Triangle2D[0];
		
		for (int i = 0; i < a.length; i++) {
			Triangle2D f = a[i];
			
			for (int j = 0; j < b.length; j++) {
				Triangle2D g = b[j];
				
				Triangle2D[] u = intersection(f, g);
				
				int n = t.length;
				int m = u.length;
				
				t = Arrays.copyOf(t, n + m);
				System.arraycopy(u, 0, t, n, m);
			}
		}
		
		return t;
	}
	
}
