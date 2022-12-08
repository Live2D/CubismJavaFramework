# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

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

[4-r.1-beta.1]: https://github.com/Live2D/CubismJavaFramework/compare/4-r.1-alpha.1...4-r.1-beta.1
