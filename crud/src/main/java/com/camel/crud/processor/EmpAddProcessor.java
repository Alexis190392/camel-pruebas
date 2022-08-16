package com.camel.crud.processor;

import com.camel.crud.model.Empleado;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class EmpAddProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        Empleado empleado = exchange.getIn().getBody(Empleado.class);
        System.out.println("\n Post empleado: \n"
                            + empleado.getId() + "\n"
                            + empleado.getNombre() + "\n"
                            + empleado.getApellido() + "\n");

        


    }
}
