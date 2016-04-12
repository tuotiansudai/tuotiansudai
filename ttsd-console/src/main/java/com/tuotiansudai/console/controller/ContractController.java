package com.tuotiansudai.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/contracts")
public class ContractController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> getAllContracts() {
        List contracts = new ArrayList();
        Map<String, String> contract = new HashMap<>();
        contract.put("id", "8907456");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        return contracts;
    }
}
