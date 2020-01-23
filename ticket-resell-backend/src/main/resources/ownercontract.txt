pragma solidity >=0.5.0 <=0.6.1;

import "openzeppelin-solidity/contracts/token/ERC721/ERC721.sol";
import "openzeppelin-solidity/contracts/ownership/Ownable.sol";
import "openzeppelin-solidity/contracts/lifecycle/Pausable.sol";

contract OwnerContract is Ownable, Pausable {

    ERC721 public tickets;
    uint256 public currentPrice;

    event Sent(address indexed owner, uint256 amount, uint256 balance);
    event Received(address indexed buyer, uint tokenId, uint256 amount, uint256 balance);

    /**
    *  The owner of the ticket can create this contract sending the address of the tickets contract and the price
    **/
    constructor(address tickets_contract_address, uint256 price) public {
        require(tickets_contract_address != address(0) && tickets_contract_address != address(this));
        require(price > 0);
        tickets = ERC721(tickets_contract_address);
        currentPrice = price;
    }

    /**
    *  The buyer can call this function to purchase the ticket of the owner,
    *  the owner must have approved this contract to sell it.
    **/
    function buyTicket(uint256 ticketId) public payable whenNotPaused {
        require(msg.sender != address(0) && msg.sender != address(this));
        require(msg.value >= currentPrice);
        address ticketSeller = tickets.ownerOf(ticketId);
        tickets.safeTransferFrom(ticketSeller, msg.sender, ticketId);
        emit Received(msg.sender, ticketId, msg.value, address(this).balance);
    }

    /**
    *  Only the owner of this contract can change the price of this contract
    **/
    function setCurrentPrice(uint256 newPrice) public onlyOwner {
        require(newPrice > 0);
        currentPrice = newPrice;
    }

    /**
    *  Only the owner of this contract can withdraw anywhere the money of this contract
    **/
    function withdraw(address payable owner) public onlyOwner {
        require(owner != address(0) && owner != address(this));
        uint256 amount = address(this).balance;
        owner.transfer(amount);
        emit Sent(owner, amount, address(this).balance);
    }

    /**
    *  Destroys the contract and gives the money to the address specified
    **/
    function destroyContract(address payable owner) public onlyOwner {
        require(owner != address(0) && owner != address(this));
        selfdestruct(owner);
    }

}