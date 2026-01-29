/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android;

import com.live2d.sdk.cubism.framework.rendering.ACubismOffscreenRenderTarget;

/**
 * A class that manages render targets for offscreen rendering.
 */
public class CubismOffscreenRenderTargetAndroid extends ACubismOffscreenRenderTarget<CubismOffscreenRenderTargetAndroid, CubismRenderTargetAndroid> {
    /**
     * Sets the render target for offscreen drawing.
     *
     * @param width  width
     * @param height height
     */
    public void setOffscreenRenderTarget(int width, int height) {
        if (getUsingRenderTextureState()) {
            // 使用中の場合はサイズだけ確認
            if (!renderTarget.isSameSize(width, height)) {
                renderTarget.createRenderTarget(width, height);
            }
            return;
        }

        renderTarget = CubismOffscreenManagerAndroid.getInstance().getOffscreenRenderTarget(width, height);
    }

    /**
     * Returns the usage state of the render target.
     *
     * @return true if in use, false if unused
     */
    public boolean getUsingRenderTextureState() {
        return CubismOffscreenManagerAndroid.getInstance().getUsingRenderTextureState(renderTarget);
    }

    /**
     * Stops using the render target for offscreen drawing.
     */
    public void stopUsingRenderTexture() {
        CubismOffscreenManagerAndroid.getInstance().stopUsingRenderTexture(renderTarget);
        renderTarget = null;
    }
}
