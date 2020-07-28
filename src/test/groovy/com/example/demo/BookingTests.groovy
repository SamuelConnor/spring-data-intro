package com.example.demo

import com.example.demo.controllers.BookingController
import com.example.demo.controllers.LocationController
import com.example.demo.entities.BookingEntity
import com.example.demo.entities.CarEntity
import com.example.demo.entities.CustomerEntity
import com.example.demo.entities.LocationEntity
import com.example.demo.repositories.BookingEntityRepository
import com.example.demo.repositories.CarEntityRepository
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
class BookingTests extends Specification {

    MockMvc mockMvc
    @Autowired BookingController bookingController
    @Autowired BookingEntityRepository bookingEntityRepository
    @Autowired LocationEntityRepository locationEntityRepository
    @Autowired CarEntityRepository carEntityRepository
    @Autowired CustomerEntityRepository customerEntityRepository
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "Booking find by ID and find all test"() {
        given: "an entity"
            def loc = locationEntityRepository.save(new LocationEntity())
            def car = new CarEntity()
                car.setLocation(loc)
                carEntityRepository.save(car)
            def cus = customerEntityRepository.save(new CustomerEntity())
            def e1 = new BookingEntity()
                e1.setCustomer(cus)
                e1.setCar(car)
                bookingEntityRepository.save(e1)
        when: "controller is called with an id"
            def result = bookingController.findById(e1.id)
        then: "result is an entity with an id"
                result.id == e1.id
                result.id > 0
        when: "findAll is called on the repository"
            def resultList = bookingEntityRepository.findAll()
        then: "some results"
                !resultList.isEmpty()
        and: "including our earlier id"
                result.id in resultList*.id
        for (i in result) {
            println("-----------------------------------------\nTesting\n" + i.id)
        }
        cleanup:
            deleteTestBooking(e1.id)
            deleteTestCar(car.id)
            deleteTestLocation(loc.id)
            deleteTestCustomer(cus.id)
    }

    def "Booking Creation test"() {
        given: "an entity"
            def loc = locationEntityRepository.save(new LocationEntity())
            def car = new CarEntity()
                car.setLocation(loc)
                carEntityRepository.save(car)
            def cus = customerEntityRepository.save(new CustomerEntity())
            def e1 = new BookingEntity()
                e1.setCustomer(cus)
                e1.setCar(car)
                bookingEntityRepository.save(e1)
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/booking/$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
                json.id == e1.id
                json.id > 0
        cleanup:
            deleteTestBooking(e1.id)
            deleteTestCar(car.id)
            deleteTestLocation(loc.id)
            deleteTestCustomer(cus.id)
    }

    def "Booking Find by ID test"()
    {
        given: "an entities"
            def loc = locationEntityRepository.save(new LocationEntity())
            def car = new CarEntity()
                car.setLocation(loc)
                carEntityRepository.save(car)
            def cus = customerEntityRepository.save(new CustomerEntity())
            def e1 = new BookingEntity()
                e1.setCustomer(cus)
                e1.setCar(car)
                bookingEntityRepository.save(e1)
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/booking/$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an corresponding id"
                json.id == e1.id
                json.id > 0
        cleanup:
            deleteTestBooking(e1.id)
            deleteTestCar(car.id)
            deleteTestLocation(loc.id)
            deleteTestCustomer(cus.id)
    }

    def "Booking Not Found Test"()
    {
        when: "controller is called with an invalid id"
            def result = mockMvc.perform(get("/api/booking/{id}","-1")).andReturn()
        then: "an exception is thrown by the controller"
                result.response.status == HttpStatus.NOT_FOUND.value()
                result.response.errorMessage == "No booking with that id"
    }

    def "All Bookings"()
    {
        given: "three entities"
            def loc = locationEntityRepository.save(new LocationEntity())
            def car = new CarEntity()
                car.setLocation(loc)
                carEntityRepository.save(car)
            def cus = customerEntityRepository.save(new CustomerEntity())
            def e1 = new BookingEntity()
                e1.setCustomer(cus)
                e1.setCar(car)
                bookingEntityRepository.save(e1)
            def e2 = new BookingEntity()
                e2.setCustomer(cus)
                e2.setCar(car)
                bookingEntityRepository.save(e2)
            def e3 = new BookingEntity()
                e3.setCustomer(cus)
                e3.setCar(car)
                bookingEntityRepository.save(e3)
            when: "controller is called for complete list"
            def result = mockMvc.perform(get("/api/booking")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Count the number of objects"
                json*.id as Set == [e1.id, e2.id, e3.id] as Set
        cleanup:
            deleteTestBooking(e1.id)
            deleteTestBooking(e2.id)
            deleteTestBooking(e3.id)
            deleteTestCar(car.id)
            deleteTestLocation(loc.id)
            deleteTestCustomer(cus.id)
    }

    def "All Bookings Today"()
    {
        given: "three entities - 2 with todays end-date, 1 without"
            def loc = locationEntityRepository.save(new LocationEntity())
            def date = new Date()
            def oldDate = new Date(2019,03,01)
            def car = new CarEntity()
                car.setLocation(loc)
                carEntityRepository.save(car)
            def cus = customerEntityRepository.save(new CustomerEntity())
            def e1 = new BookingEntity()
                e1.setCustomer(cus)
                e1.setCar(car)
                e1.setEndDate(date)
                bookingEntityRepository.save(e1)
            def e2 = new BookingEntity()
                e2.setCustomer(cus)
                e2.setCar(car)
                e2.setEndDate(date)
                bookingEntityRepository.save(e2)
            def e3 = new BookingEntity()
                e3.setCustomer(cus)
                e3.setCar(car)
                e3.setEndDate(oldDate)
                bookingEntityRepository.save(e3)
        when: "controller is called for complete list"
        def result = mockMvc.perform(get("/api/booking/today")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "Count the number of objects"
        json*.id as Set == [e1.id, e2.id] as Set
        cleanup:
            deleteTestBooking(e1.id)
            deleteTestBooking(e2.id)
            deleteTestBooking(e3.id)
            deleteTestCar(car.id)
            deleteTestLocation(loc.id)
            deleteTestCustomer(cus.id)
    }

    def "Update a Booking"()
    {
        given: "an entity"
            def loc = locationEntityRepository.save(new LocationEntity())
            def car = new CarEntity()
                car.setLocation(loc)
                carEntityRepository.save(car)
            def cus = customerEntityRepository.save(new CustomerEntity())
            def e1 = new BookingEntity()
                e1.setCustomer(cus)
                e1.setCar(car)
            def date = new Date(System.currentTimeMillis())
                e1.setStartDate(date)
                bookingEntityRepository.save(e1)
        when: "controller is called with an id, and new start date"
            def result = mockMvc.perform(put("/api/booking/{id}",e1.id).contentType(MediaType.APPLICATION_JSON_VALUE).content("{ \"id\": ${e1.id}, \"startDate\": \"2012-02-30\" }")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "verify the start date has changed from the original date"
                json.startDate != date
        cleanup:
            deleteTestBooking(e1.id)
            deleteTestCar(car.id)
            deleteTestLocation(loc.id)
            deleteTestCustomer(cus.id)

    }


    def "Delete a Booking"()
    {
        given: "two entities"
            def loc = locationEntityRepository.save(new LocationEntity())
            def car = new CarEntity()
                car.setLocation(loc)
                carEntityRepository.save(car)
            def cus = customerEntityRepository.save(new CustomerEntity())
            def e1 = new BookingEntity()
                e1.setCustomer(cus)
                e1.setCar(car)
                bookingEntityRepository.save(e1)
            def e2 = new BookingEntity()
                e2.setCustomer(cus)
                e2.setCar(car)
                bookingEntityRepository.save(e2)
        when: "controller is called with an id, delete entity"
            def result = mockMvc.perform(delete("/api/booking/{id}",e1.id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
        then: "check if the entity still exists"
                !bookingEntityRepository.findById(e1.id)
                bookingEntityRepository.findById(e2.id)
        cleanup:
            deleteTestBooking(e2.id)
            deleteTestCar(car.id)
            deleteTestLocation(loc.id)
            deleteTestCustomer(cus.id)
    }

    def "Delete a booking that doesn't exist"()
    {
        when: "controller is called with an id that doesnt exist, delete entity"
            def result = mockMvc.perform(delete("/api/booking/{id}","-1")).andReturn()
        then: "an exception is thrown by the controller"
                result.response.status == HttpStatus.NOT_FOUND.value()
                result.response.errorMessage == "No booking with that id"
    }

    def "Updating a booking that doesn't exist"()
    {
        when: "controller is called with an id that doesnt exist, update entity"
            def result = mockMvc.perform(put("/api/booking/{id}","-1")).andReturn()
        then: "an exception is thrown by the controller"
                result.response.status == HttpStatus.NOT_FOUND.value()
                result.response.errorMessage == "No booking with that id"
    }

    def "deleteTestBooking"(def id)
    {
        return mockMvc.perform(delete("/api/booking/{id}",id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
    }
    def "deleteTestLocation"(def id)
    {
        return mockMvc.perform(delete("/api/location/{id}",id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
    }
    def "deleteTestCar"(def id)
    {
        return mockMvc.perform(delete("/api/car/{id}",id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
    }
    def "deleteTestCustomer"(def id)
    {
        return mockMvc.perform(delete("/api/customer/{id}",id).contentType(MediaType.APPLICATION_JSON)).andReturn().response.getContentAsString()
    }

}
