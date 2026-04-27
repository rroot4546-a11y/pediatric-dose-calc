package com.rroot.pediatricdose.data

import com.rroot.pediatricdose.data.DoseMode.*

/**
 * Bedside paediatric quick-reference card.
 *
 * Drugs are grouped by clinical role. Doses are weight-based standard ward
 * values (BNFc 2024 / Nelson's Pediatrics 22e / Iraqi paediatric ward
 * practice). Vial concentrations follow the most common preparations sold
 * locally (Iraq / Middle East), since that is the audience.
 *
 * Always verify with the prescribing physician — this list is decision
 * support, not a substitute for the BNFc.
 */
object PediDrugList {

    val all: List<PediDrug> = listOf(
        // ------------------------------------------------------------------
        // Fluids
        // ------------------------------------------------------------------
        PediDrug(
            id = "ns-bolus",
            name = "Normal Saline bolus",
            sectionHeader = "IV Fluids",
            mode = BolusMlPerKg(mlPerKg = 20.0),
            notes = "20 mL/kg single bolus over 5–20 min. Reassess and repeat if shock persists.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "maintenance",
            name = "Maintenance fluid (G/S)",
            mode = MaintenanceHollidaySegar,
            notes = "Holliday-Segar: 100 mL/kg/d for first 10 kg, 50 mL/kg/d for next 10 kg, 20 mL/kg/d above 20 kg.",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Analgesics / antipyretics
        // ------------------------------------------------------------------
        PediDrug(
            id = "paracetamol-iv",
            name = "Paracetamol vial (IV) 1 g/100 mL",
            sectionHeader = "Analgesics & antipyretics",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 10.0,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 1000.0,
            ),
            notes = "15 mg/kg/dose every 6 h. Max 60 mg/kg/day (or 1 g per dose).",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "paracetamol-amp",
            name = "Paracetamol amp (PR/oral)",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 100.0 / 1.0,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 1000.0,
            ),
            notes = "15 mg/kg/dose × 4. Same daily cap (60 mg/kg/d).",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Anti-emetic
        // ------------------------------------------------------------------
        PediDrug(
            id = "ondansetron",
            name = "De-vomit amp (Ondansetron) 8 mg / 4 mL",
            sectionHeader = "Anti-emetic / steroid / antihistamine",
            mode = VolumeMgPerKg(
                mgPerKg = 0.15,
                mgPerCc = 2.0,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 8.0,
            ),
            notes = "0.15 mg/kg slow IV (max 8 mg). Max 3 doses/day.",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Steroids / antihistamine
        // ------------------------------------------------------------------
        PediDrug(
            id = "hydrocortisone",
            name = "H.C vial (Hydrocortisone) 100 mg + 2 mL N.S",
            mode = VolumeMgPerKg(
                mgPerKg = 4.0,
                mgPerCc = 50.0,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 100.0,
            ),
            notes = "Reconstitute 100 mg vial in 2 mL → 50 mg/cc. Status asthmaticus / anaphylaxis: 4 mg/kg × 4.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "chlorpheniramine",
            name = "Allermine amp (Chlorpheniramine) 10 mg/1 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 0.2,
                mgPerCc = 10.0,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 10.0,
            ),
            notes = "0.2 mg/kg/dose IV/IM, max 10 mg.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "dexamethasone",
            name = "Decadron amp (Dexamethasone) 8 mg/2 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 0.6,
                mgPerCc = 4.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 16.0,
            ),
            notes = "0.6 mg/kg (croup, asthma exacerbation, cerebral oedema). Max 16 mg.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "furosemide",
            name = "Lasix amp (Furosemide) 20 mg/2 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 1.0,
                mgPerCc = 10.0,
                frequencyLabel = "single dose",
                maxMgPerDose = 40.0,
            ),
            notes = "1 mg/kg slow IV. Max 40 mg per dose.",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Resuscitation
        // ------------------------------------------------------------------
        PediDrug(
            id = "adrenaline",
            name = "Adrenaline 1:1000 (1 mg/1 mL)",
            sectionHeader = "Resuscitation",
            mode = VolumeAndMass(
                mgPerKg = 0.01,
                mgPerCc = 1.0,
                frequencyLabel = "as needed",
                maxMg = 0.5,
            ),
            notes = "Anaphylaxis IM 0.01 mg/kg = 0.01 mL/kg of 1:1000 (max 0.5 mg). For arrest IV use 1:10 000 (10 µg/kg).",
            accent = Accent.Gray,
        ),
        PediDrug(
            id = "atropine",
            name = "Atropine amp 0.6 mg/1 mL",
            mode = VolumeAndMass(
                mgPerKg = 0.02,
                mgPerCc = 0.6,
                frequencyLabel = "as needed",
                minMg = 0.1,
                maxMg = 1.0,
            ),
            notes = "0.02 mg/kg IV. Minimum 0.1 mg (avoid paradoxical bradycardia). Max 1 mg single dose.",
            accent = Accent.Gray,
        ),

        // ------------------------------------------------------------------
        // Asthma
        // ------------------------------------------------------------------
        PediDrug(
            id = "aminophylline",
            name = "Aminophylline 250 mg/10 cc — loading + 30 cc N.S over 30 min",
            sectionHeader = "Asthma / bronchospasm",
            mode = VolumeMgPerKg(
                mgPerKg = 5.0,
                mgPerCc = 25.0,
                frequencyLabel = "loading",
                maxMgPerDose = 500.0,
            ),
            notes = "Loading 5 mg/kg over 30 min in 30 mL N.S, then maintenance 0.9 mg/kg/h infusion. Avoid bolus if patient on theophylline.",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Sedation / anticonvulsants
        // ------------------------------------------------------------------
        PediDrug(
            id = "diazepam-iv",
            name = "Valium amp (Diazepam) 10 mg/2 cc — Bolus IV +2 cc N.S, slow 3–5 min",
            sectionHeader = "Sedation & anticonvulsants",
            mode = VolumeMgPerKg(
                mgPerKg = 0.3,
                mgPerCc = 5.0,
                frequencyLabel = "single bolus",
                maxMgPerDose = 10.0,
            ),
            notes = "0.3 mg/kg IV (max 10 mg). Have flumazenil + bag-mask ready. Do not exceed 5 mg/min.",
            accent = Accent.Yellow,
        ),
        PediDrug(
            id = "diazepam-pr",
            name = "Diazepam — Rectally / by small NG tube",
            mode = MassMgPerKg(
                mgPerKg = 0.5,
                frequencyLabel = "single dose",
                maxMgPerDose = 10.0,
            ),
            notes = "0.5 mg/kg PR if no IV access. Max 10 mg.",
            accent = Accent.Yellow,
        ),
        PediDrug(
            id = "luminal-bolus",
            name = "Luminal (Phenobarbitone) — Bolus 200 mg/1 cc + 10 cc N.S over 20 min",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 200.0,
                frequencyLabel = "loading",
                maxMgPerDose = 1000.0,
            ),
            notes = "Loading 20 mg/kg slow IV over 20 min (max 1 g, neonatal dose 20 mg/kg). Watch for respiratory depression.",
            accent = Accent.Red,
        ),
        PediDrug(
            id = "luminal-maint",
            name = "Luminal — Maintenance",
            mode = MassMgPerKg(
                mgPerKg = 5.0,
                frequencyLabel = "÷ 2 / day",
                maxMgPerDose = 250.0,
            ),
            notes = "5 mg/kg/day divided in 2 doses (PO/IV). Adjust to therapeutic level 15–40 mg/L.",
            accent = Accent.Red,
        ),
        PediDrug(
            id = "phenytoin-bolus",
            name = "Phenytoin amp 250 mg/5 mL — Bolus +10 cc N.S over 20 min",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 50.0,
                frequencyLabel = "loading",
                maxMgPerDose = 1500.0,
            ),
            notes = "Loading 20 mg/kg IV (rate ≤ 1 mg/kg/min, max 50 mg/min). Monitor ECG. Use saline only — incompatible with dextrose.",
            accent = Accent.Red,
        ),
        PediDrug(
            id = "phenytoin-maint",
            name = "Phenytoin — Maintenance",
            mode = VolumeMgPerKg(
                mgPerKg = 5.0,
                mgPerCc = 50.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 300.0,
            ),
            notes = "5 mg/kg/day divided in 2. Therapeutic 10–20 mg/L.",
            accent = Accent.Red,
        ),
        PediDrug(
            id = "midazolam-infusion",
            name = "Midazolam amp 15 mg/3 mL — infusion (1 cc midaz + 49 cc N.S → 50 cc by syringe pump)",
            mode = Infusion(
                mgPerKgPerHour = 0.1,
                drugMgPerCc = 5.0,
                diluentSourceCc = 1.0,
                finalCc = 50.0,
                recipeText = "Add 1 cc Midaz to 49 cc N.S → 50 cc total. Run by syringe pump.",
            ),
            notes = "0.1 mg/kg/h infusion = (kg) cc/hour with this recipe. Range 0.05–0.4 mg/kg/h. Bolus 0.05–0.1 mg/kg before starting.",
            accent = Accent.Yellow,
        ),

        // ------------------------------------------------------------------
        // Antibiotics
        // ------------------------------------------------------------------
        PediDrug(
            id = "ampicillin",
            name = "Ampicillin vial 500 mg + 5 cc N.S",
            sectionHeader = "Antibiotics",
            mode = VolumeMgPerKg(
                mgPerKg = 50.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 2000.0,
            ),
            notes = "50 mg/kg/dose × 2 (×4 in newborns / meningitis: 100 mg/kg × 4). Max 12 g/day.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "amoxicillin",
            name = "Amoxil vial 500 mg + 5 cc N.S",
            mode = VolumeMgPerKg(
                mgPerKg = 25.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 1000.0,
            ),
            notes = "25 mg/kg × 2 (high dose 45 mg/kg × 2 in otitis / pneumonia).",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "ceftriaxone",
            name = "Ceftriaxone vial 1 g + 10 cc N.S",
            mode = VolumeMgPerKg(
                mgPerKg = 75.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 1 or ÷ 2",
                maxMgPerDose = 4000.0,
            ),
            notes = "50–75 mg/kg/day × 1; meningitis 100 mg/kg/day ÷ 2. Avoid in neonates with calcium-containing fluids.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "cefotaxime",
            name = "Claforan vial (Cefotaxime) 1 g + 10 cc N.S",
            mode = VolumeMgPerKg(
                mgPerKg = 50.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 2000.0,
            ),
            notes = "50 mg/kg/dose × 2 (× 3 in severe infection or neonatal sepsis).",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "gentamicin-large",
            name = "Garamycin (Gentamicin) 80 mg/2 mL + 6 cc N.S",
            mode = VolumeMgPerKg(
                mgPerKg = 7.5,
                mgPerCc = 10.0,
                frequencyLabel = "once daily",
                maxMgPerDose = 360.0,
            ),
            notes = "7.5 mg/kg once daily (or 2.5 mg/kg × 3 in neonates). Monitor renal function & trough level.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "gentamicin-small",
            name = "Garamycin amp 20 mg/2 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 7.5,
                mgPerCc = 10.0,
                frequencyLabel = "once daily",
                maxMgPerDose = 360.0,
            ),
            notes = "Same dose as 80 mg vial; smaller amp for infants.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "metronidazole",
            name = "Flagyl bottle 500 mg / 100 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 7.5,
                mgPerCc = 5.0,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 500.0,
            ),
            notes = "7.5 mg/kg × 3. Anaerobic cover, intra-abdominal sepsis, brain abscess.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "ceftazidime",
            name = "Ceftazidime vial 1 g + 10 cc N.S",
            mode = VolumeMgPerKg(
                mgPerKg = 50.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 2000.0,
            ),
            notes = "50 mg/kg/dose × 3. Pseudomonas cover (cystic fibrosis exacerbation, febrile neutropenia).",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "acyclovir",
            name = "Acyclovir vial 250 mg + 5 cc (Zovirax)",
            mode = VolumeMgPerKg(
                mgPerKg = 10.0,
                mgPerCc = 50.0,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 500.0,
            ),
            notes = "10 mg/kg/dose × 3 (HSV encephalitis 20 mg/kg × 3 in <12 y). Maintain hydration to prevent crystalluria.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "amikacin-100",
            name = "Amikacin amp 100 mg/2 cc",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 50.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 750.0,
            ),
            notes = "15 mg/kg/day in 2 divided doses (or once-daily extended interval). Trough monitoring.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "amikacin-500",
            name = "Amikacin amp 500 mg/2 cc + 3 cc N.S",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 750.0,
            ),
            notes = "Same total dose; larger amp for older children.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "vancomycin",
            name = "Vancomycin vial 500 + 5 cc N.S in 30 cc N.S /hour × 3",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 1000.0,
            ),
            notes = "15 mg/kg/dose × 3 (× 4 in MRSA/CNS). Run over 60 min minimum to avoid red-man syndrome. Monitor trough.",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "meropenem-neonate",
            name = "MERONEM 500 mg + 5 cc N.S — Neonate, in 20 cc N.S / 30 min × 2",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 1000.0,
            ),
            notes = "Neonatal sepsis: 20 mg/kg × 2. Always extended infusion (≥ 30 min).",
            accent = Accent.Yellow,
        ),
        PediDrug(
            id = "meropenem-after",
            name = "MERONEM 500 mg + 5 cc N.S — After Neonate, in 20 cc N.S / 30 min × 3",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 100.0,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 2000.0,
            ),
            notes = "20 mg/kg × 3 (40 mg/kg × 3 in meningitis / cystic fibrosis). Max 2 g per dose.",
            accent = Accent.Yellow,
        ),
    )
}
