import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class DrawWindow extends JFrame {

	DrawPanel panel;
	Thread thread;

	public DrawWindow(String title){
		super(title);

		Container content = this.getContentPane();

		{
			panel = new DrawPanel();

			content.add(panel, BorderLayout.CENTER);
			thread = new Thread(){
				@Override
				public void run() {

					while(true){

						for(Drawer line : panel.lines){
							int t = line.getT();
							line.setT(t+panel.dt);
						}
						panel.repaint();

						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread.start();


		}

		{
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1,4));

			{
				JButton button = new JButton("draw");
				panel.add(button);

				button.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						DrawWindow.this.panel.addCircle();
					}

				});

			}

			{
				JButton button = new JButton("color");
				panel.add(button);

				button.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//JColorChooser chooser = new JColorChooser();
						Color color = JColorChooser.showDialog(DrawWindow.this, "色の選択", DrawWindow.this.panel.getLineColor());
						if(color != null)
							DrawWindow.this.panel.setLineColor(color);
						//content.add(chooser, BorderLayout.EAST);
					}

				});

			}

			{
				JButton button = new JButton("+");
				panel.add(button);

				button.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						DrawWindow.this.panel.expand(0.5);
					}

				});

			}
			{
				JButton button = new JButton("-");
				panel.add(button);

				button.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						DrawWindow.this.panel.expand(2.0);
					}

				});

			}


			content.add(panel, BorderLayout.SOUTH);
		}




		{
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(1,2));

		{
			JPanel panel = new JPanel();
			ButtonGroup group = new ButtonGroup();

			JRadioButton button1 = new JRadioButton("Line", true);
			JRadioButton button2 = new JRadioButton("Circle");
			JRadioButton button3 = new JRadioButton("Division");

			button1.addChangeListener(DrawWindow.this.panel);
			button2.addChangeListener(DrawWindow.this.panel);
			button3.addChangeListener(DrawWindow.this.panel);
			group.add(button1);
			group.add(button2);
			group.add(button3);

			panel.setLayout(new FlowLayout());
			panel.add(button1);
			panel.add(button2);
			panel.add(button3);

			p.add(panel);
		}

		{
			JPanel panel = new JPanel();
			ButtonGroup group = new ButtonGroup();

			JRadioButton button1 = new JRadioButton("1px", true);
			JRadioButton button2 = new JRadioButton("5px");
			JCheckBox    button3 = new JCheckBox("交点", true);

			button1.addChangeListener(DrawWindow.this.panel);
			button2.addChangeListener(DrawWindow.this.panel);
			button3.addChangeListener(DrawWindow.this.panel);
			group.add(button1);
			group.add(button2);

			panel.setLayout(new FlowLayout());
			panel.add(button1);
			panel.add(button2);
			panel.add(button3);

			p.add(panel);
		}
		content.add(p, BorderLayout.NORTH);
		}

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(560, 660);
		this.setVisible(true);


	}

	public static void main(String[] args){

		new DrawWindow("no title");
	}
}
