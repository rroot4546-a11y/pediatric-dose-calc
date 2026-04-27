package com.rroot.pediatricdose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rroot.pediatricdose.data.Diagnosis
import com.rroot.pediatricdose.data.DiagnosisList
import com.rroot.pediatricdose.data.DoseMode
import com.rroot.pediatricdose.data.PediDrug
import com.rroot.pediatricdose.data.PediDrugList
import com.rroot.pediatricdose.data.PediSyrupList
import com.rroot.pediatricdose.data.TreatmentOption
import com.rroot.pediatricdose.domain.QuickDose

private val LabelBlue = Color(0xFF0D47A1)
private val ValueRed = Color(0xFFD32F2F)
private val Cyan = Color(0xFFB2EBF2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosesScreen(
    weightState: MutableState<String>,
) {
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    val list = remember { DiagnosisList.all }

    if (selected == null) {
        DiagnosisIndex(
            diagnoses = list,
            weightState = weightState,
            onSelect = { selected = it.id },
        )
    } else {
        val dx = list.firstOrNull { it.id == selected }
        if (dx == null) {
            selected = null
        } else {
            DiagnosisDetail(
                dx = dx,
                weightKg = weightState.value.toDoubleOrNull() ?: 0.0,
                onBack = { selected = null },
            )
        }
    }
}

@Composable
private fun DiagnosisIndex(
    diagnoses: List<Diagnosis>,
    weightState: MutableState<String>,
    onSelect: (Diagnosis) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 2.dp) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = weightState.value,
                        onValueChange = { weightState.value = it.filter { ch -> ch.isDigit() || ch == '.' } },
                        label = { Text("Weight (kg)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search diagnosis") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        val filtered = if (query.isBlank()) diagnoses else diagnoses.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.arabicName.contains(query) ||
                it.category.contains(query, ignoreCase = true)
        }

        val byCategory = filtered.groupBy { it.category }
        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            for ((cat, group) in byCategory) {
                item {
                    Surface(
                        color = LabelBlue.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
                        shape = RoundedCornerShape(6.dp),
                    ) {
                        Text(
                            text = cat,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.titleSmall,
                            color = LabelBlue,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                items(group, key = { it.id }) { dx ->
                    Surface(
                        color = Cyan,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).clickable { onSelect(dx) },
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = dx.name,
                                fontWeight = FontWeight.Bold,
                                color = LabelBlue,
                                fontSize = 15.sp,
                            )
                            Text(
                                text = dx.arabicName,
                                color = LabelBlue.copy(alpha = 0.85f),
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiagnosisDetail(dx: Diagnosis, weightKg: Double, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dx.name, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner).padding(horizontal = 12.dp)) {
            item {
                Text(
                    text = dx.arabicName,
                    fontSize = 16.sp,
                    color = LabelBlue,
                    modifier = Modifier.padding(top = 8.dp),
                )
                Text(
                    text = dx.category,
                    fontSize = 12.sp,
                    color = LabelBlue.copy(alpha = 0.8f),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Approach",
                    fontWeight = FontWeight.Bold,
                    color = LabelBlue,
                )
                Text(text = dx.notes, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))

                if (dx.redFlags.isNotEmpty()) {
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Red flags",
                                fontWeight = FontWeight.Bold,
                                color = ValueRed,
                            )
                            for (f in dx.redFlags) {
                                Text(text = "• $f", color = ValueRed, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                Text(
                    text = "First-line treatment options",
                    fontWeight = FontWeight.Bold,
                    color = LabelBlue,
                )
            }

            items(dx.firstLine) { opt ->
                TreatmentRow(opt = opt, weightKg = weightKg)
            }

            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Reference: ${dx.reference}",
                    fontSize = 12.sp,
                    color = LabelBlue.copy(alpha = 0.8f),
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun TreatmentRow(opt: TreatmentOption, weightKg: Double) {
    val drug: PediDrug? = remember(opt.drugId) {
        PediSyrupList.all.firstOrNull { it.id == opt.drugId }
            ?: PediDrugList.all.firstOrNull { it.id == opt.drugId }
    }
    Surface(
        color = Cyan,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = drug?.name ?: opt.drugId,
                    fontWeight = FontWeight.Bold,
                    color = LabelBlue,
                    modifier = Modifier.weight(1f),
                )
                AssistChip(label = opt.route)
                if (opt.durationLabel.isNotBlank()) {
                    Spacer(Modifier.width(6.dp))
                    AssistChip(label = opt.durationLabel)
                }
            }
            Spacer(Modifier.height(4.dp))
            if (drug != null) {
                renderDoseSummary(drug = drug, weightKg = weightKg)
            } else {
                Text("Drug definition missing.", color = ValueRed)
            }
            if (opt.noteForThisDiagnosis.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(opt.noteForThisDiagnosis, fontSize = 12.sp, color = LabelBlue.copy(alpha = 0.85f))
            }
        }
    }
}

@Composable
private fun AssistChip(label: String) {
    Surface(
        color = LabelBlue.copy(alpha = 0.12f),
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = LabelBlue,
        )
    }
}

@Composable
private fun renderDoseSummary(drug: PediDrug, weightKg: Double) {
    when (val mode = drug.mode) {
        is DoseMode.WeightBasedSyrup -> {
            val row = QuickDose.computeSyrup(mode, weightKg)
            for (p in row.perPreparation) {
                Row {
                    Text(
                        text = p.prepLabel,
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp,
                        color = LabelBlue,
                    )
                    Text(
                        text = "${p.cc} cc  ${row.frequency}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ValueRed,
                    )
                }
            }
            if (row.mgPerDose.isNotBlank()) {
                Text(row.mgPerDose, fontSize = 11.sp, color = LabelBlue.copy(alpha = 0.8f))
            }
        }
        is DoseMode.AgeBanded -> {
            for (band in mode.bands) {
                Row {
                    Text(
                        text = band.label,
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp,
                        color = LabelBlue,
                    )
                    Text(
                        text = "${QuickDose.formatCc(band.cc)} cc  ${band.frequency}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ValueRed,
                    )
                }
            }
        }
        else -> {
            val r = QuickDose.compute(drug, weightKg)
            Row {
                Text(
                    text = r.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (r.capped) ValueRed else ValueRed,
                )
                Spacer(Modifier.width(6.dp))
                Text(text = r.frequency, fontSize = 13.sp, color = LabelBlue)
            }
            r.secondary?.let {
                Text(it, fontSize = 11.sp, color = LabelBlue.copy(alpha = 0.85f))
            }
        }
    }
}
