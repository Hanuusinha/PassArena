package com.passArena.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.net.InetAddress;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) throws Exception{
        System.out.println("Hostname " + InetAddress.getLocalHost().getHostName());
        System.out.println("Host Address " + InetAddress.getLocalHost().getHostAddress());
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
