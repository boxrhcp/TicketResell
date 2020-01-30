package eit.tub.ec.TicketResellBackend.Transaction;

import eit.tub.ec.TicketResellBackend.Contract.ContractService;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotFoundException;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotOnSaleException;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import eit.tub.ec.TicketResellBackend.Ticket.TicketService;
import eit.tub.ec.TicketResellBackend.Transaction.Exception.BlockchainTransactionException;
import eit.tub.ec.TicketResellBackend.User.Exception.UserNotFoundException;
import eit.tub.ec.TicketResellBackend.User.Exception.UserWithoutEthWalletException;
import eit.tub.ec.TicketResellBackend.User.User;
import eit.tub.ec.TicketResellBackend.User.UserRepository;
import eit.tub.ec.TicketResellBackend.User.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private TicketService ticketService;
    private TicketRepository ticketRepository;
    private UserService userService;
    private ContractService contractService;

    public TransactionService(
            TransactionRepository transactionRepository,
            TicketService ticketService,
            TicketRepository ticketRepository,
            UserService userService,
            ContractService contractService) {
        this.transactionRepository = transactionRepository;
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.contractService = contractService;
    }

    @Transactional
    public Transaction processTransaction(Transaction transaction)
            throws
            BlockchainTransactionException,
            TicketNotFoundException {

        Ticket ticket = ticketService.findById(transaction.getTickedId());

        if (!ticket.isOnSale())
            throw new TicketNotOnSaleException(ticket.getId());

        Float price = ticket.getPrice();

        User buyer = userService.findById(transaction.getBuyerId());

        if (buyer.getEthAddress() == null) {
            throw new UserWithoutEthWalletException(buyer.getId());
        }

        try {
            contractService.purchaseTicket(ticket, buyer);
        } catch (Exception e) {
            throw new BlockchainTransactionException(e.getMessage());
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