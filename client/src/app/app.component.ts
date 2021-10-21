import { Component } from '@angular/core';
import { WebSocketAPI } from './WebSocketAPI';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'websocket-client-code-exercise';

  webSocketAPI: WebSocketAPI;
  messages: {client: boolean, message: string}[]= [];
  name: string;
  ngOnInit() {
    this.webSocketAPI = new WebSocketAPI(this);
  }

  connect(){
    this.webSocketAPI._connect();
  }

  disconnect(){
    this.webSocketAPI._disconnect();
  }

  sendMessage = () => {
    this.messages.push({client: true, message: this.name});
    this.webSocketAPI._send(this.name);
  }

  handleMessage = (message) => {
    this.messages.push({client: false, message: (JSON.parse(JSON.parse(message))).content});
  }
}
