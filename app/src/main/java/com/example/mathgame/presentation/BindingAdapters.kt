package com.example.mathgame.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.mathgame.R
import com.example.mathgame.domain.entity.GameResult

interface OnOptionClickListener{
    fun onOptionClick(option: Int)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int){
    textView.text =
        String.format(
            textView.context.getString(R.string.required_answers),
            count
        )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int){
    textView.text =
        String.format(
            textView.context.getString(R.string.score_answers),
            count
        )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percent: Int){
    textView.text =
        String.format(
            textView.context.getString(R.string.required_percentage),
            percent
        )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult){
    textView.text =
        String.format(
            textView.context.getString(R.string.score_percentage),
            getPercentOfRightAnswers(gameResult)
        )
}

private fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult){
    if (countOfQuestion == 0){
        0
    }
    else{
        ((countOfRightAnswers / countOfQuestion.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("emojiResult")
fun bindEmojiResult(imageView: ImageView, gameResult: GameResult){
    imageView.setImageResource(getSmileResId(gameResult))
}

private fun getSmileResId(gameResult: GameResult): Int{
    val smileId =
        if (gameResult.winner){
            R.drawable.good_smile
        }
        else{
            R.drawable.bad_smile
        }
    return smileId
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, enough: Boolean){
    textView.setTextColor(getColorByState(textView.context,enough))
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, enough: Boolean){
    val color = getColorByState(progressBar.context, enough)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

private fun getColorByState(context: Context, goodState: Boolean): Int {
    val colorResId =
        if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int){
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener){
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}




