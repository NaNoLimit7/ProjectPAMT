package com.example.projectpamt.ui.navigation

sealed class Destinations(val route: String) {
    object Login : Destinations("login")
    object Register : Destinations("register")
    object Dashboard : Destinations("dashboard")
}