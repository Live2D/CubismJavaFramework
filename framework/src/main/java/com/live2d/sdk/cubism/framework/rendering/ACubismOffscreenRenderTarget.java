/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering;

/**
 * A class that manages render targets for offscreen rendering.
 * Generics specify the subclass type and the render target type respectively.
 *
 * @param <T> the subclass type
 * @param <U> the render target type
 */
public abstract class ACubismOffscreenRenderTarget<T, U> {
    /**
     * Sets the offscreen index.
     *
     * @param offscreenIndex the offscreen index
     */
    public void setOffscreenIndex(int offscreenIndex) {
        this.offscreenIndex = offscreenIndex;
    }

    /**
     * Returns the offscreen index.
     *
     * @return the offscreen index
     */
    public int getOffscreenIndex() {
        return offscreenIndex;
    }

    /**
     * Sets the previous offscreen render target.
     *
     * @param oldOffscreen the previous offscreen render target
     */
    public void setOldOffscreen(T oldOffscreen) {
        this.oldOffscreen = oldOffscreen;
    }

    /**
     * Returns the previous offscreen render target.
     *
     * @return the previous offscreen render target
     */
    public T getOldOffscreen() {
        return oldOffscreen;
    }

    /**
     * Sets the parent offscreen render target.
     *
     * @param parentRenderTarget the parent offscreen render target
     */
    public void setParentPartOffscreen(T parentRenderTarget) {
        this.parentRenderTarget = parentRenderTarget;
    }

    /**
     * Returns the parent offscreen render target.
     *
     * @return the parent offscreen render target
     */
    public T getParentPartOffscreen() {
        return parentRenderTarget;
    }

    /**
     * Returns the render target.
     *
     * @return the render target
     */
    public U getRenderTarget() {
        return renderTarget;
    }

    /**
     * An render target.
     */
    protected U renderTarget;

    /**
     * Index of offscreens.
     */
    private int offscreenIndex = -1;

    /**
     * Parent offscreen render target.
     */
    private T parentRenderTarget;

    /**
     * Previous offscreen render target.
     */
    private T oldOffscreen;
}
