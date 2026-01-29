/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android;

import com.live2d.sdk.cubism.framework.math.CubismVector2;
import com.live2d.sdk.cubism.framework.rendering.ICubismRenderTarget;

import java.nio.IntBuffer;
import java.util.Arrays;

import static android.opengl.GLES20.*;

/**
 * This class is for drawing offscreen.
 **/
public class CubismRenderTargetAndroid implements ICubismRenderTarget {
    /**
     * Constructor
     */
    public CubismRenderTargetAndroid() {}

    /**
     * Copy constructor
     *
     * @param offscreenSurface offscreen surface buffer
     */
    public CubismRenderTargetAndroid(CubismRenderTargetAndroid offscreenSurface) {
        renderTexture = Arrays.copyOf(offscreenSurface.renderTexture, offscreenSurface.renderTexture.length);
        colorBuffer = Arrays.copyOf(offscreenSurface.colorBuffer, offscreenSurface.colorBuffer.length);
        oldFBO = Arrays.copyOf(offscreenSurface.oldFBO, offscreenSurface.oldFBO.length);

        bufferWidth = offscreenSurface.bufferWidth;
        bufferHeight = offscreenSurface.bufferHeight;
        isColorBufferInherited = offscreenSurface.isColorBufferInherited;
    }

    @Override
    public void beginDraw(int[] restoreFBO) {
        if (renderTexture == null) {
            return;
        }

        // Remember the back buffer surface.
        if (restoreFBO == null) {
            glGetIntegerv(GL_FRAMEBUFFER_BINDING, oldFBO, 0);
        } else {
            oldFBO = restoreFBO;
        }

        // Set the RenderTexture for the mask to active.
        glBindFramebuffer(GL_FRAMEBUFFER, renderTexture[0]);
    }

    /**
     * Begin drawing to the specific drawing target.
     * <p>
     * This method reproduces default argument of C++. The users can use this method instead of specifying null as an argument.
     * </p>
     */
    public void beginDraw() {
        beginDraw(null);
    }

    @Override
    public void endDraw() {
        if (renderTexture == null) {
            return;
        }

        // Return the drawing target.
        glBindFramebuffer(GL_FRAMEBUFFER, oldFBO[0]);
    }

    @Override
    public void clear(final float r, final float g, final float b, final float a) {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * Create CubismOffscreenSurface.
     * <p>
     * This method reproduces default argument of C++. The users can use this method instead of specifying null as colorBuffer(2rd) argument.
     * </p>
     *
     * @param displayBufferSize buffer size(Vector type)
     */
    public void createRenderTarget(CubismVector2 displayBufferSize) {
        createRenderTarget((int) displayBufferSize.x, (int) displayBufferSize.y, null);
    }

    /**
     * Create the CubismRenderTarget.
     *
     * @param displayBufferSize buffer size
     * @param colorBuffer if non-zero, use colorBuffer as pixel storage area.
     */
    public void createRenderTarget(final CubismVector2 displayBufferSize, final int[] colorBuffer) {
        createRenderTarget((int) displayBufferSize.x, (int) displayBufferSize.y, colorBuffer);
    }

    /**
     * Create the CubismRenderTarget.
     * <p>
     * This method reproduces default argument of C++. The users can use this method instead of specifying null as colorBuffer(3rd) argument.
     * </p>
     *
     * @param displayBufferWidth buffer width
     * @param displayBufferHeight buffer height
     */
    public void createRenderTarget(int displayBufferWidth, int displayBufferHeight) {
        createRenderTarget(displayBufferWidth, displayBufferHeight, null);
    }

    @Override
    public void createRenderTarget(final int displayBufferWidth, final int displayBufferHeight, final int[] colorBuffer) {
        // いったん削除
        destroyRenderTarget();

        int[] ret = new int[1];

        // Create new offscreen surface
        if (colorBuffer == null) {
            this.colorBuffer = new int[1];
            glGenTextures(1, this.colorBuffer, 0);

            glBindTexture(GL_TEXTURE_2D, this.colorBuffer[0]);
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                displayBufferWidth,
                displayBufferHeight,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                null);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, 0);

            isColorBufferInherited = false;
        }
        // Use the designated color buffer.
        else {
            this.colorBuffer = colorBuffer;
            isColorBufferInherited = true;
        }

        int[] tmpFBO = new int[1];

        glGetIntegerv(GL_FRAMEBUFFER_BINDING, tmpFBO, 0);

        glGenFramebuffers(1, ret, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, ret[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.colorBuffer[0], 0);
        glBindFramebuffer(GL_FRAMEBUFFER, tmpFBO[0]);

        this.renderTexture = new int[1];
        this.renderTexture[0] = ret[0];
        bufferWidth = displayBufferWidth;
        bufferHeight = displayBufferHeight;
    }

    @Override
    public void destroyRenderTarget() {
        if (!isColorBufferInherited && (colorBuffer != null)) {
            glDeleteTextures(1, colorBuffer, 0);
            colorBuffer = null;
        }

        if (renderTexture != null) {
            glDeleteFramebuffers(1, renderTexture, 0);
            renderTexture = null;
        }
    }

    @Override
    public int[] getRenderTexture() {
        return renderTexture;
    }

    @Override
    public int[] getColorBuffer() {
        return colorBuffer;
    }

    @Override
    public int getBufferWidth() {
        return bufferWidth;
    }

    @Override
    public int getBufferHeight() {
        return bufferHeight;
    }

    @Override
    public boolean isValid() {
        return renderTexture != null;
    }

    @Override
    public int[] getOldFBO() {
        return oldFBO;
    }

    /**
     * Whether buffer size is the same.
     *
     * @param bufferSize buffer size
     * @return Whether buffer size is the same
     */
    public boolean isSameSize(final CubismVector2 bufferSize) {
        int width = (int) bufferSize.x;
        int height = (int) bufferSize.y;

        return (width == bufferWidth && height == bufferHeight);
    }

    @Override
    public boolean isSameSize(final int width, final int height) {
        return (width == bufferWidth && height == bufferHeight);
    }

    /**
     * texture as rendering target. It is called frame buffer.
     */
    private int[] renderTexture;

    /**
     * color buffer
     */
    private int[] colorBuffer = new int[1];

    /**
     * old frame buffer
     */
    private int[] oldFBO = new int[1];

    /**
     * width specified at Create() method
     */
    private int bufferWidth;

    /**
     * height specified at Create() method
     */
    private int bufferHeight;

    /**
     * Whether the color buffer is the one set by the argument
     */
    private boolean isColorBufferInherited;
}
