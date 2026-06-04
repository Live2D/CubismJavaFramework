/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.effect.CubismBreath;
import com.live2d.sdk.cubism.framework.model.CubismModel;

/**
 * Updater for breathing motion.
 * Registered with {@link CubismUpdateScheduler} and executed in executionOrder.
 */
public class CubismBreathUpdater extends ACubismUpdater {
    /**
     * Constructor with the default execution order ({@link CubismUpdateOrder#BREATH}).
     *
     * @param breath CubismBreath instance
     */
    public CubismBreathUpdater(CubismBreath breath) {
        super(CubismUpdateOrder.BREATH);
        this.breath = breath;
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param breath         CubismBreath instance
     * @param executionOrder the execution order
     */
    public CubismBreathUpdater(CubismBreath breath, int executionOrder) {
        super(executionOrder);
        this.breath = breath;
    }

    @Override
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (model == null) {
            return;
        }
        breath.updateParameters(model, deltaTimeSeconds);
    }

    private final CubismBreath breath;
}
