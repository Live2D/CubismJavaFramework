[English](README.md) / [日本語](README.ja.md)

---

# Cubism Java Framework

This is a framework for using models output by Live2D Cubism 4 Editor in applications.

It provides various functions for displaying and manipulating the model. It is used in conjunction with the Cubism Core library to load the model.

This repository is an **alpha version**. If you have any bug reports or suggestions, please send them to us by creating an [issue](https://github.com/Live2D/CubismJavaSamples/issues) using the GitHub feature.

## Supported Java Versions

This framework can be compiled with **Java SE 6** or higher.

## License

Please check the [license](LICENSE.md) before using the framework.

## Components

Each component is offered in the form of Java packages.

### effect

Provides functions such as automatic blinking and lip sync to add motion information as an effect to the model.

### exception

Provides exception classes related to Cubism SDK Framework.

### id

Provides functions to manage the parameter name, part name, and Drawable name set in the model with unique types.

### math

Provides arithmetic functions required for manipulating and drawing the model, such as matrix and vector calculations.

### model

Provides various functions (generate, update, destroy) for handling the model.

### motion

Provides various functions (motion playback, parameter blending) for applying motion data to the model.

### physics

Provides functions for applying transformation manipulations due to physics to the model.

### rendering

Provides a renderer that implements graphics instructions for drawing the model on various platforms.

### utils

Provides utility functions such as JSON parser and log output.

## Live2D Cubism Core for Java

Live2D Cubism Core for Java is not included in this repository.

To download, please refer to [this](https://community.live2d.com/discussion/1480/download-cubism-sdk-for-java-alpha/) page.

## Samples

Please refer to the following sample repository for implementation examples of standard applications.

[CubismJavaSamples](https://github.com/Live2D/CubismJavaSamples)

## Manual

[Cubism SDK Manual](https://docs.live2d.com/cubism-sdk-manual/top/)

## Changelog

Please refer to [CHANGELOG.md](CHANGELOG.md) for the changelog of this repository.

## Community

If you want to suggest or ask questions about how to use the Cubism SDK between users, please use the community.

- [Live2D community](https://community.live2d.com/)
- [Live2D 公式コミュニティ (Japanese)](https://creatorsforum.live2d.com/)


