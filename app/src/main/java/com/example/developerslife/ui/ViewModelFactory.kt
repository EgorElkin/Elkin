package com.example.developerslife.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.developerslife.data.PostRepository
import com.example.developerslife.data.remote.RemoteDataSource
import com.example.developerslife.ui.latest.LatestViewModel
import com.example.developerslife.ui.random.RandomViewModel
import com.example.developerslife.ui.top.TopViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LatestViewModel::class.java) -> {
                LatestViewModel(PostRepository(RemoteDataSource())) as T
            }
            modelClass.isAssignableFrom(TopViewModel::class.java) -> {
                TopViewModel(PostRepository(RemoteDataSource())) as T
            }
            modelClass.isAssignableFrom(RandomViewModel::class.java) -> {
                RandomViewModel(PostRepository(RemoteDataSource())) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown class name")
            }
        }
    }

}