package com.rroot.pediatricdose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rroot.pediatricdose.data.Accent
import com.rroot.pediatricdose.data.InjectionDetail
import com.rroot.pediatricdose.data.PediDrug
import com.rroot.pediatricdose.data.PediDrugList
import com.rroot.pediatricdose.domain.ComputedDose
import com.rroot.pediatricdose.domain.QuickDose

private val Cyan = Color(0xFFB2EBF2)
private val Yellow = Color(0xFFFFF59D)
private val GrayBand = Color(0xFFE0E0E0)
private val RedHighlight = Color(0xFFEF5350)
private val LabelText = Color(0xFF0D47A1)
private val ValueText = Color(0xFFD32F2F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickReferenceScreen(weightState: MutableState<String>) {
    val weightKg = weightState.value.toDoubleOrNull() ?: 0.0
    var showMaximums by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        // Sticky header: weight input + max-dose toggle
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
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
                        placeholder = { Text("weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                    )
                    Spacer(Modifier.width(10.dp))
                    Button(
                        onClick = { /* recomputation is automatic */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Calculate", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Maximum dose", fontSize = 14.sp)
                    Spacer(Modifier.weight(1f))
                    Switch(checked = showMaximums, onCheckedChange = { showMaximums = it })
                }
            }
        }

        // Drug list
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = PediDrugList.all, key = { it.id }) { drug ->
                drug.sectionHeader?.let { header ->
                    SectionHeader(header)
                }
                DrugRow(drug = drug, weightKg = weightKg, showMaximums = showMaximums)
            }
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Reference values aligned with BNFc 2024 / Nelson's Pediatrics 22e. Always verify the prescription with the responsible physician.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
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
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF424242),
        )
    }
}

@Composable
private fun DrugRow(drug: PediDrug, weightKg: Double, showMaximums: Boolean) {
    val computed: ComputedDose = QuickDose.compute(drug, weightKg)
    val bg = when (drug.accent) {
        Accent.Cyan -> Cyan
        Accent.Yellow -> Yellow
        Accent.Gray -> GrayBand
        Accent.Red -> RedHighlight.copy(alpha = 0.18f)
        Accent.Plain -> Color.White
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bg, RoundedCornerShape(6.dp))
                .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = drug.name,
                modifier = Modifier.weight(1f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = LabelText,
            )
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = computed.primary,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (computed.capped) Color(0xFFB71C1C) else ValueText,
                    fontSize = 14.sp,
                )
                if (computed.frequency.isNotBlank()) {
                    Text(
                        text = computed.frequency,
                        fontSize = 11.sp,
                        color = Color(0xFF424242),
                    )
                }
            }
        }
        // Secondary line: dose in mg or recipe
        computed.secondary?.let { sec ->
            Text(
                text = sec,
                fontSize = 11.sp,
                color = Color(0xFF555555),
                modifier = Modifier.padding(start = 12.dp, top = 2.dp),
            )
        }
        // Always-visible bedside recipe block for injectables
        drug.injection?.let { inj ->
            InjectionDetailBlock(inj)
        }
        if (showMaximums && drug.notes.isNotBlank()) {
            Text(
                text = drug.notes,
                fontSize = 11.sp,
                color = Color(0xFF424242),
                modifier = Modifier.padding(start = 12.dp, top = 2.dp, end = 8.dp),
            )
        }
        if (showMaximums && drug.reference.isNotBlank()) {
            Text(
                text = "Ref: ${drug.reference}",
                fontSize = 10.sp,
                color = Color(0xFF607D8B),
                modifier = Modifier.padding(start = 12.dp, top = 2.dp),
            )
        }
        if (computed.capped) {
            Text(
                text = "⚠ Capped at maximum recommended dose.",
                fontSize = 11.sp,
                color = Color(0xFFB71C1C),
                modifier = Modifier.padding(start = 12.dp, top = 2.dp),
            )
        }
    }
}

@Composable
private fun InjectionDetailBlock(inj: InjectionDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(Color(0xFFF7FBFF), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFFCFD8DC), RoundedCornerShape(6.dp))
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
                color = Color(0xFFB71C1C),
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 1.dp)) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = LabelText,
            modifier = Modifier.width(96.dp),
        )
        Text(
            text = value,
            fontSize = 11.sp,
            color = Color(0xFF212121),
            modifier = Modifier.weight(1f),
        )
    }
}
