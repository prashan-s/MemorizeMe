package com.mpcs.memorizeme
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import kotlin.concurrent.schedule

class GameViewModel : ViewModel() {
    private val _sequence = MutableLiveData<List<Int>>()
    val sequence: LiveData<List<Int>> = _sequence

    private val _gameOver = MutableLiveData<Boolean>()
    val gameOver: LiveData<Boolean> = _gameOver

    private var currentSequence = mutableListOf<Int>()
    private var playerIndex = 0

    init {
        generateSequence()
    }

    fun generateSequence() {
        currentSequence.add((0..3).random())
        _sequence.value = currentSequence.toList()
        playerIndex = 0
    }

    fun verifySequence(index: Int) {
        if (currentSequence[playerIndex] == index) {
            playerIndex++
            if (playerIndex == currentSequence.size) {
                Timer("Schedule", false).schedule(1000) {
                    generateSequence()
                }
            }
        } else {
            _gameOver.value = true
        }
    }
}
