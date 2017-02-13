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

export function questions(state = {questions:{},index:1,answer: "Not done!",reduction:"r10"},action){
	switch(action.type){
		case Action.UPDATEQUESTION:
			return { ...state, questions:action.questions,index:1,answer: "Not done!",reduction:"r10"};
		case Action.ADDANSWER:
			return { ...state, questions:action.questions,index:action.index+1,reduction:action.reduction,answer: "Not done!"};
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

export function survey(state = {answers:{}},action){
	switch(action.type){
		case Action.UPDATESURVEY:
			return { ...state, answers:action.answers};
		default:
			return state;
	}
}

const Reducer = combineReducers({
	questions, visit, shared, survey
})

export default Reducer