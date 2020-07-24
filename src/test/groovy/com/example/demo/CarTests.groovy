package com.example.demo

import com.example.demo.controllers.CarController
import com.example.demo.controllers.LocationController
import com.example.demo.entities.CarEntity
import com.example.demo.entities.LocationEntity
import com.example.demo.repositories.CarEntityRepository
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
class CarTests extends Specification {

    MockMvc mockMvc

    @Autowired CarController carController
    @Autowired CarEntityRepository carEntityRepository
    @Autowired LocationEntityRepository locationEntityRepository
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "Car find by ID and find all test"() {
        given: "an entity"
            def loc = locationEntityRepository.save(new LocationEntity())
            def e1 = new CarEntity();
                e1.setLocation(loc)
                carEntityRepository.save(e1)
        when: "controller is called with an id"
            def result = carController.findCarById(e1.id)
        then: "result is an entity with an id"
            result.id == e1.id
            result.id > 0
        when: "findAll is called on the repository"
            def resultList = carEntityRepository.findAll()
        then: "some results"
            !resultList.isEmpty()
        and: "including our earlier id"
            result.id in resultList*.id
            for (i in result) {
                println("-----------------------------------------\nTesting\n" + i.id)
            }
        cleanup:
            deleteTestCar(e1.id)
            deleteTestLocation(loc.id)

    }

    def "Car Creation test"() {
        given: "an entity"
            def loc = locationEntityRepository.save(new LocationEntity())
            def e1 = new CarEntity();
                e1.setLocation(loc)
                carEntityRepository.save(e1)
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/car/$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
            json.id == e1.id
            json.location.id == e1.location.id
            json.id > 0
        cleanup:
            deleteTestCar(e1.id)
            deleteTestLocation(loc.id)

    }

    def "Car Find by ID test"()
    {
        given: "two entities"
            def loc = locationEntityRepository.save(new LocationEntity())
            def e1 = new CarEntity()
                e1.setLocation(loc)
                carEntityRepository.save(e1)
            def e2 = new CarEntity()
                e2.setLocation(loc)
                carEntityRepository.save(e2)
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/car/$e2.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an corresponding id"
            json.id == e2.id
            json.id > 0
            json.id != e1.id
        cleanup:
            deleteTestCar(e1.id)
            deleteTestCar(e2.id)
            deleteTestLocation(loc.id)

    }

    def "Car Find by Country test"()
    {
        given: "three entities, two from france, one from germany"
            def loc1 = new LocationEntity()
                loc1.setCountry("France")
                locationEntityRepository.save(loc1);
            def loc2 = new LocationEntity()
                loc2.setCountry("Germany")
                locationEntityRepository.save(loc2);
            def e1 = new CarEntity()
                e1.setLocation(loc1)
                carEntityRepository.save(e1)
            def e2 = new CarEntity()
                e2.setLocation(loc2)
                carEntityRepository.save(e2)
            def e3 = new CarEntity()
                e3.setLocation(loc1);
                carEntityRepository.save(e3)
        when: "controller is called with a country"
            def result = mockMvc.perform(get("/api/car/findByLocation/$loc1.country")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is a set of entities from that country"
            json[0].id == e1.id
            json[1].id != e2.id
            json[1].id == e3.id
        cleanup:
            deleteTestCar(e1.id)
            deleteTestCar(e2.id)
            deleteTestCar(e3.id)
            deleteTestLocation(loc1.id)
            deleteTestLocation(loc2.id)
    }

    def "Car Not Found Test"()
    {
        when: "controller is called with an invalid id"
            def result = mockMvc.perform(get("/api/car/{id}","-1")).andReturn()
        then: "an exception is thrown by the controller"
            result.response.status == HttpStatus.NOT_FOUND.value()
            result.response.errorMessage == "No car with that id"
    }

    def "All locations"()
    {
        given: "three entities"
            def loc = locationEntityRepository.save(new LocationEntity())
            def e1 = new CarEntity()
                e1.setLocation(loc)
                carEntityRepository.save(e1)
            def e2 = new CarEntity()
                e2.setLocation(loc)
                carEntityRepository.save(e2)
            def e3 = new CarEntity()
                e3.setLocation(loc)
                carEntityRepository.save(e3)
        when: "controller is called for complete list"
            def result = mockMvc.perform(get("/api/car")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Count the number of objects"
        json[0].id == e1.id
        json[1].id == e2.id
        json[2].id == e3.id
        cleanup:
            deleteTestCar(e1.id)
            deleteTestCar(e2.id)
            deleteTestCar(e3.id)
            deleteTestLocation(loc.id)
    }

    def "All locations ordered test"()
    {
        given: "three entities not in alphabetical order"
            def loc = locationEntityRepository.save(new LocationEntity())
            def e1 = new CarEntity()
                e1.setLocation(loc)
                e1.setManufacturer("Mercedes")
                carEntityRepository.save(e1)
            def e2 = new CarEntity()
                e2.setLocation(loc)
                e2.setManufacturer("Audi")
                carEntityRepository.save(e2)
            def e3 = new CarEntity()
                e3.setLocation(loc)
                e3.setManufacturer("BMW")
                carEntityRepository.save(e3)

        when: "controller is called for alphabetical list"
            def result = mockMvc.perform(get("/api/car?sort=manufacturer")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Check if objects are in the correct order"
            json[0].manufacturer == e2.manufacturer
            json[1].manufacturer == e3.manufacturer
            json[2].manufacturer == e1.manufacturer

           (json[0].manufacturer.compareTo(json[1].manufacturer) <= 0)
           (json[2].manufacturer.compareTo(json[1].manufacturer) >= 0)

        cleanup:
            deleteTestCar(e1.id)
            deleteTestCar(e2.id)
            deleteTestCar(e3.id)
            deleteTestLocation(loc.id)
    }

    def "Update a location"()
    {
        given: "an entity"
            def loc = locationEntityRepository.save(new LocationEntity())
            def e1 = new CarEntity()
                e1.setLocation(loc)
                e1.setManufacturer("Mercedes")
                carEntityRepository.save(e1)
        when: "controller is called with an id, and new manufacturer"
            def result = mockMvc.perform(put("/api/car/{id}",e1.id).contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"manufacturer\":\"Audi\",\"location\":{\"id\":${loc.id}}}")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "verify the manufacturer has changed to Audi"
            json.manufacturer == "Audi"
        cleanup:
            deleteTestCar(e1.id)
            deleteTestLocation(loc.id)

    }


    def "Delete a location"()
    {
        given: "two entities"
            def loc = locationEntityRepository.save(new LocationEntity())
            def e1 = new CarEntity()
                e1.setLocation(loc)
                carEntityRepository.save(e1)
            def e2 = new CarEntity()
                e2.setLocation(loc)
                carEntityRepository.save(e2)
        when: "controller is called with an id, delete entity"
            mockMvc.perform(delete("/api/car/{id}",e1.id).contentType(MediaType.APPLICATION_JSON))
        then: "check if the entity still exists"
            !carEntityRepository.findById(e1.id)
            carEntityRepository.findById(e2.id)
        cleanup:
            deleteTestCar(e2.id)
            deleteTestLocation(loc.id)



    }

    def "deleteTestLocation"(def id)
    {
        return mockMvc.perform(delete("/api/location/{id}",id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
    }
    def "deleteTestCar"(def id)
    {
        return mockMvc.perform(delete("/api/car/{id}",id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
    }

}
