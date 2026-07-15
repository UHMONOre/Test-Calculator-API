package com.test.springboot;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/complexcalc")
    public Double complexCalc(@RequestHeader("User-ID") Integer userId ,@RequestBody CalculationRequests.Complex request) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (request.expressionString() == null || request.expressionString().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid expression");
        }

        try {
            Expression exp = new ExpressionBuilder(request.expressionString()).build();

            ComplexCalculation calculation = new ComplexCalculation(request.expressionString());

            calculation.setResult(exp.evaluate());
            currentUser.addCalc(calculation);

            calculationsRepository.save(calculation);
            return  calculation.getResult();
        } catch (ArithmeticException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Math error: Cannot divide by zero");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid math format: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred");
        }
    }

    @PostMapping("/revertcalc/{calculationId}")
    public Calculations revertCalc(@RequestHeader("User-Id") Integer userId, @PathVariable("calculationId") Integer calculationId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        Calculations newCalculation = calculationsRepository.findById(calculationId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        if (!user.getId().equals(newCalculation.getUser().getId())) {
            throw new SecurityException("You do not have permission to view this calculation");
        }

        return newCalculation;
    }

    @DeleteMapping("/delete/{calculationid}")
    public void deleteCalc(@RequestHeader("User-Id")  Integer userId, @PathVariable("calculationid") Integer calculationId){
        Calculations calculation  = calculationsRepository.findById(calculationId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        if (calculation.getUser().getId().equals(userId)) {
            calculationsRepository.delete(calculation);
        }else {
            throw new SecurityException("You do not have permission to view this calculation");
        }
    }
}
