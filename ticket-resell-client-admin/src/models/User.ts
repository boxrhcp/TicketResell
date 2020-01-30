import axios from "axios";

export class User {
    id !: Number;
    name !: String;
    organizer !: Boolean;

    public static Retrieve() : Promise<User[]> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/users').then(result => {
                if(result.status === 200) {
                    let organizers : User[] = [];
                    result.data.forEach((element: any, index: Number) => {
                        let user: User = Object.assign(new User(), element);
                        if(user.organizer === true) {
                            organizers.push(user);
                        }
                    });
                    resolve(organizers);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }

    public static RetrieveById(id : Number) : Promise<User> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/users/' + id).then(result => {
                if(result.status === 200) {
                    let user: User = Object.assign(new User(), result.data);
                    if(user.organizer === true) {
                        resolve(user);
                    }
                    else {
                        reject(Error("User is not an organizer."));
                    }
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }
}