let passwordForm = document.getElementById('password-form');
let showButton = document.getElementById('toggle-show');
showButton.addEventListener('click', function(){
    if(passwordForm.type === 'password'){
        passwordForm.type = 'text';
        showButton.src = '/imgs/eye-hidden.svg'
    }
    else{
        passwordForm.type = 'password';
        showButton.src = '/imgs/eye-show.svg'
    }   
});

