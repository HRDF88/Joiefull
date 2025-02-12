package com.nedrysystems.joiefull.repositoryTest

import com.nedrysystems.joiefull.data.repository.GetProductApiRepositoryApi
import com.nedrysystems.joiefull.data.webservice.GetProductApiResponse
import com.nedrysystems.joiefull.data.webservice.GetProductApiService
import com.nedrysystems.joiefull.data.webservice.PictureApiResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetProductApiRepositoryApiTest {

    @Mock
    lateinit var mockGetProductApiService: GetProductApiService

    private lateinit var getProductApiRepositoryApi: GetProductApiRepositoryApi

    @Before
    fun setUp() {
        // Initialise le repository avec le mock du service
        getProductApiRepositoryApi = GetProductApiRepositoryApi(mockGetProductApiService)
    }

    @Test
    fun `getProduct should return list of products when API call is successful`() = runBlocking {
        // Arrange: Créer une réponse simulée de l'API
        val mockApiResponse = listOf(
            GetProductApiResponse(id = 1, name = "Product 1", price = 100.0, category = "haut", likes = 12, originalPrice = 120.00, picture = PictureApiResponse("jojo.com,=","une photo de product 1"),),
            GetProductApiResponse(id = 2, name = "Product 2", price = 150.0, category = "bas", likes = 126, originalPrice = 199.0, picture = PictureApiResponse("canard.com","une jolie photo de canard"))
        )

        // Simuler la réponse de l'API avec Mockito
        Mockito.`when`(mockGetProductApiService.getProduct()).thenReturn(mockApiResponse)

        // Act: Appeler la méthode du repository
        val result = getProductApiRepositoryApi.getProduct()

        // Assert: Vérifier que le résultat est bien celui attendu
        assertNotNull(result)
        assertEquals(mockApiResponse.size, result.size)
        assertEquals(mockApiResponse[0].id, result[0].id)
        assertEquals(mockApiResponse[1].name, result[1].name)
    }


}
