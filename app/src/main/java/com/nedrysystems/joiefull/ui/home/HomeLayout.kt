package com.nedrysystems.joiefull.ui.home


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import javax.inject.Inject


class HomeLayout @Inject constructor(private val imageLoader: ImageLoader) {

    @Composable
    fun Render(viewModel: HomeViewModel) {
        val uiState by viewModel.uiState.collectAsState()

        // Obtenir le context dans Composable
        val context = LocalContext.current

        // Affichage du Toast si une erreur existe
        LaunchedEffect(uiState.error) {
            if (uiState.error.isNotEmpty()) {
                Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            }
        }
        // Affichage du contenu
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                else -> {
                    // Grouper les produits par catégorie
                    val productsGroupedByCategory = uiState.products.groupBy { it.category }

                    // Affichage des produits par catégorie
                    LazyColumn {
                        productsGroupedByCategory.forEach { (category, products) ->
                            // Afficher l'entête de catégorie
                            item {
                                CategoryHeader(category = category)
                            }

                            // Afficher les produits de la catégorie
                            items(products) { product ->
                                ProductCard(product = product, imageLoader = imageLoader)
                            }
                        }
                    }
                }
            }
        }
    }

    // Définir CategoryHeader et ProductCard hors de la classe
    @Composable
    fun CategoryHeader(category: String) {
        Text(
            text = category,
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }

    @Composable
    fun ProductCard(product: ProductUiModel, imageLoader: ImageLoader) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Utilisation d'un painter pour charger l'image
                val painter = imageLoader.loadImagePainter(
                    url = product.picture.url,
                    placeholder = R.drawable.error,
                    error = R.drawable.error
                )

                // Affichage de l'image
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Limite la hauteur de l'image
                        .clip(RoundedCornerShape(8.dp)) // Bords arrondis
                )
                Spacer(modifier = Modifier.height(8.dp)) // Espacement entre l'image et les détails

                // Détails du produit
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(text = product.name, style = MaterialTheme.typography.h6)
                    Text(text = "${product.price} €", style = MaterialTheme.typography.body1)

                    product.original_Price?.let { originalPrice ->
                        if (originalPrice > product.price) {
                            Text(
                                text = "Prix original : $originalPrice €",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }

                    product.rate?.let {
                        Text(
                            text = "Note : ${it}/5",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }

                // Affichage du cœur si le produit est favori
                if (product.favorite) {
                    Text(
                        text = "❤️",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}





