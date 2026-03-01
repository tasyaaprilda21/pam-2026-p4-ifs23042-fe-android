package org.delcom.pam_p4_ifs23042.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBagData
import org.delcom.pam_p4_ifs23042.network.bags.service.IBagRepository
import javax.inject.Inject

// UI States
sealed class BagsUIState {
    object Loading : BagsUIState()
    data class Success(val data: List<ResponseBagData>) : BagsUIState()
    data class Error(val message: String) : BagsUIState()
}

sealed class BagDetailUIState {
    object Loading : BagDetailUIState()
    data class Success(val data: ResponseBagData) : BagDetailUIState()
    data class Error(val message: String) : BagDetailUIState()
}

sealed class BagActionUIState {
    object Idle : BagActionUIState()
    object Loading : BagActionUIState()
    data class Success(val message: String) : BagActionUIState()
    data class Error(val message: String) : BagActionUIState()
}

data class BagUIStateHolder(
    val bags: BagsUIState = BagsUIState.Loading,
    val bagDetail: BagDetailUIState = BagDetailUIState.Loading,
    val action: BagActionUIState = BagActionUIState.Idle
)

@HiltViewModel
class BagViewModel @Inject constructor(
    private val bagRepository: IBagRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BagUIStateHolder())
    val uiState: StateFlow<BagUIStateHolder> = _uiState

    // Ambil semua tas
    fun getAllBags(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(bags = BagsUIState.Loading) }
            val response = bagRepository.getAllBags(search)
            if (response.status == "success") {
                val bags = response.data?.bags ?: emptyList()
                _uiState.update { it.copy(bags = BagsUIState.Success(bags)) }
            } else {
                _uiState.update { it.copy(bags = BagsUIState.Error(response.message)) }
            }
        }
    }

    // Ambil detail tas
    fun getBagById(bagId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(bagDetail = BagDetailUIState.Loading) }
            val response = bagRepository.getBagById(bagId)
            if (response.status == "success" && response.data?.bag != null) {
                _uiState.update { it.copy(bagDetail = BagDetailUIState.Success(response.data.bag)) }
            } else {
                _uiState.update { it.copy(bagDetail = BagDetailUIState.Error(response.message)) }
            }
        }
    }

    // Tambah tas
    fun postBag(
        nama: String,
        merek: String,
        harga: String,
        warna: String,
        bahan: String,
        stok: String,
        deskripsi: String,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(action = BagActionUIState.Loading) }
            val response = bagRepository.postBag(
                nama = nama.toRequestBody(),
                merek = merek.toRequestBody(),
                harga = harga.toRequestBody(),
                warna = warna.toRequestBody(),
                bahan = bahan.toRequestBody(),
                stok = stok.toRequestBody(),
                deskripsi = deskripsi.toRequestBody(),
                file = file
            )
            if (response.status == "success") {
                _uiState.update { it.copy(action = BagActionUIState.Success(response.message)) }
            } else {
                _uiState.update { it.copy(action = BagActionUIState.Error(response.message)) }
            }
        }
    }

    // Update tas
    fun putBag(
        bagId: String,
        nama: String,
        merek: String,
        harga: String,
        warna: String,
        bahan: String,
        stok: String,
        deskripsi: String,
        file: MultipartBody.Part? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(action = BagActionUIState.Loading) }
            val response = bagRepository.putBag(
                bagId = bagId,
                nama = nama.toRequestBody(),
                merek = merek.toRequestBody(),
                harga = harga.toRequestBody(),
                warna = warna.toRequestBody(),
                bahan = bahan.toRequestBody(),
                stok = stok.toRequestBody(),
                deskripsi = deskripsi.toRequestBody(),
                file = file
            )
            if (response.status == "success") {
                _uiState.update { it.copy(action = BagActionUIState.Success(response.message)) }
            } else {
                _uiState.update { it.copy(action = BagActionUIState.Error(response.message)) }
            }
        }
    }

    // Hapus tas
    fun deleteBag(bagId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(action = BagActionUIState.Loading) }
            val response = bagRepository.deleteBag(bagId)
            if (response.status == "success") {
                _uiState.update { it.copy(action = BagActionUIState.Success(response.message)) }
            } else {
                _uiState.update { it.copy(action = BagActionUIState.Error(response.message)) }
            }
        }
    }

    // Reset action state
    fun resetAction() {
        _uiState.update { it.copy(action = BagActionUIState.Idle) }
    }
}