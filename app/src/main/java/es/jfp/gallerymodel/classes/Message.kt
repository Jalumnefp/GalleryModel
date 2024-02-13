package es.jfp.gallerymodel.classes

import java.time.LocalDateTime

data class Message(var id: String, var owner: String, var content: String, var date: LocalDateTime, var color: String)
