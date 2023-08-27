package com.example.perfumestore.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.perfumestore.MainViewModel
import com.example.perfumestore.R
import com.example.perfumestore.data.model.ProductItem

/**
 * Product screen with information of current chosen perfume.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    mViewModel: MainViewModel,
    navController: NavController,
    productItem: ProductItem
) {
    var showingBottomSheet: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = ""
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    // Arrow back
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.ArrowBack,
                            contentDescription = "Back arrow",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    // Settings
                    if (!mViewModel.fromCart) {
                        Box(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.surface)
                                .clickable {
                                    showingBottomSheet = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Setting",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Image
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = productItem.imageUrl,
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator()
                    },
                    modifier = Modifier
                        .heightIn(min = 300.dp, max = 400.dp)
                )
            }

            // Surface like BottomSheet
            Surface(
                shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp),
                modifier = Modifier
                    .padding(top = 25.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                        .padding(20.dp),
                ) {

                    // Name + price
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = productItem.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = String.format("%.2f", productItem.sell_price) + " руб.",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Producer + quantity at storage
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = productItem.producer,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = productItem.quantity.toString() + " шт."
                            )

                            val mPainter: Painter
                            val mContentDescription: String
                            if (mViewModel.fromCart) {
                                mPainter = painterResource(id = R.drawable.shopping_cart_2)
                                mContentDescription = "In cart"
                            } else {
                                mPainter = painterResource(id = R.drawable.boxes)
                                mContentDescription = "Boxes"
                            }
                            Icon(
                                painter = mPainter,
                                contentDescription = mContentDescription,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(26.dp)
                                    .padding(start = 5.dp)
                            )
                        }
                    }

                    // Perfume volume
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Объём",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = productItem.volume.toString() + " мл.",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.perfume),
                                contentDescription = "Liters",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(26.dp)
                            )
                        }

                    }

                    // Description
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 20.dp)
                    )

                    Text(
                        text = productItem.description,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )


                }

                // FAB
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButton(
                        onClick = {
                            mViewModel.addToCart(productItem)
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(100),
                        modifier = Modifier
                            .size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_cart),
                            contentDescription = "Add to cart",
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                }
            }
        }

        if (showingBottomSheet) {
            SettingsBottomSheet(
                mViewModel = mViewModel,
                productItem = productItem,
                onDismiss = {
                    showingBottomSheet = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    mViewModel: MainViewModel,
    productItem: ProductItem,
    onDismiss: () -> Unit
) {
    var currentQuantity: Int by remember { mutableStateOf(productItem.quantity) }
    var currentPrice: Float by remember { mutableStateOf(productItem.sell_price) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .height(400.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column() {

                // Perfume Name
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = productItem.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Quantity with buttons "Plus"&"Minus"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Количество",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Remove button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.surface)
                                .clickable {
                                    currentQuantity--
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.remove_48px),
                                contentDescription = "Minus",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        // Quantity
                        Text(
                            text = currentQuantity.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp)
                        )

                        // Add button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.surface)
                                .clickable {
                                    currentQuantity++
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_48px),
                                contentDescription = "Add",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }

                // Price with buttons "Plus"&"Minus"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Стоимость",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Remove button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.surface)
                                .clickable {
                                    currentPrice -= 50f
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.remove_48px),
                                contentDescription = "Minus",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        // Price
                        Text(
                            text = String.format("%.2f", currentPrice),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp)
                        )

                        // Add button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.surface)
                                .clickable {
                                    currentPrice += 50f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_48px),
                                contentDescription = "Add",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }

                }
            }


            // Save button
            Button(
                onClick = {
                    // Update quantity in db
                    mViewModel.updateQuantityOfProduct(
                        productItem = productItem,
                        newQuantity = currentQuantity
                    )

                    // Update sell price in db
                    mViewModel.updateSellPriceOfProduct(
                        productItem = productItem,
                        newSellPrice = currentPrice
                    )
                    onDismiss()
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Сохранить",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

