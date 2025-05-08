package com.jetkollage.nav

import androidx.navigation.NavBackStackEntry
import androidx.navigation.serialization.decodeArguments
import androidx.savedstate.savedState
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic

@Serializable
sealed class Graph {

    @Serializable
    data object About : Graph()

    @Serializable
    data object Home : Graph()

    @Serializable
    data object Canvas : Graph()


    companion object {
        // Hack to get polymorphic serialization working
        // Useful for shared viewModels
        @JvmStatic
        private val screens: Map<String?, KSerializer<out Graph>> by lazy {
            mapOf(
                Graph::class.qualifiedName to serializer(),
                About::class.qualifiedName to About.serializer(),
                Home::class.qualifiedName to Home.serializer(),
                Canvas::class.qualifiedName to Canvas.serializer(),
            )
        }

        fun NavBackStackEntry.fromRoute(): Graph? {
            val route = destination.route ?: return null
            val parsedRoute = route.substringBefore("/")
            val screen: KSerializer<out Graph> =
                requireNotNull(screens[parsedRoute]) { "Route missing, add it too list of screens" }
            val bundle = arguments ?: savedState()
            val typeMap = destination.arguments.mapValues { it.value.type }
            return screen.decodeArguments(bundle, typeMap)
        }
    }

}