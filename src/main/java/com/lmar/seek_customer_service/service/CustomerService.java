package com.lmar.seek_customer_service.service;

import com.lmar.seek_customer_service.dto.CustomerDto;
import com.lmar.seek_customer_service.entity.Customer;
import com.lmar.seek_customer_service.exception.ResourceNotFoundException;
import com.lmar.seek_customer_service.mapper.CustomerMapper;
import com.lmar.seek_customer_service.repository.ICustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    /* Esperanza de vida en Perú 2020-2025,
    * mujeres sería de 79.8 años y
    * la de los hombres de 74.5 años,
    * promedio 77.15
    **/
    private static final int LIFE_EXPECTANCY = 77;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private ICustomerRepository customerRepository;

    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado"));

        return CustomerMapper.INSTANCE.toDto(customer);
    }

    @CacheEvict(value = "customers", allEntries = true)
    public CustomerDto createCustomer(CustomerDto customerDto) {
        logger.info("Registrando cliente...");
        Customer customer = CustomerMapper.INSTANCE.toEntity(customerDto);//Convertimos entidad para guardar el cliente
        customer = customerRepository.save(customer);//Guardamos el cliente
        logger.info("Cliente registrado-> nombres: {}", customer.getNames());
        return CustomerMapper.INSTANCE.toDto(customer);//Convertimos a dto para retornar guardado
    }

    //Estadísticas
    public Map<String, Double> getCustomersStatistics() {
        logger.info("Consultando estadísticas de los clientes...");
        Double average = customerRepository.calculateAverageAge();//Obtenemos el promedio de edad de los clientes
        Double standardDeviation = customerRepository.calculateStandardDeviationAge();//Obtenemos la desviación estándar de la edad de los clientes

        Map<String, Double> metrics = new HashMap<>();
        metrics.put("average", average != null ? average : 0);
        metrics.put("standardDeviation", standardDeviation != null ? standardDeviation : 0);

        logger.info("Estadísticas -> Promedio: {} - Desviacion Estandar: {}", average, standardDeviation);
        return metrics;
    }

    @Cacheable(value = "customers")
    public List<CustomerDto> getAllCustomers() {
        logger.info("Consultando lista de clientes...");

        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            logger.warn("No hay clientes registrados en la base de datos.");
            throw new ResourceNotFoundException("No hay clientes registrados en el sistema.");
        }

        logger.debug("Se encontraron {} clientes", customers.size());

        return customers.stream().map(customer -> {
            logger.info("Procesando cliente: {}", customer.getNames());

            LocalDate estimatedDateOfDeath = customer.getBirthday().plusYears(LIFE_EXPECTANCY);
            String zodiacSign = getZodiacSign(customer.getBirthday());

            return new CustomerDto(
                    customer.getId(),
                    customer.getNames(),
                    customer.getLastName(),
                    customer.getAge(),
                    customer.getBirthday(),
                    estimatedDateOfDeath,
                    zodiacSign
            );
        }).collect(Collectors.toList());
    }

    public Page<CustomerDto> listPaginatedCustomers(int page, int size) {
        logger.info("Consultando lista de clientes con paginación...");
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Customer> customersPage = customerRepository.findAll(pageable); // No necesitas definir un método personalizado

        if (customersPage.isEmpty()) {
            logger.warn("No hay clientes registrados en la base de datos.");
            throw new ResourceNotFoundException("No hay clientes registrados en el sistema.");
        }

        List<CustomerDto> customerDtoList = customersPage.stream().map(customer -> {
            logger.info("Procesando cliente: {}", customer.getNames());

            LocalDate estimatedDateOfDeath = customer.getBirthday().plusYears(LIFE_EXPECTANCY);
            String zodiacSign = getZodiacSign(customer.getBirthday());

            return new CustomerDto(
                    customer.getId(),
                    customer.getNames(),
                    customer.getLastName(),
                    customer.getAge(),
                    customer.getBirthday(),
                    estimatedDateOfDeath,
                    zodiacSign
            );
        }).toList();

        return new PageImpl<>(customerDtoList, pageable, customersPage.getTotalElements());
    }

    private String getZodiacSign(LocalDate birthday) {
        MonthDay birthdayMonth = MonthDay.of(birthday.getMonth(), birthday.getDayOfMonth());

        if (birthdayMonth.isBefore(MonthDay.of(1, 20))) return "Capricornio";
        if (birthdayMonth.isBefore(MonthDay.of(2, 19))) return "Acuario";
        if (birthdayMonth.isBefore(MonthDay.of(3, 21))) return "Piscis";
        if (birthdayMonth.isBefore(MonthDay.of(4, 20))) return "Aries";
        if (birthdayMonth.isBefore(MonthDay.of(5, 21))) return "Tauro";
        if (birthdayMonth.isBefore(MonthDay.of(6, 21))) return "Géminis";
        if (birthdayMonth.isBefore(MonthDay.of(7, 23))) return "Cáncer";
        if (birthdayMonth.isBefore(MonthDay.of(8, 23))) return "Leo";
        if (birthdayMonth.isBefore(MonthDay.of(9, 23))) return "Virgo";
        if (birthdayMonth.isBefore(MonthDay.of(10, 23))) return "Libra";
        if (birthdayMonth.isBefore(MonthDay.of(11, 22))) return "Escorpio";
        if (birthdayMonth.isBefore(MonthDay.of(12, 22))) return "Sagitario";
        return "Capricornio"; // Para nacidos desde el 22 de diciembre
    }
}
