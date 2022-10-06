/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.rendering.android;

import com.live2d.sdk.cubism.framework.math.CubismMatrix44;
import com.live2d.sdk.cubism.framework.math.CubismRectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Context of Clipping Mask
 */
class CubismClippingContext {
    /**
     * Constructor
     */
    public CubismClippingContext(
        CubismClippingManagerAndroid manager,
        final int[] clippingDrawableIndices,
        int clipCount
    ) {
        owner = manager;

        // クリップしている（＝マスク用の）Drawableのインデックスリスト
        clippingIdList = clippingDrawableIndices;

        // マスクの数
        clippingIdCount = clipCount;
    }

    /**
     * このマスクにクリップされる描画オブジェクトを追加する
     *
     * @param drawableIndex クリッピング対象に追加する描画オブジェクトのインデックス
     */
    public void addClippedDrawable(int drawableIndex) {
        clippedDrawableIndexList.add(drawableIndex);
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void isUsing(boolean isUsing) {
        this.isUsing = isUsing;
    }

    public int[] getClippingIdList() {
        return clippingIdList;
    }

    public int getClippingIdCount() {
        return clippingIdCount;
    }

    public int getLayoutChannelNo() {
        return layoutChannelNo;
    }

    public void setLayoutChannelNo(int layoutChannelNo) {
        this.layoutChannelNo = layoutChannelNo;
    }

    public CubismRectangle getLayoutBounds() {
        return layoutBounds;
    }

    public CubismRectangle getAllClippedDrawRect() {
        return allClippedDrawRect;
    }

    public CubismMatrix44 getMatrixForMask() {
        return matrixForMask;
    }

    public CubismMatrix44 getMatrixForDraw() {
        return matrixForDraw;
    }

    public List<Integer> getClippedDrawableIndexList() {
        return clippedDrawableIndexList;
    }

    public CubismClippingManagerAndroid getOwner() {
        return owner;
    }

    /**
     * このマスクを管理するマネージャのインスタンスを取得する。
     *
     * @return クリッピングマネージャのインスタンス
     */
    public CubismClippingManagerAndroid getClippingManager() {
        return owner;
    }

    /**
     * クリッピングマスクのIDリスト
     */
    private final int[] clippingIdList;
    /**
     * 現在の描画状態でマスクの準備が必要ならtrue
     */
    private boolean isUsing;
    /**
     * クリッピングマスクの数
     */
    private final int clippingIdCount;
    /**
     * RGBAのいずれのチャンネルにこのクリップを配置するか(0:R, 1:G, 2:B, 3:A)
     */
    private int layoutChannelNo;
    /**
     * マスク用チャンネルのどの領域にマスクを入れるか(View座標-1..1, UVは0..1に直す)
     */
    private final CubismRectangle layoutBounds = CubismRectangle.create();
    /**
     * このクリッピングで、クリッピングされる全ての描画オブジェクトの囲み矩形（毎回更新）
     */
    private final CubismRectangle allClippedDrawRect = CubismRectangle.create();
    /**
     * マスクの位置計算結果を保持する行列
     */
    private final CubismMatrix44 matrixForMask = CubismMatrix44.create();
    /**
     * 描画オブジェクトの位置計算結果を保持する行列
     */
    private final CubismMatrix44 matrixForDraw = CubismMatrix44.create();
    /**
     * このマスクにクリップされる描画オブジェクトのリスト
     */
    private final List<Integer> clippedDrawableIndexList = new ArrayList<Integer>();
    /**
     * このマスクを管理しているマネージャのインスタンス
     */
    private final CubismClippingManagerAndroid owner;
}
