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
    this.methodName = {
      0:"Non-reduced code",
      1:"Reduced code by Method A",
      2:"Reduced code by Method B"
    }
	}

	handleOptionAnswer(e){
  	//console.log(e)
  	this.props.questions.answer=e.target.value;
  	//console.log(this.props.questions.answer)
  	this.forceUpdate();
  }

 	render(){
 		return(
 			<div>
        <div className="row">
          <div className="col-md-1"></div>
          <div className="col-md-10">
            <h4 className="text-center">Please match the best description for the following Java methods:</h4>
          </div>
          <div className="col-md-1"></div>
        </div>

        <div className="row">
          <br/>
          <br/>
          <div className="col-md-2"></div>
          <div className="col-md-8">
            <div className="alert alert-success" id="alternatives">
            <h5 style={this.props.questions.questions[this.props.questions.index.toString()].practice?{color:'blue'}:{color:'red'}}>
              {this.props.questions.questions[this.props.questions.index.toString()].practice?"This is a practice question. Take your time!"
              :"This is an actual test for our survey. Please stay stay focused and try your best!"}</h5>
            <p className="text-justify">{T2description}</p>
            <br/>
            <p className="text-justify"><b>Description A: </b>{this.props.questions.questions[this.props.questions.index.toString()].docA}</p>
            <p className="text-justify"><b>Description B: </b>{this.props.questions.questions[this.props.questions.index.toString()].docB}</p>
            <br/>
            {
              this.props.questions.questions[this.props.questions.index.toString()].Alternatives.map((option,index)=>{
                return (
                  <div className="radio" key={index}>
                   <label><input type="radio" name="optradio" checked={this.props.questions.answer===index.toString()} value={index.toString()} onChange={(e)=>{this.handleOptionAnswer(e)}}/>{option}</label>
                  </div>
                )
              })
            }
            <br/>
            <br/>
            <p>All Java methods in this qeustion are <b> {this.methodName[this.props.questions.questions[this.props.questions.index.toString()].method]}. </b></p>
            </div>
            </div>
          <div className="col-md-2"></div>
        </div>

        <div className="row">
   				<div className="col-md-6 text-center">
            <h5>Method A:</h5>
        		<div><AceEditor mode="java" theme="chrome" name="editor" width="100%" height="600px" editorProps={{$blockScrolling:true}} value={this.props.questions.questions[this.props.questions.index.toString()].codeA}/></div>
    			</div>
          <div className="col-md-6 text-center">  
            <h5>Method B:</h5>  
            <div><AceEditor mode="java" theme="chrome" name="editor" width="100%" height="600px" editorProps={{$blockScrolling:true}} value={this.props.questions.questions[this.props.questions.index.toString()].codeB}/></div>
          </div>
          <br/>
          <br/>
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