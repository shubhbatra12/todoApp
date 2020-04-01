package com.example.todonew

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_task.dateEdt
import kotlinx.android.synthetic.main.activity_task.imgAddCategory
import kotlinx.android.synthetic.main.activity_task.saveBtn
import kotlinx.android.synthetic.main.activity_task.spinnerCategory
import kotlinx.android.synthetic.main.activity_task.taskInpLay
import kotlinx.android.synthetic.main.activity_task.timeEdt
import kotlinx.android.synthetic.main.activity_task.timeInptlay
import kotlinx.android.synthetic.main.activity_task.titleInpLay
import kotlinx.android.synthetic.main.addcat_dialog.view.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EditTaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    var finalDate = 0L
    var finalTime = 0L

    val labels = arrayListOf(
        "Personal", "Business", "Insurance", "Shopping", "Banking"
    )

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        dateEdit.setOnClickListener(this)
        timeEdit.setOnClickListener(this)
        applyBtn.setOnClickListener(this)
        imgAddCategoryEdit.setOnClickListener(this)

        val title :String = intent.getStringExtra("Title")
        val task: String = intent.getStringExtra("Task")
        val time = intent.getStringExtra("Time")
        val date = intent.getStringExtra("Date")


        titleInpLayEdit.editText?.text?.append(title)
        taskInpLayEdit.editText?.text?.append(task)
        dateEdit.setText(date)
        timeEdit.setText(time)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels)

        labels.sort()
        spinnerCategoryEdit.adapter = adapter
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.dateEdit -> {
                setListener()
            }
            R.id.timeEdit -> {
                setTimeListener()
            }
            R.id.applyBtn -> {
                saveTodo()
            }
            R.id.imgAddCategoryEdit -> {

                openDialog(labels)
            }
        }
    }

    private fun openDialog(list : ArrayList<String>) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.addcat_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Category Add Dialog")
        val  mAlertDialog = mBuilder.show()

        mDialogView.dialogSaveBtn.setOnClickListener {
            mAlertDialog.dismiss()
            val name = mDialogView.addCatText.text.toString()
            list.add(name)
        }
        mDialogView.dialogCancelBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }



    private fun saveTodo() {
        val category = spinnerCategoryEdit.selectedItem.toString()
        val title = titleInpLayEdit.editText?.text.toString()
        val description = taskInpLayEdit.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insertTask(
                    TodoModel(
                        title,
                        description,
                        category,
                        finalDate,
                        finalTime
                    )
                )
            }
            finish()
        }
    }

    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay : Int , min :Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, min)

            updateTime()
        }
        val timePickerDialog = TimePickerDialog(
            this,timeSetListener,myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat)  // it doesnt have to be converted into time ?
        finalTime = myCalendar.time.time
        timeEdit.setText(sdf.format(myCalendar.time))

    }

    private fun setListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateDate()
        }
        val datePickerDialog = DatePickerDialog(
            this,dateSetListener,myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myFormat = "EEE, d MMM YYYY"
        val sdf = SimpleDateFormat(myFormat)
        finalDate = myCalendar.time.time
        dateEdit.setText(sdf.format(myCalendar.time))
    }

}

