package com.sujoy.hernavigator.Presentation.Screen.OnBoardinScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay
import kotlinx.coroutines.launch

@Preview(showSystemUi = true)
@Composable
fun OnboardingScreen(onFinishOnboarding: () -> Unit = {}) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Pager for Swiping Between Pages
            HorizontalPager(
                count = 3,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageContent(page)
            }
            Column {

                // Pager Indicator
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    activeColor = Pink,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    if (pagerState.currentPage!=0) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(Pink),
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Arrow Back",
                                tint = Color.White
                            )
                        }
                    }
                    // Next or Finish Button
                    Button(
                        onClick = {
                            if (pagerState.currentPage < 2) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            } else {
                                onFinishOnboarding()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Pink),
                        shape = RoundedCornerShape(13.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = if (pagerState.currentPage == 2) "Get Started" else "Next",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: Int) {
    when (page) {
        0 -> OnboardingPage(
            title = "Stay Safe, Stay Secure",
            description = "Your safety is our priority! Instantly share your real-time location with trusted contacts and send emergency alerts with just one tap.",
            imageResource = R.drawable.safe_os
        )

        1 -> OnboardingPage(
            title = "Personalized Health & Wellness",
            description = "Get tailored fitness plans, skincare tips, menstrual pain relief exercises, pregnancy guidance, and mental wellness support—all powered by Gemini AI.",
            imageResource = R.drawable.health_os
        )

        2 -> OnboardingPage(
            title = "Smart Tools for Everyday Life",
            description = "Manage your expenses, securely store passwords, track your cycles, and set self-care reminders—HerNavigator keeps you organized and in control.",
            imageResource = R.drawable.tools_os
        )
    }
}

@Composable
fun OnboardingPage(title: String, description: String, imageResource: Int) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .background(
                    color = Pink,
                    shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .padding(36.dp)
                    .size(350.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 26.sp,
                    color = Pink,
                    fontFamily = playFairDisplay,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}
