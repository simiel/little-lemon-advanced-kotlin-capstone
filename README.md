# Little Lemon Menu

Little Lemon Menu is an Android app for browsing a small restaurant menu on a phone. It shows each dish and drink as a photo and title in a two-column grid, and the options menu sorts that grid alphabetically or by price and filters it by category. Tapping an item opens a detail screen with the photo, price, and category. The catalog is packaged with the app, so the project is a complete on-device menu flow: grid, sort, filter, and a second activity. It is the advanced Kotlin capstone in this repository (`little-lemon-advanced-kotlin-capstone`).

## Features

- **Menu grid.** `ProductsGrid` uses a `LazyVerticalGrid` with two fixed columns, cropped photos, and the item title under each image. Tapping the image starts the detail screen.
- **Sorting.** The options menu sorts the catalog A–Z, by price ascending, or by price descending through `SortHelper` and `SortType`.
- **Filtering.** The same menu filters to all items, drinks, food, or dessert through `FilterHelper` and `FilterType`. Each sort or filter rebuilds the visible list from the full catalog in `ProductsWarehouse`, so one menu action replaces the previous one.
- **Product details.** `MainActivity` starts `ProductActivity` with an explicit `Intent`. Extras carry the title, price, drawable resource id, and category. `ProductDetails` renders the image, title, `Price: $…`, and category.
- **In-app catalog.** `ProductsWarehouse` holds eleven items:
  - **Drinks:** black tea ($3), green tea ($3), espresso ($5), cappuccino ($8), latte ($8), mocha ($10)
  - **Food:** boeuf bourguignon ($15), bouillabaisse ($20), lasagna ($15), onion soup ($12), salmon en papillote ($25)

Photos for croissant, custard tart, and quiche Lorraine are in `app/src/main/res/drawable`, and the options menu includes a dessert filter. Those three dishes are not rows in `ProductsWarehouse`.

## Tech stack

| Piece | What the project uses |
| --- | --- |
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose UI 1.7.6 (Material and Foundation) with the Kotlin Compose compiler plugin |
| Android | Android Gradle Plugin 8.13.0, `compileSdk` 36, `targetSdk` 36, `minSdk` 21, Java 17 |
| Build | Gradle 8.13 via the wrapper (`./gradlew`) |
| State | `MutableStateFlow`, collected in Compose with `collectAsState` |
| Tests | JUnit 4.13.2, AndroidX Test, Espresso 3.5.1, Compose UI test (`ui-test-junit4`) |
| Application id | `com.littlelemon.menu` (`versionName` `1.0`, `versionCode` 1) |

AndroidX is on, and `android.nonTransitiveRClass` is enabled. The manifest applies `Theme.LittleLemonMenu` (`ThemeOverlay.Material.ActionBar`) so the options menu has an action bar. `ui/theme` defines a Compose Material theme, `LittleLemonMenuTheme`, with light and dark palettes, default typography, and rounded shapes.

## Architecture

One application module, `:app`. `settings.gradle` names the Gradle project **Little Lemon Menu**.

```
app/src/main/java/com/littlelemon/menu/
├── MainActivity.kt        # launcher, options menu, list state, detail intent
├── ProductActivity.kt     # reads intent extras and hosts the detail screen
├── ProductsGrid.kt        # two-column grid
├── ProductDetails.kt      # image, title, price, category
├── ProductsWarehouse.kt   # in-memory catalog
├── ProductItem.kt         # ProductItem and Products
├── FilterHelper.kt        # category filter
├── FilterType.kt          # All, Drinks, Food, Dessert
├── SortHelper.kt          # alphabetical and price sorts
├── SortType.kt            # Alphabetically, PriceAsc, PriceDesc
└── ui/theme/              # color, type, shape, Material theme
app/src/main/res/menu/products_menu.xml
app/src/main/res/drawable/ # menu photography
app/src/test/              # local JVM test
app/src/androidTest/       # instrumented tests
```

The options menu is a framework `Menu` (`products_menu.xml`), inflated in `onCreateOptionsMenu` with group dividers. `onOptionsItemSelected` maps the sorting group to a `SortType` and the filter group to a `FilterType`, then writes a new `Products` value with `productsState.update`. `InitUI` collects that `MutableStateFlow` and passes the list into `ProductsGrid`, which recomposes.

`SortHelper` and `FilterHelper` are plain Kotlin. They take a `List<ProductItem>` and return a new list. Sorting uses extension functions (`sortedBy` on title, `sortedBy` and `sortedByDescending` on price). Filtering returns the full list for `FilterType.All` and otherwise keeps items whose `category` matches the filter type name (`Drinks`, `Food`, `Dessert`).

`ProductItem` stores the image as a `@DrawableRes` id. The detail activity reads that id back as an `Int` extra and loads it with `painterResource`.

## Getting started

### Prerequisites

- JDK 17 (`sourceCompatibility`, `targetCompatibility`, and `kotlinOptions.jvmTarget` are Java 17)
- Android SDK platform 36
- A device or emulator running API 21 or higher, for install and instrumented tests

The app reads no environment variables and ships no API keys. Android Studio writes a gitignored `local.properties` (`sdk.dir`) when the project is opened. `local.properties` is listed in `.gitignore`.

### Build and run

From the repository root:

```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

Opening the project in Android Studio and running the `app` configuration uses the same Gradle build.

### Tests

```bash
./gradlew :app:test
./gradlew :app:connectedDebugAndroidTest
```

`:app:test` runs the local JVM test in `app/src/test` (`ExampleUnitTest`). `:app:connectedDebugAndroidTest` needs a connected device or emulator. Instrumented sources are `ExampleInstrumentedTest` (asserts the package name `com.littlelemon.menu`) and `FilterHelperTest` (a sample list of black tea, croissant, and bouillabaisse, asserting that a dessert filter returns the croissant).

## Project status

Coursework capstone, built up in task commits: project setup, the product `Intent`, grid clicks, and the dessert filter test. The tree has no CI workflow, release artifacts, or license file.

A few exercise placeholders are still in the sources, so `./gradlew :app:assembleDebug` should be checked after they are aligned with the declarations around them:

- `MainActivity.startProductActivity` and the grid click handler refer to `product` where the parameter in scope is `productItem`.
- `FilterHelper.filterProducts` switches on `filterType` while the parameter is `type`. The helper comment still asks for a `FilterHelperTest`; that file is under `androidTest` and calls `filterProducts` on the class.
- The `KEY_TITLE`, `KEY_PRICE`, `KEY_IMAGE`, and `KEY_CATEGORY` constants are declared in a `companion object` inside `ProductActivity.onCreate`.

## What this demonstrates

- A Jetpack Compose menu (`LazyVerticalGrid`, images, a detail column) hosted in `ComponentActivity`, with a framework options menu on the same screen.
- UI state held in a `MutableStateFlow` and read with `collectAsState`, so a menu selection recomposes the grid.
- Screen-to-screen navigation with an explicit `Intent` and named extras for title, price, image resource, and category.
- A small domain model (`ProductItem`, `Products`) and sealed `SortType` and `FilterType` types, with sort and filter kept in helper classes and implemented with Kotlin collection operations and extension functions.
- A resource-backed catalog: photography in `res/drawable`, ids on the model, and a single object (`ProductsWarehouse`) as the source list for every sort and filter.
- Both test source sets wired in Gradle: a local JUnit test on the JVM and instrumented tests (AndroidX Test, Espresso, Compose UI test), including a direct assertion on dessert filtering.
- A current Android toolchain: Kotlin 2.0 with the Compose compiler plugin, Android Gradle Plugin 8.13, Gradle 8.13, Java 17, and SDK 36, with AndroidX and non-transitive R classes.

Samuel Mensah ([@simiel](https://github.com/simiel))
