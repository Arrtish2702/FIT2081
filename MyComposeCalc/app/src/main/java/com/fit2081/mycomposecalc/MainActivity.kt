package com.fit2081.mycomposecalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.mycomposecalc.ui.theme.MyComposeCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeCalcTheme {
                ThemeSwitcherApp()
            }
        }
    }
}

@Composable
fun ThemeSwitcherApp() {
    var isDarkTheme by remember { mutableStateOf(false) } // Track theme state

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CalculatorLayout(
                modifier = Modifier.padding(innerPadding),
                onThemeChange = { isDark -> isDarkTheme = isDark } // Pass theme change function
            )
        }
    }
}

@Composable
fun CalculatorLayout(modifier: Modifier = Modifier, onThemeChange: (Boolean) -> Unit) {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var calcHistory = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Number 1") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(8.dp)
        )
        TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Number 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Result: $result",
            style = TextStyle(fontSize = 24.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { calculate("+", num1, num2, calcHistory)?.let { result = it } }) {
                Text("+")
            }
            Button(onClick = { calculate("-", num1, num2, calcHistory)?.let { result = it } }) {
                Text("-")
            }
            Button(onClick = { calculate("*", num1, num2, calcHistory)?.let { result = it } }) {
                Text("*")
            }
            Button(onClick = { calculate("/", num1, num2, calcHistory)?.let { result = it } }) {
                Text("/")
            }
            Button(onClick = { num1 = ""; num2 = ""; result = "0" }) {
                Text("Clear")
            }
        }

        Text(
            text = "History",
            style = TextStyle(fontSize = 24.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .border(1.dp, Color.Gray)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight()
            ) {
                items(calcHistory.reversed()) { entry ->
                    Text(
                        text = entry,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        // Theme Switch Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { changeTheme("Light", onThemeChange) }) {
                Text("Light")
            }
            Button(onClick = { changeTheme("Dark", onThemeChange) }) {
                Text("Dark")
            }
        }
    }
}

fun changeTheme(choice: String, updateTheme: (Boolean) -> Unit) {
    when (choice) {
        "Light" -> updateTheme(false)  // False for Light Mode
        "Dark" -> updateTheme(true)   // True for Dark Mode
    }
}


fun calculate(op: String, num1Str: String, num2Str: String, calcHistory: MutableList<String>): String? {
    val num1 = num1Str.toDoubleOrNull()
    val num2 = num2Str.toDoubleOrNull()

    if (num1 == null || num2 == null) {
        return null // Return null if input is invalid
    }

    val result: Double = when (op) {
        "+" -> num1 + num2
        "-" -> num1 - num2
        "*" -> num1 * num2
        "/" -> if (num2 != 0.0) num1 / num2 else return "Cannot divide by zero"
        else -> return "Invalid operation"
    }

    // Append the calculation as a readable string
    val historyEntry = "$num1 $op $num2 = $result"
    calcHistory.add(historyEntry)

    return result.toString()
}
