package com.mpcs.memorizeme.Utility
import android.content.Context
import com.mpcs.memorizeme.ViewModel.ViewModelGame

class SharedPreferencesHelper(context: Context) {
    private val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)

    var highScore: Int
        get() = prefs.getInt("highScore", 0)
        set(value) = prefs.edit().putInt("highScore", value).apply()
}