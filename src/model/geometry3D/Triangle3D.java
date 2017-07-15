package model.geometry3D;

import model.basis.*;

public class Triangle3D {
	private Vector3D a, b, c;
	private Color color;
	
	public Triangle3D(Vector3D a, Vector3D b, Vector3D c, Color color) {
		if (a == null || b == null || c == null || color == null) {
			throw new IllegalArgumentException();
		}
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.color = color;
	}
	
	public Triangle3D(Vector3D[] points, Color color) {
		if (points == null || points.length != 3 || color == null) {
			throw new IllegalArgumentException();
		}
		
		this.a = points[0];
		this.b = points[1];
		this.c = points[2];
		this.color = color;
	}
	
	public Vector3D a() {
		return a;
	}
	
	public Vector3D b() {
		return b;
	}
	
	public Vector3D c() {
		return c;
	}
	
	public Vector3D getCenter() {
		return Vector3D.average(a, b, c);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Vector3D getNormalVector() {
		return Vector3D.crossProduct(Vector3D.subtract(b, a), 
		                             Vector3D.subtract(c, a));
	}
	
	public Plane3D getPlane() {
		Vector3D n = getNormalVector();
		
		return new Plane3D(a, n);
	}
	
	public boolean contains(Vector3D r) {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		Vector3D ar = Vector3D.subtract(r, a);
		Vector3D ab = Vector3D.subtract(b, a);
		Vector3D ac = Vector3D.subtract(c, a);
		Vector3D n = Vector3D.crossProduct(ab, ac);
		
		Vector3D[] s = new Vector3D[] {ab, ac, n};
		Matrix m = Vector3D.matrixWithColumnVectors(s);
		Matrix t = Matrix.multiply(m.inverse(), 
		                           Vector3D.matrixWithColumnVector(ar));
		
		double t1 = t.entry(0, 0);
		double t2 = t.entry(1, 0);
		
		if (Vector3D.orthogonal(ar, n) == false) {
			return false;
		}
		
		if (t1 < 0.0 || t2 < 0.0) {
			return false;
		}else if (t1 + t2 > 1.0) {
			return false;
		}
		
		return true;
	}
	
	public static Triangle3D[] intersect(Line3D l, Triangle3D t) {
		Plane3D p = t.getPlane();
		
		if (p.contains(l) == false) {
			return new Triangle3D[] {t};
		}
		
		Vector3D a = t.a();
		Vector3D b = t.b();
		Vector3D c = t.c();
		
		Line3D ab = new Line3D(a, Vector3D.subtract(b, a));
		Line3D ac = new Line3D(a, Vector3D.subtract(c, a));
		Line3D bc = new Line3D(b, Vector3D.subtract(c, b));
		
		if (l.contains(a)) {
			if (l.contains(b) || l.contains(c)) {
				return new Triangle3D[] {t};
			}
			
			Vector3D px = Line3D.intersection(l, bc);
			double x = bc.parameter(px);
			
			if (x < 0.0 || x > 1.0) {
				return new Triangle3D[] {t};
			}else{
				return new Triangle3D[] {new Triangle3D(a, b, px, t.getColor()), 
										 new Triangle3D(a, px, c, t.getColor())};
			}
			
		}else if (l.contains(b)) {
			if (l.contains(c)) {
				return new Triangle3D[] {t};
			}
			
			Vector3D px = Line3D.intersection(l, ac);
			double x = ac.parameter(px);
			
			if (x < 0.0 || x > 1.0) {
				return new Triangle3D[] {t};
			}else{
				return new Triangle3D[] {new Triangle3D(a, b, px, t.getColor()), 
										 new Triangle3D(b, px, c, t.getColor())};
			}
			
		}else if (l.contains(c)) {
			Vector3D px = Line3D.intersection(l, ab);
			double x = ab.parameter(px);
			
			if (x < 0.0 || x > 1.0) {
				return new Triangle3D[] {t};
			}else{
				return new Triangle3D[] {new Triangle3D(a, px, c, t.getColor()), 
										 new Triangle3D(b, px, c, t.getColor())};
			}
			
		}else{
			Vector3D px1 = Line3D.intersection(l, ab);
			Vector3D px2 = Line3D.intersection(l, ac);
			Vector3D px3 = Line3D.intersection(l, bc);
			
			double x1 = (px1 != null) ? ab.parameter(px1) : -1.0;
			double x2 = (px2 != null) ? ac.parameter(px2) : -1.0;
			double x3 = (px3 != null) ? bc.parameter(px3) : -1.0;
			
			if (x1 > 0.0 && x1 < 1.0 && 
				x2 > 0.0 && x2 < 1.0) {
				return new Triangle3D[] {new Triangle3D(a, px1, px2, t.getColor()), 
		 				 				 new Triangle3D(px1, px2, b, t.getColor()), 
		 				 				 new Triangle3D(px2, b, c, t.getColor())};
				
			}else if (x1 > 0.0 && x1 < 1.0 && 
					  x3 > 0.0 && x3 < 1.0) {
				return new Triangle3D[] {new Triangle3D(b, px1, px3, t.getColor()), 
		 				 				 new Triangle3D(px1, px3, a, t.getColor()), 
		 				 				 new Triangle3D(px3, a, c, t.getColor())};
				
			}else if (x2 > 0.0 && x2 < 1.0 && 
					  x3 > 0.0 && x3 < 1.0) {
				return new Triangle3D[] {new Triangle3D(c, px2, px3, t.getColor()), 
		 				 				 new Triangle3D(px2, px3, a, t.getColor()), 
		 				 				 new Triangle3D(px3, a, b, t.getColor())};
				
			}else{
				return new Triangle3D[] {t};
			}
		}
	}

	public static Triangle3D[] intersect(Plane3D a, Triangle3D t) {
		Plane3D b = t.getPlane();
		
		if (Plane3D.parallel(a, b)) {
			return new Triangle3D[] {t};
		}
		
		Line3D l = Plane3D.intersection(a, b);
		
		return intersect(l, t);
	}
	
	public static Triangle3D[] intersect(Triangle3D a, Triangle3D b) {
		Plane3D c = a.getPlane();
		Plane3D d = b.getPlane();
		
		if (Plane3D.parallel(c, d)) {
			return new Triangle3D[] {a, b};
		}
		
		Line3D l = Plane3D.intersection(c, d);
		
		Triangle3D[] as = intersect(l, a);
		Triangle3D[] bs = intersect(l, b);
		
		Triangle3D[] intersection = new Triangle3D[as.length + bs.length];
		
		System.arraycopy(as, 0, 
		                 intersection, 0, 
		                 as.length);
		System.arraycopy(bs, 0, 
		                 intersection, as.length, 
		                 bs.length);
		
		return intersection;
	}
	
	public static Triangle3D[] intersect(Triangle3D[] triangles) {
		return triangles; //TODO: implement better solution
	}
	
}
