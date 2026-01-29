/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that manages render targets for offscreen rendering.
 *
 * @param <T> the offscreen render target type
 */
public abstract class ACubismOffscreenManager<T extends ICubismRenderTarget> {
    /**
     * Releases all render targets and their associated resources.
     */
    public void dispose() {
        for (int i = 0; i < offscreenRenderTargetList.size(); i++) {
            offscreenRenderTargetList.get(i).renderTarget.destroyRenderTarget();
        }
    }

    /**
     * Performs frame start processing called before drawing the model.
     */
    public void beginFrameProcess() {
        if (hasResetThisFrame) {
            return;
        }

        previousActiveRenderTextureMaxCount = 0;
        hasResetThisFrame = true;
    }

    /**
     * Performs frame end processing called after drawing the model.
     */
    public void endFrameProcess() {
        hasResetThisFrame = false;
    }

    /**
     * Returns the list size of render targets.
     *
     * @return the number of render textures for drawing objects
     */
    public int getOffscreenRenderTargetListSize() {
        return offscreenRenderTargetList.size();
    }

    /**
     * Returns the usage state of the render target.
     *
     * @param renderTarget the render target
     * @return true if in use, false if unused
     */
    public boolean getUsingRenderTextureState(T renderTarget) {
        for (int i = 0; i < offscreenRenderTargetList.size(); i++) {
            if (offscreenRenderTargetList.get(i).renderTarget == renderTarget) {
                return offscreenRenderTargetList.get(i).isUsed;
            }
        }

        return false;
    }

    /**
     * Stops using the render target.
     *
     * @param renderTarget the render target
     */
    public void stopUsingRenderTexture(T renderTarget) {
        for (int i = 0; i < offscreenRenderTargetList.size(); i++) {
            if (offscreenRenderTargetList.get(i).renderTarget == renderTarget) {
                if (!offscreenRenderTargetList.get(i).isUsed) {
                    break;
                }

                offscreenRenderTargetList.get(i).isUsed = false;

                if (0 < currentActiveRenderTextureCount) {
                    currentActiveRenderTextureCount--;
                }

                break;
            }
        }
    }

    /**
     * Stops using all render targets.
     */
    public void stopUsingAllRenderTextures() {
        for (int i = 0; i < offscreenRenderTargetList.size(); i++) {
            offscreenRenderTargetList.get(i).isUsed = false;
        }

        currentActiveRenderTextureCount = 0;
    }

    /**
     * Releases unused render targets.
     */
    public void releaseStaleRenderTextures() {
        final int listSize = offscreenRenderTargetList.size();
        if (hasResetThisFrame || listSize == 0) {
            // 使用する量が変化する場合は解放しない
            return;
        }

        // 未使用な場所を解放して直前の最大数までリサイズする
        int findPos = 0;
        int resize = previousActiveRenderTextureMaxCount;

        for (int i = listSize; previousActiveRenderTextureMaxCount < i; i--) {
            final int index = i - 1;
            if (offscreenRenderTargetList.get(index).isUsed) {
                // 空いている場所探して移動させる
                boolean isFind = false;
                for (; findPos < previousActiveRenderTextureMaxCount; findPos++) {
                    if (!offscreenRenderTargetList.get(findPos).isUsed) {
                        T renderTarget = offscreenRenderTargetList.get(findPos).renderTarget;
                        offscreenRenderTargetList.get(findPos).renderTarget = offscreenRenderTargetList.get(index).renderTarget;
                        offscreenRenderTargetList.get(findPos).isUsed = true;
                        offscreenRenderTargetList.get(index).renderTarget = renderTarget;
                        offscreenRenderTargetList.get(index).isUsed = false;

                        isFind = true;
                        break;
                    }
                }

                if (!isFind) {
                    // 空いている場所が見つからなかったら現状のサイズでリサイズする
                    resize = i;
                    break;
                }
            }

            // 末尾側は不要なので破棄
            offscreenRenderTargetList.get(index).renderTarget.destroyRenderTarget();
            offscreenRenderTargetList.get(index).renderTarget = null;
        }

        // Listのサイズを縮小する（破棄済みの末尾側から削除）
        while (offscreenRenderTargetList.size() > resize) {
            offscreenRenderTargetList.remove(offscreenRenderTargetList.size() - 1);
        }
    }

    /**
     * Returns the maximum number of active render targets in the previous frame.
     *
     * @return the maximum number of active render targets in the previous frame
     */
    public int getPreviousActiveRenderTextureCount() {
        return previousActiveRenderTextureMaxCount;
    }

    /**
     * Returns the current number of active render targets.
     *
     * @return the current number of active render targets
     */
    public int getCurrentActiveRenderTextureCount() {
        return currentActiveRenderTextureCount;
    }

    /**
     * Constructor.
     */
    protected ACubismOffscreenManager() {
        this.previousActiveRenderTextureMaxCount = 0;
        this.currentActiveRenderTextureCount = 0;
        this.hasResetThisFrame = false;
    }

    /**
     * Updates the maximum number of render targets.
     */
    protected void updateRenderTargetCount() {
        currentActiveRenderTextureCount++;

        // 最大数更新
        previousActiveRenderTextureMaxCount = Math.max(currentActiveRenderTextureCount, previousActiveRenderTextureMaxCount);
    }

    /**
     * Returns an unused render target.
     *
     * @return an unused render target, or null if none available
     */
    protected T getUnusedOffscreenRenderTarget() {
        for (int i = 0; i < offscreenRenderTargetList.size(); i++) {
            if (!offscreenRenderTargetList.get(i).isUsed) {
                offscreenRenderTargetList.get(i).isUsed = true;
                return offscreenRenderTargetList.get(i).renderTarget;
            }
        }

        return null;
    }

    /**
     * Creates a render target.
     *
     * @return the render target
     */
    protected T createOffscreenRenderTarget() {
        offscreenRenderTargetList.add(new CubismRenderTargetContainer<T>());
        offscreenRenderTargetList.get(offscreenRenderTargetList.size() - 1).renderTarget = createRenderTargetInstanceInternal();
        return offscreenRenderTargetList.get(offscreenRenderTargetList.size() - 1).renderTarget;
    }

    /**
     * Creates a new instance of the render target.
     * <p>
     * This method exists because Java generics do not support direct instantiation of type parameters
     * (i.e., {@code new T()} is not allowed). Subclasses must implement this method to return an
     * instance of the platform-specific render target type.
     * <p>
     * NOTE:
     *  This method is intended for internal use only by {@link #createOffscreenRenderTarget()}.
     *  Do not call this method directly from outside this class hierarchy.
     *
     * @return new render target instance
     */
    protected abstract T createRenderTargetInstanceInternal();

    /**
     * Container structure for offscreen render targets.
     */
    private static class CubismRenderTargetContainer<T extends ICubismRenderTarget> {
        /**
         * Constructor.
         */
        public CubismRenderTargetContainer() {
            renderTarget = null;
            isUsed = true;
        }

        /**
         * Render target instance.
         */
        private T renderTarget;

        /**
         * Whether it is in use.
         */
        private boolean isUsed;
    }

    /**
     * Container list for offscreen drawing render targets.
     */
    private final List<CubismRenderTargetContainer<T>> offscreenRenderTargetList = new ArrayList<>();

    /**
     * Maximum number of active render targets in the previous frame.
     */
    private int previousActiveRenderTextureMaxCount;

    /**
     * Current number of active render targets.
     */
    private int currentActiveRenderTextureCount;

    /**
     * Whether it has been reset this frame.
     */
    private boolean hasResetThisFrame;
}
