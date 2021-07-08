package molina.raul.mydigimind

import java.io.Serializable

data class Tasks(var dias: ArrayList<String>,
                        var tiempo: String,
                        var nombre: String) : Serializable