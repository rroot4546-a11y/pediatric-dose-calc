package com.rroot.pediatricdose.ai

/**
 * Curated list of OpenRouter models that suit clinical Q&A.
 * The user can also paste a custom model id manually in Settings.
 */
data class AiModel(val id: String, val display: String, val tag: String)

object AiModels {
    val curated: List<AiModel> = listOf(
        AiModel("google/gemini-2.0-flash-001", "Gemini 2.0 Flash", "fast / cheap"),
        AiModel("google/gemini-2.5-pro", "Gemini 2.5 Pro", "high quality"),
        AiModel("anthropic/claude-3.5-sonnet", "Claude 3.5 Sonnet", "best clinical reasoning"),
        AiModel("anthropic/claude-3-haiku", "Claude 3 Haiku", "fast"),
        AiModel("openai/gpt-4o-mini", "GPT-4o mini", "cheap"),
        AiModel("openai/gpt-4o", "GPT-4o", "high quality"),
        AiModel("meta-llama/llama-3.3-70b-instruct", "Llama 3.3 70B", "open-weight"),
        AiModel("deepseek/deepseek-chat", "DeepSeek Chat", "open-weight, cheap"),
        AiModel("qwen/qwen-2.5-72b-instruct", "Qwen 2.5 72B", "open-weight"),
    )
}
