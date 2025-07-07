package com.assessment.epicor.controller;

import com.assessment.epicor.model.Request;
import com.assessment.epicor.model.Response;
import com.assessment.epicor.service.EpicorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/epicor")
public class EpicorController {

    private static final Logger logger = LoggerFactory.getLogger(EpicorController.class);

    @Autowired
    private EpicorService epicorService;

    @GetMapping("/call")
    public ResponseEntity<Response> callURl(@RequestParam(required = false) String url) {
        logger.info(" URL: {}", url);
        try {
            Response responseRes = epicorService.fileRead(url);
            return ResponseEntity.ok(responseRes);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
