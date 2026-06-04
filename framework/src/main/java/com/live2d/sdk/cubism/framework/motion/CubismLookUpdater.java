/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.effect.CubismLook;
import com.live2d.sdk.cubism.framework.math.CubismTargetPoint;
import com.live2d.sdk.cubism.framework.model.CubismModel;

/**
 * Updater for drag-driven look motion.
 * Registered with {@link CubismUpdateScheduler} and executed in executionOrder.
 * <p>
 * Note:
 *  this updater drives the dragManager smoothing via {@code dragManager.update}.
 *  If omitted, drag smoothing will not run.
 */
public class CubismLookUpdater extends ACubismUpdater {
    /**
     * Constructor with the default execution order ({@link CubismUpdateOrder#LOOK}).
     *
     * @param look        CubismLook instance
     * @param dragManager CubismTargetPoint instance
     */
    public CubismLookUpdater(CubismLook look, CubismTargetPoint dragManager) {
        super(CubismUpdateOrder.LOOK);
        this.look = look;
        this.dragManager = dragManager;
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param look           CubismLook instance
     * @param dragManager    CubismTargetPoint instance
     * @param executionOrder the execution order
     */
    public CubismLookUpdater(CubismLook look, CubismTargetPoint dragManager, int executionOrder) {
        super(executionOrder);
        this.look = look;
        this.dragManager = dragManager;
    }

    @Override
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (model == null) {
            return;
        }

        dragManager.update(deltaTimeSeconds);
        final float dragX = dragManager.getX();
        final float dragY = dragManager.getY();

        look.updateParameters(model, dragX, dragY);
    }

    private final CubismLook look;
    private final CubismTargetPoint dragManager;
}
