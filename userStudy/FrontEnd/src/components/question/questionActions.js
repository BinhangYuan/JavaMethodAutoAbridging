import Action, {resource, displayErrorMsg} from '../../actions'
import {surveyConfig} from './descriptionConst'


//let field = "questionAll";
let field = "questionSample";
//let field = "questionStub";

export function getQuestion(){
	return (dispatch, getState) => {
		let email = getState().visit.email;
		//console.log(email)
		return resource('GET', field, email)
		.then((response)=>{
			const questions = response.questions;
			dispatch({type:Action.UPDATEQUESTION, questions});
		})
	}
}

export function checkOneQuestion(index, answer, time, reduction){
	return (dispatch, getState) => {
		let questions = getState().questions.questions;
		questions[index].answer = answer;
		questions[index].time = time;
		questions[index].reduction = reduction;
		return dispatch({type:Action.ADDANSWER, questions,index,reduction});
	}
}

export function checkSurvey(answers){
	return (dispatch) => {
		if(Object.keys(answers).length === surveyConfig.length){
			return dispatch({type:Action.UPDATESURVEY, answers});
		}
		else{
			return dispatch(displayErrorMsg("Please finish all survey questions!"));
		}
	}
}