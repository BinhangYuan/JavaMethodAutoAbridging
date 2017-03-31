//Global variables
var data;
var originEditor;
var a50Editor;
var a20Editor;


function showProgram(){
	if(this.value>=0){
		originEditor.setValue(data[this.value].Origin);
		a50Editor.setValue(data[this.value].a50);
		a20Editor.setValue(data[this.value].a20);
    }
	else{
		originEditor.setValue("");
		a50Editor.setValue("");
		a20Editor.setValue("");
	}
}


window.onload = function(){
	//Set up 3 editors to show source code;
	originEditor = ace.edit("origin");
    originEditor.setTheme("ace/theme/solarized_light");
    originEditor.getSession().setMode("ace/mode/java");
  
    a50Editor = ace.edit("a50");
    a50Editor.setTheme("ace/theme/chrome");
    a50Editor.getSession().setMode("ace/mode/java");

    a20Editor = ace.edit("a20");
    a20Editor.setTheme("ace/theme/chrome");
    a20Editor.getSession().setMode("ace/mode/java");

    //Load my data!
    d3.json("result/result_test.json",function(error,json){
    	if (error) throw error;
    	data = json.result;
    	//console.log(data);
    	let selectButton = document.getElementById("select-example");
    	data.forEach((item,index)=>{
    		let option = document.createElement("option");
    		option.value = index;
    		option.innerHTML = "Example "+ index;
    		selectButton.appendChild(option);
    	})
    	selectButton.addEventListener("change",showProgram);
    })
}