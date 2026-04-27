package com.rroot.pediatricdose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rroot.pediatricdose.ai.ChatMessage
import com.rroot.pediatricdose.ai.OpenRouterClient
import com.rroot.pediatricdose.ai.OpenRouterException
import com.rroot.pediatricdose.ai.SecureStore
import kotlinx.coroutines.launch

private const val SYSTEM_PROMPT = """
You are a paediatric clinical assistant. The user is a paediatrician working in
Iraq / the Middle East. Always:
- Reply in concise, ward-style English (or in Arabic if the user writes Arabic).
- Use weight-based mg/kg dosing. Quote the dose, route, frequency and a max
  per-dose cap when one exists.
- Mention reference (BNFc 2024 / Nelson's 22e / WHO IMCI / AHA PALS) at the
  end of each answer.
- If the question is not clinical, refuse politely and remind the user this
  assistant is for paediatric clinical decision support.
- Never invent doses you are not sure about. If unsure, say so and suggest
  consulting the BNFc.
"""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    store: SecureStore,
    weightProvider: () -> Double,
) {
    val client = remember {
        OpenRouterClient(
            tokenProvider = { store.openRouterToken },
            modelProvider = { store.modelId },
        )
    }
    val scope = rememberCoroutineScope()
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var input by rememberSaveable { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {

        Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("AI Assistant", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        text = "Model: ${store.modelId}",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                    )
                }
                IconButton(onClick = { messages.clear() }, enabled = messages.isNotEmpty()) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear")
                }
            }
        }

        if (messages.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Ask a paediatric clinical question",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "e.g. \"6 kg infant with severe dehydration — fluid plan and antibiotics?\"",
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(messages.size) { i ->
                    ChatBubble(messages[i])
                }
                if (sending) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                            Text("Thinking…", fontSize = 13.sp, color = Color(0xFF666666))
                        }
                    }
                }
            }
        }

        error?.let {
            Surface(color = Color(0xFFFFEBEE)) {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    color = Color(0xFFB71C1C),
                    fontSize = 12.sp,
                )
            }
        }

        Surface(shadowElevation = 4.dp, color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask about a child…") },
                    maxLines = 4,
                    shape = RoundedCornerShape(20.dp),
                )
                Spacer(Modifier.width(6.dp))
                FilledIconButton(
                    enabled = input.isNotBlank() && !sending,
                    onClick = {
                        val text = input.trim()
                        if (text.isEmpty()) return@FilledIconButton
                        val w = weightProvider()
                        val withWeight = if (w > 0.0) {
                            "Patient weight: %.1f kg.\n\n%s".format(w, text)
                        } else text
                        input = ""
                        error = null
                        messages.add(ChatMessage("user", withWeight))
                        sending = true
                        scope.launch {
                            try {
                                val payload = mutableListOf<ChatMessage>()
                                payload += ChatMessage("system", SYSTEM_PROMPT.trim())
                                payload += messages
                                val reply = client.chat(payload)
                                messages.add(ChatMessage("assistant", reply))
                            } catch (e: OpenRouterException) {
                                error = e.message
                            } catch (e: Throwable) {
                                error = e.message ?: "Network error"
                            } finally {
                                sending = false
                            }
                        }
                    },
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(msg: ChatMessage) {
    val isUser = msg.role == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
    ) {
        Surface(
            color = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp,
            ),
            modifier = Modifier.widthIn(max = 320.dp),
        ) {
            Text(
                text = msg.content,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontSize = 14.sp,
            )
        }
    }
}
