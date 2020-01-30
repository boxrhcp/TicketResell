import axios from "axios";

export class User {
    id !: Number;
    name !: String;
    etherium_account !: String;

    public static Retrieve() : Promise<User[]> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/Users').then(result => {
                if(result.status === 200) {
                    let users : User[] = [];
                    result.data.forEach((element: any, index: Number) => {
                        let user: User = Object.assign(new User(), element);
                        users.push(user);
                    });
                    resolve(users);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }

    public static RetrieveById(id : Number) : Promise<User> {
        return new Promise(function(resolve, reject) {
            axios.get(process.env.VUE_APP_SERVER_URL + '/Users/' + id).then(result => {
                if(result.status === 200) {
                    let user: User = Object.assign(new User(), result.data);
                    resolve(user);
                }
                else {
                    reject(Error(result.statusText));
                }
            });
        });        
    }
}