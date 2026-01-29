/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.model;

import com.live2d.sdk.cubism.core.*;
import com.live2d.sdk.cubism.framework.CubismFramework;
import com.live2d.sdk.cubism.framework.id.CubismId;
import com.live2d.sdk.cubism.framework.math.CubismMath;
import com.live2d.sdk.cubism.framework.rendering.CubismRenderer;
import com.live2d.sdk.cubism.framework.rendering.CubismRenderer.CubismBlendMode;
import com.live2d.sdk.cubism.framework.rendering.csmBlendMode;
import com.live2d.sdk.cubism.framework.utils.CubismDebug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class created from Mclapoc data.
 */
public class CubismModel {
    /**
     * Enumeration defining index values that indicate the non-existence of objects.
     */
    public enum CubismNoIndex {
        /**
         * Index value when no parent exists.
         */
        PARENT(-1),
        /**
         * Index value when no referenced offscreen exists.
         */
        OFFSCREEN(-1);

        public final int index;

        CubismNoIndex(int index) {
            this.index = index;
        }
    }

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
     * Structure to manage texture culling settings.
     */
    public static class CullingData {
        /**
         * Constructor.
         */
        public CullingData() {
            this.isOverridden = false;
            this.isCulling = false;
        }

        /**
         * Constructor.
         *
         * @param isOverridden whether to be overridden
         * @param isCulling    whether to be culling
         */
        public CullingData(boolean isOverridden, boolean isCulling) {
            this.isOverridden = isOverridden;
            this.isCulling = isCulling;
        }

        /**
         * Copy constructor.
         *
         * @param other the CullingData object to copy from
         */
        public CullingData(CullingData other) {
            this.isOverridden = other.isOverridden;
            this.isCulling = other.isCulling;
        }

        /**
         * Whether to be overridden.
         */
        public boolean isOverridden;

        /**
         * Whether to be culling.
         */
        public boolean isCulling;
    }

    /**
     * (deprecated) Structure for color information of drawing object
     *
     * @deprecated This class is deprecated. Please use {@link ColorData} instead.
     */
    public static class DrawableColorData {
        /**
         * Constructor
         */
        public DrawableColorData() {
            color = new CubismRenderer.CubismTextureColor();
        }

        /**
         * Constructor
         *
         * @param isOverridden flag whether to be overridden
         * @param color texture color
         * @throws IllegalArgumentException if an argument is null
         */
        public DrawableColorData(boolean isOverridden, CubismRenderer.CubismTextureColor color) {
            if (color == null) {
                throw new IllegalArgumentException("color is null");
            }
            this.isOverridden = isOverridden;
            this.color = color;
        }

        /**
         * Copy constructor
         *
         * @param data DrawableColorData instance to be copied
         */
        public DrawableColorData(DrawableColorData data) {
            this.isOverridden = data.isOverridden;
            this.color = data.color;
        }

        /**
         * flag whether to be overridden
         */
        public boolean isOverridden;
        /**
         * texture color
         */
        public CubismRenderer.CubismTextureColor color;
    }

    /**
     * (deprecated) Structure to handle texture color in RGBA.
     *
     * @deprecated This class is deprecated. Please use {@link ColorData} instead.
     */
    public static class PartColorData {
        /**
         * コンストラクタ
         */
        public PartColorData() {
            color = new CubismRenderer.CubismTextureColor();
        }

        /**
         * コンストラクタ
         *
         * @param isOverridden SDKで設定した色情報でパーツの描画色を上書きするか。trueなら上書きする。
         * @param color 色情報
         */
        public PartColorData(boolean isOverridden, CubismRenderer.CubismTextureColor color) {
            if (color == null) {
                throw new IllegalArgumentException("color is null");
            }
            this.isOverridden = isOverridden;
            this.color = color;
        }

        /**
         * コピーコンストラクタ
         *
         * @param data コピーするPartColorDataインスタンス
         */
        public PartColorData(PartColorData data) {
            this.isOverridden = data.isOverridden;
            this.color = data.color;
        }

        /**
         * SDKで設定した色情報でパーツの描画色を上書きするか。trueなら上書きする。
         */
        public boolean isOverridden;

        /**
         * パーツの色情報
         */
        public CubismRenderer.CubismTextureColor color;
    }

    /**
     * (deprecated) Structure to manage texture culling settings
     *
     * @deprecated This class is deprecated. Please use {@link CullingData} instead.
     */
    public static class DrawableCullingData {
        /**
         * コンストラクタ
         */
        DrawableCullingData() {}

        /**
         * コンストラクタ
         *
         * @param isOverridden モデルのカリング設定を上書きするかどうか
         * @param isCulling カリングするかどうか
         */
        DrawableCullingData(boolean isOverridden, boolean isCulling) {
            this.isOverridden = isOverridden;
            this.isCulling = isCulling;
        }

        /**
         * モデルのカリング設定を上書きするかどうか
         */
        public boolean isOverridden;
        /**
         * カリングするかどうか
         */
        public boolean isCulling;
    }

    /**
     * Class for managing the override of parameter repetition settings
     */
    public static class ParameterRepeatData {
        /**
         * Constructor
         */
        public ParameterRepeatData() {
            this.isOverridden = false;
            this.isParameterRepeated = false;
        }

        /**
         * Constructor
         *
         * @param isOverridden whether to be overriden
         * @param isParameterRepeated override flag for settings
         */
        public ParameterRepeatData(boolean isOverridden, boolean isParameterRepeated) {
            this.isOverridden = isOverridden;
            this.isParameterRepeated = isParameterRepeated;
        }

        /**
         * Whether to be overridden
         */
        public boolean isOverridden;

        /**
         * Override flag for settings
         */
        public boolean isParameterRepeated;
    }

    /**
     * Information for part child draw objects.
     */
    public static class PartChildDrawObjects {
        /**
         * Constructor.
         */
        public PartChildDrawObjects() {
            drawableIndices = new ArrayList<>();
            offscreenIndices = new ArrayList<>();
        }

        /**
         * Constructor.
         *
         * @param drawableIndices  collection of Drawable indices
         * @param offscreenIndices collection of Offscreen indices
         */
        public PartChildDrawObjects(List<Integer> drawableIndices, List<Integer> offscreenIndices) {
            this.drawableIndices = new ArrayList<>(drawableIndices);
            this.offscreenIndices = new ArrayList<>(offscreenIndices);
        }

        /**
         * Copy constructor.
         *
         * @param other the PartChildDrawObjects object to copy from
         */
        public PartChildDrawObjects(PartChildDrawObjects other) {
            this.drawableIndices = new ArrayList<>(other.drawableIndices);
            this.offscreenIndices = new ArrayList<>(other.offscreenIndices);
        }

        /**
         * Collection of Drawable indices.
         */
        public List<Integer> drawableIndices;

        /**
         * Collection of Offscreen indices.
         */
        public List<Integer> offscreenIndices;
    }

    /**
     * Information for a Cubism model object.
     */
    public static class CubismModelObjectInfo {
        /**
         * Type used in object information.
         */
        public enum ObjectType {
            DRAWABLE(0),
            PARTS(1);

            public final int index;

            ObjectType(int index) {
                this.index = index;
            }
        }

        /**
         * Constructor.
         *
         * @param objectIndex index of the object
         * @param type        type of the object (Drawable, Parts)
         */
        public CubismModelObjectInfo(int objectIndex, ObjectType type) {
            this.objectIndex = objectIndex;
            this.objectType = type;
        }

        /**
         * Type of the object (Drawable, Parts).
         */
        public ObjectType objectType;

        /**
         * Index of the object.
         */
        public int objectIndex;
    }

    /**
     * Information for a Cubism model part.
     */
    public static class CubismModelPartInfo {
        /**
         * Constructor.
         */
        public CubismModelPartInfo() {
            objects = new ArrayList<>();
            childDrawObjects = new PartChildDrawObjects();
        }

        /**
         * Constructor
         *
         * @param objects          collection of CubismModelObjectInfo
         * @param childDrawObjects information of part child draw objects
         */
        public CubismModelPartInfo(List<CubismModelObjectInfo> objects, PartChildDrawObjects childDrawObjects) {
            this.objects = new ArrayList<>(objects);
            this.childDrawObjects = new PartChildDrawObjects(childDrawObjects);
        }

        /**
         * Collection of object information.
         */
        public List<CubismModelObjectInfo> objects;

        /**
         * Information of part child draw objects.
         */
        public PartChildDrawObjects childDrawObjects;

        /**
         * Returns the number of child objects.
         *
         * @return number of child objects
         */
        public int getChildObjectCount() {
            return objects.size();
        }
    }

    /**
     * Update model's parameters.
     */
    public void update() {
        model.update();
        model.resetDrawableDynamicFlags();
    }

    /**
     * Get the canvas width in Pixel.
     *
     * @return canvas width(pixel)
     */
    public float getCanvasWidthPixel() {
        if (model == null) {
            return 0.0f;
        }
        CubismCanvasInfo canvasInfo = model.getCanvasInfo();

        return canvasInfo.getSizeInPixels()[0];
    }

    /**
     * Get the canvas height in Pixel.
     *
     * @return canvas height(pixel)
     */
    public float getCanvasHeightPixel() {
        if (model == null) {
            return 0.0f;
        }
        CubismCanvasInfo canvasInfo = model.getCanvasInfo();

        return canvasInfo.getSizeInPixels()[1];
    }

    /**
     * Get PixelsPerUnit.
     *
     * @return PixelsPerUnit
     */
    public float getPixelPerUnit() {
        if (model == null) {
            return 0.0f;
        }
        CubismCanvasInfo canvasInfo = model.getCanvasInfo();

        return canvasInfo.getPixelsPerUnit();
    }

    /**
     * Get the canvas width.
     *
     * @return canvas width
     */
    public float getCanvasWidth() {
        if (model == null) {
            return 0.0f;
        }

        CubismCanvasInfo canvasInfo = model.getCanvasInfo();

        return canvasInfo.getSizeInPixels()[0] / canvasInfo.getPixelsPerUnit();
    }

    /**
     * Get the canvas height.
     *
     * @return canvas height
     */
    public float getCanvasHeight() {
        if (model == null) {
            return 0.0f;
        }

        CubismCanvasInfo canvasInfo = model.getCanvasInfo();

        return canvasInfo.getSizeInPixels()[1] / canvasInfo.getPixelsPerUnit();
    }

    /**
     * Returns the array of object render orders.
     *
     * @return array of object render orders
     */
    public int[] getRenderOrders() {
        final int[] renderOrders = model.getRenderOrders();
        return renderOrders;
    }

    /**
     * Get the index of parts.
     *
     * @param partId parts ID
     * @return parts index
     */
    public int getPartIndex(CubismId partId) {
        final CubismPartView partView = model.findPartView(partId.getString());
        if (partView != null) {
            return partView.getIndex();
        }

        // If the part does not exist in the model, it searches for it in the non-existent part ID list and returns its index.
        if (notExistPartIds.containsKey(partId)) {
            return notExistPartIds.get(partId);
        }

        // If the part does not exist in the non-existent part ID list, add newly the element.
        final int partCount = partValues.length;
        final int partIndex = partCount + notExistPartIds.size();
        notExistPartIds.put(partId, partIndex);
        notExistPartIndices.add(partIndex);

        float[] tmp = new float[notExistPartIndices.size()];
        System.arraycopy(notExistPartOpacities, 0, tmp, 0, notExistPartIndices.size() - 1);
        tmp[notExistPartIndices.size() - 1] = 0.0f;
        notExistPartOpacities = new float[notExistPartIndices.size()];
        System.arraycopy(tmp, 0, notExistPartOpacities, 0, notExistPartIndices.size());

        return partIndex;
    }

    /**
     * パーツのIDを取得する。
     *
     * @param partIndex 取得するパーツのインデックス
     * @return パーツのID
     */
    public CubismId getPartId(int partIndex){
        assert (0 <= partIndex && partIndex < partIds.size());
        return partIds.get(partIndex);
    }

    /**
     * Get the number of parts.
     *
     * @return number of parts
     */
    public int getPartCount() {
        return model.getPartViews().length;
    }

    /**
     * Returns the indices of the parent parts for the parts.
     *
     * @return Indices of parent parts for the parts.
     */
    public int[] getPartParentPartIndices() {
        final int[] partIndices = model.getParts().getParentPartIndices();
        return partIndices;
    }

    /**
     * Returns the index of the offscreen sources for the part.
     *
     * @return index of offscreen sources for the part
     */
    public int[] getPartOffscreenIndices() {
        final int[] offscreenSourcesIndices = model.getParts().getOffscreenIndices();
        return offscreenSourcesIndices;
    }
    /**
     * Set an opacity of the part.
     *
     * @param partId part ID
     * @param opacity opacity
     */
    public void setPartOpacity(CubismId partId, float opacity) {
        // Speeding up the process, this can get partIndex. However, it is not necessary when setting externally because it is not frequently called.
        final int index = getPartIndex(partId);

        if (index < 0) {
            // Skip processes because there is no part.
            return;
        }

        setPartOpacity(index, opacity);
    }

    /**
     * Set an opacity of the part.
     *
     * @param partIndex part index
     * @param opacity part opacity
     */
    public void setPartOpacity(int partIndex, float opacity) {
        if (notExistPartIndices.contains(partIndex)) {
            int index = notExistPartIndices.indexOf(partIndex);
            notExistPartOpacities[index] = opacity;
            return;
        }

        // Detect whether partIndex is not out of bounds index
        assert 0 <= partIndex && partIndex < getPartCount();

        partValues[partIndex].setOpacity(opacity);
    }

    /**
     * Get the opacity of the part.
     *
     * @param partId part ID
     * @return part opacity
     */
    public float getPartOpacity(CubismId partId) {
        // Speeding up the process, this can get partIndex. However, it is not necessary when setting externally because it is not frequently called.
        final int index = getPartIndex(partId);

        if (index < 0) {
            // Skip processes because there is no part
            return 0;
        }

        return getPartOpacity(index);
    }

    /**
     * Get the opacity of the part.
     *
     * @param partIndex part index
     * @return part opacity
     */
    public float getPartOpacity(int partIndex) {
        if (notExistPartIndices.contains(partIndex)) {
            // If the part ID does not exist in the model, returns the opacity from non-existence parts list.
            int index = notExistPartIndices.indexOf(partIndex);
            return notExistPartOpacities[index];
        }

        // Detect whether partIndex is not out of bounds index
        assert 0 <= partIndex && partIndex < getPartCount();

        return partValues[partIndex].getOpacity();
    }

    /**
     * Returns the index of the parent part of the part.
     *
     * @param partIndex part index
     * @return index of the parent part of the part
     */
    public int getPartParentPartIndex(int partIndex) {
        return model.getParts().getParentPartIndices()[partIndex];
    }

    /**
     * Returns the child draw objects of the part.
     *
     * @param partInfoIndex index of the part info
     */
    public void getPartChildDrawObjects(int partInfoIndex) {
        if (partsHierarchy.get(partInfoIndex).getChildObjectCount() < 1) {
            return;
        }

        PartChildDrawObjects childDrawObjects = partsHierarchy.get(partInfoIndex).childDrawObjects;

        // 既にchildDrawObjectsが処理されている場合はスキップ
        if (childDrawObjects.drawableIndices.size() != 0 || childDrawObjects.offscreenIndices.size() != 0) {
            return;
        }

        List<CubismModelObjectInfo> objects = partsHierarchy.get(partInfoIndex).objects;

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).objectType == CubismModelObjectInfo.ObjectType.PARTS) {
                // 子のパーツの場合、再帰的に子objectsを取得
                getPartChildDrawObjects(objects.get(i).objectIndex);

                // 子パーツの子Drawable, Offscreenを取得
                final int objectIndex = objects.get(i).objectIndex;
                PartChildDrawObjects childToChildDrawObjects = partsHierarchy.get(objectIndex).childDrawObjects;

                for (int j = 0; j < childToChildDrawObjects.drawableIndices.size(); j++) {
                    // 孫Drawableをパーツの子Drawableに追加
                    childDrawObjects.drawableIndices.add(childToChildDrawObjects.drawableIndices.get(j));
                }
                for (int j = 0; j < childToChildDrawObjects.offscreenIndices.size(); j++) {
                    // 孫Offscreenをパーツの子Offscreenに追加
                    childDrawObjects.offscreenIndices.add(childToChildDrawObjects.offscreenIndices.get(j));
                }

                // Offscreenの確認
                int offscreenIndex = model.getParts().getOffscreenIndices()[objects.get(i).objectIndex];
                if (offscreenIndex != CubismNoIndex.OFFSCREEN.index) {
                    // Offscreenが存在する場合、パーツの子Offscreenに追加
                    childDrawObjects.offscreenIndices.add(offscreenIndex);
                }
            } else if (objects.get(i).objectType == CubismModelObjectInfo.ObjectType.DRAWABLE) {
                // Drawableの場合、パーツの子Drawableに追加
                childDrawObjects.drawableIndices.add(objects.get(i).objectIndex);
            }
        }
    }

    /**
     * Returns the parent-child hierarchy of the parts.
     *
     * @return collection of parts hierarchy
     */
    public List<CubismModelPartInfo> getPartsHierarchy() {
        return partsHierarchy;
    }

    //========================================================
    //  Parameter Functions.
    //========================================================

    /**
     * Get the index of parameters.
     *
     * @param parameterId parameter ID
     * @return parameter index
     */
    public int getParameterIndex(CubismId parameterId) {
        final CubismParameterView parameterView = model.findParameterView(parameterId.getString());
        if (parameterView != null) {
            return parameterView.getIndex();
        }

        // If the parameter does not exist in the model, it searches for it in the non-existent parameter ID list and returns its index.
        if (notExistParameterIds.containsKey(parameterId)) {
            final Integer index = notExistParameterIds.get(parameterId);
            assert index != null;
            return index;
        }

        // If the parameter does not exist in the non-existent parameter ID list, add newly the element.
        final int parameterCount = parameterValues.length;
        final int parameterIndex = parameterCount + notExistParameterIds.size();
        notExistParameterIds.put(parameterId, parameterIndex);
        notExistParameterIndices.add(parameterIndex);

        float[] tmp = new float[notExistParameterIndices.size()];
        System.arraycopy(notExistParameterValues, 0, tmp, 0, notExistParameterIndices.size() - 1);
        tmp[notExistParameterIndices.size() - 1] = 0.0f;
        notExistParameterValues = new float[notExistParameterIndices.size()];
        System.arraycopy(tmp, 0, notExistParameterValues, 0, notExistParameterIndices.size());

        return parameterIndex;
    }

    /**
     * パラメータのIDを取得する。
     *
     * @param parameterIndex パラメータのインデックス
     * @return パラメータのID
     */
    public CubismId getParameterId(int parameterIndex) {
        assert (0 <= parameterIndex && parameterIndex < parameterIds.size());
        return parameterIds.get(parameterIndex);
    }

    /**
     * Get the number of parameters.
     *
     * @return the number of parameters
     */
    public int getParameterCount() {
        return parameterValues.length;
    }

    /**
     * Return the type of parameter at the index specified by the argument.
     *
     * @param parameterIndex parameter index
     * @return the type of parameter at the index specified by the argument
     */
    public CubismParameters.ParameterType getParameterType(int parameterIndex) {
        return model.getParameterViews()[parameterIndex].getType();
    }

    /**
     * Get the maximum value of parameters.
     *
     * @param parameterIndex parameter index
     * @return the maximum value of parameter
     */
    public float getParameterMaximumValue(int parameterIndex) {
        return model.getParameterViews()[parameterIndex].getMaximumValue();
    }

    /**
     * Get the minimum value of parameters.
     *
     * @param parameterIndex parameter index
     * @return the minimum value of parameter
     */
    public float getParameterMinimumValue(int parameterIndex) {
        return model.getParameterViews()[parameterIndex].getMinimumValue();
    }

    /**
     * Get the default value of parameters.
     *
     * @param parameterIndex parameter index
     * @return the default value of parameter
     */
    public float getParameterDefaultValue(int parameterIndex) {
        return model.getParameterViews()[parameterIndex].getDefaultValue();
    }

    /**
     * Get the value of parameter.
     *
     * @param parameterId parameter ID
     * @return parameter value
     */
    public float getParameterValue(CubismId parameterId) {
        // Speeding up the process, this can get partIndex. However, it is not necessary when setting externally because it is not frequently called.
        final int parameterIndex = getParameterIndex(parameterId);
        return getParameterValue(parameterIndex);
    }

    /**
     * Get the value of parameter.
     *
     * @param parameterIndex parameter index
     * @return the value of parameter
     */
    public float getParameterValue(int parameterIndex) {
        if (notExistParameterIndices.contains(parameterIndex)) {
            int index = notExistParameterIndices.indexOf(parameterIndex);
            final float value = notExistParameterValues[index];
            return value;
        }

        // Detect whether partIndex is not out of bounds index
        assert 0 <= parameterIndex && parameterIndex < getParameterCount();

        return parameterValues[parameterIndex].getValue();
    }

    /**
     * Set the value of parameter.
     *
     * @param parameterId parameter ID
     * @param value parameter value
     */
    public void setParameterValue(CubismId parameterId, float value) {
        setParameterValue(parameterId, value, 1.0f);
    }

    /**
     * Set the value of parameter.
     *
     * @param parameterId parameter ID
     * @param value parameter value
     * @param weight weight
     */
    public void setParameterValue(CubismId parameterId, float value, float weight) {
        final int index = getParameterIndex(parameterId);
        setParameterValue(index, value, weight);
    }

    /**
     * Set the value of parameter.
     *
     * @param parameterIndex parametere index
     * @param value parameter value
     */
    public void setParameterValue(int parameterIndex, float value) {
        setParameterValue(parameterIndex, value, 1.0f);
    }

    /**
     * Set the value of parameter.
     *
     * @param parameterIndex parameter index
     * @param value parameter value
     * @param weight weight
     */
    public void setParameterValue(int parameterIndex, float value, float weight) {
        if (notExistParameterIndices.contains(parameterIndex)) {
            int index = notExistParameterIndices.indexOf(parameterIndex);
            final float parameterValue = notExistParameterValues[index];
            final float weightedParameterValue = (weight == 1.0f)
                                                 ? value
                                                 : (parameterValue * (1.0f - weight)) + (value * weight);
            notExistParameterValues[index] = weightedParameterValue;
            return;
        }


        // Detect whether partIndex is not out of bounds index
        assert 0 <= parameterIndex && parameterIndex < getParameterCount();

        CubismParameterView parameter = parameterValues[parameterIndex];

        if (isRepeat(parameterIndex)) {
            value = getParameterRepeatValue(parameterIndex, value);
        } else {
            value = getParameterClampValue(parameterIndex, value);
        }

        final float parameterValue = parameter.getValue();
        final float weightedParameterValue = (weight == 1.0f)
                                             ? value
                                             : (parameterValue * (1.0f - weight)) + (value * weight);
        parameter.setValue(weightedParameterValue);
    }

    /**
     * Gets whether the parameter has the repeat setting.
     *
     * @param parameterIndex Parameter index
     *
     * @return true if it is set, otherwise returns false.
     */
    public boolean isRepeat(int parameterIndex) {
        if (notExistParameterIndices.contains(parameterIndex)) {
            return false;
        }

        // Detect whether partIndex is not out of bounds index
        assert 0 <= parameterIndex && parameterIndex < getParameterCount();

        boolean isRepeat;

        // Determines whether to perform parameter repeat processing
        if (isOverriddenParameterRepeat || userParameterRepeatDataList.get(parameterIndex).isOverridden) {
            // Use repeat information set on the SDK side
            isRepeat = userParameterRepeatDataList.get(parameterIndex).isParameterRepeated;
        } else {
            // Use repeat information set in Editor
            isRepeat = this.model.getParameters().getParameterRepeats()[parameterIndex];
        }

        return isRepeat;
    }

    /**
     * Returns the calculated result ensuring the value falls within the parameter's range.
     *
     * @param parameterIndex Parameter index
     * @param value Parameter value
     *
     * @return a value that falls within the parameter’s range. If the parameter does not exist, returns it as is.
     */
    public float getParameterRepeatValue(int parameterIndex, float value) {
        if (this.notExistParameterIndices.contains(parameterIndex)) {
            return value;
        }
        // In-index range detection
        assert (0 <= parameterIndex && parameterIndex < getParameterCount());

        final float maxValue = this.model.getParameters().getMaximumValues()[parameterIndex];
        final float minValue = this.model.getParameters().getMinimumValues()[parameterIndex];
        final float valueSize = maxValue - minValue;

        if (maxValue < value) {
            float overValue = CubismMath.modF(value - maxValue, valueSize);
            if (!Float.isNaN(overValue)) {
                value = minValue + overValue;
            } else {
                value = maxValue;
            }
        }
        if (value < minValue) {
            float overValue = CubismMath.modF(minValue - value, valueSize);
            if (!Float.isNaN(overValue)) {
                value = maxValue - overValue;
            } else {
                value = minValue;
            }
        }

        return value;
    }

    /**
     * Returns the result of clamping the value to ensure it falls within the parameter's range.
     *
     * @param parameterIndex Parameter index
     * @param value Parameter value
     *
     * @return the clamped value. If the parameter does not exist, returns it as is.
     */
    public float getParameterClampValue(int parameterIndex, float value) {
        if (notExistParameterIndices.contains(parameterIndex)) {
            return value;
        }

        // In-index range detection
        assert (0 <= parameterIndex && parameterIndex < this.getParameterCount());

        final float maxValue = this.model.getParameters().getMaximumValues()[parameterIndex];
        final float minValue = this.model.getParameters().getMinimumValues()[parameterIndex];

        return CubismMath.clampF(value, minValue, maxValue);
    }

    /**
     * Returns the repeat of the parameter.
     *
     * @param parameterIndex Parameter index
     *
     * @return the raw data parameter repeat from the Cubism Core.
     */
    public boolean getParameterRepeats(int parameterIndex) {
        return this.model.getParameters().getParameterRepeats()[parameterIndex];
    }

    /**
     * Add the value of parameter.
     *
     * @param parameterId parameter ID
     * @param value the value to be added
     */
    public void addParameterValue(CubismId parameterId, float value) {
        addParameterValue(parameterId, value, 1.0f);
    }

    /**
     * Add the value of parameter.
     *
     * @param parameterId parameter ID
     * @param value the value to be added
     * @param weight weight
     */
    public void addParameterValue(CubismId parameterId, float value, float weight) {
        final int index = getParameterIndex(parameterId);
        addParameterValue(index, value, weight);
    }

    /**
     * Add the value of parameter.
     *
     * @param parameterIndex parameter index
     * @param value the value to be added
     */
    public void addParameterValue(int parameterIndex, float value) {
        addParameterValue(parameterIndex, value, 1.0f);
    }

    /**
     * Add the value of parameter.
     *
     * @param parameterIndex parameter index
     * @param value the value to be added
     * @param weight weight
     */
    public void addParameterValue(int parameterIndex, float value, float weight) {
        setParameterValue(
            parameterIndex,
            getParameterValue(parameterIndex) + (value * weight)
        );
    }

    /**
     * Multiply the value of parameter.
     *
     * @param parameterId parameter ID
     * @param value the value to be multiplied
     */
    public void multiplyParameterValue(CubismId parameterId, float value) {
        multiplyParameterValue(parameterId, value, 1.0f);
    }

    /**
     * Multiply the value of parameter.
     *
     * @param parameterId parameter ID
     * @param value the value to be multiplied
     * @param weight weight
     */
    public void multiplyParameterValue(CubismId parameterId, float value, float weight) {
        final int index = getParameterIndex(parameterId);
        multiplyParameterValue(index, value, weight);
    }

    /**
     * Multiply the value of parameter.
     *
     * @param parameterIndex parameter index
     * @param value the value to be multiplied
     */
    public void multiplyParameterValue(int parameterIndex, float value) {
        multiplyParameterValue(parameterIndex, value, 1.0f);
    }

    /**
     * Multiply the value of parameter.
     *
     * @param parameterIndex parameter index
     * @param value the value to be multiplied
     * @param weight weight
     */
    public void multiplyParameterValue(int parameterIndex, float value, float weight) {
        setParameterValue(
            parameterIndex,
            getParameterValue(parameterIndex) * (1.0f + (value - 1.0f) * weight)
        );
    }

    //========================================================
    //  Drawable Functions.
    //========================================================

    /**
     * Get the index of Drawable.
     *
     * @param drawableId Drawable ID
     * @return Drawable index. If there is no index, return -1.
     */
    public int getDrawableIndex(CubismId drawableId) {
        final CubismDrawableView drawableIndex = model.findDrawableView(drawableId.getString());
        if (drawableIndex != null) {
            return drawableIndex.getIndex();
        }

        return -1;
    }

    /**
     * Get the number of Drawable.
     *
     * @return the number of Drawable
     */
    public int getDrawableCount() {
        return model.getDrawableViews().length;
    }

    /**
     * Get Drawable ID.
     *
     * @param drawableIndex Drawable index
     * @return Drawable ID
     */
    public CubismId getDrawableId(int drawableIndex) {
        assert (0 <= drawableIndex && drawableIndex < drawableIds.size());
        return drawableIds.get(drawableIndex);
    }

    /**
     * Get the texture index of Drawable
     *
     * @param drawableIndex Drawable index
     * @return the texture index of Drawable
     */
    public int getDrawableTextureIndex(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getTextureIndex();
    }

    /**
     * Get the vertex index list of Drawable.
     *
     * @param drawableIndex Drawable index
     * @return the vertex index list of Drawable
     */
    public short[] getDrawableVertexIndices(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getIndices();
    }

    /**
     * Get the number of the vertex indices in Drawable.
     *
     * @param drawableIndex Drawable index
     * @return the number of vertex indices in Drawable
     */
    public int getDrawableVertexIndexCount(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getIndices().length;
    }

    /**
     * Get the number of the vertex in Drawable.
     *
     * @param drawableIndex Drawable index
     * @return the number of vertex in Drawable
     */
    public int getDrawableVertexCount(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getVertexCount();
    }

    /**
     * Get the vertex list of Drawable
     *
     * @param drawableIndex Drawable index
     * @return the vertex list of Drawable
     */
    public float[] getDrawableVertices(int drawableIndex) {
        return getDrawableVertexPositions(drawableIndex);
    }

    /**
     * Get the vertex positions list of Drawable
     *
     * @param drawableIndex Drawable index
     * @return the vertex positions list of Drawable
     */
    public float[] getDrawableVertexPositions(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getVertexPositions();
    }

    /**
     * Get the vertex UV list of Drawable.
     *
     * @param drawableIndex Drawable index
     * @return the vertex UV list of Drawable
     */
    public float[] getDrawableVertexUvs(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getVertexUvs();
    }

    /**
     * Get the opacity of Drawable.
     *
     * @param drawableIndex Drawable index
     * @return the Drawable opacity
     */
    public float getDrawableOpacity(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getOpacity();
    }

    /**
     * Get the multiply color of the drawable.
     *
     * @param drawableIndex index of the drawable
     * @return multiply color of the drawable
     */
    public float[] getDrawableMultiplyColor(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getMultiplyColors();
    }

    /**
     * Get the screen color of the drawable.
     *
     * @param drawableIndex index of the drawable
     * @return screen color of the drawable
     */
    public float[] getDrawableScreenColor(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getScreenColors();
    }

    /**
     * Return the index of the Drawable's parent part.
     *
     * @param drawableIndex the drawable index
     * @return the index of the Drawable's parent part
     */
    public int getDrawableParentPartIndex(int drawableIndex) {
        return model.getDrawableViews()[drawableIndex].getParentPartIndex();
    }

    /**
     * Get the blend mode of Drawable.
     *
     * @param drawableIndex Drawable index
     * @return the blend mode of Drawable
     */
    public CubismBlendMode getDrawableBlendMode(int drawableIndex) {
        final byte constantFlag = model.getDrawableViews()[drawableIndex].getConstantFlag();
        return isBitSet(constantFlag, CubismDrawableFlag.ConstantFlag.BLEND_ADDITIVE)
               ? CubismBlendMode.ADDITIVE
            : isBitSet(constantFlag, CubismDrawableFlag.ConstantFlag.BLEND_MULTIPLICATIVE)
                 ? CubismBlendMode.MULTIPLICATIVE
                 : CubismBlendMode.NORMAL;
    }

    /**
     * Returns the blend mode of the drawable.
     *
     * @param drawableIndex drawable index
     * @return blend mode of the drawable
     */
    public csmBlendMode getDrawableBlendModeType(int drawableIndex) {
        // NOTE:
        //  事前に作成したListから該当するインスタンスを取得して値を更新して返す。
        //  これにより毎フレームのnewを回避しつつ、Drawable間でのインスタンス共有による事故を防ぐ。
        final csmBlendMode blendMode = drawableBlendModeTypes.get(drawableIndex);

        blendMode.setBlendMode(
            model.getDrawableViews()[drawableIndex].getBlendMode()
        );

        return blendMode;
    }

    /**
     * Get Drawable's invert setting when mask is used.
     * If mask is not used, nothing happens.
     *
     * @param drawableIndex Drawable index
     * @return the invert setting of Drawable's mask
     */
    public boolean getDrawableInvertedMask(int drawableIndex) {
        final byte constantFlag = model.getDrawableViews()[drawableIndex].getConstantFlag();

        return isBitSet(constantFlag, CubismDrawableFlag.ConstantFlag.IS_INVERTED_MASK);
    }

    /**
     * Get the visible information of Drawable.
     *
     * @param drawableIndex Drawable index
     * @return Drawable is visible, return true
     */
    public boolean getDrawableDynamicFlagIsVisible(int drawableIndex) {
        final byte dynamicFlag = model.getDrawableViews()[drawableIndex].getDynamicFlag();
        return isBitSet(dynamicFlag, CubismDrawableFlag.DynamicFlag.IS_VISIBLE);
    }

    /**
     * In recent {@link CubismModel#update()}, if the visible state is changed, return true.
     *
     * @param drawableIndex Drawable index
     * @return If the visible state is changed, return true
     */
    public boolean getDrawableDynamicFlagVisibilityDidChange(int drawableIndex) {
        final byte dynamicFlag = model.getDrawableViews()[drawableIndex].getDynamicFlag();
        return isBitSet(dynamicFlag, CubismDrawableFlag.DynamicFlag.VISIBILITY_DID_CHANGE);
    }

    /**
     * In recent {@link CubismModel#update()}, if the opacity of drawable is changed, return true.
     *
     * @param drawableIndex Drawable index
     * @return If the opacity of drawable is changed, return true
     */
    public boolean getDrawableDynamicFlagOpacityDidChange(int drawableIndex) {
        final byte dynamicFlag = model.getDrawableViews()[drawableIndex].getDynamicFlag();
        return isBitSet(dynamicFlag, CubismDrawableFlag.DynamicFlag.OPACITY_DID_CHANGE);
    }

    /**
     * In recent {@link CubismModel#update()}, if DrawOrder of Drawable is changed, return true.
     * DrawOrder is the information from 0 to 1000 specified in ArtMesh.
     *
     * @param drawableIndex Drawable index
     * @return If DrawOrder of Drawable is changed, return true
     */
    public boolean getDrawableDynamicFlagDrawOrderDidChange(int drawableIndex) {
        final byte dynamicFlag = model.getDrawableViews()[drawableIndex].getDynamicFlag();
        return isBitSet(dynamicFlag, CubismDrawableFlag.DynamicFlag.DRAW_ORDER_DID_CHANGE);
    }

    /**
     * In recent {@link CubismModel#update()}, if the drawing order of Drawable is changed, return true.
     *
     * @param drawableIndex Drawable index
     * @return If the drawing order of Drawable is changed, return true
     */
    public boolean getDrawableDynamicFlagRenderOrderDidChange(int drawableIndex) {
        final byte dynamicFlag = model.getDrawableViews()[drawableIndex].getDynamicFlag();
        return isBitSet(dynamicFlag, CubismDrawableFlag.DynamicFlag.RENDER_ORDER_DID_CHANGE);
    }

    /**
     * In recent {@link CubismModel#update()}, if the vertex information of Drawable is changed, return true.
     *
     * @param drawableIndex Drawable index
     * @return If the vertex information of Drawable is changed, return true
     */
    public boolean getDrawableDynamicFlagVertexPositionsDidChange(int drawableIndex) {
        final byte dynamicFlag = model.getDrawableViews()[drawableIndex].getDynamicFlag();
        return isBitSet(dynamicFlag, CubismDrawableFlag.DynamicFlag.VERTEX_POSITIONS_DID_CHANGE);
    }

    /**
     * Get whether the last CubismModel.update() method changed the Drawable's multiply and screen colors.
     *
     * @param drawableIndex index of the drawable
     * @return Whether the drawable's multiply and screen color is changed in the last CubismModel.update() method
     */
    public boolean getDrawableDynamicFlagBlendColorDidChange(int drawableIndex) {
        final byte dynamicFlag = model.getDrawableViews()[drawableIndex].getDynamicFlag();
        return isBitSet(dynamicFlag, CubismDrawableFlag.DynamicFlag.BLEND_COLOR_DID_CHANGE);
    }

    /**
     * Get the clipping mask list of Drawable.
     *
     * @return the clipping mask list of Drawable
     */
    public int[][] getDrawableMasks() {
        final CubismDrawableView[] drawableViews = model.getDrawableViews();
        assert drawableViews != null;

        if (drawableViews.length > 0) {
            return drawableViews[0].getDrawables().getMasks();
        } else {
            return new int[0][0];
        }
    }

    /**
     * Get the number list of the clippng mask in Drawable.
     *
     * @return the number list of the clipping mask in Drawable
     */
    public int[] getDrawableMaskCounts() {
        final CubismDrawableView[] drawableViews = model.getDrawableViews();
        assert drawableViews != null;

        if (drawableViews.length > 0) {
            return drawableViews[0].getDrawables().getMaskCounts();
        } else {
            return new int[0];
        }
    }

    //========================================================
    //  Offscreen Functions.
    //========================================================

    /**
     * Returns the blend mode of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @return blend mode of the offscreen
     */
    public csmBlendMode getOffscreenBlendModeType(int offscreenIndex) {
        // NOTE:
        //  事前に作成したListから該当するインスタンスを取得して値を更新して返す。
        //  これにより毎フレームのnewを回避しつつ、Offscreen間でのインスタンス共有による事故を防ぐ。
        csmBlendMode blendMode = offscreenBlendModeTypes.get(offscreenIndex);

        final int[] blendModes = model.getOffscreenRendering().getBlendModes();
        if (blendModes != null) {
            blendMode.setBlendMode(blendModes[offscreenIndex]);
        }

        return blendMode;
    }

    /**
     * Returns the number of offscreens.
     *
     * @return number of offscreens
     */
    public int getOffscreenCount() {
        final int offscreenCount = model.getOffscreenRendering().getCount();
        return offscreenCount;
    }

    /**
     * Returns the array of clipping masks of the offscreens.
     *
     * @return array of clipping masks of the offscreens
     */
    public int[][] getOffscreenMasks() {
        final int[][] masks = model.getOffscreenRendering().getMasks();
        return masks;
    }

    /**
     * Returns the array of the number of clipping masks of the offscreens.
     *
     * @return array of the number of clipping masks of the offscreens
     */
    public int[] getOffscreenMaskCounts() {
        final int[] maskCounts = model.getOffscreenRendering().getMaskCounts();
        return maskCounts;
    }

    /**
     * Returns the array of owner indices for the offscreen.
     *
     * @return array of owner indices for the offscreen
     */
    public int[] getOffscreenOwnerIndices() {
        final int[] ownerIndices = model.getOffscreenRendering().getOwnerIndices();
        return ownerIndices;
    }

    /**
     * Returns the ID of the offscreen owner.
     *
     * @param offscreenIndex index of the offscreen
     * @return owner ID
     */
    public CubismId getOffscreenOwnerId(int offscreenIndex) {
        final int ownerIndex = model.getOffscreenRendering().getOwnerIndices()[offscreenIndex];
        return CubismFramework.getIdManager().getId(model.getParts().getIds()[ownerIndex]);
    }

    /**
     * Returns the multiply color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @return multiply color of the offscreen
     */
    public float[] getOffscreenMultiplyColor(int offscreenIndex) {
        final float[][] offscreenColors = model.getOffscreenRendering().getMultiplyColors();
        return offscreenColors[offscreenIndex];
    }

    /**
     * Returns the screen color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @return screen color of the offscreen
     */
    public float[] getOffscreenScreenColor(int offscreenIndex) {
        final float[][] offscreenColors = model.getOffscreenRendering().getScreenColors();
        return offscreenColors[offscreenIndex];
    }

    /**
     * Returns the inverted mask setting for the offscreen.
     * <p>
     * Ignored if the mask is not used.
     *
     * @param offscreenIndex Offscreen index
     * @return Inverted mask setting of the offscreen. true if inverted.
     */
    public boolean getOffscreenInvertedMask(int offscreenIndex) {
        final byte[] constantFlags = model.getOffscreenRendering().getConstantFlags();
        return isBitSet(constantFlags[offscreenIndex], CubismDrawableFlag.ConstantFlag.IS_INVERTED_MASK);
    }

    /**
     * Returns the opacity of the Offscreen.
     *
     * @param offscreenIndex offscreen index
     * @return offscreen opacity
     */
    public float getOffscreenOpacity(int offscreenIndex) {
        if (offscreenIndex < 0 || offscreenIndex >= model.getOffscreenRendering().getCount()) {
            return 1.0f;    // オフスクリーンが無いのでスキップ
        }

        return model.getOffscreenRendering().getOpacities()[offscreenIndex];
    }

    //========================================================
    //  Other Functions.
    //========================================================

    /**
     * Checks whether the model uses clipping masks.
     *
     * @return true if the model uses clipping masks.
     */
    public boolean isUsingMasking() {
        final CubismDrawableView[] drawableViews = model.getDrawableViews();
        assert drawableViews != null;

        if (drawableViews.length > 0) {
            final int drawableCount = drawableViews.length;

            for (int i = 0; i < drawableCount; ++i) {
                final int[] drawableMaskCounts = getDrawableMaskCounts();
                if (drawableMaskCounts != null && drawableMaskCounts[i] <= 0) {
                    continue;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether the offscreen uses clipping masks.
     *
     * @return true if the offscreen uses clipping masks.
     */
    public boolean isUsingMaskingForOffscreen() {
        int offscreenCount = model.getOffscreenRendering().getCount();
        int[] offscreenMaskCounts = model.getOffscreenRendering().getMaskCounts();

        for (int i = 0; i < offscreenCount; i++) {
            if (offscreenMaskCounts[i] <= 0) {
                continue;
            }
            return true;
        }

        return false;
    }

    /**
     * Read the saved parameters.
     */
    public void loadParameters() {
        int parameterCount = getParameterCount();
        final int savedParameterCount = savedParameters.length;

        if (parameterCount > savedParameterCount) {
            parameterCount = savedParameterCount;
        }

        for (int i = 0; i < parameterCount; ++i) {
            parameterValues[i].setValue(savedParameters[i]);
        }
    }

    /**
     * Save the parameters.
     */
    public void saveParameters() {
        final int parameterCount = getParameterCount();

        if (savedParameters.length < parameterCount) {
            savedParameters = new float[parameterCount];
        }
        for (int i = 0; i < parameterCount; i++) {
            savedParameters[i] = parameterValues[i].getValue();
        }
    }

    //========================================================
    //  Color Functions.
    //========================================================

    /**
     * Get the multiply color from the list.
     *
     * @param drawableIndex index of the drawable
     * @return the multiply color
     */
    public CubismRenderer.CubismTextureColor getMultiplyColor(int drawableIndex) {
        if (getOverrideFlagForModelMultiplyColors() || getOverrideFlagForDrawableMultiplyColors(drawableIndex)) {
            return userDrawableMultiplyColors.get(drawableIndex).color;
        }

        float[] color = getDrawableMultiplyColor(drawableIndex);
        multiplyColor.r = color[0];
        multiplyColor.g = color[1];
        multiplyColor.b = color[2];
        multiplyColor.a = color[3];

        return multiplyColor;
    }

    // This is only used by 'getMultiplyColor' method.
    // Avoid creating a new CubismTextureColor instance in getter method.
    private final CubismRenderer.CubismTextureColor multiplyColor = new CubismRenderer.CubismTextureColor();

    /**
     * Get the screen color from the list.
     *
     * @param drawableIndex index of the drawable
     * @return the screen color
     */
    public CubismRenderer.CubismTextureColor getScreenColor(int drawableIndex) {
        if (getOverrideFlagForModelScreenColors() || getOverrideFlagForDrawableScreenColors(drawableIndex)) {
            return userDrawableScreenColors.get(drawableIndex).color;
        }

        float[] color = getDrawableScreenColor(drawableIndex);
        screenColor.r = color[0];
        screenColor.g = color[1];
        screenColor.b = color[2];
        screenColor.a = color[3];

        return screenColor;
    }

    // This is only used by 'getScreenColor' method.
    // Avoid creating a new CubismTextureColor instance in getter method.
    private final CubismRenderer.CubismTextureColor screenColor = new CubismRenderer.CubismTextureColor();

    /**
     * Set the multiply color of Drawable.
     *
     * @param drawableIndex index of the drawable
     * @param color the multiply color instance
     */
    public void setMultiplyColor(int drawableIndex, CubismRenderer.CubismTextureColor color) {
        setMultiplyColor(drawableIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Set the multiply color of Drawable.
     *
     * @param drawableIndex index of the drawable
     * @param r red value
     * @param g green value
     * @param b blue value
     * @param a alpha value
     */
    public void setMultiplyColor(int drawableIndex, float r, float g, float b, float a) {
        userDrawableMultiplyColors.get(drawableIndex).color.r = r;
        userDrawableMultiplyColors.get(drawableIndex).color.g = g;
        userDrawableMultiplyColors.get(drawableIndex).color.b = b;
        userDrawableMultiplyColors.get(drawableIndex).color.a = a;
    }

    /**
     * Partの乗算色を取得する。
     *
     * @param partIndex 取得したいPartのインデックス
     * @return Partの乗算色
     */
    public CubismRenderer.CubismTextureColor getPartMultiplyColor(int partIndex) {
        return userPartMultiplyColors.get(partIndex).color;
    }

    /**
     * Partのスクリーン色を取得する。
     *
     * @param partIndex 取得したいPartのインデックス
     * @return Partのスクリーン色
     */
    public CubismRenderer.CubismTextureColor getPartScreenColor(int partIndex) {
        return userPartScreenColors.get(partIndex).color;
    }

    /**
     * Partの乗算色を設定する。
     *
     * @param partIndex 乗算色を設定するパーツのインデックス
     * @param color 乗算色
     */
    public void setPartMultiplyColor(int partIndex, CubismRenderer.CubismTextureColor color) {
        setPartColor(partIndex, color.r, color.g, color.b, color.a, userPartMultiplyColors, userDrawableMultiplyColors);
    }

    /**
     * Partの乗算色を設定する。
     *
     * @param partIndex 乗算色を設定するパーツのインデックス
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @param a アルファ
     */
    public void setPartMultiplyColor(int partIndex, float r, float g, float b, float a) {
        setPartColor(partIndex, r, g, b, a, userPartMultiplyColors, userDrawableMultiplyColors);
    }

    /**
     * Set the screen color of Drawable.
     *
     * @param drawableIndex index of the drawable
     * @param color the screen color instance
     */
    public void setScreenColor(int drawableIndex, CubismRenderer.CubismTextureColor color) {
        setScreenColor(drawableIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Set the screen color of Drawable.
     *
     * @param drawableIndex index of the drawable
     * @param r red value
     * @param g green value
     * @param b blue value
     * @param a alpha value
     */
    public void setScreenColor(int drawableIndex, float r, float g, float b, float a) {
        userDrawableScreenColors.get(drawableIndex).color.r = r;
        userDrawableScreenColors.get(drawableIndex).color.g = g;
        userDrawableScreenColors.get(drawableIndex).color.b = b;
        userDrawableScreenColors.get(drawableIndex).color.a = a;
    }

    /**
     * Partのスクリーン色を設定する。
     *
     * @param partIndex スクリーン色を設定するパーツのインデックス
     * @param color スクリーン色
     */
    public void setPartScreenColor(int partIndex, CubismRenderer.CubismTextureColor color) {
        setPartScreenColor(partIndex, color.r, color.g, color.b, color.a);
    }

    /**
     * Partのスクリーン色を設定する。
     *
     * @param partIndex スクリーン色を設定するパーツのインデックス
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @param a アルファ
     */
    public void setPartScreenColor(int partIndex, float r, float g, float b, float a) {
        setPartColor(partIndex, r, g, b, a, userPartScreenColors, userDrawableScreenColors);
    }

    /**
     * Returns the multiply color from the list of offscreen.
     * <p>
     * NOTE: This method returns a cached instance for optimization purposes.
     *
     * @param offscreenIndex offscreen index
     * @return multiply color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getMultiplyColorOffscreen(int offscreenIndex) {
        if (getOverrideFlagForModelMultiplyColors() || getOverrideFlagForOffscreenMultiplyColors(offscreenIndex)) {
            return userOffscreenMultiplyColors.get(offscreenIndex).color;
        }

        final float[] tmpColor = getOffscreenMultiplyColor(offscreenIndex);

        // インスタンス生成を避けるため、キャッシュリストからインスタンスを取得して値を更新
        CubismRenderer.CubismTextureColor color = offscreenMultiplyColors.get(offscreenIndex);
        color.r = tmpColor[0];
        color.g = tmpColor[1];
        color.b = tmpColor[2];
        color.a = tmpColor[3];

        return color;
    }

    /**
     * Returns the screen color from the list of offscreen.
     * <p>
     * NOTE: This method returns a cached instance for optimization purposes.
     *
     * @param offscreenIndex offscreen index
     * @return screen color (CubismTextureColor)
     */
    public CubismRenderer.CubismTextureColor getScreenColorOffscreen(int offscreenIndex) {
        if (getOverrideFlagForModelScreenColors() || getOverrideFlagForOffscreenScreenColors(offscreenIndex)) {
            return userOffscreenScreenColors.get(offscreenIndex).color;
        }

        final float[] tmpColor = getOffscreenScreenColor(offscreenIndex);

        // インスタンス生成を避けるため、キャッシュリストからインスタンスを取得して値を更新
        CubismRenderer.CubismTextureColor color = offscreenScreenColors.get(offscreenIndex);
        color.r = tmpColor[0];
        color.g = tmpColor[1];
        color.b = tmpColor[2];
        color.a = tmpColor[3];

        return color;
    }

    /**
     * Sets the multiply color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param color          multiply color to be set (CubismTextureColor)
     */
    public void setMultiplyColorOffscreen(int offscreenIndex, final CubismRenderer.CubismTextureColor color) {
        setMultiplyColorOffscreen(offscreenIndex, color.r, color.g, color.b, color.a);
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
    public void setMultiplyColorOffscreen(
        int offscreenIndex,
        float r,
        float g,
        float b,
        float a
    ) {
        userOffscreenMultiplyColors.get(offscreenIndex).color.r = r;
        userOffscreenMultiplyColors.get(offscreenIndex).color.g = g;
        userOffscreenMultiplyColors.get(offscreenIndex).color.b = b;
        userOffscreenMultiplyColors.get(offscreenIndex).color.a = a;
    }

    /**
     * Sets the multiply color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param r              red value of the multiply color to be set
     * @param g              green value of the multiply color to be set
     * @param b              blue value of the multiply color to be set
     */
    public void setMultiplyColorOffscreen(
        int offscreenIndex,
        float r,
        float g,
        float b
    ) {
        setMultiplyColorOffscreen(offscreenIndex, r, g, b, 1.0f);
    }

    /**
     * Sets the screen color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param color          screen color to be set (CubismTextureColor)
     */
    public void setScreenColorOffscreen(int offscreenIndex, final CubismRenderer.CubismTextureColor color) {
        setScreenColorOffscreen(offscreenIndex, color.r, color.g, color.b, color.a);
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
    public void setScreenColorOffscreen(
        int offscreenIndex,
        float r,
        float g,
        float b,
        float a
    ) {
        userOffscreenScreenColors.get(offscreenIndex).color.r = r;
        userOffscreenScreenColors.get(offscreenIndex).color.g = g;
        userOffscreenScreenColors.get(offscreenIndex).color.b = b;
        userOffscreenScreenColors.get(offscreenIndex).color.a = a;
    }

    /**
     * Sets the screen color of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param r              red value of the screen color to be set
     * @param g              green value of the screen color to be set
     * @param b              blue value of the screen color to be set
     */
    public void setScreenColorOffscreen(
        int offscreenIndex,
        float r,
        float g,
        float b
    ) {
        setScreenColorOffscreen(offscreenIndex, r, g, b, 1.0f);
    }


    /**
     * Whether to override the entire model multiply color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use getOverrideFlagForModelMultiplyColors() instead.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverwriteFlagForModelMultiplyColors() {
        CubismDebug.cubismLogWarning("getOverwriteFlagForModelMultiplyColors() is a deprecated function. Please use getOverrideFlagForModelMultiplyColors().");
        return getOverrideFlagForModelMultiplyColors();
    }

    /**
     * Whether to override the entire model multiply color from the SDK.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverrideFlagForModelMultiplyColors() {
        return isOverriddenModelMultiplyColors;
    }

    /**
     * Whether to override the entire model screen color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use getOverrideFlagForModelScreenColors() instead.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverwriteFlagForModelScreenColors() {
        CubismDebug.cubismLogWarning("getOverwriteFlagForModelScreenColors() is a deprecated function. Please use getOverrideFlagForModelScreenColors().");
        return getOverrideFlagForModelScreenColors();
    }

    /**
     * Whether to override the entire model screen color from the SDK.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverrideFlagForModelScreenColors() {
        return isOverriddenModelScreenColors;
    }

    /**
     * Set the flag whether to override the entire model multiply color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideFlagForModelMultiplyColors(boolean value) instead.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverwriteFlagForModelMultiplyColors(boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteFlagForModelMultiplyColors(boolean value) is a deprecated function. Please use setOverrideFlagForModelMultiplyColors(boolean value).");
        setOverrideFlagForModelMultiplyColors(value);
    }

    /**
     * Set the flag whether to override the entire model multiply color from the SDK.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverrideFlagForModelMultiplyColors(boolean value) {
        isOverriddenModelMultiplyColors = value;
    }

    /**
     * Set the flag whether to override the entire model screen color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideFlagForModelScreenColors(boolean value) instead.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverwriteFlagForModelScreenColors(boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteFlagForModelScreenColors(boolean value) is a deprecated function. Please use setOverrideFlagForModelScreenColors(boolean value).");
        setOverrideFlagForModelScreenColors(value);
    }

    /**
     * Set the flag whether to override the entire model screen color from the SDK.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverrideFlagForModelScreenColors(boolean value) {
        isOverriddenModelScreenColors = value;
    }

    /**
     * Whether to override the drawable multiply color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use getOverrideFlagForDrawableMultiplyColors(int drawableIndex) instead.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverwriteFlagForDrawableMultiplyColors(int drawableIndex) {
        CubismDebug.cubismLogWarning("getOverwriteFlagForDrawableMultiplyColors(int drawableIndex) is a deprecated function. Please use getOverrideFlagForDrawableMultiplyColors(int drawableIndex).");
        return getOverrideFlagForDrawableMultiplyColors(drawableIndex);
    }

    /**
     * Whether to override the drawable multiply color from the SDK.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverrideFlagForDrawableMultiplyColors(int drawableIndex) {
        return userDrawableMultiplyColors.get(drawableIndex).isOverridden;
    }

    /**
     * Whether to override the drawable screen color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use getOverrideFlagForDrawableScreenColors(int drawableIndex) instead.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverwriteFlagForDrawableScreenColors(int drawableIndex) {
        CubismDebug.cubismLogWarning("getOverwriteFlagForDrawableScreenColors(int drawableIndex) is a deprecated function. Please use getOverrideFlagForDrawableScreenColors(int drawableIndex).");
        return getOverrideFlagForDrawableScreenColors(drawableIndex);
    }

    /**
     * Whether to override the drawable screen color from the SDK.
     *
     * @return If the color information on the SDK is used, return true. If the color information of the model is used, return false.
     */
    public boolean getOverrideFlagForDrawableScreenColors(int drawableIndex) {
        return userDrawableScreenColors.get(drawableIndex).isOverridden;
    }

    /**
     * SDKからPartの乗算色を上書きするかどうかのフラグを取得する。
     *
     * @deprecated This function is deprecated due to a naming change, use getOverrideColorForPartMultiplyColors(int partIndex) instead.
     *
     * @param partIndex 上書きするPartのインデックス
     * @return SDKからPartの乗算色を上書きするか。上書きするならtrue。
     */
    public boolean getOverwriteColorForPartMultiplyColors(int partIndex) {
        CubismDebug.cubismLogWarning("getOverwriteColorForPartMultiplyColors(int partIndex) is a deprecated function. Please use getOverrideColorForPartMultiplyColors(int partIndex).");
        return getOverrideColorForPartMultiplyColors(partIndex);
    }

    /**
     * SDKからPartの乗算色を上書きするかどうかのフラグを取得する。
     *
     * @param partIndex 上書きするPartのインデックス
     * @return SDKからPartの乗算色を上書きするか。上書きするならtrue。
     */
    public boolean getOverrideColorForPartMultiplyColors(int partIndex) {
        return userPartMultiplyColors.get(partIndex).isOverridden;
    }

    /**
     * SDKからPartのスクリーン色を上書きするかどうかのフラグを取得する。
     *
     * @deprecated This function is deprecated due to a naming change, use  getOverrideColorForPartScreenColors(int partIndex) instead.
     *
     * @param partIndex 上書きするPartのインデックス
     * @return SDKからPartのスクリーン色を上書きするか。上書きするならtrue。
     */
    public boolean getOverwriteColorForPartScreenColors(int partIndex) {
        CubismDebug.cubismLogWarning("getOverwriteColorForPartScreenColors(int partIndex) is a deprecated function. Please use getOverrideColorForPartScreenColors(int partIndex).");
        return getOverrideColorForPartScreenColors(partIndex);
    }

    /**
     * SDKからPartのスクリーン色を上書きするかどうかのフラグを取得する。
     *
     * @param partIndex 上書きするPartのインデックス
     * @return SDKからPartのスクリーン色を上書きするか。上書きするならtrue。
     */
    public boolean getOverrideColorForPartScreenColors(int partIndex) {
        return userPartScreenColors.get(partIndex).isOverridden;
    }

    /**
     * Set the flag whether to override the drawable multiply color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideFlagForDrawableMultiplyColors(int drawableIndex, boolean value) instead.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverwriteFlagForDrawableMultiplyColors(int drawableIndex, boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteFlagForDrawableMultiplyColors(int drawableIndex, boolean value) is a deprecated function. Please use setOverrideFlagForDrawableMultiplyColors(int drawableIndex, boolean value).");
        setOverrideFlagForDrawableMultiplyColors(drawableIndex, value);
    }

    /**
     * Set the flag whether to override the drawable multiply color from the SDK.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverrideFlagForDrawableMultiplyColors(int drawableIndex, boolean value) {
        userDrawableMultiplyColors.get(drawableIndex).isOverridden = value;
    }

    /**
     * Set the flag whether to override the drawable screen color from the SDK.
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideFlagForDrawableScreenColors(int drawableIndex, boolean value) instead.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverwriteFlagForDrawableScreenColors(int drawableIndex, boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteFlagForDrawableScreenColors(int drawableIndex, boolean value) is a deprecated function. Please use setOverrideFlagForDrawableScreenColors(int drawableIndex, boolean value).");
        setOverrideFlagForDrawableScreenColors(drawableIndex, value);
    }

    /**
     * Set the flag whether to override the drawable screen color from the SDK.
     *
     * @param value If the color information on the SDK is used, this value is true. If the color information of the model is used, this is false.
     */
    public void setOverrideFlagForDrawableScreenColors(int drawableIndex, boolean value) {
        userDrawableScreenColors.get(drawableIndex).isOverridden = value;
    }

    /**
     * SDKからPartの乗算色を上書きするかどうかのフラグを設定する。
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideColorForPartMultiplyColors(int partIndex, boolean value) instead.
     *
     * @param partIndex 上書きするPartのインデックス
     * @param value SDKからPartの乗算色を上書きするかどうか。trueなら上書きする。
     */
    public void setOverwriteColorForPartMultiplyColors(int partIndex, boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteColorForPartMultiplyColors(int partIndex, boolean value) is a deprecated function. Please use setOverrideColorForPartMultiplyColors(int partIndex, boolean value).");
        setOverrideColorForPartMultiplyColors(partIndex, value);
    }

    /**
     * SDKからPartの乗算色を上書きするかどうかのフラグを設定する。
     *
     * @param partIndex 上書きするPartのインデックス
     * @param value SDKからPartの乗算色を上書きするかどうか。trueなら上書きする。
     */
    public void setOverrideColorForPartMultiplyColors(int partIndex, boolean value) {
        userPartMultiplyColors.get(partIndex).isOverridden = value;
        setOverrideColorsForPartColors(partIndex, value, userPartMultiplyColors, userDrawableMultiplyColors);
    }

    /**
     * SDKからPartのスクリーン色を上書きするかどうかのフラグを設定する。
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideColorForPartScreenColors(int partIndex, boolean value) instead.
     *
     * @param partIndex 上書きするPartのインデックス
     * @param value SDKからPartのスクリーン色を上書きするかどうか。trueなら上書きする。
     */
    public void setOverwriteColorForPartScreenColors(int partIndex, boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteColorForPartScreenColors(int partIndex, boolean value) is a deprecated function. Please use setOverrideColorForPartScreenColors(int partIndex, boolean value).");
        setOverrideColorForPartScreenColors(partIndex, value);
    }

    /**
     * SDKからPartのスクリーン色を上書きするかどうかのフラグを設定する。
     *
     * @param partIndex 上書きするPartのインデックス
     * @param value SDKからPartのスクリーン色を上書きするかどうか。trueなら上書きする。
     */
    public void setOverrideColorForPartScreenColors(int partIndex, boolean value) {
        userPartScreenColors.get(partIndex).isOverridden = value;
        setOverrideColorsForPartColors(partIndex, value, userPartScreenColors, userDrawableScreenColors);
    }

    /**
     * Checks whether the offscreen multiply color is overridden by the SDK.
     *
     * @param offscreenIndex offscreen index
     * @return true if the color information from the SDK is used; otherwise false.
     */
    public boolean getOverrideFlagForOffscreenMultiplyColors(int offscreenIndex) {
        return userOffscreenMultiplyColors.get(offscreenIndex).isOverridden;
    }

    /**
     * Checks whether the offscreen screen color is overridden by the SDK.
     *
     * @param offscreenIndex offscreen index
     * @return true if the color information from the SDK is used; otherwise false.
     */
    public boolean getOverrideFlagForOffscreenScreenColors(int offscreenIndex) {
        return userOffscreenScreenColors.get(offscreenIndex).isOverridden;
    }

    /**
     * Sets whether the offscreen multiply color is overridden by the SDK.
     * Use true to use the color information from the SDK, or false to use the color information from the model.
     *
     * @param offscreenIndex offscreen index
     * @param value          offscreen True enable override, false to disable
     */
    public void setOverrideFlagForOffscreenMultiplyColors(int offscreenIndex, boolean value) {
        userOffscreenMultiplyColors.get(offscreenIndex).isOverridden = value;
    }

    /**
     * Sets whether the offscreen screen color is overridden by the SDK.
     * Use true to use the color information from the SDK, or false to use the color information from the model.
     *
     * @param offscreenIndex offscreen index
     * @param value          offscreen True enable override, false to disable
     */
    public void setOverrideFlagForOffscreenScreenColors(int offscreenIndex, boolean value) {
        userOffscreenScreenColors.get(offscreenIndex).isOverridden = value;
    }

    /**
     * Get the culling inforamtion of Drawable.
     *
     * @param drawableIndex Drawable index
     * @return the culling inforamtion of Drawable
     */
    public boolean getDrawableCulling(int drawableIndex) {
        if (getOverrideFlagForModelCullings() || getOverrideFlagForDrawableCullings(drawableIndex)) {
            return userDrawableCullings.get(drawableIndex).isCulling;
        }

        final byte constantFlag = model.getDrawableViews()[drawableIndex].getConstantFlag();
        return !isBitSet(constantFlag, CubismDrawableFlag.ConstantFlag.IS_DOUBLE_SIDED);
    }

    /**
     * Drawableのカリング情報を設定する
     *
     * @param drawableIndex drawableのインデックス
     * @param isCulling カリングするかどうか
     */
    public void setDrawableCulling(int drawableIndex, boolean isCulling) {
        userDrawableCullings.get(drawableIndex).isCulling = isCulling;
    }

    /**
     * Returns the culling information of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @return culling information of the offscreen
     */
    public boolean getOffscreenCulling(int offscreenIndex) {
        if (getOverrideFlagForModelCullings() || getOverrideFlagForOffscreenCullings(offscreenIndex)) {
            return userOffscreenCullings.get(offscreenIndex).isCulling;
        }

        final byte[] constantFlags = model.getOffscreenRendering().getConstantFlags();

        // NOTE: Offscreenだが、CoreJavaの定義にDrawableFlagしかないためこれを使用している。
        return !isBitSet(constantFlags[offscreenIndex], CubismDrawableFlag.ConstantFlag.IS_DOUBLE_SIDED);
    }

    /**
     * Sets the culling information of the offscreen.
     *
     * @param offscreenIndex offscreen index
     * @param isCulling      true enable culling, false to disable
     */
    public void setOffscreenCulling(int offscreenIndex, boolean isCulling) {
        userOffscreenCullings.get(offscreenIndex).isCulling = isCulling;
    }

    /**
     * Checks whether parameter repetition is performed for the entire model.
     *
     * @return true if parameter repetition is performed for the entire model; otherwise returns false.
     */
    public boolean getOverrideFlagForModelParameterRepeat() {
        return isOverriddenParameterRepeat;
    }

    /**
     * Sets whether parameter repetition is performed for the entire model.
     * Use true to perform parameter repetition for the entire model, or false to not perform it.
     */
    public void setOverrideFlagForModelParameterRepeat(boolean isRepeat) {
        isOverriddenParameterRepeat = isRepeat;
    }

    /**
     * Sets the flag indicating whether to override the parameter repeat.
     *
     * @param parameterIndex Parameter index
     * @param value true if it is to be overridden; otherwise, false.
     */
    public void setOverrideFlagForParameterRepeat(int parameterIndex, boolean value) {
        this.userParameterRepeatDataList.get(parameterIndex).isOverridden = value;
    }

    /**
     * Returns the repeat flag.
     *
     * @param parameterIndex Parameter index
     *
     * @return true if repeating, false otherwise.
     */
    public boolean getRepeatFlagForParameterRepeat(int parameterIndex) {
        return this.userParameterRepeatDataList.get(parameterIndex).isParameterRepeated;
    }

    /**
     * Sets the repeat flag.
     *
     * @param parameterIndex Parameter index
     * @param value true to enable repeating, false otherwise.
     */
    public void setRepeatFlagForParameterRepeat(int parameterIndex, boolean value) {
        this.userParameterRepeatDataList.get(parameterIndex).isParameterRepeated = value;
    }

    /**
     * SDKからモデル全体のカリング設定を上書きするか
     *
     * @deprecated This function is deprecated due to a naming change, use getOverrideFlagForModelCullings() instead.
     *
     * @return trueならSDK上のカリング設定を使用し、falseならモデルのカリング設定を使用する
     */
    public boolean getOverwriteFlagForModelCullings() {
        CubismDebug.cubismLogWarning("getOverwriteFlagForModelCullings() is a deprecated function. Please use getOverrideFlagForModelCullings().");
        return getOverrideFlagForModelCullings();
    }

    /**
     * SDKからモデル全体のカリング設定を上書きするか
     *
     * @return trueならSDK上のカリング設定を使用し、falseならモデルのカリング設定を使用する
     */
    public boolean getOverrideFlagForModelCullings() {
        return isOverriddenCullings;
    }

    /**
     * SDK上からモデル全体のカリング設定を上書きするかをセットする
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideFlagForModelCullings(boolean value) instead.
     *
     * @param value SDK上のカリング設定を使うならtrue, モデルのカリング設定を使うならfalse
     */
    public void setOverwriteFlagForModelCullings(boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteFlagForModelCullings(boolean value) is a deprecated function. Please use setOverrideFlagForModelCullings(boolean value).");
        setOverrideFlagForModelCullings(value);
    }

    /**
     * SDK上からモデル全体のカリング設定を上書きするかをセットする
     *
     * @param value SDK上のカリング設定を使うならtrue, モデルのカリング設定を使うならfalse
     */
    public void setOverrideFlagForModelCullings(boolean value) {
        isOverriddenCullings = value;
    }

    /**
     * SDKからdrawableのカリング設定を上書きするか
     *
     * @deprecated This function is deprecated due to a naming change, use getOverrideFlagForDrawableCullings(int drawableIndex) instead.
     *
     * @param drawableIndex drawableのインデックス
     * @return trueならSDK上のカリング設定を使用し、falseならモデルのカリング設定を使用する
     */
    public boolean getOverwriteFlagForDrawableCullings(int drawableIndex) {
        CubismDebug.cubismLogWarning("getOverwriteFlagForDrawableCullings(int drawableIndex) is a deprecated function. Please use getOverrideFlagForDrawableCullings(int drawableIndex).");
        return getOverrideFlagForDrawableCullings(drawableIndex);
    }

    /**
     * SDKからdrawableのカリング設定を上書きするか
     *
     * @param drawableIndex drawableのインデックス
     * @return trueならSDK上のカリング設定を使用し、falseならモデルのカリング設定を使用する
     */
    public boolean getOverrideFlagForDrawableCullings(int drawableIndex) {
        return userDrawableCullings.get(drawableIndex).isOverridden;
    }

    /**
     * SDKからdrawableのカリング設定を上書きするかをセットする
     *
     * @deprecated This function is deprecated due to a naming change, use setOverrideFlagForDrawableCullings(int drawableIndex, boolean value) instead.
     *
     * @param drawableIndex drawableのインデックス
     * @param value SDK上のカリング設定を使うならtrue, モデルのカリング設定を使うならfalse
     */
    public void setOverwriteFlagForDrawableCullings(int drawableIndex, boolean value) {
        CubismDebug.cubismLogWarning("setOverwriteFlagForDrawableCullings(int drawableIndex, boolean value) is a deprecated function. Please use setOverrideFlagForDrawableCullings(int drawableIndex, boolean value).");
        setOverrideFlagForDrawableCullings(drawableIndex, value);
    }

    /**
     * SDKからdrawableのカリング設定を上書きするかをセットする
     *
     * @param drawableIndex drawableのインデックス
     * @param value SDK上のカリング設定を使うならtrue, モデルのカリング設定を使うならfalse
     */
    public void setOverrideFlagForDrawableCullings(int drawableIndex, boolean value) {
        userDrawableCullings.get(drawableIndex).isOverridden = value;
    }

    /**
     * Checks whether the culling settings for the offscreen are overridden by the SDK.
     *
     * @param offscreenIndex offscreen index
     * @return true if the culling settings from the SDK are used; otherwise false.
     */
    public boolean getOverrideFlagForOffscreenCullings(int offscreenIndex) {
        return userOffscreenCullings.get(offscreenIndex).isOverridden;
    }

    /**
     * Sets whether the culling settings for the offscreen are overridden by the SDK.
     * Use true to use the culling settings from the SDK, or false to use the culling settings from the model.
     *
     * @param offscreenIndex offscreen index
     * @param value          true to use the override, false to keep the model's own culling settings
     */
    public void setOverrideFlagForOffscreenCullings(int offscreenIndex, boolean value) {
        userOffscreenCullings.get(offscreenIndex).isOverridden = value;
    }

    /**
     * Determines whether the drawable should be rendered with a blend mode.
     *
     * @return true if a blend mode is applied; otherwise, false.
     */
    public boolean isBlendModeEnabled() {
        return isBlendModeEnabled;
    }

    /**
     * モデルの不透明度を取得する。
     *
     * @return 不透明度の値
     */
    public float getModelOpacity() {
        return modelOpacity;
    }

    /**
     * モデルの不透明度を設定する。
     *
     * @param value 不透明度の値
     */
    public void setModelOpacity(float value) {
        modelOpacity = value;
    }

    /**
     * Get the model.
     *
     * @return model
     */
    public com.live2d.sdk.cubism.core.CubismModel getModel() {
        return model;
    }

    /**
     * Close the CubismModel instance.
     */
    public void close() {
        if (model != null) {
            model.close();
            model.getMoc().close();
        }
    }

    /**
     * Initialize the model.
     */
    void initialize() {
        assert model != null;

        parameterValues = model.getParameterViews();
        partValues = model.getPartViews();

        // Set parameter IDs to _parameterIds.
        for (CubismParameterView parameterValue : parameterValues) {
            String id = parameterValue.getId();

            parameterIds.add(CubismFramework.getIdManager().getId(id));
            userParameterRepeatDataList.add(new ParameterRepeatData(false, false));
        }

        // Set part IDs to _partIds.
        for (CubismPartView partValue : partValues) {
            String id = partValue.getId();

            partIds.add(CubismFramework.getIdManager().getId(id));
        }

        // Set drawable IDs to _drawableIds.
        CubismDrawableView[] drawableValues = model.getDrawableViews();

        // MultiplyColors
        CubismRenderer.CubismTextureColor multiplyColor = new CubismRenderer.CubismTextureColor(
            1.0f,
            1.0f,
            1.0f,
            1.f
        );
        ColorData userDrawableMultiplyColor = new ColorData(false, multiplyColor);
        ColorData userPartMultiplyColor = new ColorData(false, multiplyColor);

        // ScreenColors
        CubismRenderer.CubismTextureColor screenColor = new CubismRenderer.CubismTextureColor(
            0.0f,
            0.0f,
            0.0f,
            1.0f
        );
        ColorData userDrawableScreenColor = new ColorData(false, screenColor);
        ColorData userPartScreenColor = new ColorData(false, screenColor);

        // To prevent performance degradation due to capacity expansion, HashMap is generated with initial capacity reserved.
        int partCount = model.getPartViews().length;
        partChildDrawablesMap = new HashMap<Integer, List<Integer>>(partCount);

        // Setting for Drawables.
        for (CubismDrawableView drawableValue : drawableValues) {
            String id = drawableValue.getId();
            drawableIds.add(CubismFramework.getIdManager().getId(id));

            userDrawableMultiplyColors.add(new ColorData(userDrawableMultiplyColor));
            userDrawableScreenColors.add(new ColorData(userDrawableScreenColor));
            userDrawableCullings.add(new CullingData(false, false));

            drawableBlendModeTypes.add(new csmBlendMode());

            // Bind parent Parts and child Drawables.
            int parentIndex = drawableValue.getParentPartIndex();
            if (parentIndex >= 0) {
                List<Integer> childDrawables = partChildDrawablesMap.get(parentIndex);
                if (childDrawables == null) {
                    childDrawables = new ArrayList<Integer>();
                    partChildDrawablesMap.put(parentIndex, childDrawables);
                }
                childDrawables.add(drawableValue.getIndex());
            }
        }

        // Setting for Parts.
        for (int i = 0; i < partCount; i++) {
            userPartMultiplyColors.add(new ColorData(userPartMultiplyColor));
            userPartScreenColors.add(new ColorData(userPartScreenColor));
        }

        // blendMode
        initializeBlendMode();

        // Offscreen
        initializeOffscreen();

        if (isBlendModeEnabled) {
            setupPartsHierarchy();
        }
    }

    /**
     * Constructor
     *
     * @param model model instance
     */
    CubismModel(final com.live2d.sdk.cubism.core.CubismModel model) {
        this.model = model;
    }

    /**
     * Initializes blend mode settings based on offscreens and drawable blend modes.
     */
    void initializeBlendMode() {
        final int drawableCount = getDrawableCount();

        // オフスクリーンが存在するか、DrawableのブレンドモードでColorBlend、AlphaBlendを使用するのであればブレンドモードを有効にする。
        if (model.getOffscreenRenderingViews().length > 0) {
            isBlendModeEnabled = true;
        } else {
            csmBlendMode blendMode = new csmBlendMode();
            final int[] blendModes = model.getDrawables().getBlendModes();

            for (int i = 0; i < drawableCount; i++) {
                blendMode.setBlendMode(blendModes[i]);
                final CubismColorBlendType colorBlendType = blendMode.getColorBlendType();
                final CubismAlphaBlendType alphaBlendType = blendMode.getAlphaBlendType();

                // NormalOver、AddCompatible、MultiplyCompatible以外であればブレンドモードを有効にする。
                if (!(colorBlendType == CubismColorBlendType.NORMAL && alphaBlendType == CubismAlphaBlendType.OVER) &&
                    colorBlendType != CubismColorBlendType.ADD_COMPATIBLE &&
                    colorBlendType != CubismColorBlendType.MULTIPLY_COMPATIBLE) {
                    isBlendModeEnabled = true;
                    break;
                }
            }
        }
    }

    /**
     * Initializes offscreen rendering settings.
     */
    private void initializeOffscreen() {
        // 乗算色
        ColorData userMultiplyColor = new ColorData(
            false,
            new CubismRenderer.CubismTextureColor(
                1.0f,
                1.0f,
                1.0f,
                1.0f
            )
        );

        // スクリーン色
        ColorData userScreenColor = new ColorData(
            false,
            new CubismRenderer.CubismTextureColor(
                0.0f,
                0.0f,
                0.0f,
                1.0f
            )
        );

        final int offscreenCount = model.getOffscreenRendering().getCount();

        for (int i = 0; i < offscreenCount; i++) {
            userOffscreenMultiplyColors.add(new ColorData(userMultiplyColor));
            userOffscreenScreenColors.add(new ColorData(userScreenColor));
            userOffscreenCullings.add(new CullingData());

            offscreenBlendModeTypes.add(new csmBlendMode());

            // キャッシュ用インスタンスをセットアップ
            offscreenMultiplyColors.add(new CubismRenderer.CubismTextureColor());
            offscreenScreenColors.add(new CubismRenderer.CubismTextureColor());
        }
    }

    /**
     * Sets up the parts hierarchy by building parent-child relationships between parts and drawables.
     */
    private void setupPartsHierarchy() {
        partsHierarchy.clear();

        // すべてのパーツのパーツ情報管理構造体を作成
        final int partCount = model.getParts().getCount();
        for (int i = 0; i < partCount; i++) {
            partsHierarchy.add(new CubismModelPartInfo());
        }

        // Partごとに親パーツを取得し、親パーツの子objectリストに追加する。
        for (int i = 0; i < partCount; i++) {
            final int parentPartIndex = getPartParentPartIndex(i);

            if (parentPartIndex == CubismNoIndex.PARENT.index) {
                continue;
            }

            for (int partIndex = 0; partIndex < partsHierarchy.size(); partIndex++) {
                if (partIndex == parentPartIndex) {
                    CubismModelObjectInfo objectInfo = new CubismModelObjectInfo(
                        i,
                        CubismModelObjectInfo.ObjectType.PARTS
                    );
                    partsHierarchy.get(partIndex).objects.add(objectInfo);
                    break;
                }
            }
        }

        // Drawableごとに親パーツを取得し、親パーツの子objectリストに追加する。
        for (int i = 0; i < model.getDrawables().getCount(); i++) {
            final int parentPartIndex = getDrawableParentPartIndex(i);

            if (parentPartIndex == CubismNoIndex.PARENT.index) {
                continue;
            }

            for (int partIndex = 0; partIndex < partsHierarchy.size(); partIndex++) {
                if (partIndex == parentPartIndex) {
                    CubismModelObjectInfo objectInfo = new CubismModelObjectInfo(
                        i,
                        CubismModelObjectInfo.ObjectType.DRAWABLE
                    );
                    partsHierarchy.get(partIndex).objects.add(objectInfo);
                    break;
                }
            }
        }

        // ここまででモデルのパーツ構造が完成。

        // パーツ子描画オブジェクト情報構造体を作成
        for (int i = 0; i < partsHierarchy.size(); i++) {
            // パーツ管理構造体を取得
            getPartChildDrawObjects(i);
        }
    }

    /**
     * Return true if the logical product of flag and mask matches the mask.
     *
     * @return Return true if the logical product of flag and mask matches the mask.
     */
    private boolean isBitSet(final byte flag, final byte mask) {
        return (flag & mask) == mask;
    }

    /**
     * PartのOverrideColorを設定する。
     *
     * @param partIndex 設定するPartのインデックス
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @param a アルファ
     * @param partColors 設定するPartの上書き色のリスト
     * @param drawableColors Drawableの上書き色のリスト
     */
    private void setPartColor(
        int partIndex,
        float r, float g, float b, float a,
        List<ColorData> partColors,
        List<ColorData> drawableColors
    ) {
        partColors.get(partIndex).color.r = r;
        partColors.get(partIndex).color.g = g;
        partColors.get(partIndex).color.b = b;
        partColors.get(partIndex).color.a = a;

        if (partColors.get(partIndex).isOverridden) {
            List<Integer> childDrawables = partChildDrawablesMap.get(partIndex);
            if(childDrawables == null) return;

            for (int i = 0; i < childDrawables.size(); i++) {
                int drawableIndex = childDrawables.get(i);

                drawableColors.get(drawableIndex).color.r = r;
                drawableColors.get(drawableIndex).color.g = g;
                drawableColors.get(drawableIndex).color.b = b;
                drawableColors.get(drawableIndex).color.a = a;
            }
        }
    }

    /**
     * PartのOverrideFlagを設定する。
     *
     * @param partIndex 設定するPartのインデックス
     * @param value 真偽値
     * @param partColors 設定するPartの上書き色のリスト
     * @param drawableColors Drawableの上書き色のリスト
     */
    private void setOverrideColorsForPartColors(
        int partIndex,
        boolean value,
        List<ColorData> partColors,
        List<ColorData> drawableColors
    ) {
        partColors.get(partIndex).isOverridden = value;

        List<Integer> childDrawables = partChildDrawablesMap.get(partIndex);
        if (childDrawables == null) return;

        for (int i = 0; i < childDrawables.size(); i++) {
            int drawableIndex = childDrawables.get(i);
            drawableColors.get(drawableIndex).isOverridden = value;

            if (value) {
                drawableColors.get(drawableIndex).color.r = partColors.get(partIndex).color.r;
                drawableColors.get(drawableIndex).color.g = partColors.get(partIndex).color.g;
                drawableColors.get(drawableIndex).color.b = partColors.get(partIndex).color.b;
                drawableColors.get(drawableIndex).color.a = partColors.get(partIndex).color.a;
            }
        }
    }

    /**
     * List of opacities for non-existent parts
     */
    private float[] notExistPartOpacities = new float[1];
    private final List<Integer> notExistPartIndices = new ArrayList<Integer>();

    /**
     * List of IDs for non-existent parts
     */
    private final Map<CubismId, Integer> notExistPartIds = new HashMap<CubismId, Integer>();
    /**
     * List of values for non-existent parameters
     */
    private float[] notExistParameterValues = new float[1];
    private final List<Integer> notExistParameterIndices = new ArrayList<Integer>();
    /**
     * List of IDs for non-existent parameters
     */
    private final Map<CubismId, Integer> notExistParameterIds = new HashMap<CubismId, Integer>();
    /**
     * Saved parameters
     */
    private float[] savedParameters = new float[1];
    /**
     * model
     */
    private final com.live2d.sdk.cubism.core.CubismModel model;

    private CubismParameterView[] parameterValues;
    private CubismPartView[] partValues;

    /**
     * モデルの不透明度
     */
    private float modelOpacity = 1.0f;

    private final List<CubismId> parameterIds = new ArrayList<>();
    private final List<CubismId> partIds = new ArrayList<>();
    private final List<CubismId> drawableIds = new ArrayList<>();

    /**
     * Drawableの乗算色のリスト
     */
    private final List<ColorData> userDrawableMultiplyColors = new ArrayList<>();
    /**
     * Drawableのスクリーン色のリスト
     */
    private final List<ColorData> userDrawableScreenColors = new ArrayList<>();

    /**
     * パーツの乗算色のリスト
     */
    private final List<ColorData> userPartMultiplyColors = new ArrayList<>();
    /**
     * パーツのスクリーン色のリスト
     */
    private final List<ColorData> userPartScreenColors = new ArrayList<>();

    /**
     * List of offscreen multiply colors.
     */
    private final List<ColorData> userOffscreenMultiplyColors = new ArrayList<>();

    /**
     * List of offscreen screen colors.
     */
    private final List<ColorData> userOffscreenScreenColors = new ArrayList<>();

    /**
     * Partとその子DrawableのListとのMap
     */
    private Map<Integer,List<Integer>> partChildDrawablesMap;

    /**
     * List of culling information for drawables.
     */
    private final List<CullingData> userDrawableCullings = new ArrayList<>();

    /**
     * List of culling information for offscreens.
     */
    private final List<CullingData> userOffscreenCullings = new ArrayList<>();

    /**
     * List to manage ParameterRepeat and Override flag to be set for each Parameter
     */
    private final List<ParameterRepeatData> userParameterRepeatDataList = new ArrayList<ParameterRepeatData>();

    /**
     * Flag whether to Override all the parameter repeat
     */
    private boolean isOverriddenParameterRepeat = true;

    /**
     * Flag whether to override all the multiply colors
     */
    private boolean isOverriddenModelMultiplyColors;
    /**
     * Flag whether to override all the screen colors
     */
    private boolean isOverriddenModelScreenColors;
    /**
     * モデルのカリング設定をすべて上書きするか？
     */
    private boolean isOverriddenCullings;

    /**
     * Flag whether blend mode is enabled for this model.
     */
    private boolean isBlendModeEnabled;

    private final List<CubismModelPartInfo> partsHierarchy = new ArrayList<>();

    /**
     * List of blend modes for each drawable.
     * <p>
     * Used to prevent generating instances each time the value is returned in {@link #getDrawableBlendModeType(int)}.
     */
    private final List<csmBlendMode> drawableBlendModeTypes = new ArrayList<>();

    /**
     * List of blend modes for each offscreen.
     * <p>
     * Used to prevent generating instances each time the value is returned in {@link #getOffscreenBlendModeType(int)}.
     */
    private final List<csmBlendMode> offscreenBlendModeTypes = new ArrayList<>();

    /**
     * List of multiply color instances for each offscreen.
     * <p>
     * Used to prevent generating instances each time the value is returned in {@link #getMultiplyColorOffscreen(int)}.
     */
    private final List<CubismRenderer.CubismTextureColor> offscreenMultiplyColors = new ArrayList<>();

    /**
     * List of screen color instances for each offscreen.
     * <p>
     * Used to prevent generating instances each time the value is returned in {@link #getScreenColorOffscreen(int)}.
     */
    private final List<CubismRenderer.CubismTextureColor> offscreenScreenColors = new ArrayList<>();
}
