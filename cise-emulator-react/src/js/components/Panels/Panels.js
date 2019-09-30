import React, {Component} from 'react';
import SendMessage from './SendMessage/SendMessage';
import PushedMessage from './SendMessage/PushedMessage';
import PulledMessage from './ReceiveMessage/PulledMessage';
import {Grid} from '@material-ui/core';
import {observer} from 'mobx-react';
import messageCandidate from "../../models/message/MessageCandidate";
import MessagePushAPI from "../../models/message/MessagePushAPI";
import MessagePullAPI from "../../models/message/MessagePullAPI";
import {Event,EventStore} from "../../models/EventStore";

@observer
export default class Panels extends Component {
    LastReceivePull;
    PrevReceivePull;
    render() {
        const messagePreview = new MessagePushAPI();
        const messageReceived = new MessagePullAPI();
        const messageStore = new EventStore();

        setInterval(function() {
            messageReceived.timer += 1;
            this.LastReceivePull= messageReceived.pull(); //TODO: include comparison prev/last in messageStore 
            if (this.LastReceivePull!=="" && this.PrevReceivePull!==this.LastReceivePull) {
                messageStore.createEvent(this.LastReceivePull); 
                this.PrevReceivePull=this.LastReceivePull
            }
        }, 3000);

        return (
            <React.Fragment>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <SendMessage
                            store={this.props.store}
                            messageCandidate={messageCandidate}
                            messagePreview={messagePreview}/>
                        <PushedMessage
                            store={this.props.store}
                            messagePreview={messagePreview}/>
                        <PulledMessage
                            store={this.props.store}
                            messageReceived={messageReceived}/>
                    </Grid>
                </Grid>
            </React.Fragment>
        );
    }
}



