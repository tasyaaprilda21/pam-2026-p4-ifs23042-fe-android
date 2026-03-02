package org.delcom.pam_p4_ifs23042.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23042.R
import org.delcom.pam_p4_ifs23042.helper.ConstHelper
import org.delcom.pam_p4_ifs23042.helper.RouteHelper
import org.delcom.pam_p4_ifs23042.network.bags.data.ResponseBagData
import org.delcom.pam_p4_ifs23042.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23042.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23042.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagViewModel
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagsUIState

@Composable
fun BagsScreen(
    navController: NavHostController,
    bagViewModel: BagViewModel
) {
    val uiStateBag by bagViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var bags by remember { mutableStateOf<List<ResponseBagData>>(emptyList()) }

    fun fetchBagsData() {
        isLoading = true
        bagViewModel.getAllBags(searchQuery.text)
    }

    LaunchedEffect(Unit) {
        fetchBagsData()
    }

    LaunchedEffect(uiStateBag.bags) {
        if (uiStateBag.bags !is BagsUIState.Loading) {
            isLoading = false
            bags = if (uiStateBag.bags is BagsUIState.Success) {
                (uiStateBag.bags as BagsUIState.Success).data
            } else {
                emptyList()
            }
        }
    }

    if (isLoading) {
        LoadingUI()
        return
    }

    fun onOpen(bagId: String) {
        RouteHelper.to(
            navController = navController,
            destination = "bags/${bagId}"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Tas",
            showBackButton = false,
            withSearch = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { query -> searchQuery = query },
            onSearchAction = { fetchBagsData() }
        )

        Box(modifier = Modifier.weight(1f)) {
            BagsUI(bags = bags, onOpen = ::onOpen)

            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = {
                        RouteHelper.to(
                            navController,
                            ConstHelper.RouteNames.BagsAdd.path
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Tas"
                    )
                }
            }
        }

        BottomNavComponent(navController = navController)
    }
}

@Composable
fun BagsUI(
    bags: List<ResponseBagData>,
    onOpen: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(bags) { bag ->
            BagItemUI(bag, onOpen)
        }
    }

    if (bags.isEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Tidak ada data tas!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun BagItemUI(
    bag: ResponseBagData,
    onOpen: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onOpen(bag.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = "${org.delcom.pam_p4_ifs23042.BuildConfig.BASE_URL_BAGS_API}bags/${bag.id}/image",
                contentDescription = bag.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .size(70.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bag.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = bag.merek,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Rp ${String.format("%,.0f", bag.harga)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = bag.deskripsi,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}