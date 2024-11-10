package com.tmdb.popularmovies2024.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tmdb.popularmovies2024.R
import com.tmdb.popularmovies2024.data.dto.Pelicula
import com.tmdb.popularmovies2024.databinding.ItemListPeliculaBinding

class AdaptadorPeliculas : RecyclerView.Adapter<AdaptadorPeliculas.PeliculaViewHolder>() {

    // Usamos AsyncListDiffer para gestionar cambios de lista de manera eficiente
    private val differCallback = object : DiffUtil.ItemCallback<Pelicula>() {
        override fun areItemsTheSame(oldItem: Pelicula, newItem: Pelicula): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pelicula, newItem: Pelicula): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    // ViewHolder con ViewBinding para simplificar el acceso a las vistas
    inner class PeliculaViewHolder(private val binding: ItemListPeliculaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Configuramos los datos de la película y la acción del clic
        fun bind(pelicula: Pelicula, onItemClickListener: ((Pelicula) -> Unit)?) {
            // Cargar imagen con Glide (manejamos la posibilidad de error y la imagen por defecto)
            Glide.with(binding.root.context)
                .load(binding.root.context.getString(R.string.portada_url_base185) + pelicula.portada)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.portada)

            // Si el título es nulo o vacío, mostramos un texto por defecto
            binding.tituloPeliculaItem.text = pelicula.titulo ?: "Título no disponible"

            // Establecemos el listener de clic
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(pelicula)
            }
        }
    }

    // Enlazamos el clic a un item
    private var onItemClickListener: ((Pelicula) -> Unit)? = null
    fun setOnItemClickListener(listener: (Pelicula) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val binding =
            ItemListPeliculaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeliculaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val pelicula = differ.currentList[position]
        holder.bind(pelicula, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Método para actualizar la lista de películas
    fun submitMoviesList(newMoviesList: List<Pelicula>) {
        differ.submitList(newMoviesList)
    }
}