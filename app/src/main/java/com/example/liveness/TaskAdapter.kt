package com.example.liveness

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import com.us47codex.liveness_detection.tasks.DetectionTask


class TaskAdapter(private val dataSet: List<*>, mContext: Context) :
    ArrayAdapter<Any?>(mContext, R.layout.adapter_task, dataSet) {
    private class ViewHolder {
        lateinit var txtName: AppCompatTextView
        lateinit var checkBox: AppCompatCheckBox
    }

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(position: Int): DetectionTask {
        return dataSet[position] as DetectionTask
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        val result: View
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView =
                LayoutInflater.from(parent.context).inflate(R.layout.adapter_task, parent, false)
            viewHolder.txtName =
                convertView.findViewById(R.id.txtTask)
            viewHolder.checkBox =
                convertView.findViewById(R.id.checkBox)
            result = convertView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            result = convertView
        }

        val item: DetectionTask = getItem(position)
        viewHolder.txtName.text = item.taskDescription()
        viewHolder.checkBox.isChecked = item.isTaskPassed()
        return result
    }
}