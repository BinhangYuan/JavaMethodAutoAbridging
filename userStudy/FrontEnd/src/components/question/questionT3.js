import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import brace from 'brace'
import AceEditor from 'react-ace'
import 'brace/mode/java'
import 'brace/theme/chrome'

import {T3description} from './descriptionConst'

class QuestionT3 extends Component{
	constructor(props){
		super(props);
    this.textInput='';
    this.handleText = this.handleText.bind(this);
	}

	handleText(e){
    e.preventDefault();
  	console.log(e)
    this.textInput = e.target.value;
  	this.props.questions.answer=this.textInput;
  	console.log(this.props.questions.answer);
  	this.forceUpdate();
  }

 	render(){
 		return(
 			<div>
   			<div className="col-md-7 text-center">
       		<h4 className="text-center">{this.props.questions.questions[this.props.questions.index.toString()].method}</h4>
        	<div id="editor"><AceEditor mode="java" theme="chrome" name="editor" editorProps={{$blockScrolling:true}} value={this.props.questions.questions[this.props.questions.index.toString()].code}/></div>
    		</div>

    		<div className="col-md-5">
        	<div className="alert alert-success" id="alternatives">
            <p className="text-justify">{T3description}</p>
            <br/>
        		<textarea id={"description-textarea"+this.props.questions.index} rows ="4" style={{width:'100%'}} value={this.props.questions.answer==="Not done!"?"":this.textInput} onChange={this.handleText}/>
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
})(QuestionT3)