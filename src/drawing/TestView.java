package drawing;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import model.geometry2D.*;

@SuppressWarnings("serial")
public class TestView extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	private double x1off, x2off;
	private Triangle2D a;
	private Triangle2D b;
	
	private Point s;
	
	public TestView() {
		x1off = 0.0;
		x2off = 0.0;
		
		a = new Triangle2D(new Vector2D(300.0, 300.0), 
		                   new Vector2D(400.0, 310.0), 
		                   new Vector2D(305.0, 350.0));
		b = new Triangle2D(new Vector2D(300.0 + x1off, 300.0 + x2off), 
		                   new Vector2D(500.0 + x1off, 320.0 + x2off), 
		                   new Vector2D(315.0 + x1off, 450.0 + x2off));
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		s = e.getPoint();
	}
	
	public void mouseDragged(MouseEvent e) {		
		x1off += (e.getX() - s.getX());
		x2off += (e.getY() - s.getY());
		
		s = e.getPoint();
		
		a = new Triangle2D(new Vector2D(300.0, 300.0), 
		                   new Vector2D(400.0, 310.0), 
		                   new Vector2D(305.0, 350.0));
		b = new Triangle2D(new Vector2D(300.0 + x1off, 300.0 + x2off), 
		                   new Vector2D(500.0 + x1off, 320.0 + x2off), 
		                   new Vector2D(315.0 + x1off, 450.0 + x2off));
		
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		s = null;
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			x2off--;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			x2off++;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			x1off--;
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			x1off++;
		}
		
		a = new Triangle2D(new Vector2D(300.0, 300.0), 
		                   new Vector2D(400.0, 310.0), 
		                   new Vector2D(305.0, 350.0));
		b = new Triangle2D(new Vector2D(300.0 + x1off, 300.0 + x2off), 
		                   new Vector2D(500.0 + x1off, 320.0 + x2off), 
		                   new Vector2D(315.0 + x1off, 450.0 + x2off));
		
		repaint();
	}
	
	public void keyReleased(KeyEvent e) {}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
							 RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
							 RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.lightGray);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		GeneralPath path1 = new GeneralPath(Path2D.WIND_NON_ZERO);
		
		path1.moveTo(a.a().x1(), a.a().x2());
		path1.lineTo(a.b().x1(), a.b().x2());
		path1.lineTo(a.c().x1(), a.c().x2());
		path1.closePath();

		g2d.setColor(Color.blue);
		g2d.fill(path1);

		GeneralPath path2 = new GeneralPath(Path2D.WIND_NON_ZERO);
		
		path2.moveTo(b.a().x1(), b.a().x2());
		path2.lineTo(b.b().x1(), b.b().x2());
		path2.lineTo(b.c().x1(), b.c().x2());
		path2.closePath();
		
		g2d.setColor(Color.green);
		g2d.fill(path2);
		
		for (Triangle2D t : Triangle2D.intersection(a, b)) {
			GeneralPath path = new GeneralPath(Path2D.WIND_NON_ZERO);
			
			path.moveTo(t.a().x1(), t.a().x2());
			path.lineTo(t.b().x1(), t.b().x2());
			path.lineTo(t.c().x1(), t.c().x2());
			path.closePath();
			
			g2d.setColor(Color.red);
			g2d.fill(path);
			g2d.draw(path);
		}
	}
	
}
