package com.example.todonew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import java.util.Observer as Observer1

class HistoryActivity : AppCompatActivity() {

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    private val listHist = arrayListOf<TodoModel>()
    var adapter = HistoryAdapter(listHist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

            historyRv.apply {
                layoutManager = LinearLayoutManager(this@HistoryActivity)
                adapter = this@HistoryActivity.adapter
            }

        db.historyDao().getTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                listHist.clear()
                listHist.addAll(it)
                adapter.notifyDataSetChanged()
            }else{
                listHist.clear()
                adapter.notifyDataSetChanged()
            }
        })

    }
}
