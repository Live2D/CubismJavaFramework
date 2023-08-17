# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [5-r.1-beta.1] - 2023-08-17

### Added

* Add the function to get the ID of a given parameter.(`CubismModel.getParameterId`)
* Add the `CubismExpressionMotionManager` class.

### Changed

* Change the minimum support version of Android OS to 5.0 (API 21).
* Unify Offscreen drawing-related terminology with `OffscreenSurface`.
* Change the visibility of the `CubismId` constructor to package-private.

### Fixed

* Fix the structure of the class in renderer.
* Separate the high precision mask process from the clipping mask setup process.
* Fix a bug that the value applied by multiply was not appropriate during expression transitions.
* Fix a issue that `CubismIdManager` was not used when retrieving `CubismId`.
  * Please use `CubismFramework.getIdManager().getId` to get `CubismId`.

### Removed

* Remove an unnecessary dependency from `build.gradle`.
* Remove several arguments of `drawMesh` function.


## [4-r.1] - 2023-05-25

### Added

* Add some functions for checking consistency of MOC3 files.
  * Add the function of checking consistency in `CubismMoc.create()`.
  * Add the function of checking consistency before loading a model. (`CubismUserModel.loadModel()`)
* Add some functions to change Multiply and Screen colors on a per part basis.

### Changed

* Change access modifiers for methods in `CubismExpressionMotion`. And also chenge it to non-final class, allowing it to be extended by inheritance.
* Change to get opacity according to the current time of the motion.

### Fixed
* Refactor codes of cacheing vertex information in renderer.
  * This change does not affect the behavior of this SDK.
* Fix a crash when specifying the number of mask buffers as an integer less than or equal to 0 in the second argument of `setupRenderer` function in `CubismUserModel`.
* Fix the redundant process regarding the renderer to make the code more concise.
* Optimize a drawing process of clipping masks.
  * `CubismClippingManagerAndroid` class has a flag to indicate whether mask textures have been cleared or not, and the texture clearing process is only called if they have not been cleared.

## [4-r.1-beta.4] - 2023-03-16

### Fixed

* Fix some problems related to Cubism Core.
  * See `CHANGELOG.md` in Core.

## [4-r.1-beta.3] - 2023-03-10

### Added

* Add function to validate MOC3 files.

## [4-r.1-beta.2] - 2023-01-26

### Added

* Add a description of type package to `README.md`.

### Changed

* Change Android SDK API level from 31 (Android 12) to 33 (Android 13).
* Change the name and package of the `CubismRectangle` class to `type/csmRect` to match SDK for Native.
* Move constants related to debugging from `CubismFramework` class to the newly created `CubismFrameworkConfig` class.
* Change implementation to hide `CubismJsonString` from shallow layers. The following functions are affcted by this change.
  * `getLayoutMap` function in `ICubismModelSetting` class
  * `getLayoutMap` function in `CubismModelSettingJson` class
  * `setupFromLayout` function in `CubismModelMatrix` class
* Change the name and arguments of `createRenderer` function in `CubismUserModel`.
  * The `RendererType` enumurator is abolished. Please generate a renderer you want to use by yourself and put it in the function as an argument.

### Fixed

* Fix JSON data parsing process to improve performance.
* Fix a problem where `setClippingMaskBufferSize` in `CubismRendererAndroid` would be thrown a `NullPointerException` if there are no clipping masks in the model.

### Removed

* Remove dependencies not to be used.
* Remove the unused method `getMotionMap` in `ICubismModelSetting` and `CubismModelSettingJson` class.

## [4-r.1-beta.1] - 2022-12-08

### Added

* Add support for high-precision masks.
* Implement to throw an exception when a user attempt to give null value to a setter method.
* Add API to allow users to configure culling.
* The number of render textures used can now be increased arbitrarily.
  * The maximum number of masks when using multiple render textures has been increased to "number of render textures * 32".

### Changed

* Change the visibility of field variables in CubismClippingContext class from private to public and remove the getter and setter methods.
* Change the specification of the logging functions in `CubismDebug` so that they can take a format string as an argument.

### Fixed

* Change `radianToDirection` function in `CubismMath` so that an instance of CubismVector2 created by an API user is given as second argument, and the calculation result is stored in that instance and returned.
* Change the type of cache variables for vertex information used in `doDrawModel` function in `CubismRendererAndroid` and `setupClippingContext` function in `CubismClippingManagerAndroid` from `Map` to array.
  * The cost of converting `int` type to `Integer` type (auto-boxing) that was incurred every frame was removed by this change.
* Fix `updateParticles` and `updateParticlesForStabilization` function in `CubismPhysics` not to create an instance of CubismVector2.

## 4-r.1-alpha.1 - 2022-10-06

### Added

* New released!

[5-r.1-beta.1]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1...5-r.1-beta.1
[4-r.1]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.4...4-r.1
[4-r.1-beta.4]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.3...4-r.1-beta.4
[4-r.1-beta.3]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.2...4-r.1-beta.3
[4-r.1-beta.2]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.1...4-r.1-beta.2
[4-r.1-beta.1]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-alpha.1...4-r.1-beta.1
