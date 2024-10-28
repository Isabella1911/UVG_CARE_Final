package com.uvg.uvgcare.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.uvg.uvgcare.presentation.mainFlow.Things.favlist.FavoritesDestination
import com.uvg.uvgcare.presentation.mainFlow.addThing.CharacterProfileDestination
import com.uvg.uvgcare.presentation.mainFlow.list.NetflixStyleDestination

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destination: String
)

val navigationItems = listOf(
    NavItem(
        title = "Favorites",
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        destination = FavoritesDestination.ROUTE
    ),
    NavItem(
        title = "List",
        selectedIcon = Icons.Filled.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
        destination = NetflixStyleDestination.ROUTE
    ),
    NavItem(
        title = "Add",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        destination = CharacterProfileDestination.ROUTE
    )
)

val topLevelDestinations = listOf(
    FavoritesDestination.ROUTE,
    NetflixStyleDestination.ROUTE,
    CharacterProfileDestination.ROUTE
)
