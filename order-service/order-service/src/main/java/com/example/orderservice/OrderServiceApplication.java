package com.example.orderservice;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.naming.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class OrderServiceApplication {
	private final BeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
	@Bean
	public RequestInterceptor requestTokenBearerInterceptor(){
		return requestTemplate -> {
			JwtAuthenticationToken token = (JwtAuthenticationToken)SecurityContextHolder.getContext()
					.getAuthentication();
			requestTemplate.header("Authentication","Bearer "+token.getToken().getTokenValue());
		};
	}
	@Bean
	public ExecutorService traceableExecutorService() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		return new TraceableExecutorService(beanFactory, executorService);
	}
}
