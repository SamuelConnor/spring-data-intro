package com.example.demo.controllers;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.exceptions.LocationNotFoundException;
import com.example.demo.model.dto.CustomerCreationDTO;
import com.example.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(path = "/customer")
    public CustomerEntity createCustomer(@RequestBody CustomerCreationDTO customerDTO)
    {
        CustomerEntity customer = customerDTO.convertDtoToEntity();
        return customerService.createCustomer(customer);
    }

    @ResponseBody
    @GetMapping(path = "/customer/{id}")
    public CustomerEntity findById(@PathVariable Long id)
    {
        CustomerEntity customer = customerService.findCustomerById(id);
        if(customer == null)
            throw new CustomerNotFoundException();
        return customer;
    }

    @ResponseBody
    @GetMapping(path = "/customer")
    public List<CustomerEntity> allCustomers(Pageable page)
    {
        return customerService.findAllCustomers(page);
    }

    @PutMapping(path = "/customer/{id}")
    public ResponseEntity<CustomerEntity> updateCustomer(@RequestBody CustomerEntity customer, @PathVariable Long id)
    {
        return ResponseEntity.ok(customerService.updateCustomer(customer, id));
    }

    @DeleteMapping(path = "/customer/{id}")
    public void deleteLocation(@PathVariable Long id)
    {
        if(customerService.findCustomerById(id) == null)
            throw new CustomerNotFoundException();
        customerService.deleteCustomer(id);
    }



}
