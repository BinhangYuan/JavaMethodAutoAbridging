import Action, {resource} from '../../actions'


export function getQuestion(){
	return (dispatch) => {
		return resource('GET', 'questions')
		.then((response)=>{
			const questions = response.questions;
			dispatch({type:Action.UPDATEQUESTION, questions});
		})
	}
}

export function checkOneQuestion(index, answer, time){
	return (dispatch, getState) => {
		let questions = getState().questions.questions;
		questions[index].answer = answer;
		questions[index].time = time;
		return dispatch({type:Action.ADDANSWER, questions});
	}
}