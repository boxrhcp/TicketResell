import axios from "axios";

export class Ticket {
    id !: Number;
    ownerId !: Number;
    onSale !: Boolean;

    public static Retrieve(eventId: Number) : Promise<Ticket[]> {
        return new Promise(function(resolve, reject) {
            const requestUrl = encodeURI(process.env.VUE_APP_SERVER_URL + '/tickets?eventId=' + eventId.toString());
            console.log(requestUrl);
            axios.get(requestUrl).then(result => {
                if(result.status === 200) {
                    let tickets : Ticket[] = [];
                    result.data.forEach((element: any, index: Number) => {
                        let ticket: Ticket = Object.assign(new Ticket(), element);
                        tickets.push(ticket);
                    });
                    resolve(tickets);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }
}