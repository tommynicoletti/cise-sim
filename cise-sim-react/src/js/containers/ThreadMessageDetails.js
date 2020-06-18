import React, {Component} from 'react';
import {Box, Grid, Typography} from '@material-ui/core';
import {withStyles} from '@material-ui/core/styles';
import {observer} from "mobx-react";
import MessageInfoCard from "../forms/MessageForm/MessageInfoCard";
import Slide from "@material-ui/core/Slide";
import Logo from "./message-detail-icon.jpg"
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";

const styles = theme => ({
    root: {
        display: 'flex',
        flexWrap: 'wrap',
        padding: 16,
        margin: '16px auto',
        maxWidth: 800
    },
});


@observer
class ThreadMessageDetails extends Component {

    buildAckSuccessFail(threadWithBodyList) {

        const ackType = 'Ack Synch'; // messageType

        let ackResult = [];
        threadWithBodyList.map( (msg) => {
            if (msg.msgInfo.messageType === ackType) {
                ackResult[msg.msgInfo.messageId.split('_')[0]] = msg.msgInfo.ackResult;
            }
        });

        threadWithBodyList.map( (msg) => {
            if (msg.msgInfo.messageType !== ackType) {
                msg.msgInfo.ackResult = ackResult[msg.msgInfo.messageId];
            }
        });
    }

    buildList(classes, messageList) {

        return ( <Slide  direction="right" in={messageList.length>0} unmountOnExit>
                <Grid container className={classes.root}>
                    {messageList.map((msg) =>
                        <MessageInfoCard
                            key={msg.msgInfo.id}
                            msgInfo={msg.msgInfo}
                            body={msg.body}
                        />
                    )}
                </Grid>
            </Slide>

        )
    }

    buildWelcome(classes) {

        return (
            <TableContainer>
                <Table size="small" aria-label="a dense table" style={{marginTop:200}}>
                    <TableBody>
                        <TableRow>
                            <TableCell style={{borderBottom:0}}>
                                <p align="center">
                                <img src={Logo}  width="100" height="100" alt="Select a thread to see details" />
                                </p>
                            </TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell style={{borderBottom:0}}>
                                <Typography align="center" variant="h5">Select a Thread to see details</Typography>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>

            )
    }


    render() {

        const {classes} = this.props;
        const messageList  = this.getMessageStore().threadWithBody;

        // toDo the ordering by timestamp
        //messageList.sort(function(a,b) {return b.msgInfo.dateTime-a.msgInfo.dateTime});
        let showThreadDetails;
        if (messageList.length === 0) {
            showThreadDetails = this.buildWelcome(classes);
        }
        else {
            this.buildAckSuccessFail(messageList);
            showThreadDetails = this.buildList(classes, messageList);
        }

        return (
            <Box p="8px" mt="20px" mx="58px">
                {showThreadDetails}
            </Box>
        )
    }

    getMessageStore() {
        return this.props.store.messageStore;
    }

}

export default withStyles(styles)(ThreadMessageDetails);