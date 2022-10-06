/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import com.live2d.sdk.cubism.framework.math.CubismVector2;
import com.live2d.sdk.cubism.framework.model.CubismModel;
import com.live2d.sdk.cubism.framework.rendering.CubismRenderer;
import com.live2d.sdk.cubism.framework.utils.CubismDebug;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.*;
import static com.live2d.sdk.cubism.framework.CubismFramework.CSM_DEBUG;

/**
 * The class that implements drawing instructions for Android.
 */
public class CubismRendererAndroid extends CubismRenderer {
    /**
     * Create the renderer instance for Android platform.
     *
     * @return renderer instance
     */
    public static CubismRenderer create() {
        return new CubismRendererAndroid();
    }

    /**
     * Release static resources that this renderer keeps.
     */
    public static void staticRelease() {
        CubismRendererAndroid.doStaticRelease();
    }

    /**
     * Tegra processor support.
     * Enable/Disable drawing by extension method.
     *
     * @param extMode Whether to draw using the extended method
     * @param extPAMode Whether to enable the PA setting for the extended method
     */
    public static void setExtShaderMode(boolean extMode, boolean extPAMode) {
        CubismShaderAndroid.setExtShaderMode(extMode, extPAMode);
        CubismShaderAndroid.deleteInstance();
    }

    /**
     * Android-Tegra support. Reload shader programs.
     */
    public static void reloadShader() {
        CubismShaderAndroid.deleteInstance();
    }


    @Override
    public void initialize(final CubismModel model) {
        if (model.isUsingMasking()) {
            // Initialize clipping mask and buffer preprocessing method
            clippingManager.initialize(
                model.getDrawableCount(),
                model.getDrawableMasks(),
                model.getDrawableMaskCounts()
            );

            offscreenFrameBuffer.createOffscreenFrame(clippingManager.getClippingMaskBufferSize(), null);
        }

        sortedDrawableIndexList = new int[model.getDrawableCount()];
        super.initialize(model);
    }

    @Override
    public void close() {
        super.close();

        if (clippingManager != null) {
            clippingManager.close();
        }

        vertexArrayFloatBufferMap = null;
        uvArrayFloatBufferMap = null;
        indexArrayBufferMap = null;
    }

    /**
     * Bind processing of OpenGL textures.
     *
     * @param modelTextureNo number of the model texture to set
     * @param glTextureNo number of the OpenGL texture to bind
     */
    public void bindTexture(int modelTextureNo, int glTextureNo) {
        textures.put(modelTextureNo, glTextureNo);
        areTexturesChanged = true;
    }

    /**
     * Get textures list bound to OpenGL.
     *
     * @return textures list
     */
    public Map<Integer, Integer> getBoundTextures() {
        if (areTexturesChanged) {
            cachedImmutableTextures = Collections.unmodifiableMap(textures);
            areTexturesChanged = false;
        }
        return cachedImmutableTextures;
    }

    /**
     * Get the size of clipping mask buffer.
     *
     * @return size of clipping mask buffer
     */
    private CubismVector2 getClippingMaskBufferSize() {
        return clippingManager.getClippingMaskBufferSize();
    }

    /**
     * Set the size of clipping mask buffer.
     * This method's processing cost is high because the FrameBuffer for the mask is destroyed and recreated.
     *
     * @param width width of MaskBufferSize
     * @param height height of MaskBufferSize
     */
    public void setClippingMaskBufferSize(final float width, final float height) {
        // Destroy and recreate instances to change the size of FrameBuffer
        clippingManager = new CubismClippingManagerAndroid();
        clippingManager.setClippingMaskBufferSize(width, height);

        CubismModel model = getModel();
        clippingManager.initialize(
            model.getDrawableCount(),
            model.getDrawableMasks(),
            model.getDrawableMaskCounts()
        );
    }


    /**
     * Draw the drawing objects (ArtMesh). <br>
     * Both polygon mesh and the texture number is given to this method.
     *
     * @param textureNo number of the drawed texture
     * @param indexCount index of the drawing object
     * @param vertexCount number of the polygon mesh vertices
     * @param indexArray index array of the polygon mesh
     * @param vertexArrayBuffer vertex array of the polygon mesh
     * @param uvArrayBuffer uv array
     * @param multiplyColor multiply color
     * @param screenColor screen color
     * @param opacity opacity
     * @param colorBlendMode color blending mode
     * @param invertedMask Inverted use of masks when masks are used
     */
    protected void drawMeshAndroid(
        int textureNo,
        int indexCount,
        int vertexCount,
        ShortBuffer indexArray,
        FloatBuffer vertexArrayBuffer,
        FloatBuffer uvArrayBuffer,
        CubismTextureColor multiplyColor,
        CubismTextureColor screenColor,
        float opacity,
        CubismBlendMode colorBlendMode,
        boolean invertedMask
    ) {
        if (!CSM_DEBUG) {
            // If the texture referenced by the model is not bound, skip drawing.
            if (textures.get(textureNo) == null) {
                return;
            }
        }

        // Enabling/disabling culling
        if (isCulling()) {
            glEnable(GL_CULL_FACE);
        } else {
            glDisable(GL_CULL_FACE);
        }

        // In Cubism3 OpenGL, CCW becomes surface for both masks and art meshes.
        glFrontFace(GL_CCW);

//        CubismTextureColor modelColorRGBA = getModelColor();
        CubismTextureColor tmp = getModelColor();
        modelColorRGBA.r = tmp.r;
        modelColorRGBA.g = tmp.g;
        modelColorRGBA.b = tmp.b;
        modelColorRGBA.a = tmp.a;


        // Except at generating mask
        if (getClippingContextBufferForMask() == null) {
            modelColorRGBA.a *= opacity;
            if (isPremultipliedAlpha()) {
                modelColorRGBA.r *= modelColorRGBA.a;
                modelColorRGBA.g *= modelColorRGBA.a;
                modelColorRGBA.b *= modelColorRGBA.a;
            }
        }

        // Texture ID given to the shader
        int drawTextureId;

        // Get the bound texture ID from the texture map.
        // If the ID has not bound, set the dummy texture ID.
        if (textures.get(textureNo) != null) {
            drawTextureId = textures.get(textureNo);
        } else {
            drawTextureId = -1;
        }


        CubismShaderAndroid.getInstance().setupShaderProgram(
            this,
            drawTextureId,
            vertexCount,
//            vertexArray,
//            uvArray,
            vertexArrayBuffer,
            uvArrayBuffer,
            opacity,
            colorBlendMode,
            modelColorRGBA,
            multiplyColor,
            screenColor,
            isPremultipliedAlpha(),
            getMvpMatrix(),
            invertedMask
        );

        // Draw the prygon mesh
        GLES20.glDrawElements(
            GL_TRIANGLES,
            indexCount,
            GL_UNSIGNED_SHORT,
//            ShortBuffer.wrap(indexArray)
            indexArray
        );

        // post-processing
        glUseProgram(0);
        setClippingContextBufferForDraw(null);
        setClippingContextBufferForMask(null);
    }

    // This is only used by 'drawMeshAndroid' method.
    // Avoid creating a new CubismTextureColor instance.
    private final CubismTextureColor modelColorRGBA = new CubismTextureColor();

    @Override
    protected void doDrawModel() {
        final CubismModel model = getModel();


        // In the case of clipping mask and buffer preprocessing method
        if (clippingManager != null) {
            preDraw();

            // If offscreen frame buffer size is different from clipping mask buffer size, recreate it.
            if (!offscreenFrameBuffer.isSameSize(clippingManager.getClippingMaskBufferSize())) {
                offscreenFrameBuffer.createOffscreenFrame(clippingManager.getClippingMaskBufferSize(), null);
            }

            clippingManager.setupClippingContext(model, this, rendererProfile.lastFBO, rendererProfile.lastViewport);
        }

        // preDraw() method is called twice.
        preDraw();

        final int[] renderOrder = model.getDrawableRenderOrders();

        final int drawableCount = model.getDrawableCount();

        // Sort the index by drawing order
        for (int i = 0; i < drawableCount; i++) {
            final int order = renderOrder[i];
            sortedDrawableIndexList[order] = i;
        }

        // FloatBufferのリストが空なら全Drawableの頂点とUV頂点のFloatBufferを作成して格納する
        if (vertexArrayFloatBufferMap == null) {
            vertexArrayFloatBufferMap = new HashMap<Integer, FloatBuffer>(drawableCount, 1); // 初期容量と負荷係数を適切に設定し、拡張を行わない
            for (int i = 0; i < drawableCount; i++) {
                final int drawableIndex = sortedDrawableIndexList[i];
                float[] vertexArray = model.getDrawableVertices(drawableIndex);

                ByteBuffer bb = ByteBuffer.allocateDirect(vertexArray.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer buffer = bb.asFloatBuffer();
                vertexArrayFloatBufferMap.put(drawableIndex, buffer);
            }
        }
        if (uvArrayFloatBufferMap == null) {
            uvArrayFloatBufferMap = new HashMap<Integer, FloatBuffer>(drawableCount, 1);
            for (int i = 0; i < drawableCount; i++) {
                final int drawableIndex = sortedDrawableIndexList[i];
                float[] uvArray = model.getDrawableVertexUvs(drawableIndex);

                ByteBuffer bb = ByteBuffer.allocateDirect(uvArray.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer buffer = bb.asFloatBuffer();
                uvArrayFloatBufferMap.put(drawableIndex, buffer);
            }
        }
        if (indexArrayBufferMap == null) {
            indexArrayBufferMap = new HashMap<Integer, ShortBuffer>(drawableCount, 1);
            for (int i = 0; i < drawableCount; i++) {
                final int drawableIndex = sortedDrawableIndexList[i];
                short[] indexArray = model.getDrawableVertexIndices(drawableIndex);

                ByteBuffer bb = ByteBuffer.allocateDirect(indexArray.length * 4);
                bb.order(ByteOrder.nativeOrder());
                ShortBuffer buffer = bb.asShortBuffer();
                indexArrayBufferMap.put(drawableIndex, buffer);
            }
        }

        // Draw process
        for (int i = 0; i < drawableCount; i++) {
            final int drawableIndex = sortedDrawableIndexList[i];

            // If Drawable is not in the display state, the process is passed.
            if (!model.getDrawableDynamicFlagIsVisible(drawableIndex)) {
                continue;
            }

            // Set clipping mask
            if (clippingManager.getClippingContextListForDraw().size() != 0) {
                setClippingContextBufferForDraw((clippingManager != null)
                                                ? clippingManager.getClippingContextListForDraw().get(drawableIndex)
                                                : null
                );
            }

            isCulling(model.getDrawableCulling(drawableIndex));


            FloatBuffer vertexArrayBuffer = vertexArrayFloatBufferMap.get(drawableIndex);
            vertexArrayBuffer.clear();
            vertexArrayBuffer.put(model.getDrawableVertices(drawableIndex));
            vertexArrayBuffer.position(0);

            FloatBuffer uvArrayBuffer = uvArrayFloatBufferMap.get(drawableIndex);
            uvArrayBuffer.clear();
            uvArrayBuffer.put(model.getDrawableVertexUvs(drawableIndex));
            uvArrayBuffer.position(0);

            ShortBuffer indexArrayBuffer = indexArrayBufferMap.get(drawableIndex);
            indexArrayBuffer.clear();
            indexArrayBuffer.put(model.getDrawableVertexIndices(drawableIndex));
            indexArrayBuffer.position(0);

            drawMeshAndroid(
                model.getDrawableTextureIndex(drawableIndex),
                model.getDrawableVertexIndexCount(drawableIndex),
                model.getDrawableVertexCount(drawableIndex),
//                model.getDrawableVertexIndices(drawableIndex),
//                model.getDrawableVertices(drawableIndex),
//                model.getDrawableVertexUvs(drawableIndex),
                indexArrayBuffer,
                vertexArrayBuffer,
                uvArrayBuffer,
                model.getMultiplyColor(drawableIndex),
                model.getScreenColor(drawableIndex),
                model.getDrawableOpacity(drawableIndex),
                model.getDrawableBlendMode(drawableIndex),
                model.getDrawableInvertedMask(drawableIndex)    // Whether the mask is used inverted
            );
        }

        postDraw();
    }

    private Map<Integer, FloatBuffer> vertexArrayFloatBufferMap;
    private Map<Integer, FloatBuffer> uvArrayFloatBufferMap;
    private Map<Integer, ShortBuffer> indexArrayBufferMap;

    @Override
    protected void drawMesh(CubismModel model, int drawableIndex) {
        drawMesh(model,
            drawableIndex,
            model.getDrawableBlendMode(drawableIndex),
            model.getDrawableInvertedMask(drawableIndex));
    }

    @Override
    protected void drawMesh(final CubismModel model,
                            final int drawableIndex,
                            final CubismBlendMode blendMode,
                            final boolean isInverted) {
        CubismDebug.cubismLogWarning("Use 'drawMeshAndroid' function");
        assert false;
    }

    @Override
    protected void saveProfile() {
        rendererProfile.save();
    }

    @Override
    protected void restoreProfile() {
        rendererProfile.restore();
    }

    /**
     * Release OpenGLES2 static shader programs.
     */
    static void doStaticRelease() {
        CubismShaderAndroid.deleteInstance();
    }

    /**
     * Get the clipping context to draw to the mask texture
     *
     * @return the clipping context to draw to the mask texture
     */
    CubismClippingContext getClippingContextBufferForMask() {
        return clippingContextBufferForMask;
    }

    /**
     * Set the clipping context to draw to the mask texture
     *
     * @param clip clipping context to draw to the mask texture
     */
    void setClippingContextBufferForMask(CubismClippingContext clip) {
        clippingContextBufferForMask = clip;
    }

    /**
     * Get the clipping context to draw on display
     *
     * @return the clipping context to draw on display
     */
    CubismClippingContext getClippingContextBufferForDraw() {
        return clippingContextBufferForDraw;
    }

    /**
     * Set the clipping context to draw on display
     *
     * @param clip the clipping context to draw on display
     */
    void setClippingContextBufferForDraw(CubismClippingContext clip) {
        clippingContextBufferForDraw = clip;
    }

    /**
     * Get the offscreen frame buffer
     *
     * @return the offscreen frame buffer
     */
    CubismOffscreenSurfaceAndroid getMaskBuffer() {
        return offscreenFrameBuffer;
    }

    /**
     * Additional proccesing at the start of drawing
     * This method implements the necessary processing for the clipping mask before drawing the model
     */
    void preDraw() {
        glDisable(GL_SCISSOR_TEST);
        glDisable(GL_STENCIL_TEST);
        glDisable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        GLES20.glColorMask(true, true, true, true);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        // If the buffer has been bound before, it needs to be destroyed
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Anisotropic filtering. If it is not supported, do not set it
        if (getAnisotropy() > 0.0f) {
            for (Map.Entry<Integer, Integer> entry : textures.entrySet()) {
                glBindTexture(GL_TEXTURE_2D, entry.getValue());
                glTexParameterf(GL_TEXTURE_2D, GLES11Ext.GL_TEXTURE_MAX_ANISOTROPY_EXT, getAnisotropy());
            }
        }
    }

    /**
     * Additinal processing after drawing is completed.
     */
    void postDraw() {}

    /**
     * Frame buffer for drawing mask
     */
    CubismOffscreenSurfaceAndroid offscreenFrameBuffer = new CubismOffscreenSurfaceAndroid();

    /**
     * Map between the textures referenced by the model and the textures bound by the renderer
     */
    private final Map<Integer, Integer> textures = new HashMap<Integer, Integer>(32);

    private boolean areTexturesChanged = true;

    private Map<Integer, Integer> cachedImmutableTextures;
    /**
     * A list of drawing object indices arranged in drawing order
     */
    private int[] sortedDrawableIndexList;
    /**
     * the object which keeps the OpenGL state
     */
    private final CubismRendererProfileAndroid rendererProfile = new CubismRendererProfileAndroid();
    /**
     * Clipping mask management object
     */
    private CubismClippingManagerAndroid clippingManager = new CubismClippingManagerAndroid();
    /**
     * Clippping context for drawing on mask texture
     */
    private CubismClippingContext clippingContextBufferForMask;
    /**
     * Clipping context for drawing on the screen
     */
    private CubismClippingContext clippingContextBufferForDraw;

}
