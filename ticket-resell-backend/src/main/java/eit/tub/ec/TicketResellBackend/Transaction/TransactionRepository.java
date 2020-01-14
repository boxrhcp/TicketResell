package eit.tub.ec.TicketResellBackend.Transaction;

import eit.tub.ec.TicketResellBackend.Home.Home;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {}