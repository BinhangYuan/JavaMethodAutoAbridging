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
					<a href="#" className="alert alert-info">{"Question "+this.props.index}</a>:
					<a href="#">{"Question "+this.props.index}</a>
				}			
			</li>
		)
	}
}


export default connect()(ListItem)