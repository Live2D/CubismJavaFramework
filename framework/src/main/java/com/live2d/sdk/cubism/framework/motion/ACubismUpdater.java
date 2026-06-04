/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.model.CubismModel;

import java.util.Comparator;

/**
 * Abstract class for Updaters driven by {@link CubismUpdateScheduler}.
 * Subclasses override {@link #onLateUpdate(CubismModel, float)} to apply their update.
 * The executionOrder is set at construction.
 */
public abstract class ACubismUpdater {
    /**
     * Comparator that orders ACubismUpdater instances by executionOrder (ascending).
     */
    public static final Comparator<ACubismUpdater> COMPARATOR = new Comparator<ACubismUpdater>() {
        @Override
        public int compare(ACubismUpdater left, ACubismUpdater right) {
            return Integer.compare(left.executionOrder, right.executionOrder);
        }
    };

    /**
     * Update process.
     *
     * @param model            the target model
     * @param deltaTimeSeconds delta time[s]
     */
    public abstract void onLateUpdate(CubismModel model, float deltaTimeSeconds);

    /**
     * Constructor that uses {@link CubismUpdateOrder#MAX} as the execution order.
     */
    public ACubismUpdater() {
        this(CubismUpdateOrder.MAX);
    }

    /**
     * Constructor with a predefined execution order from {@link CubismUpdateOrder}.
     *
     * @param executionOrder one of {@link CubismUpdateOrder}
     */
    public ACubismUpdater(CubismUpdateOrder executionOrder) {
        this(executionOrder.order);
    }

    /**
     * Constructor with an arbitrary execution order (for values not defined in {@link CubismUpdateOrder}).
     *
     * @param executionOrder the execution order
     */
    public ACubismUpdater(int executionOrder) {
        this.executionOrder = executionOrder;
    }

    private final int executionOrder;
}
