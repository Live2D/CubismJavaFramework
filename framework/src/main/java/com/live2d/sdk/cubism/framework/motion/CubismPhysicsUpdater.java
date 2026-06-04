/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.model.CubismModel;
import com.live2d.sdk.cubism.framework.physics.CubismPhysics;

/**
 * Updater for physics motion.
 * Registered with {@link CubismUpdateScheduler} and executed in executionOrder.
 */
public class CubismPhysicsUpdater extends ACubismUpdater {
    /**
     * Constructor with the default execution order ({@link CubismUpdateOrder#PHYSICS}).
     *
     * @param physics CubismPhysics instance
     */
    public CubismPhysicsUpdater(CubismPhysics physics) {
        super(CubismUpdateOrder.PHYSICS);
        this.physics = physics;
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param physics        CubismPhysics instance
     * @param executionOrder the execution order
     */
    public CubismPhysicsUpdater(CubismPhysics physics, int executionOrder) {
        super(executionOrder);
        this.physics = physics;
    }

    @Override
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (model == null) {
            return;
        }
        physics.evaluate(model, deltaTimeSeconds);
    }

    private final CubismPhysics physics;
}
