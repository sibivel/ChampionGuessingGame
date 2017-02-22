<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import = "myStuff.MainStuff"%>
<!DOCTYPE html>
<html>
	
	<script type='text/javascript' src='scripts/jquery-3.1.1.js'></script>
	<head><title>Guess The Champion</title></head>
	<body>
		<%! MainStuff main = new MainStuff();%>
		<%	main.getInfo("2397222176",1);	%>
		<h1>Guess The Champion!</h1>
		
		
		<table id = 'itemTable' style="width:80%">
		  <h1>Items:</h1>
		  <tr>
		  	<% for(int i = 0; i < 7; i++)
			  	{
					%>
			  		<th class = 'itemName'><%= main.getItemNames()[i] %></th>
					<%
			  	}		  		
		  	%>
		  </tr>
		  <tr>
			<% for(int i = 0; i < 7; i++)
			  	{
				String itemURL = (main.itemImage + main.getItems()[i]) + ".png";
					%>
			  		<td class = 'itemImage'><img src= <%= itemURL %>></td>
					<%
			  	}		  		
		  	%>
			
		  </tr>
		 
		</table>
		
		<table id = 'itemTable' style="width:80%">
		<h1>Keystone Mastery: </h1>
		<tr>
			<td class = 'keystoneName'><%= main.getKeystoneName() %></td>
			<td class = 'keystoneImage'><img src = <%=(main.masteryImage + main.getKeystoneId() + ".png") %>></td>
		</tr>
		</table>
		
		<div id = 'KDA'><%="K/D/A: "+ main.getKda()[0]+ "/" + main.getKda()[1]+"/" + main.getKda()[2]%></div>
		<div id = 'CS'><%="CS: "+ main.getCS()%></div>
		<div id = 'FirstBlood'>
			<%
			String fb;
			if(main.getFirstBlood() == 1)
				fb = "Got First Blood";
			else if(main.getFirstBlood() == 2)
				fb = "Assisted in First Blood";
			else
				fb = "Did not get first blood";
			%>
			<%= fb %>
		</div>
		
		<table id = 'spellTable' style="width:80%">
		<h1>Summoner Spells: </h1>
		<tr>
			<td class = 'spellName'><%= main.getSpellNames()[0] %></td>
			<td class = 'spellName'><%= main.getSpellNames()[1] %></td>
		</tr>
		<tr>
			<td class = 'spellImage'><img src = <%=(main.spellImage + main.getSpellKeys()[0] + ".png") %>></td>
			<td class = 'spellImage'><img src = <%=(main.spellImage + main.getSpellKeys()[1] + ".png") %>></td>
		</tr>
		</table>
		
		<table id = 'skillTable' style="width:80%; border:4px solid black">
			<% int[] skills = main.getSkillLevelUpOrder(); %>
			<tr>
				<%
				for(int i = 0; i < 19; i++){
					if(i == 0){
						%><td></td><%
					}else{
						%><td><%= i %></td><%
					}
						
				}
				%>
			</tr>
			<tr>
				<%
				for(int i = 0; i < 19; i++){
					if(i == 0){
						%><td>Q</td><%
					}else if(skills[i-1] == 1){
						%><td style = "padding: 15px" bgcolor="red"></td><%
					}else{
						%><td style = "padding: 15px" bgcolor="black"></td><%
					}
						
				}
				%>
			</tr>
			<tr>
				<%
				for(int i = 0; i < 19; i++){
					if(i == 0){
						%><td>W</td><%
					}else if(skills[i-1] == 2){
						%><td style = "padding: 15px" bgcolor="red"></td><%
					}else{
						%><td style = "padding: 15px" bgcolor="black"></td><%
					}
						
				}
				%>
			</tr>
			<tr>
				<%
				for(int i = 0; i < 19; i++){
					if(i == 0){
						%><td>E</td><%
					}else if(skills[i-1] == 3){
						%><td style = "padding: 15px" bgcolor="red"></td><%
					}else{
						%><td style = "padding: 15px" bgcolor="black"></td><%
					}
						
				}
				%>
			</tr>
			<tr>
				<%
				for(int i = 0; i < 19; i++){
					if(i == 0){
						%><td>R</td><%
					}else if(skills[i-1] == 4){
						%><td style = "padding: 15px" bgcolor="red"></td><%
					}else{
						%><td style = "padding: 15px" bgcolor="black"></td><%
					}
						
				}
				%>
			</tr>
		</table>
		
		<div id = 'role'><%="Role: "+ main.getRole()%></div>
		
		<table id = 'optionTable' style="width:80%">
		  <h1>Champions:</h1>
		  <tr>
		  	<% for(int i = 0; i < main.getChampOptions().length; i++)
			  	{
					%>
			  		<th class = 'optionName'><%= main.getOptionNames()[i] %></th>
					<%
			  	}		  		
		  	%>
		  </tr>
		  <tr>
			<% for(int i = 0; i < main.getChampOptions().length; i++)
			  	{
				String champURL = (main.champImage + main.getOptionKeys()[i]) + ".png";
					%>
			  		<td class = 'champImage'>
				  		<FORM NAME="form1" METHOD="POST">
					        <INPUT TYPE="HIDDEN" NAME="buttonName">
					        <INPUT TYPE="BUTTON" VALUE="Button 1" ONCLICK="button1()">
					    </FORM>
    					<img src= <%= champURL %>>
   					</td>
					<%
			  	}		  		
		  	%>
			
		  </tr>
		 
		</table>
		
	</body>
</html>