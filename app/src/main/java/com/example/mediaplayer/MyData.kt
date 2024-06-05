package com.example.mediaplayer

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData

object MyData {
    var musicPosition = MutableLiveData<String>()
    var p: Int? = null
    var music: Music? = null
    var mediaPlayer: MediaPlayer? = null
    var list = ArrayList<Music>()

}