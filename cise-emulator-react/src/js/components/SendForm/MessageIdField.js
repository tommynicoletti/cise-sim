import {TextField} from "@material-ui/core";
import Tooltip from "@material-ui/core/Tooltip";
import React from "react";
import PropTypes from "prop-types";
import {observer} from "mobx-react";

@observer
export default class MessageIdField extends React.Component {

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.props.store.messageId = event.target.value;
    }

    render() {
        return (
            <Tooltip title={"[Required/Generated] Use this field to set a unique message Identifier"} >
            <TextField
                name="messageId"
                label="Message Id"
                required={true}
                fullWidth={true}
                color="primary"
                variant="outlined"
                value={this.props.store.messageId}
                onChange={this.handleChange}
            />
            </Tooltip>
        );
    }
}

MessageIdField.propTypes = {
    store: PropTypes.object.isRequired,
};
