/*
	1- import packages
	2- declare and initialize variables (encapsulation)
	3- Initialize Buttons in init() 
	4- Create abstract parent class and inherit from it
	5- Override abstract methods in children classes
	6- Use mouseEvents to draw 
*/
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.lang.Math;

public class Paint extends Applet implements MouseListener, MouseMotionListener {
 
    private Color currentColor = Color.BLACK; //Default 
    
	private boolean isSolid = false;
    private ArrayList<Shape> shapes = new ArrayList<>(); //Dynamic Array to store any number of drawn shapes

    private int startX, startY, beforeX, beforeY; 
    private int currentMode; // Variable to hold the currently selected tool

	// final ints "Constants" To switch over them to choose the drawing mode
    private static final int RECTANGLE_SHAPE = 1; //The Hungarian Notation
    private static final int OVAL_SHAPE = 2;
    private static final int LINE_SHAPE = 3;
    private static final int PENCIL_SHAPE = 4;
	private static final int ERASER = 5;

    public void init() {
        addMouseListener(this);
        addMouseMotionListener(this);
		
		Button rectangleButton = new Button("Rectangle");
        add(rectangleButton);
        rectangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentMode = RECTANGLE_SHAPE;
				}
				}); //Anonymous inner class
				
        Button ovalButton = new Button("Oval");
		add(ovalButton);
        ovalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentMode = OVAL_SHAPE;
				}
				});
				
        Button lineButton = new Button("Line");
		add(lineButton);
        lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentMode = LINE_SHAPE;
				}
				});
				
		Button pencilButton = new Button("Pencil");
        add(pencilButton);
        pencilButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentMode = PENCIL_SHAPE;
				}
				});
		
		Button redButton = new Button("Red");
        add(redButton);
        redButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					currentColor = Color.RED;
				}
				});
				
		Button greenButton = new Button("Green");
        add(greenButton);
        greenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					currentColor = Color.GREEN;
				}
				});
				
		Button blueButton = new Button("Blue");
        add(blueButton);
        blueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					currentColor = Color.BLUE;
				}
				});
				
		Button blackButton = new Button("Black");
		add(blackButton);
        blackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					currentColor = Color.BLACK;
				}
				});
				
		Button solidButton = new Button("Solid"); 
        add(solidButton);
        solidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					isSolid = !isSolid;
				}
				});

		Button eraseButton = new Button("Erase");
        add(eraseButton);
		eraseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentColor = getBackground();
				currentMode = ERASER;
				}
				});
		
		Button clearButton = new Button("Clear All");
		add(clearButton);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shapes.clear(); //Clears the Array
				repaint();
				}
       });
    }

    public void paint(Graphics g) {
        for (int i = 0; i< shapes.size(); i++) {
            shapes.get(i).draw(g);
        }
    }
	abstract class Shape {
        protected Color color; //Accessible to children classes
        protected boolean isSolid;
        protected int x1, y1, x2, y2;

        public Shape(Color color, boolean isSolid, int x1, int y1, int x2, int y2) {
            this.color = color;
            this.isSolid = isSolid;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
		public abstract void draw(Graphics g); //each child class will override this accordingly
	}

	class Rectangle extends Shape {
		public Rectangle(int x1, int y1, int x2, int y2, Color color, boolean isSolid) {
			super(color, isSolid, x1, y1, x2, y2);
		}

		public void draw(Graphics g) {
			g.setColor(color);
			if (isSolid) {
				g.fillRect(x1, y1, x2 - x1, y2 - y1);
			} else {
				g.drawRect(x1, y1, x2 - x1, y2 - y1);
			}
		}
	}

	class Oval extends Shape {
		public Oval(int x1, int y1, int x2, int y2, Color color, boolean isSolid) {
			super(color, isSolid, x1, y1, x2, y2);
		}

		public void draw(Graphics g) {
			g.setColor(color);
			if (isSolid) {
				g.fillOval(x1, y1, x2 - x1, y2 - y1);
			} else {
				g.drawOval(x1, y1, x2 - x1, y2 - y1);
			}
		}
	}

	class Line extends Shape {
		public Line(int x1, int y1, int x2, int y2, Color color, boolean isSolid) {
			super(color, isSolid, x1, y1, x2, y2);
		}

		public void draw(Graphics g) {
			g.setColor(color);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	public void mousePressed(MouseEvent e) {
			startX = e.getX(); //get press coordinates
			startY = e.getY();
			beforeX = startX; //store previous X
			beforeY = startY; //store previous Y
			repaint();
		}

    public void mouseDragged(MouseEvent e) {
		switch(currentMode){
			case PENCIL_SHAPE:
                int afterX = e.getX();
                int afterY = e.getY();
                shapes.add(new Line(beforeX, beforeY, afterX, afterY, currentColor, isSolid));
                beforeX = afterX;
                beforeY = afterY;
                break;
			case ERASER:
				int newX = e.getX();
                int newY = e.getY();
                shapes.add(new Line(beforeX, beforeY, newX, newY, Color.WHITE, isSolid));
                beforeX = newX;
                beforeY = newY;
                break;
		}
		repaint();
	}

    public void mouseReleased(MouseEvent e) {
		int endX = e.getX(); // get release coordinates
        int endY = e.getY();
        int width = Math.abs(endX - startX); //Absolute allows user to draw in any direction 
        int height = Math.abs(endY - startY); //by assuring the width and height values are positive
        int x = Math.min(startX, endX);// the min returns the minimum of two numbers to determine the top-left corner
        int y = Math.min(startY, endY);
        switch(currentMode) {
            case RECTANGLE_SHAPE:
                shapes.add(new Rectangle(x, y, x + width, y + height, currentColor, isSolid));
                break;
            case OVAL_SHAPE:
                shapes.add(new Oval(x, y, x + width, y + height, currentColor, isSolid));
                break;
            case LINE_SHAPE:
                shapes.add(new Line(startX, startY, endX, endY, currentColor, isSolid));
                break;
        }
        repaint();
	}
    public void mouseClicked(MouseEvent e) {} //Implement unused Methods
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}