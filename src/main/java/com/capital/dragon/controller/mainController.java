package com.capital.dragon.controller;

import com.capital.dragon.model.Security;
import com.capital.dragon.service.BlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class mainController {
    private BlpService blpService;
    @Autowired
    public mainController(BlpService blpService) {
        this.blpService = blpService;
    }
    @RequestMapping(method = RequestMethod.POST)
    public List<Security> getSecurityInfo(@RequestParam("data") String data,@RequestBody List<String> sec) throws Exception {
        //sec=sec.stream().map(ex->ex+" Equity").collect(Collectors.toList());
        return blpService.getSecurityListInfo(data,sec);
    }
}
