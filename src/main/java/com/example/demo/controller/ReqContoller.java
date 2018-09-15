package com.example.demo.controller;


import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by annakim on 5/31/18.
 */
@Controller
public class ReqContoller {

    private Logger logger = Logger.getLogger(getClass().getName());


    private String apiKey = "ak_1MDE2NTgxMDA3YjM1NTIwNzgwOWUwMzczMDAwMDAwMDA0165d6fbe5d1f6ad5e02b6d1000000002133LUNCESIgVa5g8NU7mcChGs4lEcSe";

    private String deviceId = "0165dbd60e650000000000010010027d";
    private String resourcePath = "/3303/0/5700";


    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String reqSrRequest() throws Exception {
        logger.info("/callback");
        JSONObject resObj = new JSONObject();
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", "http://13.125.248.55:5000/response_callback");


            logger.info("req:" + jsonObject.toJSONString());
            JSONObject respObject = new JSONObject();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("authorization", "Bearer " + apiKey);


            HttpEntity<String> entity;

            entity = new HttpEntity<String>(jsonObject.toString(), headers);

            respObject = restTemplate.postForObject("https://api.us-east-1.mbedcloud.com/v2/notification/callback", entity, JSONObject.class);


            logger.info("Return : " + respObject.toJSONString());


        } catch (Exception ex) {
            resObj.put("errMsg", "Unknown Error");
            logger.error(ex.toString());
        }
        return resObj.toJSONString();
    }

    // webhook 수신
    @RequestMapping(value = "/response_callback", method = {RequestMethod.PUT, RequestMethod.HEAD})
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> response_completed(@RequestBody(required = false) String payload) throws Exception {

        logger.info("/response_callback");
        logger.info(payload);
        JSONParser parser = new JSONParser();

//        Object obj = parser.parse(payload);
//        JSONObject jsonObject = (JSONObject) obj;
//        HashMap<String, Object> resourcesObj = (HashMap<String, Object>) jsonObject.get("notifications");
//        String ep = resourcesObj.get("ep").toString();
//        String path = resourcesObj.get("path").toString();
//


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("authorization", "Bearer " + apiKey);

        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.us-east-1.mbedcloud.com/v2/subscriptions/0165dbd60e650000000000010010027d/3303/0/5700", HttpMethod.PUT, new HttpEntity<byte[]>(headers), String.class);

//        JSONObject respObject = (JSONObject) parser.parse(responseEntity.getBody());

        logger.info("Return : " + responseEntity.toString());



        return new ResponseEntity<>("", HttpStatus.OK);
    }





}
