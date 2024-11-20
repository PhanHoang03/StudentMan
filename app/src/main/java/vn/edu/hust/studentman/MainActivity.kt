package vn.edu.hust.studentman

import android.widget.TextView
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )
  private lateinit var studentAdapter: StudentAdapter
  private var recentlyDeletedStudent: StudentModel? = null
  private var recentlyDeletedPosition: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_students)
    studentAdapter = StudentAdapter(students, ::onEditStudent, ::onDeleteStudent)

    recyclerView.adapter = studentAdapter
    recyclerView.layoutManager = LinearLayoutManager(this)

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showStudentDialog(null)
    }
  }

  private fun showStudentDialog(student: StudentModel?) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_student, null)
    val dialog = AlertDialog.Builder(this)
      .setTitle(if (student == null) "Add Student" else "Edit Student")
      .setView(dialogView)
      .setPositiveButton("Save") { _, _ ->
        val name = dialogView.findViewById<TextView>(R.id.edit_student_name).text.toString()
        val id = dialogView.findViewById<TextView>(R.id.edit_student_id).text.toString()

        if (student == null) {
          // Add new student
          students.add(StudentModel(name, id))
        } else {
          // Edit existing student
          student.studentName = name
          student.studentId = id
        }
        studentAdapter.notifyDataSetChanged()
      }
      .setNegativeButton("Cancel", null)
      .create()
    dialog.show()

    student?.let {
      dialogView.findViewById<TextView>(R.id.edit_student_name).text = student.studentName
      dialogView.findViewById<TextView>(R.id.edit_student_id).text = student.studentId
    }
  }

  private fun onEditStudent(student: StudentModel) {
    showStudentDialog(student)
  }

  private fun onDeleteStudent(position: Int) {
    recentlyDeletedStudent = students[position]
    recentlyDeletedPosition = position
    students.removeAt(position)
    studentAdapter.notifyItemRemoved(position)

    Snackbar.make(findViewById(R.id.main), "Student deleted", Snackbar.LENGTH_LONG)
      .setAction("Undo") {
        recentlyDeletedStudent?.let {
          students.add(recentlyDeletedPosition, it)
          studentAdapter.notifyItemInserted(recentlyDeletedPosition)
        }
      }.show()
  }
}