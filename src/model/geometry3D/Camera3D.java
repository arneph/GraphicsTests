package model.geometry3D;

import java.awt.geom.*;

import model.basis.Matrix;

public class Camera3D {
	Vector3D center;
	Vector3D hAxis;
	Vector3D vAxis;
	double eyeDistance;
	double zoom;
	
	public Camera3D(Vector3D center, Vector3D hAxis, Vector3D vAxis, double eyeDistance, double zoom) {
		this.center = center;
		this.hAxis = hAxis;
		this.vAxis = vAxis;
		this.eyeDistance = eyeDistance;
		this.zoom = zoom;
	}
	
	public Vector3D center() {
		return center;
	}
	
	public Vector3D hAxis() {
		return hAxis;
	}
	
	public Vector3D vAxis() {
		return vAxis;
	}
	
	public double eyeDistance() {
		return eyeDistance;
	}
	
	public double zoom() {
		return zoom;
	}
	
	public Vector3D normal() {
		return Vector3D.crossProduct(hAxis, vAxis);
	}
	
	public Vector3D eye() {
		return Vector3D.add(center, 
		                    Vector3D.multiply(eyeDistance, normal().unit()));
	}
	
	public Plane3D plane() {
		return new Plane3D(center, normal());
	}
	
	public Point2D projectedPoint(Vector3D v) {
		Line3D l = new Line3D(eye(), 
		                      Vector3D.subtract(v, eye()));
		Vector3D p = Plane3D.intersection(l, plane());
		
		double x = Vector3D.projectionFactor(Vector3D.subtract(p, center), 
		                                     hAxis.unit());
		double y = Vector3D.projectionFactor(Vector3D.subtract(p, center), 
		                                     vAxis.unit());
		
		return new Point2D.Double(x * zoom, y * zoom);
	}
	
	public static Camera3D offset(Camera3D camera, Vector3D offset) {
		return new Camera3D(Vector3D.add(camera.center(), offset), 
		                    camera.hAxis(), 
		                    camera.vAxis(), 
		                    camera.eyeDistance, 
		                    camera.zoom());
	}
	
	public static Camera3D rotateAboutPoint(Camera3D camera, Vector3D v, double alpha) {
		double r_alpha = Math.toRadians(alpha);
		
		Matrix transform = new Matrix(new double[][] {new double[] {+Math.cos(r_alpha), -Math.sin(r_alpha), 0},
													  new double[] {+Math.sin(r_alpha), +Math.cos(r_alpha), 0},
													  new double[] {0, 0, 1}});
		
		Vector3D rc = Vector3D.subtract(camera.center(), v);
		
		return null;
		/*
		rc = Vector3D.transform(transform, rc);
		
		Vector3D nc = Vector3D.add(v, rc);
		Vector3D nh = Vector3D.transform(transform, camera.hAxis());
		Vector3D nv = Vector3D.transform(transform, camera.vAxis());
		
		return new Camera3D(nc, nh, nv, camera.eyeDistance(), camera.zoom);*/
	}
	
}
