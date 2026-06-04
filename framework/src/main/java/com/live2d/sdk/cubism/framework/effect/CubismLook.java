/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.effect;

import com.live2d.sdk.cubism.framework.id.CubismId;
import com.live2d.sdk.cubism.framework.model.CubismModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Parameter following feature by target.
 * Provides parameter following functionality for drag input.
 */
public class CubismLook {
    /**
     * Data attached to a look parameter.
     */
    public static class LookParameterData {
        /**
         * Default constructor.
         */
        public LookParameterData() {
            this(null, 0.0f, 0.0f, 0.0f);
        }

        /**
         * Constructor (factorX/Y/XY default to 0.0).
         *
         * @param parameterId ID of the parameter to attach
         */
        public LookParameterData(CubismId parameterId) {
            this(parameterId, 0.0f, 0.0f, 0.0f);
        }

        /**
         * Constructor (factorY/XY default to 0.0).
         *
         * @param parameterId ID of the parameter to attach
         * @param factorX     coefficient for drag input along the X-axis
         */
        public LookParameterData(CubismId parameterId, final float factorX) {
            this(parameterId, factorX, 0.0f, 0.0f);
        }

        /**
         * Constructor (factorXY defaults to 0.0).
         *
         * @param parameterId ID of the parameter to attach
         * @param factorX     coefficient for drag input along the X-axis
         * @param factorY     coefficient for drag input along the Y-axis
         */
        public LookParameterData(CubismId parameterId, final float factorX, final float factorY) {
            this(parameterId, factorX, factorY, 0.0f);
        }

        /**
         * Constructor.
         *
         * @param parameterId ID of the parameter to attach
         * @param factorX     coefficient for drag input along the X-axis
         * @param factorY     coefficient for drag input along the Y-axis
         * @param factorXY    coefficient for the combined X-Y (cross) drag input
         */
        public LookParameterData(CubismId parameterId, final float factorX, final float factorY, final float factorXY) {
            this.parameterId = parameterId;
            this.factorX = factorX;
            this.factorY = factorY;
            this.factorXY = factorXY;
        }

        /**
         * ID of the parameter to attach.
         */
        public CubismId parameterId;
        /**
         * Coefficient for drag input along the X-axis.
         */
        public float factorX;
        /**
         * Coefficient for drag input along the Y-axis.
         */
        public float factorY;
        /**
         * Coefficient for the combined X-Y (cross) drag input.
         */
        public float factorXY;
    }

    /**
     * Creates a {@code CubismLook} instance.
     *
     * @return a new {@code CubismLook} instance
     */
    public static CubismLook create() {
        return new CubismLook();
    }

    /**
     * Attaches the look parameters.
     *
     * @param lookParameters list of look parameters to attach
     */
    public void setParameters(final List<LookParameterData> lookParameters) {
        this.lookParameters = lookParameters;
    }

    /**
     * Returns the parameters attached to look.
     *
     * @return list of attached look parameters
     */
    public List<LookParameterData> getParameters() {
        return lookParameters;
    }

    /**
     * Updates model parameters based on the target drag input.
     *
     * <p><b>Note:</b> Call after creating an instance with {@link #create()} and binding parameters with {@link #setParameters(List)}.
     *
     * @param model the target model
     * @param dragX X coordinate of the target
     * @param dragY Y coordinate of the target
     */
    public void updateParameters(CubismModel model, final float dragX, final float dragY) {
        final float dragXY = dragX * dragY;

        for (int i = 0; i < lookParameters.size(); i++) {
            final LookParameterData data = lookParameters.get(i);
            model.addParameterValue(
                data.parameterId,
                data.factorX * dragX + data.factorY * dragY + data.factorXY * dragXY
            );
        }
    }

    private CubismLook() {}

    private List<LookParameterData> lookParameters = new ArrayList<>();
}
