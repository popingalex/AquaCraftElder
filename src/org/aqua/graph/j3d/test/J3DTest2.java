package org.aqua.graph.j3d.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.LinkedList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingPolytope;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.CompressedGeometry;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.PickBounds;
import javax.media.j3d.QuadArray;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class J3DTest2 {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new J3DTest2();
    }
    SimpleUniverse universe;
    public J3DTest2() {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setSize(400, 400);

        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(createGraph());

        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 400, 400);
        frame.add(canvas, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public BranchGroup createGraph() {
        Appearance ap = new Appearance();
        ap.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.5f,
                TransparencyAttributes.BLEND_SRC_ALPHA, TransparencyAttributes.BLEND_ONE));
        Color3f ambient = new Color3f(Color.black);// 环境光
        Color3f emissive = new Color3f(Color.black);// 自发光
        Color3f diffuse = new Color3f(Color.green);// 扩散的
        Color3f specular = new Color3f(Color.black);// 反射的

        ap.setMaterial(new Material(ambient, emissive, diffuse, specular, 0.8f));
        int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        Sphere sphere = new Sphere(0.5f, primflags, 24, ap);

        // BoundingPolytope bp = new BoundingPolytope();

        // ColorCube cube = new ColorCube(0.5f);
        // cube.setAppearance(ap);
        // System.out.println(cube.getCollisionBounds());

        // PickBounds d = new PickBounds(new BoundingBox);

        DirectionalLight light = new DirectionalLight(new Color3f(Color.white), new Vector3f(4.0f, -7f, -12f));
        AmbientLight aLight = new AmbientLight(new Color3f(Color.green));
        aLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));

        Transform3D trans3D = new Transform3D();
        trans3D.setTranslation(new Vector3f());

        TransformGroup transGroup = new TransformGroup();
        transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transGroup.setTransform(trans3D);
        transGroup.addChild(sphere);

        float threshold = 0.1f;
        // LinkedList<PickBounds> pbs = new LinkedList<PickBounds>();
        Point3d lp = new Point3d();
        Point3d up = new Point3d();
        float r = threshold;
        QuadArray qs = new QuadArray(24, QuadArray.COORDINATES);
        Shape3D s = new Shape3D(qs, ap);
        
        transGroup.addChild(s);
        Point3f[] indexedShapeCoordinates = { //
        new Point3f(-r, r, r), //
                new Point3f(r, r, r), //
                new Point3f(r, r, -r), //
                new Point3f(-r, r, -r), //
                new Point3f(-r, -r, r), //
                new Point3f(r, -r, r), //
                new Point3f(r, -r, -r), //
                new Point3f(-r, -r, -r) //
        };
        int coordinateIndices[] = {//
        0, 1, 2, 3,//
                4, 5, 6, 7,//
                0, 4, 5, 1,//
                2, 3, 7, 6,//
                0, 3, 7, 4,//
                1, 2, 6, 5 //
        };
        IndexedQuadArray indexedShape = new IndexedQuadArray(8,
                IndexedQuadArray.COORDINATES | IndexedQuadArray.NORMALS, 24);
        indexedShape.setCoordinates(0, indexedShapeCoordinates);
        indexedShape.setCoordinateIndices(0, coordinateIndices);

        // transGroup.addChild(new Shape3D(indexedShape, ap));

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup(transGroup);
        transGroup.addChild(behavior);
        behavior.setSchedulingBounds(bounds);

        MouseTranslate behavior2 = new MouseTranslate();
        behavior2.setTransformGroup(transGroup);
        transGroup.addChild(behavior2);
        behavior2.setSchedulingBounds(bounds);

        BranchGroup groupRoot = new BranchGroup();
        groupRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        groupRoot.addChild(transGroup);

        Color3f light1Color = new Color3f(Color.white);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);

        Color3f ambientColor = new Color3f(Color.white);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
//        groupRoot.addChild(ambientLightNode);

        groupRoot.addChild(light1);
//        groupRoot.addChild(light);
//        groupRoot.addChild(aLight);
        return groupRoot;
    }
}
