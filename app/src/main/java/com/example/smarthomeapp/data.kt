package com.example.smarthomeapp

data class door(
    val id: Int,
    val name: String,
    val isOpen: Boolean,
    val motorized: Boolean
)

data class light(
    val id: Int,
    val name: String,
    val isOn: Boolean
)

data class mediaPlayer(
    val id: Int,
    val name: String,
    val type: String = "media-player",
    var isPlaying: Boolean,
    var nowPlayingSongId: Int = 0,
    val currentTimeSeconds: Double = 0.0
)

data class song(
    val id: Int,
    val name: String,
    val coverUrl: String,
    val durationSeconds: Double
)