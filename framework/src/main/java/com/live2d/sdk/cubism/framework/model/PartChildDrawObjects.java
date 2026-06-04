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
 * Information for part child draw objects.
 */
public class PartChildDrawObjects {
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
     * @param drawableIndices  list of Drawable indices
     * @param offscreenIndices list of Offscreen indices
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
     * List of Drawable indices.
     */
    public List<Integer> drawableIndices;

    /**
     * List of Offscreen indices.
     */
    public List<Integer> offscreenIndices;
}
