package com.example.perfumestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.perfumestore.data.model.ProductItem
import com.example.perfumestore.ui.screen.CartScreen
import com.example.perfumestore.ui.screen.FinishOrderScreen
import com.example.perfumestore.ui.screen.LoginScreen
import com.example.perfumestore.ui.screen.ProductScreen
import com.example.perfumestore.ui.screen.ProfileScreen
import com.example.perfumestore.ui.screen.StartScreen
import com.example.perfumestore.ui.theme.PerfumeStoreTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PerfumeStoreTheme {
                val navController = rememberNavController()
                val mViewModel: MainViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "Start",
                    enterTransition = {
                        fadeIn()
                    },
                    exitTransition = {
                        fadeOut()
                    },
                    popEnterTransition = {
                        fadeIn()
                    },
                    popExitTransition = {
                        fadeOut()
                    }
                ) {
                    composable(
                        route = "Start"
                    ) {
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
                    composable("Cart") {
                        CartScreen(
                            mViewModel = mViewModel,
                            navController = navController
                        )
                    }
                    composable("FinishOrder"){
                        FinishOrderScreen(
                            mViewModel = mViewModel,
                            navController = navController
                        )
                    }
                    composable("Profile"){
                        if(mViewModel.isLoggedIntoAccount()){
                            ProfileScreen(
                                mViewModel = mViewModel,
                                navController = navController
                            )
                        }
                        else{
                            LoginScreen(
                                mViewModel = mViewModel,
                                navController = navController
                            )
                        }

                    }
                }
            }
        }
    }
}



