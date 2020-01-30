import { ApiResponse } from './ApiResponse';
import axios from 'axios';

export class Concert {
    id !: Number;
    name !: String;
    venue !: String;
    maxTickets !: Number;
    price !: Number;
    organizerId !: Number;

    public static Save(event : Concert) : Promise<ApiResponse> {
        return new Promise(function(resolve, reject) {
            const requestBody = JSON.stringify(event);
            axios.post(process.env.VUE_APP_SERVER_URL + '/events', requestBody).then(result => {
                let apiResponse : ApiResponse = new ApiResponse();                    
                if(result.status === 200) {
                    apiResponse.success = true;
                    apiResponse.message = result.data;
                    resolve(apiResponse);
                }
                else {
                    apiResponse.success = false;
                    apiResponse.message = result.data;
                    reject(apiResponse);
                }
            });
        });        
    }

    public static Retrieve() : Promise<Concert[]> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/events').then(result => {
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