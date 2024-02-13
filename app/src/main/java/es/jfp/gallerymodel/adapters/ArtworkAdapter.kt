package es.jfp.gallerymodel.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.classes.APIArtwork
import es.jfp.gallerymodel.databinding.RecyclerArtItemBinding

class ArtworkAdapter(
    private val context: Context,
    private val artworks: MutableList<APIArtwork>,
    private val miListener: (APIArtwork) -> Unit
): RecyclerView.Adapter<ArtworkAdapter.ArtViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val binding = RecyclerArtItemBinding.inflate(LayoutInflater.from(context), parent, false)

        /*val color = getRandomColor()
        val card: CardView = binding.recyclerArtCard
        card.setCardBackgroundColor(ContextCompat.getColor(context, color))*/

        return ArtViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return artworks.size
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val item = artworks[position]
        val color = getRandomColor()
        holder.bindItem(item, context)
        val card: CardView = holder.itemView as CardView
        card.setCardBackgroundColor(ContextCompat.getColor(context, color))
        holder.itemView.setOnClickListener { miListener(item) }
    }

    private fun getRandomColor(): Int = when((Math.random()*5+1).toInt()) {
        1 -> R.color.card_background_1
        2 -> R.color.card_background_2
        3 -> R.color.card_background_3
        4 -> R.color.card_background_4
        5 -> R.color.card_background_5
        else -> R.color.white
    }

    class ArtViewHolder(binding: RecyclerArtItemBinding): RecyclerView.ViewHolder(binding.root) {

        private val artImage: ImageView = binding.artImageview
        private val artName: TextView = binding.artTitleTextview
        private val artAuthor: TextView = binding.artAuthorTextview

        fun bindItem(art: APIArtwork, context: Context) {

            artName.text = art.title
            artAuthor.text = art.author
            Picasso.get().load(art.image).into(artImage)

        }

    }

}