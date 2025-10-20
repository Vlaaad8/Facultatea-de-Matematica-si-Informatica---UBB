import express from "express";

export class restService{
    private app = express();
    private port = 3000;
    constructor(){
       this.app.listen(this.port, (error) => {
           if (error) {
           console.error(error)
           }
           else{
               console.log("Server listening on port " + this.port);
           }

       })
    }
}

