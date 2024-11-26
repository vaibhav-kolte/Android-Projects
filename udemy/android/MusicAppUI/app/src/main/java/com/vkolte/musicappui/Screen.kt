package com.vkolte.musicappui

import androidx.annotation.DrawableRes

sealed class Screen(
    val title: String,
    val route: String
) {

    sealed class BottomScreen(val bTitle: String, val bRoute: String, @DrawableRes val icon: Int) :
        Screen(bTitle, bRoute) {
        object Home : BottomScreen("Home", "home", R.drawable.home)
        object Browse : BottomScreen("Browse", "browse", R.drawable.browse_gallery)
        object Library : BottomScreen("Library", "library", R.drawable.local_library)
    }

    sealed class DrawerScreen(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int) :
        Screen(dTitle, dRoute) {
        object Account : DrawerScreen("Account", "account", R.drawable.ic_account)

        object Subscription : DrawerScreen("Subscription", "subscribe", R.drawable.ic_subscribe)

        object AddAccount :
            DrawerScreen("Add Account", "add_account", R.drawable.ic_add_person_add_alt_1_24)
    }
}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Browse,
    Screen.BottomScreen.Library
)

val screenInDrawer = listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.Subscription,
    Screen.DrawerScreen.AddAccount
)