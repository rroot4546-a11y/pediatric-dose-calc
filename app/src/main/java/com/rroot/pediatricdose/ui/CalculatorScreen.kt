package com.rroot.pediatricdose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rroot.pediatricdose.domain.DoseCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onBack: () -> Unit) {
    var weight by remember { mutableStateOf("") }
    var mgPerKg by remember { mutableStateOf("") }
    var concentration by remember { mutableStateOf("") }
    var maxSingle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generic mg/kg") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NumberField(
                label = "Weight (kg)",
                value = weight,
                onValueChange = { weight = sanitizeDecimal(it) },
            )
            NumberField(
                label = "Dose (mg/kg)",
                value = mgPerKg,
                onValueChange = { mgPerKg = sanitizeDecimal(it) },
            )
            NumberField(
                label = "Concentration (mg/mL) \u2014 optional",
                value = concentration,
                onValueChange = { concentration = sanitizeDecimal(it) },
            )
            NumberField(
                label = "Max single dose (mg) \u2014 optional",
                value = maxSingle,
                onValueChange = { maxSingle = sanitizeDecimal(it) },
            )

            val w = weight.toDoubleOrNull()
            val d = mgPerKg.toDoubleOrNull()
            val c = concentration.toDoubleOrNull()?.takeIf { it > 0 }
            val cap = maxSingle.toDoubleOrNull()?.takeIf { it > 0 }

            val weightInRange = w != null && w in DoseCalculator.WEIGHT_RANGE_KG
            val canCompute = w != null && d != null && weightInRange && d >= 0

            if (w != null && !weightInRange) {
                Text(
                    "Weight outside plausible paediatric range " +
                        "(${DoseCalculator.WEIGHT_RANGE_KG.start}\u2013" +
                        "${DoseCalculator.WEIGHT_RANGE_KG.endInclusive} kg). " +
                        "Please double-check.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(4.dp))

            if (canCompute) {
                val result = DoseCalculator.singleDose(
                    weightKg = w!!,
                    mgPerKg = d!!,
                    concentrationMgPerMl = c,
                    maxSingleMg = cap,
                )
                ResultCard(result)
            } else {
                Text(
                    "Enter weight and mg/kg to see the result.",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ResultCard(result: DoseCalculator.SingleDose) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Calculated dose", style = MaterialTheme.typography.titleMedium)
            Text(
                "${DoseCalculator.round(result.doseMg, 2)} mg",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            result.volumeMl?.let {
                Text(
                    "= ${DoseCalculator.round(it, 2)} mL",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            if (result.capped) {
                Text(
                    "Capped at the supplied maximum " +
                        "(uncapped: ${DoseCalculator.round(result.rawDoseMg, 2)} mg).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
    )
}

internal fun sanitizeDecimal(input: String): String {
    // Allow digits plus one decimal point. Empty string is valid (blank field).
    if (input.isEmpty()) return input
    val cleaned = input.filter { it.isDigit() || it == '.' }
    val firstDot = cleaned.indexOf('.')
    if (firstDot < 0) return cleaned
    return cleaned.substring(0, firstDot + 1) +
        cleaned.substring(firstDot + 1).replace(".", "")
}

