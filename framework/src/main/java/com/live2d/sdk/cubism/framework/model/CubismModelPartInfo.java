/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Information for a Cubism model part.
 */
public class CubismModelPartInfo {
    /**
     * Constructor.
     */
    public CubismModelPartInfo() {
        objects = new ArrayList<>();
        childDrawObjects = new PartChildDrawObjects();
    }

    /**
     * Constructor.
     *
     * @param objects          list of CubismModelObjectInfo
     * @param childDrawObjects information of part child draw objects
     */
    public CubismModelPartInfo(List<CubismModelObjectInfo> objects, PartChildDrawObjects childDrawObjects) {
        this.objects = new ArrayList<>(objects);
        this.childDrawObjects = new PartChildDrawObjects(childDrawObjects);
    }

    /**
     * Returns the number of child objects.
     *
     * @return number of child objects
     */
    public int getChildObjectCount() {
        return objects.size();
    }

    /**
     * List of object information.
     */
    public List<CubismModelObjectInfo> objects;

    /**
     * Information of part child draw objects.
     */
    public PartChildDrawObjects childDrawObjects;
}
