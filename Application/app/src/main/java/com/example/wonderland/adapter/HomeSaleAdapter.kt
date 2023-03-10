package com.example.wonderland.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wonderland.R
import com.example.wonderland.model.entity.Book
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat


class HomeSaleAdapter(private val arrBookOnSale: ArrayList<Book>): RecyclerView.Adapter<HomeSaleAdapter.ViewHolder>() {
    var onItemClick:((Book,Int) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var homeSaleImage: ImageView
        var homeSaleName: TextView
        var homeSaleRootPrice: TextView
        var homeSaleSalePrice: TextView

        init{
            homeSaleImage = itemView.findViewById(R.id.homeSaleImage)
            homeSaleName = itemView.findViewById(R.id.homeSaleBookName)
            homeSaleRootPrice = itemView.findViewById(R.id.homeSaleRootPrice)
            homeSaleSalePrice = itemView.findViewById(R.id.homeSaleSalePrice)

            itemView.setOnClickListener {
                onItemClick?.invoke(arrBookOnSale[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSaleAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_sale_list_items,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomeSaleAdapter.ViewHolder, position: Int) {
        var book = arrBookOnSale.get(position)

        Picasso.get().load(book.picture).into(holder.homeSaleImage);
        holder.homeSaleName.text = book.name
//        holder.homeSaleRootPrice.text = book.price.toString()
//        holder.homeSaleSalePrice.text = book.saleprice.toString()

        val formatter: NumberFormat = DecimalFormat("#,###")
        holder.homeSaleRootPrice.text = formatter.format(book.price)
        holder.homeSaleSalePrice.text = formatter.format(book.saleprice)

//        val formatter: NumberFormat = DecimalFormat("#,###")
//        homeSaleRootPrice.text= formatter.format(book.price)
//        homeSaleSalePrice.text= formatter.format(book.saleprice)

        holder.homeSaleRootPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun getItemCount(): Int {
        return arrBookOnSale.size
    }

}