package com.rezalaki.registeraddresscompose

import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.ui.screen.addressListScreen.AddressScreen
import com.rezalaki.registeraddresscompose.ui.screen.formScreen.FormScreen
import com.rezalaki.registeraddresscompose.ui.screen.mapScreen.MapScreen
import com.rezalaki.registeraddresscompose.ui.theme.RegisterAddressComposeTheme
import com.rezalaki.registeraddresscompose.ui.theme.colorBackground
import org.aviran.cookiebar2.CookieBar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppLocaleFa()
        setContent {
            RegisterAddressComposeTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorBackground),
                ) { innerPadding ->
                    AppNavigator(
                        modifier = Modifier.padding(innerPadding),
                        showSuccessMessage = {
                            showSuccessMessage(it)
                        },
                        showError = { errorList: List<String> ->
                            showErrorList(errorList)
                        }
                    )
                }
            }
        }
    }

    private fun setAppLocaleFa() {
        val locale = Locale("FA")
        Locale.setDefault(locale)
        val resources: Resources = resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun showErrorList(errors: List<String>) {
        val error: Pair<String, Long> = if (errors.size == 1) {
            Pair(
                errors.first(),
                1_500L
            )
        } else {
            Pair(
                errors.fastJoinToString(separator = "\n") { " - $it " },
                (1_200 * errors.size).toLong()
            )
        }
        CookieBar.build(this)
            .setBackgroundColor(R.color.red)
            .setMessageColor(R.color.white)
            .setDuration(error.second)
            .setMessage(error.first)
            .show()
    }

    private fun showSuccessMessage(message: String) {
        CookieBar.build(this)
            .setBackgroundColor(R.color.green)
            .setMessageColor(R.color.white)
            .setDuration(1_800L)
            .setMessage(message)
            .show()
    }

}


@Composable
fun AppNavigator(
    modifier: Modifier = Modifier,
    showSuccessMessage: (String) -> Unit,
    showError: (errorsList: List<String>) -> Unit
) {

    val ROUTES = object {
        val FORM_SCREEN = object {
            val route = "FORM"
        }
        val MAP_SCREEN = object {
            val route = "MAP"
            val paramName = "formData"
        }
        val ADDRESS_LIST_SCREEN = object {
            val route = "ADDRESS_LIST"
        }
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTES.FORM_SCREEN.route
    ) {
        composable(ROUTES.FORM_SCREEN.route) {
            FormScreen(
                goToMap = {formRequestBody ->
                    val formData = Uri.encode(Gson().toJson(formRequestBody))
                    navController.navigate("${ROUTES.MAP_SCREEN.route}/$formData")
                },
                showError = { errorList ->
                    showError.invoke(errorList)
                }
            )
        }

        composable(
            route = "${ROUTES.MAP_SCREEN.route}/{${ROUTES.MAP_SCREEN.paramName}}",
            arguments = listOf(
                navArgument(
                    ROUTES.MAP_SCREEN.paramName
                ) {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val formJson = backStackEntry.arguments?.getString(ROUTES.MAP_SCREEN.paramName)
            val formBody = Gson().fromJson(formJson, SubmitAddressRequestBody::class.java)
            MapScreen(
                formRequestBody = formBody,
                onBackPressed = { navController.popBackStack() },
                goToAddressList = {
                    navController.navigate(ROUTES.ADDRESS_LIST_SCREEN.route)
                },
                showSuccessMessage = {
                    showSuccessMessage.invoke(it)
                },
                showError = { errorList ->
                    showError.invoke(errorList)
                }
            )
        }

        composable(ROUTES.ADDRESS_LIST_SCREEN.route) {
            AddressScreen(
                backPressed = { navController.popBackStack() },
                showError = {errorList ->
                    showError.invoke(errorList)
                }
            )
        }
    }
}
