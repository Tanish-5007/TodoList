package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.view.isVisible
import com.example.todolist.databinding.ActivityTaskBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var myCalender: Calendar
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private var finalDate = 0L
    private var finalTime = 0L

    private val labels = arrayListOf("Personal", "Business", "Insurance", "Shopping", "Banking")

    private val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateEdt.setOnClickListener {
            setDateListener()
        }
        binding.timeEdt.setOnClickListener {
            setTimeListener()
        }
        binding.btnSave.setOnClickListener {
            saveTodo()
        }

        setUpSpinner()

    }

    @DelicateCoroutinesApi
    private fun saveTodo() {
        val category = binding.spinnerCategory.selectedItem.toString()
        val title = binding.titleInpLay.editText.toString()
        val description = binding.taskInpLay.editText?.text.toString()

        GlobalScope.launch(Dispatchers.IO) {
            db.todoDao().insertTask(TodoModel(title, description, category, finalDate, finalTime))
            finish()
        }



    }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)
        labels.sort()

        binding.spinnerCategory.adapter = adapter
    }

    private fun setTimeListener() {
        myCalender = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, min: Int ->
            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalender.set(Calendar.MINUTE, min)
            updateTime()
        }
        val timePickerDialog = TimePickerDialog(
            this, timeSetListener, myCalender.get(Calendar.HOUR_OF_DAY),
            myCalender.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()

    }

    private fun updateTime() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat)
        finalTime = myCalender.time.time
        binding.timeEdt.setText(sdf.format(myCalender.time))
    }

    private fun setDateListener() {
        myCalender = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this, dateSetListener, myCalender.get(Calendar.YEAR),
            myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()

    }

    private fun updateDate() {
        // Mon, 5 jan 2020
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        finalDate = myCalender.time.time
        binding.dateEdt.setText(sdf.format(myCalender.time))

        binding.timeInpLay.isVisible = true
    }


}