package com.example.mediaplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
    lateinit var musicAdapter: MusicAdapter
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(MyData.music!=null){
            binding.name.text = MyData.music?.title
            binding.auothor.text = MyData.music?.author
        }

        val list = musicFiles()
        MyData.list = list
        if (MyData.mediaPlayer!=null && MyData.a==true){
            binding.btnPlay.setImageResource(R.drawable.pause)
        }
        binding.btnPlay.setOnClickListener {
            if (MyData.mediaPlayer!=null && MyData.mediaPlayer!!.isPlaying){
                MyData.mediaPlayer!!.pause()
                binding.btnPlay.setImageResource(R.drawable.play)
            }else if (MyData.mediaPlayer!=null && !MyData.mediaPlayer!!.isPlaying){
                MyData.mediaPlayer!!.start()
                binding.btnPlay.setImageResource(R.drawable.pause)
            }
        }

        Log.d(TAG, "onCreate: $list")
        musicAdapter = MusicAdapter(object :MusicAdapter.RvAction{
            override fun onClick(music: Music, position: Int) {
                val intent = Intent(this@MainActivity,MainActivity2::class.java)
                intent.putExtra("music",music)
                intent.putExtra("key",position)
                startActivity(intent)
                if (MyData.mediaPlayer!=null && !MyData.mediaPlayer!!.isPlaying) {
                    MyData.mediaPlayer = MediaPlayer.create(this@MainActivity, Uri.parse(list[position].musicPath))
                    MyData.mediaPlayer?.start()
                    MyData.a = true
                    MyData.music = music
                }else{
                    MyData.mediaPlayer?.stop()
                    MyData.mediaPlayer = MediaPlayer.create(this@MainActivity, Uri.parse(list[position].musicPath))
                    MyData.mediaPlayer?.start()
                    MyData.a = true
                    MyData.music = music
                }
                if (MyData.mediaPlayer!=null){
                    binding.name.text = MyData.music?.title
                    binding.auothor.text = MyData.music?.author
                }
                MyData.p = position
            }
        },list)
        binding.rv.adapter = musicAdapter

    }
    @SuppressLint("Range")
    fun Context.musicFiles():ArrayList<Music>{
        val list:ArrayList<Music> = ArrayList()
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