package com.example.perfumestore.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfumestore.MainViewModel
import com.example.perfumestore.R

@Composable
fun FinishOrderScreen(
    mViewModel: MainViewModel,
    navController: NavController
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.finish_order),
                contentDescription = "Finish order",
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(1.5f)
            )

            Text(
                text = "Заказ оформлен",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 75.dp)
            )

            Text(
                text = "Ваш заказ оформлен, ожидайте подтверждения. Спасибо за покупку!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .padding(start = 100.dp, end = 100.dp, top = 20.dp),
                textAlign = TextAlign.Center
            )
        }


        // Button "Back to Home"
        Button(
            onClick = {
                navController.navigate("Start"){
                    popUpTo(0)
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
                text = "Готово",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}