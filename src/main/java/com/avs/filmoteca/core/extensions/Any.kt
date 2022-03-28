package com.avs.filmoteca.core.extensions

import com.google.gson.Gson

fun Any.toJson(): String = Gson().toJson(this)