package com.clinerds.mediagrab.database.backup

import com.clinerds.mediagrab.database.objects.CommandTemplate
import com.clinerds.mediagrab.database.objects.DownloadedVideoInfo
import com.clinerds.mediagrab.database.objects.OptionShortcut
import kotlinx.serialization.Serializable

@Serializable
data class Backup(
    val templates: List<CommandTemplate>? = null,
    val shortcuts: List<OptionShortcut>? = null,
    val downloadHistory: List<DownloadedVideoInfo>? = null,
)
