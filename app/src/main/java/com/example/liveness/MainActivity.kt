package com.example.liveness

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.liveness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        lifecycle.currentState
        setContentView(binding.root)

        val livenessLauncher = registerForActivityResult(LivenessActivity.ResultContract()) {
            binding.recyclerView.adapter = ImageAdapter(it.orEmpty())
            binding.edtName.text?.clear()
        }

        binding.startBtn.setOnClickListener {
            if (binding.edtName.text.toString().trim().isNotEmpty()) {
                LivenessActivity.name = binding.edtName.text.toString().trim()
                livenessLauncher.launch(null)
            } else {
                Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class ImageAdapter(private val images: List<String>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return object : ViewHolder(imageView) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        Glide.with(holder.itemView).load(image).into(holder.itemView as ImageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}