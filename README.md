# Pediatric Dose

Android application for weight-based paediatric dose calculation.

Written in Kotlin + Jetpack Compose + Material 3. minSdk 26, targetSdk 34.

## Features

- **Generic mg/kg calculator.** Enter weight, mg/kg, optional liquid
  concentration and optional single-dose cap to get mg and mL.
- **Built-in drug reference aligned with BNFc.** Common paediatric drugs
  (paracetamol, ibuprofen, amoxicillin, azithromycin, cefalexin,
  co-amoxiclav, ondansetron, dexamethasone, prednisolone, salbutamol,
  adrenaline, diazepam PR, midazolam buccal) with dose ranges, caps and
  age-banded notes sourced from the BNFc monographs
  (<https://bnfc.nice.org.uk/>). Each regimen displays its primary
  reference (e.g. "Reference: BNFc" or "BNFc + Resus Council UK") on
  the drug detail screen so values can be traced back to source.
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
professionals.

Primary reference: **BNFc** (British National Formulary for Children),
<https://bnfc.nice.org.uk/>. Built-in dose ranges, caps and age bands
have been aligned with BNFc monographs current at the time of authoring.
Where BNFc uses age-banded rather than weight-based dosing (e.g.
adrenaline for anaphylaxis, rectal diazepam, buccal midazolam,
nebulised salbutamol), the age bands are shown in each drug's notes and
the calculator falls back to the widely-used APLS / Resuscitation
Council UK weight-based approximation.

BNFc is updated frequently. Every dose produced by this app **must** be
independently verified against the live BNFc monograph and against your
local prescribing protocol before administration. The authors accept no
liability for clinical decisions made using this app.
