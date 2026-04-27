package com.rroot.pediatricdose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rroot.pediatricdose.data.Accent
import com.rroot.pediatricdose.data.AgeBand
import com.rroot.pediatricdose.data.DoseMode
import com.rroot.pediatricdose.data.PediDrug
import com.rroot.pediatricdose.data.PediSyrupList
import com.rroot.pediatricdose.domain.QuickDose

private val LabelBlue = Color(0xFF0D47A1)
private val ValueRed = Color(0xFFD32F2F)
private val Cyan = Color(0xFFB2EBF2)
private val Yellow = Color(0xFFFFF59D)
private val Gray = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyrupScreen(
    weightState: MutableState<String>,
) {
    val drugs = remember { PediSyrupList.all }
    var showDetails by rememberSaveable { mutableStateOf(false) }

    val weightKg = weightState.value.toDoubleOrNull() ?: 0.0

    Column(modifier = Modifier.fillMaxSize()) {
        // sticky header — weight + details toggle
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface,
        ) {
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
                    Spacer(Modifier.width(12.dp))
                    Surface(
                        color = Yellow,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(56.dp).widthIn(min = 90.dp),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Calculate", fontWeight = FontWeight.Bold, color = LabelBlue)
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = showDetails, onCheckedChange = { showDetails = it })
                    Spacer(Modifier.width(8.dp))
                    Text(if (showDetails) "Showing prescribing notes" else "Show prescribing notes")
                }
            }
        }

        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 4.dp)) {
            items(drugs, key = { it.id }) { drug ->
                drug.sectionHeader?.let { header ->
                    SectionHeader(header)
                }
                SyrupRow(drug = drug, weightKg = weightKg, showDetails = showDetails)
            }
            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Reference: BNFc 2024 (https://bnfc.nice.org.uk/) + UpToDate. " +
                        "Verify every dose against the live monograph and your local protocol before prescribing.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(label: String) {
    Surface(
        color = LabelBlue.copy(alpha = 0.1f),
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.titleSmall,
            color = LabelBlue,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun SyrupRow(drug: PediDrug, weightKg: Double, showDetails: Boolean) {
    val accent = when (drug.accent) {
        Accent.Cyan -> Cyan
        Accent.Yellow -> Yellow
        Accent.Gray -> Gray
        Accent.Red -> Color(0xFFEF5350)
        Accent.Plain -> MaterialTheme.colorScheme.surface
    }
    Surface(
        color = accent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = drug.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = LabelBlue,
            )
            when (val mode = drug.mode) {
                is DoseMode.WeightBasedSyrup -> {
                    val row = QuickDose.computeSyrup(mode, weightKg)
                    Spacer(Modifier.height(4.dp))
                    for (p in row.perPreparation) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = p.prepLabel,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                color = LabelBlue,
                            )
                            Text(
                                text = p.cc,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (row.capped) ValueRed else ValueRed,
                            )
                            Text(
                                text = " cc  ${row.frequency}",
                                fontSize = 13.sp,
                                color = LabelBlue,
                            )
                        }
                    }
                    if (row.mgPerDose.isNotBlank()) {
                        Text(
                            text = row.mgPerDose + if (row.capped) "  (capped)" else "",
                            fontSize = 12.sp,
                            color = if (row.capped) ValueRed else LabelBlue.copy(alpha = 0.8f),
                        )
                    }
                }
                is DoseMode.AgeBanded -> {
                    AgeBandedRows(mode)
                }
                else -> {
                    val r = QuickDose.compute(drug, weightKg)
                    Text(text = "${r.primary}  ${r.frequency}", fontSize = 14.sp, color = ValueRed)
                }
            }
            if (showDetails && drug.notes.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = drug.notes,
                    fontSize = 12.sp,
                    color = LabelBlue.copy(alpha = 0.85f),
                )
                Text(
                    text = "Reference: ${drug.reference}",
                    fontSize = 11.sp,
                    color = LabelBlue.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun AgeBandedRows(mode: DoseMode.AgeBanded) {
    Spacer(Modifier.height(4.dp))
    Surface(
        color = Color.White.copy(alpha = 0.5f),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            for (band in mode.bands) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = band.label,
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp,
                        color = LabelBlue,
                    )
                    Text(
                        text = QuickDose.formatCc(band.cc),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ValueRed,
                    )
                    Text(
                        text = " cc  ${band.frequency}",
                        fontSize = 12.sp,
                        color = LabelBlue,
                    )
                }
            }
        }
    }
}
