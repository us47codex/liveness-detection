package com.us47codex.liveness_detection.tasks

import com.google.mlkit.vision.face.Face

interface DetectionTask {

    var isTaskCompleted: Boolean

    fun taskName(): String {
        return "Detection"
    }

    fun taskDescription(): String {
        return ""
    }

    fun isTaskPassed(): Boolean {
        return isTaskCompleted
    }

    fun start() {}

    /**
     * @return ture if task completed
     */
    fun process(face: Face, timestamp: Long): Boolean
}