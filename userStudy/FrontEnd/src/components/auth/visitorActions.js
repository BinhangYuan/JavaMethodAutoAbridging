import Promise from 'bluebird'

import Action, {nav2Question, displayErrorMsg, resource} from '../../actions'
import { getQuestion } from '../question/questionActions'



export function visitorAction(email, level) {
    return (dispatch) => {
        return resource('POST', 'visitor', {email, level})
        .then((response) => {
            dispatch({type: Action.VISIT, level: response.level, email:response.email})
            dispatch(nav2Question())
        }).catch((err) => {
            dispatch(displayErrorMsg(`Invalid input: ${email}, ${level}`))
        })
    }
}