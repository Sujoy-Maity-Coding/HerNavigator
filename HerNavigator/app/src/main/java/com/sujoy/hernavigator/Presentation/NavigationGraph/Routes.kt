package com.sujoy.hernavigator.Presentation.NavigationGraph

import com.sujoy.hernavigator.Api.data.apiDataModel.Article
import kotlinx.serialization.Serializable

sealed class Routes{
    @Serializable
    object Login:Routes()

    @Serializable
    object SignUp:Routes()

    @Serializable
    object Home : Routes()

    @Serializable
    object MentalWellness : Routes()

    @Serializable
    object PregnancyTracker : Routes()

    @Serializable
    object YourSafety : Routes()

    @Serializable
    object PeriodPain : Routes()

    @Serializable
    object SkinCare : Routes()

    @Serializable
    object BudgetManager : Routes()

    @Serializable
    object FashionTips : Routes()

    @Serializable
    object ColourMatch : Routes()

    @Serializable
    object SkinToneDress : Routes()

    @Serializable
    object OccasionDress : Routes()

    @Serializable
    object SelfCare : Routes()

    @Serializable
    object HairCare : Routes()

    @Serializable
    object CycleTracker : Routes()

    @Serializable
    object FitnessTracker : Routes()

    @Serializable
    object DietPlan : Routes()

    @Serializable
    object PrivateLocker : Routes()

    @Serializable
    object Excercise : Routes()

    @Serializable
    object NextDietScreen : Routes()

    @Serializable
    object weatherSkin : Routes()

    @Serializable
    object pimpleReduction : Routes()

    @Serializable
    object skinGlow : Routes()

    @Serializable
    object noNetwork : Routes()

    @Serializable
    object loadingScreen : Routes()

    @Serializable
    object NewsScreen : Routes()

    @Serializable
    object StartScreen:Routes()

    @Serializable
    object Tools:Routes()

    @Serializable
    object Profile:Routes()

    @Serializable
    object AllFashionImage:Routes()

    @Serializable
    object FashionDetails:Routes()

    @Serializable
    object FashionItem:Routes()
}