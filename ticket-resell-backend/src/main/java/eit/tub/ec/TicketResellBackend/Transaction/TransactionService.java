package eit.tub.ec.TicketResellBackend.Transaction;

import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotFoundException;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import eit.tub.ec.TicketResellBackend.Transaction.Exception.BlockchainTransactionErrorException;
import eit.tub.ec.TicketResellBackend.User.User;
import eit.tub.ec.TicketResellBackend.User.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Transaction processTransaction(Transaction transaction)
            throws
            BlockchainTransactionErrorException,
            TicketNotFoundException {

        // TODO Implement ticket reselling logic
        // TODO after a transaction, the ticket should change to onSale=false

        Optional<Ticket> ticketOptional = ticketRepository.findById(transaction.getTickedId());
        Ticket ticket = ticketOptional.orElseThrow(() -> new TicketNotFoundException(transaction.getTickedId()));
        Float price = ticket.getPrice();


        // TODO check that these fields exist if not, return 400 Bad Request
        Optional<User> seller = userRepository.findById(transaction.getSellerId());
        Optional<User> buyer = userRepository.findById(transaction.getBuyerId());
        String sellerWallet = "hardcodedWalletId1"; // seller.getWalletId();
        String buyerWallet = "hardcodedWalletId2"; // buyer.getWalletId();

        if (!processPayment(ticket.getId(), sellerWallet, buyerWallet, price)) {
            throw new BlockchainTransactionErrorException();
        }

        transaction.setAmount(price);
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return transaction;
    }

    private boolean processPayment(Long ticketId, String sellerWallet, String buyerWallet, Float price) {
        // TODO Implement Blockchain ticket reselling logic
        // TODO Call blockchain library etc.
        return true;
    }
}