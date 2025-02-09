package org.thefproject.marpeh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var resultField: TextView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val history = mutableListOf<String>()

    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultField = findViewById(R.id.resultField)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyAdapter = HistoryAdapter(history)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        val buttons = listOf(
            findViewById<Button>(R.id.button0),
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button2),
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button8),
            findViewById<Button>(R.id.button9),
            findViewById<Button>(R.id.buttonDot),
            findViewById<Button>(R.id.buttonAdd),
            findViewById<Button>(R.id.buttonSub),
            findViewById<Button>(R.id.buttonMul),
            findViewById<Button>(R.id.buttonDiv),
            findViewById<Button>(R.id.buttonSqrt),
            findViewById<Button>(R.id.buttonPow),
            findViewById<Button>(R.id.buttonClear),
            findViewById<Button>(R.id.buttonEquals)
        )

        buttons.forEach { button ->
            button.setOnClickListener { onButtonClick(button.text.toString()) }
        }
    }

    private fun onButtonClick(value: String) {
        when (value) {
            "C" -> resultField.text = ""
            "=" -> calculateResult()
            "+" -> performOperation(value)
            "-" -> performOperation(value)
            "*" -> performOperation(value)
            "/" -> performOperation(value)
            "√" -> calculateSqrt()
            "^" -> calculatePow()
            else -> resultField.append(value)
        }
    }

    private fun performOperation(operation: String) {
        val value = resultField.text.toString().toDoubleOrNull()
        if (value != null) {
            if (operand1 == null) {
                operand1 = value
            } else {
                operand1 = when (pendingOperation) {
                    "=" -> value
                    "+" -> operand1!! + value
                    "-" -> operand1!! - value
                    "*" -> operand1!! * value
                    "/" -> operand1!! / value
                    else -> value
                }
            }
        }
        resultField.text = ""
        pendingOperation = operation
    }

    private fun calculateResult() {
        performOperation(pendingOperation)
        operand1?.let {
            resultField.text = it.toString()
            history.add("${operand1.toString()} $pendingOperation ${resultField.text} = ${it.toInt()}")
            historyAdapter.notifyDataSetChanged()
            operand1 = null
        }
    }

    private fun calculateSqrt() {
        val number = resultField.text.toString().toDoubleOrNull()
        number?.let {
            val result = sqrt(it)
            resultField.text = result.toString()
            history.add("√$number = $result")
            historyAdapter.notifyDataSetChanged()
        }
    }

    private fun calculatePow() {
        val number = resultField.text.toString().toDoubleOrNull()
        number?.let {
            val result = it.pow(2) // Пример возведения в степень 2
            resultField.text = result.toString()
            history.add("$number^2 = $result")
            historyAdapter.notifyDataSetChanged()
        }
    }
}
