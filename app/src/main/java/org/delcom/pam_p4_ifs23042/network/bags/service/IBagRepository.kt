package org.delcom.pam_p4_ifs23042.network.bags.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBag
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBagAdd
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBags
import org.delcom.pam_p4_ifs23042.network.data.ResponseMessage

interface IBagRepository {
    suspend fun getAllBags(search: String?): ResponseMessage<ResponseBags?>
    suspend fun getBagById(bagId: String): ResponseMessage<ResponseBag?>
    suspend fun postBag(
        nama: RequestBody,
        merek: RequestBody,
        harga: RequestBody,
        warna: RequestBody,
        bahan: RequestBody,
        stok: RequestBody,
        deskripsi: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseBagAdd?>
    suspend fun putBag(
        bagId: String,
        nama: RequestBody,
        merek: RequestBody,
        harga: RequestBody,
        warna: RequestBody,
        bahan: RequestBody,
        stok: RequestBody,
        deskripsi: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>
    suspend fun deleteBag(bagId: String): ResponseMessage<String?>
}