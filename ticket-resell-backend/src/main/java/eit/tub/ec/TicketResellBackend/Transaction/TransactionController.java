package eit.tub.ec.TicketResellBackend.Transaction;

import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotFoundException;
import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
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

        Transaction processedTransaction;
        ResponseEntity<?> response;
        try {
            processedTransaction = transactionService.processTransaction(transaction);
            response = ResponseEntity.status(HttpStatus.CREATED).body(processedTransaction);
        } catch (TicketNotFoundException e) {
            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIError(HttpStatus.BAD_REQUEST, "No ticket was found with the given ID."));
        }

        return response;
    }
}