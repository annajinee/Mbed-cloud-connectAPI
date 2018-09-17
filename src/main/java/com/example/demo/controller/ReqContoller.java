package com.example.demo.controller;


import com.example.demo.model.Bumps;
import com.example.demo.model.LocationEntity;
import com.example.demo.model.dao.BumpsRepo;
import com.example.demo.model.dao.LocationRepository;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
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
    @Autowired
    private LocationRepository locationRepo;

    private String apiKey = "apiKey";


    private String resourcePath = "path1";
    private String resourcePath_gps = "path2";


    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String reqSrRequest() throws Exception {
        logger.info("/callback");
        JSONObject resObj = new JSONObject();
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", "call-backURL");


            logger.info("req:" + jsonObject.toJSONString());
            JSONObject respObject = new JSONObject();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("authorization", "Bearer " + apiKey);


            HttpEntity<String> entity;

            entity = new HttpEntity<String>(jsonObject.toString(), headers);

            respObject = restTemplate.postForObject("requestURL", entity, JSONObject.class);


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

                    if (path.equals(resourcePath)) {

                        byte[] decodedBytes = Base64.getDecoder().decode(retpayload);
                        String decodedString = new String(decodedBytes);
                        logger.info("decode : " + decodedString);

                    }

                    if (path.equals(resourcePath_gps)) {
                        byte[] decodedBytes = Base64.getDecoder().decode(retpayload);
                        String decodedString = new String(decodedBytes);
                        logger.info("decode : " + decodedString);


                        JSONParser parser2 = new JSONParser();

                        Object objGps = parser2.parse(String.valueOf(decodedString));
                        JSONObject jsonObjGps = (JSONObject) objGps;

                        long time = System.currentTimeMillis();
                        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd HHmm");
                        String regTim = dayTime.format(new Date(time));
                        String lon = "";
                        try {
                           lon = jsonObjGps.get("lon").toString();
                            Bumps bumps = new Bumps();
                            bumps.setLat(jsonObjGps.get("lat").toString());
                            bumps.setLon(lon);
                            bumps.setVel(jsonObjGps.get("vel").toString());
                            bumps.setAcc_x(jsonObjGps.get("acc_x").toString());
                            bumps.setAcc_y(jsonObjGps.get("acc_y").toString());
                            bumps.setAcc_z(jsonObjGps.get("acc_z").toString());
                            bumps.setVehicle(jsonObjGps.get("vehicle").toString());
                            bumps.setPlate(jsonObjGps.get("plate").toString());
                            bumps.setType(jsonObjGps.get("type").toString());
                            bumps.setCuid(regTim);
                            bumps.setDateAdded(regTim);


                            List<LocationEntity> entities = new ArrayList<>();
                                final GeoJsonPoint locationPoint = new GeoJsonPoint(
                                        Double.valueOf(lon),
                                        Double.valueOf(jsonObjGps.get("lat").toString()));

                                entities.add(new LocationEntity("s", locationPoint));

                           bumps.setLocationEntity(entities);

                            bumpsRepo.save(bumps);
                            logger.info("success to insert!");

                        } catch (Exception ex) {
                        }
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

            Bumps bum = bumps.get(i);

            JSONObject dataObj = new JSONObject();
            dataObj.put("lat", bum.getLat());
            dataObj.put("lon", bum.getLon());
            dataObj.put("vel", bum.getVel());
            dataObj.put("acc_x", bum.getAcc_x());
            dataObj.put("acc_y", bum.getAcc_y());
            dataObj.put("acc_z", bum.getAcc_z());
            dataObj.put("vehicle", bum.getVehicle());
            dataObj.put("plate", bum.getPlate());
            dataObj.put("type", bum.getType());
            dataObj.put("cuid", bum.getCuid());
            dataObj.put("dateAdded", bum.getDateAdded());
            retArray.add(dataObj);
        }
        retObj.put("data",retArray);

        return retObj.toString();
    }

    @RequestMapping(value = "/checkIfDanger", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String checkIfDanger(@RequestBody(required = false) String payload) throws Exception {
        logger.info("/checkIfDanger : " + payload);

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(payload);
        JSONObject jsonObject = (JSONObject) obj;
        String lat = jsonObject.get("lat").toString();
        String lon = jsonObject.get("lon").toString();

        return String.valueOf(this.locationRepo.findBySubjectAndLocationNear("s",
                new Point(Double.valueOf(lon), Double.valueOf(lat)),
                new Distance(5, Metrics.KILOMETERS)));

    }


}
