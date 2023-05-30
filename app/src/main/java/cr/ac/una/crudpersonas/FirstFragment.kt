package cr.ac.una.crudpersonas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import cr.ac.una.crudpersonas.viewModel.PersonaViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.crudpersonas.adapter.PersonaAdapter
import cr.ac.una.crudpersonas.databinding.FragmentFirstBinding
import cr.ac.una.crudpersonas.entity.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var personaViewModel : PersonaViewModel
    private lateinit var personas :List<Persona>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView = view.findViewById<RecyclerView>(R.id.list_view)
        personas = mutableListOf<Persona>()
        val adapter = PersonaAdapter(personas as ArrayList<Persona>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

        personaViewModel = ViewModelProvider(requireActivity()).get(PersonaViewModel::class.java)
        personaViewModel.personas.observe(viewLifecycleOwner){
                elementos ->
                adapter.updateData(elementos as ArrayList<Persona>)
                personas = elementos
            println(personas)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            personaViewModel.listarPersonas()
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position == 0) {
                    adapter.notifyItemChanged(position)
                    return
                }
                val persona = adapter.personas[position - 1]
                viewLifecycleOwner.lifecycleScope.launch {
                    personaViewModel.eliminarPersona(persona.id)
                    Toast.makeText(requireContext(), "Se eliminó correctamente", Toast.LENGTH_SHORT).show()
                }
                personas = personas.filterIndexed { index, _ -> index != (position - 1) }
                adapter.updateData(personas as ArrayList<Persona>)
            }


            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (viewHolder is PersonaAdapter.ViewHolder) {
                    super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState, isCurrentlyActive)
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        val itemView = viewHolder.itemView
                        val paint = Paint()
                        paint.color = Color.RED
                        val deleteIcon = ContextCompat.getDrawable( requireContext(),android.R.drawable.ic_menu_delete)
                        val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
                        val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                        val iconBottom = iconTop + deleteIcon.intrinsicHeight

                        c.drawRect( itemView.left.toFloat(),itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(),paint)

                        val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        deleteIcon.draw(c)
                    }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(listView)


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


