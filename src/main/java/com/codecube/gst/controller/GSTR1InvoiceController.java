package com.codecube.gst.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="EVC", description="EVC Otp Controller")
public class GSTR1InvoiceController {

}
