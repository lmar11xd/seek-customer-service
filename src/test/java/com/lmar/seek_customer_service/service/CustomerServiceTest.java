package com.lmar.seek_customer_service.service;

import com.lmar.seek_customer_service.dto.CustomerDto;
import com.lmar.seek_customer_service.entity.Customer;
import com.lmar.seek_customer_service.exception.InvalidDataException;
import com.lmar.seek_customer_service.exception.ResourceNotFoundException;
import com.lmar.seek_customer_service.mapper.CustomerMapper;
import com.lmar.seek_customer_service.repository.ICustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private ICustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 1));
        customerDto = CustomerMapper.INSTANCE.toDto(customer);
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("John", result.getNames());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void testCreateCustomer_Success() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.createCustomer(customerDto);

        assertNotNull(result);
        assertEquals("John", result.getNames());
    }

    @Test
    void testCreateCustomer_InvalidAge() {
        customerDto.setAge(-1);

        assertThrows(InvalidDataException.class, () -> customerService.createCustomer(customerDto));
    }

    @Test
    void testGetCustomersStatistics() {
        when(customerRepository.calculateAverageAge()).thenReturn(30.0);
        when(customerRepository.calculateStandardDeviationAge()).thenReturn(5.0);

        Map<String, Double> stats = customerService.getCustomersStatistics();

        assertEquals(30.0, stats.get("average"));
        assertEquals(5.0, stats.get("standardDeviation"));
    }

    @Test
    void testGetAllCustomers_Success() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        List<CustomerDto> customers = customerService.getAllCustomers();

        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
    }

    @Test
    void testGetAllCustomers_NoCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getAllCustomers());
    }

    @Test
    void testListPaginatedCustomers_Success() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Customer> customerPage = new PageImpl<>(List.of(customer), pageable, 1);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<CustomerDto> result = customerService.listPaginatedCustomers(0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testListPaginatedCustomers_NoCustomers() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Customer> emptyPage = Page.empty();

        when(customerRepository.findAll(pageable)).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> customerService.listPaginatedCustomers(0, 10));
    }
}