package com.trust.ease_noting.classes
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class WordApi {

    private val client = OkHttpClient()

    // Function to get the definition of a word
    fun getWordDefinition(word: String): String {
        val url = "https://api.dictionaryapi.dev/api/v2/entries/en/$word"

        val request = Request.Builder()
            .url(url)
            .build()

        // Execute the network call and handle the response
        client.newCall(request).execute().use { response ->
            return if (response.isSuccessful) {
                extractDefinition(response)
            } else {
                "Error: ${response.code}"
            }
        }
    }

    // Function to extract the definition from the API response
    private fun extractDefinition(response: okhttp3.Response): String {
        val responseBody = response.body?.string()
        val jsonArray = JSONArray(responseBody)

        // Check if the response array is not empty
        if (jsonArray.length() > 0) {
            val meaningsArray = jsonArray.getJSONObject(0).getJSONArray("meanings")

            val definitions = StringBuilder()
            // Iterate through different parts of speech
            for (i in 0 until meaningsArray.length()) {
                val partOfSpeech = meaningsArray.getJSONObject(i).getString("partOfSpeech")
                val partDefinitions = meaningsArray.getJSONObject(i).getJSONArray("definitions")

                definitions.append("$partOfSpeech:\n")
                // Iterate through definitions for each part of speech
                for (j in 0 until partDefinitions.length()) {
                    val definition = partDefinitions.getJSONObject(j).getString("definition")
                    val example = if (partDefinitions.getJSONObject(j).has("example")) {
                        partDefinitions.getJSONObject(j).getString("example")
                    } else {
                        "No example available"
                    }

                    // Append the definition and example to the result
                    definitions.append("- $definition\nExample: $example\n\n")
                }
            }
            return definitions.toString()
        }
        return "No definition found"
    }
}
