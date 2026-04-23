package com.rroot.pediatricdose.domain

import kotlin.math.max
import kotlin.math.min

/**
 * Pure functions for weight-based pediatric dose math.
 *
 * Inputs are kept unit-explicit; callers are responsible for presenting
 * the result with the same unit they supplied.
 */
object DoseCalculator {

    /**
     * Clinically plausible weight range for a paediatric patient, in kg.
     * Values outside this range are almost certainly a data entry error.
     */
    val WEIGHT_RANGE_KG: ClosedFloatingPointRange<Double> = 0.3..150.0

    data class SingleDose(
        /** Milligrams per single administration (already capped to [maxSingleMg]). */
        val doseMg: Double,
        /** Raw uncapped mg (before applying the maximum cap). */
        val rawDoseMg: Double,
        /** True when the cap was applied. */
        val capped: Boolean,
        /** Volume in mL if a liquid concentration was supplied, else null. */
        val volumeMl: Double?,
    )

    data class DailyDose(
        val totalDailyMg: Double,
        val perDoseMg: Double,
        val perDoseVolumeMl: Double?,
        val capped: Boolean,
        val timesPerDay: Int,
    )

    /**
     * Compute a single mg (and optional mL) dose from weight-based dosing.
     *
     * @param weightKg patient weight in kilograms. Must be > 0.
     * @param mgPerKg dose in mg per kg. Must be >= 0.
     * @param concentrationMgPerMl optional liquid concentration (mg/mL). If > 0,
     *                             a volume in mL is returned; otherwise null.
     * @param maxSingleMg optional hard cap per single dose (adult maximum).
     *                    If supplied, the returned mg is min(mg, cap).
     */
    fun singleDose(
        weightKg: Double,
        mgPerKg: Double,
        concentrationMgPerMl: Double? = null,
        maxSingleMg: Double? = null,
    ): SingleDose {
        require(weightKg > 0) { "weightKg must be > 0" }
        require(mgPerKg >= 0) { "mgPerKg must be >= 0" }
        concentrationMgPerMl?.let { require(it > 0) { "concentration must be > 0" } }

        val raw = weightKg * mgPerKg
        val capped = maxSingleMg?.let { raw > it } == true
        val mg = if (capped) maxSingleMg!! else raw
        val ml = concentrationMgPerMl?.let { mg / it }
        return SingleDose(doseMg = mg, rawDoseMg = raw, capped = capped, volumeMl = ml)
    }

    /**
     * Compute a daily mg (and optional mL-per-dose) figure when a drug is
     * divided into [timesPerDay] administrations.
     *
     * If a [maxDailyMg] is supplied the total is capped, and the per-dose mg
     * is recomputed from the capped total. Per-dose mg is never allowed to
     * exceed [maxSingleMg] either.
     */
    fun dailyDose(
        weightKg: Double,
        mgPerKgPerDay: Double,
        timesPerDay: Int,
        concentrationMgPerMl: Double? = null,
        maxDailyMg: Double? = null,
        maxSingleMg: Double? = null,
    ): DailyDose {
        require(weightKg > 0) { "weightKg must be > 0" }
        require(mgPerKgPerDay >= 0) { "mgPerKgPerDay must be >= 0" }
        require(timesPerDay in 1..24) { "timesPerDay must be 1..24" }

        val rawDaily = weightKg * mgPerKgPerDay
        val dailyCapped = maxDailyMg?.let { rawDaily > it } == true
        val totalDaily = if (dailyCapped) maxDailyMg!! else rawDaily

        val rawPerDose = totalDaily / timesPerDay
        val singleCapped = maxSingleMg?.let { rawPerDose > it } == true
        val perDose = if (singleCapped) maxSingleMg!! else rawPerDose

        val perDoseMl = concentrationMgPerMl?.let { perDose / it }
        return DailyDose(
            totalDailyMg = totalDaily,
            perDoseMg = perDose,
            perDoseVolumeMl = perDoseMl,
            capped = dailyCapped || singleCapped,
            timesPerDay = timesPerDay,
        )
    }

    /**
     * Round a double to [decimals] decimal places using standard rounding.
     * Handy for display.
     */
    fun round(value: Double, decimals: Int = 2): Double {
        require(decimals >= 0) { "decimals must be >= 0" }
        var factor = 1.0
        repeat(decimals) { factor *= 10 }
        return kotlin.math.round(value * factor) / factor
    }

    /**
     * Linearly clamp [value] into [range].
     */
    fun clamp(value: Double, range: ClosedFloatingPointRange<Double>): Double =
        max(range.start, min(range.endInclusive, value))
}
