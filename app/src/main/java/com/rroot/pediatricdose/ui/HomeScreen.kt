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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCalculator: () -> Unit,
    onDrugs: () -> Unit,
    onDisclaimer: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pediatric Dose") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
            )
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Weight-based paediatric dosing",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                "Pick a built-in drug for standard mg/kg ranges with safety caps, " +
                    "or open the generic calculator to enter your own mg/kg.",
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = onCalculator, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Calculate, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Generic mg/kg calculator")
            }
            OutlinedButton(onClick = onDrugs, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.MedicalServices, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Common drugs")
            }

            Spacer(Modifier.height(8.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Reference tool only",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Text(
                        "Always verify against an authoritative formulary before prescribing.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    OutlinedButton(onClick = onDisclaimer) { Text("Read full disclaimer") }
                }
            }
        }
    }
}
