package com.example.projectpamt.ui.screens.home.kas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.ui.screens.home.kas.components.KasCard
import com.example.projectpamt.ui.navigation.EditKas
import com.example.projectpamt.ui.navigation.LogKas
import com.example.projectpamt.ui.navigation.LogTotalKas
import com.example.projectpamt.ui.navigation.TambahKas
import com.example.projectpamt.ui.navigation.TransaksiKas
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.kas.uistate.KasUiState
import com.example.projectpamt.viewmodel.kas.KasViewModel

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KasScreen(
    modifier: Modifier = Modifier,
    viewModel: KasViewModel = viewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showInfoDialog by remember { mutableStateOf(false) }
    var infoDialogTitle by remember { mutableStateOf("") }
    var infoDialogMessage by remember { mutableStateOf("") }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(infoDialogTitle, fontWeight = FontWeight.Bold) },
            text = { Text(infoDialogMessage) },
            confirmButton = {
                Button(
                    onClick = { showInfoDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    // Auto-refresh when returning from add/edit screens
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val needRefresh by savedStateHandle?.getStateFlow("need_refresh", false)?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) }

    LaunchedEffect(needRefresh) {
        if (needRefresh) {
            viewModel.refresh()
            savedStateHandle?.set("need_refresh", false)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllActiveKas()
    }

    KasContent(
        modifier = modifier,
        uiState = uiState,
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::refresh,
        onAddKasClick = {
            navController.navigate(TambahKas)
        },
        onLihatLogClick = { kas ->
            navController.navigate(LogKas(kas))
        },
        onTransaksiClick = { kas ->
            navController.navigate(TransaksiKas(kas))
        },
        onEditClick = { kas ->
            navController.navigate(EditKas(kas))
        },
        onLogTotalClick = {
            navController.navigate(LogTotalKas)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KasContent(
    modifier: Modifier = Modifier,
    uiState: KasUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onAddKasClick: () -> Unit,
    onLihatLogClick: (Kas) -> Unit,
    onTransaksiClick: (Kas) -> Unit,
    onEditClick: (Kas) -> Unit,
    onLogTotalClick: () -> Unit
) {

    val totalSaldo = when (uiState) {
        is KasUiState.Success -> uiState.data.filter { it.aktif }.sumOf { it.saldo }
        else -> 0.0
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
        ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = GreenPrimary)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Manajemen Kas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Kelola akun kas Anda",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }


                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.kas),
                    contentDescription = "Kas",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = GreenPrimary,
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Total Saldo Kas",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = formatRupiah(totalSaldo),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = (-0.9).sp
                            )
                        }


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(34.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GreenMintActive)
                                .clickable { onLogTotalClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.history),
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Log Total",
                                    fontSize = 14.sp,
                                    color = GreenPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }


            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Akun Kas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )


                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(9999.dp))
                            .background(GreenPrimary)
                            .clickable { onAddKasClick() }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.add),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Tambah Kas",
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }


            item {
                when (uiState) {
                    is KasUiState.Loading -> {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = GreenPrimary)
                        }
                    }

                    is KasUiState.Error -> {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.message,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }

                    is KasUiState.Success -> {
                        if (uiState.data.isEmpty()) {
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tidak ada akun kas",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                uiState.data.forEach { kas ->
                                    KasCard(
                                        kas = kas,
                                        onLihatLogClick = { onLihatLogClick(kas) },
                                        onTransaksiClick = { onTransaksiClick(kas) },
                                        onEditClick = { onEditClick(kas) }
                                    )
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
}
