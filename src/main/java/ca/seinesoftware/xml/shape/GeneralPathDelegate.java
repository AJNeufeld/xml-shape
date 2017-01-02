package ca.seinesoftware.xml.shape;

import java.awt.geom.Path2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLEncoder;

/**
 * Persistence Delegate for {@link java.awt.geom.GeneralPath GeneralPath}
 * objects.
 * <p>
 * Since the {@code GeneralPath} class descends from the {@link Path2D.Float}
 * class, the {@link Path2DDelegate.Float} must also be added to the
 * {@link XMLEncoder}, in order for this delegate to function properly.
 *
 * @since 1.0.0
 */
public class GeneralPathDelegate extends DefaultPersistenceDelegate {
	public GeneralPathDelegate() {
		super(Path2DDelegate.CONSTRUCTOR_PROPORTIES);
	}
}