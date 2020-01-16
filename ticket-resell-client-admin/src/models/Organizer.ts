import axios from "axios";

export class Organizer {
    id !: Number;
    name !: String;
    etherium_account !: String;

    public static Retrieve() : Promise<Organizer[]> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/Organizers').then(result => {
                if(result.status === 200) {
                    let organizers : Organizer[] = [];
                    result.data.forEach((element: any, index: Number) => {
                        let organizer: Organizer = Object.assign(new Organizer(), element);
                        organizers.push(organizer);
                    });
                    resolve(organizers);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }

    public static RetrieveById(id : Number) : Promise<Organizer> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/Organizers/' + id).then(result => {
                if(result.status === 200) {
                    let organizer: Organizer = Object.assign(new Organizer(), result.data);
                    resolve(organizer);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }
}