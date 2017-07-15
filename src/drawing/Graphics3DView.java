package drawing;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import model.geometry3D.*;

@SuppressWarnings("serial")
public class Graphics3DView extends JPanel {
	private Model3D model;
	private int cameraIndex;
	
	public Graphics3DView() {
		model = null;
		cameraIndex = 0;
	}
	
	public Model3D getModel() {
		return model;
	}
	
	public void setModel(Model3D model) {
		this.model = model;
		
		repaint();
	}
	
	public int getCameraIndex() {
		return cameraIndex;
	}
	
	public void setCameraIndex(int cameraIndex) {
		this.cameraIndex = cameraIndex;
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
							 RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
							 RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.lightGray);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setTransform(new AffineTransform(new double[] {1.0, 0.0, 0.0, -1.0, getWidth() / 2, getHeight() / 2}));
		
		if (model == null) return;
		if (cameraIndex < 0 || cameraIndex >= model.getNumberOfCameras()) return;
		
		Camera3D camera = model.getCameraAtIndex(cameraIndex);
		Triangle3D[] triangles = model.getPreparedTrianglesForCamera(cameraIndex);
		
		for (Triangle3D triangle : triangles) {
			GeneralPath path = new GeneralPath(Path2D.WIND_NON_ZERO);
			
			Point2D a = camera.projectedPoint(triangle.a());
			Point2D b = camera.projectedPoint(triangle.b());
			Point2D c = camera.projectedPoint(triangle.c());
			
			path.moveTo(a.getX(), a.getY());
			path.lineTo(b.getX(), b.getY());
			path.lineTo(b.getX(), c.getY());
			path.closePath();
			
			g2d.setColor(new Color(triangle.getColor().r(), 
			                       triangle.getColor().g(), 
			                       triangle.getColor().b()));
			g2d.fill(path);
		}
	}
	
}
