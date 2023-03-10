package com.example.wonderland.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wonderland.R
import com.example.wonderland.model.entity.Comments

class CommentAdapter(private val arrComment: ArrayList<Comments>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var rating: TextView = itemView.findViewById(R.id.ratingPoint)
        var userEmail: TextView = itemView.findViewById(R.id.userEmail)
        var comment: TextView = itemView.findViewById(R.id.comment)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.activity_bookdetail_recyclerview_comments,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.comment.text= arrComment[position].review
        holder.rating.text="Rating: "+arrComment[position].rating.toString()
        holder.userEmail.text = arrComment[position].userEmail
    }

    override fun getItemCount(): Int {
        return arrComment.size
    }
}