package cr.ac.una.crudpersonas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import cr.ac.una.crudpersonas.entity.Persona

interface PersonaDAO {

    fun guardarPersona(persona: Persona)
    fun listarPersonas(): LiveData<List<Persona>>
    fun actualizarPersona(persona: Persona)
    fun eliminarPersona(id: String)
}
