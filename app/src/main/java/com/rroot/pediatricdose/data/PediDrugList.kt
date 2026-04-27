package com.rroot.pediatricdose.data

import com.rroot.pediatricdose.data.DoseMode.*

/**
 * Bedside paediatric injection card.
 *
 * Every drug that can be given as an injection is documented here with a
 * [PediDrug.injection] block. The block carries explicit bedside fields so
 * the user can read off the full recipe in one place:
 *
 *   Vial       → what the manufacturer hands you
 *   Reconstitute → how to dissolve the powder
 *   Further dilute → optional secondary dilution before infusion
 *   Dose       → mg/kg per single dose (or per kg/h for infusions)
 *   Frequency  → how many times per day
 *   Route      → IV / IM / SC / IO / PR
 *   Time       → push speed or infusion duration
 *   Max/dose   → ceiling per single dose
 *   Max/day    → ceiling per 24 h
 *   Cautions   → bedside warnings & monitoring
 *
 * Doses are aligned with **BNFc 2024** (https://bnfc.nice.org.uk/) cross-
 * checked against **UpToDate** (paediatric drug monographs). Vial concentrations
 * follow the most common preparations sold in Iraq / Middle East. Always verify
 * the prescription with the responsible physician — this is decision support,
 * not a substitute for the BNFc.
 */
object PediDrugList {

    val all: List<PediDrug> = listOf(
        // ------------------------------------------------------------------
        // Fluids
        // ------------------------------------------------------------------
        PediDrug(
            id = "ns-bolus",
            name = "Normal Saline 0.9 % — resuscitation bolus",
            sectionHeader = "IV Fluids",
            mode = BolusMlPerKg(mlPerKg = 20.0),
            notes = "Resuscitation: 10–20 mL/kg of 0.9 % NaCl over 5–10 min, reassess after each bolus. " +
                "Septic shock / DKA: start at 10 mL/kg over 30 min. Repeat up to 40–60 mL/kg if shock persists, " +
                "then escalate to PICU + inotrope.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (paediatric resuscitation) / NICE NG29 / APLS 6e",
            injection = InjectionDetail(
                vial = "0.9 % NaCl 500 mL bag",
                dose = "10–20 mL/kg",
                frequency = "as required (reassess each bolus)",
                route = "IV",
                infusionTime = "over 5–10 min (resus) or 30 min (DKA / sepsis)",
                maxPerDose = "≤ 40–60 mL/kg total before inotrope",
                cautions = "Use 10 mL/kg in DKA / septic shock without overt circulatory collapse to avoid cerebral oedema.",
            ),
        ),
        PediDrug(
            id = "maintenance",
            name = "Maintenance fluid (Holliday-Segar)",
            mode = MaintenanceHollidaySegar,
            notes = "100 mL/kg/day for first 10 kg, 50 mL/kg/day for next 10 kg, 20 mL/kg/day for every kg above 20 kg. " +
                "Use isotonic fluid (0.9 % NaCl + 5 % glucose ± KCl) per NICE NG29. Cap at 100 mL/h (2 400 mL/24 h).",
            accent = Accent.Cyan,
            reference = "NICE NG29 / BNFc 2024",
            injection = InjectionDetail(
                vial = "0.9 % NaCl + 5 % glucose (± 10–20 mmol KCl/L)",
                dose = "100 / 50 / 20 mL/kg/day",
                frequency = "continuous over 24 h",
                route = "IV",
                infusionTime = "continuous",
                maxPerDay = "≤ 100 mL/h (≈ 2 400 mL/day)",
                cautions = "Hyponatraemia risk with hypotonic fluid — use isotonic per NICE. Reassess Na+ q24h.",
            ),
        ),

        // ------------------------------------------------------------------
        // Analgesics / antipyretics
        // ------------------------------------------------------------------
        PediDrug(
            id = "paracetamol-iv",
            name = "Paracetamol IV (Perfalgan) 10 mg/mL",
            sectionHeader = "Analgesics & antipyretics",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 10.0,
                frequencyLabel = "every 6 h",
                maxMgPerDose = 1000.0,
            ),
            notes = "< 10 kg: 7.5 mg/kg q4–6 h, max 30 mg/kg/day. ≥ 10 kg: 15 mg/kg q4–6 h, " +
                "max 60 mg/kg/day or 4 g/day, whichever is less. Minimum interval 4 h.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (paracetamol IV)",
            injection = InjectionDetail(
                vial = "Bag/vial 1 g / 100 mL = 10 mg/mL (already diluted)",
                dose = "≥ 10 kg: 15 mg/kg     <10 kg: 7.5 mg/kg",
                frequency = "every 4–6 h (max × 4/day)",
                route = "IV",
                infusionTime = "infusion over 15 min",
                maxPerDose = "1 g (≥ 10 kg)",
                maxPerDay = "60 mg/kg/day or 4 g/day (whichever lower)",
                cautions = "Reduce dose if hepatic impairment, malnutrition, dehydration, weight < 50 kg.",
            ),
        ),
        PediDrug(
            id = "paracetamol-pr",
            name = "Paracetamol suppository 125 / 250 / 500 mg",
            mode = MassMgPerKg(
                mgPerKg = 20.0,
                frequencyLabel = "every 6 h",
                maxMgPerDose = 1000.0,
            ),
            notes = "PR loading 20–30 mg/kg once, then 15–20 mg/kg q6h. Same daily cap (60 mg/kg/day or 4 g/day).",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (paracetamol rectal)",
            injection = InjectionDetail(
                vial = "Suppositories 125 mg / 250 mg / 500 mg",
                dose = "Loading 20–30 mg/kg, then 15–20 mg/kg",
                frequency = "every 6 h",
                route = "PR",
                infusionTime = "single insertion",
                maxPerDose = "1 g",
                maxPerDay = "60 mg/kg or 4 g (whichever lower)",
                cautions = "Use whole suppository nearest the calculated dose; do NOT cut.",
            ),
        ),

        // ------------------------------------------------------------------
        // Anti-emetic
        // ------------------------------------------------------------------
        PediDrug(
            id = "ondansetron",
            name = "Ondansetron (Zofran) ampoule 4 mg / 2 mL",
            sectionHeader = "Anti-emetic / steroid / antihistamine",
            mode = VolumeMgPerKg(
                mgPerKg = 0.15,
                mgPerCc = 2.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 8.0,
            ),
            notes = "Acute gastroenteritis (1m–12y): single dose 0.15 mg/kg slow IV (max 4 mg if ≤ 15 kg, " +
                "8 mg if > 15 kg). Chemo-induced nausea: 0.15 mg/kg q8h, max 16 mg/dose.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (ondansetron)",
            injection = InjectionDetail(
                vial = "Amp 4 mg / 2 mL = 2 mg/mL",
                furtherDilute = "Optional: dilute in 50 mL N.S for infusion",
                dose = "0.15 mg/kg/dose",
                frequency = "every 8 h",
                route = "IV (slow push or infusion)",
                infusionTime = "slow IV push over ≥ 30 sec, or infusion over 15 min",
                maxPerDose = "≤ 15 kg: 4 mg     > 15 kg: 8 mg     (chemo: 16 mg)",
                maxPerDay = "32 mg",
                cautions = "QT prolongation — avoid if congenital long QT, hypokalaemia, hypomagnesaemia. " +
                    "Single dose may be enough for acute gastroenteritis (NICE).",
            ),
        ),

        // ------------------------------------------------------------------
        // Steroids / antihistamine
        // ------------------------------------------------------------------
        PediDrug(
            id = "hydrocortisone",
            name = "Hydrocortisone (Solu-Cortef) 100 mg vial",
            mode = VolumeMgPerKg(
                mgPerKg = 4.0,
                mgPerCc = 50.0,
                frequencyLabel = "every 6 h",
                maxMgPerDose = 100.0,
            ),
            notes = "Status asthmaticus: 4 mg/kg q6h. Anaphylaxis (2nd-line, after adrenaline): " +
                "1m–5y: 50 mg; 6–11y: 100 mg; ≥ 12y: 200 mg. Adrenal crisis: 1m–1y 25 mg, " +
                "1–5y 50 mg, 6–11y 100 mg, ≥ 12y 100 mg q6h.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (hydrocortisone)",
            injection = InjectionDetail(
                vial = "Vial 100 mg powder",
                reconstitute = "+ 2 mL water for injection → 50 mg/mL",
                furtherDilute = "Optional: further dilute in 5–10 mL N.S",
                dose = "4 mg/kg/dose (asthma)",
                frequency = "every 6 h",
                route = "IV (or IM if no access)",
                infusionTime = "slow IV push over 1–10 min",
                maxPerDose = "100 mg",
                maxPerDay = "400 mg",
                cautions = "Faster push → hypotension, arrhythmia. Diabetics: monitor glucose.",
            ),
        ),
        PediDrug(
            id = "chlorpheniramine",
            name = "Chlorphenamine (Allermine, Piriton) 10 mg / 1 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 0.2,
                mgPerCc = 10.0,
                frequencyLabel = "every 4–6 h",
                maxMgPerDose = 10.0,
            ),
            notes = "Anaphylaxis (2nd-line, after adrenaline): age-banded — 1m–5y: 250 µg/kg (max 2.5 mg); " +
                "6–11y: 5 mg; 12–17y: 10 mg. May be repeated up to × 4/24 h. " +
                "Weight-based 0.2 mg/kg approximates the same; **always cap to age band**.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (chlorphenamine, anaphylaxis)",
            injection = InjectionDetail(
                vial = "Amp 10 mg / 1 mL",
                furtherDilute = "Dilute amp in 5–10 mL N.S to slow the push",
                dose = "0.2 mg/kg (age cap: 2.5 / 5 / 10 mg)",
                frequency = "every 4–6 h (max × 4/24 h)",
                route = "Slow IV (or IM)",
                infusionTime = "slow IV push over ≥ 1 min",
                maxPerDose = "1m–5y: 2.5 mg     6–11y: 5 mg     ≥ 12y: 10 mg",
                maxPerDay = "× 4 doses",
                cautions = "Sedation. Avoid neonates (paradoxical excitation, blood-brain barrier).",
            ),
        ),
        PediDrug(
            id = "dexamethasone",
            name = "Dexamethasone (Decadron) 8 mg / 2 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 0.6,
                mgPerCc = 4.0,
                frequencyLabel = "single dose",
                maxMgPerDose = 16.0,
            ),
            notes = "Croup / asthma exacerbation: 0.6 mg/kg single dose PO/IM/IV (max 16 mg). " +
                "Bacterial meningitis: **0.15 mg/kg q6h × 4 days** (max 10 mg/dose), first dose with/before antibiotic. " +
                "Cerebral oedema: 0.5 mg/kg q6h.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 / UpToDate (dexamethasone)",
            injection = InjectionDetail(
                vial = "Amp 8 mg / 2 mL = 4 mg/mL",
                dose = "Croup / asthma: 0.6 mg/kg     Meningitis: 0.15 mg/kg",
                frequency = "Croup / asthma: × 1     Meningitis: q6h × 4 days",
                route = "IV / IM / PO",
                infusionTime = "slow IV push over 1–4 min",
                maxPerDose = "Asthma / croup: 16 mg     Meningitis: 10 mg",
                cautions = "Give meningitis dose with first antibiotic, ideally just before. Single croup dose is sufficient.",
            ),
        ),
        PediDrug(
            id = "furosemide",
            name = "Furosemide (Lasix) 20 mg / 2 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 1.0,
                mgPerCc = 10.0,
                frequencyLabel = "every 6–12 h",
                maxMgPerDose = 40.0,
            ),
            notes = "0.5–1 mg/kg slow IV (rate ≤ 4 mg/min). Repeat q6–12 h as needed. " +
                "Pulmonary oedema: up to 2 mg/kg per dose. Maximum cumulative 4–6 mg/kg/day.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (furosemide)",
            injection = InjectionDetail(
                vial = "Amp 20 mg / 2 mL = 10 mg/mL",
                dose = "0.5–1 mg/kg",
                frequency = "every 6–12 h",
                route = "IV (or IM)",
                infusionTime = "slow IV push, rate ≤ 4 mg/min",
                maxPerDose = "40 mg",
                maxPerDay = "4–6 mg/kg",
                cautions = "Ototoxicity if pushed faster than 4 mg/min. Monitor K+, renal function.",
            ),
        ),

        // ------------------------------------------------------------------
        // Resuscitation
        // ------------------------------------------------------------------
        PediDrug(
            id = "adrenaline",
            name = "Adrenaline 1 : 1 000 (1 mg / 1 mL) — anaphylaxis IM",
            sectionHeader = "Resuscitation",
            mode = VolumeAndMass(
                mgPerKg = 0.01,
                mgPerCc = 1.0,
                frequencyLabel = "repeat × q5min",
                maxMg = 0.5,
            ),
            notes = "Anaphylaxis IM into anterolateral thigh — BNFc age-banded doses: " +
                "< 6m: 100–150 µg; 6m–5y: 150 µg; 6–11y: 300 µg; ≥ 12y: 500 µg. " +
                "Repeat every 5 min if no response. **For arrest use 1 : 10 000 IV at 10 µg/kg.**",
            accent = Accent.Gray,
            reference = "BNFc 2024 / Resuscitation Council UK",
            injection = InjectionDetail(
                vial = "Amp 1 : 1 000 = 1 mg / 1 mL",
                dose = "Weight-based: 0.01 mg/kg (= 0.01 mL/kg)\n" +
                    "Age-banded: < 6m 0.1 mL · 6m–5y 0.15 mL · 6–11y 0.3 mL · ≥ 12y 0.5 mL",
                frequency = "every 5 min if needed",
                route = "IM (anterolateral mid-thigh)",
                infusionTime = "single IM injection",
                maxPerDose = "0.5 mg (= 0.5 mL)",
                cautions = "Use 1 : 10 000 (100 µg/mL) for arrest IV/IO at 10 µg/kg (= 0.1 mL/kg). " +
                    "Never give 1 : 1 000 IV — can cause cardiac arrest.",
            ),
        ),
        PediDrug(
            id = "atropine",
            name = "Atropine 0.6 mg / 1 mL — bradycardia",
            mode = VolumeAndMass(
                mgPerKg = 0.02,
                mgPerCc = 0.6,
                frequencyLabel = "may repeat × 1",
                minMg = 0.1,
                maxMg = 0.5,
            ),
            notes = "Bradycardia / pre-intubation: 20 µg/kg IV (may repeat once). 2020 PALS removed the 0.1 mg minimum, " +
                "but BNFc still recommends 0.1 mg minimum to avoid paradoxical bradycardia. " +
                "Max single dose: child 0.5 mg; adolescent 1 mg.",
            accent = Accent.Gray,
            reference = "BNFc 2024 / PALS 2020",
            injection = InjectionDetail(
                vial = "Amp 0.6 mg / 1 mL = 0.6 mg/mL",
                dose = "0.02 mg/kg (= 20 µg/kg)",
                frequency = "may repeat × 1 after 5 min",
                route = "IV / IO (or IM, ETT 0.04–0.06 mg/kg)",
                infusionTime = "rapid IV push",
                maxPerDose = "Child 0.5 mg     Adolescent 1 mg",
                cautions = "Min 0.1 mg per BNFc to avoid paradoxical bradycardia. Tachycardia, anticholinergic toxidrome.",
            ),
        ),

        // ------------------------------------------------------------------
        // Asthma
        // ------------------------------------------------------------------
        PediDrug(
            id = "aminophylline",
            name = "Aminophylline 250 mg / 10 mL — loading dose",
            sectionHeader = "Asthma / bronchospasm",
            mode = VolumeMgPerKg(
                mgPerKg = 5.0,
                mgPerCc = 25.0,
                frequencyLabel = "loading dose",
                maxMgPerDose = 500.0,
            ),
            notes = "Severe acute asthma not responsive to nebulised SABA + IV magnesium. " +
                "Loading 5 mg/kg over 20 min (omit if patient already on theophylline). " +
                "Maintenance 1 mg/kg/h (1 m – 9 y), 0.8 mg/kg/h (9–12 y), 0.5 mg/kg/h (≥ 12 y).",
            accent = Accent.Cyan,
            reference = "BNFc 2024 / NICE NG80 (asthma)",
            injection = InjectionDetail(
                vial = "Amp 250 mg / 10 mL = 25 mg/mL",
                furtherDilute = "Loading: dose into 30–50 mL N.S",
                dose = "5 mg/kg loading, then 1 mg/kg/h maintenance",
                frequency = "load × 1, then continuous infusion",
                route = "IV",
                infusionTime = "loading over 20 min, then continuous",
                maxPerDose = "500 mg loading",
                maxPerDay = "Adjust to plasma theophylline 10–20 mg/L",
                cautions = "OMIT loading if on chronic theophylline. Cardiac monitoring. " +
                    "Toxicity: vomiting, tachyarrhythmia, seizure.",
            ),
        ),

        // ------------------------------------------------------------------
        // Sedation / anticonvulsants
        // ------------------------------------------------------------------
        PediDrug(
            id = "diazepam-iv",
            name = "Diazepam (Valium) 10 mg / 2 mL — IV bolus",
            sectionHeader = "Sedation & anticonvulsants",
            mode = VolumeMgPerKg(
                mgPerKg = 0.3,
                mgPerCc = 5.0,
                frequencyLabel = "may repeat after 10 min",
                maxMgPerDose = 10.0,
            ),
            notes = "Status epilepticus IV: 0.3–0.4 mg/kg (max 10 mg). May repeat once after 10 min. " +
                "Lorazepam 0.1 mg/kg is now preferred per APLS if available.",
            accent = Accent.Yellow,
            reference = "BNFc 2024 (diazepam) / APLS 6e",
            injection = InjectionDetail(
                vial = "Amp 10 mg / 2 mL = 5 mg/mL",
                furtherDilute = "Dilute in 8 mL N.S → 1 mg/mL for slow push",
                dose = "0.3–0.4 mg/kg",
                frequency = "may repeat × 1 after 10 min",
                route = "IV (large vein)",
                infusionTime = "slow push, rate ≤ 5 mg/min (≤ 0.25 mg/kg/min children)",
                maxPerDose = "10 mg",
                cautions = "Respiratory depression — bag-valve mask + flumazenil ready. Thrombophlebitis if peripheral.",
            ),
        ),
        PediDrug(
            id = "diazepam-pr",
            name = "Diazepam — rectal solution",
            mode = MassMgPerKg(
                mgPerKg = 0.5,
                frequencyLabel = "may repeat after 10 min",
                maxMgPerDose = 10.0,
            ),
            notes = "PR if no IV access. BNFc age bands: 1m–1y 5 mg; 2–11y 5–10 mg; 12–17y 10–20 mg. " +
                "Weight-based 0.5 mg/kg max 10 mg (cap as per age).",
            accent = Accent.Yellow,
            reference = "BNFc 2024 (diazepam rectal)",
            injection = InjectionDetail(
                vial = "Rectal tube 2.5 / 5 / 10 mg",
                dose = "0.5 mg/kg",
                frequency = "may repeat × 1 after 10 min",
                route = "PR",
                infusionTime = "single rectal insertion",
                maxPerDose = "1m–1y: 5 mg     2–11y: 10 mg     ≥ 12y: 20 mg",
                cautions = "Respiratory monitoring after dose. Onset ~ 5 min.",
            ),
        ),
        PediDrug(
            id = "luminal-bolus",
            name = "Phenobarbital (Luminal) 200 mg / 1 mL — loading",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 200.0,
                frequencyLabel = "loading dose",
                maxMgPerDose = 1000.0,
            ),
            notes = "Status epilepticus / neonatal seizures: 20 mg/kg loading IV at ≤ 1 mg/kg/min " +
                "(over ≥ 20 min). May give a further 10 mg/kg if seizure persists. Max 40 mg/kg cumulative.",
            accent = Accent.Red,
            reference = "BNFc 2024 (phenobarbital)",
            injection = InjectionDetail(
                vial = "Amp 200 mg / 1 mL",
                reconstitute = "Dilute 1 : 10 with water for injection → 20 mg/mL before IV",
                furtherDilute = "Or dilute loading dose in 10–20 mL N.S",
                dose = "20 mg/kg loading",
                frequency = "× 1 (then maintenance)",
                route = "IV (slow)",
                infusionTime = "rate ≤ 1 mg/kg/min, over ≥ 20 min",
                maxPerDose = "1 g",
                cautions = "Apnoea / hypotension — bag-valve mask ready. Levels 15–40 mg/L. " +
                    "**Mandatory dilution** before IV — irritant if undiluted.",
            ),
        ),
        PediDrug(
            id = "luminal-maint",
            name = "Phenobarbital — maintenance",
            mode = MassMgPerKg(
                mgPerKg = 5.0,
                frequencyLabel = "÷ 2 / day",
                maxMgPerDose = 250.0,
            ),
            notes = "Maintenance 2.5–5 mg/kg/day in 1–2 divided doses (PO or IV). " +
                "Adjust to therapeutic level 15–40 mg/L (start 24 h after loading).",
            accent = Accent.Red,
            reference = "BNFc 2024 (phenobarbital maintenance)",
            injection = InjectionDetail(
                vial = "Amp 200 mg / 1 mL (or 30 / 60 mg tablet PO)",
                reconstitute = "Dilute 1 : 10 in water for injection if IV",
                dose = "2.5–5 mg/kg/day",
                frequency = "once daily or ÷ 2",
                route = "IV / IM / PO",
                infusionTime = "slow IV push (≤ 1 mg/kg/min)",
                maxPerDose = "250 mg",
                cautions = "Sedation. Hepatic enzyme inducer — drug interactions.",
            ),
        ),
        PediDrug(
            id = "phenytoin-bolus",
            name = "Phenytoin 250 mg / 5 mL — loading",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 50.0,
                frequencyLabel = "loading dose",
                maxMgPerDose = 1500.0,
            ),
            notes = "Status epilepticus: 20 mg/kg loading IV in **0.9 % NaCl only** (NEVER dextrose — precipitates). " +
                "Rate ≤ 1 mg/kg/min (max 50 mg/min adults). Continuous ECG + BP monitoring required.",
            accent = Accent.Red,
            reference = "BNFc 2024 (phenytoin)",
            injection = InjectionDetail(
                vial = "Amp 250 mg / 5 mL = 50 mg/mL",
                furtherDilute = "Dilute in 50–100 mL 0.9 % NaCl → ≤ 10 mg/mL (NEVER dextrose)",
                dose = "20 mg/kg loading",
                frequency = "× 1",
                route = "IV (large peripheral or central line)",
                infusionTime = "rate ≤ 1 mg/kg/min, over ≥ 20 min",
                maxPerDose = "1.5 g",
                cautions = "Continuous ECG + BP. Bradycardia, hypotension, asystole if pushed fast. " +
                    "Extravasation → tissue necrosis (purple-glove syndrome). Saline flush before & after.",
            ),
        ),
        PediDrug(
            id = "phenytoin-maint",
            name = "Phenytoin — maintenance",
            mode = VolumeMgPerKg(
                mgPerKg = 5.0,
                mgPerCc = 50.0,
                frequencyLabel = "÷ 2 / day",
                maxMgPerDose = 300.0,
            ),
            notes = "Maintenance 5 mg/kg/day in 2 divided doses (start 12 h after loading). " +
                "Therapeutic level 10–20 mg/L (free phenytoin 1–2 mg/L).",
            accent = Accent.Red,
            reference = "BNFc 2024 (phenytoin maintenance)",
            injection = InjectionDetail(
                vial = "Amp 250 mg / 5 mL = 50 mg/mL (or 100 mg PO)",
                furtherDilute = "Dilute IV doses in 0.9 % NaCl to ≤ 10 mg/mL",
                dose = "5 mg/kg/day",
                frequency = "÷ 2 (every 12 h)",
                route = "IV / PO",
                infusionTime = "rate ≤ 1 mg/kg/min if IV",
                maxPerDose = "300 mg",
                cautions = "Non-linear kinetics — small dose changes → large level swings. Monitor levels.",
            ),
        ),
        PediDrug(
            id = "midazolam-infusion",
            name = "Midazolam 5 mg / mL — refractory status / sedation infusion",
            mode = Infusion(
                mgPerKgPerHour = 0.1,
                drugMgPerCc = 5.0,
                diluentSourceCc = 1.0,
                finalCc = 50.0,
                recipeText = "Add 1 mL midazolam (5 mg) to 49 mL N.S → 50 mL total at 0.1 mg/mL. " +
                    "Run by syringe pump at (weight in kg) mL/h to deliver 0.1 mg/kg/h.",
            ),
            notes = "Refractory status epilepticus or PICU sedation: bolus 0.05–0.1 mg/kg, then infusion " +
                "0.05–0.4 mg/kg/h titrated to effect. Most common starting rate 0.1 mg/kg/h.",
            accent = Accent.Yellow,
            reference = "BNFc 2024 (midazolam) / APLS",
            injection = InjectionDetail(
                vial = "Amp 15 mg / 3 mL or 10 mg / 2 mL = 5 mg/mL",
                reconstitute = "1 mL midazolam (5 mg) + 49 mL N.S → 50 mL at 0.1 mg/mL",
                dose = "Bolus 0.05–0.1 mg/kg, then 0.1 mg/kg/h",
                frequency = "continuous (titrate q5–15 min)",
                route = "IV (syringe pump)",
                infusionTime = "continuous",
                maxPerDay = "0.4 mg/kg/h ceiling",
                cautions = "Apnoea, hypotension. **Bag-valve mask + intubation kit at bedside.** " +
                    "Tachyphylaxis after 48 h — wean slowly to avoid withdrawal.",
            ),
        ),

        // ------------------------------------------------------------------
        // Antibiotics
        // ------------------------------------------------------------------
        PediDrug(
            id = "ampicillin",
            name = "Ampicillin 500 mg vial",
            sectionHeader = "Antibiotics",
            mode = VolumeMgPerKg(
                mgPerKg = 50.0,
                mgPerCc = 100.0,
                frequencyLabel = "every 6 h",
                maxMgPerDose = 2000.0,
            ),
            notes = "Standard 25 mg/kg q6h (max 2 g). Severe infection: 50 mg/kg q6h. " +
                "Meningitis / Listeria cover (< 3 m): 100 mg/kg q4–6h (max 2 g). " +
                "Neonate ≤ 7 d: 30 mg/kg q12h (severe q8h). 7–28 d: 30 mg/kg q8h.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (ampicillin)",
            injection = InjectionDetail(
                vial = "Vial 500 mg powder",
                reconstitute = "+ 5 mL water for injection → 100 mg/mL",
                furtherDilute = "Optional: further dilute in 50 mL N.S for infusion",
                dose = "Standard: 25–50 mg/kg     Meningitis: 100 mg/kg",
                frequency = "every 6 h (× 4/day)     Meningitis: every 4 h",
                route = "IV (or IM ≤ 250 mg/mL)",
                infusionTime = "slow IV push over 3–5 min, or infusion over 30 min",
                maxPerDose = "2 g",
                maxPerDay = "12 g",
                cautions = "Anaphylaxis (penicillin allergy). Rash with EBV. Avoid mixing with aminoglycoside in same line.",
            ),
        ),
        PediDrug(
            id = "amoxicillin",
            name = "Amoxicillin 500 mg vial (IV)",
            mode = VolumeMgPerKg(
                mgPerKg = 30.0,
                mgPerCc = 100.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 1000.0,
            ),
            notes = "IV: 20–30 mg/kg q8h (max 500 mg q8h ≥ 12 y). PO high-dose for otitis / pneumonia: " +
                "45 mg/kg q12h. Use ampicillin if local stock favours it.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (amoxicillin)",
            injection = InjectionDetail(
                vial = "Vial 500 mg powder",
                reconstitute = "+ 5 mL water for injection → 100 mg/mL",
                dose = "20–30 mg/kg",
                frequency = "every 8 h (× 3/day)",
                route = "IV / IM",
                infusionTime = "slow IV push over 3–4 min, or infusion over 30 min",
                maxPerDose = "1 g (≥ 12 y: 500 mg)",
                maxPerDay = "3 g",
                cautions = "Penicillin allergy. Mononucleosis — almost universal rash.",
            ),
        ),
        PediDrug(
            id = "ceftriaxone",
            name = "Ceftriaxone 1 g vial",
            mode = VolumeMgPerKg(
                mgPerKg = 80.0,
                mgPerCc = 100.0,
                frequencyLabel = "once daily",
                maxMgPerDose = 4000.0,
            ),
            notes = "Severe bacterial infection: 50–80 mg/kg once daily, max 4 g. " +
                "Bacterial meningitis: 80–100 mg/kg/day (once daily or ÷ 2 q12h), max 4 g/day. " +
                "**Avoid in neonates (≤ 41 weeks corrected) — bilirubin displacement, calcium precipitation.**",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (ceftriaxone)",
            injection = InjectionDetail(
                vial = "Vial 1 g powder",
                reconstitute = "IV: + 9.6 mL water → 100 mg/mL\nIM: + 3.5 mL 1 % lidocaine → 250 mg/mL",
                furtherDilute = "Further dilute in 50–100 mL N.S for IV infusion",
                dose = "Severe: 50–80 mg/kg     Meningitis: 80–100 mg/kg",
                frequency = "once daily (or ÷ 2 q12h)",
                route = "IV (preferred) or deep IM",
                infusionTime = "infusion over 30 min (60 min in neonates)",
                maxPerDose = "4 g",
                maxPerDay = "4 g",
                cautions = "**Never co-infuse or in same line as Ca²⁺-containing solutions** in neonates → fatal precipitation. " +
                    "Biliary sludge with prolonged courses.",
            ),
        ),
        PediDrug(
            id = "cefotaxime",
            name = "Cefotaxime (Claforan) 1 g vial",
            mode = VolumeMgPerKg(
                mgPerKg = 50.0,
                mgPerCc = 100.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 2000.0,
            ),
            notes = "1m–17y: 50 mg/kg q8h. Severe infection / meningitis: 50 mg/kg q6h (max 12 g/day). " +
                "Neonate ≤ 7d: 25 mg/kg q12h; 7–21d: 25 mg/kg q8h; 21–28d: 25 mg/kg q6–8h.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (cefotaxime)",
            injection = InjectionDetail(
                vial = "Vial 1 g powder",
                reconstitute = "+ 10 mL water for injection → 100 mg/mL",
                furtherDilute = "Further dilute in 50 mL N.S for infusion",
                dose = "Standard: 50 mg/kg     Severe / meningitis: 50 mg/kg q6h",
                frequency = "every 8 h (× 3/day) — every 6 h if severe",
                route = "IV / IM",
                infusionTime = "slow IV push over 3–5 min, or infusion over 20–60 min",
                maxPerDose = "2 g (3 g per dose if meningitis)",
                maxPerDay = "12 g",
                cautions = "Cross-reactivity with penicillin allergy (~ 1–2 %). Reduces vit K → bleeding risk.",
            ),
        ),
        PediDrug(
            id = "gentamicin-large",
            name = "Gentamicin (Garamycin) 80 mg / 2 mL — once-daily",
            mode = VolumeMgPerKg(
                mgPerKg = 7.0,
                mgPerCc = 10.0,
                frequencyLabel = "once daily",
                maxMgPerDose = 360.0,
            ),
            notes = "Once-daily extended interval: 7 mg/kg q24h (1 m – 17 y). Adjust by trough level " +
                "(< 1 mg/L). Neonates: 4–5 mg/kg q24–48 h depending on gestational age. " +
                "Multiple-daily-dosing alternative: 2.5 mg/kg q8h.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (gentamicin)",
            injection = InjectionDetail(
                vial = "Amp 80 mg / 2 mL = 40 mg/mL",
                furtherDilute = "Dilute the dose in 50–100 mL N.S → ≤ 10 mg/mL",
                dose = "Once-daily: 7 mg/kg     Neonate: 4–5 mg/kg",
                frequency = "once daily (q24h)",
                route = "IV (or IM)",
                infusionTime = "infusion over 30 min (or 60 min)",
                maxPerDose = "360 mg / dose adjusted by levels",
                maxPerDay = "Single dose / day",
                cautions = "Nephro- + ototoxic. Trough level (< 1 mg/L) at 18–22 h before next dose. " +
                    "Synergy with cell-wall agents in endocarditis.",
            ),
        ),
        PediDrug(
            id = "gentamicin-small",
            name = "Gentamicin amp 20 mg / 2 mL — paediatric",
            mode = VolumeMgPerKg(
                mgPerKg = 7.0,
                mgPerCc = 10.0,
                frequencyLabel = "once daily",
                maxMgPerDose = 360.0,
            ),
            notes = "Same dose as the 80 mg amp; 20 mg amp avoids dilution waste in infants.",
            accent = Accent.Cyan,
            reference = "BNFc 2024",
            injection = InjectionDetail(
                vial = "Amp 20 mg / 2 mL = 10 mg/mL",
                furtherDilute = "Dilute in 25–50 mL N.S → ≤ 10 mg/mL",
                dose = "7 mg/kg (neonate 4–5 mg/kg)",
                frequency = "once daily",
                route = "IV (or IM)",
                infusionTime = "infusion over 30 min",
                maxPerDose = "360 mg adjusted by levels",
                cautions = "Same as 80 mg amp.",
            ),
        ),
        PediDrug(
            id = "metronidazole",
            name = "Metronidazole (Flagyl) 500 mg / 100 mL premix",
            mode = VolumeMgPerKg(
                mgPerKg = 7.5,
                mgPerCc = 5.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 500.0,
            ),
            notes = "1 m – 11 y: 7.5 mg/kg q8h (max 500 mg). 12–17 y: 500 mg q8h. " +
                "Neonate term: 7.5 mg/kg q12h. Anaerobic cover (intra-abdominal sepsis, brain abscess, C. diff IV).",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (metronidazole)",
            injection = InjectionDetail(
                vial = "Premix bag 500 mg / 100 mL = 5 mg/mL (no reconstitution)",
                dose = "7.5 mg/kg",
                frequency = "every 8 h (× 3/day)",
                route = "IV",
                infusionTime = "infusion over 20–30 min (≤ 5 mL/min)",
                maxPerDose = "500 mg",
                maxPerDay = "1.5 g",
                cautions = "Disulfiram reaction — avoid alcohol for 48 h. Peripheral neuropathy with prolonged use.",
            ),
        ),
        PediDrug(
            id = "ceftazidime",
            name = "Ceftazidime 1 g vial — anti-pseudomonal",
            mode = VolumeMgPerKg(
                mgPerKg = 50.0,
                mgPerCc = 100.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 2000.0,
            ),
            notes = "Severe Pseudomonas infection (CF exacerbation, febrile neutropenia, meningitis): " +
                "50 mg/kg q8h, max 6 g/day. Standard infection: 25 mg/kg q8h. " +
                "Neonate (≤ 7d): 25 mg/kg q24h; (7d–post-32 weeks): q12h then q8h.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (ceftazidime)",
            injection = InjectionDetail(
                vial = "Vial 1 g powder",
                reconstitute = "+ 10 mL water for injection → 100 mg/mL (CO₂ released — vent the vial)",
                furtherDilute = "Further dilute in 50 mL N.S for infusion",
                dose = "Severe: 50 mg/kg     Standard: 25 mg/kg",
                frequency = "every 8 h (× 3/day)",
                route = "IV / IM",
                infusionTime = "slow IV push over 3–5 min, or infusion over 15–30 min",
                maxPerDose = "2 g (3 g if meningitis)",
                maxPerDay = "6 g",
                cautions = "Reduce dose if eGFR < 50 mL/min. CSF penetration adequate for meningitis.",
            ),
        ),
        PediDrug(
            id = "acyclovir",
            name = "Aciclovir (Zovirax) 250 mg vial",
            mode = VolumeMgPerKg(
                mgPerKg = 10.0,
                mgPerCc = 25.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 500.0,
            ),
            notes = "Varicella / HSV: 10 mg/kg q8h (3 m – 11 y); 5 mg/kg q8h (≥ 12 y). " +
                "**HSV encephalitis or VZV in immunocompromised: 20 mg/kg q8h (3 m – 11 y), 10 mg/kg q8h (≥ 12 y).** " +
                "Neonatal HSV: 20 mg/kg q8h × 14–21 days.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (aciclovir)",
            injection = InjectionDetail(
                vial = "Vial 250 mg powder",
                reconstitute = "+ 10 mL water for injection → 25 mg/mL",
                furtherDilute = "**Mandatory:** further dilute to ≤ 5 mg/mL in 0.9 % NaCl (e.g. 250 mg in 50–100 mL)",
                dose = "Standard: 10 mg/kg     Encephalitis / neonatal: 20 mg/kg",
                frequency = "every 8 h (× 3/day)",
                route = "IV",
                infusionTime = "infusion over ≥ 60 min (NEVER bolus)",
                maxPerDose = "500 mg (1 g if encephalitis)",
                maxPerDay = "Adjust for renal impairment",
                cautions = "Crystalluria → AKI if pushed fast or under-hydrated. **Maintain fluid intake** during course. " +
                    "Reduce dose if eGFR < 50 mL/min.",
            ),
        ),
        PediDrug(
            id = "amikacin-100",
            name = "Amikacin amp 100 mg / 2 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 50.0,
                frequencyLabel = "once daily",
                maxMgPerDose = 1500.0,
            ),
            notes = "Once-daily extended interval: 15 mg/kg q24h (preferred). Conventional 7.5 mg/kg q12h " +
                "(older regimen). Neonate: 15 mg/kg q24h adjusted by gestational age + level.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (amikacin)",
            injection = InjectionDetail(
                vial = "Amp 100 mg / 2 mL = 50 mg/mL",
                furtherDilute = "Dilute in 100–200 mL N.S → ≤ 5 mg/mL",
                dose = "15 mg/kg once daily",
                frequency = "once daily",
                route = "IV / IM",
                infusionTime = "infusion over 30–60 min",
                maxPerDose = "1.5 g",
                maxPerDay = "1.5 g",
                cautions = "Nephro- + ototoxic — trough < 5 mg/L. TDM mandatory after 24 h.",
            ),
        ),
        PediDrug(
            id = "amikacin-500",
            name = "Amikacin amp 500 mg / 2 mL",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 100.0,
                frequencyLabel = "once daily",
                maxMgPerDose = 1500.0,
            ),
            notes = "Same dose as the 100 mg amp; the 500 mg amp is concentrated 250 mg/mL. " +
                "Always further dilute before IV.",
            accent = Accent.Cyan,
            reference = "BNFc 2024",
            injection = InjectionDetail(
                vial = "Amp 500 mg / 2 mL = 250 mg/mL (highly concentrated)",
                reconstitute = "Add 3 mL N.S → 5 mL at 100 mg/mL",
                furtherDilute = "Then dilute in 100–200 mL N.S → ≤ 5 mg/mL",
                dose = "15 mg/kg once daily",
                frequency = "once daily",
                route = "IV (preferred)",
                infusionTime = "infusion over 30–60 min",
                maxPerDose = "1.5 g",
                cautions = "Same as 100 mg amp; the 500 mg amp must NEVER be given undiluted.",
            ),
        ),
        PediDrug(
            id = "vancomycin",
            name = "Vancomycin 500 mg vial",
            mode = VolumeMgPerKg(
                mgPerKg = 15.0,
                mgPerCc = 100.0,
                frequencyLabel = "every 6 h",
                maxMgPerDose = 1000.0,
            ),
            notes = "1 m – 11 y: 15 mg/kg q6h (max 2 g/dose). 12 – 17 y: 15–20 mg/kg q8–12h (max 2 g/dose). " +
                "Neonate: 15 mg/kg with frequency by post-menstrual age. CNS / MRSA: stay at q6h. " +
                "**Always run over ≥ 60 min** — red-man syndrome if faster.",
            accent = Accent.Cyan,
            reference = "BNFc 2024 (vancomycin)",
            injection = InjectionDetail(
                vial = "Vial 500 mg powder",
                reconstitute = "+ 10 mL water for injection → 50 mg/mL",
                furtherDilute = "**Mandatory:** further dilute in 100 mL N.S → 5 mg/mL",
                dose = "15 mg/kg",
                frequency = "every 6 h (every 8 h ≥ 12 y)",
                route = "IV",
                infusionTime = "infusion over ≥ 60 min (≤ 10 mg/min)",
                maxPerDose = "1 g initial (2 g possible in adolescents)",
                maxPerDay = "4 g",
                cautions = "Red-man syndrome (histamine release) if pushed fast — slow rate, give antihistamine. " +
                    "Trough level 10–20 mg/L (15–20 if MRSA bacteraemia / CNS).",
            ),
        ),
        PediDrug(
            id = "meropenem-neonate",
            name = "Meropenem 500 mg vial — neonate",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 100.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 1000.0,
            ),
            notes = "Neonate ≤ 7 d: 20 mg/kg q12h. Neonate 7 d – 1 m: 20 mg/kg q8h. " +
                "Severe sepsis / meningitis (neonate): 40 mg/kg q8h.",
            accent = Accent.Yellow,
            reference = "BNFc 2024 (meropenem neonatal)",
            injection = InjectionDetail(
                vial = "Vial 500 mg powder",
                reconstitute = "+ 10 mL water for injection → 50 mg/mL",
                furtherDilute = "Further dilute in 20–50 mL N.S → 10–20 mg/mL",
                dose = "Standard: 20 mg/kg     Meningitis: 40 mg/kg",
                frequency = "≤ 7 d: every 12 h     7 d – 1 m: every 8 h",
                route = "IV",
                infusionTime = "infusion over 15–30 min (or push slow over 5 min)",
                maxPerDose = "2 g",
                maxPerDay = "Adjust for renal impairment",
                cautions = "Lowers seizure threshold — caution with CNS disorders, valproate. Check electrolytes.",
            ),
        ),
        PediDrug(
            id = "meropenem-after",
            name = "Meropenem 500 mg vial — > 1 month",
            mode = VolumeMgPerKg(
                mgPerKg = 20.0,
                mgPerCc = 100.0,
                frequencyLabel = "every 8 h",
                maxMgPerDose = 2000.0,
            ),
            notes = "1 m – 11 y: 20 mg/kg q8h (max 1 g). Severe / CNS / cystic fibrosis: 40 mg/kg q8h (max 2 g). " +
                "12 – 17 y: 1 g q8h (severe 2 g q8h).",
            accent = Accent.Yellow,
            reference = "BNFc 2024 (meropenem)",
            injection = InjectionDetail(
                vial = "Vial 500 mg or 1 g powder",
                reconstitute = "+ 10 mL water for injection → 50 mg/mL (1 g + 20 mL)",
                furtherDilute = "Further dilute in 50–100 mL N.S → ≤ 20 mg/mL",
                dose = "Standard: 20 mg/kg     Meningitis / CF: 40 mg/kg",
                frequency = "every 8 h (× 3/day)",
                route = "IV",
                infusionTime = "infusion over 15–30 min (or push slow over 5 min)",
                maxPerDose = "2 g",
                maxPerDay = "6 g",
                cautions = "Same as neonatal version. Stable only 1 h after dilution at room temp.",
            ),
        ),
    )
}
