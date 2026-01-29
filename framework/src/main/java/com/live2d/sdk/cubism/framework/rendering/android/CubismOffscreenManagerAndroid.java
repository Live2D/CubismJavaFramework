/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android;

import com.live2d.sdk.cubism.framework.rendering.ACubismOffscreenManager;

/**
 * A class that manages render targets for offscreen rendering.
 */
public class CubismOffscreenManagerAndroid extends ACubismOffscreenManager<CubismRenderTargetAndroid> {
    /**
     * Return the singleton instance of the class.
     * If the instance has not been created, create it internally.
     *
     * @return the class instance
     */
    public static CubismOffscreenManagerAndroid getInstance() {
        if (s_instance == null) {
            s_instance = new CubismOffscreenManagerAndroid();
        }
        return s_instance;
    }

    /**
     * Releases the singleton instance of the class.
     */
    public static void releaseInstance() {
        if (s_instance != null) {
            s_instance.dispose();
            s_instance = null;
        }
    }

    /**
     * Returns the available render target.
     *
     * @param width  width
     * @param height height
     * @return available render target
     */
    public CubismRenderTargetAndroid getOffscreenRenderTarget(int width, int height) {
        // 使用数を更新
        updateRenderTargetCount();

        // 使われていないリソースコンテナがあればそれを返す
        CubismRenderTargetAndroid offscreenRenderTarget = getUnusedOffscreenRenderTarget();
        if (offscreenRenderTarget != null) {
            // サイズが違う場合は再作成する
            if (!offscreenRenderTarget.isSameSize(width, height)) {
                offscreenRenderTarget.createRenderTarget(width, height);
            }
            // 既存の未使用レンダーターゲットを返す
            return offscreenRenderTarget;
        }

        // 新規にレンダーターゲットを作成して登録する
        offscreenRenderTarget = createOffscreenRenderTarget();
        offscreenRenderTarget.createRenderTarget(width, height);
        return offscreenRenderTarget;
    }

    @Override
    protected CubismRenderTargetAndroid createRenderTargetInstanceInternal() {
        return new CubismRenderTargetAndroid();
    }

    /**
     * Singleton instance.
     */
    private static CubismOffscreenManagerAndroid s_instance;

    /**
     * Private constructor.
     */
    private CubismOffscreenManagerAndroid() {}
}
