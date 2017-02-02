import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import brace from 'brace'
import AceEditor from 'react-ace'
import 'brace/mode/java'
import 'brace/theme/chrome'

import {nav2End, displayErrorMsg} from '../../actions'
import Message from '../message'
import ListItem from './listItem'
import Timer from './timer'
import {checkOneQuestion} from './questionActions'

let interval = null;

const computeTime = (secondCount)=>{
    let mins = Math.floor(secondCount/60);
    let seconds = secondCount%60;
    let string = "";
    if(mins>=60){
        throw "Too long for this task! Invalid case!"
    }
    if(mins<10){
        string += '0';
    }    
    string += mins;
    string+=':';
    if(seconds<10){
        string+='0';
    }
    string+=seconds;
    return string;
}

export class Question extends Component{

	constructor(props){
		super(props);
		this.index = 1;
		this.questionCount = Object.keys(this.props.questions).length;
		this.state = { clock: 0, time: "" , answer: "Not Selected!"};
	}	

	componentDidMount() {
    	if (!interval) {
      		interval = setInterval(this.update.bind(this), 1000)
    	}
  	}

  	componentWillUnmount() {
  		clearInterval(interval);
  		interval = null;
  	}

  	update() {
    	let clock = this.state.clock;
    	clock += 1;
    	this.setState({clock: clock });
    	let time = computeTime(clock);
    	this.setState({time: time });
  	}

  	handleOption(e){
  		console.log(e)
  		this.setState({answer:e.target.value})
  		console.log(this.state.answer)
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
    					<div className="alert alert-info text-center" id="timer">{this.state.time}</div>
        				<br/>
        				<div className="alert alert-success" id="alternatives">
        					{

        						this.props.questions[this.index.toString()].Alternatives.map((option,index)=>{
        							return (
        								<div className="radio" key={"option "+index}>
											<label><input type="radio" name="optradio" checked={this.state.answer===("option "+index)} value={"option "+index}
											onChange={(e)=>{this.handleOption(e)}}/>{option}</label>
										</div>
									)
        						})
        					}
        				</div>
       					<div><Message/></div>
    				</div>
    			</div>

    			<div className="row">
    				<ul className="pager">
        				<li className="disabled"><a href="#"><span aria-hidden="true">&larr;</span> Previous</a></li>
       					<li><a href="#" id="nextButton" onClick={()=>{
       						if(this.state.answer==="Not Selected!"){
       							this.props.dispatch(displayErrorMsg("Please finish this question first!"))
       						}
       						else if(this.index < this.questionCount){
								this.props.dispatch(checkOneQuestion(this.index, this.state.answer, this.state.clock));
								this.index += 1;
       							this.state.clock = 0;
       							this.state.answer = "Not Selected!";
								this.forceUpdate();
							}
							else if(this.index === this.questionCount){//Last question!
								this.props.dispatch(checkOneQuestion(this.index, this.state.answer, this.state.clock));
								this.props.dispatch(nav2End());
							}
							else{
								throw "This is an invalid branch!"
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