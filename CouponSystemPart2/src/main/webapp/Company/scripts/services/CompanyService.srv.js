

/*service for all company related actions.
 * 
 *  when service functions are called- the relevant HTTP request is sent to the relevant method in webAPI.
 * 
 * 
*/
(function(){
	var app=angular.module("myApp");
	app.service("CompSrv",compSrvCtor);
	
	
		function compSrvCtor($http){
//		this.coupons=[
//		              new CtorService.Coupon(111,"Latte 50% off","11/6/2017","9/22/2017",50,"Food","only valid on weekdays",10.5,""),
//		              new CtorService.Coupon(222,"new shoes!","11/7/2017","10/12/2017",100,"Sports","one color only",350.99,""),
//		              new CtorService.Coupon(333," iphone cover","19/6/2017","12/5/2017",22,"Electricity","original in case",66,"")
//		              ];
//		this.company=new CtorService.Company(111,"doctor Gav", "back","gav@gmail.com");
		//////////////////////////////////////////////////////////////////
		this.createCoupon=function(coupon){
			return $http.post ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/createCoupon",coupon);
    }
/////////////////////////////////////////////////////////////////
		this.removeCoupon=function(id){
			return $http.delete ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/removeCoupon/"+id);
		}

			
/////////////////////////////////////////////////////////////////

		this.updateCoupon=function(coupon){
			return $http.put ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/updateCoupon/"+coupon.id,coupon);
    	 }
        ///////////////////////////////////////////////////////////////
        this.getCoupon=function(id){       	
			return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/getCoupon/"+id);
           
       	 }
        ///////////////////////////////////////////////////////////////

        this.getCompany=function(){
		return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/getCompany");

        }
/////////////////////////////////////////////////////////////////
this.getAllCoupons=function(){
	return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/getAllCoupons");

    }
/////////////////////////////////////////////////////////////////

this.getCouponsByType=function(type){
	console.log(type)
	if(type=="All"){
 	return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/getAllCoupons");
	}else{
 	return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/getCouponsByType/"+type);
	}}

	
//////////////////////////////////////////////////////////////////
this.getCouponsByPrice=function(price){
 	return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/getCouponsUpToPrice/"+price);

	        
		

}
/////////////////////////////////////////////////////////////////
this.getCouponsByEndDate=function(endDate){
	var requestDate = new Date(endDate);
	
	// Converting to a String in the format expected by webAPI ddMMyyyy:
    var formatDate = padDate(requestDate.getDate()) +"."+
                  padDate(1 + requestDate.getMonth()) +"."+
                  padDate(requestDate.getFullYear());
    console.log(formatDate);

	function padDate(i) {
	    return (i < 10) ? "0" + i : "" + i;
	}
 	return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CompanyService/getCouponsUntilEndDate/"+formatDate);
}
		}
				
})();