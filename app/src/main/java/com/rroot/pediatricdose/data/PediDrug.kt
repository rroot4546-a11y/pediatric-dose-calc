package com.rroot.pediatricdose.data

/**
 * Quick-reference paediatric drug entry.
 *
 * Two completely different shapes share this type:
 *
 * 1. **Weight-based** drugs (most antibiotics, paracetamol, ibuprofen,
 *    salbutamol, etc.) → `mode = WeightBased(...)`. The dose is mg/kg/dose
 *    multiplied by the entered weight, then converted to a volume in cc
 *    using the listed concentration. Multiple [preparations] (e.g. Adol
 *    120 mg/5 mL, 125 mg/5 mL, 250 mg/5 mL) all show their own cc result
 *    side-by-side, since pharmacies stock whatever they happen to have.
 *
 * 2. **Age-banded** drugs (Zinc, cough syrups, antihistamines like
 *    Loratidine / Desloratidine, simethicone drops) → `mode = AgeBanded(...)`.
 *    These don't need weight; the user picks the age band on the row.
 *
 * Each drug also carries a [reference] string (BNFc / UpToDate / Nelson's),
 * a colour [accent] used for the row band, and a free-text [notes] field
 * that is shown when the user toggles "Maximum dose / details".
 */
data class PediDrug(
    val id: String,
    val name: String,
    /** Optional brand alias / chemical synonym shown beside the brand. */
    val genericLabel: String? = null,
    val sectionHeader: String? = null,
    val mode: DoseMode,
    val notes: String = "",
    val reference: String = "BNFc 2024 / UpToDate",
    val accent: Accent = Accent.Cyan,
)

enum class Accent { Cyan, Gray, Yellow, Red, Plain }

/** A specific concentration the syrup is sold at (mg of drug per mL). */
data class Preparation(
    val label: String,        // shown to user, e.g. "120 mg/5 mL"
    val mgPerCc: Double,      // resolved concentration: 120/5 = 24
)

/** A clinically-bounded age band with a fixed dose. */
data class AgeBand(
    val label: String,        // "below 6 months"
    val cc: Double,           // 5 (mL per dose)
    val frequency: String,    // "× 1 / day for 14 days"
)

sealed class DoseMode {

    /**
     * Volume in cc per dose at given vial concentration (single preparation).
     * Used by injectables on the Injections card.
     */
    data class VolumeMgPerKg(
        val mgPerKg: Double,
        val mgPerCc: Double,
        val frequencyLabel: String,
        val maxMgPerDose: Double? = null,
    ) : DoseMode()

    /** A pure mass per dose (for "by NG tube / PR" lines in mg). */
    data class MassMgPerKg(
        val mgPerKg: Double,
        val frequencyLabel: String,
        val maxMgPerDose: Double? = null,
        val unitLabel: String = "mg",
    ) : DoseMode()

    /** Holliday-Segar maintenance fluid: cc / 24 h. */
    object MaintenanceHollidaySegar : DoseMode()

    /** Single bolus mL/kg (e.g. 20 mL/kg normal saline). */
    data class BolusMlPerKg(val mlPerKg: Double) : DoseMode()

    /**
     * Continuous IV infusion delivered by syringe pump.
     * cc/h = mgPerKgPerHour × kg × finalCc / (drugMgPerCc × diluentSourceCc).
     */
    data class Infusion(
        val mgPerKgPerHour: Double,
        val drugMgPerCc: Double,
        val diluentSourceCc: Double,
        val finalCc: Double,
        val recipeText: String,
    ) : DoseMode()

    /** Adrenaline / atropine: shows BOTH cc volume and mg dose. */
    data class VolumeAndMass(
        val mgPerKg: Double,
        val mgPerCc: Double,
        val frequencyLabel: String,
        val minMg: Double? = null,
        val maxMg: Double? = null,
        val unitLabel: String = "mg",
    ) : DoseMode()

    /** Free text recipe (for drugs that don't fit the calculators). */
    data class FreeText(val text: String) : DoseMode()

    /**
     * Weight-based oral syrup. The same mg/kg dose is computed against every
     * stocked [preparations] entry so the user sees a result for any
     * concentration the pharmacy gives them.
     */
    data class WeightBasedSyrup(
        val mgPerKgPerDose: Double,
        val frequencyLabel: String,
        val preparations: List<Preparation>,
        val maxMgPerDose: Double? = null,
    ) : DoseMode()

    /**
     * Age-banded oral syrup. No weight calculation; the user chooses the
     * age row, the dose volume is fixed.
     */
    data class AgeBanded(
        val preparations: List<Preparation>,
        val bands: List<AgeBand>,
    ) : DoseMode()
}
