package org.delcom.pam_p4_ifs23042.network.bags.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBag
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBagAdd
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBags
import org.delcom.pam_p4_ifs23042.network.data.ResponseMessage
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface BagApiService {
    // Ambil semua data tas
    @GET("bags")
    suspend fun getAllBags(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponseBags?>

    // Ambil data tas berdasarkan ID
    @GET("bags/{bagId}")
    suspend fun getBagById(
        @Path("bagId") bagId: String
    ): ResponseMessage<ResponseBag?>

    // Tambah data tas
    @Multipart
    @POST("/bags")
    suspend fun postBag(
        @Part("nama") nama: RequestBody,
        @Part("merek") merek: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("warna") warna: RequestBody,
        @Part("bahan") bahan: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseMessage<ResponseBagAdd?>

    // Ubah data tas
    @Multipart
    @PUT("/bags/{bagId}")
    suspend fun putBag(
        @Path("bagId") bagId: String,
        @Part("nama") nama: RequestBody,
        @Part("merek") merek: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("warna") warna: RequestBody,
        @Part("bahan") bahan: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data tas
    @DELETE("bags/{bagId}")
    suspend fun deleteBag(
        @Path("bagId") bagId: String
    ): ResponseMessage<String?>
}