/*
*(c) Copyright 2011 Simone Masiero. Some Rights Reserved. 
*This work is licensed under a Creative Commons Attribution-Noncommercial-Share Alike 3.0 License
*/

$(
	function(){
		$( document ).mouseup(
			function ( event ) {
				if(event.keyCode != 8) Typer.addText( event ); //Capture the keydown event and call the addText, this is executed on page load
				else { Typer.removeText(); event.preventDefault(); }
			}
		).mousewheel(function(e) {
			Typer.speed += Math.ceil(Typer.speedStep) * e.deltaY;
			Typer.speed = (Typer.speed < Math.floor(Typer.speedMin)) ? Math.floor(Typer.speedMin) : (Typer.speed > Math.floor(Typer.speedMax)) ? Math.floor(Typer.speedMax) : Typer.speed;
			console.log(Typer.speed);
		});
	}
);

var Typer={
	text: null,
	accessCountimer:null,
	index:0, // current cursor position
	speedMax:12,
	speedMin:1,
	writeCount:0,
	speedStep:0.5,
	speed:2, // speed of the Typer
	file:"", //file, must be setted
	accessCount:0, //times alt is pressed for Access Granted
	deniedCount:0, //times caps is pressed for Access Denied
	secCount:0, //times caps is pressed for Access Denied
		coldCount:0, //times caps is pressed for Access Denied
	secCount:0, //times caps is pressed for Access Denied
	radarCount:0, 
		windowCount:0, 
			ponyCount:0, 
	tagList:[],
	typeIntervalCounter:0,
	typeInterval:false,
	init: function(){// inizialize Hacker Typer
		accessCountimer=setInterval(function(){Typer.updLstChr();},500); // inizialize timer for blinking cursor
		$.get(Typer.file,function(data){// get the text file
			Typer.text=data;// save the textfile in Typer.text
		});
	},
	
	content:function(){
		return $("#console").html();// get console content
	},
	
	write:function(str){// append to console content
		$("#console").append(str);
		return false;
	},

	
	removeText:function(){
		if(Typer.text){

			Typer.index = (Typer.index > 0) ? Typer.index - Typer.speed * 2 : 0;
			Typer.addText(event);
		}
	},
	
	achievementGet:function(){
		var snd = new Audio("../ASSETS/achievements/AchievementUnlocked.mp3");
		snd.play();
		$('.box').animate({'bottom':'0px'},200);
		$(".box").delay(3200).animate({'bottom':'-100px'},200);
	},
	
	
	
	
	addTextAuto:function(key){
	
	Typer.writeCount++;
	
	

	
		if(Typer.text){ // otherway if text is loaded
			
			if(Typer.index <= 0) {
				$("#console").html('');
			}
			
			Typer.index = Typer.index % Typer.text.length
			
			var cont=Typer.content(); // get the console content
			if(cont.substring(cont.length-1,cont.length)=="_") // if the last char is the blinking cursor
				$("#console").html($("#console").html().substring(0,cont.length-1)); // remove it before adding the text

			var text=Typer.text.substr(Typer.index,Typer.speed)//Typer.index-(Typer.speed));// parse the text for stripping html enities
			var rtn= new RegExp("\n", "g"); // newline regex
			//var rts= new RegExp("\\s", "g"); // whitespace regex
			var rtt= new RegExp("\\t", "g"); // tab regex
			text = text.replace(rtn,"<br/>").replace(rtt,"&nbsp;&nbsp;&nbsp;&nbsp;");//.replace(rts,"&nbsp;");// replace newline chars with br, tabs with 4 space and blanks with an html blank
			//console.log(text);
			
			$("#console").append(text);

			
			var usedTags = new RegExp("<img.*?>", "g"),
				systemTags = new RegExp("{(.*?)}", "g");
			var foundTag = usedTags.exec(Typer.text.substring(Typer.index)),
				foundSystemTag = systemTags.exec(Typer.text.substring(Typer.index));
			if(foundTag instanceof Array && foundTag.index <= Typer.speed) {
				Typer.index += foundTag.index + foundTag[0].length;
				$("#console").append(foundTag[0]);
			} else if(foundSystemTag instanceof Array && foundSystemTag.index <= Typer.speed) {
				//Typer.text.replace(/{(.*?)}/,foundSystemTag[1]);
				Typer.index += foundSystemTag.index + foundSystemTag[0].length;
				$("#console").append(foundSystemTag[1]);
			} else Typer.index += Typer.speed;
			$('body').scrollTop($("#console").height()); 
			window.scrollBy(0,50); // scroll to make sure bottom is always visible 
			
		}
		if ( key.preventDefault && key.keyCode != 122 ) { // prevent F11(fullscreen) from being blocked
			key.preventDefault()
		};  
		if(key.keyCode != 122){ // otherway prevent keys default behavior
			key.returnValue = false;
		}
	},
	
	updLstChr:function(){ // blinking cursor
		var cont=this.content(); // get console 
		if(cont.substring(cont.length-1,cont.length)=="_") // if last char is the cursor
			$("#console").html($("#console").html().substring(0,cont.length-1)); // remove it
		else
			this.write("_"); // else write it
	


			
	
	
	
	},

	addText:function(key){//Main function to add the code
	
	

	
	
	Typer.writeCount++;
	document.getElementById("LetterCount").innerHTML = "You typed: " + Typer.writeCount + " characters";
						
				getTypingSound = localStorage.getItem("typingsound") || '0';		
						
						var snd = new Audio(getTypingSound);
		snd.play();
	
	if (Typer.writeCount == 1500) {
		getAchievement = localStorage.getItem("lazy") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("lazy", "1");
		document.getElementById("achievementname").innerHTML = "Lazy College Senior";
		document.getElementById("icon").src = "../ASSETS/achievements/lazycollegesenior.png";
		document.getElementById("getAchievement1").innerHTML = "<img title='Lazy&#13;Type 1500 Characters' src=\"../ASSETS/achievements/progress/lazy/1.png\">";
		Typer.achievementGet();
      }
	  else {return undefined;}
	}
   
   
	if (Typer.writeCount == 3000) {
		getAchievement = localStorage.getItem("shakespeare") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("shakespeare", "1");
		document.getElementById("achievementname").innerHTML = "Settle down Shakespeare";
		document.getElementById("icon").src = "../ASSETS/achievements/shakespeare.png";
		document.getElementById("getAchievement2").innerHTML = "<img title='Shakespeare&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/shakespeare/1.png\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
   
	if (Typer.writeCount == 8000) {
		getAchievement = localStorage.getItem("dostoyevsky") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("dostoyevsky", "1");
		document.getElementById("achievementname").innerHTML = "Woah there Dostoyevsky";
		document.getElementById("icon").src = "../ASSETS/achievements/dostoyevsky.png";
		document.getElementById("getAchievement3").innerHTML = "<img title='Dostoyevsky&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/dostoyevsky/1.png\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
   
	if (Typer.writeCount == 14000) {
		getAchievement = localStorage.getItem("aynrand") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("aynrand", "1");
		document.getElementById("achievementname").innerHTML = "Keep it up Ayn Rand";
		document.getElementById("icon").src = "../ASSETS/achievements/aynrand.png";
		document.getElementById("getAchievement4").innerHTML = "<img title='Ayn Rand&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/aynrand/1.png\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
   
	if (Typer.writeCount == 20000) {
		getAchievement = localStorage.getItem("monkeys") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("monkeys", "1");
		document.getElementById("achievementname").innerHTML = "1000 Monkeys typing";
		document.getElementById("icon").src = "../ASSETS/achievements/1000monkeys.png";
		document.getElementById("getAchievement5").innerHTML = "<img title='1000 Monkeys, 1000 Typewriters&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/monkeys/1.png\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
	if (Typer.writeCount == 50000) {
		getAchievement = localStorage.getItem("watson") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("watson", "1");
		document.getElementById("achievementname").innerHTML = "You are IBM Watson";
		document.getElementById("icon").src = "../ASSETS/achievements/ibmwatson.png";
		document.getElementById("getAchievement6").innerHTML = "<img title='IBM Watson&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/watson/1.png\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
   
	if (Typer.writeCount == 100000) {
		getAchievement = localStorage.getItem("celestia") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("celestia", "1");
		document.getElementById("achievementname").innerHTML = "You are Celestia";
		document.getElementById("icon").src = "../ASSETS/achievements/celestia.png";
		document.getElementById("getAchievement7").innerHTML = "<img title='Princess Celestia&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/celestia/1.png\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
   
	if (Typer.writeCount == 1000000) {
		getAchievement = localStorage.getItem("impossibru") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("impossibru", "1");
		document.getElementById("achievementname").innerHTML = "IMPOSSIBRU";
		document.getElementById("icon").src = "../ASSETS/achievements/impossibru.png";
		document.getElementById("getAchievement8").innerHTML = "<img title='IMPOSSIBRU&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/impossibru/1.png\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
   
   
      
	if (Typer.writeCount == 1000000) {
		getAchievement = localStorage.getItem("hax") || '0';
		if (getAchievement == 0) {  
		localStorage.setItem("hax", "1");
		document.getElementById("achievementname").innerHTML = "HAAAAAAAAAAX";
		document.getElementById("icon").src = "../ASSETS/achievements/hax.gif";
		document.getElementById("getAchievement9").innerHTML = "<img title='HAAAAAAAX&#13;Type 300 Characters' src=\"../ASSETS/achievements/progress/hax/1.gif\">";
		Typer.achievementGet();      }
	  else {return undefined;}
   }
	
	
		if(key.keyCode==109){// key 18 = alt key
			Typer.accessCount++; //increase counter 
			if(Typer.accessCount>=1){// if it's presed 3 times
				Typer.makeAccess(); // make access popup
			}
		}else if(Typer.text){ // otherway if text is loaded
			
			if(Typer.index <= 0) {
				$("#console").html('');
			}
			
			Typer.index = Typer.index % Typer.text.length
			
			var cont=Typer.content(); // get the console content
			if(cont.substring(cont.length-1,cont.length)=="_") // if the last char is the blinking cursor
				$("#console").html($("#console").html().substring(0,cont.length-1)); // remove it before adding the text
			/*
			if(key.keyCode!=8){ // if key is not backspace
				Typer.index+=Typer.speed;	// add to the index the speed
			}else{
				if(Typer.index>0) // else if index is not less than 0 
					Typer.index-=Typer.speed;//	remove speed for deleting text
			}
			*/
			var text=Typer.text.substr(Typer.index,Typer.speed)//Typer.index-(Typer.speed));// parse the text for stripping html enities
			var rtn= new RegExp("\n", "g"); // newline regex
			//var rts= new RegExp("\\s", "g"); // whitespace regex
			var rtt= new RegExp("\\t", "g"); // tab regex
			text = text.replace(rtn,"<br/>").replace(rtt,"&nbsp;&nbsp;&nbsp;&nbsp;");//.replace(rts,"&nbsp;");// replace newline chars with br, tabs with 4 space and blanks with an html blank
			//console.log(text);
			$("#console").append(text);
			
			var usedTags = new RegExp("<img.*?>", "g"),
				systemTags = new RegExp("{(.*?)}", "g");
			var foundTag = usedTags.exec(Typer.text.substring(Typer.index)),
				foundSystemTag = systemTags.exec(Typer.text.substring(Typer.index));
			if(foundTag instanceof Array && foundTag.index <= Typer.speed) {
				Typer.index += foundTag.index + foundTag[0].length;
				$("#console").append(foundTag[0]);
			} else if(foundSystemTag instanceof Array && foundSystemTag.index <= Typer.speed) {
				//Typer.text.replace(/{(.*?)}/,foundSystemTag[1]);
				Typer.index += foundSystemTag.index + foundSystemTag[0].length;
				$("#console").append(foundSystemTag[1]);
			} else Typer.index += Typer.speed;
			$('body').scrollTop($("#console").height()); 
			window.scrollBy(0,50); // scroll to make sure bottom is always visible 
			
			// scroll to make sure bottom is always visible
			/*
			if(Typer.typeInterval) {
				clearInterval(Typer.typeInterval);
				Typer.typeIntervalCounter = 0;
			}
			Typer.typeInterval = setInterval(function() {
			console.log(Typer.typeIntervalCounter);
			//console.log(Typer.speed);
			var text=Typer.text.substring(0,Typer.index-(Typer.speed-Typer.typeIntervalCounter));// parse the text for stripping html enities
			var rtn= new RegExp("\n", "g"); // newline regex
			//var rts= new RegExp("\\s", "g"); // whitespace regex
			var rtt= new RegExp("\\t", "g"); // tab regex
			text = text.replace(rtn,"<br/>").replace(rtt,"&nbsp;&nbsp;&nbsp;&nbsp;");//.replace(rts,"&nbsp;");// replace newline chars with br, tabs with 4 space and blanks with an html blank
			//console.log(text);
			$("#console").html(text);
			$('body').scrollTop($("#console").height()); // scroll to make sure bottom is always visible
			if(Typer.typeIntervalCounter>Typer.speed) {
				clearInterval(Typer.typeInterval);
				Typer.typeIntervalCounter = 0;
			}
			Typer.typeIntervalCounter++;
			},10);
			*/
		}
		if ( key.preventDefault && key.keyCode != 122 ) { // prevent F11(fullscreen) from being blocked
			key.preventDefault()
		};  
		if(key.keyCode != 122){ // otherway prevent keys default behavior
			key.returnValue = false;
		}
	},
	
	updLstChr:function(){ // blinking cursor
		var cont=this.content(); // get console 
		if(cont.substring(cont.length-1,cont.length)=="_") // if last char is the cursor
			$("#console").html($("#console").html().substring(0,cont.length-1)); // remove it
		else
			this.write("_"); // else write it
	}
}

function startTime()
{
var today=new Date();
var h=today.getHours();
var m=today.getMinutes();
var s=today.getSeconds();
// add a zero in front of numbers<10
m=checkTime(m);
s=checkTime(s);
document.getElementById('txt').innerHTML=h+":"+m+":"+s;
t=setTimeout(function(){startTime()},500);
}



//var sec = 15
//var timer = setInterval(function() { 
//   $('#hideMsg span').text(sec--);
//   if (sec == -1) {
//      		localStorage.setItem("targetaudience", "1");
//		document.getElementById("achievementname").innerHTML = "Target Audience";
//		document.getElementById("icon").src = "../ASSETS/achievements/targetaudience.png";
//		document.getElementById("getAchievement5").innerHTML = "<img title='Target Audience&#13;Spend 30 seconds here' src=\"../ASSETS/achievements/progress/targetaudience/1.png\">";
//		Typer.achievementGet(); 
//      clearInterval(timer);
//   } 
//}, 1000);






function checkTime(i)
{
if (i<10)
  {
  i="0" + i;
  }
return i;
}