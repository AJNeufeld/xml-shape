package ca.seinesoftware.xml.shape;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLEncoder;

/**
 * Persistence Delegate for {@link Area} CAG (Constructive Area Geometry)
 * shapes.
 * <p>
 * The {@code Area} is written to the XML stream by converting the Area into a
 * {@link Path2D.Double} shape, and writing an {@code <object/>} tag which uses
 * the {@code Path2D} shape as a constructor argument.
 * <p>
 * Since this delegate internally uses a {@link Path2D.Double} shape, the
 * {@link Path2DDelegate.Double} must be added to the {@link XMLEncoder}, in
 * order for this delegate to function properly.
 *
 * @since 1.0.0
 */
public class AreaDelegate extends PersistenceDelegate {

	@Override
	protected Expression instantiate(Object oldInstance, Encoder out) {
		Area area = (Area) oldInstance;
		Path2D.Double path_2d = new Path2D.Double(area);

		return new Expression(oldInstance, oldInstance.getClass(), "new", new Object[] { path_2d });
	}
}