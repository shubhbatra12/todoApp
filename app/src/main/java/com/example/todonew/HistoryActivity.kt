package com.example.todonew

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
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
                GlobalScope.launch(Dispatchers.IO) {
                    db.todoDao().deleteAllFinishedTasks()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
