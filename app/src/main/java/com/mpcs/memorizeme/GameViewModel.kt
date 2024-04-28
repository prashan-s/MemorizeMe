package com.mpcs.memorizeme
import kotlin.random.Random
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import kotlin.concurrent.schedule

class GameViewModel {
    private val colorSequence = mutableListOf<Int>()
    var userSequence = mutableListOf<Int>()
    private val colors = listOf(0, 1, 2, 3) // Assuming colors are represented by integers.

    fun generateNextColor() {
        colorSequence.add(colors[Random.nextInt(colors.size)])
    }

    fun getCurrentSequence(): List<Int> {
        return colorSequence
    }

    fun addUserColor(color: Int) {
        userSequence.add(color)
    }

    fun isUserSequenceCorrect(): Boolean {
        return userSequence == colorSequence
    }

    fun resetGame() {
        colorSequence.clear()
        userSequence.clear()
    }
}
