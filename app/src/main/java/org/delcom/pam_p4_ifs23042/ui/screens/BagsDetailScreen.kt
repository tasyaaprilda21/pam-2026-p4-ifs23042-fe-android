package org.delcom.pam_p4_ifs23042.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23042.BuildConfig
import org.delcom.pam_p4_ifs23042.R
import org.delcom.pam_p4_ifs23042.helper.RouteHelper
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBagData
import org.delcom.pam_p4_ifs23042.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23042.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23042.ui.components.TopAppBarMenuItem
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagActionUIState
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagDetailUIState
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagViewModel

@Composable
fun BagsDetailScreen(
    navController: NavHostController,
    bagViewModel: BagViewModel,
    bagId: String
) {
    val uiState by bagViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var bag by remember { mutableStateOf<ResponseBagData?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bagId) {
        bagViewModel.getBagById(bagId)
    }

    LaunchedEffect(uiState.bagDetail) {
        when (val state = uiState.bagDetail) {
            is BagDetailUIState.Success -> {
                bag = state.data
                isLoading = false
            }
            is BagDetailUIState.Error -> isLoading = false
            else -> {}
        }
    }

    LaunchedEffect(uiState.action) {
        when (uiState.action) {
            is BagActionUIState.Success -> {
                bagViewModel.resetAction()
                navController.popBackStack()
            }
            is BagActionUIState.Error -> {
                isLoading = false
                bagViewModel.resetAction()
            }
            else -> {}
        }
    }

    if (isLoading) {
        LoadingUI()
        return
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Tas") },
            text = { Text("Apakah kamu yakin ingin menghapus tas ini?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    isLoading = true
                    bagViewModel.deleteBag(bagId)
                }) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    val menuItems = listOf(
        TopAppBarMenuItem(
            text = "Edit Tas",
            icon = Icons.Filled.Edit,
            onClick = { RouteHelper.to(navController, "bags/${bagId}/edit") }
        ),
        TopAppBarMenuItem(
            text = "Hapus Tas",
            icon = Icons.Filled.Delete,
            isDestructive = true,
            onClick = { showDeleteDialog = true }
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBarComponent(
            navController = navController,
            title = "Detail Tas",
            showBackButton = true,
            customMenuItems = menuItems
        )

        bag?.let { b ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = "${BuildConfig.BASE_URL_BAGS_API}bags/${b.id}/image",
                    contentDescription = b.nama,
                    placeholder = painterResource(R.drawable.img_placeholder),
                    error = painterResource(R.drawable.img_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = b.nama,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = b.merek,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rp ${String.format("%,.0f", b.harga)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                DetailRow(label = "Warna", value = b.warna)
                DetailRow(label = "Bahan", value = b.bahan)
                DetailRow(label = "Stok", value = "${b.stok} pcs")

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Deskripsi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = b.deskripsi,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data tas tidak ditemukan")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}