package com.mpcs.memorizeme.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import com.mpcs.memorizeme.extensions.*
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.mpcs.memorizeme.Database.AppDatabase
import com.mpcs.memorizeme.R
import com.mpcs.memorizeme.Utility.SharedPreferencesHelper
import com.mpcs.memorizeme.ViewModel.ViewModelGame
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import android.content.Intent

class ActivityGame : AppCompatActivity() {
    private lateinit var viewModel: ViewModelGame

    lateinit var colorViews: List<View>
    lateinit var scoreTextView:TextView
    lateinit var countTextView:TextView
    lateinit var highScoreView:TextView
    lateinit var txtplayerName:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

//        val name = intent.extras?.getString("name") ?: "Default User"
        val name = intent.extras?.get("NAME")?.toString() ?: "Default User"
        println("User Name: " + name)
        val dao = AppDatabase.getDatabase(this).userDao()
        val sharedPref = SharedPreferencesHelper(this)
        viewModel = ViewModelGame(name,dao,sharedPref)

        val buttonShow = findViewById<Button>(R.id.button_show)
        val buttonBlue = findViewById<Button>(R.id.button_blue)
        val buttonGreen = findViewById<Button>(R.id.button_green)
        val buttonRed = findViewById<Button>(R.id.button_red)
        val buttonYellow = findViewById<Button>(R.id.button_yellow)

        scoreTextView = findViewById<TextView>(R.id.score_text_view)
        countTextView = findViewById<TextView>(R.id.count_text_view)
        highScoreView = findViewById<TextView>(R.id.highscore_text_view)
        txtplayerName = findViewById<TextView>(R.id.txtplayerName)

        txtplayerName.text = name
        viewModel.score.observe(this) { score -> scoreTextView.text = "Score: $score" }
        viewModel.count.observe(this) { count -> countTextView.text = "Count: $count" }
        viewModel.highScore.observe(this) { highScore -> countTextView.text = "User High Score: $highScore" }
        viewModel.lives.observe(this) { lives -> countTextView.text = "Lives: $lives" }

        viewModel.reset.observe(this) {
            lifecycleScope.launch {
                coroutineContext.cancelChildren()
                colorViews.map { launch { it.flash() } }.joinAll()
                colorViews.forEach { it.unhighlight() }
            }
        }

        viewModel.flash.observe(this){ flashIndex ->

            lifecycleScope.launch {
                    coroutineContext.cancelChildren()
                    println("flashIndex: " + flashIndex)
                    colorViews[flashIndex].flash()
            }
        }

        viewModel.win.observe(this){
            lifecycleScope.launch {
                coroutineContext.cancelChildren()
                scoreTextView.flash()
                countTextView.flash()
            }
        }

        viewModel.game_over.observe(this) {
            runOnUiThread {
            val i = Intent(this, ActivityGameOver::class.java)
            i.putExtra("NAME", viewModel.user_name)
            i.putExtra("HIGH_SCORE", viewModel.user_high_score)
            i.putExtra("SCORE", viewModel.score.value ?: 0)
            startActivity(i)
            }
        }

        buttonShow.setOnClickListener { viewModel.showSequence() }
        colorViews = listOf(buttonBlue, buttonGreen, buttonRed, buttonYellow)
        colorViews.forEachIndexed { index, view ->
            view.setOnClickListener { viewModel.onClickColor(index) }
        }

        viewModel.initialize(2)
    }
}
