# 📐 Blueprint 

[![Release](https://jitpack.io/v/popovanton0/blueprint.svg)](https://jitpack.io/#popovanton0/blueprint)
[![Introductory Medium Article](https://img.shields.io/badge/medium-article-grey?labelColor=black&logo=medium&logoColor=white&link=https://proandroiddev.com/blueprint-visualizing-paddings-in-jetpack-compose-eb62413c6d74)](https://proandroiddev.com/blueprint-visualizing-paddings-in-jetpack-compose-eb62413c6d74)
![License](https://img.shields.io/github/license/popovanton0/Blueprint?color=blue)


Visualize the dimensions of your composables on a blueprint


![Blueprint Usage Example](images/navbar-light.png#gh-light-mode-only)
![Blueprint Usage Example](images/navbar-dark.png#gh-dark-mode-only)

[Introductory Medium Article](https://proandroiddev.com/blueprint-visualizing-paddings-in-jetpack-compose-eb62413c6d74)

## The Problem

Have you ever desired to see, what *exactly* is that padding's value while looking at the composable
preview window? Especially when you are developing a button with 5 color styles, 3 sizes, and 2 
optional icons; and each combination of these parameters has different paddings?

Combinatorial explosion of UI components in design systems requires having a lot of context about
paddings, dp's, sizes, corner radiuses, and other dimensional information in your head at the 
same time.

![Many combinations of buttons](images/combinations-light.png#gh-light-mode-only)
![Many combinations of buttons](images/combinations-dark.png#gh-dark-mode-only)

In addition, code to produce those combinations can get tricky to analyze. So, verification also 
becomes hard: you make screenshots, move them to Figma, overlay them on top, and try to see the 
difference. Tedious 😩!

## The Solution

The Blueprint library provides a way to visualize dimensional information in your UI using a simple
DSL-based definition:

1. Just wrap your target UI in a `Blueprint` composable
2. Mark children with [`Modifier.blueprintId(id: String)`](https://github.com/popovanton0/Blueprint/blob/main/blueprint/src/main/java/com/popovanton0/blueprint/BlueprintId.kt) modifier
3. Write the blueprint definition

```kotlin
Blueprint(
    blueprintBuilder = {
        widths {
            group {
                "item0".right lineTo "item1".left
                "item0" lineTo "item0"
                "item2" lineTo "item3"
            }
        }
        heights {
            group { "item0Icon" lineTo "item0Text" }
            group { "item0" lineTo "item0" }
            group(End) { "item3Icon".bottom lineTo "item3Text".top }
        }
    }
) {
    val items = remember { listOf("Songs", "Artists", "Playlists", "Settings") }
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = Modifier.blueprintId("item$index"),
                icon = { Icon(Modifier.blueprintId("item${index}Icon"), TODO()) },
                label = { Text(Modifier.blueprintId("item${index}Text"), TODO()) },
                selected = index == 0,
                onClick = { TODO() }
            )
        }
    }
}
```

### Preview

![Blueprint Usage Example](images/navbar-light.png#gh-light-mode-only)
![Blueprint Usage Example](images/navbar-dark.png#gh-dark-mode-only)

### And another example:

![Blueprint Usage Example](images/button-light.png#gh-light-mode-only)
![Blueprint Usage Example](images/button-dark.png#gh-dark-mode-only)

<details>
<summary>More examples</summary>

These are snapshots from snapshot testing:

|   |   |
|---|---|
| ![almost_none_space_to_draw](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_almost_none_space_to_draw.png)  | ![no_blueprint_if_globally_disabled](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_no_blueprint_if_globally_disabled.png)  |
| ![arrow_customization 0](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_arrow_customization[0.0].png)  | ![not_enough_space_to_draw](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_not_enough_space_to_draw.png)  |
| ![arrow_customization 15](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_arrow_customization[15.0].png)  | ![padding_not_applied](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_padding_not_applied.png)  |
| ![arrow_customization 45](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_arrow_customization[45.0].png)  | ![reacts_to_blueprint_builder_update_(with_green)](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_reacts_to_blueprint_builder_update_(with_green).png)  |
| ![arrow_customization 90](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_arrow_customization[90.0].png)  | ![reacts_to_blueprint_builder_update_(without_green)](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_reacts_to_blueprint_builder_update_(without_green).png)  |
| ![basicTest](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_basicTest.png)  | ![size_labels](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_size_labels.png)  |
| ![correct_line_widths_and_alignments](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_correct_line_widths_and_alignments.png)  | ![when_blueprint_is_disabled_it_is_not_shown](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_when_blueprint_is_disabled_it_is_not_shown.png)  |
| ![customFontSizeAndColor](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_customFontSizeAndColor.png)  | ![when_specifying_blueprint_ids_that_are_not_referenced_in_the_composable_no_dimensions_are_shown](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_when_specifying_blueprint_ids_that_are_not_referenced_in_the_composable_no_dimensions_are_shown.png)  |
| ![emptyBlueprint](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_emptyBlueprint.png)  | ![when_specifying_blueprint_ids_that_are_then_removed_from_the_composition_dimensions_are_shown_and_then_hidden_(with_green)](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_when_specifying_blueprint_ids_that_are_then_removed_from_the_composition_dimensions_are_shown_and_then_hidden_(with_green).png)  |
| ![fractional_dp_values_rendering](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_fractional_dp_values_rendering.png)  | ![when_specifying_blueprint_ids_that_are_then_removed_from_the_composition_dimensions_are_shown_and_then_hidden_(without_green)](/blueprint/src/test/snapshots/images/com.popovanton0.blueprint_BlueprintScreenshotTest_when_specifying_blueprint_ids_that_are_then_removed_from_the_composition_dimensions_are_shown_and_then_hidden_(without_green).png)  |

</details>

## Features

You can customize
1. Line and border strokes (width and color)
2. Font size and color
3. Arrow style (length, angle, round or square cap)
4. Decimal precision of the dimensional values

Of course, Blueprint works in Android Studio's Preview✨!

Also, you can disable all the overhead of this library in your release builds by either:
1. Disabling blueprint rendering using [`blueprintEnabled`](https://github.com/popovanton0/Blueprint/blob/main/blueprint/src/main/java/com/popovanton0/blueprint/Blueprint.kt) property.
2. Using the `no-op` version of the library:
    ```kotlin
    dependencies {
        debugImplementation("com.github.popovanton0.blueprint:blueprint:1.0.0-alpha04")
        releaseImplementation("com.github.popovanton0.blueprint:blueprint-no-op:1.0.0-alpha04")
    }
   ```

## Getting Started

[![Release](https://jitpack.io/v/popovanton0/blueprint.svg)](https://jitpack.io/#popovanton0/blueprint)

<details>
<summary>Groovy</summary>

Add the following code to your project's *root* `build.gradle` file:

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

Next, add the dependency below to your _module_'s `build.gradle` file:

```gradle
dependencies {
    implementation "com.github.popovanton0.blueprint:blueprint:1.0.0-alpha04"
}
```
</details>

<details open>
<summary>Kotlin</summary>

Add the following code to your project's *root* `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    // ...
    repositories {
        // ...
        maven { url = uri("https://jitpack.io") }
    }
}
```

Next, add the dependency below to your _module_'s `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("com.github.popovanton0.blueprint:blueprint:1.0.0-alpha04")
}
```
Or using Gradle Version Catalog:
```toml
[versions]
blueprint = "1.0.0-alpha04"

[libraries]
blueprint = { module = "com.github.popovanton0.blueprint:blueprint", version.ref = "blueprint" }
```
</details>

> [!WARNING]
> Do not use this dependency notation: `com.github.popovanton0:blueprint:1.0.0-alpha04`. 
> It doesn't work!

### Licence

```
Copyright 2023 Anton Popov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```