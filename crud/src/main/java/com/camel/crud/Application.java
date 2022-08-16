package com.camel.crud;

import com.camel.crud.model.Empleado;
import com.camel.crud.processor.EmpAddProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
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
                .to("direct:postEmpleado");

        from("direct:postEmpleado")
                .log("Realizando post")
                .choice()
                    .when(body().isNull())
                        .unmarshal(jsonEmp)
                        .process(empAddProcessor)
                    .otherwise()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("400"))
                    .endChoice()
                .end();

        /*from("direct:list")
                .bean*/

    }
}
