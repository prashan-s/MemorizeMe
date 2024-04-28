package com.mpcs.memorizeme

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import android.animation.ArgbEvaluator
import android.view.View
import com.mpcs.memorizeme.extensions.*
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import kotlin.coroutines.coroutineContext
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    lateinit var colorViews: List<View>
    lateinit var scoreTextView:TextView
    lateinit var countTextView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel = MainViewModel()

        val buttonShow = findViewById<Button>(R.id.button_show)
        val buttonBlue = findViewById<Button>(R.id.button_blue)
        val buttonRed = findViewById<Button>(R.id.button_red)
        val buttonGreen = findViewById<Button>(R.id.button_green)
        val buttonYellow = findViewById<Button>(R.id.button_yellow)

        scoreTextView = findViewById<TextView>(R.id.score_text_view)
        countTextView = findViewById<TextView>(R.id.count_text_view)


        viewModel.score.observe(this) { score -> scoreTextView.text = "Score: $score" }
        viewModel.count.observe(this) { count -> countTextView.text = "Count: $count" }
        viewModel.reset.observe(this) {
            lifecycleScope.launch {
                coroutineContext.cancelChildren()
                colorViews.map { launch { it.flash() } }.joinAll()
                colorViews.forEach { it.unhighlight() }
            }
        }

        viewModel.flash.observe(this){flashIndex ->

            lifecycleScope.launch {
                    coroutineContext.cancelChildren()
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


        buttonShow.setOnClickListener { viewModel.showSequence() }
        colorViews = listOf(buttonBlue, buttonRed, buttonGreen, buttonYellow)
        colorViews.forEachIndexed { index, view ->
            view.setOnClickListener { viewModel.onClickColor(index) }
        }

        viewModel.initialize(colorViews.size)
    }
}
