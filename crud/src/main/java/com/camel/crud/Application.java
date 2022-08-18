package com.camel.crud;

import com.camel.crud.cache.SimilBD;
import com.camel.crud.dtos.ResponseDto;
import com.camel.crud.model.Empleado;
import com.camel.crud.processor.EmpAddProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;


@SpringBootApplication
@Component
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {

    private JacksonDataFormat jsonEmp = new JacksonDataFormat(Empleado.class);
    @Autowired
    private EmpAddProcessor empAddProcessor;
    @Autowired
    private SimilBD cache;


    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configure() throws Exception {
        /*from("timer://foo?period=5000")
            .setBody().constant("Hello World")
            .log(">>> ${body}");*/

        /*restConfiguration()
                .component("servlet")
                .port(8080);*/
        restConfiguration() //se empieza a configurar un componente rest para hacer uso de endpoints a partir de REST
                .bindingMode(RestBindingMode.auto)
                .component("jetty") //componente a utilizar //jetty: servidor
                .enableCORS(true) //permite el acceso de recursos crusados http
                .port(10000) //puerto con el que va a trabajar
                .corsHeaderProperty("Access-Control-Allow-Origin", "*") //habilitar las ip de las cuales quiero consumir separadas por coma. * es para todas
                .corsHeaderProperty("Access-Control-Allow-Headers", "*");   //permite definir que tipo de headers pueden invocarse. Accept,Content-Type

        rest("empleado")
               /* .get("/list").description("Listado de personas")
                .to("direct:list")
                .get("/{id}").description("Detalles por id")
                .to("direct:byId")*/
                .post("/post").description("Post empleado")
                    .type(Empleado.class) //lo que espero recibir
                    .outType(ResponseDto.class) //lo que espero entregar al usuario
                    .to("direct:postEmpleado")
                .get("/").outType(Empleado[].class)
                    .to("direct:getEmpleados")
                .get("/{id}").outType(String.class)
                    .to("direct:getEmpleadosById")
                .put("put")
                    .type(Empleado.class) //lo que espero recibir
                    .outType(ResponseDto.class) //lo que espero entregar al usuario
                    .to("direct:putEmp")
                .delete("delete/{id}")
                    .to("direct:deleteEmpleado");

//        from("direct:postEmpleado")
//                .streamCaching()
//                .log("Realizando post")
//                .log("${body}")
//                .bean(cache,"addEmp")
//                .end();

        from("direct:postEmpleado").streamCaching()
                .log("Realizando post ${body}")
                .bean(cache)
                //.process(empAddProcessor)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("200"))
                .end();

        from("direct:getEmpleados")
                .log("obtener empleados")
                .bean(cache,"listarEmp");

        from("direct:getEmpleadosById")
                .log("Buscar por id ${id}")
                .bean(cache,"getEmpById(${header.id})");

        from("direct:putEmp").streamCaching()
                .log("Modificar empleado")
                .bean(cache,"putEmp");

        from("direct:deleteEmpleado")
                .log("Eliminar empleado")
                .bean(cache,"deleteEmp(${header.id})");


    }
}
