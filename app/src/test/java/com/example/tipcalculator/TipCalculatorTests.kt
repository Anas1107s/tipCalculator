package com.example.tipcalculator

import com.anas.tipcalculator.calculateTip
import org.junit.Test
import java.text.NumberFormat
import org.junit.Assert.assertEquals

class TipCalculatorTests {

    @Test
    fun calculateTip_20PercentNoRounUp(

    ) {
        val indLocale = java.util.Locale("en", "IN")
        val amount = 10.0
        val percent = 20.0
        val expectedTip = NumberFormat.getCurrencyInstance(indLocale).format(2)
        val actualTip = calculateTip(amount = amount , tipPercent = percent , false)
        assertEquals(expectedTip , actualTip)
    }
}