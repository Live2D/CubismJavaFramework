/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

/**
 * Interface for providing parameter values.
 * Defines the interface for classes that supply parameter values to the model.
 */
public interface IParameterProvider {
    /**
     * Update process.
     *
     * @return true if the update is successful
     */
    boolean update();

    /**
     * Update process.
     *
     * @param deltaTimeSeconds delta time[s]
     * @return true if the update is successful
     */
    boolean update(float deltaTimeSeconds);

    /**
     * Retrieves the current value of the parameter.
     *
     * @return the parameter value as a float
     */
    float getParameter();
}
