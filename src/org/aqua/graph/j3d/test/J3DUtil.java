package org.aqua.graph.j3d.test;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.Group;
import javax.media.j3d.J3DBuffer;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PickBounds;
import javax.media.j3d.PickSegment;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.PickInfo.IntersectionInfo;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.picking.Intersect;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.pickfast.PickIntersection;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class J3DUtil {

    private LinkedList<MarkBox> markerList = new LinkedList<J3DUtil.MarkBox>();

    public J3DUtil() {
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(createCanvas());
        frame.setVisible(true);
    }

    public Canvas3D createCanvas() {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        BranchGroup rootGroup = new BranchGroup();
        BoundingSphere viewBound = new BoundingSphere(new Point3d(), 100);

        TransformGroup envGroup = new TransformGroup();
        envGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        envGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Node markGroup = getMarkGroup(null);
        Node modelGroup = getModelGroup("source/dragon_obj_2.obj");
        modelGroup = getModelGroup("source/test_obj_2.obj");

        envGroup.addChild(markGroup);
        envGroup.addChild(modelGroup);

        // new TriangleStripArray(2, TriangleStripArray.NORMALS,
        // stripVertexCounts)

        MouseRotate mouseRotateBehavior = new MouseRotate(envGroup);
        mouseRotateBehavior.setSchedulingBounds(viewBound);

        envGroup.addChild(mouseRotateBehavior);

        AmbientLight ambientLightNode = new AmbientLight(new Color3f(Color.white));
        ambientLightNode.setInfluencingBounds(viewBound);

        rootGroup.addChild(ambientLightNode);

        rootGroup.addChild(envGroup);
        rootGroup.compile();
        SimpleUniverse universe = new SimpleUniverse(canvas);
        universe.addBranchGraph(rootGroup);
        universe.getViewingPlatform().setNominalViewingTransform();
        return canvas;
    }

    private Node getMarkGroup(Bounds object) {
        TransparencyAttributes transAttr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.8f);
        Appearance transparence = new Appearance();
        transparence.setTransparencyAttributes(transAttr);
        // BoundingSphere viewBound = new BoundingSphere(new Point3d(), 100);

        float threshold;
        int height = 9;
        int length = 9;
        int width = 9;

        threshold = 0.2f;

        TransformGroup markGroup = new TransformGroup();
        for (int z = -height / 2, maxz = height / 2 + 1; z < maxz; z++) {
            for (int y = -length / 2, maxy = length / 2 + 1; y < maxy; y++) {
                for (int x = -width / 2, maxx = length / 2 + 1; x < maxx; x++) {
                    Transform3D offset = new Transform3D();
                    offset.setTranslation(new Vector3d(x * threshold, y * threshold, z * threshold));

                    MarkBox markBox = new MarkBox(threshold / 2, x, y, z);
                    // MarkBehavior markBeh = new MarkBehavior(markBox);

                    markerList.add(markBox);
                    TransformGroup offsetGroup = new TransformGroup(offset);
                    offsetGroup.addChild(markBox);
                    // offsetGroup.addChild(markBeh);

                    markGroup.addChild(offsetGroup);
                }
            }
        }
        return markGroup;
    }

    private Node getModelGroup(String path) {
        TransparencyAttributes transAttr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.8f);
        Appearance transparence = new Appearance();
        transparence.setTransparencyAttributes(transAttr);

        TransformGroup modelGroup = new TransformGroup();
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
        Scene modelScene = null;
        try {
            modelScene = file.load(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Hashtable<?, ?> modelTable = modelScene.getNamedObjects();
        for (Object key : modelTable.keySet()) {
            Shape3D srcModelPart = ((Shape3D) modelTable.get(key));
            Shape3D dstModelPart = new Shape3D();
            
            TriangleStripArray a = (TriangleStripArray) srcModelPart.getGeometry();
            float[] vertices = a.getInterleavedVertices();
            int numStrips = a.getNumStrips();
            int[] stripCounts = new int[numStrips];
            a.getStripVertexCounts(stripCounts);
            for (MarkBox mb : markerList) {
                for (int i = 0, sum = vertices.length / 9; i < sum; i++) {
                    Point3d loc = new Point3d(mb.x * mb.size, mb.y * mb.size, mb.z * mb.size);
                    
                    boolean result = Util.bat(loc, mb.size, mb.size, mb.size, vertices, stripCounts, numStrips);
                    if (result) {
                        mb.remark();
                    }
                }
            }
            
            
            
            // PickIntersection pi = new PickIntersection(localToVWorld, new
            // IntersectionInfo())

            // System.out.println(a.);
            dstModelPart.addGeometry(a);

            dstModelPart.setAppearance(transparence);
            modelGroup.addChild(dstModelPart);
        }

        return modelGroup;
    }

    static class MarkBehavior extends Behavior {
        private MarkBox               marker;
        private static BoundingSphere bound = new BoundingSphere(new Point3d(), 100);
        public MarkBehavior(MarkBox marker) {
            this.marker = marker;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void processStimulus(Enumeration criteria) {
            marker.remark();
            // for(;criteria.hasMoreElements();) {
            // WakeupOnCollisionEntry entry = (WakeupOnCollisionEntry)
            // criteria.nextElement();
            // Enumeration e = entry.allElements();
            //
            // for(;e.hasMoreElements();) {
            // System.out.println(e.nextElement());
            // }
            // }
        }
        @Override
        public void initialize() {
            wakeupOn(new WakeupOnCollisionEntry(marker));
            setSchedulingBounds(bound);
        }
    };

    static class MarkBox extends Box {
        {
            // TransparencyAttributes transAttr = ;
            markedMaterial = new Material();
            markedMaterial.setEmissiveColor(new Color3f(Color.green));

            unmarkedMaterial = new Material();
            unmarkedMaterial.setEmissiveColor(new Color3f(Color.red));

            markedAttributes = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1f);
            unmarkedAttributes = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.8f);
        }
        private Appearance                    apperance;
        private static Material               markedMaterial;
        private static Material               unmarkedMaterial;

        private static TransparencyAttributes markedAttributes;
        private static TransparencyAttributes unmarkedAttributes;

        private boolean                       marked = true;
        public float                          size;
        public int                            x;
        public int                            y;
        public int                            z;

        public MarkBox(float size, int x, int y, int z) {
            super(size, size, size, new Appearance());
            apperance = getAppearance();
            apperance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            apperance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
            remark();
            this.size = size;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void remark() {
            marked = !marked;
            if (marked) {
                apperance.setTransparencyAttributes(markedAttributes);
                apperance.setMaterial(markedMaterial);
            } else {
                apperance.setTransparencyAttributes(unmarkedAttributes);
                apperance.setMaterial(unmarkedMaterial);
            }
        }
    }

    public static void main(String[] args) {
        new J3DUtil();

    }
}
