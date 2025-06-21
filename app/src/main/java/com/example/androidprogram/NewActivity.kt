package com.example.androidprogram

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprogram.database.*
import com.example.androidprogram.ui.activity.ActivityTypeAdapter
import com.example.androidprogram.ui.activity.ActivityTypeItem
import com.example.androidprogram.UserManager
import kotlinx.coroutines.launch
import java.util.Locale

class NewActivity : AppCompatActivity() {

    private lateinit var activityDao: ActivityDao
    private lateinit var activityTypeAdapter: ActivityTypeAdapter
    private lateinit var userManager: UserManager

    private var selectedActivityType: ActivityType = ActivityType.BICYCLE
    private var startTime: Long = 0L
    private var isPaused = false
    private var timeWhenStopped: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timerRunnable: Runnable

    // Views
    private lateinit var layoutBeforeStart: LinearLayout
    private lateinit var layoutInProgress: LinearLayout
    private lateinit var activityTypeRecycler: RecyclerView
    private lateinit var startButton: Button
    private lateinit var inProgressActivityType: TextView
    private lateinit var inProgressTimer: TextView
    private lateinit var pauseButton: ImageButton
    private lateinit var finishButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        activityDao = AppDatabase.getDatabase(this).activityDao()
        userManager = UserManager(this)

        // Initialize Views
        layoutBeforeStart = findViewById(R.id.layout_before_start)
        layoutInProgress = findViewById(R.id.layout_in_progress)
        activityTypeRecycler = findViewById(R.id.activity_type_recycler)
        startButton = findViewById(R.id.start_button)
        inProgressActivityType = findViewById(R.id.in_progress_activity_type)
        inProgressTimer = findViewById(R.id.in_progress_timer)
        pauseButton = findViewById(R.id.pause_button)
        finishButton = findViewById(R.id.finish_button)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val types = listOf(
            ActivityTypeItem("Велосипед", ActivityType.BICYCLE),
            ActivityTypeItem("Бег", ActivityType.RUNNING),
            ActivityTypeItem("Шаг", ActivityType.STEP)
        )
        activityTypeAdapter = ActivityTypeAdapter(types) { type ->
            selectedActivityType = type
        }
        activityTypeRecycler.adapter = activityTypeAdapter
        activityTypeRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupClickListeners() {
        startButton.setOnClickListener {
            startActivity()
        }

        pauseButton.setOnClickListener {
            if (isPaused) resumeActivity() else pauseActivity()
        }

        finishButton.setOnClickListener {
            finishActivity()
        }
    }

    private fun startActivity() {
        startTime = SystemClock.elapsedRealtime()
        selectedActivityType = activityTypeAdapter.getSelectedActivityType()

        layoutBeforeStart.visibility = View.GONE
        layoutInProgress.visibility = View.VISIBLE
        inProgressActivityType.text = when (selectedActivityType) {
            ActivityType.BICYCLE -> "Велосипед"
            ActivityType.RUNNING -> "Бег"
            ActivityType.STEP -> "Шаг"
        }

        startTimer()
    }

    private fun pauseActivity() {
        isPaused = true
        pauseButton.setImageResource(R.drawable.ic_play_arrow)
        timeWhenStopped = SystemClock.elapsedRealtime() - startTime
        handler.removeCallbacks(timerRunnable)
    }

    private fun resumeActivity() {
        isPaused = false
        pauseButton.setImageResource(R.drawable.ic_pause)
        startTime = SystemClock.elapsedRealtime() - timeWhenStopped
        startTimer()
    }

    private fun finishActivity() {
        handler.removeCallbacks(timerRunnable)
        val endTime = System.currentTimeMillis()
        val realStartTime = endTime - (SystemClock.elapsedRealtime() - startTime)

        lifecycleScope.launch {
            val currentUser = userManager.getCurrentUser()
            val newActivity = ActivityEntity(
                userId = currentUser?.login ?: "",
                activityType = selectedActivityType,
                startTime = realStartTime,
                endTime = endTime,
                coordinates = emptyList() // Пока без координат
            )
            activityDao.insertActivity(newActivity)
            Toast.makeText(this@NewActivity, "Активность сохранена!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun startTimer() {
        timerRunnable = object : Runnable {
            override fun run() {
                val elapsedMillis = SystemClock.elapsedRealtime() - startTime
                val seconds = (elapsedMillis / 1000) % 60
                val minutes = (elapsedMillis / (1000 * 60)) % 60
                val hours = (elapsedMillis / (1000 * 60 * 60))

                inProgressTimer.text = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)

                handler.postDelayed(this, 1000)
            }
        }
        handler.post(timerRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
    }
} 