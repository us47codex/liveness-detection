package com.us47codex.liveness_detection.tasks

import com.google.mlkit.vision.face.Face
import com.us47codex.liveness_detection.utils.DetectionUtils

class FacingDetectionTask : DetectionTask {

    companion object {
        private const val FACING_CAMERA_KEEP_TIME = 1500L
    }

    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "FacingDetection"
    }

    override fun taskDescription(): String {
        return "Please squarely facing the camera"
    }

    private var startTime = 0L

    override fun start() {
        startTime = System.currentTimeMillis()
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        if (!DetectionUtils.isFacing(face)) {
            startTime = System.currentTimeMillis()
            return false
        }
        return System.currentTimeMillis() - startTime >= FACING_CAMERA_KEEP_TIME
    }
}