/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering;

import com.live2d.sdk.cubism.core.CubismAlphaBlendType;
import com.live2d.sdk.cubism.core.CubismColorBlendType;
import com.live2d.sdk.cubism.framework.rendering.android.shaderindex.CubismShaderIndexFactors;

/**
 * A class that defines blend modes.
 */
public class csmBlendMode {
    /**
     * Constructor.
     */
    public csmBlendMode() {
        this.colorBlendType = 0;  // NORMAL
        this.alphaBlendType = 0;  // OVER
    }

    /**
     * Set the blend mode.
     *
     * @param blendMode value of blendMode
     */
    public void setBlendMode(int blendMode) {
        this.colorBlendType = blendMode & 0xFF;
        this.alphaBlendType = (blendMode >> 8) & 0xFF;
    }

    /**
     * Set the blend mode.
     *
     * @param colorBlendType color blend type
     * @param alphaBlendType alpha blend type
     */
    public void setBlendMode(CubismColorBlendType colorBlendType, CubismAlphaBlendType alphaBlendType) {
        this.colorBlendType = colorBlendType.getNumber();
        this.alphaBlendType = alphaBlendType.getNumber();
    }

    /**
     * Get the color blend type.
     *
     * @return Returns the color blend type.
     */
    public CubismColorBlendType getColorBlendType() {
        return intToColorBlendType(colorBlendType);
    }

    /**
     * Get the alpha blend type.
     *
     * @return Returns the alpha blend type.
     */
    public CubismAlphaBlendType getAlphaBlendType() {
        return intToAlphaBlendType(alphaBlendType);
    }

    /**
     * Determines whether this is a Pre-5.3 blend mode.
     *
     * @return true if it is a Pre-5.3 blend mode, false otherwise.
     */
    public boolean isBlendMode() {
        CubismColorBlendType colorBlendType = getColorBlendType();
        CubismAlphaBlendType alphaBlendType = getAlphaBlendType();

        boolean isNormalOver = colorBlendType == CubismColorBlendType.NORMAL
                               && alphaBlendType == CubismAlphaBlendType.OVER;
        boolean isCompatible = colorBlendType == CubismColorBlendType.ADD_COMPATIBLE
                               || colorBlendType == CubismColorBlendType.MULTIPLY_COMPATIBLE;

        // 5.3以降の高度なブレンドモードを使用しているかどうかを判定する。
        // Normal+Over、または5.2以前の互換モード（加算・乗算）の場合はfalseを返す。
        return !(isNormalOver || isCompatible);
    }

    // Cache enum values to avoid allocation
    private static final CubismColorBlendType[] CACHED_COLOR_BLEND_TYPES = CubismColorBlendType.values();
    private static final CubismAlphaBlendType[] CACHED_ALPHA_BLEND_TYPES = CubismAlphaBlendType.values();

    /**
     * Convert int value to CubismColorBlendType enum.
     *
     * @param value int value
     * @return CubismColorBlendType enum value
     */
    private static CubismColorBlendType intToColorBlendType(int value) {
        for (int i = 0; i < CACHED_COLOR_BLEND_TYPES.length; i++) {
            CubismColorBlendType type = CACHED_COLOR_BLEND_TYPES[i];

            if (type.getNumber() == value) {
                return type;
            }
        }

        return CubismColorBlendType.NORMAL;
    }

    /**
     * Convert int value to CubismAlphaBlendType enum.
     *
     * @param value int value
     * @return CubismAlphaBlendType enum value
     */
    private static CubismAlphaBlendType intToAlphaBlendType(int value) {
        for (int i = 0; i < CACHED_ALPHA_BLEND_TYPES.length; i++) {
            CubismAlphaBlendType type = CACHED_ALPHA_BLEND_TYPES[i];

            if (type.getNumber() == value) {
                return type;
            }
        }

        return CubismAlphaBlendType.OVER;
    }

    private int colorBlendType;
    private int alphaBlendType;
}
