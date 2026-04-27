package com.rroot.pediatricdose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import com.rroot.pediatricdose.data.Accent
import com.rroot.pediatricdose.data.DoseMode
import com.rroot.pediatricdose.data.InjectionDetail
import com.rroot.pediatricdose.data.PediDrug
import com.rroot.pediatricdose.data.PediDrugList
import com.rroot.pediatricdose.domain.IndicationResult
import com.rroot.pediatricdose.domain.ComputedDose
import com.rroot.pediatricdose.domain.QuickDose
import com.rroot.pediatricdose.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickReferenceScreen(weightState: MutableState<String>) {
    val weightKg = weightState.value.toDoubleOrNull() ?: 0.0
    val palette = AppColors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Sticky header: weight input + max-dose toggle
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        value = weightState.value,
                        onValueChange = { v ->
                            weightState.value = v.filter { it.isDigit() || it == '.' }.take(6)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("weight (kg)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = palette.border,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                    )
                    Spacer(Modifier.width(10.dp))
                    Button(
                        onClick = { /* recomputation is automatic */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Calculate", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Tap a drug to see vial / dilution / dose / time / max",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Drug list
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = PediDrugList.all, key = { it.id }) { drug ->
                drug.sectionHeader?.let { header ->
                    SectionHeader(header)
                }
                DrugRow(drug = drug, weightKg = weightKg)
            }
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Reference values aligned with BNFc 2024 / Nelson's Pediatrics 22e. Always verify the prescription with the responsible physician.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "PediCalc AI · Designed by Salah Ahmod, Internal Medicine Resident · Instagram @salah_ahmod",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 4.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DrugRow(drug: PediDrug, weightKg: Double) {
    val computed: ComputedDose = QuickDose.compute(drug, weightKg)
    val palette = AppColors
    val bg = when (drug.accent) {
        Accent.Cyan -> palette.accentCyan
        Accent.Yellow -> palette.accentYellow
        Accent.Gray -> palette.accentGray
        Accent.Red -> palette.accentRed
        Accent.Plain -> MaterialTheme.colorScheme.surface
    }
    var expanded by rememberSaveable(drug.id) { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bg, RoundedCornerShape(6.dp))
                .border(1.dp, palette.border, RoundedCornerShape(6.dp))
                .clickable { expanded = !expanded }
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = drug.name,
                modifier = Modifier.weight(1f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = palette.label,
            )
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = computed.primary,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (computed.capped) palette.warn else palette.value,
                    fontSize = 14.sp,
                )
                if (computed.frequency.isNotBlank()) {
                    Text(
                        text = computed.frequency,
                        fontSize = 11.sp,
                        color = palette.secondaryText,
                    )
                }
            }
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = palette.label,
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column {
                // Secondary line: dose in mg or recipe
                computed.secondary?.let { sec ->
                    Text(
                        text = sec,
                        fontSize = 11.sp,
                        color = palette.secondaryText,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp),
                    )
                }
                // Multi-indication injectable: render the per-indication table.
                (drug.mode as? DoseMode.MultiIndicationMgPerKg)?.let { mode ->
                    IndicationsTable(
                        rows = QuickDose.computeIndications(mode, weightKg),
                        showCc = true,
                    )
                }
                drug.injection?.let { inj ->
                    InjectionDetailBlock(inj)
                }
                if (drug.notes.isNotBlank()) {
                    Text(
                        text = drug.notes,
                        fontSize = 11.sp,
                        color = palette.secondaryText,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp, end = 8.dp),
                    )
                }
                if (drug.reference.isNotBlank()) {
                    Text(
                        text = "Ref: ${drug.reference}",
                        fontSize = 10.sp,
                        color = palette.mutedText,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp),
                    )
                }
                if (computed.capped) {
                    Text(
                        text = "⚠ Capped at maximum recommended dose.",
                        fontSize = 11.sp,
                        color = palette.warn,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun InjectionDetailBlock(inj: InjectionDetail) {
    val palette = AppColors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(palette.cardBg, RoundedCornerShape(6.dp))
            .border(1.dp, palette.border, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        DetailRow("Vial", inj.vial)
        inj.reconstitute?.let { DetailRow("Reconstitute", it) }
        inj.furtherDilute?.let { DetailRow("Dilute", it) }
        DetailRow("Dose", inj.dose)
        DetailRow("Frequency", inj.frequency)
        DetailRow("Route", inj.route)
        DetailRow("Time", inj.infusionTime)
        inj.maxPerDose?.let { DetailRow("Max / dose", it) }
        inj.maxPerDay?.let { DetailRow("Max / day", it) }
        inj.cautions?.let {
            Spacer(Modifier.height(2.dp))
            Text(
                text = "⚠ $it",
                fontSize = 11.sp,
                color = palette.warn,
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    val palette = AppColors
    Row(modifier = Modifier.padding(vertical = 1.dp)) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.label,
            modifier = Modifier.width(96.dp),
        )
        Text(
            text = value,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
internal fun IndicationsTable(rows: List<IndicationResult>, showCc: Boolean) {
    val palette = AppColors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(palette.cardBg, RoundedCornerShape(6.dp))
            .border(1.dp, palette.border, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        Text(
            text = "Dose by indication",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = palette.label,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        for ((idx, r) in rows.withIndex()) {
            if (idx > 0) {
                Spacer(Modifier.height(6.dp))
                androidx.compose.material3.HorizontalDivider(
                    color = palette.border.copy(alpha = 0.4f),
                )
                Spacer(Modifier.height(6.dp))
            }
            Text(
                text = r.indication,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = palette.label,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${formatMgPerKg(r.mgPerKg)} mg/kg",
                    fontSize = 11.sp,
                    color = palette.secondaryText,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = if (showCc) "${r.cc}  •  ${r.mgPerDose}" else r.mgPerDose,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (r.capped) palette.warn else palette.value,
                )
            }
            Row {
                Text(
                    text = r.frequency,
                    fontSize = 11.sp,
                    color = palette.secondaryText,
                    modifier = Modifier.weight(1f),
                )
                if (r.route.isNotBlank()) {
                    Text(
                        text = r.route,
                        fontSize = 11.sp,
                        color = palette.secondaryText,
                    )
                }
            }
            if (r.maxLabel.isNotBlank()) {
                Text(
                    text = if (r.capped) "${r.maxLabel}  ⚠ capped" else r.maxLabel,
                    fontSize = 10.sp,
                    color = if (r.capped) palette.warn else palette.mutedText,
                )
            }
            if (r.note.isNotBlank()) {
                Text(
                    text = r.note,
                    fontSize = 10.sp,
                    color = palette.mutedText,
                )
            }
        }
    }
}

private fun formatMgPerKg(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        // strip trailing zeros up to 3 decimal places
        "%.3f".format(value).trimEnd('0').trimEnd('.')
    }
}
