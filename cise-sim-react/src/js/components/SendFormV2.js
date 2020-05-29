import React, {Component} from 'react';
import {Box, Grid, Paper} from '@material-ui/core';
import {observer} from 'mobx-react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import MessageIdField from './SendForm/MessageIdField';
import CorrelationIdField from './SendForm/CorrelationIdField';
import TemplateSelect from './SendForm/TemplateSelect';
import RequiresAckCheck from './SendForm/RequiresAckCheck';
import PreviewButton from './SendForm/PreviewButtonV2';
import SendButton from './SendForm/SendButton';
import PreviewMessage from "./PreviewMessage";

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
class SendFormV2 extends Component {

    constructor(props) {
        super(props);
    }

    getTemplateStore() {
        return this.props.store.templateStore
    };

    getAllStores() {
        return this.props.store
    };


    render() {
        const {classes} = this.props;

        return (
                <Box p="8px" mt="68px" mx="58px" bgcolor="#eeeeee">
                    <Paper  className={classes.root} >
                    <Grid container alignItems="flex-start" spacing={3}>

                        <Grid item xs={12}>
                            <TemplateSelect store={this.getTemplateStore()}/>
                        </Grid>

                        <Grid item xs={6}>
                            <MessageIdField store={this.getTemplateStore()}/>
                        </Grid>

                        <Grid item xs={6}>
                            <CorrelationIdField store={this.getTemplateStore()}/>
                        </Grid>

                        <Grid item xs={6}>
                            <RequiresAckCheck store={this.getTemplateStore()}/>
                        </Grid>

                        <Grid item xs={6}>
                            <Grid container  alignItems="flex-start" justify="flex-end" direction="row">
                                <PreviewButton store={this.getTemplateStore()}/>
                                <SendButton store={this.getAllStores()}/>
                            </Grid>
                        </Grid>

                        <Grid item xs={12}>
                            <PreviewMessage store={this. getAllStores()}/>
                        </Grid>

                    </Grid>
                </Paper>
                </Box>
        )
    }
}

SendFormV2.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SendFormV2);

