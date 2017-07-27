/*service for all customer related actions.
 * 
 *  when service functions are called- the relevant HTTP request is sent to the relevant method in webAPI.
 * 
 */

(function(){
	
	var app=angular.module("myApp");
	app.service("customerSrv",customerSrvCtor);
	
	function customerSrvCtor($http){
//		this.myCoupons=[
//		              new CtorService.Coupon(111,"Latte 50% off","11/6/2017","9/22/2017",50,"Food","only valid on weekdays",10.5,""),
//		              new CtorService.Coupon(222,"new shoes!","11/7/2017","10/12/2017",100,"Sports","one color only",350.99,""),
//		              new CtorService.Coupon(333," iphone cover","19/6/2017","12/5/2017",22,"Electricity","original in case",66,"")
//		              ]
//		this.allCoupons=[
//		              new CtorService.Coupon(111,"Latte 50% off","11/6/2017","9/22/2017",50,"Food","only valid on weekdays",10.5,""),
//		              new CtorService.Coupon(222,"new shoes!","11/7/2017","10/12/2017",100,"Sports","one color only",350.99,""),
//		              new CtorService.Coupon(333," iphone cover","6/19/2017","12/5/2017",22,"Electricity","original in case",66,""),
//		              new CtorService.Coupon(444," Galaxy 6s charger","6/19/2017","01/15/2018",36,"Electricity","original",102,""),
//		              new CtorService.Coupon(555,"cake decoration course","6/19/2017","09/15/2017",50,"Health","4 sessions of 4 hours",400,""),
//		              new CtorService.Coupon(666,"nail polish OPI","6/19/2017","01/01/2018",20,"Health","price includes VAT",75,""),
//		              new CtorService.Coupon(777,"outDoor tent","7/19/2017","12/31/2017",100,"Camping","not allowed outside",285,""),
//		                  ];
		this.getAllPurchasedCoupons=function(){
			return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CustomerService/getAllPurchasedCoupons");

    }
		////////////////////////////////////////////////////
		this.getPurchasedByType=function(type){
			if(type!="All"){
				return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CustomerService/getAllPurchasedCouponsByType/"+type);
			}
			else{return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CustomerService/getAllPurchasedCoupons");

			}}
			        
////////////////////////////////////////////////////
		this.getPurchasedByPrice=function(price){
       
			return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CustomerService/getAllPurchasedCouponsByPrice/"+price);

	}
////////////////////////////////////////////////////
		this.getAll=function(){
			return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/CustomerService/getAllCoupons");
  
		}
////////////////////////////////////////////////////
		this.purchaseCoupon=function(coupon){
			return $http.post ("http://localhost:8080/CouponSystemPart2/webapi/CustomerService/purchaseCoupon",coupon);
		}
	
	}
	
})();