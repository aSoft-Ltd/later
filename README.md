# Later
![badge][badge-maven] ![badge][badge-mpp] ![badge][badge-android] ![badge][badge-js] ![badge][badge-jvm]

The majestic Later type to ease asynchronous calls across platform

## Samples
```java
class Test {
    public static void main() {
        Later<String> laterString = Later.from(() -> "Test");
        Later<Integer> laterInt = laterString.map((string) -> 3);
        Assert.assertEquals(3, laterInt.wait());
    }
}
```

## Setup
```kotlin
dependencies {
    implementation("tz.co.asoft:later:0.0.1")
}
```

[badge-maven]: https://img.shields.io/maven-central/v/tz.co.asoft/test/1.0.1?style=flat
[badge-mpp]: https://img.shields.io/badge/kotlin-multiplatform-blue?style=flat
[badge-android]: http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat
[badge-js]: http://img.shields.io/badge/platform-js-yellow.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/platform-jvm-orange.svg?style=flat
