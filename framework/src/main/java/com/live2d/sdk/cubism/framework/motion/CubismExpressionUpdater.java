/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.model.CubismModel;

/**
 * Updater for expression motion.
 * Registered with {@link CubismUpdateScheduler} and executed in executionOrder.
 */
public class CubismExpressionUpdater extends ACubismUpdater {
    /**
     * Constructor with the default execution order ({@link CubismUpdateOrder#EXPRESSION}).
     *
     * @param expressionManager CubismExpressionMotionManager instance
     */
    public CubismExpressionUpdater(CubismExpressionMotionManager expressionManager) {
        super(CubismUpdateOrder.EXPRESSION);
        this.expressionManager = expressionManager;
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param expressionManager CubismExpressionMotionManager instance
     * @param executionOrder    the execution order
     */
    public CubismExpressionUpdater(CubismExpressionMotionManager expressionManager, int executionOrder) {
        super(executionOrder);
        this.expressionManager = expressionManager;
    }

    @Override
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (model == null) {
            return;
        }
        // Update parameters by expression (relative change).
        expressionManager.updateMotion(model, deltaTimeSeconds);
    }

    private final CubismExpressionMotionManager expressionManager;
}
