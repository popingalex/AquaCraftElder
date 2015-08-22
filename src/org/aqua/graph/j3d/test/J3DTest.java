package org.aqua.graph.j3d.test;

import java.applet.Applet;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.*;

import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.Scene;

import java.net.URL;
import java.util.Hashtable;
import java.util.Enumeration;

public class J3DTest extends Applet {
    static {
        // System.loadLibrary("j3dcore-ogl");
    }

    protected Canvas3D       c1        = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    // private Canvas3D c2 = new
    // Canvas3D(SimpleUniverse.getPreferredConfiguration());
    private static MainFrame mf;
    protected SimpleUniverse u         = null;
    protected BranchGroup    scene     = null;
    protected String         URLString = "C:/Users/Administrator/Desktop/src/dragon_obj_1.obj";
    protected float          eyeOffset = 0.03F;
    protected static int     size      = 600;
    public void init() {

        setLayout(new FlowLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        String v = getParameter("url");
        if (v != null) {
            URLString = v;
        }
        c1.setSize(size, size);
        add(c1);
        scene = createSceneGraph(0);
        u = new SimpleUniverse(c1);
        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        // u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);

    }
    public BranchGroup createSceneGraph(int i) {
        System.out.println("Creating scene for: " + URLString);
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();
        try {
            Transform3D myTransform3D = new Transform3D();
            myTransform3D.setTranslation(new Vector3f(+0.0f, -0.15f, -3.6f));
            TransformGroup objTrans = new TransformGroup(myTransform3D);
            objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            Transform3D t = new Transform3D();
            TransformGroup tg = new TransformGroup(t);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTrans.addChild(tg);
            ObjectFile f = new ObjectFile();
            f.setFlags(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
            System.out.println("About to load");

            Scene s = f.load(URLString);
            Transform3D myTrans = new Transform3D();
            TransformGroup mytg = new TransformGroup(myTrans);
            // mytg.addChild(s.getSceneGroup());
            tg.addChild(mytg);
            Transform3D myTrans2 = new Transform3D();
            TransformGroup mytg2 = new TransformGroup(myTrans2);
            // mytg2.addChild(s.getSceneGroup());
            Hashtable table = s.getNamedObjects();
            for (Enumeration e = table.keys(); e.hasMoreElements();) {
                Object key = e.nextElement();
//                System.out.println(key);
                Object obj = table.get(key);
//                System.out.println(obj.getClass().getName());
                Shape3D shape = (Shape3D) obj;
                
                System.out.println(shape.getClass());
                for (int j = 0, sum = shape.numGeometries(); j < sum; j++) {
                    Geometry g = shape.getGeometry(j);
                    
//                    System.out.println(g.getClass());
                    TriangleStripArray tsa = (TriangleStripArray) g;
                    
                }

                // shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                Appearance ap = new Appearance();
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                Color3f red = new Color3f(0.7f, .0f, .15f);
                Color3f green = new Color3f(0f, .7f, .15f);
                // ambient 环境的
                // emissive 自发的
                // diffuse 扩散的
                // specular 反射的
                // shininess 明度
                ap.setMaterial(new Material(new Color3f(Color.black), black, new Color3f(Color.white), black, 1.0f));
                Appearance ap2 = new Appearance();
                // ap2.setMaterial(new Material(red, black, red, black, 1.0f));
                float transparencyValue = 0.5f;
                TransparencyAttributes t_attr = new TransparencyAttributes(TransparencyAttributes.BLENDED,
                        transparencyValue, TransparencyAttributes.BLEND_SRC_ALPHA, TransparencyAttributes.BLEND_ONE);
                ap2.setTransparencyAttributes(t_attr);
                ap2.setRenderingAttributes(new RenderingAttributes());
                ap.setTransparencyAttributes(t_attr);
                ap.setRenderingAttributes(new RenderingAttributes());
                // bg.addChild(ap);
                shape.setAppearance(ap);
                // mytg2.addChild(new Shape3D(shape.getGeometry(),ap2));
                mytg.addChild(new Shape3D(shape.getGeometry(), ap));
            }
            tg.addChild(mytg2);
            System.out.println("Finished Loading");
            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
            Color3f light1Color = new Color3f(.9f, 0.9f, 0.9f);
            Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
            DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
            light1.setInfluencingBounds(bounds);
            objTrans.addChild(light1);
            // Set up the ambient light
            Color3f ambientColor = new Color3f(1.0f, .4f, 0.3f);
            AmbientLight ambientLightNode = new AmbientLight(ambientColor);
            ambientLightNode.setInfluencingBounds(bounds);
            objTrans.addChild(ambientLightNode);

            MouseRotate behavior = new MouseRotate();
            behavior.setTransformGroup(tg);
            objTrans.addChild(behavior);
            // Create the translate behavior node
            MouseTranslate behavior3 = new MouseTranslate();
            behavior3.setTransformGroup(tg);
            objTrans.addChild(behavior3);
            behavior3.setSchedulingBounds(bounds);

            KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(tg);
            keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
            objTrans.addChild(keyNavBeh);

            behavior.setSchedulingBounds(bounds);
            objRoot.addChild(objTrans);
        } catch (Throwable t) {
            System.out.println("Error: " + t);
        }
        return objRoot;
    }
    public J3DTest() {
    }
    public void destroy() {
        u.removeAllLocales();
    }

    public static void main(String[] args) {

        // J3DTest s = new J3DTest();
        if (args.length > 0) {
            // s.URLString = args[0];
        }
        // mf = new MainFrame(s, size, size);
    }
}
