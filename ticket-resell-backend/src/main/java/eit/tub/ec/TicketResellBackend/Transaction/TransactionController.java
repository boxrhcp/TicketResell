package eit.tub.ec.TicketResellBackend.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<?> postTransaction(@RequestBody Transaction transaction) {

        Transaction processedTransaction = transactionService.processTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(processedTransaction);
    }
}