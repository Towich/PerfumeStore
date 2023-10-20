package com.example.perfumestore.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.perfumestore.MainViewModel
import com.example.perfumestore.R
import com.example.perfumestore.data.model.ProductItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

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
                                navController.navigate("Cart")
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
                        painter = if(mViewModel.isLoggedIntoAccount()) painterResource(id = R.drawable.towich_photo) else painterResource(id = R.drawable.profile_icon),
                        contentDescription = "Your photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate("Profile")
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
            mViewModel.fromCart = false
            mViewModel.currentProduct = productItem
            navController.navigate("Product")
        }
    ) {

        // Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = productItem.imageUrl,
                contentDescription = null,
                loading = {
                    CircularProgressIndicator()
                },
                modifier = Modifier.heightIn(max = 200.dp)
            )
        }

        // Name of perfume
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
            // Name, Sell Price and Quantity
            Column {

                // Sell price
                Text(
                    text = String.format("%.2f", productItem.sell_price) + " руб.",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                )

                // Quantity
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
                    mViewModel.addToCart(productItem)
                },
                modifier = Modifier
                    .size(32.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_48px),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Add perfume"
                )
            }
        }
    }
}