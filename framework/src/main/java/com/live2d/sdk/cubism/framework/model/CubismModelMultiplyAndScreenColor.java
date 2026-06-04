/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.model;

import com.live2d.sdk.cubism.framework.rendering.CubismRenderer;
import com.live2d.sdk.cubism.framework.utils.CubismDebug;

import java.util.ArrayList;
import java.util.List;

/**
 * Handling multiply and screen colors of the model.
 */
public class CubismModelMultiplyAndScreenColor {
    /**
     * Structure for color information of drawing object.
     */
    public static class ColorData {
        /**
         * Constructor.
         */
        public ColorData() {
            this.isOverridden = false;
            this.color = new CubismRenderer.CubismTextureColor();
        }

        /**
         * Constructor.
         *
         * @param isOverridden whether to be overridden
         * @param color        texture color
         */
        public ColorData(boolean isOverridden, CubismRenderer.CubismTextureColor color) {
            this.isOverridden = isOverridden;
            this.color = new CubismRenderer.CubismTextureColor(color);
        }

        /**
         * Copy constructor.
         *
         * @param other the ColorData object to copy from
         */
        public ColorData(ColorData other) {
            this.isOverridden = other.isOverridden;
            this.color = new CubismRenderer.CubismTextureColor(
                other.color.r,
                other.color.g,
                other.color.b,
                other.color.a
            );
        }

        /**
         * Whether to be overridden.
         */
        public boolean isOverridden;

        /**
         * Color.
         */
        public CubismRenderer.CubismTextureColor color;
    }

    /**
     * Constructor.
     *
     * @param model          cubism model
     * @param partsHierarchy parts hierarchy
     */
    public CubismModelMultiplyAndScreenColor(CubismModel model, List<CubismModelPartInfo> partsHierarchy) {
        this.model = model;
        this.partsHierarchy = partsHierarchy;
    }

    /**
     * Initializes user color data for multiply and screen colors.
     *
     * @param partCount      number of parts
     * @param drawableCount  number of drawables
     * @param offscreenCount number of offscreen
     */
    public void initialize(int partCount, int drawableCount, int offscreenCount) {
        // 乗算色
        ColorData userMultiplyColor = new ColorData(
            false,
            new CubismRenderer.CubismTextureColor(1.0f, 1.0f, 1.0f, 1.0f)
        );

        // スクリーン色
        ColorData userScreenColor = new ColorData(
            false,
            new CubismRenderer.CubismTextureColor(0.0f, 0.0f, 0.0f, 1.0f)
        );

        // Part
        for (int i = 0; i < partCount; i++) {
            userPartMultiplyColors.add(new ColorData(userMultiplyColor));
            userPartScreenColors.add(new ColorData(userScreenColor));
        }

        // Drawable
        for (int i = 0; i < drawableCount; i++) {
            userDrawableMultiplyColors.add(new ColorData(userMultiplyColor));
            userDrawableScreenColors.add(new ColorData(userScreenColor));
        }

        // Offscreen
        for (int i = 0; i < offscreenCount; i++) {
            userOffscreenMultiplyColors.add(new ColorData(userMultiplyColor));
            userOffscreenScreenColors.add(new ColorData(userScreenColor));

            // getOffscreenMultiplyColor() / getOffscreenScreenColor() で都度 new せずに済むよう事前確保しておく。
            offscreenMultiplyColorCache.add(new CubismRenderer.CubismTextureColor());
            offscreenScreenColorCache.add(new CubismRenderer.CubismTextureColor());
        }
    }

    /**
     * Sets the flag indicating whether the color set at runtime is used as the multiply color for the entire model during rendering.
     *
     * @param value true if the color set at runtime is to be used; otherwise false.
     */
    public void setMultiplyColorEnabled(boolean value) {
        isOverriddenModelMultiplyColors = value;
    }

    /**
     * Returns the flag indicating whether the color set at runtime is used as the multiply color for the entire model during rendering.
     *
     * @return true if the color set at runtime is used; otherwise false.
     */
    public boolean getMultiplyColorEnabled() {
        return isOverriddenModelMultiplyColors;
    }

    /**
     * Sets the flag indicating whether the color set at runtime is used as the screen color for the entire model during rendering.
     *
     * @param value true if the color set at runtime is to be used; otherwise false.
     */
    public void setScreenColorEnabled(boolean value) {
        isOverriddenModelScreenColors = value;
    }

    /**
     * Returns the flag indicating whether the color set at runtime is used as the screen color for the entire model during rendering.
     *
     * @return true if the color set at runtime is used; otherwise false.
     */
    public boolean getScreenColorEnabled() {
        return isOverriddenModelScreenColors;
    }

    /**
     * Sets whether the part multiply color is overridden by the SDK.
     * Use true to use the color information from the SDK, or false to use the color information from the model.
     *
     * @param partIndex part index
     * @param value     true to enable override, false to disable
     */
    public void setPartMultiplyColorEnabled(int partIndex, boolean value) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("setPartMultiplyColorEnabled", partIndex, model.getPartCount() - 1);
            return;
        }

        setPartColorEnabled(
            partIndex,
            value,
            userPartMultiplyColors,
            userDrawableMultiplyColors,
            userOffscreenMultiplyColors
        );
    }

    /**
     * Checks whether the part multiply color is overridden by the SDK.
     *
     * @param partIndex part index
     * @return true if the color information from the SDK is used; otherwise false.
     */
    public boolean getPartMultiplyColorEnabled(int partIndex) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("getPartMultiplyColorEnabled", partIndex, model.getPartCount() - 1);
            return false;
        }

        return userPartMultiplyColors.get(partIndex).isOverridden;
    }

    /**
     * Sets whether the part screen color is overridden by the SDK.
     * Use true to use the color information from the SDK, or false to use the color information from the model.
     *
     * @param partIndex part index
     * @param value     true to enable override, false to disable
     */
    public void setPartScreenColorEnabled(int partIndex, boolean value) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("setPartScreenColorEnabled", partIndex, model.getPartCount() - 1);
            return;
        }

        setPartColorEnabled(
            partIndex,
            value,
            userPartScreenColors,
            userDrawableScreenColors,
            userOffscreenScreenColors
        );
    }

    /**
     * Checks whether the part screen color is overridden by the SDK.
     *
     * @param partIndex part index
     * @return true if the color information from the SDK is used; otherwise false.
     */
    public boolean getPartScreenColorEnabled(int partIndex) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("getPartScreenColorEnabled", partIndex, model.getPartCount() - 1);
            return false;
        }

        return userPartScreenColors.get(partIndex).isOverridden;
    }

    /**
     * Sets the multiply color of the part.
     *
     * @param partIndex part index
     * @param color     multiply color to be set (CubismTextureColor)
     */
    public void setPartMultiplyColor(int partIndex, CubismRenderer.CubismTextureColor color) {
        setPartMultiplyColor(partIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Sets the multiply color of the part.
     *
     * @param partIndex part index
     * @param r         red value of the multiply color to be set
     * @param g         green value of the multiply color to be set
     * @param b         blue value of the multiply color to be set
     * @param a         alpha value of the multiply color to be set
     */
    public void setPartMultiplyColor(int partIndex, float r, float g, float b, float a) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("setPartMultiplyColor", partIndex, model.getPartCount() - 1);
            return;
        }

        setPartColor(
            partIndex,
            r, g, b, a,
            userPartMultiplyColors,
            userDrawableMultiplyColors,
            userOffscreenMultiplyColors
        );
    }

    /**
     * Returns the multiply color of the part.
     *
     * @param partIndex part index
     * @return multiply color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getPartMultiplyColor(int partIndex) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("getPartMultiplyColor", partIndex, model.getPartCount() - 1);
            return new CubismRenderer.CubismTextureColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        return userPartMultiplyColors.get(partIndex).color;
    }

    /**
     * Sets the screen color of the part.
     *
     * @param partIndex part index
     * @param color     screen color to be set (CubismTextureColor)
     */
    public void setPartScreenColor(int partIndex, CubismRenderer.CubismTextureColor color) {
        setPartScreenColor(partIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Sets the screen color of the part.
     *
     * @param partIndex part index
     * @param r         red value of the screen color to be set
     * @param g         green value of the screen color to be set
     * @param b         blue value of the screen color to be set
     * @param a         alpha value of the screen color to be set
     */
    public void setPartScreenColor(int partIndex, float r, float g, float b, float a) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("setPartScreenColor", partIndex, model.getPartCount() - 1);
            return;
        }

        setPartColor(
            partIndex,
            r, g, b, a,
            userPartScreenColors,
            userDrawableScreenColors,
            userOffscreenScreenColors
        );
    }

    /**
     * Returns the screen color of the part.
     *
     * @param partIndex part index
     * @return screen color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getPartScreenColor(int partIndex) {
        if (partIndex < 0 || model.getPartCount() <= partIndex) {
            warnIndexOutOfRange("getPartScreenColor", partIndex, model.getPartCount() - 1);
            return new CubismRenderer.CubismTextureColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        return userPartScreenColors.get(partIndex).color;
    }

    /**
     * Sets the flag indicating whether the color set at runtime is used as the multiply color for the drawable during rendering.
     *
     * @param drawableIndex drawable index
     * @param value         true if the color set at runtime is to be used; otherwise false.
     */
    public void setDrawableMultiplyColorEnabled(int drawableIndex, boolean value) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("setDrawableMultiplyColorEnabled", drawableIndex, model.getDrawableCount() - 1);
            return;
        }

        userDrawableMultiplyColors.get(drawableIndex).isOverridden = value;
    }

    /**
     * Returns the flag indicating whether the color set at runtime is used as the multiply color for the drawable during rendering.
     *
     * @param drawableIndex drawable index
     * @return true if the color set at runtime is used; otherwise false.
     */
    public boolean getDrawableMultiplyColorEnabled(int drawableIndex) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("getDrawableMultiplyColorEnabled", drawableIndex, model.getDrawableCount() - 1);
            return false;
        }

        return userDrawableMultiplyColors.get(drawableIndex).isOverridden;
    }

    /**
     * Sets the flag indicating whether the color set at runtime is used as the screen color for the drawable during rendering.
     *
     * @param drawableIndex drawable index
     * @param value         true if the color set at runtime is to be used; otherwise false.
     */
    public void setDrawableScreenColorEnabled(int drawableIndex, boolean value) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("setDrawableScreenColorEnabled", drawableIndex, model.getDrawableCount() - 1);
            return;
        }

        userDrawableScreenColors.get(drawableIndex).isOverridden = value;
    }

    /**
     * Returns the flag indicating whether the color set at runtime is used as the screen color for the drawable during rendering.
     *
     * @param drawableIndex drawable index
     * @return true if the color set at runtime is used; otherwise false.
     */
    public boolean getDrawableScreenColorEnabled(int drawableIndex) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("getDrawableScreenColorEnabled", drawableIndex, model.getDrawableCount() - 1);
            return false;
        }

        return userDrawableScreenColors.get(drawableIndex).isOverridden;
    }

    /**
     * Sets the multiply color of the drawable.
     *
     * @param drawableIndex drawable index
     * @param color         multiply color to be set (CubismTextureColor)
     */
    public void setDrawableMultiplyColor(int drawableIndex, CubismRenderer.CubismTextureColor color) {
        setDrawableMultiplyColor(drawableIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Sets the multiply color of the drawable.
     *
     * @param drawableIndex drawable index
     * @param r             red value of the multiply color to be set
     * @param g             green value of the multiply color to be set
     * @param b             blue value of the multiply color to be set
     * @param a             alpha value of the multiply color to be set
     */
    public void setDrawableMultiplyColor(int drawableIndex, float r, float g, float b, float a) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("setDrawableMultiplyColor", drawableIndex, model.getDrawableCount() - 1);
            return;
        }

        userDrawableMultiplyColors.get(drawableIndex).color.r = r;
        userDrawableMultiplyColors.get(drawableIndex).color.g = g;
        userDrawableMultiplyColors.get(drawableIndex).color.b = b;
        userDrawableMultiplyColors.get(drawableIndex).color.a = a;
    }

    /**
     * Returns the multiply color from the list of drawables.
     * <p>
     * NOTE:
     *  If the override flag is disabled, returns a cached copy of the model's original color.
     *  If enabled, returns a reference to the user-overridden color.
     *  Modifying the returned value mutates the cache or the user-overridden color data.
     *  Since the cache is shared, even if a previously returned value is retained,
     *  calling this method with a different {@code drawableIndex} overwrites it.
     *
     * @param drawableIndex drawable index
     * @return multiply color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getDrawableMultiplyColor(int drawableIndex) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("getDrawableMultiplyColor", drawableIndex, model.getDrawableCount() - 1);
            return new CubismRenderer.CubismTextureColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (getMultiplyColorEnabled() || getDrawableMultiplyColorEnabled(drawableIndex)) {
            return userDrawableMultiplyColors.get(drawableIndex).color;
        }

        final float[] tmpColor = model.getDrawableMultiplyColor(drawableIndex);
        drawableMultiplyColorCache.r = tmpColor[0];
        drawableMultiplyColorCache.g = tmpColor[1];
        drawableMultiplyColorCache.b = tmpColor[2];
        drawableMultiplyColorCache.a = tmpColor[3];

        return drawableMultiplyColorCache;
    }

    /**
     * Sets the screen color of the drawable.
     *
     * @param drawableIndex drawable index
     * @param color         screen color to be set (CubismTextureColor)
     */
    public void setDrawableScreenColor(int drawableIndex, CubismRenderer.CubismTextureColor color) {
        setDrawableScreenColor(drawableIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Sets the screen color of the drawable.
     *
     * @param drawableIndex drawable index
     * @param r             red value of the screen color to be set
     * @param g             green value of the screen color to be set
     * @param b             blue value of the screen color to be set
     * @param a             alpha value of the screen color to be set
     */
    public void setDrawableScreenColor(int drawableIndex, float r, float g, float b, float a) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("setDrawableScreenColor", drawableIndex, model.getDrawableCount() - 1);
            return;
        }

        userDrawableScreenColors.get(drawableIndex).color.r = r;
        userDrawableScreenColors.get(drawableIndex).color.g = g;
        userDrawableScreenColors.get(drawableIndex).color.b = b;
        userDrawableScreenColors.get(drawableIndex).color.a = a;
    }

    /**
     * Returns the screen color from the list of drawables.
     * <p>
     * NOTE:
     *  If the override flag is disabled, returns a cached copy of the model's original color.
     *  If enabled, returns a reference to the user-overridden color.
     *  Modifying the returned value mutates the cache or the user-overridden color data.
     *  Since the cache is shared, even if a previously returned value is retained,
     *  calling this method with a different {@code drawableIndex} overwrites it.
     *
     * @param drawableIndex drawable index
     * @return screen color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getDrawableScreenColor(int drawableIndex) {
        if (drawableIndex < 0 || model.getDrawableCount() <= drawableIndex) {
            warnIndexOutOfRange("getDrawableScreenColor", drawableIndex, model.getDrawableCount() - 1);
            return new CubismRenderer.CubismTextureColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        if (getScreenColorEnabled() || getDrawableScreenColorEnabled(drawableIndex)) {
            return userDrawableScreenColors.get(drawableIndex).color;
        }

        final float[] tmpColor = model.getDrawableScreenColor(drawableIndex);
        drawableScreenColorCache.r = tmpColor[0];
        drawableScreenColorCache.g = tmpColor[1];
        drawableScreenColorCache.b = tmpColor[2];
        drawableScreenColorCache.a = tmpColor[3];

        return drawableScreenColorCache;
    }

    /**
     * Sets whether the offscreen multiply color is overridden by the SDK.
     * Use true to use the color information from the SDK, or false to use the color information from the model.
     *
     * @param offscreenIndex offscreen index
     * @param value          true to enable override, false to disable
     */
    public void setOffscreenMultiplyColorEnabled(int offscreenIndex, boolean value) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("setOffscreenMultiplyColorEnabled", offscreenIndex, model.getOffscreenCount() - 1);
            return;
        }

        userOffscreenMultiplyColors.get(offscreenIndex).isOverridden = value;
    }

    /**
     * Checks whether the offscreen multiply color is overridden by the SDK.
     *
     * @param offscreenIndex offscreen index
     * @return true if the color information from the SDK is used; otherwise false.
     */
    public boolean getOffscreenMultiplyColorEnabled(int offscreenIndex) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("getOffscreenMultiplyColorEnabled", offscreenIndex, model.getOffscreenCount() - 1);
            return false;
        }

        return userOffscreenMultiplyColors.get(offscreenIndex).isOverridden;
    }

    /**
     * Sets whether the offscreen screen color is overridden by the SDK.
     * Use true to use the color information from the SDK, or false to use the color information from the model.
     *
     * @param offscreenIndex offscreen index
     * @param value          true to enable override, false to disable
     */
    public void setOffscreenScreenColorEnabled(int offscreenIndex, boolean value) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("setOffscreenScreenColorEnabled", offscreenIndex, model.getOffscreenCount() - 1);
            return;
        }

        userOffscreenScreenColors.get(offscreenIndex).isOverridden = value;
    }

    /**
     * Checks whether the offscreen screen color is overridden by the SDK.
     *
     * @param offscreenIndex offscreen index
     * @return true if the color information from the SDK is used; otherwise false.
     */
    public boolean getOffscreenScreenColorEnabled(int offscreenIndex) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("getOffscreenScreenColorEnabled", offscreenIndex, model.getOffscreenCount() - 1);
            return false;
        }

        return userOffscreenScreenColors.get(offscreenIndex).isOverridden;
    }

    /**
     * Sets the multiply color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param color          multiply color to be set (CubismTextureColor)
     */
    public void setOffscreenMultiplyColor(int offscreenIndex, CubismRenderer.CubismTextureColor color) {
        setOffscreenMultiplyColor(offscreenIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Sets the multiply color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param r              red value of the multiply color to be set
     * @param g              green value of the multiply color to be set
     * @param b              blue value of the multiply color to be set
     * @param a              alpha value of the multiply color to be set
     */
    public void setOffscreenMultiplyColor(int offscreenIndex, float r, float g, float b, float a) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("setOffscreenMultiplyColor", offscreenIndex, model.getOffscreenCount() - 1);
            return;
        }

        userOffscreenMultiplyColors.get(offscreenIndex).color.r = r;
        userOffscreenMultiplyColors.get(offscreenIndex).color.g = g;
        userOffscreenMultiplyColors.get(offscreenIndex).color.b = b;
        userOffscreenMultiplyColors.get(offscreenIndex).color.a = a;
    }

    /**
     * Returns the multiply color from the list of offscreen.
     * <p>
     * NOTE:
     *  If the override flag is disabled, returns a cached copy of the model's original color.
     *  If enabled, returns a reference to the user-overridden color.
     *  Modifying the returned value mutates the cache or the user-overridden color data.
     *  Since the cache is shared per {@code offscreenIndex}, even if a previously returned value is retained,
     *  calling this method again with the same {@code offscreenIndex} overwrites it.
     *
     * @param offscreenIndex offscreen index
     * @return multiply color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getOffscreenMultiplyColor(int offscreenIndex) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("getOffscreenMultiplyColor", offscreenIndex, model.getOffscreenCount() - 1);
            return new CubismRenderer.CubismTextureColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (getMultiplyColorEnabled() || getOffscreenMultiplyColorEnabled(offscreenIndex)) {
            return userOffscreenMultiplyColors.get(offscreenIndex).color;
        }

        final float[] tmpColor = model.getOffscreenMultiplyColor(offscreenIndex);

        // インスタンス生成を避けるため、キャッシュリストからインスタンスを取得して値を更新
        CubismRenderer.CubismTextureColor color = offscreenMultiplyColorCache.get(offscreenIndex);
        color.r = tmpColor[0];
        color.g = tmpColor[1];
        color.b = tmpColor[2];
        color.a = tmpColor[3];

        return color;
    }

    /**
     * Sets the screen color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param color          screen color to be set (CubismTextureColor)
     */
    public void setOffscreenScreenColor(int offscreenIndex, CubismRenderer.CubismTextureColor color) {
        setOffscreenScreenColor(offscreenIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Sets the screen color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param r              red value of the screen color to be set
     * @param g              green value of the screen color to be set
     * @param b              blue value of the screen color to be set
     * @param a              alpha value of the screen color to be set
     */
    public void setOffscreenScreenColor(int offscreenIndex, float r, float g, float b, float a) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("setOffscreenScreenColor", offscreenIndex, model.getOffscreenCount() - 1);
            return;
        }

        userOffscreenScreenColors.get(offscreenIndex).color.r = r;
        userOffscreenScreenColors.get(offscreenIndex).color.g = g;
        userOffscreenScreenColors.get(offscreenIndex).color.b = b;
        userOffscreenScreenColors.get(offscreenIndex).color.a = a;
    }

    /**
     * Returns the screen color from the list of offscreen.
     * <p>
     * NOTE:
     *  If the override flag is disabled, returns a cached copy of the model's original color.
     *  If enabled, returns a reference to the user-overridden color.
     *  Modifying the returned value mutates the cache or the user-overridden color data.
     *  Since the cache is shared per {@code offscreenIndex}, even if a previously returned value is retained,
     *  calling this method again with the same {@code offscreenIndex} overwrites it.
     *
     * @param offscreenIndex offscreen index
     * @return screen color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getOffscreenScreenColor(int offscreenIndex) {
        if (offscreenIndex < 0 || model.getOffscreenCount() <= offscreenIndex) {
            warnIndexOutOfRange("getOffscreenScreenColor", offscreenIndex, model.getOffscreenCount() - 1);
            return new CubismRenderer.CubismTextureColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        if (getScreenColorEnabled() || getOffscreenScreenColorEnabled(offscreenIndex)) {
            return userOffscreenScreenColors.get(offscreenIndex).color;
        }

        final float[] tmpColor = model.getOffscreenScreenColor(offscreenIndex);

        // インスタンス生成を避けるため、キャッシュリストからインスタンスを取得して値を更新
        CubismRenderer.CubismTextureColor color = offscreenScreenColorCache.get(offscreenIndex);
        color.r = tmpColor[0];
        color.g = tmpColor[1];
        color.b = tmpColor[2];
        color.a = tmpColor[3];

        return color;
    }

    private static void warnIndexOutOfRange(String funcName, int index, int range) {
        CubismDebug.cubismLogWarning("%s() : index is out of range. index = %d, valid range = [0, %d].", funcName, index, range);
    }

    private void setPartColor(
        int partIndex,
        float r, float g, float b, float a,
        List<ColorData> partColors,
        List<ColorData> drawableColors,
        List<ColorData> offscreenColors
    ) {
        partColors.get(partIndex).color.r = r;
        partColors.get(partIndex).color.g = g;
        partColors.get(partIndex).color.b = b;
        partColors.get(partIndex).color.a = a;

        if (partColors.get(partIndex).isOverridden) {
            final int offscreenIndex = model.getPartOffscreenIndices()[partIndex];
            if (offscreenIndex == CubismModel.CubismNoIndex.OFFSCREEN.index) {
                // If no offscreen buffer is attached, the effect is applied to the children.
                List<CubismModelObjectInfo> objects = partsHierarchy.get(partIndex).objects;
                for (int i = 0; i < objects.size(); i++) {
                    CubismModelObjectInfo object = objects.get(i);
                    if (object.objectType == CubismModelObjectInfo.ObjectType.DRAWABLE) {
                        final int drawableIndex = object.objectIndex;
                        drawableColors.get(drawableIndex).color.r = r;
                        drawableColors.get(drawableIndex).color.g = g;
                        drawableColors.get(drawableIndex).color.b = b;
                        drawableColors.get(drawableIndex).color.a = a;
                    } else {
                        final int childPartIndex = object.objectIndex;
                        setPartColor(
                            childPartIndex,
                            r, g, b, a,
                            partColors,
                            drawableColors,
                            offscreenColors
                        );
                    }
                }
            } else {
                // If an offscreen buffer is attached, only that offscreen buffer is affected.
                offscreenColors.get(offscreenIndex).color.r = r;
                offscreenColors.get(offscreenIndex).color.g = g;
                offscreenColors.get(offscreenIndex).color.b = b;
                offscreenColors.get(offscreenIndex).color.a = a;
            }
        }
    }

    private void setPartColorEnabled(
        int partIndex,
        boolean value,
        List<ColorData> partColors,
        List<ColorData> drawableColors,
        List<ColorData> offscreenColors
    ) {
        partColors.get(partIndex).isOverridden = value;

        final int offscreenIndex = model.getPartOffscreenIndices()[partIndex];
        if (offscreenIndex == CubismModel.CubismNoIndex.OFFSCREEN.index) {
            // If no offscreen buffer is attached, the effect is applied to the children.
            List<CubismModelObjectInfo> objects = partsHierarchy.get(partIndex).objects;
            for (int i = 0; i < objects.size(); i++) {
                CubismModelObjectInfo object = objects.get(i);
                if (object.objectType == CubismModelObjectInfo.ObjectType.DRAWABLE) {
                    final int drawableIndex = object.objectIndex;
                    drawableColors.get(drawableIndex).isOverridden = value;
                    if (value) {
                        drawableColors.get(drawableIndex).color.r = partColors.get(partIndex).color.r;
                        drawableColors.get(drawableIndex).color.g = partColors.get(partIndex).color.g;
                        drawableColors.get(drawableIndex).color.b = partColors.get(partIndex).color.b;
                        drawableColors.get(drawableIndex).color.a = partColors.get(partIndex).color.a;
                    }
                } else {
                    final int childPartIndex = object.objectIndex;
                    if (value) {
                        partColors.get(childPartIndex).color.r = partColors.get(partIndex).color.r;
                        partColors.get(childPartIndex).color.g = partColors.get(partIndex).color.g;
                        partColors.get(childPartIndex).color.b = partColors.get(partIndex).color.b;
                        partColors.get(childPartIndex).color.a = partColors.get(partIndex).color.a;
                    }
                    setPartColorEnabled(
                        childPartIndex,
                        value,
                        partColors,
                        drawableColors,
                        offscreenColors
                    );
                }
            }
        } else {
            // If an offscreen buffer is attached, only that offscreen buffer is affected.
            offscreenColors.get(offscreenIndex).isOverridden = value;
            if (value) {
                offscreenColors.get(offscreenIndex).color.r = partColors.get(partIndex).color.r;
                offscreenColors.get(offscreenIndex).color.g = partColors.get(partIndex).color.g;
                offscreenColors.get(offscreenIndex).color.b = partColors.get(partIndex).color.b;
                offscreenColors.get(offscreenIndex).color.a = partColors.get(partIndex).color.a;
            }
        }
    }

    private final CubismModel model;

    /**
     * Reference to the parts hierarchy from the {@link CubismModel}, used in
     * {@link #setPartColor} and {@link #setPartColorEnabled} to propagate colors
     * along the parent-child structure.
     */
    private final List<CubismModelPartInfo> partsHierarchy;

    /**
     * Whether the multiply color override is enabled for the entire model.
     */
    private boolean isOverriddenModelMultiplyColors;

    /**
     * Whether the screen color override is enabled for the entire model.
     */
    private boolean isOverriddenModelScreenColors;

    private final List<ColorData> userPartMultiplyColors = new ArrayList<>();
    private final List<ColorData> userPartScreenColors = new ArrayList<>();
    private final List<ColorData> userDrawableMultiplyColors = new ArrayList<>();
    private final List<ColorData> userDrawableScreenColors = new ArrayList<>();
    private final List<ColorData> userOffscreenMultiplyColors = new ArrayList<>();
    private final List<ColorData> userOffscreenScreenColors = new ArrayList<>();

    /**
     * Single shared cache instance for {@link #getDrawableMultiplyColor(int)}
     * to avoid creating a new instance every call.
     */
    private final CubismRenderer.CubismTextureColor drawableMultiplyColorCache = new CubismRenderer.CubismTextureColor();

    /**
     * Single shared cache instance for {@link #getDrawableScreenColor(int)}
     * to avoid creating a new instance every call.
     */
    private final CubismRenderer.CubismTextureColor drawableScreenColorCache = new CubismRenderer.CubismTextureColor();

    /**
     * One cache instance per offscreenIndex for {@link #getOffscreenMultiplyColor(int)}
     * to avoid creating a new instance every call.
     */
    private final List<CubismRenderer.CubismTextureColor> offscreenMultiplyColorCache = new ArrayList<>();

    /**
     * One cache instance per offscreenIndex for {@link #getOffscreenScreenColor(int)}
     * to avoid creating a new instance every call.
     */
    private final List<CubismRenderer.CubismTextureColor> offscreenScreenColorCache = new ArrayList<>();
}
