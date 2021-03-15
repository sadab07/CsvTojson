package com.example.csvtojsonfinal.controller;

import java.io.*;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@RestController
public class CsvController {
	@GetMapping("/csv")
	public ResponseEntity<?> csss() throws JsonProcessingException, IOException{
		
		File input = new File("D:\\CsvFile\\assetenrichment.csv");
        File output = new File("C:\\Users\\ZeronSec\\Downloads\\assetenrichmentJson.json");
 
        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();
 
 
        List<Object> list = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();
 
        ObjectMapper mapper = new ObjectMapper();
 
       
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, list);
 
        JsonNode nodes = mapper.readTree(output);
   
        List<Map<String, Object>> listed = new ArrayList<>();
        Map<String , Object> map = null;
        
        for(JsonNode node : nodes) {
        	 map = new LinkedHashMap<>();
        	
        		map.put("sourceAddress", node.get("assetIP"));
        		map.put("name", node.get("assetName"));
        		map.put("hostName", node.get("assetHost"));
        		map.put("macAddress", node.get("assetMac"));
        		map.put("severity", node.get("assetCriticality"));
        		map.put("sourceLocation", node.get("assetLocation"));
        		
        		listed.add(map);
        }
    	mapper.writerWithDefaultPrettyPrinter().writeValue(output, listed);
		
        return ResponseEntity.ok(listed);
		
		
}
}
