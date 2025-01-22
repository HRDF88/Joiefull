package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import javax.inject.Inject

class GetProductLocalRepository @Inject constructor(
    private val productDao: ProductDao
) : GetProductLocalRepositoryInterface {

    override suspend fun getLocalProductInfo(): List<ProductLocalInfo> {
        return productDao.getAllProductsLocalInfo()
            .map { ProductLocalInfo(0).fromDto(it) } // Instanciation d'un objet ProductLocalInfo avant d'appeler fromDto()
    }

    override suspend fun getProductLocalInfo(id: Int): ProductLocalInfo? {
        // Récupère le ProductEntity à partir de la base de données
        val productEntity = productDao.getProductLocalInfo(id)

        // Vérifie si productEntity est non nul et le convertit en ProductLocalInfo
        return productEntity?.let {
            // Appelle fromDto() sur ProductEntity pour le convertir en ProductLocalInfo
            ProductLocalInfo(id = it.id, favorite = it.favorite, rate = it.rate)
        }
    }

    // Ajoute ou met à jour les informations locales d'un produit
    override suspend fun insertOrUpdateProductLocalInfo(productLocalInfo: ProductLocalInfo) {
        // Convertir ProductLocalInfo en ProductEntity en utilisant la méthode toDto()
        val productEntity = productLocalInfo.toDto()

        // Appel au DAO pour insérer ou mettre à jour le produit dans la base de données
        productDao.insertOrUpdateProductLocalInfo(productEntity)
    }

    // Met à jour le statut "favori" d'un produit
    override suspend fun updateFavoriteStatus(id: Int, favorite: Boolean) {
        productDao.updateFavoriteStatus(id, favorite)
    }
}


