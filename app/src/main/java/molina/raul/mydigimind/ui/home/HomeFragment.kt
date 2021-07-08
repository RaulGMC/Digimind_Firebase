package molina.raul.mydigimind.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.recordatorio.view.*
import molina.raul.mydigimind.R
import molina.raul.mydigimind.*
import molina.raul.mydigimind.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var adaptador: AdaptardorTareas? = null
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

    companion object {
        var tasks = ArrayList<Tasks>()
        var first = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        tasks = ArrayList()
        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        fillTasks()

        if (!tasks.isEmpty()) {
            var gridView: GridView = root.findViewById(R.id.grindview)

            adaptador = AdaptardorTareas(root.context, tasks)
            gridView.adapter = adaptador
        }


        return root
    }

    /*
    fun fullTasks(){
        tasks.add(Recordatorio(arrayListOf("Tuesday"), "17:30","Practice 1", ))
        tasks.add(Recordatorio(arrayListOf("Monday","Sunday"), "17:00","Practice 2"))
        tasks.add(Recordatorio(arrayListOf("Wednesday"), "14:00","Practice 3"))
        tasks.add(Recordatorio(arrayListOf("Saturday"), "11:00","Practice 4"))
        tasks.add(Recordatorio(arrayListOf("Friday"), "13:00","Practice 5"))
        tasks.add(Recordatorio(arrayListOf("Thursday"), "10:40","Practice 6"))
        tasks.add(Recordatorio(arrayListOf("Monday"), "12:00", "Practice 7" ))
    }*/
    @SuppressLint("UseRequireInsteadOfGet")
    fun fillTasks() {
        storage.collection("actividades")
            .whereEqualTo("email", usuario.currentUser?.email)
            .get()
            .addOnSuccessListener {
                it.forEach {
                    var dias = ArrayList<String>()
                    if (it.getBoolean("lu") == true) {
                        dias.add("Monday")
                    }
                    if (it.getBoolean("ma") == true) {
                        dias.add("Tuesday")
                    }
                    if (it.getBoolean("mi") == true) {
                        dias.add("Wednesday")
                    }
                    if (it.getBoolean("ju") == true) {
                        dias.add("Thursday")
                    }
                    if (it.getBoolean("vi") == true) {
                        dias.add("Friday")
                    }
                    if (it.getBoolean("sa") == true) {
                        dias.add("Saturday")
                    }
                    if (it.getBoolean("do") == true) {
                        dias.add("Sunday")
                    }
                    //tasks!!.add(Recordatorio(it.getString("actividad")!!, dias, it.getString("tiempo")!!))
                    tasks!!.add(Tasks(dias, it.getString("actividad")!!, it.getString("tiempo")!!))
                }
                adaptador = AdaptardorTareas(context!!, tasks)
                grindview.adapter = adaptador
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error: intente de nuevo", Toast.LENGTH_SHORT).show()
            }
    }

    private class AdaptardorTareas : BaseAdapter {
        var tasks = ArrayList<Tasks>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Tasks>) {
            this.contexto = contexto
            this.tasks = tasks
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var task = tasks[p0]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.recordatorio, null)

            vista.textNombreRecordatorio.setText(task.nombre)
            vista.textDiasRecordatorio.setText(task.tiempo)
            vista.textTiempoRecordatorio.setText(task.dias.toString())

            return vista
        }

        override fun getItem(p0: Int): Any {
            return tasks[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return tasks.size
        }

    }
}