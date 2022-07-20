package com.activity.workingwithmediafiles

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var medialPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playBtn = findViewById<FloatingActionButton>(R.id.playBtn)
        val pauseBtn = findViewById<FloatingActionButton>(R.id.pauseBtn)
        val stopBtn = findViewById<FloatingActionButton>(R.id.stopBtn)
        seekBar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())
        playBtn.setOnClickListener {
            if(medialPlayer == null){
                medialPlayer = MediaPlayer.create(this,R.raw.applauding)
                initializeSeekBar()
            }
            medialPlayer?.start()
        }
        pauseBtn.setOnClickListener {
            medialPlayer?.pause()
        }
        stopBtn.setOnClickListener {
            medialPlayer?.stop()
            medialPlayer?.reset()
            medialPlayer?.release()
            medialPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }


    }

    private fun initializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) medialPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        val tvPDue = findViewById<TextView>(R.id.tvDue)



        seekBar.max = medialPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = medialPlayer!!.currentPosition

            val playedTime = medialPlayer!!.currentPosition/1000
            tvPlayed.text = "$playedTime sec"
            val duration = medialPlayer!!.duration/1000
            val dueTime = duration - playedTime
            tvPDue.text = "$dueTime sec"

            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
    }
}