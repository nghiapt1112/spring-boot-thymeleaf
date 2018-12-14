var check = function () {
    if (document.getElementById('password').value ==
        document.getElementById('confirm_password').value) {
        document.getElementById('message').innerHTML = '';
        document.getElementById('btnSubmit').disabled = false;
    } else {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = '“パスワード”と”パスワード(確認用)”が一致しません。!';
        document.getElementById('btnSubmit').disabled = true;
        return false;
    }
}
var btnSubmit = document.forms["register-form"]["btn-submit"];
btnSubmit.onclick = function () {
    var txtEmail = document.forms["register-form"]["email"].value;
    var str = txtEmail.includes("@");
    if (str == 0 || str == null || str >= txtEmail.length) {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = 'メールアドレスを正しく入力してください。';
        return false;
    }
}
