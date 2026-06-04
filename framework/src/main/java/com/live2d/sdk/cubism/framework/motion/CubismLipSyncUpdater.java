/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.id.CubismId;
import com.live2d.sdk.cubism.framework.model.CubismModel;

import java.util.List;

/**
 * Updater for lip sync motion.
 * Registered with {@link CubismUpdateScheduler} and executed in executionOrder.
 */
public class CubismLipSyncUpdater extends ACubismUpdater {
    /**
     * Constructor with the default execution order ({@link CubismUpdateOrder#LIP_SYNC}).
     *
     * @param lipSyncIds     parameter IDs targeted by lip sync
     * @param wavFileHandler IParameterProvider that supplies the lip sync value
     */
    public CubismLipSyncUpdater(List<CubismId> lipSyncIds, IParameterProvider wavFileHandler) {
        super(CubismUpdateOrder.LIP_SYNC);
        this.lipSyncIds = lipSyncIds;
        this.wavFileHandler = wavFileHandler;
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param lipSyncIds     parameter IDs targeted by lip sync
     * @param wavFileHandler IParameterProvider that supplies the lip sync value
     * @param executionOrder the execution order
     */
    public CubismLipSyncUpdater(List<CubismId> lipSyncIds, IParameterProvider wavFileHandler, int executionOrder) {
        super(executionOrder);
        this.lipSyncIds = lipSyncIds;
        this.wavFileHandler = wavFileHandler;
    }

    @Override
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (model == null) {
            return;
        }

        // For real-time lip sync, the wavFileHandler returns the system volume in the [0, 1] range,
        // which is then applied to the lip sync parameters below.
        wavFileHandler.update(deltaTimeSeconds);
        float value = wavFileHandler.getParameter();

        for (int i = 0; i < lipSyncIds.size(); i++) {
            model.addParameterValue(lipSyncIds.get(i), value, 0.8f);
        }
    }

    private final IParameterProvider wavFileHandler;
    private final List<CubismId> lipSyncIds;
}
