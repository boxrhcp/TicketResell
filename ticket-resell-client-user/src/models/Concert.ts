import axios from 'axios';

export class Concert {
    id !: Number;
    name !: String;
    venue !: String;
    maxTickets !: Number;
    availableTickets : Number = 100; // TODO : Update after API
    price !: Number;
    datetime : Number = Date.now();// TODO : Update after API
    organizerId !: Number;

    public static Retrieve() : Promise<Concert[]> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/Events').then(result => {
                if(result.status === 200) {
                    let events : Concert[] = [];
                    result.data.forEach((element: any, index: Number) => {
                        let event: Concert = Object.assign(new Concert(), element);
                        events.push(event);
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
            axios.get(process.env.VUE_APP_SERVER_URL + '/Events/' + id).then(result => {
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