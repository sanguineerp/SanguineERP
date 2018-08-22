/**
 * 
 */



function funCheckNetworkConnection()
{
	if(chkNetwork==1)
	{
	var searchUrl=getContextPath()+"/checkNetworkConnection.html";
	$.ajax({
        type: "GET",
        url: searchUrl,
        dataType: "text",
        asyn:false,
        
        success: function(response)
        {
        	
        },
        
        error: function(jqXHR, exception) {
                alert('Network Issue');
                chkNetwork=chkNetwork+1;
   
        }
	});
	 $(document).ajaxStart(function(){
	 		$("#wait").css("display","none");
			  });
	 
	}
	
	
}