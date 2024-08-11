package com.mindzone.controller;

import com.mindzone.service.interfaces.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.mindzone.constants.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/payment")
public class PaymentController {

    PaymentService paymentService;

}
