let tosAgree = false;
let tosCheckbox = document.getElementById('tos-check-box');
tosCheckbox.addEventListener('change', function() {
    if(tosCheckbox.checked) {
        tosAgree = true;
    } 
    else {
        tosAgree = false;
    }
});


function signup(){
    // Hidden for public source release.
    throw new Error('Security implementation omitted from public source.');
}


let sexuallyActiveCheckbox = document.getElementById('sexually-active-check-box');
sexuallyActiveCheckbox.addEventListener('change', function() {
    document.getElementById('sexually-active-age-input').value = "";

    if(sexuallyActiveCheckbox.checked) {
        document.getElementById('sexually-active-form').style.display = 'flex';
    } 
    else {
        document.getElementById('sexually-active-form').style.display = 'none';
    }
});

let smokingActiveCheckbox = document.getElementById('smoking-check-box');
smokingActiveCheckbox.addEventListener('change', function() {
    document.getElementById('smoking-input').value = "";

    if(smokingActiveCheckbox.checked) {
        document.getElementById('smoking-form').style.display = 'flex';
    } 
    else {
        document.getElementById('smoking-form').style.display = 'none';
    }
});

let ageForm = document.getElementById('age-form');
ageForm.addEventListener('change', function() {
    document.getElementById('parent-approval-check-box').checked = false;

    let birthdate = new Date(document.getElementById('age-form').value);
    let today = new Date();
    if(today.getFullYear() - birthdate.getFullYear() <= 13){
        document.getElementById('parent-approval-check-box-container').style.display = 'flex';
    }
    else{
        document.getElementById('parent-approval-check-box-container').style.display = 'none';
    }
});

let immunizationProgressForm = document.getElementById('immunization-progress-form');
immunizationProgressForm.addEventListener('change', function() {
    if(immunizationProgressForm.value !== '0'){
        document.getElementById('previous-form-container').style.display = 'flex';
    }
    else{
        document.getElementById('previous-form-container').style.display = 'none';
        document.getElementById('previous-form').value = '0';
    }
});

let previousForm = document.getElementById('previous-form');
previousForm.addEventListener('change', function() {
    if(previousForm.value !== '0'){
        document.getElementById('last-visit-form-container').style.display = 'flex';
    }
    else{
        document.getElementById('last-visit-form-container').style.display = 'none';
        document.getElementById('last-visit-form').value = '';
    }
});


let passwordForm = document.getElementById('password-form');
// Hidden for public source release: password validation.

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

let passwordFormConfirm = document.getElementById('password-confirm-form');
// Hidden for public source release: password confirmation.

let showButtonConfirm = document.getElementById('toggle-show-confirm');
showButtonConfirm.addEventListener('click', function(){
    if(passwordFormConfirm.type === 'password'){
        passwordFormConfirm.type = 'text';
        showButtonConfirm.src = '/imgs/eye-hidden.svg'
    }
    else{
        passwordFormConfirm.type = 'password';
        showButtonConfirm.src = '/imgs/eye-show.svg'
    }   
});


const currentDate = new Date().toISOString().split("T")[0];
document.getElementById("age-form").setAttribute("max", currentDate);