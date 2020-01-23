package eit.tub.ec.TicketResellBackend.Transaction;

import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotFoundException;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import eit.tub.ec.TicketResellBackend.Transaction.Exception.BlockchainTransactionErrorException;
import eit.tub.ec.TicketResellBackend.User.Exception.UserNotFoundException;
import eit.tub.ec.TicketResellBackend.User.User;
import eit.tub.ec.TicketResellBackend.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Transaction processTransaction(Transaction transaction)
            throws
            BlockchainTransactionErrorException,
            TicketNotFoundException {

        Optional<Ticket> ticketOptional = ticketRepository.findById(transaction.getTickedId());
        Ticket ticket = ticketOptional.orElseThrow(() -> new TicketNotFoundException(transaction.getTickedId()));
        Float price = ticket.getPrice();

        Optional<User> sellerOptional = userRepository.findById(transaction.getSellerId());
        User seller = sellerOptional.orElseThrow(() -> new UserNotFoundException(transaction.getSellerId()));

        Optional<User> buyerOptional = userRepository.findById(transaction.getBuyerId());
        User buyer = buyerOptional.orElseThrow(() -> new UserNotFoundException(transaction.getBuyerId()));

        if (!processPayment(ticket.getId(), seller.getEthAccount(), buyer.getEthAccount(), price)) {
            throw new BlockchainTransactionErrorException();
        }

        transaction.setAmount(price);
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        ticket.setOwnerId(buyer.getId());
        ticket.setOnSale(false);
        ticketRepository.save(ticket);

        return transaction;
    }

    private boolean processPayment(Long ticketId, String sellerWallet, String buyerWallet, Float price) {
        // TODO Implement Blockchain ticket reselling logic
        // TODO Call blockchain library etc.
        return true;
    }
}