/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.effect.CubismPose;
import com.live2d.sdk.cubism.framework.model.CubismModel;

/**
 * Updater for pose motion.
 * Registered with {@link CubismUpdateScheduler} and executed in executionOrder.
 */
public class CubismPoseUpdater extends ACubismUpdater {
    /**
     * Constructor with the default execution order ({@link CubismUpdateOrder#POSE}).
     *
     * @param pose CubismPose instance
     */
    public CubismPoseUpdater(CubismPose pose) {
        super(CubismUpdateOrder.POSE);
        this.pose = pose;
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param pose           CubismPose instance
     * @param executionOrder the execution order
     */
    public CubismPoseUpdater(CubismPose pose, int executionOrder) {
        super(executionOrder);
        this.pose = pose;
    }

    @Override
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (model == null) {
            return;
        }
        pose.updateParameters(model, deltaTimeSeconds);
    }

    private final CubismPose pose;
}
