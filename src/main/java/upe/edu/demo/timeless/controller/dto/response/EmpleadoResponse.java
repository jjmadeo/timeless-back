package upe.edu.demo.timeless.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)// si la propiedad es null no se muestra en el json
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // convierte el nombre de las propiedades a snake_case
public class EmpleadoResponse {
    private int id;
    private String nombre;
    private String apellido;
    private String turno;
    private String usuario;
   /* private String clave;
*/

    private Integer roleId;
    /*private Date baja;*/

    private String token;

}
