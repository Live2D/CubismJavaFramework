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
 * Utility class for calculating shader indices based on blending, masking, and other rendering factors.
 */
public class CubismShaderIndexCalculator {
    /**
     * Calculates the shader index based on the provided blend mode, masking state, and alpha premultiplication settings.
     * This index indicates the offset position in the shader information list constructed based on color blending, alpha blending, and mask types.
     *
     * @param blendMode            blend mode containing both ColorBlendType and AlphaBlendType settings
     * @param maskState            mask state (no mask, normal mask, inverted mask)
     * @param isPremultipliedAlpha whether the alpha values are premultiplied
     * @return calculated index of the shader in the shader array
     */
    public static int calculateShaderIndex(
        csmBlendMode blendMode,
        CubismShaderIndexFactors.MaskState maskState,
        boolean isPremultipliedAlpha
    ) {
        CubismShaderIndexFactors.MaskType maskType = determineMaskType(
            maskState,
            isPremultipliedAlpha
        );

        CubismColorBlendType colorType = blendMode.getColorBlendType();
        CubismAlphaBlendType alphaType = blendMode.getAlphaBlendType();

        // 5.3以降の高度なブレンドモード
        if (blendMode.isBlendMode()) {
            int baseIndex = CubismShaderIndexConstants.BLEND_MODE_START_INDEX;
            int maskCount = CubismShaderIndexConstants.MASK_TYPE_COUNT;
            int alphaCount = CubismShaderIndexConstants.ALPHA_BLEND_COUNT;

            if (colorType == CubismColorBlendType.NORMAL) {
                int alphaIndex = alphaType.getNumber() - 1; // OVERを除外
                return baseIndex + (alphaIndex * maskCount) + maskType.index;
            }

            int skippedNormalAlphaBlock = (alphaCount - 1) * maskCount;     // NORMALのOVER以外のアルファブレンド分をスキップするためのオフセット計算
            int colorOffset = (colorType.getNumber() - 3) * alphaCount * maskCount; // NORMAL, ADD_COMPATIBLE, MULTIPLY_COMPATIBLE を除外して0始まりに詰めるために-3する
            int alphaOffset = alphaType.getNumber() * maskCount;

            return baseIndex + skippedNormalAlphaBlock + colorOffset + alphaOffset + maskType.index;
        }
        // 5.2以前のブレンドモード
        else {
            switch (colorType) {
                case NORMAL:
                    return CubismShaderIndexConstants.NORMAL_OVER_BASE_INDEX + maskType.index;
                case ADD_COMPATIBLE:
                    return CubismShaderIndexConstants.ADD_COMPATIBLE_BASE_INDEX + maskType.index;
                case MULTIPLY_COMPATIBLE:
                    return CubismShaderIndexConstants.MULTIPLY_COMPATIBLE_BASE_INDEX + maskType.index;
                default:
                    throw new IllegalStateException("Unexpected color blend type: " + colorType);
            }
        }
    }

    /**
     * クリッピングマスク関連情報を基に{@link CubismShaderIndexFactors.MaskType}を決定して返す。
     *
     * @param maskState マスクの状態（マスクなし、通常マスク、反転マスク）
     * @param isPremultipliedAlpha 乗算済みアルファであるかどうか
     * @return 決定した{@link CubismShaderIndexFactors.MaskType}
     */
    private static CubismShaderIndexFactors.MaskType determineMaskType(
        CubismShaderIndexFactors.MaskState maskState,
        boolean isPremultipliedAlpha
    ) {
        if (isPremultipliedAlpha) {
            switch (maskState) {
                case NONE:
                    return CubismShaderIndexFactors.MaskType.PREMULTIPLIED_ALPHA;
                case NORMAL:
                    return CubismShaderIndexFactors.MaskType.MASKED_PREMULTIPLIED_ALPHA;
                case INVERTED:
                    return CubismShaderIndexFactors.MaskType.MASKED_INVERTED_PREMULTIPLIED_ALPHA;
                default:
                    throw new IllegalStateException("Unexpected mask state: " + maskState);
            }
        } else {
            switch (maskState) {
                case NONE:
                    return CubismShaderIndexFactors.MaskType.NONE;
                case NORMAL:
                    return CubismShaderIndexFactors.MaskType.MASKED;
                case INVERTED:
                    return CubismShaderIndexFactors.MaskType.MASKED_INVERTED;
                default:
                    throw new IllegalStateException("Unexpected mask state: " + maskState);
            }
        }
    }
}
