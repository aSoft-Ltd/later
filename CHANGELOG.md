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