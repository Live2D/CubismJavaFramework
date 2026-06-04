/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.effect.CubismEyeBlink;
import com.live2d.sdk.cubism.framework.model.CubismModel;

/**
 * Updater for eye blink motion.
 * Registered with {@link CubismUpdateScheduler} and executed in executionOrder.
 */
public class CubismEyeBlinkUpdater extends ACubismUpdater {
    /**
     * Constructor with the default execution order ({@link CubismUpdateOrder#EYE_BLINK}).
     *
     * @param motionUpdated supplier that returns the latest motion update flag
     * @param eyeBlink      CubismEyeBlink instance
     */
    public CubismEyeBlinkUpdater(IBooleanSupplier motionUpdated, CubismEyeBlink eyeBlink) {
        super(CubismUpdateOrder.EYE_BLINK);
        this.motionUpdated = motionUpdated;
        this.eyeBlink = eyeBlink;
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param motionUpdated  supplier that returns the latest motion update flag
     * @param eyeBlink       CubismEyeBlink instance
     * @param executionOrder the execution order
     */
    public CubismEyeBlinkUpdater(IBooleanSupplier motionUpdated, CubismEyeBlink eyeBlink, int executionOrder) {
        super(executionOrder);
        this.motionUpdated = motionUpdated;
        this.eyeBlink = eyeBlink;
    }

    @Override
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (model == null) {
            return;
        }

        if (!motionUpdated.getAsBoolean()) {
            // Skip eye blink update when the main motion did not update parameters.
            eyeBlink.updateParameters(model, deltaTimeSeconds);
        }
    }

    /**
     * Supplier of the owner's motion-updated flag.
     * Eye blink runs only when the main motion did not update parameters this frame.
     */
    private final IBooleanSupplier motionUpdated;
    private final CubismEyeBlink eyeBlink;
}
