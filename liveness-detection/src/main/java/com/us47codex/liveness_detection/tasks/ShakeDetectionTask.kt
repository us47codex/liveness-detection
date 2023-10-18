package com.us47codex.liveness_detection.tasks

import com.google.mlkit.vision.face.Face

class ShakeDetectionTask : DetectionTask {

    companion object {
        private const val SHAKE_THRESHOLD = 18f
    }

    private var hasShakeToLeft = false
    private var hasShakeToRight = false


    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "ShakeDetection"
    }

    override fun taskDescription(): String {
        return "Please slowly shake your head left or right"
    }

    override fun start() {
        hasShakeToLeft = false
        hasShakeToRight = false
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val yaw = face.headEulerAngleY
        if (yaw > SHAKE_THRESHOLD && !hasShakeToLeft) {
            hasShakeToLeft = true
        } else if (yaw < -SHAKE_THRESHOLD && !hasShakeToRight) {
            hasShakeToRight = true
        }
        return hasShakeToLeft || hasShakeToRight
    }
}