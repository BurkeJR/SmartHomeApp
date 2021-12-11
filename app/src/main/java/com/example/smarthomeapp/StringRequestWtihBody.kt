package com.example.smarthomeapp

import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
class StringRequestWithBody(url: String, body: Any, onSuccess: (result: String) -> Unit, onError: (error: VolleyError) -> Unit)
    : StringRequest(Method.POST, url, onSuccess, onError) {
    private val stringBody: String

    init {
        val gson = Gson()
        stringBody = gson.toJson(body)
    }

    override fun getBody(): ByteArray {
        return stringBody.toByteArray()
    }
}
