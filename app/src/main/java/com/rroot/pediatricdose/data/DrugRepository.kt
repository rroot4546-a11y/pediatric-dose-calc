package com.rroot.pediatricdose.data

/**
 * Built-in paediatric drug list, ranges aligned with BNFc (British
 * National Formulary for Children) monographs.
 *
 * Where BNFc uses age-banded rather than weight-based dosing (adrenaline,
 * benzodiazepines, nebulised salbutamol), the age bands are spelled out
 * in the [DoseRegimen.note] field and the calculator falls back to the
 * widely used APLS / Resuscitation Council UK weight-based approximation.
 *
 * Clinicians must confirm every value against the live BNFc monograph
 * (https://bnfc.nice.org.uk/) and against local prescribing protocols.
 */
object DrugRepository {

    val drugs: List<Drug> = listOf(
        Drug(
            name = "Paracetamol (Acetaminophen)",
            description = "Analgesic / antipyretic",
            liquidConcentrationsMgPerMl = listOf(24.0, 32.0, 50.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Fever / mild-moderate pain (PO)",
                    minMgPerKg = 10.0,
                    maxMgPerKg = 15.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 4000.0,
                    intervalHours = 6,
                    route = "PO",
                    note = "BNFc: 15 mg/kg every 4\u20136 h (max 4 doses in 24 h). " +
                        "Max 75 mg/kg/day in children, 60 mg/kg/day in neonates. " +
                        "BNFc also lists age-banded fixed doses; verify in the live monograph.",
                    reference = "BNFc",
                ),
                DoseRegimen(
                    indication = "Fever / pain (PR)",
                    minMgPerKg = 15.0,
                    maxMgPerKg = 20.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 4000.0,
                    intervalHours = 6,
                    route = "PR",
                    note = "BNFc: loading 20 mg/kg PR, then 15 mg/kg every 4\u20136 h.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Ibuprofen",
            description = "NSAID analgesic / antipyretic",
            liquidConcentrationsMgPerMl = listOf(20.0, 40.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Fever / pain",
                    minMgPerKg = 5.0,
                    maxMgPerKg = 10.0,
                    maxSingleMg = 400.0,
                    maxDailyMg = 1200.0,
                    intervalHours = 8,
                    route = "PO",
                    note = "BNFc: 5\u201310 mg/kg every 6\u20138 h, max 30 mg/kg/day " +
                        "(max 2.4 g/day in older children/adolescents). " +
                        "Avoid <3 months, dehydration, renal impairment, active GI bleeding.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Amoxicillin",
            description = "Aminopenicillin antibiotic",
            liquidConcentrationsMgPerMl = listOf(25.0, 50.0, 80.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Standard infection (TDS)",
                    minMgPerKg = 25.0,
                    maxMgPerKg = 30.0,
                    maxSingleMg = 500.0,
                    maxDailyMg = 1500.0,
                    intervalHours = 8,
                    route = "PO",
                    note = "BNFc (age-banded): 1\u201311 mo 125 mg TDS; 1\u20134 y 250 mg TDS; " +
                        "5\u201311 y 500 mg TDS; 12\u201317 y 500 mg TDS (up to 1 g TDS). " +
                        "Weight-based approximation: 25\u201330 mg/kg TDS (max 1 g/dose).",
                    reference = "BNFc",
                ),
                DoseRegimen(
                    indication = "Severe infection / high-dose (TDS)",
                    minMgPerKg = 30.0,
                    maxMgPerKg = 45.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 3000.0,
                    intervalHours = 8,
                    route = "PO",
                    note = "BNFc: up to 30 mg/kg TDS (max 1 g/dose) for severe infection. " +
                        "Some UK trust protocols use 45 mg/kg TDS. " +
                        "Note: the US 80\u201390 mg/kg/day high-dose AOM regimen is NOT BNFc.",
                    reference = "BNFc + local trust formulary",
                ),
            ),
        ),
        Drug(
            name = "Azithromycin",
            description = "Macrolide antibiotic",
            liquidConcentrationsMgPerMl = listOf(40.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Respiratory / skin infection, pertussis",
                    minMgPerKg = 10.0,
                    maxMgPerKg = 10.0,
                    maxSingleMg = 500.0,
                    intervalHours = 24,
                    route = "PO",
                    note = "BNFc: 10 mg/kg once daily for 3 days (max 500 mg). " +
                        "Pertussis: 10 mg/kg OD day 1 then 5 mg/kg OD days 2\u20135.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Cefalexin",
            description = "1st-gen cephalosporin",
            liquidConcentrationsMgPerMl = listOf(25.0, 50.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Skin / soft-tissue, UTI",
                    minMgPerKg = 12.5,
                    maxMgPerKg = 25.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 4000.0,
                    intervalHours = 8,
                    route = "PO",
                    note = "BNFc (age-banded): Child 1 mo\u201311 mo 12.5 mg/kg BD; " +
                        "1\u20134 y 12.5 mg/kg BD/TDS; 5\u201311 y 12.5\u201325 mg/kg BD/TDS; " +
                        "12\u201317 y 500 mg BD/TDS (up to 1\u20131.5 g QDS in severe infection).",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Co-amoxiclav",
            description = "Amoxicillin + clavulanate (doses expressed as amoxicillin)",
            liquidConcentrationsMgPerMl = listOf(25.0, 50.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Standard (TDS)",
                    minMgPerKg = 20.0,
                    maxMgPerKg = 30.0,
                    maxSingleMg = 875.0,
                    intervalHours = 8,
                    route = "PO",
                    note = "BNFc: dosed by formulation (125/31, 250/62, 400/57 suspensions). " +
                        "Weight-based approximation: 20\u201330 mg/kg (amoxicillin component) TDS. " +
                        "Adult 12\u201317 y: 500/125 mg TDS.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Ondansetron",
            description = "5-HT3 antiemetic",
            liquidConcentrationsMgPerMl = listOf(0.8),
            regimens = listOf(
                DoseRegimen(
                    indication = "Nausea / vomiting (PO/IV)",
                    minMgPerKg = 0.1,
                    maxMgPerKg = 0.15,
                    maxSingleMg = 8.0,
                    intervalHours = 8,
                    route = "PO/IV",
                    note = "BNFc: 100\u2013150 micrograms/kg every 8\u201312 h (max 4 mg IV, 8 mg PO). " +
                        "Caution with QT prolongation and concomitant serotonergic drugs.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Dexamethasone (croup)",
            description = "Corticosteroid",
            liquidConcentrationsMgPerMl = listOf(1.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Croup (single dose PO)",
                    minMgPerKg = 0.15,
                    maxMgPerKg = 0.15,
                    maxSingleMg = 8.0,
                    intervalHours = null,
                    route = "PO",
                    note = "BNFc: 150 micrograms/kg (0.15 mg/kg) as a single dose, " +
                        "may repeat after 12 h if symptoms persist. " +
                        "Historical 0.6 mg/kg dose is used in some centres but is not BNFc.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Prednisolone",
            description = "Corticosteroid",
            liquidConcentrationsMgPerMl = listOf(1.0, 3.0),
            regimens = listOf(
                DoseRegimen(
                    indication = "Acute asthma exacerbation",
                    minMgPerKg = 1.0,
                    maxMgPerKg = 2.0,
                    maxSingleMg = 40.0,
                    intervalHours = 24,
                    route = "PO",
                    note = "BNFc: Child 1 mo\u201311 y 1\u20132 mg/kg OD (max 40 mg/day). " +
                        "Child 12\u201317 y 40\u201350 mg OD. " +
                        "3\u20135 day course, no taper needed unless course > 14 days.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Salbutamol (nebulised)",
            description = "Short-acting beta-2 agonist",
            regimens = listOf(
                DoseRegimen(
                    indication = "Acute wheeze / asthma (age-banded)",
                    minMgPerKg = 0.0,
                    maxMgPerKg = 0.0,
                    maxSingleMg = 5.0,
                    intervalHours = null,
                    route = "NEB",
                    note = "BNFc fixed dose (NOT weight-based): " +
                        "Child <5 y \u2192 2.5 mg per dose; child \u22655 y \u2192 5 mg per dose. " +
                        "Repeat every 20\u201330 min in acute exacerbation, then titrate to response.",
                    reference = "BNFc",
                ),
            ),
        ),
        Drug(
            name = "Adrenaline IM (1:1000)",
            description = "Anaphylaxis",
            regimens = listOf(
                DoseRegimen(
                    indication = "Anaphylaxis IM (weight-based fallback)",
                    minMgPerKg = 0.01,
                    maxMgPerKg = 0.01,
                    maxSingleMg = 0.5,
                    intervalHours = null,
                    route = "IM",
                    note = "BNFc / Resus Council UK age-banded: " +
                        "<6 mo 100\u2013150 mcg (0.1\u20130.15 mL); 6 mo\u20135 y 150 mcg (0.15 mL); " +
                        "6\u201311 y 300 mcg (0.3 mL); 12\u201317 y 500 mcg (0.5 mL; 300 mcg if small). " +
                        "Weight-based fallback: 0.01 mg/kg of 1:1000 (= 0.01 mL/kg). " +
                        "Repeat every 5 min as needed.",
                    reference = "BNFc + Resus Council UK",
                ),
            ),
        ),
        Drug(
            name = "Diazepam (rectal)",
            description = "Benzodiazepine, status epilepticus",
            regimens = listOf(
                DoseRegimen(
                    indication = "Status epilepticus PR (weight-based fallback)",
                    minMgPerKg = 0.5,
                    maxMgPerKg = 0.5,
                    maxSingleMg = 10.0,
                    intervalHours = null,
                    route = "PR",
                    note = "BNFc age-banded: Neonate 1.25\u20132.5 mg; 1 mo\u20131 y 5 mg; " +
                        "2\u201311 y 5\u201310 mg; 12\u201317 y 10 mg. " +
                        "APLS weight-based fallback: 0.5 mg/kg (max 10 mg). " +
                        "May repeat once after 10 min if seizure continues.",
                    reference = "BNFc + APLS",
                ),
            ),
        ),
        Drug(
            name = "Midazolam (buccal)",
            description = "Benzodiazepine, status epilepticus",
            regimens = listOf(
                DoseRegimen(
                    indication = "Status epilepticus buccal (weight-based fallback)",
                    minMgPerKg = 0.3,
                    maxMgPerKg = 0.3,
                    maxSingleMg = 10.0,
                    intervalHours = null,
                    route = "BUCCAL",
                    note = "BNFc age-banded: Neonate\u20132 mo 300 mcg/kg; 3\u201311 mo 2.5 mg; " +
                        "1\u20134 y 5 mg; 5\u20139 y 7.5 mg; 10\u201317 y 10 mg. " +
                        "APLS weight-based fallback: 0.3 mg/kg (max 10 mg).",
                    reference = "BNFc + APLS",
                ),
            ),
        ),
    )

    fun findByName(name: String): Drug? = drugs.firstOrNull { it.name == name }
}
