package com.example.projectpamt.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

// ─────────────────────────────────────────────────────────────────────────────
// Design Tokens (sesuai DESIGN.md Artisan Ledger)
// ─────────────────────────────────────────────────────────────────────────────

private val Primary         = Color(0xFF006241)
private val PrimaryContainer= Color(0xFF006241)
private val OnSurface       = Color(0xFF1B1C19)
private val OnSurfaceVariant= Color(0xFF3F4943)
private val Surface         = Color(0xFFFBF9F4)
private val White           = Color(0xFFFFFFFF)
private val GreenLight      = Color(0xFFD4E9E2)
private val ErrorContainer  = Color(0xFFFFDAD6)
private val Error           = Color(0xFFBA1A1A)

// ─────────────────────────────────────────────────────────────────────────────
// Data Model
// ─────────────────────────────────────────────────────────────────────────────

sealed class NavDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon
) {
    object Penjualan : NavDestination(
        route        = "penjualan",
        label        = "Penjualan",
        icon         = Icons.Filled.ShoppingCart,
        selectedIcon = Icons.Filled.ShoppingCart
    )
    object Produk : NavDestination(
        route        = "produk",
        label        = "Produk",
        icon         = Icons.Filled.Inventory2,    // gunakan alias sesuai versi Material3 Anda
        selectedIcon = Icons.Filled.Inventory2
    )
    // Posisi tengah — di-handle terpisah sebagai FAB, bukan NavItem biasa
    object Kas : NavDestination(
        route        = "kas",
        label        = "Kas",
        icon         = Icons.Filled.Storefront,
        selectedIcon = Icons.Filled.Storefront
    )
    object Pelanggan : NavDestination(
        route        = "pelanggan",
        label        = "Pelanggan",
        icon         = Icons.Filled.Group,
        selectedIcon = Icons.Filled.Group
    )
    object Dashboard : NavDestination(
        route        = "dashboard",
        label        = "Dashboard",
        icon         = Icons.Filled.Dashboard,
        selectedIcon = Icons.Filled.Dashboard
    )
}

/** Dua sub-aksi yang muncul saat FAB ditekan */
data class FabSubAction(
    val icon: ImageVector,
    val label: String,
    val tint: Color,
    val background: Color,
    val onClick: () -> Unit
)

// ─────────────────────────────────────────────────────────────────────────────
// Komponen Utama: ArtisanBottomBar
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bottom navigation bar bergaya Artisan Ledger dengan FAB tengah yang bisa
 * diperluas menjadi dua sub-aksi menggunakan animasi.
 *
 * @param currentRoute   Route yang sedang aktif.
 * @param onNavigate     Callback saat item nav ditekan.
 * @param subActions     Dua aksi yang ditampilkan saat FAB dibuka.
 *                       Kirimkan tepat 2 item.
 */
@Composable
fun ArtisanBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    subActions: List<FabSubAction>
) {
    // ── State ──────────────────────────────────────────────────────────────
    var fabExpanded by remember { mutableStateOf(false) }

    // ── Animasi icon FAB (rotasi 45° saat expanded → jadi ×) ──────────────
    val fabRotation by animateFloatAsState(
        targetValue  = if (fabExpanded) 45f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label        = "fab_rotation"
    )

    // ── Animasi skala FAB ──────────────────────────────────────────────────
    val fabScale by animateFloatAsState(
        targetValue  = if (fabExpanded) 0.92f else 1f,
        animationSpec = tween(150),
        label        = "fab_scale"
    )

    Box(modifier = Modifier.fillMaxWidth()) {

        // ── Scrim (klik di luar untuk tutup) ──────────────────────────────
        AnimatedVisibility(
            visible = fabExpanded,
            enter   = fadeIn(tween(200)),
            exit    = fadeOut(tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable(
                        indication          = null,
                        interactionSource   = remember { MutableInteractionSource() }
                    ) { fabExpanded = false }
            )
        }

        // ── Sub-action buttons (muncul di atas FAB) ────────────────────────
        // Hanya render bila ada tepat 2 aksi
        if (subActions.size == 2) {
            SubActionButtons(
                actions  = subActions,
                expanded = fabExpanded,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-76).dp) // tepat di atas navbar
            )
        }

        // ── Bottom Navigation Bar ─────────────────────────────────────────
        NavigationBarSurface(
            currentRoute = currentRoute,
            onNavigate   = onNavigate,
            fabRotation  = fabRotation,
            fabScale     = fabScale,
            fabExpanded  = fabExpanded,
            onFabClick   = { fabExpanded = !fabExpanded },
            modifier     = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-component: NavigationBarSurface
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun NavigationBarSurface(
    currentRoute: String,
    onNavigate  : (String) -> Unit,
    fabRotation : Float,
    fabScale    : Float,
    fabExpanded : Boolean,
    onFabClick  : () -> Unit,
    modifier    : Modifier = Modifier
) {
    val leftItems  = listOf(NavDestination.Penjualan, NavDestination.Produk)
    val rightItems = listOf(NavDestination.Pelanggan, NavDestination.Dashboard)

    Surface(
        modifier      = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, spotColor = Color.Black.copy(0.06f)),
        color         = White,
        tonalElevation= 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            // Kiri: Penjualan + Produk
            Row(
                modifier           = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(0.4f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment  = Alignment.CenterVertically
            ) {
                leftItems.forEach { dest ->
                    NavBarItem(
                        destination  = dest,
                        isSelected   = currentRoute == dest.route,
                        onClick      = { onNavigate(dest.route) }
                    )
                }
            }

            // Kanan: Pelanggan + Dashboard
            Row(
                modifier           = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.4f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment  = Alignment.CenterVertically
            ) {
                rightItems.forEach { dest ->
                    NavBarItem(
                        destination  = dest,
                        isSelected   = currentRoute == dest.route,
                        onClick      = { onNavigate(dest.route) }
                    )
                }
            }

            // Tengah: FAB "Kas" (menonjol ke atas)
            Column(
                modifier            = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-20).dp),   // naik keluar dari bar
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tombol FAB
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .scale(fabScale)
                        .shadow(
                            elevation  = 6.dp,
                            shape      = CircleShape,
                            spotColor  = Primary.copy(alpha = 0.35f)
                        )
                        .clip(CircleShape)
                        .background(Primary)
                        .clickable(
                            indication        = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick           = onFabClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = if (fabExpanded) Icons.Filled.Add else Icons.Filled.Storefront,
                        contentDescription = if (fabExpanded) "Tutup" else "Kas",
                        tint               = White,
                        modifier           = Modifier
                            .size(32.dp)
                            .rotate(fabRotation)
                    )
                }

                Spacer(Modifier.height(2.dp))

                // Label "Kas"
                Text(
                    text       = NavDestination.Kas.label,
                    color      = if (currentRoute == NavDestination.Kas.route)
                        Primary else OnSurfaceVariant,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-component: NavBarItem
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun NavBarItem(
    destination : NavDestination,
    isSelected  : Boolean,
    onClick     : () -> Unit
) {
    Column(
        modifier            = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                indication        = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick           = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector        = if (isSelected) destination.selectedIcon else destination.icon,
            contentDescription = destination.label,
            tint               = if (isSelected) Primary else OnSurfaceVariant,
            modifier           = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text          = destination.label,
            color         = if (isSelected) Primary else OnSurfaceVariant,
            fontSize      = 11.sp,
            fontWeight    = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 0.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-component: SubActionButtons
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Dua tombol aksi yang muncul di atas FAB saat expanded.
 * Tombol kiri muncul ke kiri-atas, tombol kanan ke kanan-atas,
 * menggunakan animasi offset + scale + fade.
 */
@Composable
private fun SubActionButtons(
    actions : List<FabSubAction>,   // harus tepat 2 item
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    // Animasi offset horizontal untuk masing-masing tombol
    val leftOffsetX by animateFloatAsState(
        targetValue   = if (expanded) -72f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label         = "left_offset"
    )
    val rightOffsetX by animateFloatAsState(
        targetValue   = if (expanded) 72f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label         = "right_offset"
    )
    val offsetY by animateFloatAsState(
        targetValue   = if (expanded) -16f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label         = "offset_y"
    )
    val scale by animateFloatAsState(
        targetValue   = if (expanded) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label         = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue   = if (expanded) 1f else 0f,
        animationSpec = tween(150),
        label         = "alpha"
    )

    Box(
        modifier          = modifier,
        contentAlignment  = Alignment.Center
    ) {
        // Tombol kiri (indeks 0)
        SubActionItem(
            action   = actions[0],
            scale    = scale,
            alpha    = alpha,
            offsetX  = leftOffsetX,
            offsetY  = offsetY
        )
        // Tombol kanan (indeks 1)
        SubActionItem(
            action   = actions[1],
            scale    = scale,
            alpha    = alpha,
            offsetX  = rightOffsetX,
            offsetY  = offsetY
        )
    }
}

@Composable
private fun SubActionItem(
    action : FabSubAction,
    scale  : Float,
    alpha  : Float,
    offsetX: Float,
    offsetY: Float
) {
    Column(
        modifier            = Modifier
            .offset(x = offsetX.dp, y = offsetY.dp)
            .scale(scale)
            .graphicsLayer(alpha = alpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Label di atas tombol
        Surface(
            shape  = RoundedCornerShape(6.dp),
            color  = Color(0xFF1B1C19).copy(alpha = 0.85f),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Text(
                text      = action.label,
                color     = White,
                fontSize  = 11.sp,
                fontWeight= FontWeight.SemiBold,
                modifier  = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        // Tombol bulat
        Box(
            modifier = Modifier
                .size(52.dp)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(action.background)
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = action.onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = action.icon,
                contentDescription = action.label,
                tint               = action.tint,
                modifier           = Modifier.size(26.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Contoh Penggunaan di Screen
// ─────────────────────────────────────────────────────────────────────────────

/*
@Composable
fun RingkasanKeuanganScreen(navController: NavController) {
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            ArtisanBottomBar(
                currentRoute = currentRoute?.destination?.route ?: "dashboard",
                onNavigate   = { route -> navController.navigate(route) },
                subActions   = listOf(
                    FabSubAction(
                        icon       = Icons.Filled.Add,
                        label      = "Penjualan Baru",
                        tint       = Color(0xFF006241),
                        background = Color(0xFFD4E9E2),
                        onClick    = { navController.navigate("new_sale") }
                    ),
                    FabSubAction(
                        icon       = Icons.Filled.RemoveShoppingCart,
                        label      = "Catat Beban",
                        tint       = Color(0xFFBA1A1A),
                        background = Color(0xFFFFDAD6),
                        onClick    = { navController.navigate("new_expense") }
                    )
                )
            )
        }
    ) { paddingValues ->
        // Konten layar di sini…
    }
}
*/