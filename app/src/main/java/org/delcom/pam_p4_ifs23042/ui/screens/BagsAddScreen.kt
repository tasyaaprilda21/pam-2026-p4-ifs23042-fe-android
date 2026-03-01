package org.delcom.pam_p4_ifs23042.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.delcom.pam_p4_ifs23042.helper.ToolsHelper
import org.delcom.pam_p4_ifs23042.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23042.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagActionUIState
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagViewModel
import java.io.File

@Composable
fun BagsAddScreen(
    navController: NavHostController,
    bagViewModel: BagViewModel
) {
    val context = LocalContext.current
    val uiState by bagViewModel.uiState.collectAsState()

    var nama by remember { mutableStateOf("") }
    var merek by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var warna by remember { mutableStateOf("") }
    var bahan by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri = uri }

    LaunchedEffect(uiState.action) {
        when (val action = uiState.action) {
            is BagActionUIState.Success -> {
                bagViewModel.resetAction()
                navController.popBackStack()
            }
            is BagActionUIState.Error -> {
                errorMessage = action.message
                isLoading = false
                bagViewModel.resetAction()
            }
            is BagActionUIState.Loading -> isLoading = true
            else -> {}
        }
    }

    if (isLoading) {
        LoadingUI()
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBarComponent(
            navController = navController,
            title = "Tambah Tas",
            showBackButton = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (errorMessage.isNotEmpty()) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Tas") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = merek, onValueChange = { merek = it }, label = { Text("Merek") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = harga, onValueChange = { harga = it },
                label = { Text("Harga (Rp)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(value = warna, onValueChange = { warna = it }, label = { Text("Warna") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = bahan, onValueChange = { bahan = it }, label = { Text("Bahan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = stok, onValueChange = { stok = it },
                label = { Text("Stok") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = deskripsi, onValueChange = { deskripsi = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Pilih Gambar
            OutlinedButton(onClick = { imagePickerLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                Text(if (selectedImageUri != null) "Gambar dipilih ✓" else "Pilih Gambar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nama.isBlank() || merek.isBlank() || harga.isBlank() || warna.isBlank() || bahan.isBlank() || stok.isBlank() || deskripsi.isBlank()) {
                        errorMessage = "Semua field harus diisi!"
                        return@Button
                    }
                    if (selectedImageUri == null) {
                        errorMessage = "Gambar harus dipilih!"
                        return@Button
                    }

                    val file = ToolsHelper.uriToFile(context, selectedImageUri!!)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                    bagViewModel.postBag(
                        nama = nama,
                        merek = merek,
                        harga = harga,
                        warna = warna,
                        bahan = bahan,
                        stok = stok,
                        deskripsi = deskripsi,
                        file = imagePart
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Tas")
            }
        }
    }
}