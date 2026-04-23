# Pediatric Dose

Android application for weight-based paediatric dose calculation.

Written in Kotlin + Jetpack Compose + Material 3. minSdk 26, targetSdk 34.

## Features

- **Generic mg/kg calculator.** Enter weight, mg/kg, optional liquid
  concentration and optional single-dose cap to get mg and mL.
- **Built-in drug reference.** Common paediatric drugs (paracetamol,
  ibuprofen, amoxicillin, azithromycin, cefalexin, co-amoxiclav,
  ondansetron, dexamethasone, prednisolone, salbutamol, adrenaline,
  diazepam PR, midazolam buccal) with conservative starting dose ranges
  and formulary caps.
- **Safety caps.** Per-dose and per-day maxima are applied automatically
  and flagged in the UI when the capped value is used.
- **Unit tests** on the pure dosing logic.

## Project structure

- `app/src/main/java/com/rroot/pediatricdose/domain/DoseCalculator.kt` -
  pure functions for mg/kg math. No Android dependencies.
- `app/src/main/java/com/rroot/pediatricdose/data/` - Drug data model
  and the built-in drug list.
- `app/src/main/java/com/rroot/pediatricdose/ui/` - Compose screens
  (home, calculator, drug list, drug detail, disclaimer).
- `app/src/test/java/com/rroot/pediatricdose/DoseCalculatorTest.kt` -
  JVM unit tests.

## Building

Requires JDK 17 and the Android SDK (platform-tools, platforms/android-34,
build-tools/34.0.0). With `ANDROID_HOME` set:

```
./gradlew assembleDebug
./gradlew test
```

The debug APK lands at `app/build/outputs/apk/debug/app-debug.apk`.

## Medical disclaimer

This app is a reference tool intended for use by trained healthcare
professionals. Dose calculations **must** be independently verified
against an authoritative formulary (e.g. BNFc, Lexicomp, hospital
protocols) before administration. The authors accept no liability for
clinical decisions made using this app.
