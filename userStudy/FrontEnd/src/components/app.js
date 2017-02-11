import React from 'react'
import { connect } from 'react-redux'

import Landing from './auth/landing'
import QuestionView from './question/questionView'
import End from './end/end'



const App = ({location}) => {
	let view;
	if (location === 'QUESTION_PAGE') {
		view = <QuestionView/>;
	} 
	else if (location === 'END_PAGE') {
		view = <End/>;
	} 
	else{
		view = <Landing/>
	}
	return (
		<div>
			{view}
		</div>
	)
}

export default connect((state) => {
	console.log(state);
	return {location:state.shared.location}
})(App)