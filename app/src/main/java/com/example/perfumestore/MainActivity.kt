package com.example.perfumestore

import android.os.Bundle
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.perfumestore.ui.theme.White
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list: MutableList<ProductItem> = mutableListOf()
        var id = 0
        list.add(
            ProductItem(
                id++,
                R.drawable.kirke,
                "Kirke",
                "Tiziana Terenzi",
                300f,
                650f,
                1,
                25,
                "Мы создали чувственный и теплый аромат, который покорил нас, раскрывая наши души красоте чувств. Это волшебный и чувственный парфюм посвященный богине Цирцее. Духи Kirke' открываются фруктовыми сладостными и сочными нотами маракуйи, персиков, малины, черной смородины и груши, настоящий золотой алхимический эликсир. В сердце аромат поддерживает фруктовую феерию нотами очаровательного ландыша, а шлейф похож на прогулку по песку босыми ногами: нежный гелиотропин, сандал, ваниль, мускус и пачули. Аромат Kirke восхищает своего владельца как знаменитый очаровывающий эликсир богини Цирцеи, направляя по таинственным путям в поисках удивительных чудес. Он вдохновляет, создавая изысканную ауру утонченной чувственности."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.tom_ford_lost_cherry,
                "Lost Cherry",
                "Tom Ford",
                300f,
                650f,
                2,
                25,
                "Сладость. Соблазн. Неутолимое желание. Lost Cherry — насыщенный аромат, открывающий двери в некогда запретный мир. Его соблазнительная двойственность основана на контрасте игривой, блестящей и гладкой изнанки с сочной мякотью внутри», — ТОМ ФОРД. Черешня, масло горького миндаля, сироп из вишни «гриот», турецкая роза, перуанский бальзам и обжаренные бобы тонка.\n" +
                        "\n" +
                        "Насыщенный и изысканный восточный аромат Lost Cherry пронизан контрастами. Он переносит нас в место, где невинность пересекается с наслаждением, а сладость встречается с соблазном.\n" +
                        "\n" +
                        "Начальные ноты Lost Cherry воссоздают притягательность и совершенство экзотических плодов черешни и ее спелой сияющей мякоти, источающей вишневый ликер с дразнящим привкусом горького миндаля. Затем раскрываются двойственные аккорды, в которых контрастируют сладкие и терпкие, яркие и таинственные ноты. Ноты сиропа из вишни «гриот» напоминают об аппетитных вымоченных ягодах, а звучание турецкой розы и арабского жасмина придает аромату глубокий землистый оттенок. Lost Cherry пробуждает неутолимое желание и будоражит фантазию, завершаясь величественным насыщенным шлейфом из чувственных нот перуанского бальзама, обжаренных бобов тонка, сандалового дерева, ветивера и кедра."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.tom_ford_bitter_peach,
                "Bitter Peach",
                "Tom Ford",
                300f,
                650f,
                3,
                25,
                "Откровенно сладкий, темный и роскошный аромат Bitter Peach накрывает волной таинственной, обволакивающей чувственности. Он напоминает спелый и сочный фрукт, перед соблазнительным ароматом которого невозможно устоять», — ТОМ ФОРД. Tom Ford представляет новый аромат в коллекции Private Blend. Bitter Peach — это роскошный соблазнительный нектар из самых спелых, головокружительно сладких фруктов с манящим ароматом. Опьяняющая фруктово-цветочная композиция открывается аппетитными сладкими нотами персика, выросшего среди виноградников, и масла красного сицилийского апельсина. Острота пряного масла кардамона как будто вызывает ощущение сочной и спелой мякоти на языке. В чувственном сердце аромата горькие ноты гелиотропа и масла полыни переплетаются с головокружительными оттенками абсолюта рома и коньячного масла. Звучание абсолюта арабского жасмина подталкивает к беззастенчивому изучению своих желаний. Роскошное сочетание абсолюта сандалового дерева, бензоиновой смолы и кашмерана делают шлейф более насыщенным и выразительным. С нотами ванили, абсолюта бобов тонка и масла индонезийского пачули Bitter Peach не теряет своей соблазнительности. Изысканный дизайн флаконов Tom Ford Bitter Peach продуман до мельчайших деталей. Изнутри флакон объемом 50 мл покрыт перламутровым лаком, а снаружи выполнен из полупрозрачного стекла нежного оттенка. Флакон подчеркивает насыщенность и многогранность композиции и манит прикоснуться к миру сладких и соблазнительных ароматов. Оттенок флакона навеян разнообразными оттенками мякоти персика и винтажными цветными стеклами."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.l1212,
                "L.12.12",
                "LACOSTE",
                300f,
                650f,
                4,
                25,
                "Парфюмерная вода Lacoste L.12.12 Blanc для него. Совершенно чуждый традиционным принципам, но при этом всегда актуальный аромат переливается контрастами, чтобы идеально подстроиться под своего владельца. Изящно искрящиеся оттенки цитрусовых сливаются с наполняющими энергией зелеными нотами. Тепло дерева вступает в противоборство со свежестью эвкалипта. Все это на фоне базовых успокаивающих нот кедра и кардамона."
            )
        )
        list.add(
            ProductItem(
                id++,
                R.drawable.tom_ford_tobacco_vanille,
                "Tobacco Vanille",
                "Tom Ford",
                300f,
                650f,
                5,
                25,
                "Аромат Tom Ford Tobacco Vanille вызывает ассоциации с английским клубом джентльменов и открывается с новой стороны благодаря неожиданным ингредиентам и ноткам специй. Tom Ford Tobacco Vanille — это богатый и теплый аромат с неожиданным и современным характером."
            )
        )


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
    navController: NavController,
    database: FirebaseDatabase = Firebase.database
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
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_cart),
                            contentDescription = "Your cart",
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
            navController = navController,
            database = database
        )
    }
}

@Composable
fun PerfumesList(
    innerPadding: PaddingValues,
    mViewModel: MainViewModel,
    navController: NavController,
    database: FirebaseDatabase
) {
    var list: List<ProductItem> by remember { mutableStateOf(listOf()) }

    // Connect our perfume list to perfume list from Firebase
    val myRef = database.getReference("perfumes")
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.getValue<List<ProductItem>>()
            list = value ?: listOf(
                ProductItem()
            )
        }

        override fun onCancelled(error: DatabaseError) {
            list = listOf(
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
                productItem = item
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    mViewModel: MainViewModel,
    navController: NavController,
    productItem: ProductItem
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
    navController: NavController,
    productItem: ProductItem
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp)
        ) {
            IconButton(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "Back arrow")
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


}