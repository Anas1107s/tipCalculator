package com.anas.tipcalculator

import android.R.attr.checked
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anas.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    TiptimeLayout()
                }
            }
        }
    }
}

@Composable
fun TiptimeLayout(
    modifier: Modifier = Modifier
) {
    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0

    var tipPercent by remember { mutableStateOf("") }
    val percent = tipPercent.toDoubleOrNull() ?: 0.0

    var roundUp by remember { mutableStateOf(false) }

    val tip = calculateTip(amount, percent, roundUp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeContentPadding()
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth()
        )

        EditNumberField(
            label = R.string.How_was_the_service,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = tipPercent,
            onValueChange = { tipPercent = it },
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth()
        )

        RoundTheTip(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier
                .padding(bottom = 30.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                //₹
                text = stringResource(R.string.tip_amount, tip),
                fontWeight = FontWeight.W500,
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(
            modifier = Modifier.height(150.dp)
        )
    }
}


@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    TextField(
        label = { Text(stringResource(label)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        /*colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFF3C6B65)
        )*/
    )
}

@Composable
fun RoundTheTip(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.Round_up_tip)
        )
        Switch(
            checked = roundUp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            onCheckedChange = onRoundUpChanged
        )
    }
}

@VisibleForTesting
internal fun calculateTip(
    amount: Double,
    tipPercent: Double,
    roundUp: Boolean
): String {
    val indLocale = java.util.Locale("en", "IN")
    var tip = tipPercent / 100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance(indLocale).format(tip)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculatorTheme {
        TiptimeLayout()
    }
}