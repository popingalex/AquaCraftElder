package org.aqua.graph.j3d.test;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.util.Enumeration;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class CollTest1 extends Applet {

    private SimpleUniverse u = null;

    public BranchGroup createSceneGraph() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

        // Create a Transformgroup to scale all objects so they
        // appear in the scene.
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(0.4);
        objScale.setTransform(t3d);

        // Create a bounds for the background and behaviors
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        // Set up the background
        Color3f bgColor = new Color3f(0.05f, 0.05f, 0.2f);
        Background bg = new Background(bgColor);
        bg.setApplicationBounds(bounds);
        objScale.addChild(bg);

        // Create a pair of transform group nodes and initialize them to
        // identity. Enable the TRANSFORM_WRITE capability so that
        // our behaviors can modify them at runtime. Add them to the
        // root of the subgraph.
        TransformGroup objTrans1 = new TransformGroup();
        objTrans1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objScale.addChild(objTrans1);

        TransformGroup objTrans2 = new TransformGroup();
        objTrans2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans1.addChild(objTrans2);

        // Create the positioning and scaling transform group node.
        Transform3D t = new Transform3D();
        t.set(0.3, new Vector3d(0.0, -1.5, 0.0));
        TransformGroup objTrans3 = new TransformGroup(t);
        objTrans2.addChild(objTrans3);

        // Create a simple shape leaf node, add it to the scene graph.
        objTrans3.addChild(new ColorCube());

        // Create a new Behavior object that will perform the desired
        // rotation on the specified transform object and add it into
        // the scene graph.
        Transform3D yAxis1 = new Transform3D();
        yAxis1.rotX(Math.PI / 2.0);
        Alpha tickTockAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 5000, 2500, 200,
                5000, 2500, 200);

        RotationInterpolator tickTock = new RotationInterpolator(tickTockAlpha, objTrans1, yAxis1,
                -(float) Math.PI / 2.0f, (float) Math.PI / 2.0f);
        tickTock.setSchedulingBounds(bounds);
        objTrans2.addChild(tickTock);

        // Create a new Behavior object that will perform the desired
        // rotation on the specified transform object and add it into
        // the scene graph.
        Transform3D yAxis2 = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 4000, 0, 0, 0, 0, 0);

        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objTrans2, yAxis2, 0.0f,
                (float) Math.PI * 2.0f);
        rotator.setSchedulingBounds(bounds);
        objTrans2.addChild(rotator);

        // Now create a pair of rectangular boxes, each with a collision
        // detection behavior attached. The behavior will highlight the
        // object when it is in a state of collision.

        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        MouseRotate behavior = new MouseRotate();

        behavior.setTransformGroup(tg);
        tg.addChild(behavior);
        behavior.setSchedulingBounds(bounds);
        tg.addChild(objScale);
        objRoot.addChild(tg);
        Group box1 = createBox(0.3, new Vector3d(-1.3, 0.0, 0.0));
        Group box2 = createBox(0.3, new Vector3d(-1.25, 0.0, 0.0));

        objScale.addChild(box1);
        objScale.addChild(box2);
//        objScale.addChild(new Box(1.5f, 0.2f, 0.2f, new Appearance()));

        // Have Java 3D perform optimizations on this scene graph.
        objRoot.compile();

        return objRoot;
    }

    private Group createBox(double scale, Vector3d pos) {
        // Create a transform group node to scale and position the object.
        Transform3D t = new Transform3D();
        t.set(scale, pos);
        TransformGroup objTrans = new TransformGroup(t);

        // Create a simple shape leaf node and add it to the scene graph
        Shape3D shape = new Box2(0.5, 5.0, 1.0);
        objTrans.addChild(shape);

        // Create a new ColoringAttributes object for the shape's
        // appearance and make it writable at runtime.
        Appearance app = shape.getAppearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(0.6f, 0.3f, 0.0f);
        app.setCapability(app.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);

        // Create a new Behavior object that will perform the collision
        // detection on the specified object, and add it into
        // the scene graph.
        CollisionDetector cd = new CollisionDetector(shape);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        cd.setSchedulingBounds(bounds);

        // Add the behavior to the scene graph
        objTrans.addChild(cd);

        return objTrans;
    }

    public CollTest1() {
    }

    public void init() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D c = new Canvas3D(config);
        add("Center", c);

        // Create a simple scene and attach it to the virtual universe
        BranchGroup scene = createSceneGraph();
        u = new SimpleUniverse(c);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        u.getViewingPlatform().setNominalViewingTransform();

        u.addBranchGraph(scene);
    }

    public void destroy() {
        u.cleanup();
    }

    //
    // The following allows TickTockCollision to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
        new MainFrame(new CollTest1(), 700, 700);
    }
}

class CollisionDetector extends Behavior {
    private static final Color3f            highlightColor = new Color3f(0.0f, 1.0f, 0.0f);

    private static final ColoringAttributes highlight      = new ColoringAttributes(highlightColor,
                                                                   ColoringAttributes.SHADE_GOURAUD);

    private boolean                         inCollision    = false;

    private Shape3D                         shape;

    private ColoringAttributes              shapeColoring;

    private Appearance                      shapeAppearance;

    private WakeupOnCollisionEntry          wEnter;

    private WakeupOnCollisionExit           wExit;

    public CollisionDetector(Shape3D s) {
        shape = s;
        shapeAppearance = shape.getAppearance();
        shapeColoring = shapeAppearance.getColoringAttributes();
        inCollision = false;
    }

    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(shape);
        wExit = new WakeupOnCollisionExit(shape);
        wakeupOn(wEnter);
    }

    public void processStimulus(Enumeration criteria) {
        inCollision = !inCollision;

        if (inCollision) {
            shapeAppearance.setColoringAttributes(highlight);
            wakeupOn(wExit);
        } else {
            shapeAppearance.setColoringAttributes(shapeColoring);
            wakeupOn(wEnter);
        }
    }
}

class Box2 extends Shape3D {

    public Box2(double xsize, double ysize, double zsize) {
        super();
        double xmin = -xsize / 2.0;
        double xmax = xsize / 2.0;
        double ymin = -ysize / 2.0;
        double ymax = ysize / 2.0;
        double zmin = -zsize / 2.0;
        double zmax = zsize / 2.0;

        QuadArray box = new QuadArray(24, QuadArray.COORDINATES);

        Point3d verts[] = new Point3d[24];

        // front face
        verts[0] = new Point3d(xmax, ymin, zmax);
        verts[1] = new Point3d(xmax, ymax, zmax);
        verts[2] = new Point3d(xmin, ymax, zmax);
        verts[3] = new Point3d(xmin, ymin, zmax);
        // back face
        verts[4] = new Point3d(xmin, ymin, zmin);
        verts[5] = new Point3d(xmin, ymax, zmin);
        verts[6] = new Point3d(xmax, ymax, zmin);
        verts[7] = new Point3d(xmax, ymin, zmin);
        // right face
        verts[8] = new Point3d(xmax, ymin, zmin);
        verts[9] = new Point3d(xmax, ymax, zmin);
        verts[10] = new Point3d(xmax, ymax, zmax);
        verts[11] = new Point3d(xmax, ymin, zmax);
        // left face
        verts[12] = new Point3d(xmin, ymin, zmax);
        verts[13] = new Point3d(xmin, ymax, zmax);
        verts[14] = new Point3d(xmin, ymax, zmin);
        verts[15] = new Point3d(xmin, ymin, zmin);
        // top face
        verts[16] = new Point3d(xmax, ymax, zmax);
        verts[17] = new Point3d(xmax, ymax, zmin);
        verts[18] = new Point3d(xmin, ymax, zmin);
        verts[19] = new Point3d(xmin, ymax, zmax);
        // bottom face
        verts[20] = new Point3d(xmin, ymin, zmax);
        verts[21] = new Point3d(xmin, ymin, zmin);
        verts[22] = new Point3d(xmax, ymin, zmin);
        verts[23] = new Point3d(xmax, ymin, zmax);

        box.setCoordinates(0, verts);
        setGeometry(box);
        setAppearance(new Appearance());
    }
}