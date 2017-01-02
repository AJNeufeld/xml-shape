xml-shape  [![Build Status](https://travis-ci.org/seinesoftware/xml-shape.svg?branch=master)](https://travis-ci.org/seinesoftware/xml-shape)
=========

Persistence Delegates for java.awt.Shape objects

The `java.beams.XMLEncoder` does not, without additional assistance, properly serialize `Area`, `Path2D`, or `GeneralPath` objects.
Assistance to the `XMLEncoder` may be provided via `PersistenceDelegate` objects registered for the appropriate classes.
This package provides the required assistance.
 
Usage
-----

    import ca.seinesoftware.xml.shapes.ShapeDelegates;
    
    class YourClass {
       void serializeToXML() {
          try (XMLEncoder encoder = new XMLEncoder( ... )) {
             ShapeDelegates.setDelegates(encoder);   // Register Delegates for Area, Path2D, & GeneralPath
             encoder.writeObject( objects );         // Write out desired objects
          }
       }
    }


Implementation Notes
--------------------

XML Encoding of `Path2D` objects is done by writing out statement which,
when read back in by the `XMLDecoder`, will execute methods to reconstruct the original shape.

For example, the following `Path2D.Float` object

    Path2D.Float object = new Path2D.Float(Path2D.WIND_EVEN_ODD);
    object.moveTo(1.0, 1.0);
    object.lineTo(2.0, 0.0);
    object.lineTo(0.0, 3.0);
    object.closePath();

Is written out as:

    <object class="java.awt.geom.Path2D$Float">
     <int>0</int>
     <void method="moveTo">
      <float>1.0</float>
      <float>1.0</float>
     </void>
     <void method="lineTo">
      <float>2.0</float>
      <float>0.0</float>
     </void>
     <void method="lineTo">
      <float>0.0</float>
      <float>3.0</float>
     </void>
     <void method="closePath"/>
    </object>

The `<int>0</int>` in the second line is the argument to the constructor
`java.awt.geom.Path2D.Float(int)`, which sets the winding rule for the `Path2D`.
The `<void method="..."/>` statements exactly mirror the statements used to create the original path.

Since `GeneralPath` objects are derived from the `Path2D.Float` class, they are encoded in exactly the same way.

The `Area` CAG (Constructive Area Geometry) objects are more complex.
They cannot be created with moveTo and lineTo type methods, but rather by
adding, subtracting, or exclusive-or-ing one or more Shape objects.
Since the `Area` object can be constructed from an arbitrary `Shape`,
and a `Path2D` object can be constructed from an `Area` object,
the `Area` object is serialized by serializing the equivalent `Path2D` object,
and using that as the argument to construct the `Area`.

    <object class="java.awt.geom.Area">
      <object class="java.awt.geom.Path2D$Double">
        <int>1</int>
        <!-- moveTo/lineTo/quadTo/curveTo methods for equivalent Path2D -->
      </object>
    </object>

Acknowledgements
----------------
Thanks to [Andrew Thompson][].
Without his [question on StackOverflow][], this project may have ended with just
a persistence delegate for just my own `Path2D` object in my private project.


[Andrew Thompson]: http://stackoverflow.com/users/418556/andrew-thompson
[question on StackOverflow]: http://stackoverflow.com/questions/26579729/how-to-serialize-java-2d-shape-objects-as-xml/

