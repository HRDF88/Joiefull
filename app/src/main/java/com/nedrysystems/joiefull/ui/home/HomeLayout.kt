package com.nedrysystems.joiefull.ui.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.domain.model.ProductUiModel
import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import javax.inject.Inject


class HomeLayout @Inject constructor(private val imageLoader: ImageLoader) {

    @Composable
    fun Render(viewModel: HomeViewModel) {
        // Observer les données du ViewModel
        val products = viewModel.products.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        val errorMessage = viewModel.errorMessage.collectAsState()

        // Affichage du contenu
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                isLoading.value -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                errorMessage.value != null -> {
                    Text(
                        text = "Erreur : ${errorMessage.value}",
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                else -> {
                    LazyColumn {
                        val groupedProducts = products.value.groupBy { it.category }
                        groupedProducts.forEach { (category, items) ->
                            item {
                                CategoryHeader(category)
                            }
                            items(items) { product ->
                                ProductCard(product, imageLoader)
                            }
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
            // Utiliser GlideUtils pour charger une image statique ou fictive
            val painter = imageLoader.loadImagePainter(
                url = product.picture.url,
                placeholder = R.drawable.error,
                error = R.drawable.error
            )

            // Afficher l'image
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(320.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Détails du produit
            Row(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.h1)
                Text(text = "${product.price} €", style = MaterialTheme.typography.h1)

                product.original_Price?.let { originalPrice ->
                    if (originalPrice > product.price) {
                        Text(
                            text = "Prix original : $originalPrice €",
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }

                product.rate?.let {
                    Text(
                        text = "Note : ${it}/5",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            if (product.favorite) {
                Text(
                    text = "❤️",

                )
            }
        }

    }
}





