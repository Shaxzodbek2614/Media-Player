package com.example.mediaplayer

import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediaplayer.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    lateinit var mediaPlayer: MediaPlayer
    lateinit var handler: Handler
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())
        //mediaPlayer = MediaPlayer.create(this,R.raw.bethoven)
        val list = musicFiles()
      Log.d(TAG, "onCreate: $list")
        println("salom")
        mediaPlayer = MediaPlayer.create(this, Uri.parse("/storage/emulated/0/Music/telegram_afgana_muz_avara_avara_98.mp3"))
        mediaPlayer.start()

       binding.btnPlay.setOnClickListener {
           if (mediaPlayer.isPlaying){
               mediaPlayer.pause()
               binding.btnPlay.text = "Play"
           }else{
               mediaPlayer.start()
               binding.btnPlay.text = "Pause"
           }
       }

        binding.seekBar.max = mediaPlayer.duration
        handler.postDelayed(runnable,1000)
        binding.musicDuration.text = millisToTime(mediaPlayer.duration)
        binding.seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
               // mediaPlayer.seekTo(binding.seekBar.progress)
            }
        })

    }
    val runnable = object :Runnable {
        override fun run() {
            binding.musicPosition.text = millisToTime(mediaPlayer.currentPosition)
            binding.seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(this,1000)
        }
    }
    fun millisToTime(millis:Int):String{
        val minut = millis/60000
        val sekund = millis%60000/1000
        return "$minut:$sekund"
    }
    fun Context.musicFiles():MutableList<Music>{
        val list:MutableList<Music> = mutableListOf()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor: Cursor? = this.contentResolver.query(
            uri,
            null,
            selection,
            null,
            sortOrder
        )

        if (cursor!= null && cursor.moveToFirst()){
            val id:Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title:Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val imageId:Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
            val authorId:Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)

            do {
                val audioId:Long = cursor.getLong(id)
                val audioTitle:String = cursor.getString(title)
                var imagePath = ""
                if (imageId !=-1){
                    imagePath = cursor.getString(imageId)
                }
                val musicPath:String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val artist = cursor.getString(authorId)

                list.add(Music(audioId,audioTitle, imagePath, musicPath, artist))
            }while (cursor.moveToNext())
        }
        return  list
    }
}