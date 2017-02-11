import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import brace from 'brace'
import AceEditor from 'react-ace'
import 'brace/mode/java'
import 'brace/theme/chrome'

import {T2description} from './descriptionConst'

class QuestionT2 extends Component{
	constructor(props){
		super(props);
	}

	handleOption(e){
  		console.log(e)
  		this.props.questions.answer=e.target.value;
  		console.log(this.props.questions.answer)
  		this.forceUpdate();
  	}

 	render(){
 		return(
 			<div>
   				<div className="col-md-7 text-center">
       				<h5 className="text-center">Please choose the best description for the following Java method:</h5>
        			<div id="editor"><AceEditor mode="java" theme="chrome" name="editor" editorProps={{$blockScrolling:true}} value={this.props.questions.questions[this.props.questions.index.toString()].code}/></div>
    			</div>

    			<div className="col-md-5">
        			<div className="alert alert-success" id="alternatives">
              <p className="text-justify">{T2description}</p>
              <br/>
        			{
        				this.props.questions.questions[this.props.questions.index.toString()].Alternatives.map((option,index)=>{
        					return (
        						<div className="radio" key={"option "+index}>
									   <label><input type="radio" name="optradio" checked={this.props.questions.answer===("option "+index)} value={"option "+index} onChange={(e)=>{this.handleOption(e)}}/>{option}</label>
								    </div>
							    )
        				})
        			}
        			</div>
    			</div>
      </div>
    )
 	}
}

export default connect((state) => {
	return {
		questions:state.questions
	}
})(QuestionT2)