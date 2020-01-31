package eit.tub.ec.TicketResellBackend.Contract;

import eit.tub.ec.TicketResellBackend.Contract.Exception.*;
import eit.tub.ec.TicketResellBackend.Ethereum.OwnerContractManager;
import eit.tub.ec.TicketResellBackend.Ethereum.PublishTicket;
import eit.tub.ec.TicketResellBackend.Ethereum.TicketLibrary;
import eit.tub.ec.TicketResellBackend.Ethereum.TicketPaymentManager;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.User.User;
import eit.tub.ec.TicketResellBackend.User.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;


@Service
public class ContractService {
    private ContractRepository contractRepository;
    private UserService userService;


    @Value("${ethereum.connection.url:http://18.194.30.246:8545}")
    private String blockchainUrl;

    @Value("${ethereum.contract.ticket.library:0x0d2139319b5473d7b5bcbd6eae5d92a982532ad8}")
    private String ticketLibraryAddress;

    public ContractService(
            ContractRepository contractRepository,
            UserService userService) {
        this.contractRepository = contractRepository;
        this.userService = userService;
    }

    private TicketLibrary getTicketLibrary(String userPK) {
        return new TicketLibrary(ticketLibraryAddress, blockchainUrl, userPK);
    }

    public Contract findById(Long contractId) {
        Optional<Contract> ownerContractOptional = contractRepository.findById(contractId);
        return ownerContractOptional.orElseThrow(
                () -> new ContractNotFoundException(contractId));
    }

    public Ticket createTicket(Ticket ticket) {
        User owner = userService.findById(ticket.getOwnerId());
        TicketLibrary ticketLibrary = getTicketLibrary(owner.getEthKey());

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
    public void offerTicketForSale(Ticket ticket) {
        User owner = userService.findById(ticket.getOwnerId());

        String forSaleContractAddress;
        try {
            forSaleContractAddress = PublishTicket.deploy(
                    owner.getEthKey(),
                    blockchainUrl,
                    ticketLibraryAddress,
                    ticket.getPrice().floatValue());
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

        this.approveTicketForModification(contract, ticket, owner);
    }

    @Transactional
    private void approveTicketForModification(Contract ownerContract, Ticket ticket, User owner) {
        TicketLibrary ticketLibrary = getTicketLibrary(owner.getEthKey());

        try {
            ticketLibrary.approve(ownerContract.getEthAddress(), BigInteger.valueOf(ticket.getEthId()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainTicketApprovalException(e.getMessage(), ticket.getId());
        }
    }

    @Transactional
    public void cancelTicketForSale(Ticket ticket) {
        Contract ownerContract = this.findById(ticket.getSellContractId());
        User owner = userService.findById(ticket.getOwnerId());

        if(!destroyTicketContract(ownerContract, owner)) {
            throw new BlockchainUnsuccessfulOwnerContractDestructionException(ownerContract.getId(), owner.getId());
        }

        ticket.setSellContractId(null);
    }

    @Transactional
    public void purchaseTicket(Ticket ticket, User buyer) {
        Contract ownerContract = this.findById(ticket.getSellContractId());
        User owner = userService.findById(ticket.getOwnerId());

        if(!changeTicketOwner(ownerContract, ticket, buyer)) {
            throw new BlockchainUnsuccessfulTicketPurchaseException(ticket.getId(), buyer.getId());
        }

        ticket.setOwnerId(buyer.getId());

        if(!destroyTicketContract(ownerContract, owner)) {
            throw new BlockchainUnsuccessfulOwnerContractDestructionException(ownerContract.getId(), owner.getId());
        }

        ticket.setOnSale(false);
        ticket.setSellContractId(null);
    }

    private boolean changeTicketOwner(Contract ownerContract, Ticket ticket, User buyer) {
        TicketPaymentManager ticketPaymentManager = new TicketPaymentManager(
                ownerContract.getEthAddress(),
                this.blockchainUrl,
                buyer.getEthKey());

        boolean success;
        try {
            success = ticketPaymentManager.buyTicket(
                BigInteger.valueOf(ticket.getEthId()),
                ticket.getPrice().floatValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainTicketPurchaseException(e.getMessage(), ticket.getId(), buyer.getId());
        }

        return success;
    }

    @Transactional
    private boolean destroyTicketContract(Contract ownerContract, User owner) {
        OwnerContractManager ownerContractManager = new OwnerContractManager(
                ownerContract.getEthAddress(),
                this.blockchainUrl,
                owner.getEthKey());

        boolean success;
        try {
            success = ownerContractManager.destroyContract();
            ownerContract.setDestroyed(true);
            contractRepository.save(ownerContract);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainTicketOwnerContractDestructionException(
                    e.getMessage(),
                    ownerContract.getId(),
                    owner.getId());
        }

        return success;
    }

    public boolean changeTicketPrice(Ticket ticket) {
        Contract ownerContract = this.findById(ticket.getSellContractId());
        User owner = userService.findById(ticket.getOwnerId());

        OwnerContractManager ownerContractManager = new OwnerContractManager(
                ownerContract.getEthAddress(),
                this.blockchainUrl,
                owner.getEthKey());

        boolean success;
        try {
            success = ownerContractManager.setNewPrice(ticket.getPrice().floatValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainTicketPriceUpdateException(e.getMessage(), ticket.getId(), ticket.getPrice());
        }

        return success;
    }
}
