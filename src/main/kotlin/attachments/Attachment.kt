package attachments

import java.time.LocalDateTime

abstract class Attachment(
    val id: Int,
    val title: String,
    val type: String,
    val date: LocalDateTime
)

class AttachmentPhoto(
    id: Int,
    title: String,
    val photoData: Photo,
    date: LocalDateTime
) : Attachment(id, title, "Photo", date)

data class Photo(
    val id: Int,
    val title: String,
    val URL: String
)

class AttachmentVideo(
    id: Int,
    title: String,
    val videoData: Video,
    date: LocalDateTime
) : Attachment(id, title, "Video", date)

data class Video(
    val id: Int,
    val title: String,
    val URL: String
)

class AttachmentAudio(
    id: Int,
    title: String,
    val audioData: Audio,
    date: LocalDateTime
) : Attachment(id, title, "Audio", date)

data class Audio(
    val id: Int,
    val title: String,
    val URL: String
)

class AttachmentDocument(
    id: Int,
    title: String,
    val documentData: Document,
    date: LocalDateTime
) : Attachment(id, title, "Document", date)

data class Document(
    val id: Int,
    val title: String,
    val URL: String,
    val size: Long
)

class AttachmentLink(
    id: Int,
    title: String,
    val linkData: Link,
    date: LocalDateTime
) : Attachment(id, title, "Link", date)

data class Link(
    val id: Int,
    val title: String,
    val URL: String,
    val description: String
)