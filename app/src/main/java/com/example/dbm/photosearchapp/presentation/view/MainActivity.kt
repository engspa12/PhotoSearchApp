package com.example.dbm.photosearchapp.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dbm.photosearchapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, PhotosGridFragment())
                .commit()
        }

    }
}