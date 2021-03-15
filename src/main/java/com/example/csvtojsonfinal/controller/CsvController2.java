package com.example.csvtojsonfinal.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@RestController
public class CsvController2 {

	@GetMapping("/Duplicate")
	public ResponseEntity<?> duplicatedata() throws JsonGenerationException, JsonMappingException, IOException {
		File input = new File("D:\\CsvFile\\duplicatedatatask.txt");
		File output = new File("C:\\Users\\ZeronSec\\Downloads\\Duplicatevalue.json");

		CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
		CsvMapper csvMapper = new CsvMapper();

		List<Object> list = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();

		Set<Object> set = new LinkedHashSet<Object>();
		final String privateIp = "(^10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(^192\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(^172\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;

		mapper.writerWithDefaultPrettyPrinter().writeValue(output, list);

		JsonNode nodes = mapper.readTree(output);
		List<Map<String, Object>> listed = new ArrayList<>();

		for (String ip : nodes.findValuesAsText("assetIP")) 
			set.add(ip);

		for (Object i : set) {

			map = new LinkedHashMap<>();
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			String regextype = "";
			String ip = "";
			for (JsonNode node : nodes) {
				Map<String, Object> map1 = new LinkedHashMap<String, Object>();
				String s = node.findValuesAsText("assetIP").toString().replace("[", "").replace("]", "");
//				System.out.println("set" +i + "=" + s);
				if (s.equals(i)) {

					ip = node.get("assetIP").toPrettyString().replace("\"", "");
//					
					if (ip.matches(privateIp)) {
						regextype = "private";
					}

					else {
						regextype = "public";
					}
//					String firstSubIp = ip.substring(ip.indexOf(".")).replaceFirst(".", "");
//					
//					String secondSegment = firstSubIp.substring(0,ip.indexOf("."));
////					
////					String secondSubIp = secondSegment.substring(secondSegment.indexOf(".") + 1);
////					
////					String thirdSegment = secondSubIp.substring(0,ip.indexOf("."));
////					
////					String thirdSubIp = thirdSegment.substring(0,thirdSegment.indexOf(".") + 1);
//					
//					System.err.println(firstSubIp + " " + secondSegment); 

//							" " + firstSubIp + " " + secondSegment + " " + secondSubIp + " " + thirdSegment + " " + thirdSubIp);
//					for(String k : ip) {
//						System.err.println(k);
//					}
//					System.err.println(node.findValuesAsText("assetIP").toString().replace("[", "").replace("]", "").split("."));
//					if(Integer.parseInt(ipDetect) >= 10 && Integer.parseInt(ipDetect) <= 255) {
//						map1.put("ipType", "private");
//					}
//					else {
//						map1.put("ipType", "public");
//					}

					map1.put("sourceAddress", node.get("assetIP"));

					list2.add(map1);

				}

			}

			map.put("sourceAddress", ip);
			map.put("count", list2.size());
			map.put("ipType", regextype);
			listed.add(map);

		}
		mapper.writerWithDefaultPrettyPrinter().writeValue(output, listed);

		return ResponseEntity.ok(listed);

	}
}
