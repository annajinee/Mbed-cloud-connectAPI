package com.example.demo.controller;


import com.example.demo.model.Bumps;
import com.example.demo.model.dao.BumpsRepo;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by annakim on 9/15/18.
 */
@Controller
public class ReqContoller {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private BumpsRepo bumpsRepo;


    private String apiKey = "ak_1MDE2NTgxMDA3YjM1NTIwNzgwOWUwMzczMDAwMDAwMDA0165d6fbe5d1f6ad5e02b6d1000000002133LUNCESIgVa5g8NU7mcChGs4lEcSe";


    private String resourcePath = "3303/0/5700";
    private String resourcePath_gps = "3201/0/5853";


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

        if (payload != null) {

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(payload);
            JSONObject jsonObject = (JSONObject) obj;
            HashMap<String, Object> resourcesObj = (HashMap<String, Object>) obj;

            if (resourcesObj != null && resourcesObj.get("notifications") != null) {

                List<HashMap> items = new ArrayList<HashMap>((Collection<? extends HashMap>) resourcesObj.get("notifications"));

                for (int i = 0; i < items.size(); i++) {

                    String ep = (String) items.get(i).get("ep");
                    String path = (String) items.get(i).get("path");
                    String retpayload = (String) items.get(i).get("payload");
                    logger.info("Return!!" + ep + ", " + path + ", " + retpayload);

                    if (path.equals("/3303/0/5700")) {

                        byte[] decodedBytes = Base64.getDecoder().decode(retpayload);
                        String decodedString = new String(decodedBytes);
                        logger.info("decode : " + decodedString);

//                        RestTemplate restTemplate = new RestTemplate();
//                        HttpHeaders headers = new HttpHeaders();
//                        headers.setContentType(MediaType.APPLICATION_JSON);
//                        headers.add("authorization", "Bearer " + apiKey);

//                        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.us-east-1.mbedcloud.com/v2/subscriptions/" + ep + "/" + resourcePath, HttpMethod.PUT, new HttpEntity<byte[]>(headers), String.class);
//
//                        logger.info("return : " + responseEntity.toString());


                    }

                    if (path.equals("/3201/0/5853")) {
                        byte[] decodedBytes = Base64.getDecoder().decode(retpayload);
                        String decodedString = new String(decodedBytes);
                        logger.info("decode : " + decodedString);

                        String[] splitData = decodedString.split("id\":");
                        String data = "{\"id\":" + splitData[1] + "id\":" + splitData[2];

//                        logger.info("makeJsonData : " +data);


                        JSONParser parser2 = new JSONParser();

                        Object objGps = parser2.parse(String.valueOf(data));
                        JSONObject jsonObjGps = (JSONObject) objGps;

                        long time = System.currentTimeMillis();
                        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd HHmm");
                        String regTim = dayTime.format(new Date(time));

                        Bumps bumps = new Bumps();
                        bumps.setLat(jsonObjGps.get("lat").toString());
                        bumps.setLon(jsonObjGps.get("lon").toString());
                        bumps.setVel(jsonObjGps.get("vel").toString());
                        bumps.setAcc_x(jsonObjGps.get("acc_x").toString());
                        bumps.setAcc_y(jsonObjGps.get("acc_y").toString());
                        bumps.setAcc_z(jsonObjGps.get("acc_z").toString());
                        bumps.setVehicle(jsonObjGps.get("vehicle").toString());
                        bumps.setPlate(jsonObjGps.get("plate").toString());
                        bumps.setType(jsonObjGps.get("type").toString());
                        bumps.setCuid(jsonObjGps.get("cuid").toString());
                        bumps.setDateAdded(regTim);
                        bumpsRepo.save(bumps);
                        logger.info("success to insert!");

                    }

                }
            }

        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }


    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String selectAll() throws Exception {
        logger.info("/selectAll");
        bumpsRepo.deleteByLatIsNull();

        List<Bumps> bumps = bumpsRepo.findAll();
        logger.info("result : " + bumps);

        JSONObject retObj = new JSONObject();
        JSONArray retArray = new JSONArray();


        for(int i=0; i<bumps.size(); i++){
            retArray.add(bumps.get(i));
        }
        retObj.put("data",retArray);

        return retObj.toJSONString();
    }

    @RequestMapping(value = "/selectByRegdate", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String selectByRegdate(@RequestBody(required = false) String payload) throws Exception {
        logger.info("/selectByRegdate : " + payload);

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(payload);
        JSONObject jsonObject = (JSONObject) obj;
        String fromDate = jsonObject.get("fromDate").toString()+" 0000";
        String toDate = jsonObject.get("toDate").toString()+" 2359";

        List<Bumps> bumps = bumpsRepo.findByDateAddedBetween(fromDate, toDate);
        logger.info("result : "+bumps.toString());

        JSONObject retObj = new JSONObject();
        JSONArray retArray = new JSONArray();


        for(int i=0; i<bumps.size(); i++){
            retArray.add(bumps.get(i));
        }
        retObj.put("data",retArray);

        return retObj.toString();
    }
}
