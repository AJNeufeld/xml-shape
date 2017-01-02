package ca.seinesoftware.xml.shape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class ShapeDelegatesTest {

	private static Area testArea() {
		Path2D.Float p2d = new Path2D.Float();
		p2d.moveTo(0, 0);
		p2d.lineTo(8, 0);
		p2d.lineTo(0, 8);
		p2d.closePath();
		Area area = new Area(p2d);

		p2d = new Path2D.Float();
		p2d.moveTo(0, 0);
		p2d.lineTo(8, 0);
		p2d.lineTo(8, 8);
		p2d.closePath();
		area.exclusiveOr(new Area(p2d));

		return area;
	}

	private static void initPath(Path2D path) {
		path.moveTo(1, 1);
		path.lineTo(2, 0);
		path.lineTo(0, 3);
		path.closePath();
	}

	private static GeneralPath testGeneralPath() {
		GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		initPath(gp);
		return gp;
	}

	private static Path2D.Float testPath2DFloat() {
		Path2D.Float path2d = new Path2D.Float(GeneralPath.WIND_NON_ZERO);
		initPath(path2d);
		return path2d;
	}

	private static Path2D.Double testPath2DDbl() {
		Path2D.Double path2d = new Path2D.Double(GeneralPath.WIND_EVEN_ODD);
		initPath(path2d);
		return path2d;
	}

	private static Polygon testPolygon() {
		Polygon polygon = new Polygon();
		polygon.addPoint(5, 0);
		polygon.addPoint(10, 5);
		polygon.addPoint(5, 10);
		polygon.addPoint(0, 5);
		return polygon;
	}

	private Shape serializeShape(Shape shape) throws IOException {
		byte data[];
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (XMLEncoder encoder = new XMLEncoder(baos)) {
				ShapeDelegates.setDelegates(encoder);
				encoder.writeObject(shape);
			}
			// System.out.println(baos.toString("UTF-8"));
			data = baos.toByteArray();
		}
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data); XMLDecoder decoder = new XMLDecoder(bais)) {
			shape = (Shape) decoder.readObject();
		}
		return shape;
	}

	@Test
	public void areaTest() throws IOException {
		Area area = testArea();
		Area actual = (Area) serializeShape(area);
		compareShapes(area, actual);
	}

	@Test
	public void generalPathTest() throws IOException {
		GeneralPath path = testGeneralPath();
		Shape actual = serializeShape(path);
		compareShapes(path, actual);
	}

	@Test
	public void path2DFloatTest() throws IOException {
		Path2D.Float path = testPath2DFloat();
		Shape actual = serializeShape(path);
		compareShapes(path, actual);
	}

	@Test
	public void path2DDoubleTest() throws IOException {
		Path2D.Double path = testPath2DDbl();
		Shape actual = serializeShape(path);
		compareShapes(path, actual);
	}

	@Test
	public void polygonTest() throws IOException {
		Polygon polygon = testPolygon();
		Shape actual = serializeShape(polygon);
		compareShapes(polygon, actual);
	}

	@Test
	public void multiplePathTest() throws IOException {
		Area area = testArea();
		GeneralPath gp = testGeneralPath();
		Path2D.Float pathf = testPath2DFloat();
		Path2D.Double pathd = testPath2DDbl();
		Polygon polygon = testPolygon();

		byte data[];
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (XMLEncoder encoder = new XMLEncoder(baos)) {
				ShapeDelegates.setDelegates(encoder);
				encoder.writeObject(area);
				encoder.writeObject(gp);
				encoder.writeObject(pathf);
				encoder.writeObject(pathd);
				encoder.writeObject(polygon);
			}
			// System.out.println(baos.toString("UTF-8"));
			data = baos.toByteArray();
		}
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data); XMLDecoder decoder = new XMLDecoder(bais)) {
			compareShapes(area, (Shape) decoder.readObject());
			compareShapes(gp, (Shape) decoder.readObject());
			compareShapes(pathf, (Shape) decoder.readObject());
			compareShapes(pathd, (Shape) decoder.readObject());
			compareShapes(polygon, (Shape) decoder.readObject());
		}
	}

	private final static double DELTA = 1e-16;

	private void compareShapes(Shape expected, Shape actual) {
		// Ensure the classes are the same
		assertEquals("Shape classes differ", expected.getClass(), actual.getClass());

		PathIterator iterE = expected.getPathIterator(null);
		PathIterator iterA = actual.getPathIterator(null);

		// Ensure the winding rules are the same
		assertEquals("Winding rules differ", iterE.getWindingRule(), iterA.getWindingRule());

		double expeectedCoords[] = new double[6];
		double actualCoords[] = new double[6];

		while (!iterE.isDone() && !iterA.isDone()) {
			int typeE = iterE.currentSegment(expeectedCoords);
			int typeA = iterE.currentSegment(actualCoords);

			// Ensure the segment types are the same
			assertEquals("Segment types differ", typeE, typeA);

			int n = 0;
			switch (typeE) {
			case PathIterator.SEG_MOVETO:
			case PathIterator.SEG_LINETO:
				n = 2;
				break;
			case PathIterator.SEG_QUADTO:
				n = 4;
				break;
			case PathIterator.SEG_CUBICTO:
				n = 6;
				break;
			case PathIterator.SEG_CLOSE:
				n = 0;
				break;
			default:
				throw new IllegalStateException("Unknown segment type: " + typeE);
			}

			// Ensure the coordinates are the same
			for (int i = 0; i < n; i++) {
				assertEquals("Coordinates differ", expeectedCoords[i], actualCoords[i], DELTA);
			}
			iterE.next();
			iterA.next();
		}

		// printShape("Expected: ", expected);
		// printShape("Actual: ", actual);
		assertTrue("Path lengths differ", iterE.isDone() && iterA.isDone());

		// If the shapes are truly identical, creating Constructive Area
		// Geometry objects, and exclusive or'ing them together will produce an
		// empty area.
		Area difference = new Area(expected);
		difference.exclusiveOr(new Area(actual));
		assertTrue("Areas differ", difference.isEmpty());
	}

	@SuppressWarnings("unused")
	private void printShape(String prefix, Shape shape) {
		System.out.println(prefix + shape.getClass().getName());
		double coord[] = new double[6];
		for (PathIterator pi = shape.getPathIterator(null); !pi.isDone(); pi.next()) {

			int type = pi.currentSegment(coord);

			switch (type) {
			case PathIterator.SEG_MOVETO:
				System.out.format("  moveTo(%f,%f)%n", coord[0], coord[1]);
				break;
			case PathIterator.SEG_LINETO:
				System.out.format("  lineTo(%f,%f)%n", coord[0], coord[1]);
				break;
			case PathIterator.SEG_QUADTO:
				System.out.format("  quadTo(%f,%f ; %f,%f)%n", coord[0], coord[1], coord[2], coord[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				System.out.format("  curveTo(%f,%f ; %f,%f ; %f,%f)%n", coord[0], coord[1], coord[2], coord[3],
						coord[4], coord[5]);
				break;
			case PathIterator.SEG_CLOSE:
				System.out.format("  closePath()%n", coord[0], coord[1]);
				break;
			}
		}
	}
}
