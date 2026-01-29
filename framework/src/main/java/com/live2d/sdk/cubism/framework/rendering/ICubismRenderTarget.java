/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering;

public interface ICubismRenderTarget {
    /**
     * Begin drawing to the specific drawing target.
     *
     * @param restoreFBO If it is not "null", EndDraw will run glBindFrameBuffer this value.
     **/
    void beginDraw(int[] restoreFBO);

    /**
     * Finish drawing.
     **/
    void endDraw();

    /**
     * Clear the rendering target.
     * Note: Call this after BeginDraw().
     *
     * @param r red(0.0~1.0)
     * @param g green(0.0~1.0)
     * @param b blue(0.0~1.0)
     * @param a α(0.0~1.0)
     */
    void clear(final float r, final float g, final float b, final float a);

    /**
     * Create the CubismRenderTarget.
     *
     * @param displayBufferWidth  display buffer width
     * @param displayBufferHeight display buffer height
     * @param colorBuffer         if non-zero, use colorBuffer as pixel storage area.
     */
    void createRenderTarget(
        final int displayBufferWidth,
        final int displayBufferHeight,
        final int[] colorBuffer
    );

    /**
     * Destroy this CubismRenderTarget.
     */
    void destroyRenderTarget();

    /**
     * Returns the address of the render texture (as an int array).
     *
     * @return the address of the render texture
     */
    int[] getRenderTexture();

    /**
     * Returns color buffer.
     *
     * @return color buffer
     */
    int[] getColorBuffer();

    /**
     * Returns buffer width
     *
     * @return buffer width
     */
    int getBufferWidth();

    /**
     * Returns buffer height.
     *
     * @return buffer height
     */
    int getBufferHeight();

    /**
     * Whether render texture is valid.
     *
     * @return If it is valid, return true
     */
    boolean isValid();

    /**
     * Returns the old frame buffer object (FBO).
     *
     * @return the old frame buffer object
     */
    int[] getOldFBO();

    /**
     * Whether buffer size is the same.
     *
     * @param width  buffer width
     * @param height buffer height
     * @return Whether buffer size is the same
     */
    boolean isSameSize(final int width, final int height);
}
