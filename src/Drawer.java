import java.awt.Graphics2D;


public interface Drawer {

	public int getT();

	public void setT(int t);

	public void paint(Graphics2D g2, int w, int h, double lx, double ly);

}
