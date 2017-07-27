(function(){
	var app= angular.module("myApp");
	app.service("CtorService", CtorServiceFunc);
	
	function CtorServiceFunc(){
	
	this.Customer=function(id,name,password){
		this.id=id;
		this.name=name;
		this.password=password;
	};
	this.Company=function(id,name,password,email){
	    this.id=id;
	    this.name=name;
	    this.password=password;
	    this.email=email;
	};
	this.Coupon=function(id,title,startDate,endDate,amount,type,message,price,image){
		this.id=id;
		this.title=title;
		this.startDate=startDate;
		this.endDate=new Date(endDate);
		//this.endDate.format("mm/dd/yy");	
		this.amount=amount;
		this.type=type;
		this.message=message;
		this.price=Number(price);
		this.image=image;
		
	}
	}
	
	
})();