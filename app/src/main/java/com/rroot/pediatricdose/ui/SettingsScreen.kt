package com.rroot.pediatricdose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rroot.pediatricdose.ai.AiModels
import com.rroot.pediatricdose.ai.SecureStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(store: SecureStore) {
    var token by rememberSaveable { mutableStateOf(store.openRouterToken.orEmpty()) }
    var modelId by rememberSaveable { mutableStateOf(store.modelId) }
    var revealed by rememberSaveable { mutableStateOf(false) }
    var savedFlash by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            "AI Assistant — OpenRouter",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "PediCalc can answer free-text clinical questions about a child by routing them through OpenRouter (https://openrouter.ai). Your API token is stored encrypted on this device only and is sent solely to api.openrouter.ai.",
            fontSize = 13.sp,
            color = Color(0xFF555555),
        )

        OutlinedTextField(
            value = token,
            onValueChange = { token = it.trim() },
            label = { Text("OpenRouter API token") },
            placeholder = { Text("sk-or-v1-…") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (revealed) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { revealed = !revealed }) {
                    Icon(
                        imageVector = if (revealed) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (revealed) "Hide token" else "Show token",
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            "Get a token from https://openrouter.ai/keys (free tier available).",
            fontSize = 11.sp,
            color = Color(0xFF777777),
        )

        Spacer(Modifier.height(8.dp))
        Text("Model", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

        AiModels.curated.forEach { m ->
            val selected = modelId == m.id
            Surface(
                onClick = { modelId = m.id },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = selected, onClick = { modelId = m.id })
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text(m.display, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${m.id} — ${m.tag}",
                            fontSize = 11.sp,
                            color = Color(0xFF555555),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Or use a custom model id:", fontSize = 13.sp)
        OutlinedTextField(
            value = modelId,
            onValueChange = { modelId = it.trim() },
            label = { Text("OpenRouter model id") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                store.openRouterToken = token.takeIf { it.isNotBlank() }
                store.modelId = modelId.ifBlank { SecureStore.DEFAULT_MODEL }
                savedFlash = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Save") }

        if (savedFlash) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2_000)
                savedFlash = false
            }
            Text(
                "Saved ✓",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.SemiBold,
            )
        }

        Divider(Modifier.padding(vertical = 16.dp))
        Text(
            "Disclaimer",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )
        Text(
            "PediCalc AI is a decision-support tool. The final dose, dilution and route are the prescribing physician's responsibility. AI replies can be wrong — verify with the BNFc and local protocols.",
            fontSize = 12.sp,
            color = Color(0xFF555555),
        )
    }
}
