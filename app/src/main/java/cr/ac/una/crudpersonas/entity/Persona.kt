package cr.ac.una.crudpersonas.entity

data class Persona(
    var id : String,
    var nombre : String,
    var apellido : String,

    ){
    constructor() : this("", "", "")

}