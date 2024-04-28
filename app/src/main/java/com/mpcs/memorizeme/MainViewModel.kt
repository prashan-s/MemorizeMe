package com.mpcs.memorizeme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val _score = MutableLiveData<Int>().apply { value = 0 }
    val score: LiveData<Int> = _score

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    private val _reset = MutableLiveData<Boolean>()
    val reset: LiveData<Boolean> = _reset

    private val _flash = MutableLiveData<Int>()
    val flash: LiveData<Int> = _flash

    private val _win = MutableLiveData<Boolean>()
    val win: LiveData<Boolean> = _win

    private val sequenceSize = MutableLiveData<Int>().apply { value = START_SEQUENCE_SIZE }
    private var canPlay = false

    private var randomValues = listOf<Int>()
    private val responseValues = mutableListOf<Int>()

    fun initialize(viewCount: Int) {
        randomize(viewCount)
        _count.value = sequenceSize.value ?: START_SEQUENCE_SIZE
    }

    private fun randomize(viewCount: Int) {
        randomValues = List(sequenceSize.value ?: START_SEQUENCE_SIZE) { Random.nextInt(0, viewCount) }
    }

    fun showSequence() {
        responseValues.clear()
        canPlay = false
        viewModelScope.launch {
            randomValues.forEach {
                _count.value = _count.value?.minus(1)
                // simulate colorViews[it].flash()
                _flash.value = it
                delay(COOLDOWN_INTERVAL)
            }
            canPlay = true
            _count.value = sequenceSize.value
        }
    }

    fun onClickColor(index: Int) {
        if (canPlay) {
            _count.value = _count.value?.minus(1)
            responseValues.add(index)
            checkSequence()
        }
    }

    private fun checkSequence() {
        when {
            responseValues == randomValues -> win()
            responseValues.size == sequenceSize.value -> fail()
            else -> canPlay = true
        }
    }

    private fun win() {
        _score.value = (_score.value ?: 0) + 1
        sequenceSize.value = (sequenceSize.value ?: START_SEQUENCE_SIZE) + 1
        randomize(sequenceSize.value ?: START_SEQUENCE_SIZE)
        _count.value = sequenceSize.value

        canPlay = false
    }

    private fun fail() {
        responseValues.clear()
        canPlay = false
        _reset.value = true
        _count.value = sequenceSize.value
        canPlay = true
    }

    companion object {
        private const val COOLDOWN_INTERVAL = 300L
        private const val START_SEQUENCE_SIZE = 2
    }
}

