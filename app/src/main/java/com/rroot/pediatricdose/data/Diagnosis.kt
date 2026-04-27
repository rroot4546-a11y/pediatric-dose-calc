package com.rroot.pediatricdose.data

/**
 * A paediatric clinical diagnosis with the standard first-line treatment
 * options. Each option resolves to the same drug entries used elsewhere
 * in the app, so the calculator can compute the volume.
 *
 * The diagnosis screen is intended as decision support — the underlying
 * doses still come from `PediSyrupList` / `PediDrugList`, the per-disease
 * notes summarise the indication-specific caveats (duration, severity
 * cut-offs, when to escalate). Always verify the live BNFc monograph and
 * UpToDate recommendation before prescribing.
 */
data class Diagnosis(
    val id: String,
    val name: String,
    val category: String,
    val firstLine: List<TreatmentOption>,
    val notes: String,
    val redFlags: List<String> = emptyList(),
    val reference: String = "BNFc 2024 / UpToDate",
)

/**
 * One drug option for a diagnosis. [drugId] references the drug's `id` in
 * either `PediDrugList` (injections) or `PediSyrupList` (oral). [route]
 * is shown as a small chip (PO / IV / IM / nebulised / PR …).
 */
data class TreatmentOption(
    val drugId: String,
    val route: String,
    val durationLabel: String = "",
    val noteForThisDiagnosis: String = "",
)

object DiagnosisList {

    val all: List<Diagnosis> = listOf(

        // ------------------------------------------------------------------
        // Respiratory
        // ------------------------------------------------------------------
        Diagnosis(
            id = "dx-cap",
            name = "Community-acquired pneumonia (CAP)",
            category = "Respiratory",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-amoxicillin",
                    route = "PO",
                    durationLabel = "5–7 days",
                    noteForThisDiagnosis = "Use high-dose 30 mg/kg × 3 (90 mg/kg/d) for suspected pneumococcus.",
                ),
                TreatmentOption(
                    drugId = "syr-azithromycin",
                    route = "PO",
                    durationLabel = "3–5 days",
                    noteForThisDiagnosis = "Add for atypical cover (>5 y, slow response, suspect mycoplasma).",
                ),
                TreatmentOption(
                    drugId = "syr-coamoxiclav",
                    route = "PO",
                    durationLabel = "5–7 days",
                    noteForThisDiagnosis = "Second-line if amoxicillin fails or aspiration risk.",
                ),
                TreatmentOption(
                    drugId = "ceftriaxone",
                    route = "IV/IM",
                    durationLabel = "10–14 days (severe)",
                    noteForThisDiagnosis = "Hospitalised / severe CAP: 50–75 mg/kg once daily.",
                ),
                TreatmentOption(
                    drugId = "paracetamol-iv",
                    route = "IV",
                    noteForThisDiagnosis = "Antipyretic for ill child.",
                ),
            ),
            notes = "Tachypnoea is the single most useful clinical sign (WHO IMCI). Outpatient: oral amoxicillin × 5 days. Inpatient (severe / hypoxia): IV ampicillin or ceftriaxone + macrolide if atypical features.",
            redFlags = listOf(
                "Saturation < 92% on room air",
                "Severe respiratory distress / grunting / cyanosis",
                "Inability to feed",
                "Suspected complicated pneumonia (effusion, empyema, abscess)",
            ),
            reference = "BNFc 2024 / UpToDate / BTS 2011 / WHO IMCI",
        ),

        Diagnosis(
            id = "dx-bronchiolitis",
            name = "Bronchiolitis (RSV)",
            category = "Respiratory",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "ns-bolus",
                    route = "IV",
                    noteForThisDiagnosis = "Only if dehydrated / shock. Maintenance fluids if not feeding.",
                ),
                TreatmentOption(
                    drugId = "maintenance",
                    route = "IV",
                    noteForThisDiagnosis = "Use isotonic fluids (avoid hyponatraemia).",
                ),
                TreatmentOption(
                    drugId = "paracetamol-iv",
                    route = "IV",
                    noteForThisDiagnosis = "If febrile.",
                ),
            ),
            notes = "Mostly supportive: oxygen if SpO₂ ≤ 92%, NG/IV fluids if not feeding. Bronchodilators, steroids and antibiotics are NOT routinely indicated. High-flow nasal cannula for moderate–severe disease.",
            redFlags = listOf(
                "Apnoea (especially preterm < 6 weeks)",
                "SpO₂ < 92% room air",
                "Severe distress / exhaustion",
                "Poor feeding < 50% of normal intake",
            ),
            reference = "NICE NG9 / AAP 2014 / UpToDate",
        ),

        Diagnosis(
            id = "dx-asthma-exac",
            name = "Acute asthma exacerbation",
            category = "Respiratory",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-dexamethasone",
                    route = "PO",
                    durationLabel = "1–2 days",
                    noteForThisDiagnosis = "0.6 mg/kg × 1–2 doses (max 16 mg). Equivalent to prednisolone 1 mg/kg × 3 days.",
                ),
                TreatmentOption(
                    drugId = "syr-salbutamol",
                    route = "PO",
                    noteForThisDiagnosis = "Inhaled MDI + spacer is preferred — oral form mostly historical.",
                ),
                TreatmentOption(
                    drugId = "hydrocortisone",
                    route = "IV",
                    noteForThisDiagnosis = "Severe / unable to take PO: 4 mg/kg × 4.",
                ),
                TreatmentOption(
                    drugId = "aminophylline",
                    route = "IV",
                    noteForThisDiagnosis = "Refractory to inhaled beta-agonist + steroid + ipratropium.",
                ),
            ),
            notes = "Stepwise: O₂ to keep SpO₂ ≥ 94%, salbutamol MDI 6–10 puffs via spacer (q20 min), ipratropium for moderate–severe, oral / IV steroid early. IV magnesium for severe. Aminophylline if still refractory.",
            redFlags = listOf(
                "Silent chest, exhaustion, drowsiness",
                "PEF < 33% best/predicted",
                "SpO₂ < 92% on air",
                "Cyanosis or paradoxical chest movement",
            ),
            reference = "BTS / SIGN 158 / GINA 2024 / UpToDate",
        ),

        Diagnosis(
            id = "dx-croup",
            name = "Croup (laryngotracheobronchitis)",
            category = "Respiratory",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-dexamethasone",
                    route = "PO",
                    durationLabel = "single dose",
                    noteForThisDiagnosis = "0.15 mg/kg PO single dose (mild). 0.6 mg/kg if moderate–severe.",
                ),
                TreatmentOption(
                    drugId = "adrenaline",
                    route = "Nebulised",
                    noteForThisDiagnosis = "Severe stridor at rest: 0.5 mL/kg of 1:1000 (max 5 mL) nebulised; observe ≥ 4 h.",
                ),
                TreatmentOption(
                    drugId = "paracetamol-iv",
                    route = "PO/IV",
                    noteForThisDiagnosis = "Antipyretic / comfort.",
                ),
            ),
            notes = "Single-dose dexamethasone shortens illness even in mild disease. Avoid examining the throat in stridor at rest. Antibiotics not indicated.",
            redFlags = listOf(
                "Stridor at rest",
                "Marked recession / drooling",
                "Cyanosis or altered consciousness — consider epiglottitis / bacterial tracheitis",
            ),
            reference = "BNFc 2024 / UpToDate / Cochrane 2018",
        ),

        Diagnosis(
            id = "dx-otitis",
            name = "Acute otitis media",
            category = "Respiratory / ENT",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-amoxicillin",
                    route = "PO",
                    durationLabel = "5 days (≥ 2 y) / 10 days (< 2 y)",
                    noteForThisDiagnosis = "High-dose 30 mg/kg × 3 / day (90 mg/kg/d). First-line for AOM needing antibiotics.",
                ),
                TreatmentOption(
                    drugId = "syr-coamoxiclav",
                    route = "PO",
                    durationLabel = "5–10 days",
                    noteForThisDiagnosis = "If failed amoxicillin, recurrent disease, or recent antibiotic exposure.",
                ),
                TreatmentOption(
                    drugId = "syr-paracetamol",
                    route = "PO",
                    noteForThisDiagnosis = "First-line analgesic.",
                ),
                TreatmentOption(
                    drugId = "syr-ibuprofen",
                    route = "PO",
                    noteForThisDiagnosis = "Combine with paracetamol if pain not controlled.",
                ),
            ),
            notes = "Most cases resolve without antibiotics. Treat empirically when: < 6 months, bilateral disease in < 2 years, otorrhoea, severe symptoms, or no improvement after 48–72 h of analgesia.",
            redFlags = listOf(
                "Mastoiditis (post-auricular swelling, ear protrusion)",
                "Facial nerve palsy",
                "Persistent fever > 72 h on antibiotics",
            ),
            reference = "NICE NG91 / AAP 2013 / UpToDate",
        ),

        Diagnosis(
            id = "dx-pharyngitis",
            name = "Streptococcal pharyngitis / tonsillitis",
            category = "Respiratory / ENT",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-amoxicillin",
                    route = "PO",
                    durationLabel = "10 days",
                    noteForThisDiagnosis = "50 mg/kg once daily (max 1 g) is also acceptable.",
                ),
                TreatmentOption(
                    drugId = "syr-erythromycin",
                    route = "PO",
                    durationLabel = "10 days",
                    noteForThisDiagnosis = "Penicillin allergy alternative.",
                ),
                TreatmentOption(
                    drugId = "syr-paracetamol",
                    route = "PO",
                ),
                TreatmentOption(
                    drugId = "syr-ibuprofen",
                    route = "PO",
                ),
            ),
            notes = "Antibiotics only after positive throat swab / RADT or strong clinical features (Centor / FeverPAIN). Aim is to prevent rheumatic fever — relevant in our region.",
            reference = "BNFc 2024 / IDSA / UpToDate",
        ),

        // ------------------------------------------------------------------
        // GI
        // ------------------------------------------------------------------
        Diagnosis(
            id = "dx-age",
            name = "Acute gastroenteritis (AGE) with dehydration",
            category = "Gastrointestinal",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "ns-bolus",
                    route = "IV",
                    noteForThisDiagnosis = "Severe dehydration / shock: 20 mL/kg over 5–20 min, repeat if needed.",
                ),
                TreatmentOption(
                    drugId = "maintenance",
                    route = "IV",
                    noteForThisDiagnosis = "Replace deficit + maintenance with isotonic fluids.",
                ),
                TreatmentOption(
                    drugId = "syr-ondansetron",
                    route = "PO",
                    durationLabel = "single dose",
                    noteForThisDiagnosis = "0.15 mg/kg single dose enables ORT; reduces hospital admission.",
                ),
                TreatmentOption(
                    drugId = "syr-zinc-20",
                    route = "PO",
                    durationLabel = "10–14 days",
                    noteForThisDiagnosis = "Reduces duration and severity (WHO/UNICEF).",
                ),
            ),
            notes = "Oral rehydration solution (ORS) is first-line. IV fluids only for failed ORT, severe dehydration, or shock. Antibiotics only for dysentery (bloody stools), suspected cholera, or specific pathogens.",
            redFlags = listOf(
                "Shock / severe dehydration / lethargy",
                "Bloody diarrhoea — consider Shigella, EHEC",
                "Bilious vomiting → surgical cause",
            ),
            reference = "WHO IMCI / NICE CG84 / UpToDate",
        ),

        Diagnosis(
            id = "dx-giardia",
            name = "Giardiasis",
            category = "Gastrointestinal",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-flagyl",
                    route = "PO",
                    durationLabel = "5 days",
                    noteForThisDiagnosis = "15 mg/kg × 1 / day (or 7.5 mg/kg × 3) for 5 days.",
                ),
            ),
            notes = "Tinidazole 50 mg/kg single dose is an effective alternative when available.",
            reference = "BNFc 2024 / UpToDate",
        ),

        // ------------------------------------------------------------------
        // GU
        // ------------------------------------------------------------------
        Diagnosis(
            id = "dx-uti",
            name = "Urinary tract infection",
            category = "Genitourinary",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-cefixime",
                    route = "PO",
                    durationLabel = "7–10 days",
                    noteForThisDiagnosis = "Outpatient first-line in our region. 8 mg/kg once daily.",
                ),
                TreatmentOption(
                    drugId = "syr-bactrim",
                    route = "PO",
                    durationLabel = "3–7 days (cystitis)",
                    noteForThisDiagnosis = "Avoid if local E. coli resistance > 20% or under 6 weeks.",
                ),
                TreatmentOption(
                    drugId = "ceftriaxone",
                    route = "IV",
                    durationLabel = "Until afebrile then complete PO",
                    noteForThisDiagnosis = "Pyelonephritis / unwell / vomiting / under 3 months.",
                ),
                TreatmentOption(
                    drugId = "gentamicin-large",
                    route = "IV",
                    noteForThisDiagnosis = "Add for febrile UTI not improving on cephalosporin alone.",
                ),
            ),
            notes = "Always send a clean catch / catheter / SPA culture before antibiotics. Imaging (US ± DMSA / MCUG) per NICE/AAP guideline. < 3 months = parenteral antibiotics, admit.",
            redFlags = listOf(
                "Age < 3 months → admit, IV antibiotics",
                "Toxic / septic appearance",
                "Failure to respond after 48 h",
            ),
            reference = "NICE NG224 / AAP 2011 / UpToDate",
        ),

        // ------------------------------------------------------------------
        // CNS / fever
        // ------------------------------------------------------------------
        Diagnosis(
            id = "dx-febrile-seizure",
            name = "Febrile seizure",
            category = "Neurology",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "diazepam-pr",
                    route = "PR",
                    durationLabel = "single",
                    noteForThisDiagnosis = "Active seizure > 5 min: 0.5 mg/kg PR (max 10 mg).",
                ),
                TreatmentOption(
                    drugId = "diazepam-iv",
                    route = "IV",
                    noteForThisDiagnosis = "If IV access available: 0.3 mg/kg IV (max 10 mg).",
                ),
                TreatmentOption(
                    drugId = "syr-paracetamol",
                    route = "PO",
                    noteForThisDiagnosis = "Antipyretic comfort — does NOT prevent recurrence.",
                ),
            ),
            notes = "Simple febrile seizures (< 15 min, generalised, no recurrence in 24 h, age 6 months–6 years) are benign. Investigate fever source. LP if focal, prolonged, post-ictal deficit, or < 18 months unimmunised.",
            redFlags = listOf(
                "Age < 6 months or > 6 years",
                "Focal features / prolonged > 15 min",
                "Recurrence within 24 h",
                "Persistent altered consciousness — consider meningitis / encephalitis",
            ),
            reference = "BNFc 2024 / NICE / UpToDate",
        ),

        Diagnosis(
            id = "dx-meningitis",
            name = "Bacterial meningitis (empirical)",
            category = "Neurology",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "ceftriaxone",
                    route = "IV",
                    durationLabel = "7–14 days",
                    noteForThisDiagnosis = "100 mg/kg/d ÷ 2 (or once daily). Cornerstone empirical therapy > 1 month.",
                ),
                TreatmentOption(
                    drugId = "ampicillin",
                    route = "IV",
                    durationLabel = "Add < 3 months",
                    noteForThisDiagnosis = "Listeria cover for neonates / < 3 months: 100 mg/kg × 4.",
                ),
                TreatmentOption(
                    drugId = "syr-dexamethasone",
                    route = "IV",
                    durationLabel = "4 days",
                    noteForThisDiagnosis = "0.15 mg/kg × 4 (with or just before first antibiotic dose) for HiB / pneumococcal meningitis > 1 month.",
                ),
                TreatmentOption(
                    drugId = "ns-bolus",
                    route = "IV",
                    noteForThisDiagnosis = "Resuscitate shock; avoid fluid restriction.",
                ),
            ),
            notes = "Time-critical. Take cultures BEFORE antibiotics only if no delay. Add aciclovir if encephalitis suspected. Notify public health.",
            redFlags = listOf(
                "Petechial / purpuric rash → meningococcal disease",
                "Bulging fontanelle, neck stiffness",
                "Altered consciousness / seizure",
                "Shock — escalate to PICU",
            ),
            reference = "BNFc 2024 / NICE NG240 / UpToDate",
        ),

        // ------------------------------------------------------------------
        // Skin
        // ------------------------------------------------------------------
        Diagnosis(
            id = "dx-skin-cellulitis",
            name = "Skin / soft-tissue infection (cellulitis, impetigo)",
            category = "Skin",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-keflex",
                    route = "PO",
                    durationLabel = "7 days",
                    noteForThisDiagnosis = "First-line for non-purulent cellulitis. 12.5 mg/kg × 4.",
                ),
                TreatmentOption(
                    drugId = "syr-coamoxiclav",
                    route = "PO",
                    durationLabel = "7 days",
                    noteForThisDiagnosis = "If animal/human bite or facial cellulitis.",
                ),
                TreatmentOption(
                    drugId = "syr-erythromycin",
                    route = "PO",
                    durationLabel = "7 days",
                    noteForThisDiagnosis = "Penicillin allergy alternative.",
                ),
            ),
            notes = "Mark borders, follow ≤ 48 h. Admit if systemic symptoms, rapidly spreading, periorbital/orbital, or immunocompromised. MRSA cover (clindamycin / Bactrim) if local prevalence is high or recurrent abscess.",
            redFlags = listOf(
                "Severe pain out of proportion → necrotising fasciitis",
                "Periorbital → orbital cellulitis",
                "Systemic toxicity / sepsis",
            ),
            reference = "BNFc 2024 / IDSA 2014 / UpToDate",
        ),

        // ------------------------------------------------------------------
        // Allergy
        // ------------------------------------------------------------------
        Diagnosis(
            id = "dx-allergic-rhinitis",
            name = "Allergic rhinitis",
            category = "Allergy",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "syr-loratadine",
                    route = "PO",
                    durationLabel = "as needed",
                    noteForThisDiagnosis = "Non-sedating, once daily.",
                ),
                TreatmentOption(
                    drugId = "syr-desloratadine",
                    route = "PO",
                    durationLabel = "as needed",
                    noteForThisDiagnosis = "From 6 months. Once daily.",
                ),
                TreatmentOption(
                    drugId = "syr-chlorpheniramine",
                    route = "PO",
                    noteForThisDiagnosis = "Sedating — limit to short courses, especially night-time itch.",
                ),
            ),
            notes = "Intranasal corticosteroid is the most effective single therapy for moderate–severe disease. Antihistamines for episodic / mild symptoms. Avoid sedating antihistamines in infants.",
            reference = "ARIA 2019 / BNFc 2024",
        ),

        Diagnosis(
            id = "dx-anaphylaxis",
            name = "Anaphylaxis",
            category = "Allergy / emergency",
            firstLine = listOf(
                TreatmentOption(
                    drugId = "adrenaline",
                    route = "IM",
                    noteForThisDiagnosis = "0.01 mg/kg of 1:1000 IM (max 0.5 mg) anterolateral mid-thigh. Repeat every 5 min.",
                ),
                TreatmentOption(
                    drugId = "ns-bolus",
                    route = "IV",
                    noteForThisDiagnosis = "20 mL/kg if shock; repeat as needed.",
                ),
                TreatmentOption(
                    drugId = "hydrocortisone",
                    route = "IV",
                    noteForThisDiagnosis = "4 mg/kg (adjuvant; not first line).",
                ),
                TreatmentOption(
                    drugId = "chlorpheniramine",
                    route = "IV/IM",
                    noteForThisDiagnosis = "0.2 mg/kg adjunct for cutaneous symptoms.",
                ),
            ),
            notes = "IM adrenaline is FIRST and only step that saves life — do not delay for IV access. Call for help, lie patient flat (raise legs), high-flow oxygen, IV fluids if shock. Observe ≥ 6 h after biphasic-risk reaction.",
            redFlags = listOf(
                "Airway compromise / stridor",
                "Hypotension / shock",
                "Persistent / biphasic reaction — admit",
            ),
            reference = "Resus Council UK 2021 / WAO 2020",
        ),
    )

    val categories: List<String>
        get() = all.map { it.category }.distinct()
}
