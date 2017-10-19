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
    	    top.location.replace('loginpage');
			console.log('top->'+top.location + ', this->'+this.location);
			return;
    	}
	},
	registerEvt: function() {
		$('.theme-kaptcha-img').click(function() {
			$(this).attr('src', LoginPage.contextPath+'/images/kaptcha.jpg?t='+Math.random());
		});
		$('#loginBtn').click(function() {
			if(LoginPage.validLogin()) {
				$('#loginForm').submit();
			}else{
				return false;
			}
		});
	},
	validLogin: function() {
		$('#frontErrorSpan').text('');
		var username = $('#username').val();
		if(!username) {
			$('#frontErrorSpan').text('用户名不能为空');
			$('#username').focus();
			return false;
		}
		var password = $('#password').val();
		if(!password) {
			$('#frontErrorSpan').text('密码不能为空');
			return false;
		}
		var kaptchaId = $('#kaptcha').attr('id');
		if(kaptchaId) {
			var kaptcha = $('#kaptcha').val();
			if(!kaptcha) {
				$('#frontErrorSpan').text('验证码不能为空');
				$('#kaptcha').focus();
				return false;
			}
		}
		
		return true;
	}
};