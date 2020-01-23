package eit.tub.ec.TicketResellBackend.Transaction;

import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private TransactionService transactionService;
    private TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<?> postTransaction(@RequestBody Transaction transaction) {

        if (transaction.getTickedId() == null
                || transaction.getSellerId() == null
                || transaction.getBuyerId() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIError(HttpStatus.BAD_REQUEST, " The fields ticketId or sellerId, or buyerId can't be null"));
        }

        Transaction processedTransaction = transactionService.processTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(processedTransaction);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public ResponseEntity<?> getTransactions() {

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionRepository.findAll());
    }
}