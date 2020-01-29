package eit.tub.ec.TicketResellBackend.Contract;

import eit.tub.ec.TicketResellBackend.Ethereum.Exception.BlockchainContractNotFoundException;
import eit.tub.ec.TicketResellBackend.Ethereum.Exception.BlockchainTicketApprovalException;
import eit.tub.ec.TicketResellBackend.Ethereum.Exception.BlockchainTicketCreationException;
import eit.tub.ec.TicketResellBackend.Ethereum.Exception.BlockchainTicketPublishingException;
import eit.tub.ec.TicketResellBackend.Ethereum.PublishTicket;
import eit.tub.ec.TicketResellBackend.Ethereum.TicketLibrary;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.User.Exception.UserNotFoundException;
import eit.tub.ec.TicketResellBackend.User.User;
import eit.tub.ec.TicketResellBackend.User.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Optional;


@Service
public class ContractService {
    private ContractRepository contractRepository;
    private UserRepository userRepository;

    @Value("${ethereum.connection.url:http://18.194.30.246:8545}")
    private String blockchainUrl;

    @Value("${ethereum.contract.ticket.library:0x0d2139319b5473d7b5bcbd6eae5d92a982532ad8}")
    private String ticketLibraryAddress;

    @Value("${ethereum.admin.pk:0x8919ebf1ab2451f7b7f1b1428dc9f81a34eb9aecef4580b275ed57030c23451a}")
    private String adminPK;

    private TicketLibrary ticketLibrary;

    public ContractService(ContractRepository contractRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    @Transactional
    public void postConstruct() {
        ticketLibrary = new TicketLibrary(ticketLibraryAddress, blockchainUrl, adminPK);

        Contract contract = new Contract();
        contract.setOwnerId(0L);
        contract.setEthAddress(ticketLibraryAddress);
        contract.setType(ContractType.TICKET_LIBRARY);
        contractRepository.save(contract);
    }

    public Ticket createTicket(Ticket ticket) {
        try {
            ticketLibrary.createTicket(ticket.getEthUri());
            ticket.setEthId(ticketLibrary.getLastTicketId().longValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainTicketCreationException(
                    e.getMessage(),
                    ticket.getId(),
                    ticket.getEventId(),
                    ticket.getOwnerId(),
                    ticket.getPrice());
        }

        return ticket;
    }

    @Transactional
    public Ticket offerTicketForSale(Ticket ticket) {
        Optional<User> ownerOptional = userRepository.findById(ticket.getOwnerId());
        User owner = ownerOptional.orElseThrow(() -> new UserNotFoundException(ticket.getOwnerId()));

        String forSaleContractAddress;
        try {
            forSaleContractAddress = PublishTicket.deploy(
                    owner.getEthKey(),
                    blockchainUrl,
                    ticketLibraryAddress,
                    BigInteger.valueOf(ticket.getPrice().longValue()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainTicketPublishingException(e.getMessage(), ticket.getId());
        }

        Contract contract = new Contract();
        contract.setOwnerId(ticket.getOwnerId());
        contract.setEthAddress(forSaleContractAddress);
        contract.setType(ContractType.TICKET_OWNER);
        contract = contractRepository.save(contract);

        ticket.setSellContractId(contract.getId());
        return ticket;
    }

    @Transactional
    public void approveTicketForModification(Ticket ticket) {
        Optional<Contract> contractOptional = contractRepository.findById(ticket.getSellContractId());
        Contract contract = contractOptional.orElseThrow(
                () -> new BlockchainContractNotFoundException(ticket.getSellContractId()));

        try {
            ticketLibrary.approve(contract.getEthAddress(), BigInteger.valueOf(ticket.getEthId()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainTicketApprovalException(e.getMessage(), ticket.getId());
        }
    }
}
