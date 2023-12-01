package com.excel.to.sql.convertor.exceltosqlconvertor.Controller;

import com.excel.to.sql.convertor.exceltosqlconvertor.Service.ExcelDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataController {

    // Inject the service that handles schema creation and data import
    private final ExcelDataService excelDataService;

    @Autowired
    public DataController(ExcelDataService excelDataService) {
        this.excelDataService = excelDataService;
    }

    @PostMapping("/createschema")
    public ResponseEntity<String> createSchema() {
        boolean result = excelDataService.createSchemaFromExcelFiles();
        if (result) {
            return ResponseEntity.ok("Schema created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating schema.");
        }
    }

    @PostMapping("/importdata")
    public ResponseEntity<String> importData() {
        boolean result = excelDataService.importDataFromExcelFiles();
        if (result) {
            return ResponseEntity.ok("Data imported successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error importing data.");
        }
    }
    @GetMapping("/get/customer/transactions")
    public List<JSONObject> getCustomerTransactions() {
        return excelDataService.getCustomerTransactions();
    }
    @GetMapping(value = "/get/customer/details/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public String groupTransactionsByCustomerOne() {
        List<List<JSONObject>> responseObject = excelDataService.groupTransactionsByCustomer(excelDataService.getCustomerTransactions());
//        System.out.println(responseObject);
        return responseObject.get(0).toString();
    }
//    @GetMapping(value = "/get/customer/details/one", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<String> groupTransactionsByCustomerOne() {
//        List<List<String>> responseObject=excelDataService.groupTransactionsByCustomer(excelDataService.getCustomerTransactions());
//        System.out.println(responseObject);
//        return responseObject.get(0);
//
//    }
//    @GetMapping(value = "/get/customer/details/2", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Map<Integer, String> groupTransactionsByCustomerTwo() {
//        List<List<String>> responseObject=excelDataService.groupTransactionsByCustomer(excelDataService.getCustomerTransactions());
//        return responseObject;
//
//    }
//    @GetMapping(value = "/get/customer/details/3", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Map<Integer, String> groupTransactionsByCustomerThree() {
//        Map<Integer, String> responseObject= excelDataService.groupTransactionsByCustomer(excelDataService.getCustomerTransactions());
//        return responseObject;
//
//    }
//    @GetMapping(value = "/get/customer/details/4", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Map<Integer, String> groupTransactionsByCustomerFour() {
//        Map<Integer, String> responseObject= excelDataService.groupTransactionsByCustomer(excelDataService.getCustomerTransactions());
//        return responseObject;
//
//    }
//    @GetMapping(value = "/get/customer/details/5", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Map<Integer, String> groupTransactionsByCustomerFive() {
//        Map<Integer, String> responseObject= excelDataService.groupTransactionsByCustomer(excelDataService.getCustomerTransactions());
//        return responseObject;
//
//    }
}
