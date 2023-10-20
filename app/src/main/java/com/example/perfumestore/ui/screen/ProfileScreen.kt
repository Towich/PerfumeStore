package com.example.perfumestore.ui.screen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfumestore.MainActivity
import com.example.perfumestore.MainViewModel
import com.example.perfumestore.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    mViewModel: MainViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        var emailInput by remember { mutableStateOf("") }
        var passwordInput by remember { mutableStateOf("") }

        // Email input
        TextField(
            value = emailInput,
            onValueChange = { newEmail ->
                emailInput = newEmail
            },
            label = {
                Text(
                    text = "Email"
                )
            }
        )

        // Password input
        TextField(
            value = passwordInput,
            onValueChange = { newPass ->
                passwordInput = newPass
            },
            label = {
                Text(
                    text = "Password"
                )
            }
        )

        // Button "Register"
        Button(
            onClick = {
                mViewModel.registerViaEmailPassword(emailInput, passwordInput) { authResult ->
                    if (authResult.isSuccessful) {
                        navController.navigate("Profile") {
                            popUpTo("Start")
                        }
                    } else {
                        Log.e("AUTH", authResult.exception.toString())
                    }
                }
            }
        ) {
            Text(text = "Register")
        }

        // Button "Login"
        Button(
            onClick = {
                mViewModel.signInViaEmailPassword(emailInput, passwordInput){ authResult ->
                    if (authResult.isSuccessful) {
                        navController.navigate("Profile") {
                            popUpTo("Start")
                        }
                    } else {
                        Log.e("AUTH", authResult.exception.toString())
                    }
                }
            }
        ) {
            Text(text = "Login")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    mViewModel: MainViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Профиль",
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
                },
                actions = {

                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),

            ) {

            Image(
                painter = painterResource(id = R.drawable.towich_photo),
                contentDescription = "Your photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(128.dp)
                    .clip(CircleShape)
            )

            Text(
                text = mViewModel.getNameAccount(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )


            repeat(4) {
                ActionCard(nameCard = "Account", iconCard = Icons.Sharp.AccountCircle) {

                }
            }

            Button(
                onClick = {
                    mViewModel.logOutFromAccount()

                    navController.navigate("Profile") {
                        popUpTo("Start")
                    }
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
                    text = "Выйти из аккаунта",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ActionCard(
    nameCard: String,
    iconCard: ImageVector,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = iconCard,
                contentDescription = "$iconCard",
                modifier = Modifier
                    .padding(10.dp)
                    .padding(start = 10.dp)
                    .size(56.dp)
            )
            Text(
                text = nameCard,
                modifier = Modifier.padding(start = 10.dp),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow right",
                    modifier = Modifier
                        .size(32.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ActionCardPreview() {
    ActionCard(nameCard = "Account", iconCard = Icons.Sharp.AccountCircle) {

    }
}