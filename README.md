[English](README.md) / [日本語](README.ja.md)

---

# Cubism Java Framework

This is a framework for using models output by Live2D Cubism Editor in applications.

It provides various functions for displaying and manipulating the model. It is used in conjunction with the Cubism Core library to load the model.

## Supported Java Versions

This framework can be compiled with **Java SE 7** or higher.

## License

Please check the [license](LICENSE.md) before using the framework.


## Compatibility with Cubism 5 new features and previous Cubism SDK versions

This SDK is compatible with Cubism 5.
For SDK compatibility with new features in Cubism 5 Editor, please refer to [here](https://docs.live2d.com/en/cubism-sdk-manual/cubism-5-new-functions/).
For compatibility with previous versions of Cubism SDK, please refer to [here](https://docs.live2d.com/en/cubism-sdk-manual/compatibility-with-cubism-5/).


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

### type

Provides classes that are defined from the basic types used within this framework.

### utils

Provides utility functions such as JSON parser and log output.

## Live2D Cubism Core for Java

Live2D Cubism Core for Java is not included in this repository.

To download, please refer to [this](https://www.live2d.com/download/cubism-sdk/download-java/) page.

## Samples

Please refer to the following sample repository for implementation examples of standard applications.

[CubismJavaSamples](https://github.com/Live2D/CubismJavaSamples)

## Manual

[Cubism SDK Manual](https://docs.live2d.com/cubism-sdk-manual/top/)

## Changelog

Please refer to [CHANGELOG.md](CHANGELOG.md) for the changelog of this repository.

## Contributing

There are many ways to contribute to the project: logging bugs, submitting pull requests on this GitHub, and reporting issues and making suggestions in Live2D Community.

### Forking And Pull Requests

We very much appreciate your pull requests, whether they bring fixes, improvements, or even new features. To keep the main repository as clean as possible, create a personal fork and feature branches there as needed.

### Bugs

We are regularly checking issue-reports and feature requests at Live2D Community. Before filing a bug report, please do a search in Live2D Community to see if the issue-report or feature request has already been posted. If you find your issue already exists, make relevant comments and add your reaction.

### Suggestions

We're also interested in your feedback for the future of the SDK. You can submit a suggestion or feature request at Live2D Community. To make this process more effective, we're asking that you include more information to help define them more clearly.

## Forum

If you want to suggest or ask questions about how to use the Cubism SDK between users, please use the forum.

- [Live2D Creator's Forum](https://community.live2d.com/)
- [Live2D 公式クリエイターズフォーラム (Japanese)](https://creatorsforum.live2d.com/)
