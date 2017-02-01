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