package com.us47codex.liveness_detection.tasks

import com.google.mlkit.vision.face.Face
import com.us47codex.liveness_detection.utils.DetectionUtils
import com.us47codex.liveness_detection.tasks.DetectionTask

class MouthOpenDetectionTask : DetectionTask {

    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "MouthOpenDetection"
    }

    override fun taskDescription(): String {
        return "Please open your mouth"
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        return DetectionUtils.isFacing(face) && DetectionUtils.isMouthOpened(face)
    }
}