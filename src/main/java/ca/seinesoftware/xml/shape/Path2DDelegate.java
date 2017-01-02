package ca.seinesoftware.xml.shape;

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Statement;

/**
 * Persistence Delegates for {@link java.awt.geom.Path2D Path2D} objects.
 *
 * @since 1.0.0
 */
public class Path2DDelegate {

	/**
	 * Persistence Delegates for GeneralPath, Path2D.Float, and Path2D.Double
	 * can indicate the constructors for the corresponding shapes should include
	 * the {@code windingRule} property.
	 */
	final static String[] CONSTRUCTOR_PROPORTIES = { "windingRule" };

	/**
	 * Persistence Delegates for {@link java.awt.geom.Path2D.Float Path2D.Float}
	 * objects.
	 *
	 * @since 1.0.0
	 */
	public static class Float extends DefaultPersistenceDelegate {
		public Float() {
			super(CONSTRUCTOR_PROPORTIES);
		}

		private float coords[] = new float[6];
		private java.lang.Float pnt0[] = new java.lang.Float[0];
		private java.lang.Float pnt1[] = new java.lang.Float[2];
		private java.lang.Float pnt2[] = new java.lang.Float[4];
		private java.lang.Float pnt3[] = new java.lang.Float[6];
		private java.lang.Float pnts[];

		@Override
		protected void initialize(Class<?> cls, Object oldInstance, Object newInstance, Encoder out) {
			super.initialize(cls, oldInstance, newInstance, out);

			Shape shape = (Shape) oldInstance;

			for (PathIterator pi = shape.getPathIterator(null); !pi.isDone(); pi.next()) {
				int type = pi.currentSegment(coords);
				String cmd;
				switch (type) {
				case PathIterator.SEG_CLOSE:
					cmd = "closePath";
					pnts = pnt0;
					break;
				case PathIterator.SEG_MOVETO:
					cmd = "moveTo";
					pnts = pnt1;
					break;
				case PathIterator.SEG_LINETO:
					cmd = "lineTo";
					pnts = pnt1;
					break;
				case PathIterator.SEG_QUADTO:
					cmd = "quadTo";
					pnts = pnt2;
					break;
				case PathIterator.SEG_CUBICTO:
					cmd = "curveTo";
					pnts = pnt3;
					break;
				default:
					throw new IllegalStateException("Unexpected segment type: " + type);
				}

				for (int i = 0; i < pnts.length; i++) {
					pnts[i] = coords[i];
				}

				out.writeStatement(new Statement(oldInstance, cmd, pnts));
			}
		}
	}

	/**
	 * Persistence Delegates for {@link java.awt.geom.Path2D.Double
	 * Path2D.Double} objects.
	 *
	 * @since 1.0.0
	 */
	public static class Double extends DefaultPersistenceDelegate {

		public Double() {
			super(CONSTRUCTOR_PROPORTIES);
		}

		private double coords[] = new double[6];
		private java.lang.Double pnt0[] = new java.lang.Double[0];
		private java.lang.Double pnt1[] = new java.lang.Double[2];
		private java.lang.Double pnt2[] = new java.lang.Double[4];
		private java.lang.Double pnt3[] = new java.lang.Double[6];
		private java.lang.Double pnts[];

		@Override
		protected void initialize(Class<?> cls, Object oldInstance, Object newInstance, Encoder out) {
			super.initialize(cls, oldInstance, newInstance, out);

			Shape shape = (Shape) oldInstance;

			for (PathIterator pi = shape.getPathIterator(null); !pi.isDone(); pi.next()) {
				int type = pi.currentSegment(coords);
				String cmd;
				switch (type) {
				case PathIterator.SEG_CLOSE:
					cmd = "closePath";
					pnts = pnt0;
					break;
				case PathIterator.SEG_MOVETO:
					cmd = "moveTo";
					pnts = pnt1;
					break;
				case PathIterator.SEG_LINETO:
					cmd = "lineTo";
					pnts = pnt1;
					break;
				case PathIterator.SEG_QUADTO:
					cmd = "quadTo";
					pnts = pnt2;
					break;
				case PathIterator.SEG_CUBICTO:
					cmd = "curveTo";
					pnts = pnt3;
					break;
				default:
					throw new IllegalStateException("Unexpected segment type: " + type);
				}

				for (int i = 0; i < pnts.length; i++) {
					pnts[i] = coords[i];
				}

				out.writeStatement(new Statement(oldInstance, cmd, pnts));
			}
		}
	}
}