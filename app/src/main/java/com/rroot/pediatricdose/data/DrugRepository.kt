package com.rroot.pediatricdose.data

/**
 * Built-in paediatric drug list with standard weight-based dosing.
 *
 * References: BNFc 2024, Nelson Textbook of Pediatrics, AAP Red Book.
 * Values are conservative and must be verified locally before use.
 */
object DrugRepository {

    val drugs: List<Drug> = listOf(
        Drug(
            name = "Paracetamol (Acetaminophen)",
            description = "Analgesic / antipyretic",
            liquidConcentrationsMgPerMl = listOf(24.0, 32.0, 50.0), // 120/5, 160/5, 250/5
            regimens = listOf(
                DoseRegimen(
                    indication = "Fever / mild-moderate pain (PO)",
                    minMgPerKg = 10.0,
                    maxMgPerKg = 15.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 4000.0,
                    intervalHours = 6,
                    route = "PO",
                    note = "Max 75 mg/kg/day in children, 60 mg/kg/day in neonates.",
                ),
                DoseRegimen(
                    indication = "Fever / pain (PR)",
                    minMgPerKg = 15.0,
                    maxMgPerKg = 20.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 4000.0,
                    intervalHours = 6,
                    route = "PR",
                ),
            ),
        ),
        Drug(
            name = "Ibuprofen",
            description = "NSAID analgesic / antipyretic",
            liquidConcentrationsMgPerMl = listOf(20.0, 40.0), // 100/5, 200/5
            regimens = listOf(
                DoseRegimen(
                    indication = "Fever / pain",
                    minMgPerKg = 5.0,
                    maxMgPerKg = 10.0,
                    maxSingleMg = 400.0,
                    maxDailyMg = 1200.0,
                    intervalHours = 8,
                    route = "PO",
                    note = "Avoid <3 months, dehydration, renal impairment, active GI bleeding.",
                ),
            ),
        ),
        Drug(
            name = "Amoxicillin",
            description = "Aminopenicillin antibiotic",
            liquidConcentrationsMgPerMl = listOf(25.0, 50.0, 80.0), // 125/5, 250/5, 400/5
            regimens = listOf(
                DoseRegimen(
                    indication = "Standard infection (divided TDS)",
                    minMgPerKg = 25.0,
                    maxMgPerKg = 50.0,
                    maxSingleMg = 500.0,
                    maxDailyMg = 1500.0,
                    intervalHours = 8,
                    route = "PO",
                ),
                DoseRegimen(
                    indication = "Acute otitis media / pneumonia (high-dose)",
                    minMgPerKg = 80.0,
                    maxMgPerKg = 90.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 3000.0,
                    intervalHours = 12,
                    route = "PO",
                    note = "High-dose amoxicillin; divided BID or TID.",
                ),
            ),
        ),
        Drug(
            name = "Azithromycin",
            description = "Macrolide antibiotic",
            liquidConcentrationsMgPerMl = listOf(40.0), // 200/5
            regimens = listOf(
                DoseRegimen(
                    indication = "Community-acquired pneumonia, pertussis",
                    minMgPerKg = 10.0,
                    maxMgPerKg = 10.0,
                    maxSingleMg = 500.0,
                    intervalHours = 24,
                    route = "PO",
                    note = "Day 1: 10 mg/kg; days 2-5: 5 mg/kg/day (max 250 mg).",
                ),
            ),
        ),
        Drug(
            name = "Cefalexin",
            description = "1st-gen cephalosporin",
            liquidConcentrationsMgPerMl = listOf(25.0, 50.0), // 125/5, 250/5
            regimens = listOf(
                DoseRegimen(
                    indication = "Skin / soft-tissue, UTI",
                    minMgPerKg = 25.0,
                    maxMgPerKg = 50.0,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 4000.0,
                    intervalHours = 6,
                    route = "PO",
                ),
            ),
        ),
        Drug(
            name = "Co-amoxiclav",
            description = "Amoxicillin + clavulanate",
            liquidConcentrationsMgPerMl = listOf(25.0, 50.0), // dosed as amoxicillin component
            regimens = listOf(
                DoseRegimen(
                    indication = "Standard (dose based on amoxicillin component)",
                    minMgPerKg = 25.0,
                    maxMgPerKg = 45.0,
                    maxSingleMg = 875.0,
                    intervalHours = 12,
                    route = "PO",
                ),
            ),
        ),
        Drug(
            name = "Ondansetron",
            description = "5-HT3 antiemetic",
            liquidConcentrationsMgPerMl = listOf(0.8), // 4 mg/5 mL
            regimens = listOf(
                DoseRegimen(
                    indication = "Nausea / vomiting",
                    minMgPerKg = 0.1,
                    maxMgPerKg = 0.15,
                    maxSingleMg = 8.0,
                    intervalHours = 8,
                    route = "PO/IV",
                    note = "Caution with QT prolongation.",
                ),
            ),
        ),
        Drug(
            name = "Dexamethasone",
            description = "Corticosteroid",
            liquidConcentrationsMgPerMl = listOf(1.0), // oral solution 1 mg/mL
            regimens = listOf(
                DoseRegimen(
                    indication = "Croup (single dose)",
                    minMgPerKg = 0.15,
                    maxMgPerKg = 0.6,
                    maxSingleMg = 16.0,
                    intervalHours = null,
                    route = "PO",
                    note = "Usually 0.15-0.6 mg/kg single dose.",
                ),
            ),
        ),
        Drug(
            name = "Prednisolone",
            description = "Corticosteroid",
            liquidConcentrationsMgPerMl = listOf(1.0, 3.0), // 5/5, 15/5
            regimens = listOf(
                DoseRegimen(
                    indication = "Asthma exacerbation",
                    minMgPerKg = 1.0,
                    maxMgPerKg = 2.0,
                    maxSingleMg = 40.0,
                    intervalHours = 24,
                    route = "PO",
                    note = "3-5 day course, typically no taper.",
                ),
            ),
        ),
        Drug(
            name = "Salbutamol (nebulized)",
            description = "Short-acting beta-2 agonist",
            regimens = listOf(
                DoseRegimen(
                    indication = "Acute wheeze (<5 y)",
                    minMgPerKg = 0.0, // weight-independent
                    maxMgPerKg = 0.0,
                    maxSingleMg = 2.5,
                    intervalHours = null,
                    route = "NEB",
                    note = "Fixed dose: 2.5 mg (<5 y), 5 mg (>=5 y). Not weight-based.",
                ),
            ),
        ),
        Drug(
            name = "Adrenaline (IM, 1:1000)",
            description = "Anaphylaxis",
            regimens = listOf(
                DoseRegimen(
                    indication = "Anaphylaxis IM",
                    minMgPerKg = 0.01,
                    maxMgPerKg = 0.01,
                    maxSingleMg = 0.5,
                    intervalHours = null,
                    route = "IM",
                    note = "0.01 mg/kg of 1:1000 (1 mg/mL) = 0.01 mL/kg. Repeat q5-15 min prn.",
                ),
            ),
        ),
        Drug(
            name = "Diazepam (rectal)",
            description = "Benzodiazepine, status epilepticus",
            regimens = listOf(
                DoseRegimen(
                    indication = "Status epilepticus PR",
                    minMgPerKg = 0.5,
                    maxMgPerKg = 0.5,
                    maxSingleMg = 10.0,
                    intervalHours = null,
                    route = "PR",
                    note = "May repeat once after 10 minutes.",
                ),
            ),
        ),
        Drug(
            name = "Midazolam (buccal)",
            description = "Benzodiazepine, status epilepticus",
            regimens = listOf(
                DoseRegimen(
                    indication = "Status epilepticus buccal",
                    minMgPerKg = 0.3,
                    maxMgPerKg = 0.5,
                    maxSingleMg = 10.0,
                    intervalHours = null,
                    route = "BUCCAL",
                    note = "Age-banded doses also commonly used.",
                ),
            ),
        ),
    )

    fun findByName(name: String): Drug? = drugs.firstOrNull { it.name == name }
}
