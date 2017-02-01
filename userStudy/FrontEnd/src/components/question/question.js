import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import brace from 'brace'
import AceEditor from 'react-ace'
import 'brace/mode/java'
import 'brace/theme/chrome'

import ListItem from './listItem'
import OptionItem from './optionItem'

export class Question extends Component{

	constructor(props){
		super(props);
		this.index = 1;
		this.questionCount = Object.keys(this.props.questions).length;
	}	


	render(){

		return (
			<div>
				<div className="jumbotron text-center">
  					<h3>User Study</h3>
				</div>
				<div className="row">
    				<div className="col-md-2 text-center">
        				<ul className="nav nav-pills nav-stacked">
        				{

        					Object.keys(this.props.questions).map((key)=>{
        						if(parseInt(key)===this.index){
        							return <ListItem key={parseInt(key)} index={parseInt(key)} active={true}/>
        						}
        						else{
        							return <ListItem key={parseInt(key)} index={parseInt(key)} active={false}/>
        						}
        					})
        				}
        				</ul>
   					</div>

   					<div className="col-md-6 text-center">
       					<h5 className="text-center">Please choose the best description for the following Java method:</h5>
        				<div id="editor"><AceEditor mode="java" theme="chrome" name="editor" editorProps={{$blockScrolling:true}} value={this.props.questions[this.index.toString()].code}/></div>
    				</div>

    				<div className="col-md-4">
    					<div className="alert alert-info text-center" id="timer">00:00</div>
        				<br/>
        				<div className="alert alert-success" id="alternatives">
        					{

        						this.props.questions[this.index.toString()].Alternatives.map((option,index)=>{
        							return <OptionItem key={option+index} text={option}/>
        						})
        					}
        				</div>
       					<div id="alertMessage"></div>
    				</div>
    			</div>

    			<div className="row">
    				<ul className="pager">
        				<li className="disabled"><a href="#"><span aria-hidden="true">&larr;</span> Previous</a></li>
       					<li><a href="#" id="nextButton" onClick={()=>{
       						if(this.index < this.questionCount){
       							this.index += 1;
								this.forceUpdate();
							}
       					}}>Next <span aria-hidden="true">&rarr;</span></a></li>
    				</ul>
				</div>
    		</div>
		)
	}
}


export default connect((state) => {
	return {
		questions:state.questions.questions
	}
})(Question)