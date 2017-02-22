import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'

import { submitStudy } from './endActions'
import Message from '../message'

export class End extends Component{

	constructor(props){
		super(props);
		this.submitted = false;
	}

	render(){
		return (
			<div>
				<div className="jumbotron text-center">
  					<h3>End of the Survey</h3>
				</div>
				<div className="container text-center">
					{	this.submitted?
						<Message/>:
						(<div>
							<div className="alert alert-info">Please click submit button to finish the survey!</div>
							<button type="button" className="btn btn-primary" onClick={()=>{
								this.props.dispatch(submitStudy(this.props.visit, this.props.questions, this.props.survey))
								this.submitted = true;
								this.forceUpdate();
							}}>Submit!</button>
						</div>
						)
					}
				</div>
				<br/>
            	<br/>
            	<br/>
            	<br/>
            	<br/>
            	<br/>
				<nav className="navbar navbar-default navbar-fixed-bottom">
         			<div className="container">
            			<br/>
            			<p className="text-center"> &copy; Computer Science Department, Rice University</p>
            			<p className="text-center"> Any problem? <a href="mailto:by8@rice.edu?Subject=User study problem" target="_top">Contact us!</a></p>
            			<br/>
          			</div>
        		</nav>
			</div>
		)
	}
}

export default connect((state) => {
	return {
		visit: state.visit,
		questions:state.questions.questions,
		survey: state.survey.answers
	}
})(End)