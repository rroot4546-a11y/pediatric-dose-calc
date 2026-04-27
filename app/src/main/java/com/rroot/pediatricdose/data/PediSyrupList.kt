package com.rroot.pediatricdose.data

import com.rroot.pediatricdose.data.DoseMode.*

/**
 * Quick-reference paediatric oral syrups card.
 *
 * Mirrors the bedside ward card the user shared. Each weight-based syrup
 * is computed against every concentration the pharmacy commonly stocks,
 * so the user can pick whichever bottle they were given. Age-banded
 * preparations (Zinc, cough mixtures, antihistamines for kids without a
 * recorded weight) are listed with their fixed dose volumes.
 *
 * Reference: BNFc 2024 (https://bnfc.nice.org.uk/) and UpToDate paediatric
 * dosing monographs current at time of authoring. The notes column is the
 * authoring shorthand — verify the full monograph before prescribing.
 */
object PediSyrupList {

    val all: List<PediDrug> = listOf(

        // ------------------------------------------------------------------
        // Antipyretics / analgesics
        // ------------------------------------------------------------------
        PediDrug(
            id = "syr-paracetamol",
            name = "Adol (Paracetamol)",
            sectionHeader = "Antipyretics & analgesics",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 15.0,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 1000.0,
                preparations = listOf(
                    Preparation("120 mg / 5 mL", 120.0 / 5.0),
                    Preparation("125 mg / 5 mL", 125.0 / 5.0),
                    Preparation("250 mg / 5 mL", 250.0 / 5.0),
                ),
            ),
            notes = "10–15 mg/kg/dose every 4–6 h, max 4 doses / 24 h, max 60 mg/kg/d. Single-dose cap 1 g.",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-ibuprofen",
            name = "Ibuprofen",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 7.5,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 400.0,
                preparations = listOf(
                    Preparation("40 mg / 1 mL drops", 40.0),
                    Preparation("100 mg / 5 mL", 20.0),
                ),
            ),
            notes = "5–10 mg/kg/dose every 6–8 h. Avoid if dehydrated, on anticoagulants, or with renal impairment. Not under 3 months.",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Oral antibiotics
        // ------------------------------------------------------------------
        PediDrug(
            id = "syr-amoxicillin",
            name = "Amoxil (Amoxicillin)",
            sectionHeader = "Oral antibiotics",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 25.0,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 1000.0,
                preparations = listOf(
                    Preparation("125 mg / 5 mL", 25.0),
                    Preparation("250 mg / 5 mL", 50.0),
                ),
            ),
            notes = "Standard 25 mg/kg × 3. High dose (otitis, pneumonia, suspected pneumococcal): 30 mg/kg × 3.",
            reference = "BNFc 2024 / UpToDate (community-acquired pneumonia)",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-ampiclox",
            name = "Ampiclox (Ampicillin + Cloxacillin)",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 25.0,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 500.0,
                preparations = listOf(
                    Preparation("125 mg / 5 mL", 25.0),
                    Preparation("250 mg / 5 mL", 50.0),
                ),
            ),
            notes = "Combination broad-spectrum penicillin used locally; modern alternatives are amoxicillin or co-amoxiclav.",
            reference = "Local formulary (Iraq) / BNFc 2024",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-erythromycin",
            name = "Erythromycin",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 12.5,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 500.0,
                preparations = listOf(
                    Preparation("125 mg / 5 mL", 25.0),
                    Preparation("250 mg / 5 mL", 50.0),
                ),
            ),
            notes = "Macrolide cover for atypicals (pertussis, mycoplasma). Watch for QT prolongation, drug interactions.",
            reference = "BNFc 2024",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-azithromycin",
            name = "Azithromycin",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 10.0,
                frequencyLabel = "× 1 / day × 3 days",
                maxMgPerDose = 500.0,
                preparations = listOf(
                    Preparation("100 mg / 5 mL", 20.0),
                    Preparation("200 mg / 5 mL", 40.0),
                ),
            ),
            notes = "10 mg/kg once daily for 3 days (or 10 mg/kg day 1 then 5 mg/kg days 2–5). Pertussis: 10 mg/kg × 5 days.",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-flagyl",
            name = "Flagyl (Metronidazole)",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 7.5,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 500.0,
                preparations = listOf(
                    Preparation("125 mg / 5 mL", 25.0),
                    Preparation("200 mg / 5 mL", 40.0),
                ),
            ),
            notes = "Anaerobic / amoebic / Giardia. Giardia: 15 mg/kg × 1 / day for 5 days. Avoid alcohol-containing solutions.",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-keflex",
            name = "Keflex (Cefalexin)",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 12.5,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 1000.0,
                preparations = listOf(
                    Preparation("125 mg / 5 mL", 25.0),
                    Preparation("250 mg / 5 mL", 50.0),
                ),
            ),
            notes = "1st-gen cephalosporin: skin infections, UTI. Standard 25 mg/kg/d ÷ 4 (severe 50–100 mg/kg/d ÷ 4).",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-cefixime",
            name = "Suprax (Cefixime)",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 8.0,
                frequencyLabel = "× 1 / day or ÷ 2",
                maxMgPerDose = 400.0,
                preparations = listOf(
                    Preparation("100 mg / 5 mL", 20.0),
                ),
            ),
            notes = "8 mg/kg once daily (or 4 mg/kg × 2). UTI, otitis. Not for pneumococcal pneumonia.",
            reference = "BNFc 2024",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-bactrim",
            name = "Bactrim (TMP-SMX) — TMP component",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 4.0,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 160.0,
                preparations = listOf(
                    // 240 mg/5 mL = 40 mg TMP + 200 mg SMX → 40 mg TMP/5 mL = 8 mg TMP/mL
                    Preparation("240 mg / 5 mL (40 TMP + 200 SMX)", 8.0),
                ),
            ),
            notes = "4 mg/kg of TMP component × 2. UTI, PCP prophylaxis (5 mg/kg × 2 / d, 3 days/week). Avoid <6 weeks (kernicterus).",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-coamoxiclav",
            name = "Co-amoxiclav (Augmentin)",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 22.5,
                frequencyLabel = "× 2 / day",
                maxMgPerDose = 875.0,
                preparations = listOf(
                    // 312 mg/5 mL → 250 amox + 62 clav → 250/5 = 50 mg amox/mL
                    Preparation("312 mg / 5 mL (4:1)", 50.0),
                    // 457 mg/5 mL → 400 amox + 57 clav → 400/5 = 80 mg amox/mL
                    Preparation("457 mg / 5 mL (7:1)", 80.0),
                ),
            ),
            notes = "Calculated by amoxicillin component. Standard 22.5 mg/kg × 2; high-dose (45 mg/kg × 2) only with 7:1 formulations to avoid clavulanate diarrhoea.",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Anti-emetic / steroid / bronchodilator
        // ------------------------------------------------------------------
        PediDrug(
            id = "syr-ondansetron",
            name = "Zofran / De-vomit (Ondansetron)",
            sectionHeader = "Anti-emetic, steroid, bronchodilator",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 0.15,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 8.0,
                preparations = listOf(
                    Preparation("4 mg / 5 mL", 0.8),
                ),
            ),
            notes = "0.15 mg/kg PO. Single dose effective in acute gastroenteritis vomiting. Max 8 mg per dose.",
            reference = "BNFc 2024 / UpToDate (pediatric AGE)",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-dexamethasone",
            name = "Dexon (Dexamethasone)",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 0.15,
                frequencyLabel = "× 4 / day",
                maxMgPerDose = 16.0,
                preparations = listOf(
                    Preparation("0.5 mg / 5 mL", 0.1),
                ),
            ),
            notes = "Croup: 0.15 mg/kg single dose (0.6 mg/kg if severe, max 16 mg). Asthma exacerbation: 0.6 mg/kg × 1–2 days.",
            reference = "BNFc 2024 / UpToDate (croup)",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-salbutamol",
            name = "Butadiene (Salbutamol)",
            mode = WeightBasedSyrup(
                mgPerKgPerDose = 0.1,
                frequencyLabel = "× 3 / day",
                maxMgPerDose = 4.0,
                preparations = listOf(
                    Preparation("2 mg / 5 mL", 0.4),
                ),
            ),
            notes = "0.1 mg/kg/dose × 3–4 / day; oral salbutamol is now rarely first-line — prefer inhaled. Watch for tremor, tachycardia, hypokalaemia.",
            reference = "BNFc 2024",
            accent = Accent.Cyan,
        ),

        // ------------------------------------------------------------------
        // Zinc (acute gastroenteritis adjunct, age-banded)
        // ------------------------------------------------------------------
        PediDrug(
            id = "syr-zinc-10",
            name = "Zinc syrup 10 mg / 5 mL",
            sectionHeader = "Zinc — acute diarrhoea / GI",
            mode = AgeBanded(
                preparations = listOf(Preparation("10 mg / 5 mL", 2.0)),
                bands = listOf(
                    AgeBand("Below 6 months", 5.0, "× 1 / day for 14 days"),
                    AgeBand("Over 6 months", 10.0, "× 1 / day for 14 days"),
                ),
            ),
            notes = "WHO recommendation: zinc 10 mg/d (<6 mo) or 20 mg/d (≥6 mo) for 10–14 days during acute diarrhoea reduces duration and severity.",
            reference = "WHO IMCI / BNFc 2024 / UpToDate",
            accent = Accent.Yellow,
        ),
        PediDrug(
            id = "syr-zinc-20",
            name = "Zinc syrup 20 mg / 5 mL",
            mode = AgeBanded(
                preparations = listOf(Preparation("20 mg / 5 mL", 4.0)),
                bands = listOf(
                    AgeBand("Below 6 months", 2.5, "× 1 / day for 14 days"),
                    AgeBand("Over 6 months", 5.0, "× 1 / day for 14 days"),
                ),
            ),
            notes = "Same total daily zinc as 10 mg/5 mL syrup, in half the volume.",
            reference = "WHO IMCI / BNFc 2024 / UpToDate",
            accent = Accent.Yellow,
        ),

        // ------------------------------------------------------------------
        // Cough / GI symptomatic syrups (age-banded)
        // ------------------------------------------------------------------
        PediDrug(
            id = "syr-prospan",
            name = "Prospan syrup (Ivy leaf extract)",
            sectionHeader = "Cough syrups",
            mode = AgeBanded(
                preparations = listOf(Preparation("Standard syrup", 1.0)),
                bands = listOf(
                    AgeBand("1–6 years", 2.5, "× 2 / day"),
                    AgeBand("6–12 years", 5.0, "× 2 / day"),
                    AgeBand("Over 12 years", 5.0, "× 3 / day"),
                ),
            ),
            notes = "Herbal mucolytic. Evidence is limited; symptomatic only.",
            reference = "Manufacturer SmPC / local formulary",
            accent = Accent.Yellow,
        ),
        PediDrug(
            id = "syr-solvodin",
            name = "Solvodin syrup",
            mode = AgeBanded(
                preparations = listOf(Preparation("Standard syrup", 1.0)),
                bands = listOf(
                    AgeBand("6–12 years", 5.0, "× 2 / day"),
                    AgeBand("Over 12 years", 10.0, "× 3 / day"),
                ),
            ),
            notes = "Mucolytic / expectorant. Evidence limited; avoid under 6 years.",
            reference = "Manufacturer SmPC",
            accent = Accent.Yellow,
        ),
        PediDrug(
            id = "syr-tussilet",
            name = "Tussilet syrup",
            mode = AgeBanded(
                preparations = listOf(Preparation("Standard syrup", 1.0)),
                bands = listOf(
                    AgeBand("6–12 months", 2.0, "× 2 / day"),
                    AgeBand("Over 12 months", 5.0, "× 3 / day"),
                ),
            ),
            notes = "Cough symptom-relief (combination). Avoid in dry cough with respiratory distress; treat the underlying disease first.",
            reference = "Manufacturer SmPC",
            accent = Accent.Yellow,
        ),
        PediDrug(
            id = "syr-colicez",
            name = "Colic EZ drops (Simethicone)",
            mode = AgeBanded(
                preparations = listOf(Preparation("Drops", 1.0)),
                bands = listOf(
                    AgeBand("Below 6 months", 0.5, "× 4 / day"),
                    AgeBand("6–12 months", 0.75, "× 4 / day"),
                    AgeBand("Over 12 months", 1.0, "× 4 / day"),
                ),
            ),
            notes = "Anti-foaming agent for infant colic. Safe; reassurance + feeding modifications equally effective.",
            reference = "Manufacturer SmPC",
            accent = Accent.Yellow,
        ),

        // ------------------------------------------------------------------
        // Antihistamines (age-banded for the older two, weight-based + age band for chlorpheniramine)
        // ------------------------------------------------------------------
        PediDrug(
            id = "syr-chlorpheniramine",
            name = "Chlorpheneramine 2 mg / 5 mL",
            sectionHeader = "Antihistamines",
            mode = AgeBanded(
                preparations = listOf(Preparation("2 mg / 5 mL", 0.4)),
                bands = listOf(
                    AgeBand("1–2 years", 1.0, "× 2 / day"),
                    AgeBand("2–6 years", 2.5, "× 4 / day"),
                    AgeBand("6–12 years", 5.0, "× 4–6 / day"),
                ),
            ),
            notes = "Sedating H1 antihistamine. Avoid in infants under 1 year. Max 6 mg/d (1–2 y), 12 mg/d (2–6 y), 24 mg/d (6–12 y).",
            reference = "BNFc 2024",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-loratadine",
            name = "Loratidine 5 mg / 5 mL",
            mode = AgeBanded(
                preparations = listOf(Preparation("5 mg / 5 mL", 1.0)),
                bands = listOf(
                    AgeBand("2–11 years (<30 kg)", 5.0, "× 1 / day"),
                    AgeBand("≥ 12 years or > 30 kg", 10.0, "× 1 / day"),
                ),
            ),
            notes = "Non-sedating H1 antihistamine. Allergic rhinitis, urticaria.",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),
        PediDrug(
            id = "syr-desloratadine",
            name = "Desloratidne 2.5 mg / 5 mL",
            mode = AgeBanded(
                preparations = listOf(Preparation("2.5 mg / 5 mL", 0.5)),
                bands = listOf(
                    AgeBand("6–12 months", 2.0, "× 1 / day"),
                    AgeBand("1–5 years", 2.5, "× 1 / day"),
                    AgeBand("6–11 years", 5.0, "× 1 / day"),
                    AgeBand("Over 12 years", 10.0, "× 1 / day"),
                ),
            ),
            notes = "Active metabolite of loratadine. Used from 6 months. Allergic rhinitis, chronic urticaria.",
            reference = "BNFc 2024 / UpToDate",
            accent = Accent.Cyan,
        ),
    )
}
