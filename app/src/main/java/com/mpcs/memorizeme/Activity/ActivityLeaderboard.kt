package com.mpcs.memorizeme.Activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mpcs.memorizeme.Database.AppDatabase
import com.mpcs.memorizeme.Database.User
import com.mpcs.memorizeme.R
import com.mpcs.memorizeme.cells.UserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityLeaderboard:  AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var users: List<User>? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val v = this
        scope.launch {
            val dao = AppDatabase.getDatabase(v).userDao()
            users = dao.getAllUsers()?.toList()

            runOnUiThread {
            if (users != null) {
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(v)
                recyclerView.adapter = UserAdapter(users!!)
            }
            }

        }



    }
}

