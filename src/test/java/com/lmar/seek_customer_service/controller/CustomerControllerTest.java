package com.lmar.seek_customer_service.controller;

import com.lmar.seek_customer_service.dto.CustomerDto;
import com.lmar.seek_customer_service.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto("John", "Doe", 30, LocalDate.of(1994, 1, 1));
    }

    @Test
    void testGetAllCustomers() {
        List<CustomerDto> customers = Arrays.asList(customerDto);
        when(customerService.getAllCustomers()).thenReturn(customers);

        ResponseEntity<List<CustomerDto>> response = customerController.getAllCustomers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getNames());
    }

    @Test
    void testCreateCustomer() {
        when(customerService.createCustomer(customerDto)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.createCustomer(customerDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John", response.getBody().getNames());
    }

    @Test
    void testGetCustomersStatistics() {
        Map<String, Double> statistics = new HashMap<>();
        statistics.put("averageAge", 30.0);
        statistics.put("standardDeviation", 5.0);

        when(customerService.getCustomersStatistics()).thenReturn(statistics);

        ResponseEntity<Map<String, Double>> response = customerController.getCustomersStatistics();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(30.0, response.getBody().get("averageAge"));
        assertEquals(5.0, response.getBody().get("standardDeviation"));
    }
}
