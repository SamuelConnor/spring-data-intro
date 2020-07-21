package com.example.demo

import com.example.demo.controllers.LocationController
import com.example.demo.entities.LocationEntity
import com.example.demo.exceptions.LocationNotFoundException
import com.example.demo.repositories.LocationEntityRepository
import com.example.demo.services.LocationService
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
class LocationTests extends Specification {

    MockMvc mockMvc

    @Autowired LocationController locationController
    @Autowired LocationEntityRepository locationEntityRepository
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "Location find by ID and find all test"() {
        given: "an entity"
            def e1 = locationEntityRepository.save(new LocationEntity())
        when: "controller is called with an id"
            def result = locationController.findById(e1.id)
        then: "result is an entity with an id"
            result.id == e1.id
            result.id > 0
        when: "findAll is called on the repository"
            def resultList = locationEntityRepository.findAll()
        then: "some results"
            !resultList.isEmpty()
        and: "including our earlier id"
            result.id in resultList*.id
            for (i in result) {
                println("-----------------------------------------\nTesting\n" + i.id)
            }
        cleanup:
        deleteTestLocation(e1.id)
    }

    def "Location Creation test"() {
        given: "an entity"
            def e1 = locationEntityRepository.save(new LocationEntity()) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/location/$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
            json.id == e1.id
            json.id > 0
        cleanup:
        deleteTestLocation(e1.id)
    }

    def "Location Find by ID test"()
    {
        given: "two entities"
            def e1 = new LocationEntity()
                e1.setCountry("Canada")
                locationEntityRepository.save(e1)
            def e2 = new LocationEntity()
                e2.setCountry("Algeria")
                locationEntityRepository.save(e2)
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/location/$e2.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an corresponding id"
            json.id == e2.id
            json.id > 0
            json.id != e1.id
        cleanup:
        deleteTestLocation(e1.id)
        deleteTestLocation(e2.id)
    }

    def "Location Not Found Test"()
    {
        when: "controller is called with an invalid id"
            def result = mockMvc.perform(get("/api/location/{id}","-1")).andReturn()
        then: "an exception is thrown by the controller"
            result.response.status == HttpStatus.NOT_FOUND.value()
            result.response.errorMessage == "No location with that id"
    }

    def "All locations"()
    {
        given: "three entities"
            def e1 = locationEntityRepository.save(new LocationEntity())
            def e2 = locationEntityRepository.save(new LocationEntity())
            def e3 = locationEntityRepository.save(new LocationEntity())
        when: "controller is called for complete list"
            def result = mockMvc.perform(get("/api/location")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Count the number of objects"
        json[0].id == e1.id
        json[1].id == e2.id
        json[2].id == e3.id
        cleanup:
        deleteTestLocation(e1.id)
        deleteTestLocation(e2.id)
        deleteTestLocation(e3.id)
    }





    def "All locations ordered test"()
    {
        given: "three entities not in alphabetical order"
            def e1 = new LocationEntity()
                e1.setCountry("Canada")
                locationEntityRepository.save(e1)
            def e2 = new LocationEntity()
                e2.setCountry("Algeria")
                locationEntityRepository.save(e2)
            def e3 = new LocationEntity()
                e3.setCountry("Brazil")
                locationEntityRepository.save(e3)

        when: "controller is called for alphabetical list"
            def result = mockMvc.perform(get("/api/location?sort=country")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Check if objects are in the correct order"
            json[0].country == e2.country
            json[1].country == e3.country
            json[2].country == e1.country

           (json[0].country.compareTo(json[1].country) <= 0)
           (json[2].country.compareTo(json[1].country) >= 0)

        cleanup:
        deleteTestLocation(e1.id)
        deleteTestLocation(e2.id)
        deleteTestLocation(e3.id)
    }




    def "Update a location"()
    {
        given: "an entity"
            def e1 = new LocationEntity()
            e1.setCountry("Canada")
            locationEntityRepository.save(e1)
        when: "controller is called with an id, and new location"
            def result = mockMvc.perform(put("/api/location/{id}",e1.id).contentType(MediaType.APPLICATION_JSON_VALUE).content("{ \"id\": ${e1.id}, \"country\": \"Portugal\" }")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "verify the country has changed to Portugal"
            json.country == "Portugal"
        cleanup:
        deleteTestLocation(e1.id)

    }


    def "Delete a location"()
    {
        given: "two entities"
            def e1 = locationEntityRepository.save(new LocationEntity())
            def e2 = locationEntityRepository.save(new LocationEntity())
        when: "controller is called with an id, delete entity"
            def result = mockMvc.perform(delete("/api/location/{id}",e1.id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
        then: "check if the entity still exists"
            !locationEntityRepository.findById(e1.id)
            locationEntityRepository.findById(e2.id)
        cleanup:
        deleteTestLocation(e2.id)



    }

    def "deleteTestLocation"(def id)
    {
        return mockMvc.perform(delete("/api/location/{id}",id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
    }

}
