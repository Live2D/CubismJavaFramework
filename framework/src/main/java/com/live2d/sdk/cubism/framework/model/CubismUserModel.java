/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.model;

import com.live2d.sdk.cubism.framework.effect.CubismBreath;
import com.live2d.sdk.cubism.framework.effect.CubismEyeBlink;
import com.live2d.sdk.cubism.framework.effect.CubismPose;
import com.live2d.sdk.cubism.framework.id.CubismId;
import com.live2d.sdk.cubism.framework.math.CubismModelMatrix;
import com.live2d.sdk.cubism.framework.math.CubismTargetPoint;
import com.live2d.sdk.cubism.framework.motion.*;
import com.live2d.sdk.cubism.framework.physics.CubismPhysics;
import com.live2d.sdk.cubism.framework.rendering.CubismRenderer;
import com.live2d.sdk.cubism.framework.rendering.android.CubismRendererAndroid;

import static com.live2d.sdk.cubism.framework.CubismFramework.VERTEX_OFFSET;
import static com.live2d.sdk.cubism.framework.CubismFramework.VERTEX_STEP;
import static com.live2d.sdk.cubism.framework.utils.CubismDebug.cubismLogError;
import static com.live2d.sdk.cubism.framework.utils.CubismDebug.cubismLogInfo;

/**
 * This is the base class of the model that the user actually utilizes. The user defined model class inherits this class.
 */
public abstract class CubismUserModel {
    /**
     * A callback for registering with CubismMotionQueueManager for an event.
     * Call the EventFired which is inherited from CubismUserModel.
     *
     * @param eventValue the string data of the fired event
     * @param model an instance inherited with CubismUserModel
     */
    public static void cubismDefaultMotionEventCallback(String eventValue, CubismUserModel model) {
        if (model != null) {
            model.motionEventFired(eventValue);
        }
    }

    /**
     * Get the collision detection.
     * <p>
     * Get whether the Drawable has been hit at the specified position.
     *
     * @param drawableId Drawable ID which will be verified.
     * @param pointX X-position
     * @param pointY Y-position
     * @return true      If it is hit, return true.
     */
    public boolean isHit(CubismId drawableId, float pointX, float pointY) {
        final int drawIndex = model.getDrawableIndex(drawableId);

        // If there are no hit Drawable, return false
        if (drawIndex < 0) {
            return false;
        }

        final int count = model.getDrawableVertexCount(drawIndex);
        final float[] vertices = model.getDrawableVertices(drawIndex);

        float left = vertices[0];
        float right = vertices[0];
        float top = vertices[1];
        float bottom = vertices[1];


        for (int i = 1; i < count; ++i) {
            float x = vertices[VERTEX_OFFSET + i * VERTEX_STEP];
            float y = vertices[VERTEX_OFFSET + i * VERTEX_STEP + 1];

            if (x < left) {
                // Min x
                left = x;
            }

            if (x > right) {
                // Max x
                right = x;
            }

            if (y < top) {
                // Min y
                top = y;
            }

            if (y > bottom) {
                // Max y
                bottom = y;
            }
        }

        final float tx = modelMatrix.invertTransformX(pointX);
        final float ty = modelMatrix.invertTransformY(pointY);

        return (left <= tx) && (tx <= right) && (top <= ty) && (ty <= bottom);
    }

    /**
     * Creates and initializes a renderer. The default number of mask buffers is 1.
     *
     * @param type renderer type
     * @throws IllegalArgumentException Thrown when an undefined renderer type is given.
     */
    public void createRenderer(final RendererType type) {
        createRenderer(type, 1);
    }

    /**
     * Creates and initializes a renderer.<br>
     * Please use this method if you would like to set the number of mask buffers other than default of 1.
     *
     * @param type renderer type
     * @param maskBufferCount number of mask buffers to be generated
     * @throws IllegalArgumentException thrown when an undefined renderer type is given.
     */
    public void createRenderer(RendererType type, int maskBufferCount) {
        switch (type) {
            case ANDROID:
                renderer = CubismRendererAndroid.create();
                break;
            default:
                throw new IllegalArgumentException("Given renderer type does not exist.");
        }

        // Bind a renderer with a model instance
        renderer.initialize(model, maskBufferCount);
    }

    /**
     * Do a standard process at firing the event.
     * <p>
     * This method deals with the case where an Event occurs during the playback process.
     * It is basically overrided by inherited class.
     * If it is not overrided, output log.
     *
     * @param eventValue the string data of the fired event
     */
    public void motionEventFired(String eventValue) {
        cubismLogInfo(eventValue);
    }

    /**
     * Get initializing status.
     *
     * @return If this class is initialized, return true.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Set an initializing setting.
     *
     * @param isInitialized initializing status
     */
    public void isInitialized(boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * Get the updating status.
     *
     * @return If this class is updated, return true.
     */
    public boolean isUpdated() {
        return isUpdated;
    }

    /**
     * Set an updating status.
     *
     * @param isUpdated updating status
     */
    public void isUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    /**
     * Set an information of mouse dragging.
     *
     * @param x X-position of the cursor being dragging
     * @param y Y-position of the cursor being dragging
     */
    public void setDragging(float x, float y) {
        dragManager.set(x, y);
    }

    /**
     * Set an acceleration information.
     *
     * @param x Acceleration in X-axis direction
     * @param y Acceleration in Y-axis direction
     * @param z Acceleration in Z-axis direction
     */
    public void setAcceleration(float x, float y, float z) {
        accelerationX = x;
        accelerationY = y;
        accelerationZ = z;
    }

    /**
     * Get the model matrix.
     * This method returns a copy of this _modelMatrix.
     *
     * @return the model matrix
     */
    public CubismModelMatrix getModelMatrix() {
        return modelMatrix;
    }

    /**
     * Get the opacity.
     *
     * @return the opacity
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Set an opacity.
     *
     * @param opacity an opacity
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /*
     * Get the model.
     *
     * @return the model
     */
    public CubismModel getModel() {
        return model;
    }

    /**
     * Get the renderer. This method is the generics. The user have to give the renderer type that they would like to use.
     * <p>
     * (example of use) this{@literal .<CubismRendererAndroid>getRenderer();}
     * </p>
     *
     * @param <T> renderer type to use
     * @return renderer instance
     */
    public <T> T getRenderer() {
        return (T) renderer;
    }

    protected enum RendererType {
        ANDROID,
    }

    /**
     * Constructor
     */
    protected CubismUserModel() {
        // Because this class inherits MotionQueueManager, the usage is the same.
        motionManager.setEventCallback(cubismDefaultMotionEventCallback, this);
    }

    /**
     * Read a model data
     *
     * @param buffer a buffer where the moc3 file is loaded
     */
    protected void loadModel(final byte[] buffer) {
        final CubismMoc moc = CubismMoc.create(buffer);

        if (moc == null) {
            cubismLogError("Failed to create CubismMoc instance.");
            return;
        }

        this.moc = moc;
        final CubismModel model = this.moc.createModel();

        if (model == null) {
            cubismLogError("Failed to create the model.");
            return;
        }

        this.model = model;

        this.model.saveParameters();
        modelMatrix = CubismModelMatrix.create(this.model.getCanvasWidth(), this.model.getCanvasHeight());
    }

    /**
     * Delete Moc and Model instances.
     */
    protected void delete() {
        if (moc == null || model == null) {
            return;
        }
        moc.deleteModel(model);

        moc.delete();
        model.close();
        renderer.close();

        moc = null;
        model = null;
        renderer = null;
    }

    /**
     * Load a motion data.
     *
     * @param buffer a buffer where motion3.json file is loaded.
     * @param onFinishedMotionHandler the callback method called at finishing motion play. If it is null, callbacking methods is not conducting.
     * @return motion class
     */
    protected CubismMotion loadMotion(
        byte[] buffer,
        IFinishedMotionCallback onFinishedMotionHandler
    ) {
        return CubismMotion.create(buffer, onFinishedMotionHandler);
    }

    /**
     * Load a motion data.
     *
     * @param buffer a buffer where motion3.json file is loaded.
     * @return motion class
     */
    protected CubismMotion loadMotion(byte[] buffer) {
        return CubismMotion.create(buffer, null);
    }

    /**
     * Load a expression data.
     *
     * @param buffer a buffer where exp3.json is loaded
     * @return motion class
     */
    protected CubismExpressionMotion loadExpression(final byte[] buffer) {
        return CubismExpressionMotion.create(buffer);
    }

    /**
     * Load pose data.
     *
     * @param buffer a buffer where pose3.json is loaded.
     */
    protected void loadPose(final byte[] buffer) {
        pose = CubismPose.create(buffer);
    }

    /**
     * Load physics data.
     *
     * @param buffer a buffer where physics3.json is loaded.
     */
    protected void loadPhysics(final byte[] buffer) {
        physics = CubismPhysics.create(buffer);
    }

    /**
     * Load a user data attached the model.
     *
     * @param buffer a buffer where userdata3.json is loaded.
     */
    protected void loadUserData(final byte[] buffer) {
        modelUserData = CubismModelUserData.create(buffer);
    }

    /**
     * A Moc data,
     */
    protected CubismMoc moc;
    /**
     * A model instance
     */
    protected CubismModel model;

    /**
     * A motion manager
     */
    protected CubismMotionManager motionManager = new CubismMotionManager();
    /**
     * A expression manager
     */
    protected CubismMotionManager expressionManager = new CubismMotionManager();
    /**
     * Auto eye-blink
     */
    protected CubismEyeBlink eyeBlink;
    /**
     * Breathing
     */
    protected CubismBreath breath;
    /**
     * A model matrix
     */
    protected CubismModelMatrix modelMatrix;
    /**
     * m
     * Pose manager
     */
    protected CubismPose pose;
    /**
     * A mouse dragging manager
     */
    protected CubismTargetPoint dragManager = new CubismTargetPoint();
    /**
     * physics
     */
    protected CubismPhysics physics;
    /**
     * A user data
     */
    protected CubismModelUserData modelUserData;

    /**
     * An initializing status
     */
    protected boolean isInitialized;
    /**
     * An updating status
     */
    protected boolean isUpdated;
    /**
     * Opacity
     */
    protected float opacity = 1.0f;
    /**
     * A lip-sync status
     */
    protected boolean lipSync = true;
    /**
     * A control value of the last lip-sync
     */
    protected float lastLipSyncValue;
    /**
     * An X-position of mouse dragging
     */
    protected float dragX;
    /**
     * An Y-position of mouse dragging
     */
    protected float dragY;
    /**
     * An acceleration in X-axis direction
     */
    protected float accelerationX;
    /**
     * An acceleration in Y-axis direction
     */
    protected float accelerationY;
    /**
     * An acceleration in Z-axis direction
     */
    protected float accelerationZ;
    /**
     * Whether it is debug mode
     */
    protected boolean debugMode;

    /**
     * An entity of CubismMotionEventFunction.
     */
    private static final ICubismMotionEventFunction cubismDefaultMotionEventCallback = new ICubismMotionEventFunction() {
        @Override
        public void apply(
            CubismMotionQueueManager caller,
            String eventValue,
            Object customData
        ) {
            if (customData != null) {
                ((CubismUserModel) customData).motionEventFired(eventValue);
            }
        }
    };

    /**
     * A renderer
     */
    private CubismRenderer renderer;
}
