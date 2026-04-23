package com.rroot.pediatricdose.data

/**
 * A weight-based paediatric dose regimen.
 *
 * Primary reference: BNFc (British National Formulary for Children,
 * https://bnfc.nice.org.uk/). Where BNFc specifies age-banded rather than
 * weight-based dosing (e.g. adrenaline, benzodiazepines, salbutamol),
 * the age bands are documented in the [note] field and a pragmatic
 * weight-based approximation (APLS / Resuscitation Council UK) is used
 * for the calculator.
 *
 * Values are reference aids only; clinicians must verify against the live
 * BNFc monograph and local protocols before administration.
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
    /**
     * Primary reference citation (e.g. "BNFc" or "BNFc + Resus Council UK").
     * Rendered on the drug detail screen so clinicians can trace each dose
     * back to its source.
     */
    val reference: String = "BNFc",
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
