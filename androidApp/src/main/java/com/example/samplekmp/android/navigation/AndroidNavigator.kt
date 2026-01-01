package com.example.samplekmp.android.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import com.example.samplekmp.android.view.MenuRoute
import com.example.samplekmp.android.view.PokemonDetailRoute
import com.example.samplekmp.android.view.PokemonListRoute
import com.example.samplekmp.navigation.Destination
import com.example.samplekmp.navigation.Navigator

class AndroidNavigator(
    private val navController: NavController
) : Navigator {
    override fun navigate(destination: Destination) {
        when (destination) {
            is Destination.PokemonList -> navController.navigate(PokemonListRoute) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            is Destination.Menu -> navController.navigate(MenuRoute) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            is Destination.PokemonDetail -> navController.navigate(
                PokemonDetailRoute(destination.pokemonId)
            )
        }
    }

    override fun navigateBack() {
        navController.popBackStack()
    }
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No Navigator provided")
}
