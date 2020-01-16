pragma solidity >=0.4.0 <0.7.0;

contract TicketResell {
    
    struct TicketMapping{
        uint timestamp;
        address owner;
        uint price;
    }

    mapping (string => TicketMapping) tickets;

    function createTicket(string memory ticket, uint price) public{
        tickets[ticket] = TicketMapping(block.timestamp, msg.sender, price);
    }

    function getTicketInfo(string calldata ticket) external view returns (uint timestamp, address owner, uint price){
        return (tickets[ticket].timestamp, tickets[ticket].owner, tickets[ticket].price);
    }

    function buyTicket(string memory ticket) payable public{
        require(msg.value == tickets[ticket].price);
        tickets[ticket].owner = msg.sender;
        tickets[ticket].timestamp = block.timestamp;
    }

}