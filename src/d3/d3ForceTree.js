function spawnNewWindow()
{
	newWindow = window.open("","Graph","width=550,height=170,0,status=0,titlebar=no,menubar=no,location=no,toolbar=no,status=no");
	newWindow.document.write("Hello world");
}

function resort()
{
  	var compareChoice =  document.getElementById("sortByWhat").value;
  
	nodes.sort( function(a,b) {
 					 if (a[compareChoice]< b[compareChoice])
     						return -1;
  					if (a[compareChoice]> b[compareChoice])
    					return 1;
  					return 0; } );
	
	setInitialPositions();
}

// modded from http://mbostock.github.com/d3/talk/20111116/force-collapsible.html
var w,h,nodes, 
    links,
    link,
    root;
                
    
  var firstUpdate = true;
  var reverse =false;
  var initHasRun = false;
  var firstFlatten = true;
  var topNodes = [];
  var ranges={};
  var ordinalScales={};
  var colorScales = {};
  var labelCheckBoxes=[];  
 var dirty = true;
    
var force, drag, vis;


function reforce()
{
	w =  window.innerWidth-300,
    h = window.innerHeight-100,
    
	force = d3.layout.force()
    .charge(function(d) { return d._children ? -d.numSeqs / 100 : -30; })
    .linkDistance(function(d) { return d.target._children ? 80 * (d.level-16)/16 : 30; })
    .size([w, h - 60]).gravity(document.getElementById("gravitySlider").value/100)
    
    drag = force.drag().on("dragstart", function(d) { d.fixed=true; update();});

	vis = d3.select("body").append("svg:svg")
    .attr("width", w)
    .attr("height", h)
	 
}

function reVis()
{

	checkForStop()
	vis.remove();
	reforce();
	dirty=true;
    update();
	setInitialPositions();
	update();
	
}
  
  
  function setQuantitativeDynamicRanges()
  {
  		var chosen = document.getElementById("colorByWhat");	
  		
  		var aRange = ranges[chosen.value];
  		
  		if( aRange == null)
  		{
  			document.getElementById("lowQuantRange").value = "categorical";
  			document.getElementById("highQuantRange").value = "categorical";
  			document.getElementById("lowQuantRange").enabled = false;
  			document.getElementById("highQuantRange").enabled = false;
  		}
  		else
  		{
  			document.getElementById("lowQuantRange").value = aRange[0];
  			document.getElementById("highQuantRange").value = aRange[1];
  			
  		}
  
  		if( ! firstUpdate ) 
			redrawScreen();  	
  }

  function addDynamicMenuContent()
  {
  	if( ! firstFlatten) 
  		return;
  	
  	var mySidebar = document.getElementById("sidebar");
  	
   	mySidebar.innerHTML +=  "<select id=\"sortByWhat\" onchange=sort())></select>"
	
  	mySidebar.innerHTML += "<h3> Size: <h3>"
  	var selectHTML =  "<select id=\"sizeByWhat\" onchange=redrawScreen()>"
	selectHTML +=  "</select>"	
	mySidebar.innerHTML += selectHTML
	mySidebar.innerHTML += "<br>Max size: <input type=\"number\"" + 
			 " id=\"maxSize\" min=\"0\" max=\"100\" value=\"30\" onchange=redrawScreen()></input>" +
			 "<br>Min size: <input type=\"number\"" + 
			 " id=\"minSize\" min=\"0\" max=\"100\"  value=\"1\" onchange=redrawScreen()></input>"   + 
	"<br><input type=\"checkbox\"" + 
			"id=\"logSize\" onchange=redrawScreen()>log</input>"
			+"<input type=\"checkbox\"" + 
			"id=\"invertSize\" onchange=redrawScreen()>invert</input><br>"
	
	for( var propertyName in nodes[0])
  		if( propertyName != "forceTreeNodeID" && propertyName != "x" && propertyName != "y"
  				&& propertyName != "children" && propertyName != "fixed" )
  		{
  			var isNumeric = true;
  			var selectHTML = "<option value=\"" + propertyName
  				+ "\">" + propertyName   +"</option>"
  				
  				var range=[]
  				range[0] = nodes[0][propertyName];
  				range[1] = nodes[0][propertyName];
  							
  				for( var x=0;  isNumeric && x < nodes.length; x++)
  				{
  					var aVal =nodes[x][propertyName]; 
  					
  					if( ! isNumber(aVal))
  						isNumeric = false;
  					
  					if( aVal < range[0]) 
  						range[0] = aVal;
  						
  					if( aVal > range[1]) 
  						range[1] = aVal;
  				}
  				
  				if( isNumeric) 
  				{
  					ranges[propertyName] = range; 
  				}
  				else
  				{
  					ordinalScales[propertyName] = 
  					d3.scale.ordinal();
  					
  					//todo: the range needs to be updated when maxSize changes
  					ordinalScales[propertyName].range([0,document.getElementById("maxSize").value]);
  					
  					colorScales[propertyName] = 
  						d3.scale.category20b();
  				}
  				
  				document.getElementById("sizeByWhat").innerHTML += selectHTML
  				document.getElementById("sortByWhat").innerHTML += selectHTML
  				document.getElementById("scatterX").innerHTML += selectHTML
  				document.getElementById("scatterY").innerHTML += selectHTML	
  		}
	
	mySidebar.innerHTML += "<h3> Color: <h3>";
  	selectHTML =  "<select id=\"colorByWhat\" onchange=setQuantitativeDynamicRanges()>"
  	
  	selectHTML += "<option value=\"nodeDepth" + "\">" + "node depth"+"</option>"
			
	for( var propertyName in nodes[0])
  		if( propertyName != "forceTreeNodeID" && propertyName != "x" && propertyName != "y"
  				&& propertyName != "children" && propertyName != "fixed" && propertyName != "nodeDepth" )
  		{
  			selectHTML += "<option value=\"" + propertyName
  				+ "\">" + propertyName   +"</option>"
  						
  		}
  	
  	selectHTML += "<option value=\"colorByMarked" 
  				+ "\">" + "marked"+"</option>"
  	
  	selectHTML +=  "</select>"
  	mySidebar.innerHTML += selectHTML
  	mySidebar.innerHTML += "<br><input type=\"checkbox\"" + 
			"id=\"logColor\" onchange=redrawScreen()>log</input>"
	
  	mySidebar.innerHTML += "<input type=\"checkbox\" id=\"textIsBlack\"" + 
				"onchange=redrawScreen()>text always black</input>";
  	    
  	var labelHTML = "<li><a>Labels</a><ul>";
  	labelHTML += "<li><input type=\"checkbox\" id=\"labelOnlyTNodes\"" + 
			"onchange=redrawScreen() checked=true> Label only T-Nodes</input></li>"	
				
	for( var propertyName in nodes[0])
  		if( propertyName != "forceTreeNodeID" 
  			&& propertyName != "x" && propertyName != "y")
	  	{
	  		var newHTML = "<li><input type=\"checkbox\" id=\"label" + propertyName + "\"" + 
				"onchange=redrawScreen()>" + propertyName + "</input></li>";
				
			 labelHTML += newHTML;
			 labelCheckBoxes.push("label" + propertyName );
	  	}
	  	
	  	
	labelHTML +="<li>Font Adjust <input type=\"range\" id=\"fontAdjust\""
		 labelHTML += "min=\"5\" max=\"25\" value=\"15\" onchange=redrawScreen()></input></li>"
			 labelHTML += "</ul></li>"	  	
	  	document.getElementById("nav").innerHTML+= labelHTML;
  	mySidebar.innerHTML += "<h3> Filter: <h3>"
  	
  	mySidebar.innerHTML += "level: <input type=\"number\" id=\"depthFilter\" min=\"2\" " + 
  		"max=\" ranges[\"nodeDepth\"] value=2 onchange=setTopNodes()></input><br>"; 
  		
  	
  	var rangeHTML = "Depth Filter:<input type=\"range\" id=\"depthFilterRange\" min=\"0\" " + 
  	"max=\"" + topNodes.length + "\" value=\"0\" onchange=showOnlyMarked()><br></input>";
  	
    mySidebar.innerHTML+= rangeHTML;
  	setTopNodes();
  	
  	var aTable =""
  	
  	aTable += "<table border=1 id=\"tNodeTable\">"
	aTable +=		"<tr>"
	aTable +=		"<td>Number of Visible Nodes</td>"
	aTable +=			"<td></td>"
	aTable +=		"</tr>"
	aTable +=		"<tr>"
	aTable +=		"<td>Number of TNodes</td>"
	aTable +=		"<td></td>"
	aTable +=		"</tr>"
	aTable +=	"</table>"
  	
  	mySidebar.innerHTML+= aTable;
  
  }
  
  function setTopNodes()
  {
  	topNodes = [];
  
  	for( var x =0; x < nodes.length; x++)
  	{
  		if( nodes[x].nodeDepth == document.getElementById("depthFilter").value) 
  		{	
  			topNodes.push(nodes[x]);
  		}
  	}
  	
  	
  	document.getElementById("depthFilterRange").max = topNodes.length;
  	
  	showOnlyMarked();
  }
  
  function showOnlyMarked()
  {
  	var aVal = document.getElementById("depthFilterRange").value;
  	
  	if( aVal==0)
  	{	
  		for( var x=0; x < nodes.length; x++)
  			nodes[x].doNotShow=false;
  	}
  	else
  	{
  		for( var x=0; x < nodes.length; x++)
  			nodes[x].doNotShow=true;
  			
  		aVal = aVal -1;
  		var myNode = topNodes[aVal];
  		
  		function markSelfAndDaughters(aNode)
  		{
  			aNode.doNotShow=false;
  			
  			if( aNode.children != null)
  			{
  				for( var y=0; y < aNode.children.length;y++)
  				{
  					markSelfAndDaughters(aNode.children[y]);
  				}
  			}
  		}
  		
  		markSelfAndDaughters(myNode);
  	}
  	
  	dirty=true;
  	update();
  }
  
   
  function redrawScreen()
  {
  	// can't log an ordinal color scale...
  	if(  ordinalScales[ document.getElementById("sizeByWhat").value] != null )  
  	{
  		aBox = document.getElementById("logSize");
  		aBox.checked=false;
  		aBox.enabled=false;
  	}
  	else
  	{
  		document.getElementById("logSize").enabled=true;
  	}
  	
  	// can't log an ordinal color scale...
  	if(  ordinalScales[ document.getElementById("colorByWhat").value] != null )  
  	{
  		aBox = document.getElementById("logColor");
  		aBox.checked=false;
  		aBox.enabled=false;
  	}
  	else
  	{
  		aBox = document.getElementById("logColor").enabled=true;
  	}
  	
  	dirty = true;
  	update()
  }


function getLabelText(d)
{	
	if( d.marked == false && document.getElementById("labelOnlyTNodes").checked  )
		return "";
	
	var returnString ="";
	
	for( var propertyName in nodes[0])
	{
		var aCheckBox = document.getElementById("label" + propertyName);
		if( aCheckBox != null &&  aCheckBox.checked)
		{
			returnString += d[propertyName] + " ";
		}
	}
		
	return returnString;	
}

function myFilterNodes(d)
{
	 if( ! d.doNotShow )
	 	return true;
	 	
	 return false;
}

function myFilterLinks(d)
{
     if( d.source.setVisible  && d.target.setVisible)
      		return true;
      	
      return false;
      		
}

// from http://stackoverflow.com/questions/18082/validate-numbers-in-javascript-isnumeric
function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}



function getRadiusVal(d)
{
	var propToSize = document.getElementById("sizeByWhat").value
	var returnVal = document.getElementById("maxSize").value;
	
	// quantitative values
	if( ranges[propToSize] != null)
	{
		if( document.getElementById("logSize").checked) 
		{
			// d3's log scale yields problems with p=0 in range so we 
			// covert everything to log and feed it to the linear scale..
			// as a nice side effect, you don't have to multiply p-values by negative 1
			if( d[propToSize] >0) // a p-value of zero yields a maximum sized radius
			{
				maxScale = Math.log(ranges[propToSize][1]) / Math.LN10; 
			
				var aScale= d3.scale.linear().domain([0,maxScale]).range([document.getElementById("minSize").value,
	  					document.getElementById("maxSize").value]).clamp(true);
	  			returnVal = aScale(Math.log(d[propToSize]) / Math.LN10 );
			}
		}
		else
		{
			var aScale= d3.scale.linear().domain(ranges[propToSize]).range([document.getElementById("minSize").value,
	  					document.getElementById("maxSize").value]).clamp(true);
	  		returnVal = aScale(d[propToSize]);
		}
		
		
	}
	else //ordinal values - much easier
	{
		aScale = ordinalScales[propToSize].range([document.getElementById("minSize").value,
	  					document.getElementById("maxSize").value])
		returnVal =aScale(d[propToSize]);
	}
	
	if( document.getElementById("invertSize").checked ) 
	{
		returnVal = document.getElementById("maxSize").value - returnVal;
	}
	
	return returnVal;
	
}

function getToolTipText(d)
{
	var aVal = "";

	for( var propertyName in d)
	{
			if( ranges[propertyName] != null	)
  				aVal += (propertyName + ":" +  d[propertyName] + " ");
	}
  				
  	return aVal
}

var updateNum=0;
function update() 
{
	if( ! initHasRun )
		return;
 
	if( dirty ) 
	{
		console.log("Full update " + ++updateNum);
		dirty = false;
		var anyLabels = false;
		
		for( var x=0; ! anyLabels && x < labelCheckBoxes.length; x++)
		{
			var aCheckBox = document.getElementById(labelCheckBoxes[x]);
			
			if( aCheckBox != null) 
				anyLabels = aCheckBox.checked
		}
		
		
	   	var numMarked =0;
  		var numVisible=0;
	 	for (var i = 0; i < nodes.length; i++)
	 	{
	 		nodes[i].marked= false;
	 		if( ! nodes[i].doNotShow &&  nodes[i].setVisible== true) 
	 		{
	 			nodes[i].marked = true;
		 		numVisible++;
		 		
		 		if( nodes[i].children != null) 
		 		{
		 			for( var j=0; nodes[i].marked && j < nodes[i].children.length; j++ ) 
		 			{
		 				if( ! nodes[i].children[j].doNotShow )
		 				{
		 					nodes[i].marked=false;
		 				}
		 			}
		 		}
		 		
		 		if( nodes[i].marked == true) 
		 			numMarked = numMarked + 1
	 		}
	 	}
	 	
	 	for (var i = 0; i < nodes.length; i++)
	 	{
	 		nodes[i].thisNodeColor = color(nodes[i]);
	 		nodes[i].thisNodeRadius = getRadiusVal(nodes[i]);
	 	}	
	
		vis.selectAll("text").remove()
		vis.selectAll("circle.node").remove();
		vis.selectAll("line.link").remove();
		for( var z=0; z < nodes.length; z++)
			nodes[z].setVisible=false;
		
		var filteredNodes = nodes.filter(myFilterNodes);	
		
		for( z=0; z < filteredNodes .length; z++)
			filteredNodes[z].setVisible=true;
		
		if( document.getElementById("graphType").value == "ForceTree") 
		{
				links = d3.layout.tree().links(nodes);
		}
		
  	// Restart the force layout.
 	 
 	 if( document.getElementById("graphType").value == "ForceTree"  ) 
 	 force
      .nodes(nodes)
      
      if( document.getElementById("graphType").value == "ForceTree" && ! document.getElementById("hideLinks").checked )
      force.links(links)
      
      if( document.getElementById("graphType").value == "ForceTree" )
      	force.start().gravity(document.getElementById("gravitySlider").value/100);
  
		
	  var node = vis.selectAll("circle.node")
	      .data(filteredNodes, function(d) {return d.forceTreeNodeID; } )
	      .style("fill", function(d) { return d.thisNodeColor} )
	      .style("opacity",document.getElementById("opacitySlider").value/100 );
	
	  // Enter any new nodes.
	 node.enter().append("svg:circle").on("click", myClick)
	      .attr("class", "node")
	      .attr("cx", function(d) { return d.x; })
	      .attr("cy", function(d) { return d.y; })
	      .attr("r", function(d) {  return d.thisNodeRadius})
	      .style("fill", function(d) { return d.thisNodeColor}).
	      style("opacity",document.getElementById("opacitySlider").value/100 ) 
	     .on("mouseenter", myMouseEnter)
	      .on("mouseleave", myMouseLeave)
	      
	      if( document.getElementById("graphType").value == "ForceTree"  )
	      	node.call(force.drag);
	      
	      function updateNodesLinksText()
	      {
	      
	      if( document.getElementById("graphType").value == "ForceTree"  )
	      {
	      	node.attr("cx", function(d) { return d.x; })
	      .attr("cy", function(d) { return d.y; });
	      }
	      else if( document.getElementById("graphType").value == "scatter"  )
	      {
	      	node.attr("cx", function(d) {   return 400 * Math.random(); })
	      .attr("cy", function(d) {   return 400 * Math.random(); });
	      }
	      	
	      if ( anyLabels )
			text.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
			
		if( document.getElementById("graphType").value == "ForceTree"  && ! document.getElementById("hideLinks").checked )
		{
				link.attr("x1", function(d) { return d.source.x; })
	      .attr("x1", function(d) { return d.source.x; })
	      .attr("y1", function(d) { return d.source.y; })
	      .attr("x2", function(d) { return d.target.x; })
	      .attr("y2", function(d) { return d.target.y; });
		}
		
		  	checkForStop();
	      }
	    
		force.on("tick", updateNodesLinksText);
		
		force.on("end", updateNodesLinksText);
	    
	      
	      	// Update the links
	      	if( document.getElementById("graphType").value == "ForceTree" && ! document.getElementById("hideLinks").checked )
  		link = vis.selectAll("line.link")
      .data(links.filter(myFilterLinks), function(d) {  return d.target.forceTreeNodeID; }
      		);
	   
	  // Enter any new links.
	  if( document.getElementById("graphType").value == "ForceTree" && ! document.getElementById("hideLinks").checked )
	  link.enter().insert("svg:line", ".node")
	      .attr("class", "link")
	       
	 	var table = document.getElementById("tNodeTable"); //.rows[0].cells[1].item[0] = "" + numMarked ;
	 	
	 	table.rows[0].cells[1].innerHTML = "" + numVisible;
	 	
	 	var row = table.rows[1];
	 	var cell =row.cells[1];
	 	cell.innerHTML = "" + numMarked;
	 	
	 	if( anyLabels  )
  var text=vis.selectAll("text").data(filteredNodes).enter().append("svg:text").
 	attr("dx", function(d) { return 15; })
                 .attr("dy", function(d) { return ".35em"; })
		 .text( function (d) {  return getLabelText(d); })
                 .attr("font-family", "sans-serif")
                 .attr("font-size", document.getElementById("fontAdjust").value + "px")
                 .attr("fill", function(d) {return  getTextColor(d) } )	    

 // cleanup
  if( document.getElementById("graphType").value == "ForceTree" && ! document.getElementById("hideLinks").checked )
  link.exit().remove();
  
  node.exit().remove();
	}
  	
  	checkForStop();
  	// the color choosers don't work unless they are initialized first
  	// hence they are initialized in the "section" and then moved to the appropriate menu
  	// once everything else has settled in...
	if( firstUpdate ) 
	{
		setQuantitativeDynamicRanges();
  		document.getElementById("ColorSubMenu").appendChild(document.getElementById("color1"));
		document.getElementById("color1").style.visibility="visible";
		
		document.getElementById("ColorSubMenu").appendChild(document.getElementById("color2"));
		document.getElementById("color2").style.visibility="visible";
		
		firstUpdate = false;
	}	
  	
  
}

function checkForStop()
{
	
	if ( document.getElementById("graphType").value != "ForceTree" || ! document.getElementById("animate").checked)
  		force.stop();
	
}

function getTextColor(d)
{
	if(  document.getElementById("textIsBlack").checked ) 
		return "#000000";
		
	var chosen = document.getElementById("colorByWhat").value;
	
	if( colorScales[chosen] != null || ranges[chosen] != null)
		return color(d);
		
}

function myMouseEnter(d)
{
	if (! document.getElementById("mouseOverHighlights").checked)
		return;
	
	function highlightNodeAndChildren(d2)
	{
		d2.highlight=true;
	
		if( d2.children != null ) 
		{
			for(var x=0; x < d2.children.length; x++) 
			{
				highlightNodeAndChildren(d2.children[x]);
			}		
		}
	}
	
	highlightNodeAndChildren(d);
	
	dirty = true;
	update();
}

function myMouseLeave()
{
	
	if (! document.getElementById("mouseOverHighlights").checked)
		return;

	for(var x=0; x < nodes.length; x++) 
	{
		nodes[x].highlight = false;
	}

	dirty = true;
	update();
}

function setInitialPositions()
{
	root.x = w / 2;
  	root.y = h / 2 - 80;
	
	var radius = Math.min(w,h)/2 - 300;
	
	for( var x=0; x < nodes.length; x++) 
	{
		if( nodes[x] != root ) 
		{
			nodes[x].x = radius * Math.cos( (nodes.length-x) / 360.0) + root.x;
			nodes[x].y = radius * Math.sin( (nodes.length-x) / 360.0) + root.y
			nodes[x].fixed=false;
		}
	}
}


function initialize() {
  flatten(root),
      
  initHasRun = true;
 
   update();
}

function getQuantiativeColor(d)
{
	var chosen = document.getElementById("colorByWhat").value;
	
	var lowColor = "#" + document.getElementById("quantColorLow").value;
	var highColor ="#" + document.getElementById("quantColorHigh").value; 
		
	var aRange = []
	aRange.push(document.getElementById("lowQuantRange").value);
	aRange.push(document.getElementById("highQuantRange").value);
		
	if( lowColor > highColor) 
	{
		var temp = lowColor
		lowColor=  highColor;
		highColor = temp;
	}
		
	if( document.getElementById("logColor").checked) 
	{
		aVal =d[chosen]; 
		maxScale = Math.log(aRange[1]) / Math.LN10; 
		
		if( aVal ==0)
				aVal = maxScale;
		else
			aVal = Math.log(aVal) / Math.LN10; 
			
		var aScale= d3.scale.linear().domain([0,maxScale]).range([lowColor,highColor]).clamp(true);
	  	return aScale(aVal);
	}
	else
	{
		var aScale= d3.scale.linear().domain(aRange).range([lowColor,highColor]).clamp(true);
		return aScale(d[chosen]);	
	}
		
}

function color(d) 
{
	
	var chosen = document.getElementById("colorByWhat").value;
	
	if( ranges[chosen] != null)
		return getQuantiativeColor(d);
	
	if( colorScales[chosen] != null) 
		return colorScales[chosen]( d[chosen] );
		
	if( d._children != null)
		return  "#3182bd";  // bright blue
	
	if ( d.highlight == true) 
		return "#fd8d3c"; // orange

	if(  d.marked )
		return "#000000";  // black
		
		
	return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
		
}

// Toggle children on click.
var myClick= function (d) {

	var aValue =document.getElementById("clickDoesWhat").value;
	
	if ( aValue == "deletes")
	{
		initHasRun = false;
		d.children=null;
		d._children=null;
		initialize();
	}
	if (aValue=="collapses")
	{
		initHasRun = false;
		
		if( d._children == null)
		{
			d._children = d.children;
			d.children =null;
		}
		else
		{
			d.children = d._children 
			d._children = null;
		}
		
		initialize();
	}
	else if ( aValue == "hides")
	{
		reverse = ! reverse;
		
		if( reverse == false)
		{
			for( var x =0; x < nodes.length; x++)
				nodes[x].doNotShow=false;
		}
		else
		{
			for( var x =0; x < nodes.length; x++)
				nodes[x].doNotShow=true;
		
			highlightAllChildren(d);
			highlightAllParents(d);
		}
		
	}
	
	dirty=true;
	update();	
}



function highlightAllChildren(d)
{
	if( d== null)
		return;

	if( ! d.children || d.children == null)
		return;	

	d.doNotShow = false;
	for( var x=0; x < d.children.length; x++) 
	{
		highlightAllChildren(d.children[x]);
	}
}

function highlightAllParents(d)
{
	if ( d== null)
		return;

	d.doNotShow = false;
	if( ! d.aParentNode ||  d.aParentNode != null)
	{
		highlightAllParents(d.aParentNode);
	}
}


// Returns a list of all nodes under the root.
function flatten() 
{
  var myNodes = [];
  var level =0;
  
  function addNodeAndChildren( aNode) 
	{
		level++;
		if( aNode != null) 
		{
			aNode.nodeDepth = level;
			myNodes.push(aNode);
	
			if( aNode.children != null)
				for( var x=0; x < aNode.children.length; x++)
				{
					addNodeAndChildren(aNode.children[x])
					aNode.children[x].aParentNode = aNode;
				}
					
		}
		level--;
			
	}
  
  addNodeAndChildren(root);
  
  for( var i=0; i < myNodes.length; i++)
  {
  	if (!myNodes[i].forceTreeNodeID) myNodes[i].forceTreeNodeID = i+1;
  }
  
  nodes = myNodes;  
  
  if( firstFlatten) 
  {
  		setInitialPositions();
  		addDynamicMenuContent();
  		firstFlatten = false;
  		
  }
}

