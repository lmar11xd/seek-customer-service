package com.lmar.seek_customer_service.controller;

import com.lmar.seek_customer_service.dto.CustomerDto;
import com.lmar.seek_customer_service.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "Obtener todos los clientes",
            description = "Retorna la lista de clientes registrados con cálculos derivados como esperanza de vida y signo del sodiaco."
    )
    @SecurityRequirement(name = "BearerAuth") // Protegido con JWT
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(
            summary = "Registrar un cliente",
            description = "Registra un cliente (nombres, apellidos, edad y fecha de nacimiento)."
    )
    @SecurityRequirement(name = "BearerAuth") // Protegido con JWT
    @PostMapping("/create")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @Operation(
            summary = "Obtener estadísticas de los clientes",
            description = "Retorna el promedio y desviación estándar de edad de los clientes."
    )
    @SecurityRequirement(name = "BearerAuth") // Protegido con JWT
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Double>> getCustomersStatistics() {
        return ResponseEntity.ok(customerService.getCustomersStatistics());
    }
}
