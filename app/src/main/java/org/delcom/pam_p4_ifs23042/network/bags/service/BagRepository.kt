package org.delcom.pam_p4_ifs23042.network.bags.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23042.helper.SuspendHelper
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBag
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBagAdd
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBags
import org.delcom.pam_p4_ifs23042.network.data.ResponseMessage

class BagRepository(private val bagApiService: BagApiService) : IBagRepository {
    override suspend fun getAllBags(search: String?): ResponseMessage<ResponseBags?> {
        return SuspendHelper.safeApiCall {
            bagApiService.getAllBags(search)
        }
    }

    override suspend fun getBagById(bagId: String): ResponseMessage<ResponseBag?> {
        return SuspendHelper.safeApiCall {
            bagApiService.getBagById(bagId)
        }
    }

    override suspend fun postBag(
        nama: RequestBody,
        merek: RequestBody,
        harga: RequestBody,
        warna: RequestBody,
        bahan: RequestBody,
        stok: RequestBody,
        deskripsi: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseBagAdd?> {
        return SuspendHelper.safeApiCall {
            bagApiService.postBag(
                nama = nama,
                merek = merek,
                harga = harga,
                warna = warna,
                bahan = bahan,
                stok = stok,
                deskripsi = deskripsi,
                file = file
            )
        }
    }

    override suspend fun putBag(
        bagId: String,
        nama: RequestBody,
        merek: RequestBody,
        harga: RequestBody,
        warna: RequestBody,
        bahan: RequestBody,
        stok: RequestBody,
        deskripsi: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            bagApiService.putBag(
                bagId = bagId,
                nama = nama,
                merek = merek,
                harga = harga,
                warna = warna,
                bahan = bahan,
                stok = stok,
                deskripsi = deskripsi,
                file = file
            )
        }
    }

    override suspend fun deleteBag(bagId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            bagApiService.deleteBag(bagId)
        }
    }
}