package com.mpcs.memorizeme.Activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mpcs.memorizeme.R

class ActivityGameOver: AppCompatActivity() {

    private lateinit var userName:String
    private lateinit var highScore:String
    private lateinit var score:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        userName = intent.extras?.get("NAME")?.toString() ?: "Default User"
        highScore = intent.extras?.get("HIGH_SCORE")?.toString() ?: "0"
        score = intent.extras?.get("SCORE")?.toString() ?: "0"

        val btnRetry = findViewById<Button>(R.id.btnRestart)
        val btnExit = findViewById<Button>(R.id.btnExit)
        val txtScore = findViewById<TextView>(R.id.txtScore)
        val txtHighScore = findViewById<TextView>(R.id.txtHighScore)

        txtScore.text = score
        txtHighScore.text = highScore


        btnExit.setOnClickListener {
            finish()
        }

        btnRetry.setOnClickListener {
            val i = Intent(this, ActivityLeaderboard::class.java)
            i.putExtra("NAME", userName)
            finish()
        }
    }
}