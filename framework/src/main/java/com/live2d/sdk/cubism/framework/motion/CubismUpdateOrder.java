/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

/**
 * Default execution order values for {@link ACubismUpdater} instances.
 * <p>
 * Smaller values are executed first by {@link CubismUpdateScheduler}.
 * Users can pass arbitrary int values to {@link ACubismUpdater} to customize the order.
 */
public enum CubismUpdateOrder {
    EYE_BLINK(200),
    EXPRESSION(300),
    LOOK(400),
    BREATH(500),
    PHYSICS(600),
    LIP_SYNC(700),
    POSE(800),
    MAX(Integer.MAX_VALUE);

    public final int order;

    CubismUpdateOrder(int order) {
        this.order = order;
    }
}
