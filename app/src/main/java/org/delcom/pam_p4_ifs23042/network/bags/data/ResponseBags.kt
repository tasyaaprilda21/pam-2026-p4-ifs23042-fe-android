package org.delcom.pam_p4_ifs23042.network.bags.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseBags(
    val bags: List<ResponseBagData>
)

@Serializable
data class ResponseBag(
    val bag: ResponseBagData
)

@Serializable
data class ResponseBagAdd(
    val bagId: String
)

@Serializable
data class ResponseBagData(
    val id: String,
    val nama: String,
    val merek: String,
    val harga: Double,
    val warna: String,
    val bahan: String,
    val stok: Int,
    val deskripsi: String,
    val createdAt: String,
    val updatedAt: String
)