package com.rroot.pediatricdose.data

/**
 * A weight-based paediatric dose regimen.
 *
 * The reference ranges here are conservative, widely cited paediatric
 * starting doses (BNFc / Nelson / AAP). They are NOT a substitute for a
 * formulary lookup; callers must verify against local protocols.
 */
data class DoseRegimen(
    /** What this regimen treats (e.g. "Mild-moderate pain / fever"). */
    val indication: String,
    /** Minimum mg/kg per dose. */
    val minMgPerKg: Double,
    /** Maximum mg/kg per dose. */
    val maxMgPerKg: Double,
    /** Hard cap on a single dose, in mg (usually the adult maximum). */
    val maxSingleMg: Double? = null,
    /** Hard cap on total daily dose, in mg. */
    val maxDailyMg: Double? = null,
    /** Typical dosing interval in hours (e.g. 6 for q6h). Null if situational. */
    val intervalHours: Int? = null,
    /** Route (PO, IV, IM, PR, ...). */
    val route: String = "PO",
    /** Free-text clinical note (contraindications, renal adjustment, etc.). */
    val note: String? = null,
)

data class Drug(
    val name: String,
    /** Short description / drug class. */
    val description: String,
    /**
     * Available liquid concentrations (mg per mL) the user can pick from.
     * Used to convert mg -> mL. Empty if typically not a liquid.
     */
    val liquidConcentrationsMgPerMl: List<Double> = emptyList(),
    val regimens: List<DoseRegimen>,
)
