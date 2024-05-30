package com.example.mediaplayer

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediaplayer.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var handler:Handler

    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())
        val position = intent.getIntExtra("key",0)
        //mediaPlayer = MediaPlayer.create(this,R.raw.bethoven)
        val list = MyData.list
        var mediaPlayer = MyData.mediaPlayer


        binding.play.setOnClickListener {
            if (mediaPlayer!!.isPlaying){
                mediaPlayer!!.pause()
                binding.play.setImageResource(R.drawable.play)
            }else{
                mediaPlayer!!.start()
                binding.play.setImageResource(R.drawable.pause)
            }
        }
        binding.qaytish.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.seekBar.max = mediaPlayer!!.duration
        handler.postDelayed(runnable,1000)
        binding.musicDuration.text = millisToTime(mediaPlayer!!.duration)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // mediaPlayer.seekTo(binding.seekBar.progress)
            }
        })
        binding.musicTezOtish.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition?.plus(10000)?:0)
        }
        binding.musicTezQaytish.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition?.minus(10000)?:0)
        }
        binding.musicOtish.setOnClickListener {
            if (MyData.p!=list.size-1){
                println("salom")
                mediaPlayer!!.stop()
                mediaPlayer = MediaPlayer.create(this,Uri.parse(list[MyData.p!!+1].musicPath))
                binding.musicDuration.text = millisToTime(mediaPlayer!!.duration)
                MyData.music = MyData.list[MyData.p!!.plus(1)]
                binding.seekBar.max =MyData.mediaPlayer!!.duration
                handler.postDelayed(runnable,1000)
                MyData.p =MyData.p!!+1
                mediaPlayer!!.start()
            }else{
                Toast.makeText(this, "Oxiriga keldingiz", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val runnable = object :Runnable {
        override fun run() {
            val mediaPlayer = MyData.mediaPlayer
                binding.musicPosition.text = millisToTime(mediaPlayer!!.currentPosition)
                binding.seekBar.progress = mediaPlayer.currentPosition
                handler.postDelayed(this, 1000)

        }
    }
    fun millisToTime(millis:Int):String{
        val minut = millis/60000
        val sekund = millis%60000/1000
        return "$minut:$sekund"
    }
}