/**Checks if user has cookies and if so- 
 * uses them to fill out the login form automatically. enables Auto Login.*/

var checkForCookies=function(){
	
	var cookiesArray= document.cookie.split(";");
	var id,password,type;
	
	  for(var i = 0; i <cookiesArray.length; i++) {
	        var cookieStr = cookiesArray[i];
	        while (cookieStr.charAt(0) == ' ') {
	        	cookieStr = cookieStr.substring(1);
	        }
	        if (cookieStr.indexOf("userID") == 0) {
	        	id=cookieStr.substring("userID=".length, cookieStr.length);
	        	console.log("id "+ id);
	        	
	        }else if(cookieStr.indexOf("password") == 0){
	        	password=cookieStr.substring("password=".length, cookieStr.length);
	        	console.log("password "+password);
	        	
	        }else if(cookieStr.indexOf("clientType") == 0){
	        	type=cookieStr.substring("clientType=".length, cookieStr.length);
	        	console.log("type "+type);
	        }
	  }
	        
	        if(id!=undefined && password!=undefined && type!=undefined){
	        	document.getElementById('id').value = id;
	        	document.getElementById('password').value = password;
	        	document.getElementById('clientType').value = type;
	        	document.forms[0].submit();
	        }
	    
	
	}
