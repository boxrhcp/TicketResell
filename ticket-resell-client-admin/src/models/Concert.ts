import { ApiResponse } from './ApiResponse';
import axios from 'axios';

export class Concert {
    id !: Number;
    name !: String;
    date !: Date;
    place !: String;
    price !: Number;
    organizerId !: Number;
    ntickets !: Number;

    public static Save(event : Concert) : Promise<ApiResponse> {
        return new Promise(function(resolve, reject) {
            console.log(JSON.stringify(event));
            let apiResponse : ApiResponse = new ApiResponse();       
            axios.post(process.env.VUE_APP_SERVER_URL + '/events', event).then(result => {
                             
                if(result.status === 201) {
                    apiResponse.success = true;
                    apiResponse.message = result.data;
                    resolve(apiResponse);
                }
                else {
                    apiResponse.success = false;
                    apiResponse.message = result.data;
                    reject(apiResponse);
                }
            }).catch(error => {
                apiResponse.success = false;
                if(error.response) {
                    apiResponse.message = error.response.data.message;
                }
                else {
                    apiResponse.message = error.message;
                }
                reject(apiResponse);
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