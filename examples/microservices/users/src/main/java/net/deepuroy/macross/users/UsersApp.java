package net.deepuroy.macross.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@EnableEurekaClient
@Import(RepositoryRestMvcConfiguration.class)
public class UsersApp {

//	@Bean
//	public ConfigurableEurekaInstanceConfigBean eurekaInstanceConfigBean() {
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>Created config bean");
//		return new ConfigurableEurekaInstanceConfigBean();
//	}

	public static void main(String[] args) throws Exception {
		 SpringApplication.run(UsersApp.class, args);
	}
}
