package com.example.smarthomeapp

data class door(
    val id: Integer,
    val name: String,
    val isOpen: Boolean,
    val motorized: Boolean
)

data class light(
    val id: Integer,
    val name: String,
    val isOn: Boolean
)

data class mediaPlayer(
    val id: Integer,
    val name: String,
    val isPlaying: Boolean,
    val nowPlayingSongId: Integer,
    val currentTimeSeconds: Float
)

data class song(
    val id: Integer,
    val name: String,
    val coverUrl: String,
    val durationSeconds: Float
)