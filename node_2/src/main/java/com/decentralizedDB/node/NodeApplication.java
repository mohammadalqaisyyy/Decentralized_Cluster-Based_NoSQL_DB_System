package com.decentralizedDB.node;

import com.decentralizedDB.node.config.RsaKeyProperties;
import com.decentralizedDB.node.controller.BootstrapController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableCaching
public class NodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NodeApplication.class, args);
        BootstrapController.startup();
	}

}
