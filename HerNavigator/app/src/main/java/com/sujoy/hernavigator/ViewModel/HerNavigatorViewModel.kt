package com.sujoy.hernavigator.ViewModel

import android.graphics.Bitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.sujoy.hernavigator.Api.Constant
import com.sujoy.hernavigator.Model.FitnessData
import com.sujoy.hernavigator.Presentation.Util.formatText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HerNavigatorViewModel() : ViewModel() {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constant.apiKey
    )

    private val generativeModelImg = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constant.apiKey
    )
    private val _pregnencyTrackerResult = MutableStateFlow<AnnotatedString?>(null)
    val pregnencyTrackerResult = _pregnencyTrackerResult.asStateFlow()

    private val _mentalWellnessResult = MutableStateFlow<AnnotatedString?>(null)
    val mentalWellnessResult = _mentalWellnessResult.asStateFlow()

    private val _colourMatchResult = MutableStateFlow<AnnotatedString?>(null)
    val colourMatchResult = _colourMatchResult.asStateFlow()

    private val _skinToneResult = MutableStateFlow<AnnotatedString?>(null)
    val skinToneResult = _skinToneResult.asStateFlow()

    private val _occasionResult = MutableStateFlow<AnnotatedString?>(null)
    val occasionResult = _occasionResult.asStateFlow()

    private val _periodPainResult = MutableStateFlow<AnnotatedString?>(null)
    val periodPainResult = _periodPainResult.asStateFlow()

    private val _skinCareResult = MutableStateFlow<AnnotatedString?>(null)
    val skinCareResult = _skinCareResult.asStateFlow()

    private val _pimpleReduction = MutableStateFlow<AnnotatedString?>(null)
    val pimpleReduction = _pimpleReduction.asStateFlow()

    private val _skinGlowResult = MutableStateFlow<AnnotatedString?>(null)
    val skinGlowResult = _skinGlowResult.asStateFlow()

    private val _hairCareResult = MutableStateFlow<AnnotatedString?>(null)
    val hairCareResult = _hairCareResult.asStateFlow()

    private val _cycleTrackerResult = MutableStateFlow<AnnotatedString?>(null)
    val cycleTrackerResult = _cycleTrackerResult.asStateFlow()

    private val _dietPlannerResult = MutableStateFlow<AnnotatedString?>(null)
    val dietPlannerResult = _dietPlannerResult.asStateFlow()

    private val _excerciseResult = MutableStateFlow<AnnotatedString?>(null)
    val excerciseResult = _excerciseResult.asStateFlow()

    private val _fashionResult = MutableStateFlow<AnnotatedString?>(null)
    val fashionResult = _fashionResult.asStateFlow()

    private val _fitnessData = MutableLiveData<FitnessData>()
    val fitnessData: LiveData<FitnessData> = _fitnessData

    private val _selectedImageBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedImageBitmap: StateFlow<Bitmap?> = _selectedImageBitmap

    fun pregnencyTrackerGenerator(week: String) {
        val prompt: String =
            "Generate a pregnancy tip for week [$week] that includes fetal development and advice for the mother."
        _pregnencyTrackerResult.value = AnnotatedString("Generating pregnancy tip...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _pregnencyTrackerResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _pregnencyTrackerResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun mentalWellnessGenerator(mood: String) {
        val prompt: String =
            "The user expressed feeling ${mood} in her journal. Suggest some activities and affirmation to improve her mood."
        _mentalWellnessResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _mentalWellnessResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _mentalWellnessResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun colourMatchGenerator(imageBitmap: Bitmap) {
        val prompt: String =
            "Analyze the provided image. If it features a woman's clothing item, suggest a complementary outfit, keeping in mind current fashion trends. Recommend additional pieces such as tops, bottoms, shoes, and accessories that enhance the overall style, considering color coordination and style of the clothing in the image. If the image features men's clothing, please upload an image of women's clothing to proceed."
        _colourMatchResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModelImg.generateContent(
                    content {
                        text(prompt)
                        image(imageBitmap)
                    }
                )
                // Update the UI with the generated and formatted text
                _colourMatchResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _colourMatchResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun setSelectedImage(bitmap: Bitmap) {
        _selectedImageBitmap.value = bitmap
    }

    fun fashionGenerator(imageBitmap: Bitmap) {
        val prompt = "Analyze the given image of a person and provide the following details:\n" +
                "- Dress color (top wear)\n" +
                "- Skin tone (light, medium, dark)\n" +
                "- Bottom wear color\n" +
                "- The likely occasion (casual, formal, festive, traditional, etc.)\n" +
                "Ensure the response is clear and structured."

        _fashionResult.value = AnnotatedString("Loading ...")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModelImg.generateContent(
                    content {
                        text(prompt)
                        image(imageBitmap)
                    }
                )
                _fashionResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _fashionResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun skinToneDressGenerator(imageBitmap: Bitmap) {
        val prompt: String =
            "Analyze the skin tone in the provided image. If the image shows a woman, create personalized outfit and accessory recommendations that complement her skin tone. Suggest fashionable items such as shirts, pants, skirts, dresses, shoes, and accessories to enhance her overall look, taking current fashion trends into account. If the image shows a man, please upload an image of a woman to proceed."
        _skinToneResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModelImg.generateContent(
                    content {
                        text(prompt)
                        image(imageBitmap)
                    }
                )
                // Update the UI with the generated and formatted text
                _skinToneResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _skinToneResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun occasionOutfitGenerator(event: String) {
        val prompt: String =
            "Based on the user's query about a [$event], suggest a stylish and appropriate outfit for the event. The user does not have specific details such as dress code, time of day, wedding style, personal style, budget, or prevalent colors. Please provide a versatile, universally suitable outfit suggestion for a [$event]."
        _occasionResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _occasionResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _occasionResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun periodPainGenerator(symptoms: String) {
        val prompt: String =
            "Provide exercises or tips to reduce period pain when the user reports specific symptoms like [$symptoms]."
        _periodPainResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _periodPainResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _periodPainResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun skinCareGenerator(weather: String, skinType: String) {
        val prompt: String =
            "Based on the user's skin type and current weather [$weather], suggest a skincare routine for [$skinType]."
        _skinCareResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _skinCareResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _skinCareResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun pimpleReductionGenerator(imageBitmap: Bitmap) {
        // Updated prompt for pimple reduction advice
        val prompt = """
    Analyze the skin condition visible in the provided image and provide practical, over-the-counter recommendations for reducing pimples. 
    Please include:
    1. A detailed skincare routine with specific product types for morning and evening (e.g., gentle cleansers, acne treatments, moisturizers, sunscreen) tailored to acne-prone skin.
    2. Suggested active ingredients (like salicylic acid, benzoyl peroxide, niacinamide) that can help reduce pimples and inflammation.
    3. Lifestyle and dietary suggestions, such as foods to avoid or consume, and habits that can promote clearer skin.
    Avoid suggesting consulting a dermatologist, and instead focus on practical steps that the user can try independently.
    Offer advice specific to the visible skin condition in the image without mentioning the need for medical consultation.
""".trimIndent()
        _pimpleReduction.value = AnnotatedString("Generating...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModelImg.generateContent(
                    content {
                        text(prompt)
                        image(imageBitmap)
                    }
                )
                // Update the UI with the generated and formatted text
                _pimpleReduction.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _pimpleReduction.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun skinGlowGenerator(imageBitmap: Bitmap) {
        val prompt = """
    Analyze the skin condition visible in the provided image and provide practical, over-the-counter recommendations for achieving a natural glow. 
    Please include:
    1. A detailed skincare routine with specific product types for morning and evening (e.g., gentle cleansers, hydrating serums, moisturizers, sunscreen) tailored to enhance skin radiance.
    2. Suggested active ingredients (like vitamin C, hyaluronic acid, niacinamide) that can improve skin texture, hydration, and brightness.
    3. Lifestyle and dietary suggestions, such as foods that promote healthy skin and habits that can enhance a natural glow.
    Focus on practical steps the user can try independently, specific to the visible skin condition in the image, without mentioning the need for medical consultation.
""".trimIndent()
        _skinGlowResult.value = AnnotatedString("Generating...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModelImg.generateContent(
                    content {
                        text(prompt)
                        image(imageBitmap)
                    }
                )
                // Update the UI with the generated and formatted text
                _skinGlowResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _skinGlowResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }


    fun hairCareGenerator(hairType: String, weather: String) {
        val prompt: String =
            "Based on the user's hair type and current weather [$weather], suggest a haircare routine for [$hairType]."
        _hairCareResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _hairCareResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _hairCareResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun cycleTrackerGenerator(selectedDate: String) {
        val prompt: String =
            "Given the user's last period start date in the format dd/mm/yyyy as $selectedDate, calculate the next period date by adding 28 days to the input date. Make sure to correctly adjust for changes in the day, month, and year as needed to provide an accurate prediction. Return the next period date in the same dd/mm/yyyy format and include tips for managing common menstrual symptoms."
        _cycleTrackerResult.value = AnnotatedString("Generating ...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _cycleTrackerResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _cycleTrackerResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun generateDietPlan(height: String, weight: String, age: String) {
        val prompt = """
    Based on my height of $height feet, weight of $weight kg, age of $age years, and gender of female, 
    create a personalized one-month diet plan. The plan should consider my aiming to support my specific needs (e.g., weight loss, weight gain, maintenance, 
    or overall health improvement). 

    The plan should include 3 main meals (Breakfast, Lunch, Dinner) and 2 snacks per day. Each meal should have:
    - A brief description of the food and its nutritional purpose.
    - The exact time the meal should be eaten.
    - Any specific dietary considerations (e.g., vegetarian, gluten-free).

    Format the response as follows:

    Breakfast: [meal description, dietary considerations if any] at [time]
    Lunch: [meal description, dietary considerations if any] at [time]
    Dinner: [meal description, dietary considerations if any] at [time]
    Snack 1: [meal description, dietary considerations if any] at [time]
    Snack 2: [meal description, dietary considerations if any] at [time]
""".trimIndent()


        _dietPlannerResult.value = AnnotatedString("Generating...")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _dietPlannerResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _dietPlannerResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun generateExcercisePlan(height: String, weight: String, age: String) {
        val prompt = """
    Based on my height of $height feet, weight of $weight kg, age of $age years, and gender female, 
    create a personalized one-month exercise plan. 

    The plan should include daily exercises split into morning and evening sessions. Each exercise should specify the type of exercise, duration, intensity, and target muscle groups or fitness goals (e.g., cardio, strength, flexibility, balance).

    Format the response as follows:

    Day 1:
    - Morning Exercise: [exercise description] for [duration] at [intensity level] (target areas)
    - Evening Exercise: [exercise description] for [duration] at [intensity level] (target areas)

    Please ensure the exercises vary throughout the month to cover different aspects of fitness, such as cardio, strength training, flexibility, and balance, and are suitable for my profile.
""".trimIndent()


        _excerciseResult.value = AnnotatedString("Generating...")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                // Update the UI with the generated and formatted text
                _excerciseResult.value = result.text?.let { formatText(it) }
            } catch (e: Exception) {
                _excerciseResult.value = AnnotatedString("Error: ${e.localizedMessage}")
            }
        }
    }

    fun setFitnessData(data: FitnessData) {
        _fitnessData.value = data
    }
}