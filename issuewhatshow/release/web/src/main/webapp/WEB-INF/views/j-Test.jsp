<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!--
	ver.3 : 
		노드 : 노드에 텍스트로 표시, 노드 그룹별 자동 색상
		링크 : 링크 길이 지정값 주기, 링크에 작은 구체 꾸미기
		뷰어 : 노드클릭시 카메라 카메라 줌인
-->
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<style> body { margin: 0; } </style>
 <!-- current resources -->
  <script type="text/javascript" src="resources/js/home.js"></script>
  <script type="text/javascript" src="resources/third-party-etc/three.js"></script>
  <script type="text/javascript" src="resources/third-party-etc/three-spritetext.js"></script>
  <script type="text/javascript" src="resources/third-party-etc/3d-force-graph.min.js"></script>
</head>

<body>
	<div id="3d-graph"></div>
	<script>  
		const gData = {
			nodes : [
				{"id": "조국", "group": 1, "val":5},
    	  		{"id": "사퇴", "group": 2, "val":3},
    	  		{"id": "서초구", "group": 3, "val":2},
    	  		{"id": "시위", "group": 3, "val":2},
    	  		{"id": "촛불", "group": 3, "val":1},
    	  		{"id": "박히야", "group": 4, "val":10},
    	  		{"id": "히야", "group": 4, "val":4},
    	  		{"id": "수원", "group": 4, "val":2}  	  
      		],
      		links : [
    	  		{"source": "사퇴", "target": "조국", "dist" : 1},
    	  		{"source": "서초구", "target": "조국", "dist" : 3},
    	  		{"source": "서초구", "target": "사퇴", "dist" : 3},
    	  		{"source": "촛불", "target": "서초구", "dist" : 7},
    	  		{"source": "시위", "target": "서초구", "dist" : 50},
    	  		{"source": "박히야", "target": "조국", "dist" : 100},
    	  		{"source": "히야", "target": "박히야", "dist" : 5},
    	 		{"source": "수원", "target": "박히야", "dist" : 5},
    			{"source": "히야", "target": "시위", "dist" : 200}
      		]
		};
		const elem = document.getElementById('3d-graph');
		const graph = ForceGraph3D()
			(elem) 	   
			.nodeAutoColorBy('group')      
	        .nodeThreeObject(node => {
				const obj = new THREE.Mesh(
	            	new THREE.SphereGeometry(10),
	            	new THREE.MeshBasicMaterial({ depthWrite: false, transparent: true, opacity: 0 })
	          	);          
				const sprite = new SpriteText(node.id);
					sprite.color = node.color;
	     	    	sprite.textHeight = 8;
	     	    	obj.add(sprite);
	     		return obj;
	        })      
			.linkOpacity(0.3)		
			.linkDirectionalParticles(3)		
			.onNodeHover(node => elem.style.cursor = node ? 'pointer' : null)
	        .onNodeClick(node => {
        		const distance = 40;
	          	const distRatio = 1 + distance/Math.hypot(node.x, node.y, node.z);
		        graph.cameraPosition(
		        	{ x: node.x * distRatio, y: node.y * distRatio, z: node.z * distRatio }, // new position
		            node, // lookAt ({ x, y, z })
		            3000  // ms transition duration
		        );
	        })		
	        .graphData(gData);    
    
		const linkForce = graph
        	.d3Force('link')
        	.distance(link => link.dist);
	</script>  
</body>

</html>