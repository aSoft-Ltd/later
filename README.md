# Later

![badge][badge-maven] ![badge][badge-mpp] ![badge][badge-linux] ![badge][badge-macos] ![badge][badge-android] ![badge][badge-ios] ![badge][badge-watchos] ![badge][badge-tvos] ![badge][badge-js] ![badge][badge-jvm]

## Introduction

Heavily inspired by
javascript [promises](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise), Later(
s) are mainly here to ease the bridge between kotlin coroutines for multiplatform and their respective platforms.

## Problem Statement

While it is true that kotlin can compile down to (js,jvm or native). Usually there are some limitations. One of the
heaviest ones are usually trying to call suspend functions from other platforms (mostly Java and Javascript).

A Later, comes in handy as it lets you work with suspend functions from kotlin, promises from javascript and also
Futures from Java. All you have to do is return a Later object and you are good to go.

## Samples

In Kotlin you can do something like this

```kotlin
class TokenService(val scope: CoroutineScope) {
    fun getToken(id: String) = scope.later { // this is a suspending labda
        delay(100) // simulating heavy work
        getUserTokenFromServer() // returned result will be the valued that will be Later delivered to you upon completion
    }
}
```

Notice that getToken doesn't have to be a suspend function. Which means, it is callable from Java and Javascript. Even C

In Javascript, a Later is
a [Thenable](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise/resolve#Resolving_thenables_and_throwing_Errors)

meaning you can chain `then`, `catch` and `finally methods as you would`

```javascript
const tokenService = require('your-published-shared-lib');

tokenService.getUser("user-1")
.then(result => doSomethingElse(result))
.then(newResult => doThirdThing(newResult))
.catch(failureCallback)
.finally(finalState => {
  console.log(`Got the final result: ${finalState}`);
});
```

But since a Thenable is never really fully a Promise, We have provided a way that you can easily convert a Later to a
Promise like so

```javascript
const tokenService = require('your-published-shared-lib');

tokenService.getUser("user-1").asPromise()   // notice asPromise() here
.then(result => doSomethingElse(result))
.then(newResult => doThirdThing(newResult))
.catch(failureCallback)
.then(finalResult => {
  console.log(`Got the final result: ${finalResult}`);
});
```

Using the same kotlin code in java, is piece of cake

```java
import your.published.shared.lib.TokenService;

class Test {
    TokenService tokenService = TokenService.getDefault();

    public static void testLater() {
        tokenService.getToken("user-1")
                .then(result -> doSomethingElse(result))
                .then(newResult -> doThirdThing(newResult))
                .error(failureCallback) // notice it is error instead of catch (catch is a reserved keyword in java)
                .complete(finalState -> {
                    console.log("Got the final result: " + finalState);
                    return null;
                });
    }
}
``` 

as normal, if you are well versed to using CompletableFutures and like that, there is a convenient
method `asCompletableFuture()`
that returns a CompletableFuture backed by the Later

## Setup

```kotlin
dependencies {
    implementation("tz.co.asoft:later-core:0.0.63")

    implementation("tz.co.asoft:later-ktx:0.0.63") // if using with kotlinx coroutines
}
```

## Extensions

Later is being made to extend into any Deferred data type. It even has built in integration with Deferred support from
kotlinx coroutines

## Conclusion

Now, go and build real multiplatform code, that can be shared across mobile, web and server

[badge-maven]: https://img.shields.io/maven-central/v/tz.co.asoft/later-core/0.0.63?style=flat

[badge-mpp]: https://img.shields.io/badge/kotlin-multiplatform-blue?style=flat

[badge-macos]: http://img.shields.io/badge/platform-macos-silver.svg?style=flat

[badge-linux]: http://img.shields.io/badge/platform-linux-green.svg?style=flat

[badge-android]: http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat

[badge-jvm]: http://img.shields.io/badge/platform-jvm-orange.svg?style=flat

[badge-ios]: http://img.shields.io/badge/platform-ios-silver.svg?style=flat

[badge-tvos]: http://img.shields.io/badge/platform-tvos-silver.svg?style=flat

[badge-watchos]: http://img.shields.io/badge/platform-watchos-silver.svg?style=flat

[badge-js]: http://img.shields.io/badge/platform-js-yellow.svg?style=flat
