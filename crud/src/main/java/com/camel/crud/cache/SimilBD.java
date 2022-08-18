package com.camel.crud.cache;

import com.camel.crud.model.Empleado;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimilBD {

    public static final Map<String, Empleado> listEmp = new ConcurrentHashMap<>();

    /**
     * POST
     * @param empleado
     */
    @Handler //definir por default
    public void addEmp(@Body Empleado empleado){
        listEmp.put(empleado.getId(), empleado);
    }

    /**
     * Get
     * @return
     */
    public List<Empleado> listarEmp(){
        return new ArrayList<>(listEmp.values());
    }

    /**
     * Get by id
     * @param id
     * @return
     */
    public Empleado getEmpById(String id){
        return listEmp.get(id);
    }

    /**
     * Put
     */
    public Empleado putEmp(@Body Empleado empleado){
        listEmp.put(empleado.getId(), empleado);
        return listEmp.get(empleado.getId());
    }

    /**
     * Delete by id
      * @param id
     */
    public void deleteEmp(String id){
        listEmp.remove(id);
    }


}
