/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.math;

import java.util.Arrays;

/**
 * Utility class for 4x4 matrix.
 */
public class CubismMatrix44 {
    /**
     * Creates a {@code CubismMatrix4x4} instance.
     * The created instance is initialized to a unit matrix.
     *
     * @return a {@code CubismMatrix44} instance
     */
    public static CubismMatrix44 create() {
        return new CubismMatrix44();
    }

    /**
     * Creates a {@code CubismMatrix44} instance with float array (length == 16).
     * If the argument (float array) is either {@code null} or it does not have a size of 16, throws {@code IllegalArgumentException}.
     *
     * @param matrix the 4x4 (length == 16) matrix represented by 16 floating-point numbers array
     * @return a {@code CubismMatrix44} instance
     *
     * @throws IllegalArgumentException if the argument is either {@code null} or it does not have a size of 16.
     */
    public static CubismMatrix44 create(float[] matrix) {
        if (isNot4x4Matrix(matrix)) {
            throw new IllegalArgumentException("The passed array is either 'null' or does not have a size of 16.");
        }
        return new CubismMatrix44(matrix);
    }

    /**
     * Creates new {@code CubismMatrix44} instance with a {@code CubismMatrix44} instance.
     * This method works the same as the copy method.
     *
     * <p>
     * If the argument {@code null}, throws {@code IllegalArgumentException}.
     *
     * @param matrix the 4x4 matrix represented by a {@code CubismMatrix44}
     * @return a {@code CubismMatrix44} instance
     *
     * @throws IllegalArgumentException if the argument is either {@code null} or it does not have a size of 16.
     */
    public static CubismMatrix44 create(CubismMatrix44 matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("The passed CubismMatrix44 instance is 'null'");
        }
        return new CubismMatrix44(matrix.tr);
    }

    /**
     * Returns the product of the two matrices received.
     * This is a matrix calculation, therefore note that exchanging the first and second arguments will produce different results.
     *
     * @param multiplicand the multiplicand matrix
     * @param multiplier the multiplier matrix
     * @param dst the destination array
     * @throws IllegalArgumentException if the argument is either {@code null} or it does not have a size of 16.
     */
    public static void multiply(float[] multiplicand, float[] multiplier, float[] dst) {
        if (isNot4x4Matrix(multiplicand) || isNot4x4Matrix(multiplier) || isNot4x4Matrix(dst)) {
            throw new IllegalArgumentException("The passed array is either 'null' or does not have a size of 16.");
        }

        for (int i = 0; i < 16; i++) {
            matrixForMultiplication[i] = 0.0f;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    matrixForMultiplication[j + i * 4] += multiplicand[k + i * 4] * multiplier[j + k * 4];
                }
            }
        }

        System.arraycopy(matrixForMultiplication, 0, dst, 0, 16);
    }

    /**
     * Initialize to a unit matrix.
     */
    public void loadIdentity() {
        resetMatrixForCalcToUnitMatrix();
        setMatrix(matrixForCalculation);
    }

    /**
     * Return the matrix as an array of floating point numbers.
     *
     * @return the 4x4 matrix represented by 16 floating-point numbers array
     */
    public float[] getArray() {
        return tr;
    }

    /**
     * Sets this matrix with the given {@code CubismMatrix44} instance.
     *
     * @param matrix the new 4x4 matrix represented by {@code CubismMatrix44} instance
     */
    public void setMatrix(CubismMatrix44 matrix) {
        System.arraycopy(matrix.tr, 0, tr, 0, 16);
    }

    /**
     * Sets this matrix with the array of float type (length == 16).
     *
     * @param matrix the new 4x4 matrix represented by 16 floating-point numbers array
     * @throws IllegalArgumentException if the argument is either {@code null} or it does not have a size of 16.
     */
    public void setMatrix(float[] matrix) {
        if (isNot4x4Matrix(matrix)) {
            throw new IllegalArgumentException("The passed array is either 'null' or does not have a size of 16.");
        }

        System.arraycopy(matrix, 0, tr, 0, 16);
    }

    /**
     * Returns the scale of the X-axis.
     *
     * @return the scale of the X-axis
     */
    public float getScaleX() {
        return tr[0];
    }

    /**
     * Returns the scale of the Y-axis.
     *
     * @return the scale of the Y-axis
     */
    public float getScaleY() {
        return tr[5];
    }

    /**
     * Returns the amount of movement in the X-axis direction.
     *
     * @return the amount of movement in the X-axis direction
     */
    public float getTranslateX() {
        return tr[12];
    }

    /**
     * Returns the amount of movement in the Y-axis direction.
     *
     * @return the amount of movement in the Y-axis direction
     */
    public float getTranslateY() {
        return tr[13];
    }

    /**
     * Returns the X-coordinate value transformed by the current matrix.
     * <p>
     * For example, used to obtain the position on the screen from the vertex coordinates of a model in the local coordinate system.
     *
     * @param src the X-coordinate value to be transformed
     * @return the X-coordinate value transformed by the current matrix
     */
    public float transformX(float src) {
        return tr[0] * src + tr[12];
    }

    /**
     * Returns the Y-coordinate value transformed by the current matrix.
     * <p>
     * For example, used to obtain the position on the screen from the vertex coordinates of a model in the local coordinate system.
     *
     * @param src the Y-coordinate value to be transformed
     * @return the Y-coordinate value transformed by the current matrix
     */
    public float transformY(float src) {
        return tr[5] * src + tr[13];
    }

    /**
     * Returns the X-coordinate value inverse-transformed by the current matrix.
     * <p>
     * For example, used to obtain the coordinates in the model's local coordinate system from the entered coordinates in the screen coordinate system, such as collision detection.
     *
     * @param src the X-coordinate value to be inverse-transformed
     * @return the X-coordinate value inverse-transformed by the current matrix
     */
    public float invertTransformX(float src) {
        return (src - tr[12]) / tr[0];
    }

    /**
     * Returns the Y-coordinate value inverse-transformed by the current matrix.
     * <p>
     * For example, used to obtain the coordinates in the model's local coordinate system from the entered coordinates in the screen coordinate system, such as collision detection.
     *
     * @param src the Y-coordinate value to be inverse-transformed
     * @return the Y-coordinate value inverse-transformed by the current matrix
     */
    public float invertTransformY(float src) {
        return (src - tr[13]) / tr[5];
    }

    /**
     * Translates the current matrix relatively by the amount of the arguments.
     * The coordinate of the arguments must be entered in a screen coodinate system.
     *
     * @param x the amount of movement in X-axis direction
     * @param y the amount of movement in Y-axis direction
     */
    public void translateRelative(float x, float y) {
        resetMatrixForCalcToUnitMatrix();
        matrixForCalculation[12] = x;
        matrixForCalculation[13] = y;

        multiply(matrixForCalculation, tr, tr);
    }

    /**
     * Translates the current matrix to the position specified by the arguments.
     * The coordinate of the arguments must be entered in a screen coodinate system.
     *
     * @param x X-coordinate of destination
     * @param y Y-coordinate of destination
     */
    public void translate(float x, float y) {
        tr[12] = x;
        tr[13] = y;
    }

    /**
     * Translates the X-coordinate of the current matrix to the position specified by the argument.
     * The coordinate of the argument must be entered in a screen coodinate system.
     *
     * @param x X-coordinate of destination
     */
    public void translateX(float x) {
        tr[12] = x;
    }

    /**
     * Translates the Y-coordinate of the current matrix to the position specified by the argument.
     * The coordinate of the argument must be entered in a screen coodinate system.
     *
     * @param y Y-coordinate of destination
     */
    public void translateY(float y) {
        tr[13] = y;
    }

    /**
     * Sets relatively the scaling rate to the current matrix.
     *
     * @param x the scaling rate in the X-axis direction
     * @param y the scaling rate in the Y-axis direction
     */
    public void scaleRelative(float x, float y) {
        resetMatrixForCalcToUnitMatrix();
        matrixForCalculation[0] = x;
        matrixForCalculation[5] = y;
        multiply(matrixForCalculation, tr, tr);
    }

    /**
     * Sets the scaling rate of the current matrix the specified scale by the arguments.
     *
     * @param x the scaling rate in the X-axis direction
     * @param y the scaling rate in the Y-axis direction
     */
    public void scale(float x, float y) {
        tr[0] = x;
        tr[5] = y;
    }

    /**
     * Returns the product of the current matrix and the given {@code CubismMatrix44} instance for the argument.
     *
     * @param multiplier the multiplier matrix
     */
    public void multiplyByMatrix(CubismMatrix44 multiplier) {
        multiply(tr, multiplier.tr, tr);
    }

    /**
     * Calculates the inverse matrix of the current matrix.
     * <p>
     * NOTE: Creates a new {@code CubismMatrix44} instance on each call.
     *
     * @return inverse matrix of the current matrix; identity matrix when epsilon is small.
     */
    public CubismMatrix44 getInvert() {
        CubismMatrix44 dst = new CubismMatrix44();
        getInvert(dst);
        return dst;
    }

    /**
     * Calculates the inverse matrix of the current matrix and stores the result in the destination matrix.
     * <p>
     * This method does not allocate a new instance.
     *
     * @param dst destination matrix to store the inverse matrix result
     */
    public void getInvert(CubismMatrix44 dst) {
        float r00 = tr[0];
        float r10 = tr[1];
        float r20 = tr[2];
        float r01 = tr[4];
        float r11 = tr[5];
        float r21 = tr[6];
        float r02 = tr[8];
        float r12 = tr[9];
        float r22 = tr[10];

        float tx = tr[12];
        float ty = tr[13];
        float tz = tr[14];

        float det = r00 * (r11 * r22 - r12 * r21) -
                    r01 * (r10 * r22 - r12 * r20) +
                    r02 * (r10 * r21 - r11 * r20);

        if (CubismMath.absF(det) < CubismMath.EPSILON) {
            dst.loadIdentity();
            return;
        }

        float invDet = 1.0f / det;

        float inv00 = (r11 * r22 - r12 * r21) * invDet;
        float inv01 = -(r01 * r22 - r02 * r21) * invDet;
        float inv02 = (r01 * r12 - r02 * r11) * invDet;
        float inv10 = -(r10 * r22 - r12 * r20) * invDet;
        float inv11 = (r00 * r22 - r02 * r20) * invDet;
        float inv12 = -(r00 * r12 - r02 * r10) * invDet;
        float inv20 = (r10 * r21 - r11 * r20) * invDet;
        float inv21 = -(r00 * r21 - r01 * r20) * invDet;
        float inv22 = (r00 * r11 - r01 * r10) * invDet;

        dst.tr[0] = inv00;
        dst.tr[1] = inv10;
        dst.tr[2] = inv20;
        dst.tr[3] = 0.0f;
        dst.tr[4] = inv01;
        dst.tr[5] = inv11;
        dst.tr[6] = inv21;
        dst.tr[7] = 0.0f;
        dst.tr[8] = inv02;
        dst.tr[9] = inv12;
        dst.tr[10] = inv22;
        dst.tr[11] = 0.0f;

        dst.tr[12] = -(inv00 * tx + inv01 * ty + inv02 * tz);
        dst.tr[13] = -(inv10 * tx + inv11 * ty + inv12 * tz);
        dst.tr[14] = -(inv20 * tx + inv21 * ty + inv22 * tz);
        dst.tr[15] = 1.0f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CubismMatrix44 that = (CubismMatrix44) o;
        return Arrays.equals(tr, that.tr);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tr);
    }

    /**
     * Constructs a new 4x4 matrix initialized with a unit matrix without taking any arguments.
     */
    protected CubismMatrix44() {
        tr = new float[16];
        loadIdentity();
    }

    /**
     * Constructs a new 4x4 matrix. It is initialized with a unit matrix represented by the float array that has a size of 16 passed for the argument.
     *
     * <p>
     * This is not a public method, therefore it does not expect that {@code null} value or other illegal value is passed.
     *
     * @param matrix the 4x4 (length == 16) matrix represented by 16 floating-point numbers array
     */
    protected CubismMatrix44(float[] matrix) {
        tr = new float[16];
        setMatrix(matrix);
    }

    /**
     * The 4×4 matrix array.
     */
    protected float[] tr;

    /**
     * If the argument given is 4x4 matrix array, returns {@code true}. If the argument is {@code null} or does not have a size of 16, returns {@code false}.
     *
     * @param array the float array to be checked
     * @return {@code true} if the argument given is a 4x4 matrix, otherwise {@code false}
     */
    private static boolean isNot4x4Matrix(float[] array) {
        return (array == null || array.length != 16);
    }

    /**
     * Resets the variable '_4x4MatrixForCalculation' to a unit matrix.
     */
    private static void resetMatrixForCalcToUnitMatrix() {
        for (int i = 0; i < 16; i++) {
            matrixForCalculation[i] = 0.0f;
        }
        matrixForCalculation[0] = 1.0f;
        matrixForCalculation[5] = 1.0f;
        matrixForCalculation[10] = 1.0f;
        matrixForCalculation[15] = 1.0f;
    }

    /**
     * The 4x4 matrix array for matrix calculation.
     * This exists for avoiding creating a new float array at running method.
     */
    private static final float[] matrixForCalculation = new float[16];
    /**
     * The 4x4 matrix array for 'multiply' method.
     * Prevents _4x4MatrixForCalculation from resetting the multiplicand information when it is passed to the 'multiply' method.
     */
    private static final float[] matrixForMultiplication = new float[16];
}
