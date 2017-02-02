let url = "http://127.0.0.1:10006";

var editor;
var questions;
var index = 0;
var secondCount = 0;

const alert=(message)=>{
    let div = document.getElementById("alertMessage");
    div.innerHTML = message;
    div.setAttribute("class","alert alert-danger");
}

const clearAlert=()=>{
    let div = document.getElementById("alertMessage");
    div.innerHTML = "";
    div.setAttribute("class","");
}


const timeFunction = ()=>{
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
    document.getElementById("timer").innerHTML = string;
    secondCount += 1;
}

var timer = setInterval(timeFunction,1000);

const setQuestion = (i)=>{
    console.log(i)
    editor.setValue(questions[i].code);
    let alternatives = document.getElementById("alternatives");
    while (alternatives.firstChild){
        alternatives.removeChild(alternatives.firstChild);
    }
    questions[i].Alternatives.forEach((item,i)=>{
        console.log(item);
        let option = document.createElement("div");
        option.setAttribute("class","radio");
        let label = document.createElement("label");
        let input = document.createElement("input");
        input.setAttribute("type","radio");
        input.setAttribute("name","optradio");
        input.setAttribute("id","opt"+i);
        label.appendChild(input);
        label.appendChild(document.createTextNode(item));
        option.appendChild(label);
        alternatives.appendChild(option);
    })
}

const nextClickHandler = ()=>{
    console.log("Next click Handler")
    if(index<questions.length){
        let current = document.getElementById("nav"+(index));
        current.setAttribute("class","");
        //Stub record the solution!
        let selected = -1;
        questions[index].Alternatives.forEach((item,i)=>{
            let input = document.getElementById("opt"+i);
            if(input.checked){
                if(selected!==-1){
                    throw "Error in radio check!";
                }
                selected = i;
            }
        })
        if(selected === -1){
            alert("Please finish this question first!");
        }
        else{
            questions[index]['userSelected'] = selected;
            questions[index]['timeCount'] = secondCount;
            clearAlert();
            //Update timer:
            document.getElementById("timer").innerHTML = "00:00";
            secondCount = 0;
            index += 1;
            if(index<questions.length){
                setQuestion(index);
                let next = document.getElementById("nav"+(index));
                next.setAttribute("class","active");
            }
            else{
                console.log(questions);
                window.location.href = "thankyou.html"
            }
        }
    }
    else{
        throw "Invalid index in nextClickHandler!";
    }
}


window.onload = function(){
    let formData = location.search.substring(1);
    let formArray = formData.split("&");
    console.log(formArray)


	editor = ace.edit("editor");
    editor.setTheme("ace/theme/chrome");
    editor.getSession().setMode("ace/mode/java");
    console.log(`${url}/questions`)
    d3.json(`${url}/questions`,function(error,json){
        console.log(`${url}/questions`)
        if (error) throw error;
        questions = json.questions;
        console.log(questions);
        let nextButton = document.getElementById("nextButton");
        nextButton.addEventListener("click",nextClickHandler);
        setQuestion(index);
        //index += 1;
        let nav = document.getElementById("questionNavList");
        questions.forEach((item,i)=>{
            let li = document.createElement("li");
            let a = document.createElement("a");
            a.appendChild(document.createTextNode("Question "+(i+1)));
            a.setAttribute("href","#");
            li.appendChild(a);
            if(i==0){
                li.setAttribute("class","active");
            }
            li.setAttribute("id","nav"+i);
            nav.appendChild(li);
        })

    })
}