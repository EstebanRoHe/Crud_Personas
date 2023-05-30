package cr.ac.una.crudpersonas

import cr.ac.una.crudpersonas.viewModel.PersonaViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cr.ac.una.crudpersonas.dao.PersonaDAO
import cr.ac.una.crudpersonas.databinding.FragmentSecondBinding
import cr.ac.una.crudpersonas.entity.Persona
import kotlinx.coroutines.launch


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var personaDAO: PersonaDAO
    private lateinit var personaViewModel: PersonaViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)

        }
        val txtidPersona = view.findViewById<EditText>(R.id.idPersona)
        val txtNombre = view.findViewById<EditText>(R.id.nombre)
        val txtApellido = view.findViewById<EditText>(R.id.apellido)

        personaViewModel = ViewModelProvider(this).get(PersonaViewModel::class.java)

        binding.btnSave.setOnClickListener {

            val idPersona = txtidPersona.text.toString()
            val nombre = txtNombre.text.toString()
            val apellido = txtApellido.text.toString()

            if (idPersona != "" && nombre != "" && apellido != "" ) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val persona = Persona(idPersona,nombre,apellido)
                    personaViewModel.guardarPersona(persona)
                    Toast.makeText(requireContext(), "Se agregó correctamente", Toast.LENGTH_SHORT).show()
                    txtidPersona.setText("")
                    txtNombre.setText("")
                    txtApellido.setText("")


                }
            } else {
                Toast.makeText(requireContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}