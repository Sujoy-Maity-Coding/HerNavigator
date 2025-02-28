package com.sujoy.hernavigatoradmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sujoy.hernavigatoradmin.Presentation.AppNav
import com.sujoy.hernavigatoradmin.Presentation.ViewModel.ApiViewModel
import com.sujoy.hernavigatoradmin.ui.theme.HerNavigatorAdminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HerNavigatorAdminTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = remember { ApiViewModel() }
                    AppNav(viewModel = viewModel,innerPadding)
                }
            }
        }
    }
}