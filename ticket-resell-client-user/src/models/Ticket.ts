import axios from "axios";
import { ApiResponse } from './ApiResponse';

export class Ticket {
    id !: Number;
    eventId !: Number;
    ownerId !: Number;
    price !: Number;
    onSale !: Boolean;

    public static RetrieveByEvent(eventId: Number) : Promise<Ticket[]> {
        return new Promise(function(resolve, reject) {
            const requestUrl = encodeURI(process.env.VUE_APP_SERVER_URL + '/tickets?eventId=' + eventId.toString());
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

    public static RetrieveByOwner(ownerId: Number) : Promise<Ticket[]> {
        return new Promise(function(resolve, reject) {
            const requestUrl = encodeURI(process.env.VUE_APP_SERVER_URL + '/tickets?ownerId=' + ownerId);
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

    public static Buy(userId : Number, ticketId : Number) : Promise<ApiResponse> {
        return new Promise(function(resolve, reject) {
            let apiResponse = new ApiResponse();
            const requestUrl = encodeURI(process.env.VUE_APP_SERVER_URL + '/transactions');
            axios.post(requestUrl, {
                buyerId: userId,
                tickedId: ticketId 
            }).then(result => {
                if(result.status === 201) {
                    apiResponse.success = true;
                    resolve(apiResponse);
                }
                else {
                    apiResponse.success = false;
                    apiResponse.message = result.statusText;
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

    public static Resell(ticketId : Number) : Promise<ApiResponse> {
        return new Promise(function(resolve, reject) {
            let apiResponse = new ApiResponse();
            const requestUrl = encodeURI(process.env.VUE_APP_SERVER_URL + '/tickets/' + ticketId);
            axios.patch(requestUrl, {
                onSale: true
            }).then(result => {
                if(result.status === 200) {
                    apiResponse.success = true;
                    resolve(apiResponse);
                }
                else {
                    apiResponse.success = false;
                    apiResponse.message = result.statusText;
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
            });
        });
    }

}