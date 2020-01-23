pragma solidity >=0.5.0 <=0.6.1;

import "openzeppelin-solidity/contracts/token/ERC721/ERC721Full.sol";
import "openzeppelin-solidity/contracts/drafts/Counters.sol";

contract Tickets is ERC721Full {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    constructor() ERC721Full("Tickets", "TCK") public {
    }

    /**
    *  The caller of this function creates a ticket in this contract. This ticket will get a unique ID and it will set
    *  the caller as owner of the ticket. It expects a URI that will retrieve the metadata information of the ticket.
    **/
    function createTicket(string memory tokenURI) public returns (uint256) {
        _tokenIds.increment();

        uint256 newTicketId = _tokenIds.current();
        _mint(msg.sender, newTicketId);
        _setTokenURI(newTicketId, tokenURI);

        return newTicketId;
    }
}