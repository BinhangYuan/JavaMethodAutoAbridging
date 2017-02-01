import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'

class OptionItem extends Component{

	constructor(props){
		super(props);
	}

 	render(){
		return(
			<div className="radio">
				<label><input type="radio" name="optradio"/>{this.props.text}</label>
			</div>
		)
	}
}


export default connect()(OptionItem)