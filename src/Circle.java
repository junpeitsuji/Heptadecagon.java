import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


public class Circle implements Drawer {
	final int tMax = 100;
	int t = 0;

	double x1, y1, r;

	Color col;
	BasicStroke stroke;

	public double getX() {
		return x1;
	}

	public double getY() {
		return y1;
	}

	public double getR() {
		return r;
	}

	public Circle(double x1, double y1, double r, Color col, BasicStroke stroke){
		this.x1 = x1;
		this.y1 = y1;
		this.r  = r;

		this.col = col;
		this.stroke = stroke;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public void paint(Graphics2D g2, int w, int h, double lx, double ly){

		for(int t=0; t<this.t&&t<tMax; t++){

		double tx1 = x1 + r*Math.cos(2.0*Math.PI*t/(double)tMax);
		double ty1 = y1 + r*Math.sin(2.0*Math.PI*t/(double)tMax);
		double tx2 = x1 + r*Math.cos(2.0*Math.PI*(t+1)/(double)tMax);
		double ty2 = y1 + r*Math.sin(2.0*Math.PI*(t+1)/(double)tMax);

		int a1 = DrawPanel.xtoa(tx1, lx, ly, w, h);
		int b1 = DrawPanel.ytob(ty1, lx, ly, w, h);
		int a2 = DrawPanel.xtoa(tx2, lx, ly, w, h);
		int b2 = DrawPanel.ytob(ty2, lx, ly, w, h);

		g2.setPaint(col);

		g2.setStroke(stroke);
		g2.draw(new Line2D.Double(a1, b1, a2, b2));

		}

	}

}
