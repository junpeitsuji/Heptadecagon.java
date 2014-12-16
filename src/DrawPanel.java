import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DrawPanel extends JPanel implements MouseListener, ChangeListener {

	public List<Point2D.Double> crossPoints = new ArrayList<Point2D.Double>();

	public List<Drawer> lines = new ArrayList<Drawer>();
	public int dt = 1;

	Point2D.Double[] choosingPoints = { new Point2D.Double(0, 0), new Point2D.Double(1, 0), new Point2D.Double(0, 0) };
	public int index = 0;

	String state = "Line";

	double lx = 2.0, ly = 2.0;

	Color lineColor = Color.gray;

	BasicStroke WIDE_STROKE  = 	new BasicStroke(5.0f);
	BasicStroke DEFAULT_STROKE  = 	new BasicStroke(1.0f);

	BasicStroke lineStroke = DEFAULT_STROKE;

	boolean addCrossPoints = true;

	public void expand(double ex){
		lx = lx * ex;
		ly = ly * ex;
	}

	public BasicStroke getLineStroke() {
		return lineStroke;
	}

	public void setLineStroke(BasicStroke lineStroke) {
		this.lineStroke = lineStroke;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public DrawPanel() {
		super();
		addMouseListener(this);

		crossPoints.add(new Point2D.Double(0,0));
		crossPoints.add(new Point2D.Double(1,0));
		//crossPoints.add(new Point2D.Double(0.25,0));
		crossPoints.add(new Point2D.Double(0,1));
		crossPoints.add(new Point2D.Double(0,-1));
		//crossPoints.add(new Point2D.Double((-1+Math.sqrt(5))*0.25,0));
		//lines.add(new Line(0, 0, 1, 0, Color.gray));

	}

	public void addCircle() {


		if (state.equals("Circle")) {

			double x1 = choosingPoints[0].x;
			double y1 = choosingPoints[0].y;
			double x2 = choosingPoints[1].x;
			double y2 = choosingPoints[1].y;
			double x3 = choosingPoints[2].x;
			double y3 = choosingPoints[2].y;

			double r = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

			lines.add(new Circle(x3, y3, r, lineColor, lineStroke));

			for (int i = 0; i < lines.size() - 1; i++) {

				Drawer d1 = lines.get(i);
				Drawer d2 = lines.get(lines.size()-1);

				if(d1 instanceof Circle){
					Circle c1 = (Circle) d1;
					Circle c2 = (Circle) d2;

					if(this.addCrossPoints)
						cpCircleByCircle(c1, c2);

				}
				else {
					Line l1 = (Line) d1;
					Circle c2 = (Circle) d2;

					if(this.addCrossPoints)
						cpLineByCircle(l1, c2);
				}
			}

		} else if (state.equals("Line")) {

			double x1 = choosingPoints[0].x;
			double y1 = choosingPoints[0].y;
			double x2 = choosingPoints[1].x;
			double y2 = choosingPoints[1].y;

			lines.add(new Line(x1, y1, x2, y2, lineColor, lineStroke));

			for (int i = 0; i < lines.size() - 1; i++) {

				Drawer d1 = lines.get(i);
				Drawer d2 = lines.get(lines.size()-1);

				if(d1 instanceof Line){
					Line l1 = (Line) d1;
					Line l2 = (Line) d2;

					if(this.addCrossPoints)
						cpLineByLine(l1, l2);

				}
				else{
					Line l1 = (Line) d2;
					Circle c2 = (Circle) d1;

					if(this.addCrossPoints)
						cpLineByCircle(l1, c2);
				}
			}
		}
		else if(state.equals("Division")){
			double x1 = choosingPoints[0].x;
			double y1 = choosingPoints[0].y;
			double x2 = choosingPoints[1].x;
			double y2 = choosingPoints[1].y;

			double x = (x1 + x2)*0.5;
			double y = (y1 + y2)*0.5;

			if(this.addCrossPoints)
				crossPoints.add( new Point2D.Double(x, y) );
		}
	}

	public void cpLineByLine(Line l1, Line l2){
		double a1 = l1.getA();
		double b1 = l1.getB();
		double c1 = l1.getC();

		double a2 = l2.getA();
		double b2 = l2.getB();
		double c2 = l2.getC();

		double det = a1 * b2 - a2 * b1;
		if (det != 0) {
			double x = (b1 * c2 - b2 * c1) / det;
			double y = -(a1 * c2 - a2 * c1) / det;

			crossPoints.add( new Point2D.Double(x, y) );
		}
	}

	public void cpCircleByCircle(Circle c1, Circle c2){
		double x1 = c1.getX();
		double y1 = c1.getY();
		double r1 = c1.getR();

		double x2 = c2.getX();
		double y2 = c2.getY();
		double r2 = c2.getR();

		x2 = x2 - x1;
		y2 = y2 - y1;

		double a = (x2*x2 + y2*y2 + r1*r1 - r2*r2) * 0.5;

		double d = (x2*x2 + y2*y2) * r1*r1 - a*a;

		if (d >= 0) {
			double xp = (a*x2 + y2*Math.sqrt(d)) / (x2*x2+y2*y2) + x1;
			double yp = (a*y2 - x2*Math.sqrt(d)) / (x2*x2+y2*y2) + y1;
			double xm = (a*x2 - y2*Math.sqrt(d)) / (x2*x2+y2*y2) + x1;
			double ym = (a*y2 + x2*Math.sqrt(d)) / (x2*x2+y2*y2) + y1;

			crossPoints.add( new Point2D.Double(xp, yp) );
			crossPoints.add( new Point2D.Double(xm, ym) );
		}
	}

	public void cpLineByCircle(Line l1, Circle c2){
		double a = l1.getA();
		double b = l1.getB();
		double c = l1.getC();

		double x = c2.getX();
		double y = c2.getY();
		double r = c2.getR();

		double d = a * x + b * y + c;

		if ( (a*a+b*b)*r*r - d*d>=0) {
			double Det = (a*a+b*b)*r*r - d*d;
			double xp = (-a*d + b*Math.sqrt(Det))/(a*a+b*b) + x;
			double yp = (-b*d - a*Math.sqrt(Det))/(a*a+b*b) + y;
			double xm = (-a*d - b*Math.sqrt(Det))/(a*a+b*b) + x;
			double ym = (-b*d + a*Math.sqrt(Det))/(a*a+b*b) + y;

			crossPoints.add( new Point2D.Double(xp, yp) );
			crossPoints.add( new Point2D.Double(xm, ym) );
		}
	}


	@Override
	public void paintComponent(Graphics g) {

		int w = this.getSize().width;
		int h = this.getSize().height;

		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);

		Graphics2D g2 = (Graphics2D)g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		BasicStroke wideStroke = new BasicStroke(4.0f);


		for (Drawer line : lines) {
			line.paint(g2, w, h, lx, ly);
		}

		g.setColor(Color.green);
		for(Point2D.Double p : crossPoints){

			int a = xtoa(p.x, lx, ly, w, h);
			int b = ytob(p.y, lx, ly, w, h);
			int r = 3;

			g.fillOval(a - r, b - r, 2 * r, 2 * r);
		}

		//g.setColor(Color.red);
		//int r = 5;
		{

			int a0 = xtoa(choosingPoints[0].x, lx, ly, w, h);
			int b0 = ytob(choosingPoints[0].y, lx, ly, w, h);
			int a1 = xtoa(choosingPoints[1].x, lx, ly, w, h);
			int b1 = ytob(choosingPoints[1].y, lx, ly, w, h);

			int l = 40;
			double r = 0.5;

			g2.setPaint(Color.PINK);
			g2.setStroke(wideStroke);

			g2.draw(new Line2D.Double(a0, b0, a0, b0-l));
			g2.draw(new Line2D.Double(a0, b0, (int)(a0+0.5*r*l), (int)(b0-1.732*0.5*r*l)));
			g2.draw(new Line2D.Double(a0, b0, (int)(a0-0.5*r*l), (int)(b0-1.732*0.5*r*l)));

			//g.drawLine(a0, b0, a0, b0-l);
			//g.drawLine();
			//g.drawLine();
			g2.draw(new Line2D.Double(a1, b1, a1, b1-l));
			g2.draw(new Line2D.Double(a1, b1, (int)(a1+0.5*r*l), (int)(b1-1.732*0.5*r*l)));
			g2.draw(new Line2D.Double(a1, b1, (int)(a1-0.5*r*l), (int)(b1-1.732*0.5*r*l)));

		}

		//int r = 5;
		if (state.equals("Circle")) {
			g2.setPaint(Color.BLUE);
			g2.setStroke(wideStroke);

			int a = xtoa(choosingPoints[2].x, lx, ly, w, h);
			int b = ytob(choosingPoints[2].y, lx, ly, w, h);

			//int l = 40;
			int r = 8;
			g.drawLine((int)(a-1.41*0.5*r), (int)(b-1.41*0.5*r), (int)(a+1.41*0.5*r), (int)(b+1.41*0.5*r));
			g.drawLine((int)(a+1.41*0.5*r), (int)(b-1.41*0.5*r), (int)(a-1.41*0.5*r), (int)(b+1.41*0.5*r));

			g.drawOval(a - r, b - r, 2 * r, 2 * r);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int w = this.getSize().width;
		int h = this.getSize().height;

		int ax = e.getX();
		int ay = e.getY();

		double ppx = atox(ax, lx, ly, w, h);
		double ppy = btoy(ay, lx, ly, w, h);

		// Nearest Neighborsの探索
		if(crossPoints.size()>0){
			int index = 0;
			Point2D.Double cp0 = crossPoints.get(0);

			double min = (cp0.x-ppx)*(cp0.x-ppx) + (cp0.y-ppy)*(cp0.y-ppy);
			for(int i=1; i<crossPoints.size(); i++){
				Point2D.Double cp = crossPoints.get(i);
				double d = (cp.x-ppx)*(cp.x-ppx) + (cp.y-ppy)*(cp.y-ppy);

				if(d < min){
					min = d;
					index = i;
				}
			}

			if (state.equals("Line")) {

				choosingPoints[this.index].x = crossPoints.get(index).x;
				choosingPoints[this.index].y = crossPoints.get(index).y;

				this.index = (this.index + 1) % 2;
			} else {
				choosingPoints[this.index].x = crossPoints.get(index).x;
				choosingPoints[this.index].y = crossPoints.get(index).y;

				this.index = (this.index + 1) % 3;
			}
			this.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		Object source = e.getSource();
		if( source instanceof JRadioButton ){
		JRadioButton cb = (JRadioButton) source;
		if (cb.isSelected()) {

			String text = cb.getText();
			if( text.equals("Line") || text.equals("Circle") || text.equals("Division") ){
				this.state = text;
				index = 0;

				this.repaint();
			}
			else if( text.equals("1px") ){
				this.lineStroke = DEFAULT_STROKE;
			}
			else if( text.equals("5px") ){
				this.lineStroke = WIDE_STROKE;
			}
		}
		}
		else if( source instanceof JCheckBox ){
			JCheckBox cb = (JCheckBox) source;

			this.addCrossPoints = cb.isSelected();
		}
	}

	public static int xtoa(double x, double lx, double ly, int w, int h){
		int a = (int)( w/2 + (w/2)*x/lx);
		return a;
	}
	public static int ytob(double y, double lx, double ly, int w, int h){
		int b = (int)( h/2 - (h/2)*y/ly);
		return b;
	}
	public static double atox(int a, double lx, double ly, int w, int h){
		double x = (a - w / 2) * lx / (w / 2);
		return x;
	}
	public static double btoy(int b, double lx, double ly, int w, int h){
		double y = (h / 2 - b) * ly / (h / 2);
		return y;
	}
}
