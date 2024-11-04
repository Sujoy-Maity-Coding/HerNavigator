package com.sujoy.hernavigator.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import android.telephony.SmsManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.*
import com.sujoy.hernavigator.Presentation.Screen.Safety.isLocationEnabled

class SafetyViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var messageSent = false

    // SharedPreferences for storing emergency contacts
    private val sharedPreferences =
        application.getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE)
    private val contactsKey = "saved_contacts"

    // Function to retrieve saved contacts
    fun getSavedContacts(): MutableList<String> {
        val contactsString = sharedPreferences.getString(contactsKey, "") ?: ""
        return if (contactsString.isNotEmpty()) {
            contactsString.split(",").toMutableList()
        } else {
            mutableListOf()
        }
    }

    // Function to save contacts to SharedPreferences
    fun saveContacts(contacts: List<String>) {
        val contactsString = contacts.joinToString(",")
        sharedPreferences.edit().putString(contactsKey, contactsString).apply()
    }

    // Function to send emergency message to the first contact in the list
    fun sendEmergencyMessageToFirstContact(context: Context) {
        val savedContacts = getSavedContacts()
        if (savedContacts.isNotEmpty()) {
            val firstContact = savedContacts[0]
            sendEmergencyMessage(context, firstContact) // Send SMS to the first contact
        } else {
            Toast.makeText(context, "No contacts available for emergency in \"Your Safety\" tools", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to add a contact
    fun addContact(contact: String): Boolean {
        val contacts = getSavedContacts()
        if (contacts.size < 5) { // Limit to 5 contacts
            contacts.add(contact)
            saveContacts(contacts)
            return true
        }
        return false
    }

    // Function to remove a contact
    fun removeContact(contact: String) {
        val contacts = getSavedContacts()
        contacts.remove(contact)
        saveContacts(contacts)
    }

    @SuppressLint("MissingPermission")
    fun sendEmergencyMessage(context: Context, phoneNumber: String) {
        if (!messageSent) {
            if (isLocationEnabled(context)) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        sendSmsWithLocation(context, phoneNumber, location)
                    } else {
                        requestLocationUpdate(context, phoneNumber)
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enable your location services", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(context, "Emergency message already sent", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate(context: Context, phoneNumber: String) {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000 // 5 seconds
            fastestInterval = 2000 // 2 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    sendSmsWithLocation(context, phoneNumber, location)
                    fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting the location
                } else {
                    Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            }
        }, Looper.getMainLooper())
    }

    private fun sendSmsWithLocation(context: Context, phoneNumber: String, location: Location) {
        val locationMessage = "https://www.google.com/maps?q=${location.latitude},${location.longitude}"
        val emergencyMessage = "I need help! My current location: $locationMessage"

        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, emergencyMessage, null, null)
            messageSent = true // Mark message as sent
            Toast.makeText(context, "Emergency SMS sent", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Reset the messageSent flag to allow sending a new emergency message if needed
    fun resetMessageSent() {
        messageSent = false
    }
}
