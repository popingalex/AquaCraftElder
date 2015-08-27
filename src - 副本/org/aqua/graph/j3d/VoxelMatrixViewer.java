package org.aqua.graph.j3d;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.Enumeration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JFrame;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.aqua.graph.voxel.VoxelMatrixNode;
import org.aqua.graph.voxel.VoxelMatrixRoot;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class VoxelMatrixViewer {

    private VoxelSandbox sandbox;

    public static void main(String[] args) {
        new VoxelMatrixViewer();
    }

    public VoxelMatrixViewer() {
        sandbox = new VoxelSandbox();

        JFrame frame = new JFrame();
        frame.add(buildCanvas());
        frame.setBounds(20, 20, 480, 360);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Canvas3D buildCanvas() {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                sandbox.scale(-e.getWheelRotation());
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
        MouseRotate rotateBehavior = new MouseRotate(deflectionGroup) {

            @Override
            public void processMouseEvent(MouseEvent evt) {
                y_last = evt.getY();
                super.processMouseEvent(evt);
            }

        };

        AmbientLight ambientLight = new AmbientLight(new Color3f(Color.white));
        ambientLight.setInfluencingBounds(behaviorBounding);

        pickBehavior.setSchedulingBounds(behaviorBounding);
        rotateBehavior.setSchedulingBounds(behaviorBounding);

        elevationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        elevationTrans.setRotation(new AxisAngle4d(1, 0, 0, Math.PI / 4));
        elevationGroup.setTransform(elevationTrans);
        elevationGroup.addChild(deflectionGroup);

        deflectionGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        deflectionTrans.setRotation(new AxisAngle4d(0, 1, 0, Math.PI / 4));
        deflectionGroup.setTransform(deflectionTrans);
        deflectionGroup.addChild(sandbox);

        sandbox.addChild(rotateBehavior);
        sandbox.addChild(pickBehavior);

        rootGroup.addChild(elevationGroup);
        rootGroup.addChild(ambientLight);

        Transform3D viewTrans = new Transform3D();
        viewTrans.setTranslation(new Vector3d(0f, 0f, 20f));

        universe.getViewingPlatform().getViewPlatformTransform().setTransform(viewTrans);
        universe.addBranchGraph(rootGroup);

        return canvas;
    }

    private static class PickBehavior extends PickMouseBehavior {

        public PickBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds) {
            super(canvas, root, bounds);
            setMode(PickTool.GEOMETRY);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void processStimulus(Enumeration criteria) {
            super.processStimulus(criteria);
            if (mevent.getID() == MouseEvent.MOUSE_MOVED) {
                updateScene(mevent.getX(), mevent.getY());
            }
        }

        @Override
        public void updateScene(int xpos, int ypos) {
            pickCanvas.setShapeLocation(xpos, ypos);
            PickResult result = pickCanvas.pickClosest();
            if (result != null) {
                Node node = result.getNode(PickResult.PRIMITIVE);
                System.out.println(node);

                switch (mevent.getButton()) {
                case MouseEvent.BUTTON1:
                    break;
                case MouseEvent.BUTTON2:
                    break;
                case MouseEvent.BUTTON3:
                    break;
                default:
                    break;
                }
            }
        }

    }

    public static class VoxelBox extends Box {
        public VoxelBox(float width, float length, float height) {
            super(width, length, height, new Appearance());
        }

    }

    private static class VoxelSandbox extends BranchGroup {
        VoxelMatrixRoot workbenchRoot;
        VoxelMatrixRoot modelRoot;
        VoxelBox        cursor;

        Float           scale;

        TransformGroup  scaleGroup;

        public VoxelSandbox() {
            scale = 0.1f;
            modelRoot = new VoxelMatrixRoot() {

                @Override
                public Object attachNode(int[] coord3) {
                    VoxelBox box = new VoxelBox(scale * 10, scale * 10, scale * 10);
                    Transform3D trans = new Transform3D();
                    trans.setTranslation(new Vector3d(coord3[IndexX], coord3[IndexY], coord3[IndexZ]));
                    TransformGroup tg = new TransformGroup();
                    tg.setTransform(trans);
                    tg.addChild(box);
                    addChild(tg);
                    return box;
                }

            };
            workbenchRoot = new VoxelMatrixRoot();

            buildWorkbench(2);
        }

        private void buildWorkbench(int radius) {

//            float size = 0.4f;
//            workbenchRoot.centerNode.content = new VoxelBox(size, size, size);
            workbenchRoot.realloc(new int[] { -radius, -2, -radius }, new int[] { radius, 2, radius });
        }

        private void buildModel() {

        }

        public void buildCursor(int x, int y, int z, int direction) {

        }

        public void scale(int scale) {
            Transform3D scaleTransform = new Transform3D();
            scaleGroup.getTransform(scaleTransform);
            scaleTransform.setScale(Math.max(scaleTransform.getScale() + scale, 1));
            scaleGroup.setTransform(scaleTransform);
        }
    }
}
