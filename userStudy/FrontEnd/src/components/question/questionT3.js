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
    this.methodName = {
      0:"Non-reduced code",
      1:"Reduced code by Method A",
      2:"Reduced code by Method B"
    }
	}

	handleText(e){
    e.preventDefault();
  	//console.log(e)
    this.textInput = e.target.value;
  	this.props.questions.answer=this.textInput;
  	//console.log(this.props.questions.answer);
  	this.forceUpdate();
  }

  handleOptionMethod(e){
    //console.log(e)
    this.props.questions.questions[this.props.questions.index.toString()].method=parseInt(e.target.value);
    this.props.questions.reduction='r50%';
    this.forceUpdate();
  }

  handleOptionReduction(e){
    //console.log(e)
    this.props.questions.reduction=e.target.value;
    this.forceUpdate();
  }

 	render(){
 		return(
 			<div>
        <div className="row">
          <div className="col-md-2"></div>
          <div className="col-md-8">
            <h4 className="text-center">Please write the description for the following method:</h4>
          </div>
          <div className="col-md-2"></div>
        </div>
        <div className="row">
          <div className="col-md-3 text-center">
            <div className="form-group">
              <label className="control-label">Choose Reduction Option:</label>
              <div className="selectContainer">
                <select name="language" className="form-control"
                  value={this.props.questions.questions[this.props.questions.index.toString()].method}
                  onChange={(e)=>{this.handleOptionMethod(e)}}>
                  <option value="0" key="0">Original Code</option>
                  <option value="1" key="1">Reduction Method A</option>
                  <option value="2" key="2">Reduction Method B</option>
                </select>
              </div>
            </div>
            <br/>
            <div className="form-group">
              <label className="control-label">Choose Reduction Level:</label>
              <div className="selectContainer">
                <select name="language" className="form-control" 
                  value={this.props.questions.reduction}
                  disabled={this.props.questions.questions[this.props.questions.index.toString()].method===0} 
                  onChange={(e)=>{this.handleOptionReduction(e)}}>
                  <option value="r15" key="r15">Reduce to around 15 lines</option>
                  <option value="r30" key="r30">Reduce to around 30 lines</option>
                  <option value="r50%" key="r50%">Reduce by 50%</option>
                </select>
              </div>
            </div>
          </div>
          <div className="col-md-8">
            <div className="alert alert-success" id="alternatives">
              <p className="text-justify">{T3description}</p>
              <br/>
              <textarea id={"description-textarea"+this.props.questions.index} rows ="4" style={{width:'100%'}} value={this.props.questions.answer==="Not done!"?"":this.textInput} onChange={this.handleText}/>
            </div>
          </div>
          <div className="col-md-1"></div>
        </div>
        <div className="row">
          <div className="col-md-1"></div>
   			  <div className="col-md-10 text-center">
       		  <h4 className="text-center">{this.methodName[this.props.questions.questions[this.props.questions.index.toString()].method]}</h4>
        	  <div id="editor"><AceEditor mode="java" theme="chrome" name="editor" width="100%" height="600px" editorProps={{$blockScrolling:true}} 
              value={
                this.props.questions.questions[this.props.questions.index.toString()].method===0?this.props.questions.questions[this.props.questions.index.toString()].code_original:
                (this.props.questions.questions[this.props.questions.index.toString()].method===1?this.props.questions.questions[this.props.questions.index.toString()].code_native[this.props.questions.reduction]:
                (this.props.questions.questions[this.props.questions.index.toString()].method===2?this.props.questions.questions[this.props.questions.index.toString()].code_mymethod[this.props.questions.reduction]:
                "Internal error of this web App! Contact by8@rice.edu!"))
              }/>
            </div>
    		  </div>
          <div className="col-md-1"></div>
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