package com.live2d.sdk.cubism.framework;

/**
 * Functional interface that defines file loading operations for the Cubism SDK.
 * <p>
 * This interface is used by the Cubism SDK when loading shader files and other resources.
 * Implementations should provide platform-specific file-loading logic.
 * </p>
 */
@FunctionalInterface
public interface ICubismLoadFileFunction {
    /**
     * Loads a file from the specified file path and returns it as a byte array.
     *
     * @param filePath the path to the file to be loaded
     * @return byte array containing the file contents.
     */
    byte[] load(final String filePath);
}
