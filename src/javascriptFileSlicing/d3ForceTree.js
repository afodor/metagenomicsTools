 
function StaticHolder()
{
	if( !StaticHolder.ranges)
	{
		StaticHolder.ranges ={};
		StaticHolder.ordinalScales={};
		StaticHolder.colorScales = {};
		StaticHolder.labelCheckBoxes=[]; 
		StaticHolder.counter =0;
		StaticHolder.goObjects = {};
		StaticHolder.nodes=null;
		StaticHolder.root=null;
		StaticHolder.highlightedNode=null;
		StaticHolder.maxLevel = -1;
		StaticHolder.highlightReverse=false;
	}
	
	this.setMaxLevel = function(aLevel)
	{
		StaticHolder.maxLevel = aLevel;
	}
	
	this.getHighlightReverse = function()
	{
		return StaticHolder.highlightReverse;
	}
	
	this.setHighlightReverse =function(b)
	{
		StaticHolder.highlightReverse=b;
	}
	
	this.getMaxLevel = function()
	{
		return StaticHolder.maxLevel;
	}
	
	
	this.getNodes = function()
	{
		return StaticHolder.nodes;
	}
	
	this.getHighlightedNode = function()
	{
		return StaticHolder.highlightedNode;
	}
	
	this.setHighlightedNode = function(aNode)
	{
		StaticHolder.highlightedNode = aNode;
	}
	
	this.getRoot = function()
	{
		return StaticHolder.root;
	}
	
	this.setRoot = function(aRoot)
	{
		StaticHolder.root = aRoot;
	}
	
	
	this.setNodes = function(someNodes)
	{
		StaticHolder.nodes = someNodes;
	}
	
	this.getRanges = function()
	{
		return StaticHolder.ranges;
	}
	
	this.getOrdinalScales = function()
	{
		return StaticHolder.ordinalScales;
	}
	
	this.getColorScales = function()
	{
		return StaticHolder.colorScales;
	}
	
	this.getLabelCheckBoxes= function()
	{
		return StaticHolder.labelCheckBoxes;
	}
	
	this.addGoObject = function(goObject)
	{
		StaticHolder.counter++;
		
		StaticHolder.goObjects[StaticHolder.counter] = goObject;
		
		return StaticHolder.counter;
	}
	
	this.getGoObjects = function()
	{
		return StaticHolder.goObjects;
	}
} 


// modded from http://dotnetprof.blogspot.com/2012/11/get-querystring-values-using-javascript.html
function getQueryStrings(aWindow) 
{
    //Holds key:value pairs
    var queryStringColl = null;
            
    //Get querystring from url
    var requestUrl = aWindow.location.search.toString();

    if (requestUrl != '') 
    {
    	requestUrl = requestUrl.substring(1);

        queryStringColl = {};

        //Get key:value pairs from querystring
        var kvPairs = requestUrl.split('&');

        for (var i = 0; i < kvPairs.length; i++) 
        {
            var kvPair = kvPairs[i].split('=');
            queryStringColl[kvPair[0]] = kvPair[1];
        }
    }
   
    return queryStringColl;
}

function GO(parentWindow,thisWindow,isRunFromTopWindow)
{

var aDocument = parentWindow.document;
var thisDocument = thisWindow.document;
var statics = parentWindow.statics;
var thisID = statics.addGoObject(this);
var graphType = "ForceTree"
var queryStrings = getQueryStrings(thisWindow)
var addNoise= false;
var firstNoise = true;
var dataNames = [];
var lastSelected = null;
var animationOn=false;

var stopOnChild = false;
var displayDataset= null; 
var dragging =false;


this.addNoise = function()
{
	addNoise= true;
	this.redrawScreen();
}

this.getThisDocument = function()
{
	return thisDocument;
}

this.getParentDocument = function()
{
	return aDocument;
}


if( queryStrings ) 
{
	var aGraphType = queryStrings["GraphType"];
	if( aGraphType != null) 
		graphType = aGraphType;
}


this.resort = function()
{
	var compareChoice =  aDocument.getElementById("sortByWhat").value;
	
	quantitativeSort = function(a,b) 
		{
		 if (1.0 * a[compareChoice]< 1.0 * b[compareChoice])
				return -1;
		if (1.0 * a[compareChoice]> 1.0 * b[compareChoice])
			return 1;
		return 0; 
		} 

	nonQuantitativeSort = function(a,b) 
	{
		if (a[compareChoice]< b[compareChoice])
				return -1;
		if (a[compareChoice]> b[compareChoice])
		return 1;
		return 0;
	}
  
	if( ! aDocument.getElementById("treeAwareSort").checked)
	{
		// quantiative
	  	if( statics.getRanges()[compareChoice] != null ) 
	  	{
			nodes.sort( quantitativeSort );
	  	}
	  	else
	  	{
	  			nodes.sort( nonQuantitativeSort );
	  	}
	}
	else
	{
		var newNodes = [];
		
		addNodeAndSortDaughters = function(aNode)
		{
			newNodes.push(aNode);
			
			if( ! aNode.children || aNode.children.length ==0 )
				return;
			
			var childrenNodes = [];
			for( var x=0; x < aNode.children.length; x++)
				childrenNodes.push(aNode.children[x]);
			
			// quantiative
		  	if( statics.getRanges()[compareChoice] != null ) 
		  	{
				childrenNodes.sort( quantitativeSort );
		  	}
		  	else
		  	{
		  		childrenNodes.sort( nonQuantitativeSort );
		  	}
			
		  	for( var x=0; x < childrenNodes.length; x++)
		  		addNodeAndSortDaughters( childrenNodes[x]);
		}
		
		addNodeAndSortDaughters(statics.getRoot());
		nodes = newNodes;
		statics.setNodes(newNodes);
	}
	
	for( var x=0; x < nodes.length; x++) 
		nodes[x].listPosition =x;  		
  	
	this.setInitialPositions();
	this.redrawScreen();
}

// modded from http://mbostock.github.com/d3/talk/20111116/force-collapsible.html
var w,h, 
    links,
    link,
    thisContext = this;
    
  	var firstUpdate = true;
  	var initHasRun = false;
    
  	topNodes= [];
  
  	var dirty = true;
  	
  	var circleDraws = {};
  
    
var force, drag, vis;

this.makeDirty = function()
{
	this.dirty=true;
}

this.reforce = function()
{

	this.setWidthAndHeight();
	
	if( graphType == "ForceTree")
	{

	    force = d3.layout.force()
	    .charge(function(d) { return d._children ? -d.numSeqs / 100 : -30; })
	    .linkDistance(function(d) { return d.target._children ? 80 * (d.nodeDepth-16)/16 : 30; })
	    .size([w, h - 60]).gravity(aDocument.getElementById("gravitySlider").value/100)
	    
	    drag = force.drag().on("dragstart", function(d) 
	    { 
	    	d.fixed =true;    	
	    	d.userMoved = true;
	    	dragging =true;
	    	force.start();
	    	
		});
	    
	    drag = force.drag().on("drag", function(d) 
	    { 
	    	dragging =true;
	    	force.start();	
	    });
	    
	    drag = force.drag().on("dragend", function(d) 
	    { 
	    	d.fixed=true;
	    	d.userMoved = true;    	
	    	d.parentDataNode.xMap[thisID] = d.x;
	    	d.parentDataNode.yMap[thisID] = d.y;
	    });
	    
	    force.start();
	    
	    stopOnChild = true;

	}
	

    if( graphType != "ForceTree")
    {
    	vis = d3.select("body").append("svg:svg")
        .attr("width", w)
        .attr("height", h);
    	
    	//.append("g").call(d3.behavior.zoom().scaleExtent([0.01, 100]).on("zoom", thisContext.zoom))
      //.append("g");
    }
    else
    {
    	vis = d3.select("body").append("svg:svg")
        .attr("width", w)
        .attr("height", h)
    }
}

// from http://blog.luzid.com/2013/extending-the-d3-zoomable-sunburst-with-labels/
this.computeTextRotation = function(d) {
	  var angle = x(d.x + d.dx / 2) - Math.PI / 2;
	  return angle / Math.PI * 180;
	}


this.zoom = function() {
  vis.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
  thisContext.redrawScreen();
}

this.getDisplayDataset = function()
{
	if(  displayDataset)
		return displayDataset;
	
	displayDataset= { nodes : [] }; 
	
	var index =0;
	
	function addAndReturnChild(aNode, childIndex)
	{
		var newChildNode = aNode.children[childIndex];
		var newDisplayNode= {};
		newDisplayNode.name = "NAME_" + index;
		index++;
		newDisplayNode.fixed = true;
		displayDataset.nodes.push(newDisplayNode);
		newDisplayNode.parentDataNode = newChildNode;
		
		if( newChildNode.children)
		{
			newDisplayNode.children=[];
			for( var x=0; x < newChildNode.children.length; x++)
			{
				newDisplayNode.children.push( addAndReturnChild(newChildNode, x) );
			}
		}
		
		return newDisplayNode;
	}
	
	var rootDisplayNode = {};
	rootDisplayNode.name = "NAME_" +  index;
	index++;
	rootDisplayNode.fixed= false;
	displayDataset.nodes.push(rootDisplayNode);
	rootDisplayNode.parentDataNode = statics.getRoot();
	rootDisplayNode.children = [];
	
	for( var x=0; x < statics.getRoot().children.length; x++)
		rootDisplayNode.children.push( addAndReturnChild(statics.getRoot(), x) );
	
	return displayDataset;
	
}

this.setWidthAndHeight = function()
{
	//if( isRunFromTopWindow ) 
	{
		w =  thisWindow.innerWidth-25,
    	h = thisWindow.innerHeight-25;
	}
	//else
	{	
		//w =  thisWindow.innerWidth-25;
    	//h = thisWindow.innerHeight;
	}
	
}

this.getThisId = function()
{
	return thisID;
}

// to be called on window call
this.unregister = function()
{
	console.log("Got unregister " + thisID );
	statics.getGoObjects()[thisID] = null;
	
	if( force ) 
	{
		force.stop();
	}
	
	if( vis ) 
	{
		vis.selectAll("text").remove()
		vis.selectAll("circle.node").remove();
		vis.selectAll("line.link").remove();
		vis.selectAll("line").remove();
	}
	
	
}

this.reVis = function(revisAll)
{
	if( revisAll )
	{
		registered = statics.getGoObjects();
  		for (id in registered) if( registered[id] )
		{	
			registered[id].reVisOne();
		}			
	}
	else
	{	
		this.reVisOne();
	}

}

this.reVisOne = function(resetPositions) 
{
	
	this.setWidthAndHeight();
	
	if( graphType != "ForceTree"  )
		this.setInitialPositions();
	

	if( graphType != "ForceTree"  )
		vis.selectAll("g").remove();
	
	
	vis.remove();
	
	
	this.reforce();
	
	dirty=true;
    this.update();
}
  
  
  this.setQuantitativeDynamicRanges = function()
  {
  		var chosen = aDocument.getElementById("colorByWhat");	
  		
  		var aRange = statics.getRanges()[chosen.value];
  		
  		if( isRunFromTopWindow ) 
  		{
	  		if( aRange == null)
	  		{
	  			aDocument.getElementById("lowQuantRange").value = "categorical";
	  			aDocument.getElementById("highQuantRange").value = "categorical";
	  			aDocument.getElementById("lowQuantRange").enabled = false;
	  			aDocument.getElementById("highQuantRange").enabled = false;
	  		}
	  		else
	  		{
	  			aDocument.getElementById("lowQuantRange").value = aRange[0];
	  			aDocument.getElementById("highQuantRange").value = aRange[1];
	  			
	  		}
  		}
  		if( ! firstUpdate ) 
			this.redrawScreen();  	
  }
  
  this.addIndividualMenuDynamicMenuContent = function()
  {
  		var allNames = [];
  		
  		var scatterX = thisDocument.getElementById("scatterX")
  		var scatterY = thisDocument.getElementById("scatterY")
  		
  		var xString = "<option value=\"circleX\">circleX</option>";
  		var yString = "<option value=\"circleY\">circleY</option>";
  		
  		scatterX.innerHTML += xString;
  		scatterY.innerHTML += yString;
  		scatterX.innerHTML += yString;
  		scatterY.innerHTML += xString;
  		
  		//todo: these will be in a different order than other menus
  		for (prop1 in statics.getRanges())
  			allNames.push(prop1);
  		
  		for (prop2 in statics.getOrdinalScales())	
  			allNames.push(prop2);
  			
  		for( var x=0; x< allNames.length; x++)
  		{
  			var propertyName = allNames[x];
  			
  			var selectHTML = "<option value=\"" + propertyName
  				+ "\">" + propertyName   +"</option>"
  			
  			scatterX.innerHTML += selectHTML;
  			scatterY.innerHTML += selectHTML;
  		}
  
  }

  this.addDynamicMenuContent =function()
  {
  	if( ! isRunFromTopWindow) 
  		return;
  	
  	var mySidebar = aDocument.getElementById("sidebar");
  	
   	mySidebar.innerHTML +=  "<select id=\"sortByWhat\" onChange=myGo.resort()></select>"

   	mySidebar.innerHTML += "<h3> Size: <h3>"
  	var selectHTML =  "<select id=\"sizeByWhat\" onchange=myGo.reVis(false)>"
	selectHTML +=  "</select>"	
	mySidebar.innerHTML += selectHTML
	mySidebar.innerHTML += "<br>Max size: <input type=\"number\"" + 
			 " id=\"maxSize\" min=\"0\" max=\"100\" value=\"30\" onkeypress=\"return myGo.isNumber(event)\" onchange=myGo.reVis(false)></input>" +
			 "<br>Min size: <input type=\"number\"" + 
			 " id=\"minSize\" min=\"0\" max=\"100\"  value=\"2\" onkeypress=\"return myGo.isNumber(event)\" onchange=myGo.reVis(false)></input>"   + 
	"<br><input type=\"checkbox\"" + 
			"id=\"logSize\" onchange=myGo.reVis(false)>log</input>"
			+"<input type=\"checkbox\"" + 
			"id=\"invertSize\" onchange=myGo.reVis(false)>invert</input><br>"
	
	var dataMenuHTML =   "<li id=\"dataMenu\"><a>Data</a><ul>";
	for( var propertyName in nodes[0])
  		if( 	propertyName != "forceTreeNodeID" 
  				&& propertyName != "x" 
  				&& propertyName != "y"
  				&& propertyName != "children" 
  				&& propertyName != "fixed" 
  				)
  		{
  			var isNumeric = true;
  			var selectHTML = "<option value=\"" + propertyName
  				+ "\">" + propertyName   +"</option>"
  				
  				var range=[]
  				range[0] = nodes[0][propertyName];
  				range[1] = nodes[0][propertyName];
  				
  				if( this.isNumber(range[0]) && this.isNumber(range[1]) ) 
  				{
  					range[0] = 1.0 * range[0];
  					range[1] = 1.0 * range[1];
  				}
  				
  				
  				for( var x=0;  isNumeric && x < nodes.length; x++)
  				{
  					var aVal =nodes[x][propertyName]; 
  					
  					if( ! this.isNumber(aVal))
  					{
  						isNumeric = false;
  					}
  					else
  					{	
  						aVal = 1.0 * aVal;
  						if( aVal < range[0]) 
  							range[0] = aVal;
  						
  						if( aVal > range[1]) 
  							range[1] = aVal;
  					}
  				}
  				
  				if( isNumeric) 
  				{
  					statics.getRanges()[propertyName] = range; 
  				}
  				else
  				{
  					statics.getOrdinalScales()[propertyName] = d3.scale.ordinal();
  					statics.getColorScales()[propertyName] = d3.scale.category20b();
  				}
  				
  				aDocument.getElementById("sizeByWhat").innerHTML += selectHTML
  				aDocument.getElementById("sortByWhat").innerHTML += selectHTML
  				
  				if(propertyName != "xMap" 
						&& propertyName != "yMap" 
						&& propertyName != "xMapNoise"
						&& propertyName != "yMapNoise")
					dataMenuHTML+=
						"<li id=\"dataRange" + propertyName + "\"><a>" + propertyName   +" </a></li>"  
						
					dataNames.push( "dataRange" + propertyName );
  		}
	
	dataMenuHTML+= "</ul></li>";
	
	for( var x=0; x < dataNames.push; x++)
	{
		var innerString = "";
		
		for( var y=0; y < 5; y++)
			innerString += "<li>Number " + x + "</li>";
		
		innerString += "";
		aDocument.getElementById(dataNames).innerHTML += innerString;
		
		
	}
	
	aDocument.getElementById("nav").innerHTML+= dataMenuHTML;
		
	mySidebar.innerHTML += "<h3> Color: <h3>";
  	selectHTML =  "<select id=\"colorByWhat\" onchange=myGo.setQuantitativeDynamicRanges()>"
  	
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
			"id=\"logColor\" onchange=myGo.redrawScreen()>log</input>"
	
  	mySidebar.innerHTML += "<input type=\"checkbox\" id=\"textIsBlack\"" + 
				"onchange=myGo.redrawScreen()>text always black</input>";
  	    
  	var labelHTML = "<li><a>Labels</a><ul>";
  	labelHTML += "<li><input type=\"checkbox\" id=\"cicleLabelScheme\"" + 
			"onchange=myGo.redrawScreen() checked=true>" +
			"Smart circular labels</input><br><input type=\"checkbox\" id=\"labelOnlyTNodes\"" + 
			"onchange=myGo.redrawScreen()> Label only T-Nodes</input></li>"	
				
	for( var propertyName in nodes[0])
  		if( propertyName != "forceTreeNodeID" 
  			&& propertyName != "x" && propertyName != "y")
	  	{
	  		var newHTML = "<li><input type=\"checkbox\" id=\"label" + propertyName + "\"" + 
				"onchange=myGo.redrawScreen()>" + propertyName + "</input></li>";
				
			 labelHTML += newHTML;
			 statics.getLabelCheckBoxes().push("label" + propertyName );
	  	}
	  	
	  	
	labelHTML +="<li>Font Adjust <input type=\"range\" id=\"fontAdjust\""
		 labelHTML += "min=\"5\" max=\"25\" value=\"15\" onchange=myGo.redrawScreen()></input></li>"
			 labelHTML += "</ul></li>"	  	
	  	aDocument.getElementById("nav").innerHTML+= labelHTML;
  	mySidebar.innerHTML += "<h3> Filter: <h3>"
  	
  	mySidebar.innerHTML += "node depth: <input type=\"number\" id=\"depthFilter\" onkeypress=\"return myGo.isNumber(event)\" " +
  	" min=\"2\" " + 
  		"max=\" ranges[\"nodeDepth\"] value=2 onchange=myGo.setTopNodes()></input><br>"; 
  		
  	
  	var rangeHTML = "Depth Filter:<input type=\"range\" id=\"depthFilterRange\" min=\"0\" " + 
  	"max=\"" + topNodes.length + "\" value=\"0\" onchange=myGo.setTopNodes()><br></input>";
  	
    mySidebar.innerHTML+= rangeHTML;
  	this.setTopNodes();
  	
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
  
  this.setTopNodes = function()
  {
  	topNodes= [];
  
  	for( var x =0; x < nodes.length; x++)
  	{
  		if( nodes[x].nodeDepth == aDocument.getElementById("depthFilter").value) 
  		{	
  			topNodes.push(nodes[x]);
  		}
  	}
  	
  	if( isRunFromTopWindow ) 
  		aDocument.getElementById("depthFilterRange").max = topNodes.length;
  	
  	this.showOnlyMarked(true);
  }
  
  this.showOnlyMarked = function(withRedraw)
  {
  	var aVal = aDocument.getElementById("depthFilterRange").value;
  	
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
  	
  	statics.getRoot().doNotShow=false;
  	
  	
  	if( withRedraw)
  	{
  		dirty=true;
  	  	this.redrawScreen();
  	}
  	
  	
  }
  
  // calls redrawAScreen on all registered listeners
  this.redrawScreen= function()
  {
  	registered = statics.getGoObjects();
  	for (id in registered) if (registered[id])
	{	
		registered[id].redrawAScreen();
	}
  }
   
  this.redrawAScreen = function()
  {
  	aDocument.getElementById("logSize").enabled=true;
  	aBox = aDocument.getElementById("logColor").enabled=true;
  
  	/* right now these are getting stuck in the off position
  	// can't log an ordinal color scale...
  	if(  statics.getOrdinalScales()[ aDocument.getElementById("sizeByWhat").value] != null )  
  	{
  		aBox = aDocument.getElementById("logSize");
  		aBox.checked=false;
  		aBox.enabled=false;
  	}
  	else
  	{
  		aDocument.getElementById("logSize").enabled=true;
  	}
  	
  	// can't log an ordinal color scale...
  	if(  statics.getOrdinalScales()[ aDocument.getElementById("colorByWhat").value] != null )  
  	{
  		aBox = aDocument.getElementById("logColor");
  		aBox.checked=false;
  		aBox.enabled=false;
  	}
  	else
  	{
  		aBox = aDocument.getElementById("logColor").enabled=true;
  	}
  	*/
  	
  	dirty = true;
  	this.update()
  }

  

this.getLabelText = function(d)
{	
	if( d.marked == false && aDocument.getElementById("labelOnlyTNodes").checked  )
		return "";
	
	var returnString ="";
	
	for( var propertyName in nodes[0])
	{
		var aCheckBox = aDocument.getElementById("label" + propertyName);
		if( aCheckBox != null &&  aCheckBox.checked)
		{
			returnString += d[propertyName] + " ";
		}
	}
	
	if(  aDocument.getElementById("cicleLabelScheme").checked  && graphType=="ForceTree" )
	{

			if( circleDraws[d.nodeDepth] ==  returnString)	
			{
				return "";	
			}
			
	}
	
	circleDraws[d.nodeDepth] =  "" +  returnString;
	
	
	return returnString;	
}

this.myFilterNodes = function(d)
{
	 if( d.parentDataNode.doNotShow == false)
	 	return true;
	 	
	 return false;
}

this.myFilterLinks= function(d)
{
	 if( d.source.parentDataNode.doNotShow == true|| d.target.parentDataNode.doNotShow == true)
      		return false;
      	
      return true;
      		
}

this.gravityAdjust = function()
{
		if  (graphType !=  "ForceTree")		
		{
			myGo.setInitialPositions();
		}	
		
		myGo.redrawScreen();
}

// from http://stackoverflow.com/questions/18082/validate-numbers-in-javascript-isnumeric
this.isNumber = function (n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

this.getAVal = function (d, xAxis)
{
	if( graphType == "ForceTree" )
	{
		return xAxis? d.x: d.y;	
	}
	
	d = d.parentDataNode;
	
	chosen = null;
	
	if( xAxis)
	{
		chosen = thisDocument.getElementById("scatterX").value;
	}
	else
	{
		chosen = thisDocument.getElementById("scatterY").value;
	}
	
	if( chosen == "circleX" )
		return d.xMap[thisID];
		
	if( chosen == "circleY" ) 
		return d.yMap[thisID];
		
	// quantitative scale 
	if( statics.getRanges()[chosen] != null)
	{	
		var aRange = statics.getRanges()[chosen];
		var aScale = d3.scale.linear().domain(aRange).
			range([0, xAxis ? w : h]);
		return aScale(d[chosen]);
	}
	else
	{
		statics.getOrdinalScales()[chosen].
			range([0, xAxis ? w : h]); 
			
		return statics.getOrdinalScales()[chosen](d[chosen]);
	}
	
}
		
this.addAxis = function(chosen, isXAxis)
{
	if( chosen == "circleX" || chosen == "circleY")
		return;
		
	if( statics.getRanges()[chosen] != null)
	{	
		if( isXAxis)
		{
				var aRange = statics.getRanges()[chosen];
		var aScale = d3.scale.linear().domain(aRange).
					range([0, w]);
		var xAxis = d3.svg.axis()
                  .scale(aScale)
                  .orient( "bottom");
        vis.append("svg:svg").call(xAxis); 
		}
		else
		{
		var aRange = statics.getRanges()[chosen];
		var aScale = d3.scale.linear().domain(aRange).
					range([0, h]);
		var yAxis = d3.svg.axis()
                  .scale(aScale)
                  .orient( "right");
        vis.append("svg:svg").call(yAxis); 
		}
	  }
}

this.getRadiusVal= function(d)
{
	var propToSize = aDocument.getElementById("sizeByWhat").value
	var returnVal = aDocument.getElementById("maxSize").value;
	var minValue = aDocument.getElementById("minSize").value * 1.0 ;
	var maxValue= aDocument.getElementById("maxSize").value * 1.0 ;
	var aRange = maxValue- minValue;
	
	
	// quantitative values
	if( statics.getRanges()[propToSize] != null)
	{
		if( aDocument.getElementById("logSize").checked) 
		{
			if( d[propToSize] >0) // a p-value of zero yields a maximum sized radius
			{
				maxScale = Math.log(statics.getRanges()[propToSize][1]) / Math.LN10; 
				var aValue = Math.log( d[propToSize]  ) / Math.LN10;
				
				var partial = aValue / maxScale;
				partial = partial * aRange;
				returnVal = minValue + partial;
			}
		}
		else
		{
			var aValue = 1.0 * d[propToSize] ;
			
			var partial = ( aValue- statics.getRanges()[propToSize][0] )
							/ (statics.getRanges()[propToSize][1] - statics.getRanges()[propToSize][0]);
			
			partial = partial *  aRange;
			returnVal = minValue +  partial;	
		}
			
	}
	else //ordinal values 
	{
		statics.getOrdinalScales()[propToSize].range(minValue,maxValue); 
  					
		returnVal = statics.getOrdinalScales()[propToSize](d[propToSize]);
		
	}
	
	if( aDocument.getElementById("invertSize").checked ) 
	{
		returnVal = maxValue- returnVal;
	}
	
	if( returnVal < minValue)
		returnVal = minValue;
		
	if( returnVal > maxValue)
		returnVal = maxValue;
	
	return returnVal;
	
}
var updateNum=0;

this.toggleVisibilityOfSidebars =function()
{
	var registered = statics.getGoObjects();
  	for (id in registered) if (registered[id])
	{
		registered[id].getThisDocument().getElementById("sidebar").style.backgroundColor="#ffffff";
		
		var aDoc =registered[id].getThisDocument(); 
		
		if( aDoc ) 
		{
			if( aDocument.getElementById("showLeftControl").checked )
			{ 
				aDoc.getElementById("sidebar").style.visibility="visible";
			}
			else
			{
				aDoc.getElementById("sidebar").style.visibility="hidden";
			}
		}
		else
		{
			console.log("Could not get doc for " + id);
		}
	}
	
	
	if( aDocument.getElementById("showRightDataPanel").checked ) 
	{
		aDocument.getElementById("rightInfoArea").style.visibility="visible";
		
	}
	else
	{
		aDocument.getElementById("rightInfoArea").style.visibility="hidden";
	}
		
	aDocument.getElementById("rightInfoArea").style.backgroundColor="#ffffff";
		
}

this.handleKeyboardEvent = function(e)
{
	// modded from http://stackoverflow.com/questions/4368036/how-to-listener-the-keyboard-type-text-in-javascript
	e = e || thisWindow.event;
    var charCode = (typeof e.which == "number") ? e.which : e.keyCode;
    if (charCode) 
    {
        if( String.fromCharCode(charCode) == "A" || String.fromCharCode(charCode) == 'a')
        {
        	thisContext.arrangeForcePlot(true);
        }
        else if( String.fromCharCode(charCode) == "T" || String.fromCharCode(charCode) == 't')
        {
        	thisContext.arrangeForcePlot(false);
        }
        else if( String.fromCharCode(charCode) == "L" || String.fromCharCode(charCode) == 'l')
        {
        	if( thisContext.getParentDocument().getElementById("showLeftControl").checked )
        	{
        		thisContext.getParentDocument().getElementById("showLeftControl").checked =false;
        	}
        	else
        	{
        		thisContext.getParentDocument().getElementById("showLeftControl").checked =true;
        	}
        	
        	
        	thisContext.toggleVisibilityOfSidebars();
        }
        else if( String.fromCharCode(charCode) == "R" || String.fromCharCode(charCode) == 'r')
        {
        	if( thisContext.getParentDocument().getElementById("showRightDataPanel").checked )
        	{
        		thisContext.getParentDocument().getElementById("showRightDataPanel").checked =false;
        	}
        	else
        	{
        		thisContext.getParentDocument().getElementById("showRightDataPanel").checked =true;
        	}
        	
        	thisContext.toggleVisibilityOfSidebars();
        }
        else if ( String.fromCharCode(charCode) == "V" || String.fromCharCode(charCode) == 'v')
        {
        	thisContext.hideAndShow();
        	thisContext.redrawScreen();
        }
        else if ( String.fromCharCode(charCode) == "Q" || String.fromCharCode(charCode) == 'q')
        {
        	var aVal = parseFloat( aDocument.getElementById("localGravity").value);
        	aVal = aVal + 0.5;
        	aDocument.getElementById("localGravity").value = aVal
        }
        else if ( String.fromCharCode(charCode) == "W" || String.fromCharCode(charCode) == 'w')
        {
        	var aVal = parseFloat( aDocument.getElementById("localGravity").value);
        	aVal = aVal - 0.5;
        	aDocument.getElementById("localGravity").value = aVal
        }
    }
}



	// from http://stackoverflow.com/questions/7295843/allow-only-numbers-to-be-typed-in-a-textbox
	this.isNumber = function(evt) {
evt = (evt) ? evt : window.event;
var charCode = (evt.which) ? evt.which : evt.keyCode;

if( charCode == 46)  //decimal places are allowed
	return true;

if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    return false;
}
return true;
}

this.update = function() 
{
	if( ! initHasRun )
		return;
 	
	if( dirty ) 
	{
		if( animationOn == false && stopOnChild == false)
		{
			dataset = thisContext.getDisplayDataset();
			for( var x=0; x < dataset.nodes.length; x++)
			{
				if( ! dataset.nodes[x].userMoved )
				{
					dataset.nodes[x].x = dataset.nodes[x].parentDataNode.xMap[thisID]
					dataset.nodes[x].y = dataset.nodes[x].parentDataNode.yMap[thisID]			  				
				}
			}
		}
		
			
		
		dirty = false;
		var anyLabels = false;
		
		for( var x=0; x<= statics.getMaxLevel(); x++ )
		{
			circleDraws[x] = "";
		}
		
		
		for( var x=0; ! anyLabels && x < statics.getLabelCheckBoxes().length; x++)
		{
			var aCheckBox = aDocument.getElementById(statics.getLabelCheckBoxes()[x]);
			
			if( aCheckBox != null) 
				anyLabels = aCheckBox.checked
		}
		
		var noiseValue = aDocument.getElementById("noiseSlider").value;
		
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
	 		
	 		if( addNoise )
	 		{
	 			if( firstNoise)
	 			{
	 				nodes[i].xMapNoise  = nodes[i].xMap[thisID];
	 				nodes[i].yMapNoise  = nodes[i].yMap[thisID];
	 			}
	 			else
	 			{
	 				nodes[i].xMap[thisID]=nodes[i].xMapNoise ;
	 				nodes[i].yMap[thisID]= nodes[i].yMapNoise;
	 				
	 			}
	 		
	 			var noiseX = 0.1 * nodes[i].xMap[thisID]* Math.random() * (noiseValue/100);
	 			var noiseY = 0.1 * nodes[i].yMap[thisID]* Math.random() * (noiseValue/100);
	 			
	 			if( Math.random() < 0.5) 
	 				noiseX = -noiseX;
	 				
	 			if( Math.random() < 0.5) 
	 				noiseY = -noiseY;
	 				
	 			nodes[i].xMap[thisID] += noiseX;
	 			nodes[i].yMap[thisID] += noiseY;
	 			
	 		}
	 	}
	 	
	 	if( addNoise) 
	 		firstNoise = false;
	 	
	 	for (var i = 0; i < nodes.length; i++)
	 	{
	 		nodes[i].thisNodeColor = this.color(nodes[i]);
	 		nodes[i].thisNodeRadius = this.getRadiusVal(nodes[i]);
	 	}	
		
		var filteredNodes = thisContext.getDisplayDataset().nodes.filter(thisContext.myFilterNodes)
		vis.selectAll("text").remove();
		
	
  	// Restart the force layout.
 	 
 	 if( graphType == "ForceTree"  ) 
 	 {
 		force.nodes(filteredNodes);
 	      
 		if(! aDocument.getElementById("hideLinks").checked )
 		{
 			links = d3.layout.tree().links(filteredNodes);
 			force.links(links)  
 		}
 		else
 		{
 			links = d3.layout.tree().links([]);
 			force.links(links);
 		}
 		
 		
 		if(stopOnChild == true || animationOn == true)
 			force.start().gravity(aDocument.getElementById("gravitySlider").value/100);
 	 }
 	 
	  var node = vis.selectAll("circle.node")
	      .data(filteredNodes, function(d) {return d.name; } )
	      .style("fill", function(d) { return d.parentDataNode.thisNodeColor} )
	      .style("opacity",aDocument.getElementById("opacitySlider").value/100 );
	
	
	  // Enter any new nodes.
	 node.enter().append("svg:circle").on("click", this.myClick)
	      .attr("class", "node")
	      .attr("r", function(d) {  return d.parentDataNode.thisNodeRadius})
	      .style("fill", function(d) { return d.parentDataNode.thisNodeColor}).
	      style("opacity",aDocument.getElementById("opacitySlider").value/100 ) 
	     .on("mouseenter", this.myMouseEnter)
	      .on("mouseleave", this.myMouseLeave)
	      
	       node.attr("cx", 
					function (d){ 
	    		 				
	    		 				return thisContext.getAVal( d,true)
	    		 			}
	    		 		)
	      	.attr("cy", 
	      			
					function (d){
	      						
	      					return thisContext.getAVal( d,false)}
				)
	    
	      
	      if( graphType == "ForceTree"  )
	      {
	      		node.call(force.drag);
	      }
	      	
	      function updateNodesLinksText()
	      {
	    	  //console.log("tick " + dragging);
	    	  
	    	  if( ! animationOn && ( stopOnChild == true || dragging == true))
		  		{
		  			var dataset = thisContext.getDisplayDataset();
		  			
		  			for( var x=0; x < dataset.nodes.length; x++)
		  			{
		  				if( ! dataset.nodes[x].userMoved )
		  				{
			  				dataset.nodes[x].x = dataset.nodes[x].parentDataNode.xMap[thisID]
			  				dataset.nodes[x].y = dataset.nodes[x].parentDataNode.yMap[thisID]			  				
		  				}
		  				
		  				if( animationOn == false)
		  					dataset.nodes[x].fixed = true;
		  			}
		  			
		  		  if( force && animationOn == false  && dragging == false)
	    			  force.stop();
	    		  
		  		  if( stopOnChild == true)
		  			  stopOnChild=false;

		  		}
		  		
	    	 node.attr("cx", 
					function (d){ 
	    		 				
	    		 				return thisContext.getAVal( d,true)
	    		 			}
	    		 		)
	      	.attr("cy", 
	      			
					function (d){
	      						
	      					return thisContext.getAVal( d,false)}
				)
	    
		  if ( anyLabels )
	      {	
	      	if( graphType == "ForceTree" ) 
	      	{
	      		
	      	text.attr("transform", function(d) { return "translate(" + 
						d.x
							+ "," + d.y+ ")"; });
			
	      	}
	      	else
	      	{
	      		/* radial labels: todo: this should be an option
	      		console.log("set rotate " + Math.PI *
	      				d.listPosition / statics.getNodes().length);
	      		text.attr("transform", function(d) { return "rotate(" + Math.PI *
	      				d.listPosition / statics.getNodes().length
	      					+ ")"});
	      					*/
	      		
	      	text.attr("transform", function(d) { return "translate(" + 
						d.xMap[thisID]
							+ "," + d.yMap[thisID]+ ")"; });
	      	}	      
	      }
			
		if( graphType == "ForceTree"  && ! aDocument.getElementById("hideLinks").checked )
		{
			link.attr("x1", function(d) { return d.source.x; })
	      .attr("y1", function(d) { return d.source.y; })
	      .attr("x2", function(d) { return d.target.x; })
	      .attr("y2", function(d) { return d.target.y; });
		}
		
	      }
	      
	      if( graphType != "ForceTree"  && ! aDocument.getElementById("hideLinks").checked
	    		  && ((thisDocument.getElementById("scatterX").value == "circleX" || 
	  					thisDocument.getElementById("scatterX").value == "circleY" ) && 
						(thisDocument.getElementById("scatterY").value == "circleX" 
							|| thisDocument.getElementById("scatterY").value == "circleY" )))
		  {
	    	  	var depth =0;
	    	  
		    	  function addNodeAndChildren(aNode)
		    	  {
		    		  depth++;
		    		  if( !aNode.doNotShow && aNode.children && aNode.children.length > 0 )
		    		  {
		    			  for( var i=0; i < aNode.children.length; i++)
		    			  {
		    				  var childNode = aNode.children[i];
		    				  
		    				  if( ! childNode.doNotShow)
		    				  {
			    				  vis.append("line").attr("x1", aNode.xMap[thisID]).
			    				  					attr("y1", aNode.yMap[thisID]).
			    				  					attr("x2", childNode.xMap[thisID]).
			    				  					attr("y2", childNode.yMap[thisID]).
			    				  					attr("stroke-width", 0.5).
			    				  					attr("stroke", "black");
			    				  
			    				  addNodeAndChildren( childNode );
		    				  }
		    			  }
		    		  }
		    		  depth--;
		    	  }
		    		  
		    		  
		          addNodeAndChildren(statics.getRoot());
		    		  
			}
	    else if(graphType != "ForceTree" )
	    {
	    	 vis.selectAll("line").remove();
	    }
	      
	      
	    if( graphType == "ForceTree")
	    	force.on("tick", updateNodesLinksText);
		
		//force.on("end", updateNodesLinksText);
	    
	    // Update the links	      	
		if( graphType == "ForceTree" && ! aDocument.getElementById("hideLinks").checked )
		{
			link = vis.selectAll("line.link")
	        .data(links.filter(this.myFilterLinks), function(d) {  return d.target.name; }
	        		);
		}
		
  		
//);
	   
	  // Enter any new links.
		//vis.remove("svg:line");
	  if( graphType == "ForceTree" && ! aDocument.getElementById("hideLinks").checked )
	  link.enter().insert("svg:line", ".node")
	      .attr("class", "link")
	       
	 	var table = aDocument.getElementById("tNodeTable"); //.rows[0].cells[1].item[0] = "" + numMarked ;
	 	
	 	table.rows[0].cells[1].innerHTML = "" + numVisible;
	 	
	 	var row = table.rows[1];
	 	var cell =row.cells[1];
	 	cell.innerHTML = "" + numMarked;
	

	for( var x=0; x < nodes.length; x++)
	 {	
		nodes[x].nodeLabelText = this.getLabelText(nodes[x]);
	 }


	if ( anyLabels  ) 
	{

    	var text=vis.selectAll("text").data(filteredNodes).enter().append("svg:text")
  				.text( function (d) {  return d.parentDataNode.nodeLabelText; })
                 .attr("font-family", "sans-serif")
                 .attr("font-size", aDocument.getElementById("fontAdjust").value + "px")
                 .attr("fill", function(d) {return  thisContext.getTextColor(d.parentDataNode) });
         			 
                    if( graphType != "ForceTree")
	                {
	                	 text.attr("x",
	                	 function (d){return thisContext.getAVal( d,true)})
  						.attr("y", 
  						function (d){return thisContext.getAVal( d,false)})
  						
  						/* todo: radial labels should be an option
  						.attr("transform", 
  	         				 	function(d) 
  	         				 	{ 
  									var anAngle = 360.0 *  
       			 					d.listPosition / (Math.PI *statics.getNodes().length);
  									
  									console.log( anAngle);
  									
  	         			 			return "rotate(" + anAngle + "," 
  	         			 					+ thisContext.getAVal( d,true)
  												+ "," + thisContext.getAVal( d,false) + ")"
  	         			         }
  	         		         );
  	         		         */
                    }
                    else
                    {
                    	text.attr("dx", function(d) { return 15; })
                 		.attr("dy", function(d) { return ".35em"; })
                    }
	
	}
  	 	    
  	 	if( graphType != "ForceTree")
  	 	{
  	 		this.addAxis( 	thisDocument.getElementById("scatterX").value, true);
  	 		this.addAxis( 	thisDocument.getElementById("scatterY").value, false);
  	 	}
  	 	
 		

 // cleanup
  if( graphType == "ForceTree" && ! aDocument.getElementById("hideLinks").checked )
  link.exit().remove();
  
  node.exit().remove();
	}
  	
	// the color choosers don't work unless they are initialized first
  	// hence they are initialized in the "section" and then moved to the appropriate menu
  	// once everything else has settled in...
	if( firstUpdate && isRunFromTopWindow) 
	{
		this.setQuantitativeDynamicRanges();
  		//aDocument.getElementById("ColorSubMenu").appendChild(aDocument.getElementById("color1"));
		//aDocument.getElementById("color1").style.visibility="visible";
		
		//aDocument.getElementById("ColorSubMenu").appendChild(aDocument.getElementById("color2"));
		//aDocument.getElementById("color2").style.visibility="visible";
	}	
  	
  	firstUpdate = false;
}


this.releaseAllFixed = function()
{
	var displayNodes = this.getDisplayDataset().nodes;
	
	for ( var x=0; x < displayNodes.length; x++)
	{
		if( ! displayNodes[x].userMoved)
		{
			displayNodes[x].fixed = false;
			displayNodes[x].x = displayNodes[x].parentDataNode.xMap[thisID];
			displayNodes[x].y= displayNodes[x].parentDataNode.yMap[thisID];
		}
	}
	
	stopOnChild = true;
	animationOn=true;
	
	if(force)
		force.start();
	
	this.redrawScreen();
}


this.getTextColor= function(d)
{
	if(  aDocument.getElementById("textIsBlack").checked ) 
		return "#000000";
		
	var chosen = aDocument.getElementById("colorByWhat").value;
	
	if( statics.getColorScales()[chosen] != null || statics.getRanges()[chosen] != null)
		return this.color(d);
		
}


this.myMouseEnter = function(d)
{
	if( force && animationOn == false && dragging == false)
		force.stop();
	
	if (! aDocument.getElementById("mouseOverHighlights").checked)
		return;
	
	if( statics.getHighlightedNode())
	{
		statics.getHighlightedNode().highlight = false;			
	}
	
	d = d.parentDataNode 
	
	statics.setHighlightedNode(d);
	d.highlight = true;
	lastSelected = d;
	
	infoPane = aDocument.getElementById("rightInfoArea")
	
	var someHTML = "<table>";
	
	for( prop in d)
	{
		if( prop != "forceTreeNodeID" 
				&& prop != "x" 
				&& prop != "y"
				&& prop != "children" 
				&& prop != "fixed" 
					&& prop != "xMap" 
					&& prop != "yMap" 
					&& prop != "xMapNoise"
					&& prop != "yMapNoise" && prop != "highlight" && prop != "nodeLabelText" &&
						prop != "thisNodeRadius" && prop != "thisNodeColor" &&
						prop != "marked" && prop != "doNotShow" && prop != "listPosition" && prop != "px" &&
						prop != "py" && prop != "weight" && prop != "aParentNode" )
		{
			var aVal = "" + d[prop];
			
			//todo: This will truncate long strings..
			someHTML += ( "<tr><td>" +  prop + "</td><td> " + aVal.substring(0,25) + "</td></tr>" )
		}
	}
	
	someHTML += "</table>"
		
	infoPane.innerHTML = someHTML;
	
	dirty = true;
	thisContext.redrawScreen();
}

this.myMouseLeave= function ()
{
	if( force && animationOn == false && dragging == false)
		force.stop();
	
	if (! aDocument.getElementById("mouseOverHighlights").checked)
		return;
	
	if( statics.getHighlightedNode())
	{
		statics.getHighlightedNode().highlight = false;			
	}
	
		
	dirty = true;
	thisContext.redrawScreen();
}

this.setInitialPositions = function ()
{
	if( animationOn == false)
		this.arrangeForcePlot(false);
}

this.arrangeForcePlot = function(arrangeChildren)
{		
	var topNode = statics.getRoot();
	
	if( arrangeChildren && lastSelected)
	{
		topNode = lastSelected;
	}

	var displayNodes = this.getDisplayDataset().nodes;
	
	for( var x=0; x < displayNodes.length; x++)
	{
		displayNodes[x].fixed =false;
		
		if( arrangeChildren == false)
			displayNodes[x].userMoved=false
		
		if(  arrangeChildren &&  topNode == displayNodes[x].parentDataNode )
		{
			displayNodes[x].fixed=true;
			displayNodes[x].userMoved = true;
		}
	}
	
	numVisibleArray = [];
	numAssignedArray = [];
	
	for( var x=0; x <= statics.getMaxLevel(); x++)
	{
		numVisibleArray.push(0);
		numAssignedArray.push(0);
	}
	
	var localMaxLevel = statics.getMaxLevel()
	
	if( topNode != statics.getRoot())
	{
		localMaxLevel =0;
	}
	
	var nodesToRun = nodes;
	
	if( arrangeChildren && lastSelected )
	{
		nodesToRun = [];
		
		function addNodeAndChildren(aNode)
		{
			nodesToRun.push(aNode);
			localMaxLevel = Math.max(aNode.nodeDepth, localMaxLevel);
			
			if( aNode.children)
				for( var x=0; x < aNode.children.length; x++)
					addNodeAndChildren(aNode.children[x]);
		}
		
		addNodeAndChildren(lastSelected);

	}
	
	for( var x=0; x < nodesToRun.length; x++ )
	{
		if( nodesToRun[x].doNotShow==false)
			numVisibleArray[nodesToRun[x].nodeDepth] = numVisibleArray[nodesToRun[x].nodeDepth]+ 1;
	}
	
	// if we are not arranging to a child node
	// the root is at the top of the tree in the center of the screen
	if( topNode == statics.getRoot())
	{
		topNode.xMap[thisID] =  w / 2.0  + 20.0;
		topNode.yMap[thisID] = h /2.0;
	}
	
	var radius = parseFloat( Math.min(w,h))/2.0;
	
	radius = radius - radius * parseFloat(aDocument.getElementById("gravitySlider").value)/100.0;

	var localMinLevel = 0.0;
	
	if(  arrangeChildren &&  lastSelected)
	{
		radius = radius / aDocument.getElementById("localGravity").value;
		localMinLevel = topNode.nodeDepth;
	}
	
	localMinLevel = parseFloat(localMinLevel);
		
	var piTwice= 2.0* Math.PI ;
	
	var range = parseFloat( statics.getMaxLevel() - localMinLevel)
	for( var x=0; x < nodesToRun.length; x++) if( nodesToRun[x].doNotShow==false ) 
	{
		var aPosition = parseFloat(numAssignedArray[nodesToRun[x].nodeDepth])
				/numVisibleArray[nodesToRun[x].nodeDepth];
		
		var aRad = (parseFloat(nodesToRun[x].nodeDepth)- localMinLevel)/range * radius;
		nodesToRun[x].xMap[thisID] = topNode.xMap[thisID]- 
			aRad * Math.cos( piTwice * aPosition) ;
		nodesToRun[x].yMap[thisID]  = aRad * Math.sin( piTwice *  aPosition) + topNode.yMap[thisID];
		numAssignedArray[nodesToRun[x].nodeDepth] = numAssignedArray[nodesToRun[x].nodeDepth]+ 1;
	}
	
	animationOn = false;
	stopOnChild = true;
	
}


this.initialize = function () {
	
  this.flatten();
  
  if( graphType != "ForceTree" )
  {
	  this.addIndividualMenuDynamicMenuContent();
	   
  }
      
  initHasRun = true;
 	dirty = true;
   this.update();
   
   this.toggleVisibilityOfSidebars();
   
   if( graphType == "ForceTree" )
	   this.arrangeForcePlot(false)
	   
   this.redrawScreen();
}

this.getQuantiativeColor= function (d)
{
	var chosen = aDocument.getElementById("colorByWhat").value;
	
	var lowColor = "#" + aDocument.getElementById("quantColorLow").value;
	var highColor ="#" + aDocument.getElementById("quantColorHigh").value; 
		
	var aRange = []
	aRange.push(aDocument.getElementById("lowQuantRange").value);
	aRange.push(aDocument.getElementById("highQuantRange").value);
		
	if( lowColor > highColor) 
	{
		var temp = lowColor
		lowColor=  highColor;
		highColor = temp;
	}
		
	if( aDocument.getElementById("logColor").checked) 
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


this.color= function (d) 
{
	if ( d.highlight == true) 
		return "#fd8d3c"; // orange

	var chosen = aDocument.getElementById("colorByWhat").value;
	
	if( statics.getRanges()[chosen] != null)
		return this.getQuantiativeColor(d);
	
	if( statics.getColorScales()[chosen] != null) 
		return statics.getColorScales()[chosen]( d[chosen] );
		
	if( d._children != null)
		return  "#3182bd";  // bright blue
	
		if(  d.marked )
		return "#000000";  // black
		
		
	return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
		
}

// Toggle children on click.
this.myClick= function (d) {

	var aValue =aDocument.getElementById("clickDoesWhat").value;
	
	if ( aValue == "deletes")
	{
		initHasRun = false;
		d.children=null;
		d._children=null;
		thisContext.initialize();
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
		
		thisContext.initialize();
	}
	else if ( aValue == "hides")
	{
		thisContext.hideAndShow(d);
	}
	
	dirty=true;
	thisContext.update();	
}

this.hideAndShow = function(d)
{
	if( ! d)
		d=statics.getHighlightedNode();
	
	if( ! d)
		d = statics.getRoot();
	
	statics.setHighlightReverse( ! statics.getHighlightReverse() );
	
	if( statics.getHighlightReverse() == false)
	{
		this.showOnlyMarked(false);
	}
	else
	{
		for( var x =0; x < nodes.length; x++)
			nodes[x].doNotShow=true;
	
		thisContext.highlightAllChildren(d);
		thisContext.highlightAllParents(d);
	}	
}


this.highlightAllChildren = function (d)
{
	if( d== null)
		return;

	d.doNotShow = false;
	
	if( ! d.children || d.children == null)
		return;	

	for( var x=0; x < d.children.length; x++) 
	{
		this.highlightAllChildren(d.children[x]);
	}
}

this.highlightAllParents = function (d)
{
	if ( d== null)
		return;

	d.doNotShow = false;
	if( ! d.aParentNode ||  d.aParentNode != null)
	{
		thisContext.highlightAllParents(d.aParentNode);
	}
}


// Returns a list of all nodes under the root.
this.flatten= function () 
{
	if( ! isRunFromTopWindow  )
	{
		nodes = statics.getNodes();
		this.setInitialPositions();
  		this.addDynamicMenuContent();
  		return;
	}

  var myNodes = [];
  var level =0.0;
  
  function addNodeAndChildren( aNode) 
	{
		level++;
		statics.setMaxLevel (Math.max(level,statics.getMaxLevel()));
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
  
  addNodeAndChildren(statics.getRoot());
  
  for( var i=0; i < myNodes.length; i++)
  {
  	if (!myNodes[i].forceTreeNodeID) myNodes[i].forceTreeNodeID = i+1;
  	
  	myNodes[i].listPosition =i;
  	myNodes[i].xMap = {};
  	myNodes[i].yMap = {};
  	myNodes[i].xMapNoise = {};
  	myNodes[i].yMapNoise = {};
  }
  
  nodes = myNodes;
  statics.setNodes(nodes);
  
  this.setInitialPositions();
  this.addDynamicMenuContent();
  
}

this.reforce();

if( isRunFromTopWindow ) 
{
	//aDocument.getElementById("color1").style.visibility="hidden";
	//aDocument.getElementById("color2").style.visibility="hidden";
	//todo: nice error message if file can't be found
	d3.json(getQueryStrings(thisWindow)["FileToOpen"], function(json) 
	{
  		statics.setRoot(json);
  	thisContext.initialize();  // wait until the data is loaded to initialize
	});
}
else
{
	thisContext.initialize();  // data is already loaded - ok to initialize.
}

}
