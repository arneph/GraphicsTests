package model.geometry3D;

import java.util.*;

public class Model3D {
	private Polygon3D[] polygons;
	private Camera3D[] cameras;
	
	//Cache:
	private boolean cacheIsValid;
	private Vector3D min;
	private Vector3D max;
	private Vector3D center;
	private Triangle3D[] preparedTriangles;
	
	private boolean[] cacheForCamerasIsValid;
	private Triangle3D[][] preparedTrianglesForCameras;
	
	public Model3D() {
		polygons = new Polygon3D[0];
		cameras = new Camera3D[0];
		
		cacheIsValid = false;
		min = null;
		max = null;
		center = null;
		preparedTriangles = null;
		
		cacheForCamerasIsValid = new boolean[0];
		preparedTrianglesForCameras = new Triangle3D[0][0];
	}
	
	public int getNumberOfPolygons() {
		return polygons.length;
	}
	
	public Polygon3D[] getPolygons() {
		return polygons;
	}
	
	public Polygon3D getPolygonAtIndex(int index) {
		if (index < 0 || index >= polygons.length) {
			throw new IllegalArgumentException();
		}
		
		return polygons[index];
	}
	
	public void clearModel() {
		polygons = new Polygon3D[0];
		
		cacheIsValid = false;
		
		for (int i = 0; i < cameras.length; i++) {
			cacheForCamerasIsValid[i] = false;
		}
	}
	
	public void addPolygon(Polygon3D polygon) {
		if (polygon == null) {
			throw new IllegalArgumentException();
		}
		
		int n = polygons.length;
		
		polygons = Arrays.copyOf(polygons, n + 1);
		polygons[n] = polygon;
		
		cacheIsValid = false;

		for (int i = 0; i < cameras.length; i++) {
			cacheForCamerasIsValid[i] = false;
		}
	}
	
	public void addPolygons(Polygon3D[] ps) {
		if (ps == null) {
			throw new IllegalArgumentException();
		}
		
		int n = polygons.length;
		int m = ps.length;
		
		polygons = Arrays.copyOf(polygons, n + m);
		System.arraycopy(ps, 0, polygons, n, m);
		
		cacheIsValid = false;
		
		for (int i = 0; i < cameras.length; i++) {
			cacheForCamerasIsValid[i] = false;
		}
	}
	
	public int getNumberOfCameras() {
		return cameras.length;
	}
	
	public Camera3D[] getCameras() {
		return cameras;
	}
	
	public Camera3D getCameraAtIndex(int index) {
		if (index < 0 || index >= cameras.length) {
			throw new IllegalArgumentException();
		}
		
		return cameras[index];
	}
	
	public void clearCameras() {
		cameras = new Camera3D[0];
		
		cacheForCamerasIsValid = new boolean[0];
		preparedTrianglesForCameras = new Triangle3D[0][0];
	}
	
	public void addCamera(Camera3D camera) {
		if (camera == null) {
			throw new IllegalArgumentException();
		}
		
		int n = cameras.length;
		
		cameras = Arrays.copyOf(cameras, n + 1);
		cameras[n] = camera;
		
		cacheForCamerasIsValid = Arrays.copyOf(cacheForCamerasIsValid, n + 1);
		cacheForCamerasIsValid[n] = false;
		
		preparedTrianglesForCameras = Arrays.copyOf(preparedTrianglesForCameras, n + 1);
		preparedTrianglesForCameras[n] = new Triangle3D[0];
	}
	
	public void addCameras(Camera3D[] cs) {
		if (cs == null) {
			throw new IllegalArgumentException();
		}
		
		int n = cameras.length;
		int m = cs.length;
		
		cameras = Arrays.copyOf(cameras, n + m);
		System.arraycopy(cs, 0, cameras, n, m);
		
		cacheForCamerasIsValid = Arrays.copyOf(cacheForCamerasIsValid, n + m);
		preparedTrianglesForCameras = Arrays.copyOf(preparedTrianglesForCameras, n + m);
		
		for (int i = n; i < n + m; i++) {
			cacheForCamerasIsValid[i] = false;
			preparedTrianglesForCameras[i] = new Triangle3D[0];
		}
	}
	
	public Vector3D getMin() {
		if (cacheIsValid == false) {
			recalculateCache();
		}
		
		return min;
	}
	
	public Vector3D getMax() {
		if (cacheIsValid == false) {
			recalculateCache();
		}
		
		return max;
	}
	
	public Vector3D getCenter() {
		if (cacheIsValid == false) {
			recalculateCache();
		}
		
		return center;
	}
	
	public Triangle3D[] getPreparedTriangles() {
		if (cacheIsValid == false) {
			recalculateCache();
		}
		
		return preparedTriangles;
	}
	
	public Triangle3D[] getPreparedTrianglesForCamera(int index) {
		if (index < 0 || index >= cameras.length) {
			throw new IllegalArgumentException();
		}
		
		if (cacheIsValid == false) {
			recalculateCache();
		}
		if (cacheForCamerasIsValid[index] == false) {
			recalculateCacheForCamera(index);
		}
		
		return preparedTrianglesForCameras[index];
	}
	
	private void recalculateCache() {
		if (cacheIsValid) {
			return;
		}
		
		cacheIsValid = true;
		
		double minX = Double.NaN;
		double maxX = Double.NaN;
		double minY = Double.NaN;
		double maxY = Double.NaN;
		double minZ = Double.NaN;
		double maxZ = Double.NaN;
		
		for (Polygon3D polygon : polygons) {
			for (Vector3D v : polygon.getPoints()) {
				double x = v.x1();
				double y = v.x2();
				double z = v.x3();
				
				if (Double.isNaN(minX) || 
					minX > x) {
					minX = x;
				}
				if (Double.isNaN(maxX) || 
					maxX < x) {
					maxX = x;
				}
				
				if (Double.isNaN(minY) || 
					minY > y) {
					minY = y;
				}
				if (Double.isNaN(maxY) || 
					maxY < y) {
					maxY = y;
				}
					
				if (Double.isNaN(minZ) || 
					minZ > z) {
					minZ = z;
				}
				if (Double.isNaN(maxZ) || 
					maxZ < z) {
					maxZ = z;
				}
			}
		}
		
		if (polygons.length > 0) {
			min = new Vector3D(minX, minY, minZ);
			max = new Vector3D(maxX, maxY, maxZ);
			center = new Vector3D(minX + (maxX - minX) / 2.0, 
			                      minY + (maxY - minY) / 2.0, 
			                      minZ + (maxZ - minZ) / 2.0);
		}else{
			min = Vector3D.origin;
			max = Vector3D.origin;
			center = Vector3D.origin;
		}
		
		preparedTriangles = Polygon3D.triangles(polygons);
	}
	
	private void recalculateCacheForCamera(int index) {
		if (index < 0 || index >= cameras.length) {
			throw new IllegalArgumentException();
		}else if (cacheForCamerasIsValid[index] == true) {
			return;
		}
		
		if (cacheIsValid == false) {
			recalculateCache();
		}
		
		cacheForCamerasIsValid[index] = true;
		
		Camera3D camera = cameras[index];
		Plane3D cameraPlane = camera.plane();
		
		int n = preparedTriangles.length;
		int m = 0;
		
		preparedTrianglesForCameras[index] = new Triangle3D[n];
		
		for (int i = 0; i < n; i++) {
			Triangle3D p = preparedTriangles[i];
			Vector3D c = p.getCenter();
			
			Line3D l = new Line3D(camera.eye(), 
			                      Vector3D.subtract(c, camera.eye()));
			Vector3D pi = Plane3D.intersection(l, cameraPlane);
			double ti = l.parameter(pi);
			
			if (ti <= 0.0 || ti > 1.0) {
				continue;
			}else{
				preparedTrianglesForCameras[index][m] = p;
				m++;
			}
		}
		
		preparedTrianglesForCameras[index] = Arrays.copyOf(preparedTrianglesForCameras[index], m);
		
		for (int i = 0; i < m - 1; i++) {
			Triangle3D p1 = preparedTrianglesForCameras[index][i];
			Triangle3D p2 = preparedTrianglesForCameras[index][i + 1];
			
			double d1 = Vector3D.subtract(p1.getCenter(), camera.eye()).norm();
			double d2 = Vector3D.subtract(p2.getCenter(), camera.eye()).norm();
			
			if (d2 > d1) {
				preparedTrianglesForCameras[index][i] = p2;
				preparedTrianglesForCameras[index][i + 1] = p1;
				
				if (i > 0) i -= 2;
			}
		}
	}
	
}
