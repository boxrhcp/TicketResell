package eit.tub.ec.TicketResellBackend.Transaction;

import eit.tub.ec.TicketResellBackend.Contract.ContractRepository;
import eit.tub.ec.TicketResellBackend.Contract.ContractService;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotFoundException;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotOnSaleException;
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
    private ContractService contractService;
    private ContractRepository contractRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository,
            ContractService contractService,
            ContractRepository contractRepository) {
        this.transactionRepository = transactionRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.contractService = contractService;
        this.contractRepository = contractRepository;
    }

    @Transactional
    public Transaction processTransaction(Transaction transaction)
            throws
            BlockchainTransactionErrorException,
            TicketNotFoundException {

        Optional<Ticket> ticketOptional = ticketRepository.findById(transaction.getTickedId());
        Ticket ticket = ticketOptional.orElseThrow(() -> new TicketNotFoundException(transaction.getTickedId()));

        if (!ticket.isOnSale())
            throw new TicketNotOnSaleException(ticket.getId());

        Float price = ticket.getPrice();

        Optional<User> buyerOptional = userRepository.findById(transaction.getBuyerId());
        User buyer = buyerOptional.orElseThrow(() -> new UserNotFoundException(transaction.getBuyerId()));

        try {
            contractService.purchaseTicket(ticket, buyer);
        } catch (Exception e) {
            throw new BlockchainTransactionErrorException(e.getMessage());
        }

        transaction.setAmount(price);
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        ticket.setOwnerId(buyer.getId());
        ticket.setOnSale(false, null);
        ticketRepository.save(ticket);

        return transaction;
    }
}