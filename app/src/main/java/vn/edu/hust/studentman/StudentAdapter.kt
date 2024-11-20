package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(
  private val students: List<StudentModel>,
  private val onEdit: (StudentModel) -> Unit,
  private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]
    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    holder.imageEdit.setOnClickListener { onEdit(student) }
    holder.imageRemove.setOnClickListener {
      AlertDialog.Builder(holder.itemView.context).apply {
        setTitle("Xác nhận xóa")
        setMessage("Bạn có chắc chắn muốn xóa sinh viên này không?")
        setPositiveButton("Xóa") { _, _ ->
          (students as MutableList).removeAt(position)
          notifyItemRemoved(position)
          notifyItemRangeChanged(position, students.size)
          Snackbar.make(holder.itemView, "Đã xóa sinh viên", Snackbar.LENGTH_LONG)
            .setAction("Hoàn tác") {
              students.add(position, student)
              notifyItemInserted(position)
            }.show()
        }
        setNegativeButton("Hủy", null)
      }.show()
    }
  }

  override fun getItemCount(): Int = students.size
}
