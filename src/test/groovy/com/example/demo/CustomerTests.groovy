package com.example.demo

import com.example.demo.controllers.CustomerController
import com.example.demo.controllers.LocationController
import com.example.demo.entities.CustomerEntity
import com.example.demo.entities.LocationEntity
import com.example.demo.repositories.CustomerEntityRepository
import com.example.demo.repositories.LocationEntityRepository
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
class CustomerTests extends Specification {

    MockMvc mockMvc

    @Autowired CustomerController customerController
    @Autowired CustomerEntityRepository customerEntityRepository
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "Customer find by ID and find all test"() {
        given: "an entity"
            def e1 = customerEntityRepository.save(new CustomerEntity())
        when: "controller is called with an id"
            def result = customerController.findById(e1.id)
        then: "result is an entity with an id"
            result.id == e1.id
            result.id > 0
        when: "findAll is called on the repository"
            def resultList = customerEntityRepository.findAll()
        then: "some results"
            !resultList.isEmpty()
        and: "including our earlier id"
            result.id in resultList*.id
            for (i in result) {
                println("-----------------------------------------\nTesting\n" + i.id)
            }
        cleanup:
            deleteTestCustomer(e1.id)
    }

    def "Customer Creation test"() {
        given: "an entity"
            def e1 = customerEntityRepository.save(new CustomerEntity())
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/customer/$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
            json.id == e1.id
            json.id > 0
        cleanup:
            deleteTestCustomer(e1.id)
    }

    def "Customer Find by ID test"()
    {
        given: "two entities"
            def e1 = new CustomerEntity()
                e1.setName("Sam")
                customerEntityRepository.save(e1)
            def e2 = new CustomerEntity()
                e2.setName("Dave")
                customerEntityRepository.save(e2)
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/customer/$e2.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an corresponding id"
            json.id == e2.id
            json.id > 0
            json.id != e1.id
        cleanup:
            deleteTestCustomer(e1.id)
            deleteTestCustomer(e2.id)
    }

    def "Customer Not Found Test"()
    {
        when: "controller is called with an invalid id"
            def result = mockMvc.perform(get("/api/customer/{id}","-1")).andReturn()
        then: "an exception is thrown by the controller"
            result.response.status == HttpStatus.NOT_FOUND.value()
            result.response.errorMessage == "No customer with that id"
    }

    def "All Customers"()
    {
        given: "three entities"
            def e1 = customerEntityRepository.save(new CustomerEntity())
            def e2 = customerEntityRepository.save(new CustomerEntity())
            def e3 = customerEntityRepository.save(new CustomerEntity())
        when: "controller is called for complete list"
            def result = mockMvc.perform(get("/api/customer")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Count the number of objects"
            json[0].id == e1.id
            json[1].id == e2.id
            json[2].id == e3.id
        cleanup:
            deleteTestCustomer(e1.id)
            deleteTestCustomer(e2.id)
            deleteTestCustomer(e3.id)
    }

    def "All Customers ordered test"()
    {
        given: "three entities not in alphabetical order"
            def e1 = new CustomerEntity()
                e1.setName("Cameron")
                customerEntityRepository.save(e1)
            def e2 = new CustomerEntity()
                e2.setName("Adrian")
                customerEntityRepository.save(e2)
            def e3 = new CustomerEntity()
                e3.setName("Bert")
                customerEntityRepository.save(e3)

        when: "controller is called for alphabetical list"
            def result = mockMvc.perform(get("/api/customer?sort=name")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Check if objects are in the correct order"
            json[0].name == e2.name
            json[1].name == e3.name
            json[2].name == e1.name

           (json[0].name.compareTo(json[1].name) <= 0)
           (json[2].name.compareTo(json[1].name) >= 0)

        cleanup:
            deleteTestCustomer(e1.id)
            deleteTestCustomer(e2.id)
            deleteTestCustomer(e3.id)
    }

    def "Update a Customer"()
    {
        given: "an entity"
            def e1 = new CustomerEntity()
            e1.setName("Cameron")
            customerEntityRepository.save(e1)
        when: "controller is called with an id, and new name"
            def result = mockMvc.perform(put("/api/customer/{id}",e1.id).contentType(MediaType.APPLICATION_JSON_VALUE).content("{ \"id\": ${e1.id}, \"name\": \"Phillip\" }")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "verify the country has changed to Phillip"
            json.name == "Phillip"
        cleanup:
            deleteTestCustomer(e1.id)

    }

    def "Delete a Customer"()
    {
        given: "two entities"
            def e1 = customerEntityRepository.save(new CustomerEntity())
            def e2 = customerEntityRepository.save(new CustomerEntity())
        when: "controller is called with an id, delete entity"
            mockMvc.perform(delete("/api/customer/{id}",e1.id).contentType(MediaType.APPLICATION_JSON))
        then: "check if the entity still exists"
            !customerEntityRepository.findById(e1.id)
            customerEntityRepository.findById(e2.id)
        cleanup:
            deleteTestCustomer(e2.id)
    }

    def "deleteTestCustomer"(def id)
    {
        return mockMvc.perform(delete("/api/customer/{id}",id).contentType(MediaType.APPLICATION_JSON))
    }
}
