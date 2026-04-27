package com.rroot.pediatricdose.domain

import com.rroot.pediatricdose.data.AgeBand
import com.rroot.pediatricdose.data.DoseMode
import com.rroot.pediatricdose.data.PediDrug
import com.rroot.pediatricdose.data.Preparation
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/** Result of computing one drug at one concentration. */
data class ComputedDose(
    val primary: String,
    val frequency: String,
    val secondary: String? = null,
    val capped: Boolean = false,
)

/** Result row used by syrup screen — multiple concentrations side-by-side. */
data class SyrupRow(
    val perPreparation: List<PrepResult>,
    val frequency: String,
    val mgPerDose: String,
    val capped: Boolean,
)

data class PrepResult(
    val prepLabel: String,    // "120 mg/5 mL"
    val cc: String,           // "5" or "—"
)

object QuickDose {

    /**
     * Compute the dose for an injection-style drug (single preparation,
     * mg/kg or fluid-rate). Returns "—" when weight is non-positive.
     */
    fun compute(drug: PediDrug, weightKg: Double): ComputedDose {
        if (weightKg <= 0.0) return ComputedDose(primary = "—", frequency = "")
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
            is DoseMode.WeightBasedSyrup -> {
                // Compatibility: collapse to a single primary string showing the first preparation.
                val row = computeSyrup(mode, weightKg)
                val firstCc = row.perPreparation.firstOrNull()?.cc ?: "—"
                ComputedDose(
                    primary = "$firstCc cc",
                    frequency = row.frequency,
                    secondary = row.mgPerDose,
                    capped = row.capped,
                )
            }
            is DoseMode.AgeBanded -> ComputedDose(
                primary = "(see age bands)",
                frequency = "",
            )
        }
    }

    /** Compute a syrup row across all stocked preparations. */
    fun computeSyrup(mode: DoseMode.WeightBasedSyrup, weightKg: Double): SyrupRow {
        if (weightKg <= 0.0) {
            return SyrupRow(
                perPreparation = mode.preparations.map { p ->
                    PrepResult(p.label, "—")
                },
                frequency = mode.frequencyLabel,
                mgPerDose = "",
                capped = false,
            )
        }
        var mg = mode.mgPerKgPerDose * weightKg
        val capped = mode.maxMgPerDose != null && mg > mode.maxMgPerDose
        if (capped) mg = mode.maxMgPerDose!!
        val results = mode.preparations.map { p ->
            PrepResult(prepLabel = p.label, cc = formatCc(mg / p.mgPerCc))
        }
        return SyrupRow(
            perPreparation = results,
            frequency = mode.frequencyLabel,
            mgPerDose = "${formatMg(mg)} mg / dose",
            capped = capped,
        )
    }

    /**
     * Holliday-Segar daily maintenance fluid in mL.
     */
    fun hollidaySegarPerDay(weightKg: Double): Double {
        val a = min(weightKg, 10.0) * 100.0
        val b = max(0.0, min(weightKg, 20.0) - 10.0) * 50.0
        val c = max(0.0, weightKg - 20.0) * 20.0
        return a + b + c
    }

    fun formatCc(value: Double): String {
        if (value <= 0.0) return "0"
        val rounded = (value * 100).roundToInt() / 100.0
        return if (rounded == rounded.toLong().toDouble()) {
            rounded.toLong().toString()
        } else {
            "%.2f".format(rounded)
        }
    }

    fun formatMg(value: Double): String {
        val rounded = (value * 10).roundToInt() / 10.0
        return if (rounded == rounded.toLong().toDouble()) {
            rounded.toLong().toString()
        } else {
            "%.1f".format(rounded)
        }
    }
}
