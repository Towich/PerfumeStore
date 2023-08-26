package com.example.perfumestore.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfumestore.MainViewModel
import com.example.perfumestore.R
import com.example.perfumestore.data.model.ProductItem
import com.example.perfumestore.ui.theme.White

/**
 * Shopping cart screen.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    mViewModel: MainViewModel,
    navController: NavController
) {
    var itemsInCart: MutableList<ProductItem> by remember { mutableStateOf(mViewModel.itemsInCart) }
    var totalSum: Float by remember { mutableStateOf(mViewModel.getTotalCartSum()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Моя корзина",
                        fontWeight = FontWeight.SemiBold
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
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(top = 10.dp)
        ) {

            // List of products
            for (item in itemsInCart) {
                ProductInCart(
                    mViewModel = mViewModel,
                    navController = navController,
                    productItem = item,
                    onChangeQuantity = {
                        totalSum = mViewModel.getTotalCartSum()
                    },
                    onRemoveItem = { itemToRemove ->
                        itemsInCart =
                            itemsInCart.minus(itemToRemove) as MutableList<ProductItem>
                    }
                )
            }


            // Divider
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            )

            // Row with total sum
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Сумма",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = String.format("%.2f", totalSum) + " руб.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Button "Order now"
            Button(
                onClick = {
                    var orderText: String = "У Вас новый заказ! Стоимость: " + String.format("%.2f", mViewModel.getTotalCartSum()) + " руб."
                    for(item in mViewModel.itemsInCart){
                        orderText += "\n~ ${item.producer} ${item.name}, ${item.quantity} шт."
                    }
                    mViewModel.sendMessageToTelegram(orderText)
                    navController.navigate("FinishOrder")
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .height(65.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Оформить заказ",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ProductInCart(
    mViewModel: MainViewModel,
    navController: NavController,
    productItem: ProductItem,
    onChangeQuantity: () -> Unit,
    onRemoveItem: (productItem: ProductItem) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .clickable {
                mViewModel.fromCart = true
                mViewModel.currentProduct = productItem
                navController.navigate("Product")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                // Image of Product
                Image(
                    painter = painterResource(id = productItem.imageId),
                    contentDescription = "image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .height(115.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)
                )

                // Column with Name, Producer&Volume and Sell Price
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = productItem.name,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = productItem.producer + ", " + productItem.volume.toString() + " мл.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Text(
                        text = String.format("%.2f", productItem.sell_price) + " руб.",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

            // Surface with actions and quantity
            Surface(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(40.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    // Button "Add"
                    IconButton(
                        onClick = {
                            productItem.quantity = productItem.quantity + 1
                            onChangeQuantity()
                        },
                        modifier = Modifier
                            .size(22.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_48px),
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }

                    // Text with quantity
                    Text(
                        text = productItem.quantity.toString(),
                        fontWeight = FontWeight.Bold
                    )

                    // Button "Minus"
                    IconButton(
                        onClick = {
                            productItem.quantity = productItem.quantity - 1
                            onChangeQuantity()

                            if (productItem.quantity < 1) {
                                mViewModel.itemsInCart =
                                    mViewModel.itemsInCart.minus(productItem) as MutableList<ProductItem>
                                onRemoveItem(productItem)
                            }
                        },
                        modifier = Modifier
                            .size(22.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.remove_48px),
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

        }
    }
}