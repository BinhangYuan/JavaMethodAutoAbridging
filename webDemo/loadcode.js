//Global variables
var data;
var originEditor;
var manualEditor;
var autoEditor;


function showProgram(){
	if(this.value>=0){
		originEditor.setValue(data[this.value].Origin);
		manualEditor.setValue(data[this.value].manual);
		autoEditor.setValue(data[this.value].Automatic);
	}
	else{
		originEditor.setValue("");
		manualEditor.setValue("");
		autoEditor.setValue("");
	}
}


window.onload = function(){
	//Set up 3 editors to show source code;
	originEditor = ace.edit("origin");
    originEditor.setTheme("ace/theme/solarized_light");
    originEditor.getSession().setMode("ace/mode/java");
  
    manualEditor = ace.edit("manual");
    manualEditor.setTheme("ace/theme/chrome");
    manualEditor.getSession().setMode("ace/mode/java");

    autoEditor = ace.edit("auto");
    autoEditor.setTheme("ace/theme/chrome");
    autoEditor.getSession().setMode("ace/mode/java");

    //Load my data!
    d3.json("result/result1485027566964.json",function(error,json){
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