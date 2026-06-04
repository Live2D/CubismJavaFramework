/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.model;

/**
 * Information for a Cubism model object.
 */
public class CubismModelObjectInfo {
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
     * @param type        type of the object (Drawable or Parts)
     */
    public CubismModelObjectInfo(int objectIndex, ObjectType type) {
        this.objectIndex = objectIndex;
        this.objectType = type;
    }

    /**
     * Type of the object (Drawable or Parts).
     */
    public ObjectType objectType;

    /**
     * Index of the object.
     */
    public int objectIndex;
}
