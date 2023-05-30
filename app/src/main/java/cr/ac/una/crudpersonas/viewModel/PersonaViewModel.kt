package cr.ac.una.crudpersonas.viewModel

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import cr.ac.una.crudpersonas.adapter.PersonaAdapter
import cr.ac.una.crudpersonas.dao.PersonaDAO
import cr.ac.una.crudpersonas.entity.Persona
import java.util.concurrent.CompletableFuture


class PersonaViewModel : ViewModel(), PersonaDAO {
    private val personasRef = FirebaseDatabase.getInstance().getReference("persona")
    private val _persona: MutableLiveData<List<Persona>> = MutableLiveData()
    val personas: LiveData <List<Persona>> = _persona

    override fun guardarPersona(persona: Persona) {
        val personaId = personasRef.push().key
        personasRef.child(personaId!!).setValue(persona)
    }
    override fun listarPersonas(): LiveData<List<Persona>> {
        personasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val personasList = mutableListOf<Persona>()
                for (personaSnapshot in dataSnapshot.children) {
                    val persona = personaSnapshot.getValue(Persona::class.java)
                    persona?.let {
                        personasList.add(it)
                    }
                }
                _persona.postValue(personasList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Error : ${databaseError.message}")
            }
        })
        return personas
    }

    override fun eliminarPersona(id: String) {
        val personaRef = FirebaseDatabase.getInstance().getReference("persona")
        personaRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val personaSnapshot = dataSnapshot.children.first()
                    val personaKey = personaSnapshot.key
                    println("Clave de la persona: $personaKey")
                    personaRef.child(personaKey.toString()).removeValue()
                } else {
                    println("No se encontró ninguna persona con el ID especificado")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Error al obtener la clave de la persona: ${databaseError.message}")
            }
            })
    }
    override fun actualizarPersona(persona: Persona) {
        //  actualización
    }


}


