package com.mpcs.memorizeme.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.mpcs.memorizeme.R

class ActivityWelcome:  AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val txtName = findViewById<TextView>(R.id.editTextName)
        val btnStart = findViewById<Button>(R.id.button_next)
        val btnhighscore = findViewById<TextView>(R.id.button_score)

        btnStart.setOnClickListener {

            if (txtName.text.length == 0 || txtName.text == "" ) {
                Toast.makeText(this,"Username cannot be empty",LENGTH_LONG).show()
            }else {
                val i = Intent(this, ActivityGame::class.java)
                i.putExtra("NAME", txtName.text)
                startActivity(i)
            }
        }

        btnhighscore.setOnClickListener {

            val i = Intent(this, ActivityLeaderboard::class.java)
            i.putExtra("name", txtName.text)
            startActivity(i)
        }

    }
}