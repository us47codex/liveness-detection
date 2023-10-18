package com.us47codex.liveness_detection.utils

import android.content.Intent
import android.util.Log


object AppLog {
    const val isDebug = true
    const val showLog = isDebug

    //    public static final boolean showLog = G5.isLogging;
    const val showEncryptDecrypt = false
    fun log(tag: String?, text: String?) {
        var tag = tag
        var text = text
        if (showLog) {
            if (tag == null) tag = ""
            if (text == null) text = ""
            if (tag.length > 22) { //new max tag length is 22 otherwise log wont show anything
                tag = pickFirstToNthCharacter(tag, 22)
            }
            if (text.length > 4000) {
                Log.d(tag, "-" + text.substring(0, 4000))
                log(tag, text.substring(4000))
            } else {
                Log.d(tag, "-$text")
            }
        }
    }

    fun logE(tag: String?, text: String?) {
        var tag = tag
        var text = text
        if (showLog) {
            if (tag == null) tag = ""
            if (text == null) text = ""
            if (tag.length > 22) { //new max tag length is 22 otherwise log wont show anything
                tag = pickFirstToNthCharacter(tag, 22)
            }
            if (text.length > 4000) {
                Log.e(tag, "-" + text.substring(0, 4000))
                log(tag, text.substring(4000))
            } else {
                Log.e(tag, "-$text")
            }
        }
    }

    fun logE(tag: String?, e: Exception) {
        var tag = tag
        if (showLog) {
            if (tag == null) tag = ""
            Log.e(tag, "-" + e.localizedMessage)
            e.printStackTrace()
        }
    }

    fun logBR(tag: String?, intent: Intent?) {
        var tag = tag
        if (showLog) { // "IMEI":"11aa9fa9255370c4"
            if (tag == null) tag = ""
            if (intent == null) return

            //new max tag length is 22 otherwise log wont show anything
            if (tag.length > 22) tag = pickFirstToNthCharacter(tag, 22)
            var brMethod = intent.getStringExtra("method")
            if (brMethod == null) brMethod = ""
            log(tag, "BR onReceive:: Method: $brMethod")
            if (intent.hasExtra("data2")) {
                log(tag, "BR onReceive:: data2: " + intent.getStringExtra("data2"))
            }
            if (intent.hasExtra("data3")) {
                log(tag, "BR onReceive:: data3: " + intent.getStringExtra("data3"))
            }
            if (intent.hasExtra("data4")) {
                log(tag, "BR onReceive:: data4: " + intent.getStringExtra("data4"))
            }
        }
    }

    fun pickFirstToNthCharacter(text: String?, length: Int): String {
        return if (text == null) { //must check for null
            ""
        } else {
            if (text.length < length) { //might input a length which is greater than text length
                text
            } else {
                text.substring(0, length)
            }
        }
    }

    //Path to log file: "Android/data/PACKAGE_NAME/files".
    fun logToFile(tag: String, text: String) {
        log(tag, text)
        /*val path = applicationContext1?.getExternalFilesDir("")
//        val logFile = File(path, "log.txt")
        val logFile = File(applicationContext1?.cacheDir, "log.txt")
        val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS | ", Locale.getDefault())
        val logLine: String = formatter.format(Date()) + tag + " | " + text
        try {
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append(logLine)
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }*/
    }
}