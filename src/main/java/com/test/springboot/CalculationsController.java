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
    public Double calculate(@RequestHeader("User-ID") Integer userId ,@RequestBody CalculationRequests.Simple request) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Double firstNum = request.firstNum();
        Double secondNum = request.secondNum();
        String operator = request.operator();
        Double sum = firstNum;

        if (secondNum == null) {
            secondNum = firstNum;
        }

        if (operator.equals("+")) {
            sum += secondNum;
        }else if (operator.equals("-")) {
            sum -= secondNum;
        }else if (operator.equals("*")) {
            sum *= secondNum;
        }else if (operator.equals("/")) {
            if (secondNum != 0) {
                sum /= secondNum;
            }else  {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
        }else {
            throw new IllegalArgumentException("Invalid operator");
        }

        SimpleCalculation calculation = new SimpleCalculation(
                firstNum,
                secondNum,
                operator
        );

        calculation.setResult(sum);

        currentUser.addCalc(calculation);

        calculationsRepository.save(calculation);
        return  calculation.getResult();
    }

    @PostMapping("/revertcalc")
    public Calculations revertCalc(@RequestHeader("User-Id") Integer userId, @PathVariable Integer calculationId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        Calculations newCalculation = calculationsRepository.findById(calculationId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        if (!user.getId().equals(newCalculation.getUser().getId())) {
            throw new SecurityException("You do not have permission to view this calculation");
        }

        return newCalculation;
    }
}
