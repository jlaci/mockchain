package hu.jlaci.mockchain.transaction.api;

import hu.jlaci.mockchain.transaction.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity addTestTransaction(@RequestParam(required = false) Integer amount) {

        if (amount != null) {
            int last = 0;
            for (int i = 0; i < amount; i++) {
                last = transactionService.createTestTransaction();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(last);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTestTransaction());
        }
    }
}
