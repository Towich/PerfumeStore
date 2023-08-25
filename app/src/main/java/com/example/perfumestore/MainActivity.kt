package com.example.perfumestore

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.perfumestore.data.model.ProductItem
import com.example.perfumestore.ui.theme.PerfumeStoreTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PerfumeStoreTheme {
                val navController = rememberNavController()
                val mViewModel: MainViewModel = viewModel()
                NavHost(navController = navController, startDestination = "Home") {
                    composable("Home") {
                        StartScreen(
                            navController = navController,
                            mViewModel = mViewModel
                        )
                    }
                    composable("Product") {
                        ProductScreen(
                            mViewModel = mViewModel,
                            navController = navController,
                            productItem = mViewModel.currentProduct ?: ProductItem()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    mViewModel: MainViewModel,
    navController: NavController
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Парфюмерия",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {

                    // Cart
                    Box(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.background)
                            .clickable {

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_cart),
                            contentDescription = "Your cart",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }

                    // Profile
                    Image(
                        painter = painterResource(id = R.drawable.towich_photo),
                        contentDescription = "Your photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                //navController.navigate("Product")
                            }
                    )
                }
            )
        }
    ) {
        PerfumesList(
            innerPadding = it,
            mViewModel = mViewModel,
            navController = navController
        )
    }
}

@Composable
fun PerfumesList(
    innerPadding: PaddingValues,
    mViewModel: MainViewModel,
    navController: NavController
) {
    var list: MutableList<ProductItem> by remember { mutableStateOf(mutableListOf()) }

    // Connect our perfume list to perfume list from Firebase
    val myRef = mViewModel.getPerfumesListReference()
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.getValue<MutableList<ProductItem>>()
            list = value ?: mutableListOf(
                ProductItem()
            )
        }

        override fun onCancelled(error: DatabaseError) {
            list = mutableListOf(
                ProductItem()
            )
        }
    })

    // Show all perfumes
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(innerPadding),
        contentPadding = PaddingValues(10.dp)
    ) {

        itemsIndexed(
            items = list,
            key = { _: Int, item: ProductItem ->
                item.hashCode()
            }
        ) { _, item ->
            ProductCard(
                mViewModel = mViewModel,
                navController = navController,
                productItem = item,
                onClickFAB = { id ->
                    myRef.child(id.toString()).child("quantity").setValue(5)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    mViewModel: MainViewModel,
    navController: NavController,
    productItem: ProductItem,
    onClickFAB: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            mViewModel.currentProduct = productItem
            navController.navigate("Product")
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = productItem.imageId),
                contentDescription = productItem.name,
                modifier = Modifier.heightIn(max = 200.dp)
            )
        }

        Text(
            text = productItem.name,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(start = 10.dp, top = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, bottom = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = String.format("%.2f", productItem.sell_price) + " руб.",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.boxes),
                        contentDescription = "In storage",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(22.dp)

                    )

                    Text(
                        text = "${productItem.quantity} шт.",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(start = 5.dp)
                    )
                }

            }

            // FAB
            FloatingActionButton(
                onClick = {
                    onClickFAB(productItem.id)
                },
                modifier = Modifier
                    .size(32.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add perfume"
                )
            }
        }
    }
}

@Composable
fun ProductScreen(
    mViewModel: MainViewModel,
    navController: NavController,
    productItem: ProductItem
) {
    var showingBottomSheet: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Actions (back arrow + settings)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

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

            // Settings
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

        // Image
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = productItem.imageId),
                contentDescription = productItem.name,
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
                        Icon(
                            painter = painterResource(id = R.drawable.boxes),
                            contentDescription = "Boxes",
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
                    onClick = { /*TODO*/ },
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
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(){
                // Quantity with buttons "Plus"&"Minus"
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Количество",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
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
                        fontWeight = FontWeight.Bold
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