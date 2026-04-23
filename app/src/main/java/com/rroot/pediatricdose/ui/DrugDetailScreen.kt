package com.rroot.pediatricdose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rroot.pediatricdose.data.DoseRegimen
import com.rroot.pediatricdose.data.Drug
import com.rroot.pediatricdose.data.DrugRepository
import com.rroot.pediatricdose.domain.DoseCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugDetailScreen(name: String, onBack: () -> Unit) {
    val drug = remember(name) { DrugRepository.findByName(name) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(drug?.name ?: name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                ),
            )
        },
    ) { inner ->
        if (drug == null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(16.dp),
            ) {
                Text("Drug not found.")
            }
            return@Scaffold
        }

        DrugDetailBody(drug, Modifier.padding(inner))
    }
}

@Composable
private fun DrugDetailBody(drug: Drug, modifier: Modifier) {
    var weight by remember { mutableStateOf("") }
    var selectedRegimenIndex by remember { mutableStateOf(0) }
    var concentrationIndex by remember { mutableStateOf(0) }

    val regimen = drug.regimens[selectedRegimenIndex]
    val w = weight.toDoubleOrNull()
    val concentration = drug.liquidConcentrationsMgPerMl.getOrNull(concentrationIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(drug.description, style = MaterialTheme.typography.bodyMedium)

        if (drug.regimens.size > 1) {
            Text("Indication", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScrollable(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                drug.regimens.forEachIndexed { idx, r ->
                    AssistChip(
                        onClick = { selectedRegimenIndex = idx },
                        label = { Text(r.indication) },
                        leadingIcon = null,
                    )
                }
            }
        }

        RegimenCard(regimen)

        NumberField(
            label = "Weight (kg)",
            value = weight,
            onValueChange = { weight = sanitizeDecimal(it) },
        )

        if (drug.liquidConcentrationsMgPerMl.isNotEmpty()) {
            Text("Liquid concentration (mg/mL)", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                drug.liquidConcentrationsMgPerMl.forEachIndexed { idx, c ->
                    AssistChip(
                        onClick = { concentrationIndex = idx },
                        label = {
                            val mgPer5ml = DoseCalculator.round(c * 5, 0).toInt()
                            Text("$mgPer5ml mg / 5 mL")
                        },
                    )
                }
            }
        }

        HorizontalDivider()

        if (w != null && w in DoseCalculator.WEIGHT_RANGE_KG) {
            CalculationResult(regimen, w, concentration)
        } else if (w != null) {
            Text(
                "Weight outside plausible paediatric range.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            Text(
                "Enter weight to see the calculated dose.",
                style = MaterialTheme.typography.bodySmall,
            )
        }

        regimen.note?.let {
            Spacer(Modifier.height(4.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Note", style = MaterialTheme.typography.titleSmall)
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun RegimenCard(regimen: DoseRegimen) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(regimen.indication, style = MaterialTheme.typography.titleMedium)
            Text(
                buildString {
                    if (regimen.minMgPerKg == regimen.maxMgPerKg) {
                        append("${regimen.minMgPerKg} mg/kg")
                    } else {
                        append("${regimen.minMgPerKg}\u2013${regimen.maxMgPerKg} mg/kg")
                    }
                    regimen.intervalHours?.let { append(" every ${it} h") }
                    append(" \u00b7 ${regimen.route}")
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            val caps = buildList {
                regimen.maxSingleMg?.let { add("max ${it.stripTrailing()} mg/dose") }
                regimen.maxDailyMg?.let { add("max ${it.stripTrailing()} mg/day") }
            }
            if (caps.isNotEmpty()) {
                Text(
                    caps.joinToString(" \u00b7 "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            Text(
                "Reference: ${regimen.reference}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun CalculationResult(
    regimen: DoseRegimen,
    weightKg: Double,
    concentrationMgPerMl: Double?,
) {
    val lowDose = DoseCalculator.singleDose(
        weightKg = weightKg,
        mgPerKg = regimen.minMgPerKg,
        concentrationMgPerMl = concentrationMgPerMl,
        maxSingleMg = regimen.maxSingleMg,
    )
    val highDose = DoseCalculator.singleDose(
        weightKg = weightKg,
        mgPerKg = regimen.maxMgPerKg,
        concentrationMgPerMl = concentrationMgPerMl,
        maxSingleMg = regimen.maxSingleMg,
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Calculated dose (per administration)", style = MaterialTheme.typography.titleMedium)
            val mgText = if (regimen.minMgPerKg == regimen.maxMgPerKg) {
                "${DoseCalculator.round(lowDose.doseMg, 2)} mg"
            } else {
                "${DoseCalculator.round(lowDose.doseMg, 2)} \u2013 " +
                    "${DoseCalculator.round(highDose.doseMg, 2)} mg"
            }
            Text(mgText, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)

            if (concentrationMgPerMl != null) {
                val mlText = if (regimen.minMgPerKg == regimen.maxMgPerKg) {
                    "${DoseCalculator.round(lowDose.volumeMl!!, 2)} mL"
                } else {
                    "${DoseCalculator.round(lowDose.volumeMl!!, 2)} \u2013 " +
                        "${DoseCalculator.round(highDose.volumeMl!!, 2)} mL"
                }
                Text("= $mlText", style = MaterialTheme.typography.titleMedium)
            }

            if (lowDose.capped || highDose.capped) {
                Text(
                    "Dose capped at ${regimen.maxSingleMg?.stripTrailing()} mg " +
                        "(adult / formulary maximum).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            regimen.intervalHours?.let { hrs ->
                val perDay = 24 / hrs
                if (perDay >= 1) {
                    val totalMaxDaily = DoseCalculator.round(highDose.doseMg * perDay, 2)
                    Text(
                        "Dosing every ${hrs} h (\u2248 ${perDay} doses/day). " +
                            "Approx. total/day at upper mg/kg: ${totalMaxDaily} mg.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

private fun Double.stripTrailing(): String {
    val v = this
    return if (v == v.toLong().toDouble()) v.toLong().toString() else v.toString()
}

/** Horizontal scroll modifier for pill rows. */
@Composable
private fun Modifier.horizontalScrollable(): Modifier {
    val state = rememberScrollState()
    return this.then(horizontalScroll(state))
}
