package ca.seinesoftware.xml.shape;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ShapeDemo {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ShapeDemo();
			}
		});
	}

	ShapeDemo() {
		JFrame frame = new JFrame("Serialization Check");
		Box content_pane = Box.createVerticalBox();
		frame.setContentPane(content_pane);
		frame.setSize(400, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		List<Shape> shapes = createShapes();
		JPanel view;
		view = new View(shapes);
		view.setBorder(BorderFactory.createTitledBorder("Original Shapes"));
		content_pane.add(view);
		view = new View(serializeToFromXML(shapes));
		view.setBorder(BorderFactory.createTitledBorder("Serialized by XML"));
		content_pane.add(view);

		frame.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	private List<Shape> serializeToFromXML(List<Shape> shapes) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (XMLEncoder encoder = new XMLEncoder(baos)) {
				ShapeDelegates.setDelegates(encoder);
				encoder.writeObject(shapes);
			}
			// System.out.println(baos.toString("UTF-8"));
			try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
					XMLDecoder decoder = new XMLDecoder(bais)) {
				return (List<Shape>) decoder.readObject();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	List<Shape> createShapes() {
		int[] xpoints = { 205, 295, 205, 295 };
		int[] ypoints = { 5, 25, 25, 45 };
		List<Shape> shapes = new ArrayList<>();

		shapes.add(new Rectangle2D.Double(5, 5, 90, 40));
		shapes.add(new Ellipse2D.Double(105, 5, 90, 40));
		shapes.add(new Polygon(xpoints, ypoints, xpoints.length));
		shapes.add(new GeneralPath(new Rectangle2D.Double(5, 55, 90, 40)));
		shapes.add(new Path2D.Double(new Rectangle2D.Double(105, 55, 90, 40)));
		shapes.add(new Area(new Rectangle2D.Double(205, 55, 90, 40)));

		shapes.add(new CubicCurve2D.Double(5, 5, 20, 60, 60, -20, 90, 40));
		shapes.add(new Line2D.Float(5, 5, 90, 40));

		shapes.add(new Arc2D.Double(5, 5, 90, 40, 15, 300, Arc2D.PIE));

		shapes.add(new RoundRectangle2D.Double(5, 5, 90, 40, 35, 25));
		return shapes;
	}

	@SuppressWarnings("serial")
	class View extends JPanel {
		View(List<Shape> shapes) {
			super(new FlowLayout());
			for (Shape shape : shapes) {
				JLabel lbl = new JLabel(getIcon(shape));
				add(lbl);
			}
		}

		Icon getIcon(Shape shape) {
			Rectangle r = shape.getBounds();

			BufferedImage img = new BufferedImage(r.width + 10, r.height + 10, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = img.createGraphics();

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, r.width + 10, r.height + 10);

			g.translate(5 - r.x, 5 - r.y);

			g.setColor(Color.GREEN);
			g.fill(shape);

			g.setColor(Color.BLACK);
			g.draw(shape);

			String name = shape.getClass().getName();
			name = name.substring(name.lastIndexOf('.') + 1);
			int i = name.indexOf('$');
			if (i >= 0) {
				name = name.substring(0, i);
			}
			g.drawString(name, r.x + 3, r.y + 12);

			g.setColor(Color.WHITE);

			g.dispose();

			return new ImageIcon(img);
		}
	}
}
