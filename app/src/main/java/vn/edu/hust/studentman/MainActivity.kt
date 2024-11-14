package vn.edu.hust.studentman

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val students = mutableListOf(
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

    studentAdapter = StudentAdapter(
      students,
      onEditClick = { student ->
        openEditStudentDialog(studentAdapter, students, student)
      },
      onRemoveClick = { student ->
        openRemoveStudentDialog(studentAdapter, students, student)
      }
    )

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    val addNewButton = findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      openAddStudentDialog(studentAdapter, students)
    }
  }

  private fun openAddStudentDialog(adapter: StudentAdapter, students: MutableList<StudentModel>) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)

    val nameEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val idEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

    AlertDialog.Builder(this)
      .setView(dialogView)
      .setTitle("Add New Student")
      .setPositiveButton("Add") { dialogInterface, _ ->
        val name = nameEditText.text.toString().trim()
        val id = idEditText.text.toString().trim()

        if (name.isNotEmpty() && id.isNotEmpty()) {
          val newStudent = StudentModel(name, id)
          students.add(newStudent)
          adapter.notifyItemInserted(students.size - 1)
          dialogInterface.dismiss()
        } else {
          if (name.isEmpty()) nameEditText.error = "Enter student name"
          if (id.isEmpty()) idEditText.error = "Enter student ID"
        }
      }
      .setNegativeButton("Cancel") { dialogInterface, _ ->
        dialogInterface.dismiss()
      }
      .create().show()
  }

  private fun openEditStudentDialog(adapter: StudentAdapter, students: MutableList<StudentModel>, student: StudentModel) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
    val nameEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val idEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

    nameEditText.setText(student.studentName)
    idEditText.setText(student.studentId)

    AlertDialog.Builder(this)
      .setView(dialogView)
      .setTitle("Edit Student")
      .setPositiveButton("Save") { dialogInterface, _ ->
        val newName = nameEditText.text.toString().trim()
        val newId = idEditText.text.toString().trim()

        if (newName.isNotEmpty() && newId.isNotEmpty()) {
          val index = students.indexOf(student)
          students[index] = StudentModel(newName, newId)
          adapter.notifyItemChanged(index)
        }
        dialogInterface.dismiss()
      }
      .setNegativeButton("Cancel") { dialogInterface, _ ->
        dialogInterface.dismiss()
      }
      .create().show()
  }

  private fun openRemoveStudentDialog(adapter: StudentAdapter, students: MutableList<StudentModel>, student: StudentModel) {
    AlertDialog.Builder(this)
      .setTitle("Remove Student")
      .setMessage("Are you sure you want to remove ${student.studentName}?")
      .setPositiveButton("Remove") { dialogInterface, _ ->
        val index = students.indexOf(student)
        if (index != -1) {
          students.removeAt(index)
          adapter.notifyItemRemoved(index)

          val snackbar = Snackbar.make(findViewById(R.id.main), "${student.studentName} has been removed", Snackbar.LENGTH_LONG)
          snackbar.setAction("Undo") {
            students.add(index, student)
            adapter.notifyItemInserted(index)
          }
          snackbar.show()
        }
        dialogInterface.dismiss()
      }
      .setNegativeButton("Cancel") { dialogInterface, _ ->
        dialogInterface.dismiss()
      }
      .create()
      .show()
  }
}