# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).


## [5-r.2] - 2024-11-07

### Added

* Add function to get `CombinedParameters` listed in `cdi3.json`.
* Add the functionality to call a function when motion playback starts.

### Changed

* Change the access level of `CubismMotionJson` class to public.
* Change the threshold for enabling anisotropic filtering.

### Fixed

* Fix a bug in which a method to acquire events fired during motion playback returned incorrect values when called multiple times.
* Fix a potential problem with division by 0 when a pose fade time is set to 0 seconds.
* Fix a bug that thrown an exception when playing CubismExpresionMotion with CubismMotionQueueManager.startMotion().


## [5-r.1] - 2024-03-26

### Added

* Add type constraint to the generics type of `getRenderer` function in `CubismUserModel`.
* Add function `modF()` to compute floating-point remainder in `CubismMath` class.

### Changed

* Change the default value of the flag for debugging from `true` to `false`.
* Change to output log if the argument `motionQueueEntry` is `null` in the `updateFadeWeight()` function of the `ACubismMotion` class.

### Deprecated

* Deprecate the `fadeWeight` variable and the `getFadeWeight()` function of the `CubismExpressionMotion` class.
  * The `fadeWeight` variable of the `CubismExpressionMotion` class can cause problems.
  * Please use the `getFadeWeight()` function of the `CubismExpressionMotionManager` class with one argument from now on.
* The `startMotion()` function of the `CubismMotionQueueManager` class with the unnecessary second argument `userTimeSeconds` is deprecated.
  * Please use the `startMotion()` function with one argument from now on.

### Fixed

* Fix a bug that caused incorrect weight values when expression motions were shared by multiple models.
  * Change the way fadeWeight is managed for expression motions.


## [5-r.1-beta.3] - 2024-01-18

### Added

* Add exception catching and error logging handling when an exception is thrown while loading a JSON file.

### Changed

* Change the compile and target SDK version of Android OS to 14.0 (API 34).
  * Upgrade the version of Android Gradle Plugin from 8.0.2 to 8.1.1.
  * Upgrade the version of Gradle from 8.1.1 to 8.2.
  * Change the minimum version of Android Studio to Hedgehog(2023.1.1).
* Change the visibility of the `CubismPhysicsInternal` and `CubismPhysicsJson` classes to `public`.

### Fixed

* Fix an issue where models with a specific number of masks could not be drawn correctly.
* Replace deprecated notation in `build.gradle`.


## [5-r.1-beta.2] - 2023-09-28

### Added

* Add final modifier to some private fields in `CubismModel`.

### Changed

* Change getter functions to get some data without `getIdManager` in `CubismModel`.
* Change some private fields in `CubismModel` to `final` variable.


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

[5-r.2]: https://github.com/Live2D/CubismJavaFramework/compare/5-r.1...5-r.2
[5-r.1]: https://github.com/Live2D/CubismJavaFramework/compare/5-r.1-beta.3...5-r.1
[5-r.1-beta.3]: https://github.com/Live2D/CubismJavaFramework/compare/5-r.1-beta.2...5-r.1-beta.3
[5-r.1-beta.2]: https://github.com/Live2D/CubismJavaFramework/compare/5-r.1-beta.1...5-r.1-beta.2
[5-r.1-beta.1]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1...5-r.1-beta.1
[4-r.1]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.4...4-r.1
[4-r.1-beta.4]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.3...4-r.1-beta.4
[4-r.1-beta.3]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.2...4-r.1-beta.3
[4-r.1-beta.2]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-beta.1...4-r.1-beta.2
[4-r.1-beta.1]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-alpha.1...4-r.1-beta.1
