package ca.seinesoftware.xml.shape;

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.PersistenceDelegate;
import java.beans.Statement;

/**
 * Persistence Delegates for {@link java.awt.geom.Path2D Path2D} objects.
 * <p>
 * The following {@link PersistenceDelegate} classes are defined:
 *
 * <ul>
 * <li>{@link Path2DDelegate.Float} for {@link java.awt.geom.Path2D.Float
 * Path2D.Float} shapes
 * <li>{@link Path2DDelegate.Double} for {@link java.awt.geom.Path2D.Double
 * Path2D.Double} shapes
 * </ul>
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
		private java.lang.Float pnts[];

		@Override
		protected void initialize(Class<?> cls, Object oldInstance, Object newInstance, Encoder out) {
			super.initialize(cls, oldInstance, newInstance, out);

			Shape shape = (Shape) oldInstance;

			for (PathIterator pi = shape.getPathIterator(null); !pi.isDone(); pi.next()) {
				int type = pi.currentSegment(coords);
				String method;
				switch (type) {
				case PathIterator.SEG_CLOSE:
					method = "closePath";
					pnts = new java.lang.Float[0];
					break;
				case PathIterator.SEG_MOVETO:
					method = "moveTo";
					pnts = new java.lang.Float[2];
					break;
				case PathIterator.SEG_LINETO:
					method = "lineTo";
					pnts = new java.lang.Float[2];
					break;
				case PathIterator.SEG_QUADTO:
					method = "quadTo";
					pnts = new java.lang.Float[4];
					break;
				case PathIterator.SEG_CUBICTO:
					method = "curveTo";
					pnts = new java.lang.Float[6];
					break;
				default:
					throw new IllegalStateException("Unexpected segment type: " + type);
				}

				for (int i = 0; i < pnts.length; i++) {
					pnts[i] = coords[i];
				}

				out.writeStatement(new Statement(oldInstance, method, pnts));
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
		private java.lang.Double pnts[];

		@Override
		protected void initialize(Class<?> cls, Object oldInstance, Object newInstance, Encoder out) {
			super.initialize(cls, oldInstance, newInstance, out);

			Shape shape = (Shape) oldInstance;

			for (PathIterator pi = shape.getPathIterator(null); !pi.isDone(); pi.next()) {
				int type = pi.currentSegment(coords);
				String method;
				switch (type) {
				case PathIterator.SEG_CLOSE:
					method = "closePath";
					pnts = new java.lang.Double[0];
					break;
				case PathIterator.SEG_MOVETO:
					method = "moveTo";
					pnts = new java.lang.Double[2];
					break;
				case PathIterator.SEG_LINETO:
					method = "lineTo";
					pnts = new java.lang.Double[2];
					break;
				case PathIterator.SEG_QUADTO:
					method = "quadTo";
					pnts = new java.lang.Double[4];
					break;
				case PathIterator.SEG_CUBICTO:
					method = "curveTo";
					pnts = new java.lang.Double[6];
					break;
				default:
					throw new IllegalStateException("Unexpected segment type: " + type);
				}

				for (int i = 0; i < pnts.length; i++) {
					pnts[i] = coords[i];
				}

				out.writeStatement(new Statement(oldInstance, method, pnts));
			}
		}
	}
}