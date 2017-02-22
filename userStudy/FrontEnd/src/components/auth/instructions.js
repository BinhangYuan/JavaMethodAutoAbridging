import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'

class Instructions extends Component{

	constructor(props){
		super(props);
	}

 	render(){
		return(
			<div className="row">
				<div className="col-md-2"></div>
				<div className="col-md-8 text-justify">
					<h3 className="text-center"> Instructions </h3>
					<p> Our research tries to reduce source code in Java methods so that large amounts of queried Java code can be displayed by limited space without hindering human comprehension.</p>
					<p> In this user study, you are invited to finish 3 kinds of questions:</p>
					<ul>
  						<li>
  							<b>Select 1 from 5 Java methods based on a natural language description</b>: 
  							You will first be provided 2 examples for practice, an original version and a reduced version of the same Java method. You could play with these examples as much as you want. 
  							After that, you should finish 6 questions, alternating between non-reduced code and reduced code by 2 different methods. Please stay focused and try your best to find the correct answer.
  							Your accuracy and react time are the key factors in our study.
  						</li>
  						<li>
  							<b>Match 2 Java methods with 2 natural language descriptions</b>:
  							You will also be provided two examples for practice, an original version and a reduced version of the same Java method. You could play with these examples as much as you want.
  							After that, you should finish 9 questions, alternating between non-reduced code and reduced code by 2 different methods.
  						</li>
  						<li>
  							<b>Write a one-sentence description of a Java method</b>:
  							You are provided 5 Java methods. For each method, non-reduced code and reduced codes are provided. For the reduced code, you could also change the reduction level. 
  							Please check each option carefully. In the end, we will conduct a survey about your preferences for the reduction method and the reduced level.
  						</li>
					</ul>
					<p> You can click the next button at the bottom of the page to go forward. Remember you cannot go back.</p>
					<p style={{color:'red'}}> Please do not refresh your browers during this user study! </p>
					<p> Thanks for your participation! </p>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
				</div>
				<div className="col-md-2"></div>
			</div>
		)
	}
}


export default connect()(Instructions)