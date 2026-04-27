package com.rroot.pediatricdose.domain

import com.rroot.pediatricdose.data.DoseMode
import com.rroot.pediatricdose.data.PediDrug
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Computed dose for a single drug at a given child weight.
 */
data class ComputedDose(
    val primary: String,
    val frequency: String,
    val secondary: String? = null,
    val capped: Boolean = false,
)

object QuickDose {

    fun compute(drug: PediDrug, weightKg: Double): ComputedDose {
        if (weightKg <= 0.0) {
            return ComputedDose(primary = "—", frequency = "")
        }
        return when (val mode = drug.mode) {
            is DoseMode.VolumeMgPerKg -> {
                val mg = mode.mgPerKg * weightKg
                val capped = mode.maxMgPerDose != null && mg > mode.maxMgPerDose
                val mgFinal = if (capped) mode.maxMgPerDose!! else mg
                val cc = mgFinal / mode.mgPerCc
                ComputedDose(
                    primary = "${formatCc(cc)} cc",
                    frequency = mode.frequencyLabel,
                    secondary = "${formatMg(mgFinal)} mg/dose",
                    capped = capped,
                )
            }
            is DoseMode.MassMgPerKg -> {
                val mg = mode.mgPerKg * weightKg
                val capped = mode.maxMgPerDose != null && mg > mode.maxMgPerDose
                val mgFinal = if (capped) mode.maxMgPerDose!! else mg
                ComputedDose(
                    primary = "${formatMg(mgFinal)} ${mode.unitLabel}",
                    frequency = mode.frequencyLabel,
                    capped = capped,
                )
            }
            DoseMode.MaintenanceHollidaySegar -> {
                val perDay = hollidaySegarPerDay(weightKg)
                val perHour = perDay / 24.0
                ComputedDose(
                    primary = "${formatCc(perDay)} cc / 24 h",
                    frequency = "Holliday-Segar",
                    secondary = "≈ ${formatCc(perHour)} cc / hour",
                )
            }
            is DoseMode.BolusMlPerKg -> {
                val cc = mode.mlPerKg * weightKg
                ComputedDose(
                    primary = "${formatCc(cc)} cc",
                    frequency = "single bolus",
                )
            }
            is DoseMode.Infusion -> {
                // Final concentration after recipe.
                val totalDrugMg = mode.drugMgPerCc * mode.diluentSourceCc
                val finalConcMgPerCc = totalDrugMg / mode.finalCc
                val ccPerHour = (mode.mgPerKgPerHour * weightKg) / finalConcMgPerCc
                ComputedDose(
                    primary = "${formatCc(ccPerHour)} cc / hour",
                    frequency = "syringe pump",
                    secondary = mode.recipeText,
                )
            }
            is DoseMode.VolumeAndMass -> {
                var mg = mode.mgPerKg * weightKg
                var capped = false
                if (mode.minMg != null && mg < mode.minMg) {
                    mg = mode.minMg
                    capped = true
                }
                if (mode.maxMg != null && mg > mode.maxMg) {
                    mg = mode.maxMg
                    capped = true
                }
                val cc = mg / mode.mgPerCc
                ComputedDose(
                    primary = "${formatCc(cc)} cc",
                    frequency = mode.frequencyLabel,
                    secondary = "${formatMg(mg)} ${mode.unitLabel}",
                    capped = capped,
                )
            }
            is DoseMode.FreeText -> ComputedDose(
                primary = mode.text,
                frequency = "",
            )
        }
    }

    /**
     * Holliday-Segar daily maintenance fluid in mL.
     *  100 mL/kg for first 10 kg
     *  50 mL/kg for next 10 kg
     *  20 mL/kg above 20 kg
     */
    fun hollidaySegarPerDay(weightKg: Double): Double {
        val a = min(weightKg, 10.0) * 100.0
        val b = max(0.0, min(weightKg, 20.0) - 10.0) * 50.0
        val c = max(0.0, weightKg - 20.0) * 20.0
        return a + b + c
    }

    private fun formatCc(value: Double): String {
        if (value <= 0.0) return "0"
        val rounded = (value * 100).roundToInt() / 100.0
        return if (rounded == rounded.toLong().toDouble()) {
            rounded.toLong().toString()
        } else {
            "%.2f".format(rounded)
        }
    }

    private fun formatMg(value: Double): String {
        val rounded = (value * 10).roundToInt() / 10.0
        return if (rounded == rounded.toLong().toDouble()) {
            rounded.toLong().toString()
        } else {
            "%.1f".format(rounded)
        }
    }
}
