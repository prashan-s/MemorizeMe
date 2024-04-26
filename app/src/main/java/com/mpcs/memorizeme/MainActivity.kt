package com.mpcs.memorizeme

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import android.animation.ArgbEvaluator

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: GameViewModel
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.button1);
        val button2 = findViewById<Button>(R.id.button1);
        val button3 = findViewById<Button>(R.id.button1);
        val button4 = findViewById<Button>(R.id.button1);
        buttons = listOf(button1,button2,button3,button4)

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Observe the game state
        viewModel.sequence.observe(this) { sequence ->
            showSequence(sequence)
        }
        viewModel.gameOver.observe(this) { isOver ->
            if (isOver) finish() // or show game over dialog
        }


        // Button listeners
        button1.setOnClickListener { viewModel.verifySequence(0) }
        button2.setOnClickListener { viewModel.verifySequence(1) }
        button3.setOnClickListener { viewModel.verifySequence(2) }
        button4.setOnClickListener { viewModel.verifySequence(3) }

        viewModel.generateSequence()
    }

    private fun showSequence(sequence: List<Int>) {
        val delay = 600L // Delay in milliseconds between flickers
        sequence.forEachIndexed { index, buttonId ->
            buttons[buttonId].postDelayed({
                flickerButton(buttons[buttonId], 300) // Flicker each button for 300 ms
            }, index * delay)
        }
    }

    private fun flickerButton(button: Button, duration: Long) {
        val colorFrom = ContextCompat.getColor(this, android.R.color.holo_blue_bright)
        val colorTo = ContextCompat.getColor(this, android.R.color.holo_red_light)
        val colorAnimation = ObjectAnimator.ofObject(
            button,
            "backgroundColor",
            ArgbEvaluator(),
            colorFrom,
            colorTo,
            colorFrom
        )
        colorAnimation.duration = duration
        colorAnimation.repeatMode = ObjectAnimator.REVERSE
        colorAnimation.repeatCount = 1  // Adjust this depending on how many times you want it to flicker
        colorAnimation.start()
    }

}