package com.example.productshopmobile.data.repository

import com.example.productshopmobile.data.entity.ProductEntity
import com.example.productshopmobile.data.local.ProductDAO
import com.example.productshopmobile.data.remote.dto.ProductDTO
import com.example.productshopmobile.domain.model.Product
import com.example.productshopmobile.network.ShopAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.UUID
import javax.inject.Inject

// Łączy dane z lokalnej bazy danych (przez DAO) oraz dane zdalne z API (ShopAPI)
// @Inject - wstrzyknięcie zależności przez Hilt
class ProductRepository @Inject constructor(
    private val dao: ProductDAO,
    private val api: ShopAPI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getProductsFlow(): Flow<List<Product>> = dao.getAll() // pobranie z lokalnej bazy
        .map { entities -> entities.map { it.toDomain() } } // konwertuje ProductEntity na Product (czyli model domenowy)
        .onStart { //wywoływanie przed emisją Flow
            emitAll(refreshAndGetLocal()) // odświeżenie danych z sieci i emisja zaktualizowanej listy z, zapewnia że te dane trafiają do tego samego strumienia.
        }

    fun getProductDetailFlow(id: UUID): Flow<Product> =
        dao.getById(id)
            .mapNotNull { it?.toDomain() } // pomija wartości null gdyby produkt nie istniał
            .onStart {
                val dto = api.getProductDetail(id)
                val entity = ProductEntity.fromDto(dto)
                dao.insertAll(listOf(entity))
                emit(entity.toDomain()) // ręcznie emituje produkt (nie trzeba czekać na bazę)
            }
            .flowOn(ioDispatcher) // zapewnia, że operacja będzie wykonana na dispatcherze I/O

    private fun refreshAndGetLocal(): Flow<List<Product>> = flow {
        val remoteDtos: List<ProductDTO> = api.getProducts()
        val entities: List<ProductEntity> = remoteDtos.map { ProductEntity.fromDto(it) }
        dao.clearAll() // czyści starą listę produktów w lokalnej bazie
        dao.insertAll(entities) // zapisuje nową listę produktów
        emit(dao.getAll().first().map { it.toDomain() }) // ręczna emisja wartości w Flow
    }.flowOn(ioDispatcher) // zapewnia, że operacja będzie wykonana na dispatcherze I/O
}