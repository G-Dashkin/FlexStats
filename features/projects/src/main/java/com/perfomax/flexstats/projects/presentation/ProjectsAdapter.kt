package com.perfomax.flexstats.projects.presentation

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.perfomax.flexstats.models.Project
import com.perfomax.projects.databinding.ItemProjectBinding

class ProjectsAdapter(
    private val itemProjectClick: (Int) -> Unit,
    private val editProjectClick: (Int, String) -> Unit,
    private val deleteProjectClick: (Int, String) -> Unit
): ListAdapter<Project, RecyclerView.ViewHolder>(ProjectsDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  ProjectHolder(binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) as Project
        val viewHolder = holder as ProjectHolder
        viewHolder.bind(item)
    }

    inner class ProjectHolder(private val binding: ItemProjectBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(project: Project) {
            binding.projectName.text = project.name
            if (project.isSelected == true) binding.rootElement.setBackgroundColor(Color.GRAY)
            else binding.rootElement.setBackgroundColor(Color.WHITE)
            binding.root.setOnClickListener { itemProjectClick.invoke(project.id!!) }
            binding.btnEdit.setOnClickListener { editProjectClick.invoke(project.id!!, project.name) }
            binding.btnDelete.setOnClickListener { deleteProjectClick.invoke(project.id!!, project.name) }
        }
    }

    internal class ProjectsDiffCallback : DiffUtil.ItemCallback<Project>(){
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean =
            oldItem.id == newItem.id
    }
}