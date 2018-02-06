package com.example.springfuctional;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.catalina.LifecycleException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.ipc.netty.http.server.HttpServer;

import java.io.File;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class SpringFuctionalApplication {

	static RouterFunction getRouter() {
        ObjectMapper objectMapper = new ObjectMapper();
        Object o = new Object();
        try {
            o = objectMapper.readValue(new File("src/main/resources/heroes.json"),Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object finalO = o;
        HandlerFunction hello = request -> ok().body(fromObject(finalO));

		return
				route(
						GET("/"), hello)
						.andRoute(
								GET("/json"), req -> ok().contentType(APPLICATION_JSON).body(fromObject(new Hello("world"))));
	}


	public static void main(String[] args) throws IOException, LifecycleException, InterruptedException {

		RouterFunction router = getRouter();

		HttpHandler httpHandler = RouterFunctions.toHttpHandler(router);

		HttpServer
				.create("localhost", 8080)
				.newHandler(new ReactorHttpHandlerAdapter(httpHandler))
				.block();

		Thread.currentThread().join();
	}
}

@Data
class Hello {
	private final String name;
}