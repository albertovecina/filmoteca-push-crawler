package com.avs.filmoteca.core.util

import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object Logger {

    val logger: Logger by lazy {
        Logger.getLogger("com.avs.filmoteca").apply {
            addHandler(FileHandler("filmoteca.log", true).apply {
                formatter = SimpleFormatter()
            })
        }
    }

}