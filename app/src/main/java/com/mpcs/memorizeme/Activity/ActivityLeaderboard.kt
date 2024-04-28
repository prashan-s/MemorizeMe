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


class UserItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val nameTextView: TextView
    private val highScoreTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.leaderboard_cell, this, true)
        nameTextView = findViewById(R.id.nameTextView)
        highScoreTextView = findViewById(R.id.highScoreTextView)
    }

    fun bindUser(user: User) {
        nameTextView.text = user.name
        val hs = user.highScore
        highScoreTextView.text = "$hs"
        println("Test: " + user.highScore)
    }
}

class UserViewHolder(private val userItemView: UserItemView) : RecyclerView.ViewHolder(userItemView) {
    fun bind(user: User) {
        userItemView.bindUser(user)
    }
}

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = UserItemView(parent.context)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = userList.size



}
