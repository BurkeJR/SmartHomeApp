package com.example.smarthomeapp

data class Media(
    val id: Int,
    val name: String,
    val isPlaying: Boolean,
    val currentTimeSeconds: Int,
    val nowPlayingSongId: Int,
)
