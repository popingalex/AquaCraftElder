package org.aqua.graph.j3d.test;

import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import java.awt.event.*;
import java.awt.*;


public class Picker extends MouseAdapter {

private PickCanvas pickCanvas;


public Picker()

{

    Frame frame = new Frame("Box and Sphere");

    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

    Canvas3D canvas = new Canvas3D(config);

    canvas.setSize(400, 400);

    SimpleUniverse universe = new SimpleUniverse(canvas);

    BranchGroup group = new BranchGroup();


    // create a color cube


    Vector3f vector = new Vector3f(-0.3f, 0.0f, 0.0f);

    Transform3D transform = new Transform3D();

    transform.setTranslation(vector);

    TransformGroup transformGroup = new TransformGroup(transform);

    ColorCube cube = new ColorCube(0.3);

    transformGroup.addChild(cube);



    //create a sphere


    Vector3f vector2 = new Vector3f(+0.3f, 0.0f, 0.0f);

    Transform3D transform2 = new Transform3D();

    transform2.setTranslation(vector2);

    TransformGroup transformGroup2 = new TransformGroup(transform2);

    Appearance appearance = new Appearance();

    appearance.setPolygonAttributes(

       new PolygonAttributes(PolygonAttributes.POLYGON_LINE,

       PolygonAttributes.CULL_BACK,0.0f));

    Sphere sphere = new Sphere(0.3f,appearance);

    transformGroup2.addChild(sphere);


    BoundingSphere viewBound = new BoundingSphere(new Point3d(), 100);
    TransformGroup tg = new TransformGroup();
    group.addChild(tg);;
    tg.addChild(transformGroup);
    tg.addChild(transformGroup2);
    MouseRotate mouseRotateBehavior = new MouseRotate(tg);
    mouseRotateBehavior.setSchedulingBounds(viewBound);
    tg.addChild(mouseRotateBehavior);
    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    
    universe.getViewingPlatform().setNominalViewingTransform();

    universe.addBranchGraph(group);

    frame.addWindowListener(new WindowAdapter() {

       public void windowClosing(WindowEvent winEvent) {

          System.exit(0);

       }

     });

    frame.add(canvas);

    pickCanvas = new PickCanvas(canvas, group);

    pickCanvas.setMode(PickCanvas.BOUNDS);

    canvas.addMouseListener(this);

    frame.pack();

    frame.show();

}


public static void main( String[] args ) {

    new Picker();

}


public void mouseClicked(MouseEvent e)

{

    pickCanvas.setShapeLocation(e);

    PickResult result = pickCanvas.pickClosest();

    if (result == null) {

       System.out.println("Nothing picked");

    } else {

       Primitive p = (Primitive)result.getNode(PickResult.PRIMITIVE);

       Shape3D s = (Shape3D)result.getNode(PickResult.SHAPE3D);

       if (p != null) {

          System.out.println(p.getClass().getName());

       } else if (s != null) {

             System.out.println(s.getClass().getName());

       } else{

          System.out.println("null");

       }

    }

}

} // end of class Pick