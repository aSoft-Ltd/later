# 0.0.64: 2021.08.04

- Reverted kotlin from 1.5.21 to 1.5.10
- Upgraded builders from 1.3.30 to 1.3.42
- Upgrade expect from 0.0.40 to 0.0.42
- added kotlinx-atomic-collections as a dependency
- Removed atomic-fu from dependency list
- Removed asoft.test from dependency list
- Reverted coroutines from 1.5.1-native-mt to 1.5.0-native-mt

# 0.0.63: 2021.07.23

- Improved Java usability

# 0.0.62 : 2021.07.23

- Fixed the Invalid Mutability Exception that persisted on scope extension

# 0.0.61 : 2021.07.23

- Bumped publishing jdk from 8 to 16
- Upgraded kotlin from 1.5.0 to 1.5.21
- Upgraded expect from 0.0.30 to 0.0.40
- Upgraded gradle from 6.8.1 to 7.0.2
- Fixed InvalidMutabilityException on native

# 0.0.60 : 2021.05.02

- Added kotlin 1.5.0 support

# 0.0.51 : 2021.04.09

- Adding another linux targets as well
- Updated build script
- Updated test from 1.1.10 to 1.1.20
- Updated expect from 0.0.10 to 0.0.21

# 0.0.50 : 2021.02.04

- Updated gradle from 6.7.1 to 6.8.1
- changed package `tz.co.asoft` to `later`

# 0.0.42 : 2021.01.02

- Improved performance of `Later.await()`
- Updated to builders 1.3.0

# 0.0.41 : 2020.12.30

- Added static resolving by adding generics to LaterState
- Added expect extensions functions to further ease the use of Later while testing

# 0.0.40 : 2020.12.29

- Rewrote everything
- Created portable javascript thenables that can universally be used in any language
- Provided bindings for java (functional interfaces and the wait method that blocks)
- Provided javascript binding asPromise to easily use it in javascript
- Provided a later-ktx that brings Later to the land of courutines

# 0.0.3 : 2020.12.17

- including missing jar in maven central

# 0.0.2 : 2020.12.17

- added `suspend fun Later<T>.await():T`

# 0.0.1 : 2020.12.17

## Availability

- Published to maven central

## Build Src

- Updated to gradle version 6.7.1

## New Features

- Initial release

## Documentation

- Update readme

## Samples

- Added samples