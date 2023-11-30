package com.excel.to.sql.convertor.exceltosqlconvertor.Controller;

import com.excel.to.sql.convertor.exceltosqlconvertor.Service.ExcelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
