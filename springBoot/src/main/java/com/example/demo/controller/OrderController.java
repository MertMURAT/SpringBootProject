package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;
    // Product entity si icin de CRUD işlemleri yapıbilir. Ancak Customer entity si ile CascadeType.All yaptığımız için
    // Customer da bulunan bütün CRUD işlemleri Product entity si içinde geçerlidir.
    //private final ProductRepository productRepository;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Customer> placeOrder(@RequestBody OrderRequest request) {
        try {
            return new ResponseEntity<>(orderService.addOrder(request), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAllOrders")
    public List<Customer> findAllOrders() {
        return orderService.orderList();
    }

    @GetMapping("/getInfo")
    public List<OrderResponse> getJoinInformation() {
        return orderService.orderInformation();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable int id) throws Exception {
        Customer orderById = orderService.getCustomerById(id);
        if (!ObjectUtils.isEmpty(orderById)) {
            return new ResponseEntity<>(orderById, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateOrder(@PathVariable int id, @RequestBody OrderRequest request) throws Exception {
        Customer orderById = orderService.getCustomerById(id);
        if (!ObjectUtils.isEmpty(orderById)) {
            orderById.setGender(request.getCustomer().getGender());
            orderById.setEmail(request.getCustomer().getEmail());
            orderById.setName(request.getCustomer().getName());
            orderById.setProducts(request.getCustomer().getProducts());
            return new ResponseEntity<>(orderService.addOrder(request), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable int id) {
        try {
            orderService.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
