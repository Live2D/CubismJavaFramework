/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.motion;

import com.live2d.sdk.cubism.framework.model.CubismModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds registered {@link ACubismUpdater} instances and runs them in order of executionOrder.
 */
public class CubismUpdateScheduler {
    /**
     * Adds an {@link ACubismUpdater} to the update list.
     *
     * @param updatable the {@link ACubismUpdater} instance to be added
     */
    public void addUpdatableList(ACubismUpdater updatable) {
        if (updatable == null) {
            return;
        }
        cubismUpdatableList.add(updatable);
        needsSort = true;
    }

    /**
     * Sorts the update list in ascending order of executionOrder.
     */
    public void sortUpdatableList() {
        Collections.sort(cubismUpdatableList, ACubismUpdater.COMPARATOR);
        needsSort = false;
    }

    /**
     * Updates every element in the list.
     *
     * @param model            the target model
     * @param deltaTimeSeconds delta time[s]
     */
    public void onLateUpdate(CubismModel model, final float deltaTimeSeconds) {
        if (needsSort) {
            sortUpdatableList();
        }

        for (int i = 0; i < cubismUpdatableList.size(); i++) {
            cubismUpdatableList.get(i).onLateUpdate(model, deltaTimeSeconds);
        }
    }

    private final List<ACubismUpdater> cubismUpdatableList = new ArrayList<ACubismUpdater>();
    private boolean needsSort = true;
}
