import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'

class ListItem extends Component{

	constructor(props){
		super(props);
	}

 	render(){
		return(
			<li>
				{this.props.active?
					<a href="#" className="alert alert-info">{this.props.title}</a>:
					<a href="#">{this.props.title}</a>
				}			
			</li>
		)
	}
}


export default connect()(ListItem)