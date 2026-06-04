/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

/**
 * Functional interface that returns a boolean on each call.
 * Defined here because the framework targets Java 7 (Java 8's
 * {@code java.util.function.BooleanSupplier} is unavailable).
 *
 * <p>Provides on-demand access to a specific boolean state. For example:
 * <pre>{@code
 * updateScheduler.addUpdatableList(new CubismEyeBlinkUpdater(new IBooleanSupplier() {
 *     public boolean getAsBoolean() {
 *         return motionUpdated;
 *     }
 * }, eyeBlink));
 * }</pre>
 */
@FunctionalInterface
public interface IBooleanSupplier {
    /**
     * Returns the latest boolean value.
     *
     * @return the current boolean
     */
    boolean getAsBoolean();
}
