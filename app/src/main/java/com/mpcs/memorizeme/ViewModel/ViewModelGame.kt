package com.mpcs.memorizeme.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.mpcs.memorizeme.Database.AppDatabase
import com.mpcs.memorizeme.Database.User
import com.mpcs.memorizeme.Interface.UserDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

import com.mpcs.memorizeme.Utility.SharedPreferencesHelper

class ViewModelGame(val name: String,
                    private val userDao: UserDao,
                    private val sharedPreferencesHelper: SharedPreferencesHelper) : ViewModel() {
    private var currentUser: User? = null

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


    init {
        this.initializeUser(name)
    }

    fun initialize(sequenceLength: Int) {
        randomize(sequenceLength)
        _count.value = sequenceLength
    }

    private fun randomize(sequenceLength: Int) {
        randomValues = List(sequenceLength) { Random.nextInt(0, 4) }
        println("Randomized Seq:" + randomValues);
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




    fun initializeUser(name: String) {

        viewModelScope.launch {
            val user = userDao.getUserByName(name)
            if (user == null) {
                val userId = userDao.insertUser(User(name = name, highScore = 0))
                currentUser = User(id = userId, name = name, highScore = 0)
            } else {
                currentUser = user
            }
        }
    }

    fun updateScore(newScore: Int) {
        currentUser?.let { user ->
            if (newScore > user.highScore) {
                user.highScore = newScore
                viewModelScope.launch {
                    userDao.updateUser(user)
                    checkAndUpdateMaxHighScore(newScore)
                }
            }
        }
    }

    private fun checkAndUpdateMaxHighScore(newScore: Int) {
        val maxHighScore = sharedPreferencesHelper.highScore
        if (newScore > maxHighScore) {
            sharedPreferencesHelper.highScore = newScore
        }
    }

}

