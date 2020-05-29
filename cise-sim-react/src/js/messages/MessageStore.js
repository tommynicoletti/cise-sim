import {action, computed, observable} from 'mobx';
import {
    pullMessage,
    pullMessageByHistoryId,
    pullMessageByHistoryIdFull,
    pullMessageHistoryAfter,
    sendMessage
} from './MessageService';
import Message from './Message';
import MessageEasy from "./MessageEasy";
import MessageThInfo from "./MessageThInfo";

export default class MessageStore {

    @observable sentMessage          = new Message({body: "", acknowledge: ""});
    @observable receivedMessage      = new Message({body: "", acknowledge: ""});
    @observable receivedMessageError = null;

    @observable historyMsgList       = [];
    historyLasTimestamp = 0;
    historyMaxCapacity = 0;

    @observable threadMessageDetails = [];
    @observable threadWithBody = [];
    @observable threadIdSelected = "";

    @computed
    get isSentMessagePresent() {
        return !(this.sentMessage);
    }

    @computed
    get isReceivedMessagePresent() {
        return !(this.sentMessage);
    }

    @action
    setSentMessage(sentMessage) {
        this.sentMessage = sentMessage;
    }

    @action
    setReceivedMessage(receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    @action
    consumeErrorMessage() {
        this.receivedMessageError = null;
    }


    setHistoryMaxCapacity(maxLength) {
        if (this.historyMaxCapacity !== maxLength) {
            this.historyMaxCapacity = maxLength;
            this.threadMaxCapacity = maxLength;
            this.historyLasTimestamp = 0

            console.log("setHistoryMaxCapacity to "+maxLength)
        }
    }

    /*
    @action
    updateHistorySecure(newChunkMsgShortInfoRcv) {

        // Adding new data to old ones and find the most recent timestamp
        const newList = [...this.historyMsgList];

        newChunkMsgShortInfoRcv.forEach(t => {
            newList.push(t);
            if (t.dateTime > this.historyLasTimestamp) this.historyLasTimestamp = t.dateTime;
        });

        // Do the ordering by timestamp
        newList.sort(function(a,b) {return b.dateTime-a.dateTime});

        // take only the first historyMaxCapacity item
        this.historyMsgList = newList.slice(0, this.historyMaxCapacity);
    }
    */

    @action
    updateHistory(newChunkMsgShortInfoRcv) {

        const newList = [...newChunkMsgShortInfoRcv, ...this.historyMsgList];
        this.historyLasTimestamp = newList[0].dateTime;
        this.historyMsgList = newList.slice(0, this.historyMaxCapacity);
    }


    @action
    clearHistory() {
        this.historyMsgList = [];
    }

    @action
    updateThreadDetails(newMessagesThread) {
        this.threadMessageDetails = newMessagesThread;
    }

    @action
    updateThreadWithBody(newThreadWithBody) {
        this.threadWithBody = newThreadWithBody;
    }

    @action
    updateThreadIdSelected(correlationId) {
        this.threadIdSelected = correlationId;
    }

    async send(seletedTemplate, messageId, correlationId, requiresAck) {
        const sendMessageResponse = await sendMessage(
            seletedTemplate,
            messageId,
            correlationId,
            requiresAck);
        console.log("sendMessageResponse:", sendMessageResponse);

        if (sendMessageResponse.errorCode) {
            console.log("Send returned an error.");
        } else {
            console.log("Send returned successfully.");
            this.sentMessage = sendMessageResponse;
        }
        return sendMessageResponse;
    }


    startPull() {
        this.interval = setInterval(async function (that)
         {
            const pullMessageResponse = await pullMessage();
            if (!pullMessageResponse) return;
            if (pullMessageResponse.errorCode) {
                that.receivedMessageError = pullMessageResponse;
            } else {
                that.receivedMessage = pullMessageResponse;
            }
        }, 3000, this);
    }

    startPullHistoryProgressive() {
        this.interval = setInterval(async function (that)
        {
            const pullMessageResponse = await pullMessageHistoryAfter(that.historyLasTimestamp);
            if (!pullMessageResponse) return;
            if (pullMessageResponse.errorCode) {
                that.receivedMessageError = pullMessageResponse;
            } else {
                that.updateHistory(pullMessageResponse);
            }
        }, 3000, this);
    }

    async getByShortInfoId(shortInfoId) {
        const messageResponse = await pullMessageByHistoryId(shortInfoId);
        if (!messageResponse) return;
        if (messageResponse.errorCode) {
            this.receivedMessageError = messageResponse;
        } else {
            this.receivedMessage = new MessageEasy(messageResponse);
        }
    }

    async getThreadWithBodyOLD(newMessagesThread) {

        let result = [];

        let msgInfo;
        for (msgInfo of newMessagesThread) {
            const body = await pullMessageByHistoryId(msgInfo.id);
            if (!body || body.errorCode) {
                result.push(new MessageThInfo(msgInfo, "Message body not available"));
            }else {
                result.push(new MessageThInfo(msgInfo, body));
            }
        }

        this.updateThreadWithBody(result);
    }

    async getThreadWithBody(newMessagesThread) {

        let result = [];
        let requestPromises = [];

        let msgInfo;
        for (msgInfo of newMessagesThread) {
            const req = pullMessageByHistoryIdFull(msgInfo);
            requestPromises.push(req);
        }

        const requestResult = await Promise.all(requestPromises);
        let response;
        for (response of requestResult) {
            result.push(response)
        }

        this.updateThreadWithBody(result);
    }
}