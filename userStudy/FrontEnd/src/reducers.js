import { combineReducers } from 'redux'
import Action from './actions'

export function shared(state = {location:'', errorMsg:'', successMsg:''}, action){
	let cleanMsg = {errorMsg:'', successMsg:''}
	switch(action.type){
		case Action.ERRORMSG:
			return { ...state, ...cleanMsg, errorMsg: action.errorMsg};
		case Action.SUCCESSMSG:
			return { ...state, ...cleanMsg, successMsg: action.successMsg};
		case Action.NAV2QUESTION:
			return { ...state, ...cleanMsg, location: 'QUESTION_PAGE'};
		case Action.NAV2END:
			return { ...state, ...cleanMsg, location: 'END_PAGE'};
		case Action.NAV2INDEX:
			return { ...state, ...cleanMsg, location: ''};
		default:
			return {...state,...cleanMsg};
	}
}	

export function questions(state = {questions:{}},action){
	switch(action.type){
		case Action.UPDATEQUESTION:
			return { ...state, questions:action.questions};
		default:
			return state;
	}
}

export function visit(state = {email:'',level:''},action){
	switch(action.type){
		case Action.VISIT:
			return { ...state, email:action.email, level: action.level};
		default:
			return state;
	}
}

const Reducer = combineReducers({
	questions, visit, shared
})

export default Reducer