import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


public class Line implements Drawer {

	final int tMax = 100;
	int t = 0;

	double x1, y1, x2, y2;

	double a, b, c;

	Color col;
	BasicStroke stroke;

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public double getC() {
		return c;
	}

	public Line(double x1, double y1, double x2, double y2, Color col, BasicStroke stroke){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

		this.a = -(y2-y1);
		this.b = x2-x1;
		this.c = (y2-y1)*x1 - (x2-x1)*y1;

		this.col = col;
		this.stroke = stroke;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	private int t(int x){
		return (x*9-400);
	}

	public void paint(Graphics2D g2, int w, int h, double lx, double ly){

		int tt = t((t<tMax) ? t : tMax) ;
		//if( t<=tMax ){

		//double tx1 = x1; //(x1 * (tMax - t) / (tMax)) + (x2 * t / (tMax));
		//double ty1 = y1; //(y1 * (tMax - t) / (tMax)) + (y2 * t / (tMax));
		double tx1 = (x1 * (tMax - (t(0))) / (tMax)) + (x2 * (t(0)) / (tMax));
		double ty1 = (y1 * (tMax - (t(0))) / (tMax)) + (y2 * (t(0)) / (tMax));

		double tx2 = (x1 * (tMax - (tt)) / (tMax)) + (x2 * (tt) / (tMax));
		double ty2 = (y1 * (tMax - (tt)) / (tMax)) + (y2 * (tt) / (tMax));

		int a1 = DrawPanel.xtoa(tx1, lx, ly, w, h);
		int b1 = DrawPanel.ytob(ty1, lx, ly, w, h);
		int a2 = DrawPanel.xtoa(tx2, lx, ly, w, h);
		int b2 = DrawPanel.ytob(ty2, lx, ly, w, h);

		g2.setPaint(col);
		g2.setStroke(stroke);
		g2.draw(new Line2D.Double(a1, b1, a2, b2));

		//}

	}
}
