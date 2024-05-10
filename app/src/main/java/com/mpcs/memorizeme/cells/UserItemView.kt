package com.mpcs.memorizeme.cells

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mpcs.memorizeme.Database.User
import com.mpcs.memorizeme.R


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
