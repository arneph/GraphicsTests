package graphicsTests;

import java.awt.*;

import javax.swing.*;

import model.basis.Color;
import model.geometry3D.*;
import drawing.*;

public class GraphicsTests {
	
	public static void main(String[] args) {
		/*
		Vector3D a = new Vector3D(0, 0, 0);
		Vector3D b = new Vector3D(0, 0, 1);
		Vector3D c = new Vector3D(0, 1, 1);
		Vector3D d = new Vector3D(0, 1, 0);
		Vector3D e = new Vector3D(1, 0, 0);
		Vector3D f = new Vector3D(1, 0, 1);
		Vector3D g = new Vector3D(1, 1, 1);
		Vector3D h = new Vector3D(1, 1, 0);
		
		Polygon3D s1 = new Polygon3D(new Vector3D[] {a, b, c, d}, 
		                             new Color(0, 255, 0));
		Polygon3D s2 = new Polygon3D(new Vector3D[] {e, f, g, h}, 
		                             new Color(0, 255, 255));
		Polygon3D s3 = new Polygon3D(new Vector3D[] {a, e, f, b}, 
		                             new Color(0, 0, 255));
		Polygon3D s4 = new Polygon3D(new Vector3D[] {c, g, h, d}, 
		                             new Color(255, 255, 0));
		Polygon3D s5 = new Polygon3D(new Vector3D[] {a, e, h, d}, 
		                             new Color(255, 0, 0));
		Polygon3D s6 = new Polygon3D(new Vector3D[] {b, c, g, f}, 
		                             new Color(255, 0, 255));
		
		Polygon3D base = new Polygon3D(new Vector3D[] {new Vector3D(0.0, 0.0, 0.0), 
		                               				   new Vector3D(1.0, 0.0, 0.0), 
		                               				   new Vector3D(1.0, 0.2, 0.0), 
		                               				   new Vector3D(0.6, 0.2, 0.0), 
		                               				   new Vector3D(0.6, 0.8, 0.0), 
		                               				   new Vector3D(1.0, 0.8, 0.0), 
		                               				   new Vector3D(1.0, 1.0, 0.0), 
		                               				   new Vector3D(0.0, 1.0, 0.0), 
		                               				   new Vector3D(0.0, 0.8, 0.0), 
		                               				   new Vector3D(0.4, 0.8, 0.0), 
		                               				   new Vector3D(0.4, 0.2, 0.0), 
		                               				   new Vector3D(0.0, 0.2, 0.0)}, 
		                               new Color(0, 127, 255));
		base.getTriangles();
		Polygon3D[] prism = Polygon3D.prism(base, Vector3D.k);
		
		Camera3D camera = new Camera3D(new Vector3D(5.0, 0.5, 0.5), 
		                               new Vector3D(0, 1, 0), 
		                               new Vector3D(0, 0, 1), 
		                               1.0, 
		                               1200.0);
		
		Model3D model = new Model3D();
		/*
		model.addPolygon(s1);
		model.addPolygon(s2);
		model.addPolygon(s3);
		model.addPolygon(s4);
		model.addPolygon(s5);
		model.addPolygon(s6);
		*//*
		model.addPolygons(prism);
		
		model.addCamera(camera);*/
		
		JFrame window = new JFrame();
		TestView view = new TestView();
		//Graphics3DView view = new Graphics3DView();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//view.setModel(model);
		view.setPreferredSize(new Dimension(720, 405));
		
		window.setLayout(new BorderLayout());
		window.add(view);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		view.requestFocusInWindow();
		/*
		for (int alpha = 0; true; alpha = (alpha + 1) % 720) {
			Camera3D nc = Camera3D.offset(camera, 
			                              new Vector3D(0, 0, 1.5 * Math.sin(Math.toRadians(alpha * 1.5))));
			
			nc = Camera3D.rotateAboutPoint(nc, model.getCenter(), alpha);
			
			model.clearCameras();
			model.addCamera(nc);
			
			//view.paintImmediately(view.getBounds());
			
			try {
				Thread.sleep(16);
			} catch (Exception e1) {
			}
		}*/
	}

}
