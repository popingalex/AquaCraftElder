package org.aqua.graph.j3d;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Enumeration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Geometry;
import javax.media.j3d.Node;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.aqua.graph.voxel.VoxelModel;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Voxel3DViewBack {
    private Sandbox sandboxGroup = new Sandbox();

    public Voxel3DViewBack() {
        JFrame frame = new JFrame();
        frame.add(createCanvas());
        frame.setBounds(10, 10, 400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Canvas3D createCanvas() {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                sandboxGroup.mouseWheelScroll(-e.getWheelRotation());
            }
        });
        BoundingSphere behaviorBounding = new BoundingSphere(new Point3d(), 100);
        SimpleUniverse universe = new SimpleUniverse(canvas);

        BranchGroup rootGroup = new BranchGroup();
        TransformGroup elevationGroup = new TransformGroup();
        TransformGroup deflectionGroup = new TransformGroup();

        Transform3D elevationTrans = new Transform3D();
        Transform3D deflectionTrans = new Transform3D();

        PickMouseBehavior pickBehavior = new PickBehavior(canvas, rootGroup, behaviorBounding);
        MouseRotate rotateBehavior = new RotateBehavior(deflectionGroup);

        AmbientLight ambientLight = new AmbientLight(new Color3f(Color.white));
        ambientLight.setInfluencingBounds(behaviorBounding);

        pickBehavior.setSchedulingBounds(behaviorBounding);
        rotateBehavior.setSchedulingBounds(behaviorBounding);

        elevationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        elevationTrans.setRotation(new AxisAngle4d(1, 0, 0, Math.PI / 8));
        elevationGroup.setTransform(elevationTrans);
        elevationGroup.addChild(deflectionGroup);

        deflectionGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        deflectionTrans.setRotation(new AxisAngle4d(0, 1, 0, Math.PI / 4));
        deflectionGroup.setTransform(deflectionTrans);
        deflectionGroup.addChild(sandboxGroup);

        sandboxGroup.addChild(rotateBehavior);
        sandboxGroup.addChild(pickBehavior);

        rootGroup.addChild(elevationGroup);
        rootGroup.addChild(ambientLight);

        Transform3D viewTrans = new Transform3D();
        viewTrans.setTranslation(new Vector3d(0f, 0f, 20f));
        universe.getViewingPlatform().getViewPlatformTransform().setTransform(viewTrans);
        universe.addBranchGraph(rootGroup);

        return canvas;
    }

    public static void main(String[] args) {
        new Voxel3DViewBack();
    }

    static class RotateBehavior extends MouseRotate {
        public RotateBehavior(TransformGroup transformGroup) {
            super(transformGroup);
        }

        @Override
        public void processMouseEvent(MouseEvent evt) {
            y_last = evt.getY();
            super.processMouseEvent(evt);
        }
    }

    private static class PickBehavior extends PickMouseBehavior {
        public PickBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds) {
            super(canvas, root, bounds);
            this.setMode(PickTool.GEOMETRY);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void processStimulus(Enumeration criteria) {
            super.processStimulus(criteria);
            switch (mevent.getID()) {
            case MouseEvent.MOUSE_MOVED:
                // updateScene(mevent.getPoint().x, mevent.getPoint().y);
                break;
            default:
            }
        }

        @Override
        public void updateScene(int xpos, int ypos) {
            pickCanvas.setShapeLocation(xpos, ypos);
            {
                
            }
            PickResult result = pickCanvas.pickClosest();
            if (result != null) {
                // SceneGraphPath path = result.getSceneGraphPath();

                Node node = result.getNode(PickResult.PRIMITIVE);
                System.out.println("pp :" + node);

                Node picker = result.getNode(PickResult.SHAPE3D);
                System.out.println("pc :" + picker);

                Point3d p3d = new Point3d();
                switch (mevent.getButton()) {
                case MouseEvent.BUTTON1:
                    break;
                case MouseEvent.BUTTON2:
                    break;
                case MouseEvent.BUTTON3:
                    break;
                default:// just motion
                    break;
                }
            } else {
                System.out.println("noth");
            }
        }
    };

    private static class Sandbox extends BranchGroup {
        VoxelMatrixBox workbenchRoot;
        VoxelMatrixBox modelRoot;
        VoxelMatrixBox cursor;
        // VoxelModel voxel;
        static Float   scale;

        public Sandbox() {
            super();
            // setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            // setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            scale = 0.01f;

            // TransparencyAttributes transAttr = new
            // TransparencyAttributes(TransparencyAttributes.BLENDED, 0.8f);
            // Appearance transparence = new Appearance();
            // transparence.setTransparencyAttributes(transAttr);

            workbenchRoot = new VoxelMatrixBox(100, 0, 100);

            new TransformGroup().addChild(workbenchRoot);
            cursor = new VoxelMatrixBox(10, 10, 10);

            addChild(workbenchRoot.getParent());
//            addChild(workbenchRoot);
//            addChild(new Box(10f, 1f, 10f, new Appearance()));

            rebuild();
        }

        public void mouseWheelScroll(int amount) {
            Transform3D t3d = new Transform3D();
            TransformGroup tg = (TransformGroup) workbenchRoot.getParent();
            tg.getTransform(t3d);
            t3d.setScale(Math.max(1, t3d.getScale() + amount));
            tg.setTransform(t3d);
        }

        private void buildWorkbench(int height) {
            Transform3D transform = new Transform3D();

            transform.transform(new Vector3d(0f, -0.01f, 0f));
            ((TransformGroup) workbenchRoot.getParent()).setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            ((TransformGroup) workbenchRoot.getParent()).setTransform(transform);
        }

        public void buildModel() {

        }

        public void rebuild() {
            buildWorkbench(0);
            // addChild(cursor);

            Transform3D t1 = new Transform3D();
            Transform3D t2 = new Transform3D();
            Transform3D t3 = new Transform3D();
            Transform3D t4 = new Transform3D();

            t1.setTranslation(new Vector3d(2.2, 2.2, 2.2));
            t2.setTranslation(new Vector3d(2.2, 2.2, -2.2));
            t3.setTranslation(new Vector3d(-2.2, 2.2, 2.2));
            t4.setTranslation(new Vector3d(-2.2, 2.2, -2.2));

            TransformGroup tg1 = new TransformGroup(t1);
            TransformGroup tg2 = new TransformGroup(t2);
            TransformGroup tg3 = new TransformGroup(t3);
            TransformGroup tg4 = new TransformGroup(t4);

            ColorCube cc1 = new ColorCube(1);
            ColorCube cc2 = new ColorCube(1);
            ColorCube cc3 = new ColorCube(1);
            ColorCube cc4 = new ColorCube(1);

            cc1.setName("cc1");
            cc2.setName("cc2");
            cc3.setName("cc3");
            cc4.setName("cc4");

            tg1.addChild(cc1);
            tg2.addChild(cc2);
            tg3.addChild(cc3);
            tg4.addChild(cc4);

            addChild(tg1);
            System.out.println(tg1.getBounds());
            addChild(tg2);
            addChild(tg3);
            addChild(tg4);
        }

        private static class VoxelMatrixBox extends Box {
            public int            x;
            public int            y;
            public int            z;

            public int            posCountX;
            public int            negCountX;
            public int            posCountY;
            public int            negCountY;
            public int            posCountZ;
            public int            negCountZ;

            public VoxelMatrixBox preNodeX;
            public VoxelMatrixBox preNodeY;
            public VoxelMatrixBox preNodeZ;
            public VoxelMatrixBox sufNodeX;
            public VoxelMatrixBox sufNodeY;
            public VoxelMatrixBox sufNodeZ;

            public int            width;
            public int            length;
            public int            height;

            public VoxelMatrixBox(int width, int length, int height) {
                super(width * scale, length * scale, height * scale, new Appearance());
                this.width = width;
                this.length = length;
                this.height = height;
            }
        }
    }
}
