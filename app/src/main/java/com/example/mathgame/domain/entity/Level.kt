package com.example.mathgame.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
//Уровни сложности игры
enum class Level:Parcelable{
    TEST, EASY, NORMAL, HARD
}