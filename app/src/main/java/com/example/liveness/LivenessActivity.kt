package com.example.liveness

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat

import com.example.liveness.databinding.ActivityLivenessBinding
import com.us47codex.liveness_detection.FaceAnalyzer
import com.us47codex.liveness_detection.LivenessDetector
import com.us47codex.liveness_detection.tasks.DetectionTask
import com.us47codex.liveness_detection.tasks.EyesBlinkDetectionTask
import com.us47codex.liveness_detection.tasks.FacingDetectionTask
import com.us47codex.liveness_detection.tasks.MouthOpenDetectionTask
import com.us47codex.liveness_detection.tasks.ShakeDetectionTask
import com.us47codex.liveness_detection.tasks.SmileDetectionTask
import java.io.File

open class LivenessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLivenessBinding
    private lateinit var cameraController: LifecycleCameraController
    private var imageFiles = arrayListOf<String>()
    private lateinit var adapter: TaskAdapter
    private var livenessDetector = LivenessDetector(
        FacingDetectionTask(),
        EyesBlinkDetectionTask(),
        ShakeDetectionTask(),
        MouthOpenDetectionTask(),
        SmileDetectionTask()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationContext1 = this.applicationContext
        binding = ActivityLivenessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { finish() }
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                Toast.makeText(this, "Permission deny", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.launch(Manifest.permission.CAMERA)

        binding.cameraPreview.clipToOutline = true
        binding.cameraPreview.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, view.height / 2.0f)
            }
        }
        binding.name.text = name
        adapter = TaskAdapter(livenessDetector.getTasks(), applicationContext)
        binding.lst.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun startCamera() {
        cameraController = LifecycleCameraController(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(this),
            FaceAnalyzer(buildLivenessDetector())
        )
        cameraController.bindToLifecycle(this)
        binding.cameraPreview.controller = cameraController

        binding.cameraSwitch.setOnClickListener {
            cameraController.cameraSelector =
                if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    private fun buildLivenessDetector(): LivenessDetector {
        val listener = object : LivenessDetector.Listener {
            @SuppressLint("SetTextI18n")
            override fun onTaskStarted(task: DetectionTask) {
                binding.guide.text = task.taskDescription()
            }

            override fun onTaskCompleted(task: DetectionTask, isLastTask: Boolean) {
                adapter?.notifyDataSetChanged()
                takePhoto(
                    File(
                        cacheDir,
                        "${name + "_" + task.taskName() + "_" + System.currentTimeMillis()}.jpg"
                    )
                ) {
                    imageFiles.add(it.absolutePath)
                    if (isLastTask) {
                        finishForResult()
                    }
                }
            }

            override fun onTaskFailed(task: DetectionTask, code: Int) {
                adapter?.notifyDataSetChanged()
                when (code) {
                    LivenessDetector.ERROR_MULTI_FACES -> {
                        Toast.makeText(
                            this@LivenessActivity,
                            "Please make sure there is only one face on the screen.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    LivenessDetector.ERROR_NO_FACE -> {
                        Toast.makeText(
                            this@LivenessActivity,
                            "Please make sure there is a face on the screen.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    LivenessDetector.ERROR_OUT_OF_DETECTION_RECT -> {
                        Toast.makeText(
                            this@LivenessActivity,
                            "Please make sure there is a face in the Rectangle.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> {
                        Toast.makeText(
                            this@LivenessActivity,
                            "${task.taskName()} Failed.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return livenessDetector.also { it.setListener(listener) }
    }

    private fun finishForResult() {
        val result = ArrayList(imageFiles.takeLast(livenessDetector.getTaskSize()))
        setResult(RESULT_OK, Intent().putStringArrayListExtra(ResultContract.RESULT_KEY, result))
        finish()
    }


    private fun takePhoto(file: File, onSaved: (File) -> Unit) {
        cameraController.takePicture(
            ImageCapture.OutputFileOptions.Builder(file).build(),
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    e.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    onSaved(file)
                }
            }
        )
    }

    class ResultContract : ActivityResultContract<Any?, List<String>?>() {

        companion object {
            const val RESULT_KEY = "images"
        }

        override fun createIntent(context: Context, input: Any?): Intent {
            return Intent(context, LivenessActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): List<String>? {
            if (resultCode == RESULT_OK && intent != null) {
                return intent.getStringArrayListExtra(RESULT_KEY)
            }
            return null
        }
    }

    companion object {
        var applicationContext1: Context? = null
        var name: String = ""
    }
}