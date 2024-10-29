package attachments

import java.time.LocalDateTime

sealed class Attachment(val type: String)

class AttachmentPhoto(val photoData: Photo) : Attachment("Photo")
data class Photo(
    val id: Int,
    val title: String,
    val url: String,
    val date: LocalDateTime
)

class AttachmentVideo(val videoData: Video) : Attachment("Video")
data class Video(
    val id: Int,
    val title: String,
    val url: String,
    val date: LocalDateTime
)

class AttachmentAudio(val audioData: Audio) : Attachment("Audio")
data class Audio(
    val id: Int,
    val title: String,
    val url: String,
    val date: LocalDateTime
)

class AttachmentDocument(val documentData: Document) : Attachment("Document")
data class Document(
    val id: Int,
    val title: String,
    val url: String,
    val date: LocalDateTime
)

class AttachmentLink(val linkData: Link) : Attachment("Link")
data class Link(
    val id: Int,
    val title: String,
    val url: String,
    val date: LocalDateTime
)

fun getTypeAttachment(attachment: Attachment): String {
    return when (attachment) {
        is AttachmentPhoto -> "Photo"
        is AttachmentVideo -> "Video"
        is AttachmentAudio -> "Audio"
        is AttachmentDocument -> "Document"
        is AttachmentLink -> "Link"
    }
}