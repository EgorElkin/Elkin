package com.example.developerslife.ui.top

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.developerslife.R
import com.example.developerslife.model.Post
import com.example.developerslife.model.Result
import com.example.developerslife.ui.ViewModelFactory

class TopFragment: Fragment(R.layout.fragment_page) {
    private var progressBar: ProgressBar? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private var buttonForward: Button? = null
    private var buttonBack: Button? = null
    private var textViewError: TextView? = null
    private var buttonRetry: Button? = null

    private lateinit var viewModel: TopViewModel

    private var currentState: Result.Status = Result.Status.LOADING

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        setUpObservers()
        setButtonListeners()
    }

    private fun initUI(rootView: View) {
        progressBar = rootView.findViewById(R.id.progressBar)
        imageView = rootView.findViewById(R.id.imageView)
        imageView.visibility = View.INVISIBLE
        textView = rootView.findViewById(R.id.textViewDescription)
        textView.visibility = View.INVISIBLE
        buttonForward = rootView.findViewById(R.id.buttonForward)
        buttonBack = rootView.findViewById(R.id.buttonBack)
        textViewError = rootView.findViewById(R.id.textViewError)
        textViewError?.visibility = View.INVISIBLE
        buttonRetry = rootView.findViewById(R.id.buttonRetry)
        buttonRetry?.visibility = View.INVISIBLE
        viewModel = ViewModelProvider(this, ViewModelFactory()).get(TopViewModel::class.java)
    }

    private fun setUpObservers(){
        viewModel.state.observe(this.viewLifecycleOwner) { newState ->
            when (currentState) {
                Result.Status.LOADING -> {
                    when (newState) {
                        Result.Status.SUCCESS -> {
                            transition12(viewModel.postsList[viewModel.currentPosition])
                        }
                        Result.Status.ERROR -> {
                            transition13()
                        }
                        else -> { }
                    }
                }
                Result.Status.SUCCESS -> {
                    when (newState) {
                        Result.Status.LOADING -> {
                            transition21()
                        }
                        else -> { }
                    }
                }
                Result.Status.ERROR -> {
                    when (newState) {
                        Result.Status.LOADING -> {
                            transition31()
                        }
                        else -> { }
                    }
                }
            }
            currentState = newState
        }
    }

    private fun setButtonListeners() {
        buttonForward?.setOnClickListener {
            viewModel.forwardPushed()
        }
        buttonBack?.setOnClickListener {
            viewModel.backPushed()
        }
        buttonRetry?.setOnClickListener {
            viewModel.retryPushed()
        }
    }

    private fun transition12(newPost: Post) {
        buttonForward?.visibility = View.VISIBLE
        buttonForward?.isEnabled = true
        buttonBack?.visibility = View.VISIBLE
        buttonBack?.isEnabled = viewModel.isFirst == false
        textView.visibility = View.VISIBLE
        textView.text = newPost.description

        Glide.with(imageView)
            .load(newPost.gifURL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    showToast(getString(R.string.glide_load_error_message))
                    progressBar?.visibility = View.INVISIBLE
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    progressBar?.visibility = View.INVISIBLE
                    imageView.visibility = View.VISIBLE
                    return false
                }

            })
            .error(R.drawable.baseline_broken_image_24)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(imageView)

    }

    private fun transition13() {
        progressBar?.visibility = View.INVISIBLE

        buttonForward?.visibility = View.VISIBLE
        buttonBack?.visibility = View.VISIBLE
        buttonForward?.isEnabled = false
        buttonBack?.isEnabled = viewModel.isFirst == false
        textViewError?.visibility = View.VISIBLE
        buttonRetry?.visibility = View.VISIBLE
    }

    private fun transition21() {
        imageView.visibility = View.INVISIBLE
        textView.visibility = View.INVISIBLE
        buttonForward?.visibility = View.INVISIBLE
        buttonBack?.visibility = View.INVISIBLE

        progressBar?.visibility = View.VISIBLE
    }

    private fun transition31() {
        textViewError?.visibility = View.INVISIBLE
        buttonRetry?.visibility = View.INVISIBLE
        buttonForward?.visibility = View.INVISIBLE
        buttonBack?.visibility = View.INVISIBLE

        progressBar?.visibility = View.VISIBLE
    }

    private fun showToast(message: String){
        val toast = Toast.makeText(this.context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}