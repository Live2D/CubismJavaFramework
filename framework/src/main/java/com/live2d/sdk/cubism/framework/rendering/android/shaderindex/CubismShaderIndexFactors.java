/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android.shaderindex;

import com.live2d.sdk.cubism.core.CubismAlphaBlendType;
import com.live2d.sdk.cubism.core.CubismColorBlendType;
import com.live2d.sdk.cubism.framework.rendering.csmBlendMode;

/**
 * Definitions of factors used for shader index calculation.
 */
public class CubismShaderIndexFactors {
    /**
     * Utility shader type enumeration for setup and utility operations.
     */
    public enum UtilityShaderType {
        COPY(0),
        SETUP_MASK(1);

        public final int index;

        UtilityShaderType(int index) {
            this.index = index;
        }
    }


    /**
     * Mask state enumeration.
     */
    public enum MaskState {
        /**
         * No clipping mask is applied.
         */
        NONE,
        /**
         * Clipping mask is applied.
         */
        NORMAL,
        /**
         * Inverted clipping mask is applied.
         */
        INVERTED
    }

    /**
     * Mask type enumeration.
     */
    public enum MaskType {
        NONE(0),
        MASKED(1),
        MASKED_INVERTED(2),
        PREMULTIPLIED_ALPHA(3),
        MASKED_PREMULTIPLIED_ALPHA(4),
        MASKED_INVERTED_PREMULTIPLIED_ALPHA(5);

        public final int index;

        MaskType(int index) {
            this.index = index;
        }
    }

    /**
     * Color blend mode enumeration.
     */
    public enum ColorBlendMode {
        NORMAL(0),
        ADD(1),
        ADD_GLOW(2),
        DARKEN(3),
        MULTIPLY(4),
        COLOR_BURN(5),
        LINEAR_BURN(6),
        LIGHTEN(7),
        SCREEN(8),
        COLOR_DODGE(9),
        OVERLAY(10),
        SOFT_LIGHT(11),
        HARD_LIGHT(12),
        LINEAR_LIGHT(13),
        HUE(14),
        COLOR(15);

        public final int offset;

        ColorBlendMode(int offset) {
            this.offset = offset;
        }
    }

    /**
     * Alpha blend mode enumeration.
     */
    public enum AlphaBlendMode {
        OVER(0),
        ATOP(1),
        OUT(2),
        CONJOINT_OVER(3),
        DISJOINT_OVER(4);

        public final int offset;

        AlphaBlendMode(int offset) {
            this.offset = offset;
        }
    }
}
