/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.utils.jsonparser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class expresses JSON Array
 */
class CubismJsonArray extends ACubismJsonValue {
    /**
     * Add a JSON Value.
     *
     * @param value JSON value
     */
    public void putValue(ACubismJsonValue value) {
        this.value.add(value);
    }

    @Override
    public ACubismJsonValue get(String key) {
        return new CubismJsonErrorValue().setErrorNotForClientCall(JsonError.TYPE_MISMATCH.message);
    }

    @Override
    public ACubismJsonValue get(int index) {
        if (index < 0 || value.size() <= index) {
            return new CubismJsonErrorValue().setErrorNotForClientCall(JsonError.INDEX_OUT_OF_BOUNDS.message);
        }

        ACubismJsonValue value = this.value.get(index);

        if (value == null) {
            return new CubismJsonNullValue();
        }

        return value;
    }

    @Override
    public String getString() {
        StringBuilder builder = new StringBuilder("[\n");

        for (int i = 0; i < value.size(); i++) {
            builder.append(stringBuffer);
            builder.append(" ");
            builder.append(value.get(i).getString());
            builder.append("\n");
        }

        builder.append("]\n");

        stringBuffer = builder.toString();
        return stringBuffer;
    }

    @Override
    public String getString(String defaultValue) {
        return getString(defaultValue, "");
    }

    @Override
    public String getString(String defaultValue, String indent) {
        StringBuilder builder = new StringBuilder(indent + "[\n");

        for (int i = 0; i < value.size(); i++) {
            builder.append(indent);
            builder.append(" ");
            builder.append(value.get(i).getString(indent + " "));
            builder.append("\n");
        }

        builder.append(indent);
        builder.append("]\n");

        stringBuffer = builder.toString();
        return stringBuffer;
    }

    @Override
    public List<ACubismJsonValue> getList() {
        return value;
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CubismJsonArray that = (CubismJsonArray) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * JSON Array value
     */
    private final List<ACubismJsonValue> value = new ArrayList<ACubismJsonValue>();
}
