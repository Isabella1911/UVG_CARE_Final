package com.uvg.uvgcare.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.uvg.uvgcare.presentation.mainFlow.Things.favlist.FavoritesDestination
import com.uvg.uvgcare.presentation.mainFlow.addThing.CharacterProfileDestination
import com.uvg.uvgcare.presentation.mainFlow.list.NetflixStyleDestination

@Composable
fun BottomNavBar(
    checkItemSelected: (String) -> Boolean,
    onNavItemClick: (String) -> Unit
) {
    NavigationBar {
        val items = listOf(
            FavoritesDestination.ROUTE,
            CharacterProfileDestination.ROUTE,
            NetflixStyleDestination.ROUTE
        )

        items.forEach { destination ->
            NavigationBarItem(
                selected = checkItemSelected(destination),
                onClick = { onNavItemClick(destination) },
                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                label = { Text(destination) }
            )
        }
    }
}
