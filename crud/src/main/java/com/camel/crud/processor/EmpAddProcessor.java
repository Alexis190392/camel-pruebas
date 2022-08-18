package com.camel.crud.processor;

import com.camel.crud.dtos.ResponseDto;
import com.camel.crud.model.Empleado;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmpAddProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(EmpAddProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        Empleado empleado = exchange.getIn().getBody(Empleado.class);
        logger.info("\n Post empleado: id:{} , nombre:{} , apellido:{}"
                , empleado.getId()
                , empleado.getNombre()
                , empleado.getApellido());

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("dto");

        exchange.getOut().setBody(responseDto);

    }
}
