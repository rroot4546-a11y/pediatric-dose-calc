package com.rroot.pediatricdose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rroot.pediatricdose.ai.SecureStore
import com.rroot.pediatricdose.ui.AiChatScreen
import com.rroot.pediatricdose.ui.DiagnosesScreen
import com.rroot.pediatricdose.ui.QuickReferenceScreen
import com.rroot.pediatricdose.ui.SettingsScreen
import com.rroot.pediatricdose.ui.SyrupScreen
import com.rroot.pediatricdose.ui.theme.PediatricDoseTheme

class MainActivity : ComponentActivity() {

    private lateinit var secureStore: SecureStore

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        secureStore = SecureStore(applicationContext)

        setContent {
            PediatricDoseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppShell(secureStore)
                }
            }
        }
    }
}

private enum class Tab(val title: String) {
    Diagnoses("Diagnoses"),
    Syrups("Syrups"),
    Injections("Injections"),
    Ai("AI"),
    Settings("Settings"),
}

@Composable
private fun AppShell(store: SecureStore) {
    var selected by rememberSaveable { mutableStateOf(Tab.Diagnoses) }
    // Hoisted so that switching tabs preserves the typed weight.
    val weight = rememberSaveable { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selected == Tab.Diagnoses,
                    onClick = { selected = Tab.Diagnoses },
                    icon = { Icon(Icons.Filled.MedicalServices, contentDescription = null) },
                    label = { Text(Tab.Diagnoses.title) },
                )
                NavigationBarItem(
                    selected = selected == Tab.Syrups,
                    onClick = { selected = Tab.Syrups },
                    icon = { Icon(Icons.Filled.LocalDrink, contentDescription = null) },
                    label = { Text(Tab.Syrups.title) },
                )
                NavigationBarItem(
                    selected = selected == Tab.Injections,
                    onClick = { selected = Tab.Injections },
                    icon = { Icon(Icons.Filled.Calculate, contentDescription = null) },
                    label = { Text(Tab.Injections.title) },
                )
                NavigationBarItem(
                    selected = selected == Tab.Ai,
                    onClick = { selected = Tab.Ai },
                    icon = { Icon(Icons.Filled.AutoAwesome, contentDescription = null) },
                    label = { Text(Tab.Ai.title) },
                )
                NavigationBarItem(
                    selected = selected == Tab.Settings,
                    onClick = { selected = Tab.Settings },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                    label = { Text(Tab.Settings.title) },
                )
            }
        },
    ) { inner ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            color = MaterialTheme.colorScheme.background,
        ) {
            when (selected) {
                Tab.Diagnoses -> DiagnosesScreen(weightState = weight)
                Tab.Syrups -> SyrupScreen(weightState = weight)
                Tab.Injections -> QuickReferenceScreen(weightState = weight)
                Tab.Ai -> AiChatScreen(
                    store = store,
                    weightProvider = { weight.value.toDoubleOrNull() ?: 0.0 },
                )
                Tab.Settings -> SettingsScreen(store)
            }
        }
    }
}
