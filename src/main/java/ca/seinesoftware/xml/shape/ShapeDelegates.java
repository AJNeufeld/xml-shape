package ca.seinesoftware.xml.shape;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.beans.PersistenceDelegate;
import java.beans.XMLEncoder;

/**
 * Persistence Delegates for {@link java.awt.Shape} objects.
 * <p>
 * The following {@link PersistenceDelegate} classes are defined:
 *
 * <ul>
 * <li>{@link AreaDelegate} for {@link Area} shapes
 * <li>{@link GeneralPathDelegate} for {@link GeneralPath} shapes
 * <li>{@link Path2DDelegate.Float} for {@link java.awt.geom.Path2D.Float
 * Path2D.Float} shapes
 * <li>{@link Path2DDelegate.Double} for {@link java.awt.geom.Path2D.Double
 * Path2D.Double} shapes
 * </ul>
 *
 * @author Arthur Neufeld
 */
public class ShapeDelegates {

	/**
	 * Add all supported {@link PersistenceDelegate}s to the {@link XMLEncoder}
	 *
	 * @param encoder
	 *            - {@code XMLEncoder} which will be encoding {@link Shape}
	 *            objects.
	 */
	public static void setDelegates(XMLEncoder encoder) {
		encoder.setPersistenceDelegate(Area.class, new AreaDelegate());
		encoder.setPersistenceDelegate(GeneralPath.class, new GeneralPathDelegate());
		encoder.setPersistenceDelegate(Path2D.Float.class, new Path2DDelegate.Float());
		encoder.setPersistenceDelegate(Path2D.Double.class, new Path2DDelegate.Double());
	}
}
