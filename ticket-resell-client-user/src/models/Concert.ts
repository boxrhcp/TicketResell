import axios from 'axios';
import { Ticket } from './Ticket';

export class Concert {
    id !: Number;
    name !: String;
    date !: Date;
    place !: String;
    price !: Number;
    organizerId !: Number;
    ntickets !: Number; 
    availableTickets !: Number;

    public static Retrieve() : Promise<Concert[]> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/events').then(result => {
                if(result.status === 200) {
                    let events : Concert[] = [];
                    result.data.forEach((element: any, index: Number) => {
                        let event: Concert = Object.assign(new Concert(), element);
                        Ticket.RetrieveUnSold(event.id).then(t => {
                            event.availableTickets = t.length;
                            events.push(event);
                        });
                    });
                    resolve(events);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }

    public static RetrieveById(id : Number) : Promise<Concert> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/events/' + id).then(result => {
                if(result.status === 200) {
                    let event: Concert = Object.assign(new Concert(), result.data);
                    resolve(event);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }

}