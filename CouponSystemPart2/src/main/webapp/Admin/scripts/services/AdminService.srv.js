
/*service for all admin actions
 * 
 *  when service functions are called- the relevant HTTP request is sent to the relevant method in webAPI.
 * 
 * 
 * **/
(function() {
	var app= angular.module("myApp");
	app.service("adminSrv", adminSrvCtor);

	
	function adminSrvCtor($http){
//		this.customers=[
//		                  new CtorService.Customer(111,"George Bush","998877"),
//		                  new CtorService.Customer(222,"Harrison ford","btr25"),
//		                  new CtorService.Customer(333,"Henry Ford","443326")
//		                  ];
//		this.companies=[
//		                 new CtorService.Company(111,"doctor Gav", "back","gav@gmail.com"),
//		                 new CtorService.Company(222,"doctor Leck", "ice","glida@gmail.com"),
//		                 new CtorService.Company(333,"doctor shakshuka", "eggs","eggs@gmail.com"),
//		                 new CtorService.Company(444,"doctor nargila", "huff22","smoke@gmail.com")
//		                 
//		             ]
//		
//		
			   ///////////////////////////////////////////////////////////////
		 this.getAllCustomers = function(){
			return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/adminService/getCustomer");
         }
		             ///////////////////////////////////////////////////////////////
             this.getAllCompanies = function(){

            	 return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/adminService/getCompany");
         }

             ///////////////////////////////////////////////////////////////
             this.addCompany=function(company){
      	 
            	 return $http.post("http://localhost:8080/CouponSystemPart2/webapi/adminService/addCompany",company);

            	 
             }

                 ///////////////////////////////////////////////////////////////
                 this.addCustomer=function(customer){
            	 return $http.post("http://localhost:8080/CouponSystemPart2/webapi/adminService/addCustomer",customer);
 
                 }
                                     ///////////////////////////////////////////////////////////////
                     this.getCompany=function(id){
                     						return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/adminService/getCompany/"+id);

                    	 }
                     
				///////////////////////////////////////////////////////////////

					this.getCustomer=function(id){
              			return $http.get ("http://localhost:8080/CouponSystemPart2/webapi/adminService/getCustomer/"+id);


                   	 }
					///////////////////////////////////////////////////////////////
					this.removeCompany=function(id){
						return $http.delete("http://localhost:8080/CouponSystemPart2/webapi/adminService/deleteCompany/"+id);

                	 }
					///////////////////////////////////////////////////////////////
					this.removeCustomer=function(id){
           			return $http.delete("http://localhost:8080/CouponSystemPart2/webapi/adminService/deleteCustomer/"+id);

                	 }
					///////////////////////////////////////////////////////////////
					this.updateCompany=function(company){
						return $http.put("http://localhost:8080/CouponSystemPart2/webapi/adminService/updateCompany/"+company.id,company);
                	 }
						
					///////////////////////////////////////////////////////////////
					this.updateCustomer=function(customer){
						return $http.put("http://localhost:8080/CouponSystemPart2/webapi/adminService/updateCustomer/"+customer.id,customer);

					}
					
						
					
					
                   
	
	}	
})();