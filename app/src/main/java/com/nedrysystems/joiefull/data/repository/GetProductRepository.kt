package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryApiInterface
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryInterface
import com.nedrysystems.joiefull.domain.model.PictureApiResponse
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import com.nedrysystems.joiefull.domain.model.ProductUiModel
import javax.inject.Inject

class GetProductRepository @Inject constructor(
    private val getProductApiServiceInterface: GetProductRepositoryApiInterface, // pour l'API
    private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface
) : GetProductRepositoryInterface {

    override suspend fun getProducts(): List<ProductUiModel> {
        val productUiModels = mutableListOf<ProductUiModel>()

        try {
            // Appel à l'API pour récupérer les produits
            val apiProducts = getProductApiServiceInterface.getProduct()

            // Récupérer tous les produits locaux sous forme de ProductLocalInfo
            val localProducts = getProductLocalRepositoryInterface.getLocalProductInfo()

            // Pour chaque produit dans la base de données, convertis-le en ProductLocalInfo
            val localProductInfos =
                localProducts.map { ProductLocalInfo(it.id, it.favorite, it.rate) }

            // Optimisation : obtenir uniquement les IDs des produits locaux en une seule requête
            val localProductsMap = localProductInfos.associateBy { it.id }

            for (apiProduct in apiProducts) {
                // Vérifier si l'ID existe dans la base locale
                val localProduct = localProductsMap[apiProduct.id]

                // Si le produit n'existe pas dans la base locale, on l'ajoute avec des valeurs par défaut
                if (localProduct == null) {
                    val newLocalProduct =
                        ProductLocalInfo(id = apiProduct.id, favorite = false, rate = null)
                    // Ajouter directement ProductLocalInfo sans transformation
                    getProductLocalRepositoryInterface.insertOrUpdateProductLocalInfo(
                        newLocalProduct
                    )
                }

                // Fusionner les données de l'API avec les données locales
                val productUiModel = ProductUiModel(
                    id = apiProduct.id,
                    picture = apiProduct.picture,
                    name = apiProduct.name,
                    category = apiProduct.category,
                    likes = apiProduct.likes,
                    price = apiProduct.price,
                    original_Price = apiProduct.original_Price,
                    favorite = localProduct?.favorite
                        ?: false, // Utiliser la valeur de la base locale (par défaut false)
                    rate = localProduct?.rate // Utiliser la note de la base locale (peut être null)
                )

                productUiModels.add(productUiModel)
            }
        } catch (exception: Exception) {
            // Gérer l'erreur si l'API échoue
            println("Erreur lors de la récupération des produits : ${exception.message}")

            // Retourner uniquement les produits locaux si l'API échoue
            val localProducts = getProductLocalRepositoryInterface.getLocalProductInfo()

            for (localProduct in localProducts) {
                // Créer des valeurs par défaut lorsque l'API échoue
                val productUiModel = ProductUiModel(
                    id = localProduct.id,
                    picture = PictureApiResponse("", ""), // Image manquante si l'API échoue
                    name = "", // Nom manquant si l'API échoue
                    category = "", // Catégorie manquante si l'API échoue
                    likes = 0, // Likes manquants si l'API échoue
                    price = 0.0, // Prix manquant si l'API échoue
                    original_Price = 0.0, // Prix original manquant si l'API échoue
                    favorite = localProduct.favorite,
                    rate = localProduct.rate
                )

                productUiModels.add(productUiModel)
            }
        }

        return productUiModels
    }
}
