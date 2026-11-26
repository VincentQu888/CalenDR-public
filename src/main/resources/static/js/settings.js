function showName(){
    document.getElementById("settings-card-name").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideName(){
    document.getElementById("settings-card-name").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    document.getElementById('name-input').value = "";
    event.preventDefault();
}

function saveName(){
    if(document.getElementById('name-input').value === "") {
        alert('Please fill in all required fields.');
    }else{
        document.getElementById("name-input-info").value = "name";
        document.getElementById("settings-card-name").action = "/save";
        document.getElementById("settings-card-name").submit();
        hideName();
    }
}


function showPassword(){
    document.getElementById("settings-card-password").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hidePassword(){
    document.getElementById("settings-card-password").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    document.getElementById("old-passworda").value = "";
    document.getElementById("password-input").value = "";
    event.preventDefault();
}

function savePassword(){
    // Hidden for public source release.
    throw new Error('Security implementation omitted from public source.');
}


function showCountry() {
    document.getElementById("settings-card-country").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideCountry() {
    document.getElementById("settings-card-country").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    event.preventDefault();
}

function saveCountry() {
    document.getElementById("country-input-info").value = "countryOfResidence";
    document.getElementById("settings-card-country").action = "/save";
    document.getElementById("settings-card-country").submit();
    hideCountry();
}


function showNotification() {
    document.getElementById("settings-card-notification").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideNotification() {
    document.getElementById("settings-card-notification").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    event.preventDefault();
}

function saveNotification() {
    document.getElementById("notification-input-info").value = "notificationPreference";
    document.getElementById("settings-card-notification").action = "/save";
    document.getElementById("settings-card-notification").submit();
    hideNotification();
}


// function showImmunization() {
//     document.getElementById("settings-card-immunization").style.display = "block";
//     document.getElementById("darkened-overlay").style.display = "block";
// }

// function hideImmunization() {
//     document.getElementById("settings-card-immunization").style.display = "none";
//     document.getElementById("darkened-overlay").style.display = "none";
//     event.preventDefault();
// }

// function saveImmunization() {
//     document.getElementById("immunization-input-info").value = "immunizationProgress";
//     document.getElementById("settings-card-immunization").action = "/save";
//     document.getElementById("settings-card-immunization").submit();
//     hideImmunization();
// }


function showSexuallyActive() {
    document.getElementById("settings-card-sexuallyActive").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideSexuallyActive() {
    document.getElementById("settings-card-sexuallyActive").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    document.getElementById('sexuallyActive-input').value = "";
    event.preventDefault();
}

function saveSexuallyActive() {
    if(document.getElementById('sexuallyActive-input').value === "") {
        alert('Please fill in all required fields.');
    }else{
        document.getElementById("sexuallyActive-input-info").value = "sexuallyActiveAge";
        document.getElementById("settings-card-sexuallyActive").action = "/save";
        document.getElementById("settings-card-sexuallyActive").submit();
        hideSexuallyActive();
    }
}


function showSmoking() {
    document.getElementById("settings-card-yearsSmoking").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideSmoking() {
    document.getElementById("settings-card-yearsSmoking").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    document.getElementById('yearsSmoking-input').value = "";
    event.preventDefault();
}

function saveSmoking() {
    if(document.getElementById('yearsSmoking-input').value === "") {
        alert('Please fill in all required fields.');
    }else{
        document.getElementById("yearsSmoking-input-info").value = "timeSmoking";
        document.getElementById("settings-card-yearsSmoking").action = "/save";
        document.getElementById("settings-card-yearsSmoking").submit();
        hideSmoking();
    }
}


function showRisks() {
    document.getElementById("settings-card-risks").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideRisks() {
    document.getElementById("settings-card-risks").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    event.preventDefault();
}

function saveRisks() {
    document.getElementById("risks-input-info").value = "familyRisks";
    document.getElementById("settings-card-risks").action = "/save";
    document.getElementById("settings-card-risks").submit();
    hideRisks();
}


function showSchedule() {
    document.getElementById("settings-card-schedule").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideSchedule() {
    document.getElementById("settings-card-schedule").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    event.preventDefault();
}

function saveSchedule() {
    document.getElementById("schedule-input-info").value = "schedule";
    document.getElementById("settings-card-schedule").action = "/save";
    document.getElementById("settings-card-schedule").submit();
    hideSchedule();
}


function deleteAccount(){
    document.getElementById("settings-card-delete").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function deleteConfirm(){
    // Hidden for public source release.
    throw new Error('Security implementation omitted from public source.');
}

function hideDelete(){
    document.getElementById("settings-card-delete").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    document.getElementById("delete-password-input").value = "";
    event.preventDefault();
}

function deleteProcessingPopUp(){
    document.getElementById("settings-card-delete-title").innerHTML = "Verifying Password...";
    document.getElementById("delete-password-input").style.display = "none";
    document.getElementById("delete-password-cancel").style.display = "none";
    document.getElementById("delete-password-save").style.display = "none";
    document.getElementById("delete-password-input").value = "";
    event.preventDefault();
}