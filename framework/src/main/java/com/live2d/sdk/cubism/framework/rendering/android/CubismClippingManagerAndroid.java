/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android;

import android.opengl.GLES20;
import com.live2d.sdk.cubism.framework.math.CubismMatrix44;
import com.live2d.sdk.cubism.framework.math.CubismRectangle;
import com.live2d.sdk.cubism.framework.math.CubismVector2;
import com.live2d.sdk.cubism.framework.model.CubismModel;
import com.live2d.sdk.cubism.framework.rendering.CubismRenderer;

import java.io.Closeable;
import java.nio.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.opengl.GLES20.*;
import static com.live2d.sdk.cubism.framework.CubismFramework.VERTEX_OFFSET;
import static com.live2d.sdk.cubism.framework.CubismFramework.VERTEX_STEP;
import static com.live2d.sdk.cubism.framework.utils.CubismDebug.cubismLogError;

/**
 * This class deals with clipping mask processes.
 */
class CubismClippingManagerAndroid implements Closeable {
    /**
     * Constructor
     */
    public CubismClippingManagerAndroid() {
        CubismRenderer.CubismTextureColor tmp = new CubismRenderer.CubismTextureColor();
        tmp.r = 1.0f;
        tmp.g = 0.0f;
        tmp.b = 0.0f;
        tmp.a = 0.0f;
        channelColors.add(tmp);

        tmp = new CubismRenderer.CubismTextureColor();
        tmp.r = 0.0f;
        tmp.g = 1.0f;
        tmp.b = 0.0f;
        tmp.a = 0.0f;
        channelColors.add(tmp);

        tmp = new CubismRenderer.CubismTextureColor();
        tmp.r = 0.0f;
        tmp.g = 0.0f;
        tmp.b = 1.0f;
        tmp.a = 0.0f;
        channelColors.add(tmp);

        tmp = new CubismRenderer.CubismTextureColor();
        tmp.r = 0.0f;
        tmp.g = 0.0f;
        tmp.b = 0.0f;
        tmp.a = 1.0f;
        channelColors.add(tmp);
    }

    /**
     * Close resources.
     */
    @Override
    public void close() {
        clippingContextListForMask.clear();
        clippingContextListForDraw.clear();

        if (maskTexture != null) {
            int[] texture = {maskTexture.texture};
            glDeleteFramebuffers(1, IntBuffer.wrap(texture));
            maskTexture = null;
        }

        GLES20.glDeleteTextures(1, IntBuffer.wrap(colorBuffer));

        channelColors.clear();

        vertexArrayMap = null;
        uvArrayMap = null;
        indexArrayMap = null;
    }

    /**
     * Initialization process of the manager.
     * Register drawing objects that use clipping masks.
     *
     * @param drawableCount number of drawing objects
     * @param drawableMasks list of drawing object indices to mask drawing objects
     * @param drawableMaskCounts number of drawing objects to mask drawing objects.
     */
    public void initialize(
        int drawableCount,
        final int[][] drawableMasks,
        final int[] drawableMaskCounts
    ) {
        // Register all drawing objects that use clipping masks.
        // The use of clipping masks is usually limited to a few objects.
        for (int i = 0; i < drawableCount; i++) {
            if (drawableMaskCounts[i] <= 0) {
                // Art mesh with no clipping mask (often not used)
                clippingContextListForDraw.add(null);
                continue;
            }

            // Check if it is the same as an already existing ClipContext.
            CubismClippingContext cc = findSameClip(drawableMasks[i], drawableMaskCounts[i]);
            if (cc == null) {
                // Generate if no identical mask exists.
                cc = new CubismClippingContext(this, drawableMasks[i], drawableMaskCounts[i]);
                clippingContextListForMask.add(cc);
            }
            cc.addClippedDrawable(i);
            clippingContextListForDraw.add(cc);
        }
    }

    /**
     * Create a clipping context. Run at drawing the model.
     *
     * @param model model instance
     * @param renderer renderer instance
     */
    public void setupClippingContext(CubismModel model, CubismRendererAndroid renderer, int[] lastFBO, int[] lastViewport) {
        currentFrameNumber++;

        // FloatBufferのリストが空なら全Drawableの頂点とUV頂点のFloatBufferを作成して格納する
        // マップサイズ算出
        int mapSize = 0;
        for (int i = 0; i < clippingContextListForMask.size(); i++) {
            CubismClippingContext clippingContext = clippingContextListForMask.get(i);

            final int clipDrawCount = clippingContext.getClippingIdCount();
            mapSize += clipDrawCount;
        }

        if (vertexArrayMap == null) {
            vertexArrayMap = new HashMap<Integer, FloatBuffer>(mapSize, 1);
            for (int j = 0; j < clippingContextListForMask.size(); j++) {
                CubismClippingContext clipContext = clippingContextListForMask.get(j);

                final int clipDrawCount = clipContext.getClippingIdCount();
                for (int i = 0; i < clipDrawCount; i++) {
                    final int clipDrawIndex = clipContext.getClippingIdList()[i];
                    float[] vertexArray = model.getDrawableVertices(clipDrawIndex);

                    ByteBuffer bb = ByteBuffer.allocateDirect(vertexArray.length * 4);
                    bb.order(ByteOrder.nativeOrder());
                    FloatBuffer vertexArrayBuffer = bb.asFloatBuffer();
                    vertexArrayMap.put(clipDrawIndex, vertexArrayBuffer);
                }
            }
        }
        if (uvArrayMap == null) {
            uvArrayMap = new HashMap<Integer, FloatBuffer>(mapSize, 1);
            for (int j = 0; j < clippingContextListForMask.size(); j++) {
                CubismClippingContext clipContext = clippingContextListForMask.get(j);

                final int clipDrawCount = clipContext.getClippingIdCount();
                for (int i = 0; i < clipDrawCount; i++) {
                    final int clipDrawIndex = clipContext.getClippingIdList()[i];
                    float[] uvArray = model.getDrawableVertexUvs(clipDrawIndex);

                    ByteBuffer bb = ByteBuffer.allocateDirect(uvArray.length * 4);
                    bb.order(ByteOrder.nativeOrder());
                    FloatBuffer uvArrayBuffer = bb.asFloatBuffer();
                    uvArrayMap.put(clipDrawIndex, uvArrayBuffer);
                }
            }
        }
        if (indexArrayMap == null) {
            indexArrayMap = new HashMap<Integer, ShortBuffer>(mapSize, 1);
            for (int j = 0; j < clippingContextListForMask.size(); j++) {
                CubismClippingContext clipContext = clippingContextListForMask.get(j);

                final int clipDrawCount = clipContext.getClippingIdCount();
                for (int i = 0; i < clipDrawCount; i++) {
                    final int clipDrawIndex = clipContext.getClippingIdList()[i];
                    short[] indexArray = model.getDrawableVertexIndices(clipDrawIndex);

                    ByteBuffer bb = ByteBuffer.allocateDirect(indexArray.length * 4);
                    bb.order(ByteOrder.nativeOrder());
                    ShortBuffer indexArrayBuffer = bb.asShortBuffer();
                    indexArrayMap.put(clipDrawIndex, indexArrayBuffer);
                }
            }
        }

        // Prepare all clipping.
        // Set only once when using the same clip (or a group of clips if there are multiple clips).
        int usingClipCount = 0;
        for (int i = 0; i < clippingContextListForMask.size(); i++) {
            CubismClippingContext clipContext = clippingContextListForMask.get(i);

            // Calculate the rectangle that encloses the entire group of drawing objects that use this clip.
            calcClippedDrawTotalBounds(model, clipContext);

            if (clipContext.isUsing()) {
                // Count as in use.
                usingClipCount++;
            }
        }

        if (!(usingClipCount > 0)) {
            return;
        }

        // TODO: 高精細マスクに対応
//        if (!renderer.isUsingHighPrecisionMask()) {
//            // 生成したFrameBufferと同じサイズでビューポートを設定
//            glViewport(0, 0, (int) _clippingMaskBufferSize.x, (int) _clippingMaskBufferSize.y);
//
//            // モデル描画時にDrawMeshNowに渡される変換 (モデルtoワールド座標変換)
//            CubismMatrix4x4 modelToWorldF = renderer.getMvpMatrix();
//
//            // バッファをクリアする
//            renderer.preDraw();
//
//            // _offscreenFrameBufferへ切り替え
//            renderer._offscreenFrameBuffer.beginDraw(lastFBO);
//
//            // マスクをクリアする
//            // 1が無効(描画されない)領域、0が有効(描画される)領域。(シェーダーでCd*Csで0に近い値をかけてマスクを作る。1をかけると何も起こらない。)
//            renderer._offscreenFrameBuffer.clear(1.0f, 1.0f, 1.0f, 1.0f);
//        }

        // Process of creating mask.
        // Save the current value of viewport.
//        int[] viewport = new int[4];
        for (int i = 0; i < 4; i++) {
            viewport[i] = 0;
        }

        glGetIntegerv(GL_VIEWPORT, viewport, 0);

        // Set up a viewport with the same size as the generated FrameBuffer.
        glViewport(0, 0, (int) clippingMaskBufferSize.x, (int) clippingMaskBufferSize.y);

        // Save the FBO before switching to mask active
        oldFBO[0] = 0;

        glGetIntegerv(GL_FRAMEBUFFER_BINDING, oldFBO, 0);

        // Set mask to active.
        final int maskRenderTexture = getMaskRenderTexture();

        // モデル描画時にDrawMeshNowに渡される変換（モデルtoワールド座標変換）
//        CubismMatrix44 modelToWorldF = renderer.getMvpMatrix();

        // Clear the buffer.
        renderer.preDraw();

        // Determine the layout of each mask.
        setupLayoutBounds(usingClipCount);

        // ---------- Mask Drawing Process -----------
        // Set the RenderTexture for the mask to active.
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, maskRenderTexture);

        // Clear the mask.
        // (temporary spec) 1 is invalid (not drawn), 0 is valid (drawn).
        // (In the shader, in Cd*Cs multiply by a value close to 0 to create a mask; multiply by 1 and nothing happens)
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Actually generate the masks.
        // Determine how to layout and draw all the masks, and store them in ClipContext and ClippedDrawContext.
        for (int j = 0; j < clippingContextListForMask.size(); j++) {
            CubismClippingContext clipContext = clippingContextListForMask.get(j);

            // The enclosing rectangle in logical coordinates of all drawing objects that use this mask.
            CubismRectangle allClippedDrawRect = clipContext.getAllClippedDrawRect();
            // Fit the mask in here.
            CubismRectangle layoutBoundsOnTex01 = clipContext.getLayoutBounds();

            // Use a rectangle on the model coordinates with margins as appropriate.
//            CubismRectangle _tmpBoundsOnModel = CubismRectangle.create(allClippedDrawRect);
            tmpBoundsOnModel.setX(allClippedDrawRect.getX());
            tmpBoundsOnModel.setY(allClippedDrawRect.getY());
            tmpBoundsOnModel.setWidth(allClippedDrawRect.getWidth());
            tmpBoundsOnModel.setHeight(allClippedDrawRect.getHeight());


            final float margin = 0.05f;
//            tmpBoundsOnModel.setRect(allClippedDrawRect);
            tmpBoundsOnModel.expand(
                allClippedDrawRect.getWidth() * margin,
                allClippedDrawRect.getHeight() * margin
            );

            // ######## It is best to keep the size to a minimum, rather than using the entire allocated space.
            // Find the formula for the shader. If rotation is not taken into account, the formula is as follows.
            // movePeriod' = movePeriod * scaleX + offX     [[ movePeriod' = (movePeriod - tmpBoundsOnModel.movePeriod)*scale + layoutBoundsOnTex01.movePeriod ]]
            final float scaleX = layoutBoundsOnTex01.getWidth() / tmpBoundsOnModel.getWidth();
            final float scaleY = layoutBoundsOnTex01.getHeight() / tmpBoundsOnModel.getHeight();

            // Calculate the matrix to be used for mask generation.
            tmpMatrix.loadIdentity();

            // Find the matrix to pass to the shader <<< optimization required (can be simplified by calculating in reverse order)
            // Convert Layout0..1 to -1..1
            tmpMatrix.translateRelative(-1.0f, -1.0f);
            tmpMatrix.scaleRelative(2.0f, 2.0f);
            // view to Layout0..1
            tmpMatrix.translateRelative(
                layoutBoundsOnTex01.getX(),
                layoutBoundsOnTex01.getY()
            );
            tmpMatrix.scaleRelative(scaleX, scaleY);
            tmpMatrix.translateRelative(
                -tmpBoundsOnModel.getX(),
                -tmpBoundsOnModel.getY()
            );
            clipContext.getMatrixForMask().setMatrix(tmpMatrix);

            // Calculate the mask reference matrix for draw.
            // Find the matrix to pass to the shader <<< optimization required (can be simplified by calculating in reverse order)
            tmpMatrix.loadIdentity();
            tmpMatrix.translateRelative(
                layoutBoundsOnTex01.getX(),
                layoutBoundsOnTex01.getY()
            );
            tmpMatrix.scaleRelative(scaleX, scaleY);
            tmpMatrix.translateRelative(
                -tmpBoundsOnModel.getX(),
                -tmpBoundsOnModel.getY()
            );
            clipContext.getMatrixForDraw().setMatrix(tmpMatrix);

            final int clipDrawCount = clipContext.getClippingIdCount();
            for (int i = 0; i < clipDrawCount; i++) {
                final int clipDrawIndex = clipContext.getClippingIdList()[i];

                // If vertex information is not updated and reliable, pass drawing.
                if (!model.getDrawableDynamicFlagVertexPositionsDidChange(clipDrawIndex)) {
                    continue;
                }

                renderer.isCulling(model.getDrawableCulling(clipDrawIndex));

                // Apply this special transformation to draw it.
                // Switching channel is also needed.(A,R,G,B)
                renderer.setClippingContextBufferForMask(clipContext);

                FloatBuffer vertexArrayBuffer = vertexArrayMap.get(clipDrawIndex);
                vertexArrayBuffer.clear();
                vertexArrayBuffer.put(model.getDrawableVertices(clipDrawIndex));
                vertexArrayBuffer.position(0);

                FloatBuffer uvArrayBuffer = uvArrayMap.get(clipDrawIndex);
                uvArrayBuffer.clear();
                uvArrayBuffer.put(model.getDrawableVertexUvs(clipDrawIndex));
                uvArrayBuffer.position(0);

                ShortBuffer indexArrayBuffer = indexArrayMap.get(clipDrawIndex);
                indexArrayBuffer.clear();
                indexArrayBuffer.put(model.getDrawableVertexIndices(clipDrawIndex));
                indexArrayBuffer.position(0);

                renderer.drawMeshAndroid(
                    model.getDrawableTextureIndex(clipDrawIndex),
                    model.getDrawableVertexIndexCount(clipDrawIndex),
                    model.getDrawableVertexCount(clipDrawIndex),
                    indexArrayBuffer,
                    vertexArrayBuffer,
                    uvArrayBuffer,
                    model.getMultiplyColor(clipDrawIndex),
                    model.getScreenColor(clipDrawIndex),
                    model.getDrawableOpacity(clipDrawIndex),
                    CubismRenderer.CubismBlendMode.NORMAL,  // Clipping is forced normal drawing.
                    false   // The inverted use of clipping is completely irrelevant when generating masks.
                );
            }
        }

        // --- Post Processing ---
        // Return the drawing target
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, oldFBO[0]);
        renderer.setClippingContextBufferForMask(null);

        glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);
    }

    private final int[] viewport = new int[4];
    private final int[] oldFBO = new int[1];

    private final CubismMatrix44 tmpMatrix = CubismMatrix44.create();
    private Map<Integer, FloatBuffer> vertexArrayMap;
    private Map<Integer, FloatBuffer> uvArrayMap;
    private Map<Integer, ShortBuffer> indexArrayMap;

    private final CubismRectangle tmpBoundsOnModel = CubismRectangle.create();


    /**
     * Get the flag of color channel(RGBA).
     *
     * @param channelNo number of color channel(RGBA)(0:R, 1:G, 2:B, 3:A)
     */
    public CubismRenderer.CubismTextureColor getChannelFlagAsColor(int channelNo) {
        return channelColors.get(channelNo);
    }

    /**
     * Get color buffer.
     *
     * @return color buffer
     */
    public int getColorBuffer() {
        return colorBuffer[0];
    }

    /**
     * Get a list of clipping masks to be used for screen drawing.
     *
     * @return list of clipping masks to be usef for screen drawing
     */
    public List<CubismClippingContext> getClippingContextListForDraw() {
        return clippingContextListForDraw;
    }

    /**
     * Get the size of clipping mask buffer.
     *
     * @return size of clipping mask buffer
     */
    public CubismVector2 getClippingMaskBufferSize() {
        return clippingMaskBufferSize;
    }

    /**
     * Set the size of clipping mask buffer.
     *
     * @param width width of clipping mask buffer
     * @param height height of clipping mask buffer
     */
    public void setClippingMaskBufferSize(float width, float height) {
        clippingMaskBufferSize.set(width, height);
    }

    /**
     * This class defines resources of render texture.
     * <p>
     * It is used in clipping masks.
     */
    private static class CubismRenderTextureResource {
        /**
         * renderer's frame number
         */
        int frameNo;
        /**
         * texture address number
         */
        int texture;

        /**
         * Constructor
         *
         * @param frameNo renderer's frame number
         * @param texture texture address
         */
        CubismRenderTextureResource(int frameNo, int texture) {
            this.frameNo = frameNo;
            this.texture = texture;
        }
    }

    /**
     * 1 for one channel at the time of the experiment, 3 for only RGB, and 4 for including alpha.
     */
    private static final int COLOR_CHANNEL_COUNT = 4;

    /**
     * Check if the mask has already been created.
     * If it has, return an instance of the corresponding clipping mask.
     * If it has not been created, return null.
     *
     * @param drawableMasks list of drawing objects to mask drawing objects
     * @param drawableMaskCounts number of drawing objects to mask drawing objects
     * @return returns an instance of the corresponding clipping mask if it exists, or null if it does not.
     */
    private CubismClippingContext findSameClip(final int[] drawableMasks, int drawableMaskCounts) {
        // Check if the ClippingContext matches the one already created.
        for (int k = 0; k < clippingContextListForMask.size(); k++) {
            CubismClippingContext clipContext = clippingContextListForMask.get(k);

            final int count = clipContext.getClippingIdCount();
            if (count != drawableMaskCounts) {
                // If the number of pieces is different, it's different.
                continue;
            }
            int sameCount = 0;

            // Check if they have the same ID. Since the number of arrays is the same, if the number of matches is the same, it is assumed the same thing.
            for (int i = 0; i < count; i++) {
                final int clipId = clipContext.getClippingIdList()[i];
                for (int j = 0; j < count; ++j) {
                    if (drawableMasks[j] == clipId) {
                        sameCount++;
                        break;
                    }
                }
            }
            if (sameCount == count) {
                return clipContext;
            }
        }
        // Cannot find the same clip.
        return null;
    }


    /**
     * Calculate the rectangle that encloses the entire group of drawing objects to be masked(model coordinate system).
     *
     * @param model model instance
     * @param clippingContext context of clipping mask
     */
    private void calcClippedDrawTotalBounds(CubismModel model, CubismClippingContext clippingContext) {
        // The overall rectangle of the clipping mask (the drawing object to be masked).
        float clippedDrawTotalMinX = Float.MAX_VALUE;
        float clippedDrawTotalMinY = Float.MAX_VALUE;
        float clippedDrawTotalMaxX = -Float.MAX_VALUE;
        float clippedDrawTotalMaxY = -Float.MAX_VALUE;

        // Determine if this mask is actually needed.
        // If there is even one "drawing object" available that uses this clipping, generating a mask is required.
        final int clippedDrawCount = clippingContext.getClippedDrawableIndexList().size();

        for (int clippedDrawableIndex = 0; clippedDrawableIndex < clippedDrawCount; clippedDrawableIndex++) {
            // Find the rectangle to be drawn for a drawing object that uses a mask.
            final int drawableIndex = clippingContext.getClippedDrawableIndexList().get(clippedDrawableIndex);

            final int drawableVertexCount = model.getDrawableVertexCount(drawableIndex);
            float[] drawableVertices = model.getDrawableVertices(drawableIndex);

            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;

            int loop = drawableVertexCount * VERTEX_STEP;
            for (int pi = VERTEX_OFFSET; pi < loop; pi += VERTEX_STEP) {
                float x = drawableVertices[pi];
                float y = drawableVertices[pi + 1];
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }

            // If getting a single valid point is falied, skip it.
            if (minX == Float.MAX_VALUE) {
                continue;
            }

            // Reflect in the overall rectangle.
            if (minX < clippedDrawTotalMinX) clippedDrawTotalMinX = minX;
            if (maxX > clippedDrawTotalMaxX) clippedDrawTotalMaxX = maxX;
            if (minY < clippedDrawTotalMinY) clippedDrawTotalMinY = minY;
            if (maxY > clippedDrawTotalMaxY) clippedDrawTotalMaxY = maxY;
        }

        if (clippedDrawTotalMinX == Float.MAX_VALUE) {
            clippingContext.isUsing(false);

            CubismRectangle clippedDrawRect = clippingContext.getAllClippedDrawRect();
            clippedDrawRect.setX(0.0f);
            clippedDrawRect.setY(0.0f);
            clippedDrawRect.setWidth(0.0f);
            clippedDrawRect.setHeight(0.0f);
        } else {
            clippingContext.isUsing(true);
            float w = clippedDrawTotalMaxX - clippedDrawTotalMinX;
            float h = clippedDrawTotalMaxY - clippedDrawTotalMinY;

            CubismRectangle clippedDrawRect = clippingContext.getAllClippedDrawRect();
            clippedDrawRect.setX(clippedDrawTotalMinX);
            clippedDrawRect.setY(clippedDrawTotalMinY);
            clippedDrawRect.setWidth(w);
            clippedDrawRect.setHeight(h);
        }
    }

    /**
     * Get the address of the temporary render texture.
     * If FrameBufferObject does not exist, create a new one.
     *
     * @return address of render texture
     */
    private int getMaskRenderTexture() {
//        int[] result = new int[1];
        forGetMaskRenderTextureResult[0] = 0;

        // Get a temporary RenderTexture.
        if (maskTexture != null && maskTexture.texture != 0) {
            maskTexture.frameNo = currentFrameNumber;
            forGetMaskRenderTextureResult[0] = maskTexture.texture;
        }

        if (forGetMaskRenderTextureResult[0] == 0) {
            // If FramebufferObject does not exist, create a new one.
            final int width = (int) clippingMaskBufferSize.x;
            final int height = (int) clippingMaskBufferSize.y;

            GLES20.glGenTextures(1, IntBuffer.wrap(colorBuffer));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, colorBuffer[0]);
            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_RGBA,
                width,
                height,
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                null
            );
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            int[] tmpFrameBufferObject = new int[1];
            GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, tmpFrameBufferObject, 0);

            GLES20.glGenFramebuffers(1, forGetMaskRenderTextureResult, 0);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, forGetMaskRenderTextureResult[0]);
            GLES20.glFramebufferTexture2D(
                GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                colorBuffer[0],
                0
            );
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, tmpFrameBufferObject[0]);

            maskTexture = new CubismRenderTextureResource(currentFrameNumber, forGetMaskRenderTextureResult[0]);
        }

        return forGetMaskRenderTextureResult[0];
    }

    private final int[] forGetMaskRenderTextureResult = new int[1];


    /**
     * Layout to place the clipping context.
     * Layout masks using as much of a single render texture as possible.
     * Layout masks using as much of a single render texture as possible.
     * If the number of mask groups is 4 or less, layout one mask for each RGBA channel; if the number is between 5 and 6, layout RGBA as 2,2,1,1.
     *
     * @param usingClipCount number of clipping contexts to place
     */
    private void setupLayoutBounds(int usingClipCount) {
        // Layout the masks using as much of one RenderTexture as possible.
        // If the number of mask groups is 4 or less, place one mask in each RGBA channel; if the number is between 5 and 6, place RGBA 2,2,1,1

        // Use the RGBAs in order
        // Basic number of masks to place on one channel
        final int div = usingClipCount / COLOR_CHANNEL_COUNT;
        // Remainder. Allocate one to each channel of this number.
        final int mod = usingClipCount % COLOR_CHANNEL_COUNT;

        // Prepare the channels for each RGBA. (0:R, 1:G, 2:B, 3:A)
        // Set them in order.
        int curClipIndex = 0;

        for (int channelNo = 0; channelNo < COLOR_CHANNEL_COUNT; channelNo++) {
            // Number of layouts for this channel.
            final int layoutCount = div + (channelNo < mod ? 1 : 0);

            // Determine the division method.
            if (layoutCount == 0) {
                // Do nothing.
            } else if (layoutCount == 1) {
                // Use everything as is.
                CubismClippingContext cc = clippingContextListForMask.get(curClipIndex++);
                cc.setLayoutChannelNo(channelNo);
                CubismRectangle bounds = cc.getLayoutBounds();

                bounds.setX(0.0f);
                bounds.setY(0.0f);
                bounds.setWidth(1.0f);
                bounds.setHeight(1.0f);
            } else if (layoutCount == 2) {
                for (int i = 0; i < layoutCount; i++) {
                    final int xpos = i % 2;

                    CubismClippingContext cc = clippingContextListForMask.get(curClipIndex++);
                    cc.setLayoutChannelNo(channelNo);
                    CubismRectangle bounds = cc.getLayoutBounds();

                    bounds.setX(xpos * 0.5f);
                    bounds.setY(0.0f);
                    bounds.setWidth(0.5f);
                    bounds.setHeight(1.0f);
                    // UVを2つに分解して使う
                }
            } else if (layoutCount <= 4) {
                // 4分割して使う
                for (int i = 0; i < layoutCount; i++) {
                    final int xpos = i % 2;
                    final int ypos = i / 2;

                    CubismClippingContext cc = clippingContextListForMask.get(curClipIndex++);
                    cc.setLayoutChannelNo(channelNo);
                    CubismRectangle bounds = cc.getLayoutBounds();

                    bounds.setX(xpos * 0.5f);
                    bounds.setY(ypos * 0.5f);
                    bounds.setWidth(0.5f);
                    bounds.setHeight(0.5f);
                }
            } else if (layoutCount <= 9) {
                // 9分割して使う
                for (int i = 0; i < layoutCount; i++) {
                    final int xpos = i % 3;
                    final int ypos = i / 3;

                    CubismClippingContext cc = clippingContextListForMask.get(curClipIndex++);
                    cc.setLayoutChannelNo(channelNo);
                    CubismRectangle bounds = cc.getLayoutBounds();

                    bounds.setX(xpos / 3.0f);
                    bounds.setY(ypos / 3.0f);
                    bounds.setWidth(1.0f / 3.0f);
                    bounds.setHeight(1.0f / 3.0f);
                }
            } else {
                cubismLogError("not supported mask count : " + layoutCount);

                // Stop this program if in development mode.
                assert false;

                // If you continue to run, SetupShaderProgram() method will cause over-access, so you have no choice but to put it in properly.
                // Of course, the result of drawing is so bad.
                for (int i = 0; i < layoutCount; i++) {
                    CubismClippingContext cc = clippingContextListForMask.get(curClipIndex++);
                    cc.setLayoutChannelNo(0);

                    CubismRectangle bounds = cc.getLayoutBounds();
                    bounds.setX(0.0f);
                    bounds.setY(0.0f);
                    bounds.setWidth(1.0f);
                    bounds.setHeight(1.0f);
                }
            }
        }
    }


    /**
     * Color buffer for masks.
     */
    private final int[] colorBuffer = new int[1];
    /**
     * Frame number given to the mask texture.
     */
    private int currentFrameNumber;
    /**
     * list of flags of color channel(RGBA)(0:R, 1:G, 2:B, 3:A)
     */
    private final List<CubismRenderer.CubismTextureColor> channelColors = new ArrayList<CubismRenderer.CubismTextureColor>();
    /**
     * texture resources for masks.
     */
    private CubismRenderTextureResource maskTexture;
    /**
     * List of clipping contexts for masks.
     */
    private final List<CubismClippingContext> clippingContextListForMask = new ArrayList<CubismClippingContext>();
    /**
     * List of clipping contexts for drawing.
     */
    private final List<CubismClippingContext> clippingContextListForDraw = new ArrayList<CubismClippingContext>();
    /**
     * Buffer size for clipping mask.
     */
    private final CubismVector2 clippingMaskBufferSize = new CubismVector2(256, 256);
}
