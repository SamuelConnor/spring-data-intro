package com.example.demo.services;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.repositories.CustomerEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired private CustomerEntityRepository customerEntityRepository;

    public CustomerEntity createCustomer(CustomerEntity location)
    {
        return customerEntityRepository.save(location);
    }

    public CustomerEntity findCustomerById(Long Id)
    {
        return customerEntityRepository.findById(Id).orElse(null);
    }


    public List<CustomerEntity> findAllCustomers(Pageable page)
    {
        return customerEntityRepository.findAll(page).getContent();
    }

    public CustomerEntity updateCustomer(CustomerEntity newCustomer, Long id)
    {
        CustomerEntity customer = findCustomerById(id);
       customer.setName(newCustomer.getName());
       return customerEntityRepository.save(customer);
    }

    public void deleteCustomer(Long id)
    {
        customerEntityRepository.deleteById(id);
    }
}
