/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android.shaderindex;

import com.live2d.sdk.cubism.core.CubismAlphaBlendType;
import com.live2d.sdk.cubism.core.CubismColorBlendType;

/**
 * Constants for shader index calculation in the Cubism rendering system.
 * For internal use by the Cubism SDK.
 */
public class CubismShaderIndexConstants {
    // ========================================
    // Public constants (no dependencies on private constants)
    // ========================================

    /**
     * The count of mask type.
     */
    public static final int MASK_TYPE_COUNT = CubismShaderIndexFactors.MaskType.values().length;

    /**
     * The count of color blend modes.
     * <p>
     * This excludes the Cubism 5.2 and earlier compatible modes ({@code ADD_COMPATIBLE} and {@code MULTIPLY_COMPATIBLE}).
     */
    public static final int COLOR_BLEND_COUNT = CubismShaderIndexFactors.ColorBlendMode.values().length;

    /**
     * The count of alpha blend modes.
     */
    public static final int ALPHA_BLEND_COUNT = CubismShaderIndexFactors.AlphaBlendMode.values().length;

    // ========================================
    // Private constants (for internal calculations)
    // ========================================

    /**
     * The count of utility shader. (Copy + SetupMask)
     */
    private static final int UTILITY_SHADER_COUNT = CubismShaderIndexFactors.UtilityShaderType.values().length;

    // ========================================
    // Public constants (with dependencies on private constants)
    // ========================================

    /**
     * Base index for Normal + Over blend mode.
     */
    public static final int NORMAL_OVER_BASE_INDEX = UTILITY_SHADER_COUNT;

    /**
     * Base index for Add(Compatible).
     */
    public static final int ADD_COMPATIBLE_BASE_INDEX = NORMAL_OVER_BASE_INDEX + MASK_TYPE_COUNT;

    /**
     * Base index for Multiply(Compatible).
     */
    public static final int MULTIPLY_COMPATIBLE_BASE_INDEX = NORMAL_OVER_BASE_INDEX + MASK_TYPE_COUNT * 2;

    /**
     * Start index for Cubism 5.3+ advanced blend mode shaders.
     */
    public static final int BLEND_MODE_START_INDEX = UTILITY_SHADER_COUNT + MASK_TYPE_COUNT * 3;

    /**
     * Total shader count.
     * <p>
     * Blend mode combinations = Add (Compatible) + Multiply (Compatible)
     * + (Normal + Add + Add (Glow) + Darken + Multiply + Color Burn + Linear Burn + Lighten + Screen + Color Dodge + Overlay + Soft Light + Hard Light + Linear Light + Hue + Color)
     * * (Over + Atop + Out + Conjoint Over + Disjoint Over)
     *
     * <p>
     * Number of shaders = Copy + SetupMask
     * + (Normal + Add + Multiply + blend mode combinations)
     * * (No mask + Mask + Inverted mask + No mask with premultiplied alpha + Mask with premultiplied alpha + Inverted mask with premultiplied alpha)
     */
    public static final int SHADER_COUNT = calculateTotalShaderCount();

    // ========================================
    // Private methods
    // ========================================

    /**
     * Calculates the total shader count.
     *
     * @return total shader count
     */
    private static int calculateTotalShaderCount() {
        int total = UTILITY_SHADER_COUNT;
        total += 3 * MASK_TYPE_COUNT;   // 5.2以前のブレンドモードの数 * マスクの種類

        // NORMALのOVER以外のアルファブレンド（ATOP, OUT, CONJOINT_OVER, DISJOINT_OVER)についてマスクの種類との組み合わせを計算
        total += (ALPHA_BLEND_COUNT - 1) * MASK_TYPE_COUNT;

        // NORMAL以外のブレンドモード × 全アルファモード × マスクの種類
        int colorBlendModeCount = COLOR_BLEND_COUNT - 1;    // NORMALを除外
        total += colorBlendModeCount * ALPHA_BLEND_COUNT * MASK_TYPE_COUNT;

        return total;
    }
}
