package com.example.developerslife.ui.latest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.developerslife.data.PostRepository
import com.example.developerslife.model.Post
import com.example.developerslife.model.Result
import com.example.developerslife.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LatestViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _state: MutableLiveData<Result.Status> = MutableLiveData(Result.Status.LOADING)
    val state: LiveData<Result.Status>
        get() = _state

    private var _isFirst: Boolean = true
    val isFirst: Boolean
        get() = _isFirst

    private val _postsList: MutableList<Post> = mutableListOf()
    val postsList: List<Post>
        get() = _postsList

    private var _currentPosition: Int = -1
    val currentPosition: Int
        get() = _currentPosition

    private var pageNumber: Int = 0

    init {
        loadNextLatestPosts()
    }

    fun forwardPushed() {
        if (_currentPosition >= _postsList.size - 1) {
            loadNextLatestPosts()
        } else {
            _state.value = Result.Status.LOADING
            nextPost()
        }
    }

    private fun nextPost() {
        _currentPosition++
        if (_currentPosition > 0) {
            _isFirst = false
        }
        _state.value = Result.Status.SUCCESS
    }

    fun backPushed() {
        if (_currentPosition > 0) {
            if (_state.value == Result.Status.ERROR) {
                _currentPosition++
            }
            _state.value = Result.Status.LOADING
            previousPost()
        }
    }

    private fun previousPost() {
        _currentPosition--
        if (_currentPosition == 0) {
            _isFirst = true
        }
        _state.value = Result.Status.SUCCESS
    }

    fun retryPushed() {
        loadNextLatestPosts()
    }

    private fun loadNextLatestPosts() {
        viewModelScope.launch(Dispatchers.Default) {
            postRepository.getLatestResult(pageNumber, Constants.PAGE_SIZE)
                .collect {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            _state.postValue(Result.Status.LOADING)
                        }
                        Result.Status.ERROR -> {
                            _state.postValue(Result.Status.ERROR)
                        }
                        Result.Status.SUCCESS -> {
                            if(it.data?.result.isNullOrEmpty() || it.data?.result?.size == 0){
                                _state.postValue(Result.Status.ERROR)
                            } else {
                                _postsList.addAll(it.data!!.result)
                                _currentPosition++
                                if (_currentPosition > 0) {
                                    _isFirst = false
                                }
                                _state.postValue(Result.Status.SUCCESS)
                                pageNumber++
                            }
                        }
                    }
                }
        }
    }
}