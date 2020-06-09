import React, {Component} from 'react';
import {withStyles} from '@material-ui/core/styles';

import IncidentSelect from "./IncidentSelectorInfo";
import Tooltip from "@material-ui/core/Tooltip";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import Typography from "@material-ui/core/Typography";
import TextField from "@material-ui/core/TextField";
import Paper from "@material-ui/core/Paper";

const styles = theme => ({
    root: {
        display: 'flex',
        flexWrap: 'wrap',
        padding: 16,
        margin: '16px auto',
        maxWidth: 800
    },

    button: {
        margin: theme.spacing(1),
    },

    rightIcon: {
        marginLeft: theme.spacing(1),
    },
    icon : {
        padding: 0,
        margin: 0,
        color: "red",
    },
    cellicon : {
        padding: 4,
        width: 16,
    }


});

class IncidentMainInfoInput extends Component {

    state = {

        incidentType: undefined,

        isValidLatitude:false,
        isValidLongitude:false,

        listIdVessel: [0],
        lastIdVessel: 0,

        listIdContent: [0],
        lastIdContent: 0,
    }

    constructor(props) {
        super(props);
    }

    getIncidentStore() {
        return this.props.store.incidentStore
    };

    // Incident Type
    handleChangeType = (event) => {

        this.setState({incidentType: event.target.value})

        //  this.setState((prevState) => {return {incidentType: event.target.value};});

        this.getIncidentStore().getIncidentInputInfo().incidentType =  event.target.value;
    }

    getSelectType() {
        const list = this.getIncidentStore().labelIncidentType
        return <IncidentSelect
            title="Incident"
            listValueLabel={list}
            change={this.handleChangeType}
        />
    }

    // Sub Type
    handleChangeSubType = (event) => {
        this.getIncidentStore().getIncidentInputInfo().subType =  event.target.value;
    }

    getSelectSubType() {
        let list =[];
        if (this.state.incidentType) {
            list = this.getIncidentStore().labelIncidentSubTypeList[this.state.incidentType];
        }
        else {
            list[0] = {value:'empty', label:'Select Incident'};
        }

        return <IncidentSelect
            title="Sub Type"
            listValueLabel={list}
            change={this.handleChangeSubType}
        />

    }

    // Latitude
    handleChangeLatitude = (event) => {
        const isValid = isFinite(event.target.value) && Math.abs(event.target.value) <= 90;
        if (isValid) {
            this.getIncidentStore().getIncidentInputInfo().latitude =  event.target.value;
        }
        this.setState({isValidLatitude:isValid});
    }

    getLatitudineInput() {

        return  <Tooltip title={"Insert the Latitude value"} >
            <TextField
                name="latitudeId"
                label="Latitude"
                fullWidth={true}
                color="primary"
                variant="outlined"
                // value={this.state.latitude}
                onChange={this.handleChangeLatitude}
                error={!this.state.isValidLatitude}
            />
        </Tooltip>
    }

    // Longitude
    handleChangeLongitude = (event) => {

        const isValid = isFinite(event.target.value) && Math.abs(event.target.value) <= 180;
        if (isValid) {
            this.getIncidentStore().getIncidentInputInfo().longitude =  event.target.value;
        }
        this.setState({isValidLongitude:isValid})
    }

    getLongitudeInput() {
        return  <Tooltip title={"Insert the Longitude value"} >
            <TextField
                name="longitudeId"
                label="Longitude"
                fullWidth={true}
                color="primary"
                variant="outlined"
                // value={this.state.longitude}
                onChange={this.handleChangeLongitude}
                error={!this.state.isValidLongitude}
            />
        </Tooltip>
    }


    render() {

        const {classes} = this.props;

        return (
            <TableContainer component={Paper} >

                <Typography variant="h6" component="h6" align={"left"}>
                    Incident main info
                </Typography>

                <Table size="small" aria-label="a dense table">
                    <TableBody>

                        <TableRow>

                            <TableCell>
                                {this.getSelectType()}
                            </TableCell>

                            <TableCell>
                                {this.getSelectSubType()}
                            </TableCell>

                        </TableRow>

                        <TableRow>
                            <TableCell>
                                {this.getLatitudineInput()}
                            </TableCell>

                            <TableCell>
                                {this.getLongitudeInput()}
                            </TableCell>

                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        )
    }
}

export default withStyles(styles)(IncidentMainInfoInput);