package com.rroot.pediatricdose.data

/**
 * Quick-reference paediatric drug entry as shown on the bedside drug card.
 *
 * Each entry computes either a volume (cc/mL) at the bedside dilution, a mass
 * (mg or units), a fluid rate (cc/hour), or a custom plain-text result, given
 * a child weight in kg. The presentation field [accent] controls the colour
 * band (cyan = standard / continuous, gray = adrenaline-style emphasis,
 * yellow = highlighted infusion / sedation drug, red = warning/maintenance).
 *
 * Notes are deliberately concise and reflect ward-card practice: the
 * detailed prescribing reference is BNFc 2024 / Nelson's Pediatrics 22e and
 * users are expected to verify the final prescription before administration.
 */
data class PediDrug(
    val id: String,
    val name: String,
    val sectionHeader: String? = null,
    val mode: DoseMode,
    val notes: String = "",
    val accent: Accent = Accent.Cyan,
)

enum class Accent { Cyan, Gray, Yellow, Red, Plain }

sealed class DoseMode {
    /** Volume in cc per dose at given vial concentration. */
    data class VolumeMgPerKg(
        val mgPerKg: Double,
        val mgPerCc: Double,
        val frequencyLabel: String,
        val maxMgPerDose: Double? = null,
    ) : DoseMode()

    /** A mass (mg or unit) per dose, used when the bedside card lists units. */
    data class MassMgPerKg(
        val mgPerKg: Double,
        val frequencyLabel: String,
        val maxMgPerDose: Double? = null,
        val unitLabel: String = "mg",
    ) : DoseMode()

    /** Holliday-Segar maintenance fluid: cc/24h. */
    object MaintenanceHollidaySegar : DoseMode()

    /** Single bolus mL/kg (e.g. 20 mL/kg normal saline). */
    data class BolusMlPerKg(val mlPerKg: Double) : DoseMode()

    /**
     * Continuous IV infusion delivered by syringe pump.
     *
     * The card recipe is: take [drugMgPerCc] vial, add [diluentCc] of
     * normal saline, total volume [finalCc] cc. The infusion rate in
     * cc/hour to deliver [mgPerKgPerHour] is then:
     *
     *     ccPerHour = mgPerKgPerHour × kg × finalCc / (drugMgPerCc × diluentSourceCc)
     *
     * Defaults are chosen so the rate equals (kg) cc/hour, matching the
     * bedside shortcut on the original ward card.
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
}
