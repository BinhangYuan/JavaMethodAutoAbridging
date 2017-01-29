const drawLine = ()=>{
    let svg = d3.select("#lineChart");
    
    let margin = {
        top: 40,
        bottom: 40,
        right: 60,
        left: 60
    };

    let width = svg.attr("width") - margin.left - margin.right;
    let height = svg.attr("height") - margin.top - margin.bottom;

    let g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    d3.json('result/ParaILSTrainingCurve.json',function(error,jsonData){
        if (error){
            throw error;
        }

        let minData = jsonData.min;
        let minlineData = [];

        minData.forEach((cost,index)=>{
            minlineData.push([index,cost]);
        })

        let iterData = jsonData.iterations;
        let iterlineData = [];

        iterData.forEach((cost,index)=>{
            iterlineData.push([index,cost]);
        })

        let xScale = d3.scaleLinear().range([0, width]).domain([0, minData.length]);

        let yScale = d3.scaleLinear().range([height, 0]).domain([0, 1]);

        let xAxis = d3.axisBottom().scale(xScale)

        let yAxis = d3.axisLeft().scale(yScale)

        let line = d3.line()
        .x(function(d) {
            return xScale(d[0]);
        })
        .y(function(d) {
            return yScale(d[1]);
        });

        g.append("g")
        .attr("class", "x axis")
        .call(xAxis)
        .attr("transform", "translate(0," + height + ")");

        g.append("g")
        .attr("class", "y axis")
        .call(yAxis)

        g.append("path")
        .datum(minlineData)
        .attr("class", "minline")
        .attr("d", line);

        g.append("path")
        .datum(iterlineData)
        .attr("class", "iterline")
        .attr("d", line);
    })
}

const drawScatterplot = ()=>{
    let svg = d3.select('#scatterplot');
    let margin = {
        top: 30,
        bottom: 30,
        right: 30,
        left: 30
    };

    let width = svg.attr("width") - margin.left - margin.right;
    let height = svg.attr("height") - margin.top - margin.bottom;

    let g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    d3.json('result/result.json',function(error,jsonData){
        if (error){
            throw error;
        }
        let programs = jsonData.result;
        let maxLines = d3.max(programs,(d)=>{return d.Original_Lines})
        let dataMap = {};

        programs.forEach((data,index)=>{
            let key = data.Original_Lines + "_" + data.Target_Lines;
            if(!dataMap[key]){
                dataMap[key] = {
                    x:data.Original_Lines,
                    y:data.Target_Lines,
                    count:0,
                    programIDs:[]
                }
            }
            dataMap[key].count += 1;
            dataMap[key].programIDs.push(index)
        })

        let dataArray = Object.keys(dataMap).map((key)=>dataMap[key]);

        let xScale = d3.scaleLinear().range([0,width]).domain([0,maxLines]);
        let yScale = d3.scaleLinear().range([height,0]).domain([0,maxLines]);
        let rScale = d3.scaleLinear().range([3,6]).domain([1,d3.max(dataArray,(d)=>{return d.count})])

        let xAxis = d3.axisBottom().scale(xScale)
        let yAxis = d3.axisLeft().scale(yScale)

        const hovered = (flag) =>{
             return function(d) {
                d3.select(this)
                .classed("circle-hover",flag);
             }   
        }


        let node = g.selectAll('circle')
        .data(dataArray)
        .enter()
        .append('circle')
        .attr('cx', (d)=>xScale(d.x))
        .attr('cy', (d)=>yScale(d.y))
        .attr('r',(d)=>rScale(d.count))
        .attr('fill','steelblue')
        .on('mouseover',hovered(true))
        .on('mouseout',hovered(false))

        node.append('title')
        .text((d)=>{
            let string = '';
            d.programIDs.forEach((id,i)=>{
                if(i==0){
                    string = 'Program '+id;
                }
                else{
                    string = string +'\nProgram '+id;
                }
            })
            return string;
        })

        g.append("g")
        .attr("class", "x axis")
        .call(xAxis)
        .attr("transform", "translate(0," + height + ")");

        g.append("g")
        .attr("class", "y axis")
        .call(yAxis)
    })
}



const drawWordClound = (tag,attribute)=>{
    
    let svg = d3.select("#"+tag);
    
    let margin = {
        top: 10,
        bottom: 10,
        right: 10,
        left: 10
    };

    let width = svg.attr("width") - margin.left - margin.right;
    let height = svg.attr("height") - margin.top - margin.bottom;
    let size = Math.max(width, height);
    

    let g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

     d3.json('result/NaiveBayesTextWordDistribution.json',function(error,jsonData){
        if (error){
            throw error;
        }
        
        let wordData = jsonData[attribute];

        let rScale = d3.scaleLinear().range([10, 80]).domain([0, d3.max(wordData,(d)=>{return d.count})]); 

        let circles = d3.packSiblings(wordData.map((tuple)=>{
            return {
                r: rScale(tuple.count),
                word: tuple.word,
                count: tuple.count
            }
        }))


        g.selectAll("circle")
        .data(circles)
        .enter().append("circle")
        .attr("cx", function(d) { return d.x + width/2; })
        .attr("cy", function(d) { return d.y + height/2; })
        .attr("r", function(d) { return d.r - 0.25; });

        g.selectAll('text')
        .data(circles)
        .enter().append('text')
        .text((d)=>d.count>=10?d.word+'('+d.count+')':"")
        .attr("x", function(d) { return d.x + width/2; })
        .attr("y", function(d) { return d.y + height/2; })
        .attr('font-size','12px')
    })
}


window.onload = function(){
    drawLine();
    drawScatterplot();
    drawWordClound("wordChouldPositive","positive");
    drawWordClound("wordChouldNegative","negative");
}

