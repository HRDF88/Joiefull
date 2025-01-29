package com.nedrysystems.joiefull.ui.home


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                            // Afficher l'en-tête de catégorie
                            item {
                                CategoryHeader(category = category)
                            }

                            // Afficher les produits en disposition horizontale
                            item {
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(products) { product ->
                                        Log.d(
                                            "HomeLayout",
                                            "Displaying Product: Name: ${product.name}, Price: ${product.price}, Category: ${product.category}, Original Price: ${product.originalPrice}, Rate: ${product.rate}, Favorite: ${product.favorite}"
                                        )
                                        ProductCard(
                                            product = product,
                                            imageLoader = imageLoader,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
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
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }

    @Composable
    fun ProductCard(
        product: ProductUiModel,
        imageLoader: ImageLoader,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .wrapContentHeight(), // Laisser la hauteur s'ajuster en fonction du contenu
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Image avec un Box pour centrer
                val painter = imageLoader.loadImagePainter(
                    url = product.picture.url,
                    placeholder = R.drawable.error,
                    error = R.drawable.error
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacement entre l'image et le texte

                // Nom et prix du produit
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = product.name,
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp
                        )
                    Text(
                        text = "${product.likes}",
                        textAlign = TextAlign.End,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)

                        )

                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacement entre les informations principales et les autres détails

                // Prix original et Note (si disponibles)
                Row(
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,

                        ) {
                        Text(
                            text = "${product.price} €",
                            textAlign = TextAlign.Start,
                            fontSize = 12.sp,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End) {
                        product.originalPrice?.let { originalPrice ->
                            if (originalPrice > product.price) {
                                Text(
                                    text = "$originalPrice €",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 62.dp)
                                        .align(Alignment.Bottom)

                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacement avant le cœur

                // Affichage du cœur si le produit est favori
                if (product.favorite) {
                    Text(
                        text = "❤️",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.End) // Alignement à droite
                    )
                }
            }
        }
    }
}






