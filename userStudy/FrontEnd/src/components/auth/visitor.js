import React from 'react'
import { connect } from 'react-redux'
import {url} from '../../actions'
import {visitorAction} from './visitorActions'

//The login section react component

const Login = ({dispatch}) => {
	let email, level;

	return (
		<div className="row">
        	<div className="col-md-3"></div>
        	<div className="col-md-6">
                <div className="form-group">
                    <label for="exampleInputEmail1">Email Address</label>
                    <input type="email" name="email" className="form-control" id="InputEmail1" placeholder="Enter email" required/>
                    <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
                </div>
                <div className="form-group">
                    <label for="exampleSelect1">Java Proficiency Level</label>
                    <select className="form-control" id="exampleSelect1" name="level" required>
                        <option disabled selected value> -- select a level -- </option>
                        <option>Expert</option>
                        <option>Proficient</option>
                        <option>Competent</option>
                        <option>Advanced Beginner</option>
                        <option>Novice</option>
                    </select>
                </div>
                <div className="form-group">
                    <div className="text-center">
                        <input type="submit" className="btn btn-primary" value="Get Started!" onClick={}/>
                    </div>
                </div>   
        	</div>
        	<div class="col-md-3"></div>
    </div>
	)
}

export default connect()(Login)