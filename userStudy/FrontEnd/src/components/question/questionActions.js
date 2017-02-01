import Action, {resource} from '../../actions'


export function getQuestion(){
	return (dispatch) => {
		return resource('GET', 'questions')
		.then((response)=>{
			const questions = response.questions.reduce((object,item) => {
				object[item._id] = item;
				return object;
			},{})
			dispatch({type:Action.UPDATEQUESTION, questions});
		})
	}
}