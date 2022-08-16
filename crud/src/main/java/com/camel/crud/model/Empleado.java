package com.camel.crud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Empleado {

    @JsonProperty("id")
    private String id;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("apellido")
    private String apellido;
}
