<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<head>
  <meta charset="UTF-8">
  <title>JH-test</title>
  
  
  
  <style> body { margin: 0; } </style>
</head>

<body>
  
  <h1>VISDATA to 3d-force 테스트</h1>
  <button type="button" id="btnGetLast">마지막 timeline visdata 가져오기</button>
  <br/>
  <hr/>
  <div id="3d-graph"></div>
  
  
  <!-- global resources -->
  <%@ include file="global/resources_body.jsp" %>
  
  <!-- current resources -->
  <script type="text/javascript" src="resources/js/home.js"></script>
  <script type="text/javascript" src="resources/third-party-etc/three.js"></script>
  <script type="text/javascript" src="resources/third-party-etc/three-spritetext.js"></script>
  <script type="text/javascript" src="resources/third-party-etc/3d-force-graph.min.js"></script>
  
  <script>
  	$(document).ready(function(){
  		$("#btnGetLast").click(function(){
  			
  			//테스트코드
  			parcel = {
				'key' : 'test',
  				'val' : 100
  			};
  			
  			$.ajax({
		        url: 'apis/getLastTimeline',
		        type: 'post',
		        dataType: 'json',
		        contentType: 'application/json',
		        success: function(data){
		        	console.log("success: apis/getLastTimeline");
		        	vis2force(data);
		        	whatTime(data);
		        },
		        error: function(equest,status,error) {
		        	console.error("fail: apis/getLastTImeline");
		        },
		        data: JSON.stringify(parcel)
		        	
		    });
  		});
  	});
  
  	
  	function vis2force(data){
  		//console.log(data)
  		
  		var nodesJson = [];
  		var linkJson = [];
  		
  		var visdata = JSON.parse(data['visdata']);
  		console.log(visdata);

  		
		var nodes = visdata['nodes'];
		var minmax = getNodeMinMax(nodes);
		var min = minmax['min'];
		var max = minmax['max'];
		var nodeVals = []
		nodes.forEach(function(d, k){
			val = d['val'];
			nodeVals.push(val);
			min = Math.min(min, val);
			max = Math.max(max, val)
			node = { 
				"id" : d['id'],
 				"word" : d['word'],
 				"group" : d['group'],
  				"val" : Math.max(
					Math.min(
						Math.sqrt(((d['val'] - min) / (max - min))*100)*4
						, 20
					)
					, 2
  				)
  			}
			nodesJson.push(node);
			//console.log(node);
  		});
  			  			
		
  		//link 처리
  		//dmatrix 는 link 부분이므로 나중에...
  		var dmatrix = visdata['dmatrix'];
  		var dlines = dmatrix.split("\n");
  		dlines.pop();
  		dlines.forEach(function(d, i){
  			console.log(d)
  		});
  			
  		
  		var mtrx = []
  		for (j in dlines) {
  			var dcols = dlines[j].split(",");
  			mtrx[j] = dcols;
  		}	
  		len = mtrx.length
  			
  			
  		for (j = 1; j < len; j++){
  			var tempLinks = []
  			for(k = 1; k < j; k++) {
  				dist = mtrx[j][k];
  				if (dist > 0.7)
  					continue;
  				else
  					dist = Math.pow(dist*5, 4);
  				
  				var forward = {
 					"source" : nodes[j-1]['id'], "target" : nodes[k-1]['id'], "dist" : dist,
 					"group" : nodes[k-1]['gorup']
   				};
   				var reverse = {
   					"source" : nodes[k-1]['id'], "target" : nodes[j-1]['id'], "dist" : dist	,
   					"group" : nodes[j-1]['gorup']
   				};
  					
 				if (nodeVals[j-1] < nodeVals[k-1]){
 					tempLinks.push(forward);
 				} else if (nodeVals[j-1] > nodeVals[k-1]){
 					tempLinks.push(reverse)
 				} else {
 					tempLinks.push(forward);
 					tempLinks.push(reverse);
 				}
 			}
 			tempLinks.forEach(function(d, k){
				//console.log(d);
				linkJson.push(d);
			});
		}  			
 		
  		//실제 노드를 뿌려주는 함수
		drawGalaxy({ 
			nodes: nodesJson,
			links: linkJson
		});
  		
  	}
  	
  	
  	function getNodeMinMax(nodes){
  		var min = 100;
  		var max = 0;
  		nodes.forEach(function(d, i){
  			min = Math.min(min, d['val']);
  			max = Math.max(max, d['val']);
  		});
  		
  		return {"min" : min, "max" : max}
  	}
  	
  	
  	
  	function drawGalaxy(gData){
  		console.log(gData);
  		const graph = ForceGraph3D()
  		(document.getElementById('3d-graph'))   
		.nodeAutoColorBy('group')      
        .nodeThreeObject(node => {
			const obj = new THREE.Mesh(
            	new THREE.SphereGeometry(10),
            	new THREE.MeshBasicMaterial({ depthWrite: false, transparent: true, opacity: 0 })
          	);          
			const sprite = new SpriteText(node.word);
				sprite.color = node.color;
     	    	sprite.textHeight = node.val;
     	    	obj.add(sprite);
     	    return obj;
        })      
		.linkOpacity(0.05)		
		//.linkDirectionalParticles(3)
        .graphData(gData);
  		
        //graph.d3Force('charge').strength(-500);
        
        //오른쪽 클릭 하면 검색! 
		graph.onNodeRightClick(node => searchingnode(node , gData));
        
		const linkForce = graph
	    	.d3Force('link')
	    	.distance(link => link.dist);
		
	

  	}	
  	
	//시간 빼오기
   	var nowTime = null;
  	var vdata = null;
  	function whatTime(data){
  		//console.log(data)
  		nowTime ="20" + data['yymmdd'];
  		vdata = JSON.parse(data['visdata'])
  	}
  	//클릭시 창 띄우기 해보자
	function searchingnode(node , gData ){
  		
  		console.log(node['word'])
  		//gData.nodes[node['index']-1]['index'] 하면 node index전의 값이 뽑힌다.
  		//console.log(gData.links[node['index']].target['word'])
  		//console.log(gData.links[node['index']].source['word'])
  		var swap = "";
  	  	
  		//if(gData.nodes[node])
  		//val 값으로 구현 하지만 실패실패 슬프당
  	/* 	 for(var i=0 ; i<=vdata.nodes.length-1 ; i++){
  			for(var j = 1 ; j<=vdata.nodes.length-1 ; j++){
  				if(vdata.nodes[j-1]['val'] > vdata.nodes[j]['val']){
  					swap = vdata.nodes[j-1]
  					vdata.nodes[j-1] = vdata.nodes[j]
  					vdata.nodes[j] = swap
  					}
  			}
  		}
  	  	console.log(vdata.nodes)
		for(var i = 0 ; i <=vdata.nodes.length-1 ; i++){
  	  		if(node['word'] == vdata.nodes[i]['word']){
  	  			num1node = vdata.nodes[i+1]['word']
  	  			//num2node = vdata.nodes[i+2]['word']
  	  			
  	  		}
  	  	}  */
  	  	
  	  	//이번에는 거리로 해보자.
  	  	//저장 리스트 
  	  	var distlist = [];
  	  	for(var i = 0 ; i<gData['links'].length ; i++){
  	  		if(node['word'] == gData['links'][i].source['word'] || node['word'] == gData['links'][i].target['word']){
  	  			distlist.push(gData['links'][i])
  	  			
  	  		}
  	  	}
  	  	console.log(distlist)
  	  	
  	  	//저장 리스트 이쁘게 배열 하자
  	  	for(var i = 0 ; i< distlist.length ; i++){
  	  		for(var j = 1 ; j<distlist.length ; j++){
  	  			if(distlist[j-1].dist > distlist[j].dist){
  	  				swap = distlist[j-1]
  	  				distlist[j-1] = distlist[j]
  	  				distlist[j] = swap
  	  			}
  	  		}
  	  	}
  	  	//저장리스트에서 검색 할 것 만 뺴오자.
  	  	var searchnode = [];
  	  	 for(var i = 0 ; i <distlist.length[3] ; i++){
  	  		if(distlist[i].source['word'] != node['word']){
  	  		searchnode.push(distlist[i].source['word'])
  	  		 console.log(distlist[i].souuce['word'])
  	  		}else{
  	  		searchnode.push(distlist[i].target['word'])
  	  		}
  	  	} 
  	  	 console.log(searchnode[0])
  	  	 //클릭시 검색되게!
			window.open("https://search.daum.net/search?DA=STC&cluster=y&ed="
				+ nowTime + "235959"
				+ "&https_on=on&nil_suggest=btn&period=u&q="
				//+ node['word'] + "+" + num1node
				+ node['word'] + "+" + searchnode[0] + "+" + searchnode[1] + "+" + searchnode[2] + "+" + searchnode[3]
		    	+ "&sd="
		    	+ nowTime + "000000"
		    	+ "&w=news" , 'window 팝업' , 'width = 300 , height = 300 , menubar = no , toolbar = no' )
  	}
  	
  	
  </script>
  
  
  
</body>
</html>