import React from 'react';
import {withStyles} from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import Table from "@material-ui/core/Table";
import TableContainer from "@material-ui/core/TableContainer";

import ExpansionPanelPreview from "./ExpansionPanelPreview";
import IconMsgOk from "../svg/msg-ok.svg";
import IconMsgKo from "../svg/msg-alert.svg";
import {fontSizeLarge, fontSizeNormal, fontSizeSmall} from "../../../layouts/Font";

const styles = theme => ({
    root: {
        minWidth: 275,
    },
    card : {
        minWidth: 275,
        marginBottom: 4,
        borderRadius: 16,
    },

    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },

    pos: {
        marginBottom: 12,
    },
    icon: {
        marginRight: "5px",
        color: "#f7931e",
    },
    hide: {
        visibility: 'hidden',
    },
    xml: {
        maxHeight:250
    },

    msgtype :{
        textAlign: "left",
        color: "black",
        fontWeight: "bold",
        fontSize: fontSizeLarge,
        width: "15%",
        paddingBottom:0,
        borderBottom:0,
        paddingRight: 0,
    },

    localdate :{
        textAlign: "right",
        width: "10%",
        fontSize: fontSizeSmall,
        paddingLeft:0,
        "&:last-child": {
            paddingRight: 0
        },
        paddingBottom:0,
        borderBottom:0,
    },

    fromto: {
        textAlign: "left",
        fontSize: fontSizeNormal,
        paddingTop: 0,
        borderBottom: 0,
        paddingBottom:0
    },

    srvtype :{
        textAlign: "left",
        fontSize: fontSizeNormal,

        borderBottom: 0,
        paddingBottom: 0,
        paddingTop: 0,

    },

});


const messageInfoCard = (props)  => {

    const {classes} = props;
    const msgInfo = props.msgInfo;

    // Direction and From/To
    let direction;
    let fromto;
    if (msgInfo.isSent) {
        direction = "SENT";
        if (msgInfo.to.length > 0) {
            fromto = "To: " + msgInfo.to;
        }
        else {
            fromto = "To: Unknown";
        }
    } else {
        direction = "RECEIVED";
        if (msgInfo.from.length > 0) {
            fromto = "From: " + msgInfo.from;
        }
        else {
            fromto = "From: Unknown";
        }
    }

    // Formatting the Date Time
    const timestamp = new Date(msgInfo.dateTime);
    const msec = timestamp.getMilliseconds();
    let padding = '';
    if (msec < 10) {
        padding ='00';
    }
    else if (msec < 100) {
        padding = '0';
    }
    const localeDate = timestamp.toLocaleString().replace(',', ' ° ')+'.'+padding+msec;

    // Ack Style
    if (!msgInfo.ackResult) {
        msgInfo.ackResult = 'Not Received';
    }
    const isSuccess = msgInfo.ackResult.includes('Success');
    const ackTextColor = isSuccess ? "green" : "red";
    const rowAckCodeStyle = {color:ackTextColor, textAlign:'right'};
  //  const cardStyle = { border: "2px solid " + ackTextColor };

    // Message type with wrong ack result
    let iconMsg = IconMsgOk;
    let messageType = msgInfo.messageType;
    let messageTypeColor = null;
    if (!isSuccess && messageType === 'Ack Synch') {
        messageType = messageType+" - "+msgInfo.ackResult;
        messageTypeColor = {color: "red"};
        iconMsg = IconMsgKo;
    }

    // Rendering
    return (
        <Card className={classes.card} key={msgInfo.id} >
            <CardContent>
                <TableContainer>
                    <Table size="small" aria-label="a dense table">
                        <TableBody>

                            <TableRow>
                                <TableCell className={classes.msgtype} style={messageTypeColor}>
                                    <img src={iconMsg} alt="thn" style={{paddingRight:6, width:22}}/>
                                    {messageType}
                                </TableCell>
                                <TableCell className={classes.localdate}>{localeDate} <strong>{direction}</strong></TableCell>
                            </TableRow>

                            <TableRow>
                                <TableCell className={classes.fromto}>{fromto}</TableCell>
                                <TableCell className={classes.fromto}/>
                            </TableRow>


                            <TableRow>
                                <TableCell className={classes.srvtype}>Service Type : {msgInfo.serviceType}</TableCell>
                                <TableCell className={classes.srvtype}/>
                            </TableRow>

                        </TableBody>
                    </Table>
                </TableContainer>
                <ExpansionPanelPreview body={props.body} numLines={6}/>
            </CardContent>
        </Card>
    );
}

export default withStyles(styles)(messageInfoCard);
