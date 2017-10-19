var LoginPage = {
	contextPath: '',
	init: function(contextPath) {
		this.contextPath = contextPath;
		this.initLocation();
		this.registerEvt();
	},
	initLocation: function() {
		console.log('---->'+self.location+", ---->"+top.location);
		if(this.location && top.location != this.location){
    	    top.location.replace('loginpage2');
			console.log('top->'+top.location + ', this->'+this.location);
			return;
    	}
	},
	registerEvt: function() {
		$(".name,.pwd,#valid").keyup(function(event){
            if( event.keyCode == 13 ) {
                $('#loginBtn').click();
            }
             
		});
		
		$("form").submit( function(event){
	          var account = $(".name").val(); 
	          var pwd = $(".pwd").val();
	          var code = $("#valid").val();
              if(!account) {
                 $("#err").css("display","inline-block");
                 $("#err").text("请输入用户名！");
                 return false;
              }
              if(!pwd) {
                  $("#err").css("display","inline-block");
                  $("#err").text("请输入密码！");
                  return false;
               }
              if(!code) {
            	  $("#err").css("display","inline-block");
                  $("#err").text("请输入验证码！");
                  return false;
              }
	    });
		
		$('#verifyCodePic').click(function(){
			$(this).attr('src', LoginPage.contextPath+'/images/kaptcha.jpg?t='+Math.random());
	     });
	}
}