package com.example.todonew

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.delete_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    private val listHist = arrayListOf<TodoModel>()
    var adapter = HistoryAdapter(listHist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        historyRv.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = this@HistoryActivity.adapter
        }

        db.historyDao().getTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                listHist.clear()
                listHist.addAll(it)
                adapter.notifyDataSetChanged()
            } else {
                listHist.clear()
                adapter.notifyDataSetChanged()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearHistory -> {
                openDeleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDeleteDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Delete")
        val  mAlertDialog = mBuilder.show()

        mDialogView.dialogApplyBtnDel.setOnClickListener {
            mAlertDialog.dismiss()
            GlobalScope.launch(Dispatchers.IO) {
                db.todoDao().deleteAllFinishedTasks()
            }
        }
        mDialogView.dialogCancelBtnDel.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }
}
