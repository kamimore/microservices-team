package com.microservices.product_services;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.product_services.dto.ProductRequest;
import com.microservices.product_services.repository.ProductRepository;
import com.mongodb.assertions.Assertions;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
//we need to auto configure the mock mvc, we are using it to call the controllers endpoint
class ProductServicesApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	//giving it static so we can modify the mongodb url, inside the MongoDBContainer object we need to pass the docker image we want to use, before it, it takes the mongo version on its own but now we have to pass the docker image on it
	//1.  first the mongodb will be download from the docker image

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	//this annotation will help to add this property dynamically to the spring context at run time
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}
	//2. mongodb uri will be set

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		//we have to convert this into the string, so that's why we are using the objectMapper
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		 
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product") // Correct placement
        .contentType(MediaType.APPLICATION_JSON) // Correct usage
        .content(productRequestString)) // Validate response
		.andExpect(status().isCreated());

	 Assertions.assertTrue(productRepository.findAll().size() == 1);
	}

	@Test
	void testGetProduct() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		Assertions.assertTrue(productRepository.findAll().size() == 0); 
	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
		.name("IPhone 13")
		.description("IPhone 13")
		.price(BigDecimal.valueOf(1200))
		.build();
	}

}
