package com.test.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculator")
public class CalculationsController {
    @Autowired
    private final CalculationsRepository calculationsRepository;

    @Autowired
    private final UserRepository userRepository;


    public CalculationsController(CalculationsRepository calculationsRepository, UserRepository userRepository) {
        this.calculationsRepository = calculationsRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/calc")
    public Double calculate(@RequestBody Calculations calculations) {
        User currentUser = userRepository.findById(calculations.getUser().getId()).orElseThrow();

        Double sum = calculations.getFirstNum();

        if (calculations.getSecondNum() == null) {
            calculations.setSecondNum(calculations.getFirstNum());
        }

        if (calculations.getOperator().equals("+")) {
            sum += calculations.getSecondNum();
        }else if (calculations.getOperator().equals("-")) {
            sum -= calculations.getSecondNum();
        }else if (calculations.getOperator().equals("*")) {
            sum *= calculations.getSecondNum();
        }else if (calculations.getOperator().equals("/")) {
            if (calculations.getSecondNum() != 0) {
                sum /= calculations.getSecondNum();
            }else  {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
        }else {
            throw new IllegalArgumentException("Invalid operator");
        }

        calculations.setResult(sum);

        currentUser.addCalc(calculations);

        calculationsRepository.save(calculations);
        return  calculations.getResult();
    }

    @PostMapping("/revertcalc")
    public Calculations revertCalc(@RequestHeader("User-Id") Integer userId, @RequestBody Integer calculationId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        Calculations newCalculation = calculationsRepository.findById(calculationId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        return newCalculation;
    }
}
