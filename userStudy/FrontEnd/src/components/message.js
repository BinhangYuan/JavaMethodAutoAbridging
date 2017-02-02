import React, {PropTypes} from 'react'
import { connect } from 'react-redux'

import Action, {displayErrorMsg, displaySuccessMsg} from '../actions'

//React component to show messages

export const Message = ({errorMsg, successMsg}) => (
	<div className="text-center">
		{errorMsg===''?'':
			<div id="error_message" className="alert alert-danger"> {errorMsg} </div>
		}
		{successMsg===''?'':
			<div id="success_message" className="alert alert-success"> {successMsg} </div>
		}
	</div>
)

Message.PropTypes = {
	errorMsg: PropTypes.string.isRequired,
	successMsg: PropTypes.string.isRequired
}

export default connect((state) => {
	return {
		errorMsg: state.shared.errorMsg,
		successMsg: state.shared.successMsg
	}
})(Message)