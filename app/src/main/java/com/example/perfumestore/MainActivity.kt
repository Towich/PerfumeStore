package com.example.perfumestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.perfumestore.data.model.ProductItem
import com.example.perfumestore.ui.theme.PerfumeStoreTheme
import com.example.perfumestore.ui.theme.White

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            PerfumeStoreTheme {
                StartScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StartScreen() {

    val list: MutableList<ProductItem> = mutableListOf()
    var id = 0
    list.add(ProductItem(id++, 0, "0", 0f, 0f, 0))
    list.add(ProductItem(id++, 0, "0", 0f, 0f, 0))
    list.add(ProductItem(id++, 0, "0", 0f, 0f, 0))
    list.add(ProductItem(id++, 0, "0", 0f, 0f, 0))
    list.add(ProductItem(id++, 0, "0", 0f, 0f, 0))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Home",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {

                    Box(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color = White)
                            .clickable {

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.shopping_cart),
                            contentDescription = "Your cart",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.towich_photo),
                        contentDescription = "Your photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {

                            }
                    )
                }
            )
        }
    ) {
        Text(
            text = "Perfumes",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(it)
                .padding(start = 20.dp)
        )

        LazyVerticalStaggeredGrid(
            state = rememberLazyStaggeredGridState(),
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .padding(it)
                .padding(top = 50.dp),
            contentPadding = PaddingValues(10.dp)
        ){
            itemsIndexed(
                items = list,
                key = { _: Int, item: ProductItem ->
                    item.hashCode()
                }
            ) { _, item ->
                ProductCard(productItem = item)
            }


        }
    }
}

@Composable
fun ProductCard(
    productItem: ProductItem,
){
    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.tiziana_terenzi),
                contentDescription = "Tiziana Terenzi",
                modifier = Modifier.size(75.dp, 150.dp)
            )
        }


        Text(
            text = "Kirke",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
        )

        Text(
            text = "700 руб.",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = 20.dp, bottom = 20.dp)
        )
    }
}
