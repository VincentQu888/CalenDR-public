const mediaQuery = window.matchMedia("(max-width: 1000px)");
let showType = 'inline';
if(mediaQuery.matches){
    showType = 'block';
} 



document.getElementById("date-picker").min = new Date().toISOString().split("T")[0];
document.getElementById("notify-date").min = new Date().toISOString().split("T")[0];
document.getElementById("date-picker").addEventListener("input", function (){
    document.getElementById("notify-date").max = this.value;
});

document.getElementById("edit-date-picker").min = new Date().toISOString().split("T")[0];
document.getElementById("edit-notify-date").min = new Date().toISOString().split("T")[0];
document.getElementById("edit-date-picker").addEventListener("input", function (){
    document.getElementById("edit-notify-date").max = this.value;
});

function addAppointment() {
    document.getElementById("add-appointment-card").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";
}

function hideAdd() {
    document.getElementById("add-appointment-card").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    event.preventDefault();
}

function addConfirm() {
    var input = document.getElementById("custom-title").value;
    var type = document.getElementById('add-appointment-type').value;
    if(input.length > 64){
        alert('Text too long, try using 64 characters or less.');
    }else if(type === 'custom' && /[!@#$%^&*()\-+={}[\]:;"'<>,.?\/|\\]/.test(input)){
        alert('Text contains special characters.');
    }else if(document.getElementById('date-picker').value === ""  || document.getElementById('notify-date').value === "" || (type === 'custom' && input === "")){
        alert('Please fill in all forms.');
    }else{
        document.getElementById("add-appointment-card").action = "/saveAppointment";
        document.getElementById("add-appointment-card").submit();
        hideImmunization();
    }
}

function editAppointment(type, date, index, notiDate) {
    document.getElementById("edit-appointment-card").style.display = "block";
    document.getElementById("darkened-overlay").style.display = "block";

    let appointmentType = document.getElementById("edit-appointment-type");
    let immunizationType = document.getElementById("edit-immunization-type");
    let cancerType = document.getElementById("edit-cancer-type");
    let typeKnown = false;

    for(let i = 0; i < immunizationType.options.length; ++i){
        if(type === immunizationType.options[i].value) {
            appointmentType.options[0].selected = true;
            immunizationType.options[i].selected = true;
            typeKnown = true;

            document.getElementById('edit-immunization-type').style.display = showType;
            document.getElementById('edit-custom-title').style.display = 'none';
            document.getElementById('edit-cancer-type').style.display = 'none';
            break;
        }
    }
    for(let i = 0; i < cancerType.options.length; ++i){
        if(type === cancerType.options[i].value) {
            appointmentType.options[1].selected = true;
            cancerType.options[i].selected = true;
            typeKnown = true;

            document.getElementById('edit-cancer-type').style.display = showType;
            document.getElementById('edit-custom-title').style.display = 'none';
            document.getElementById('edit-immunization-type').style.display = 'none';
            break;
        }
    }
    if(type === 'Influenza Shot') {
        appointmentType.options[2].selected = true;
        typeKnown = true;
    }
    if(!typeKnown){
        appointmentType.options[3].selected = true;
        document.getElementById('edit-custom-title').style.display = showType;
        document.getElementById('edit-immunization-type').style.display = 'none';
        document.getElementById('edit-cancer-type').style.display = 'none';
        document.getElementById('edit-custom-title').value = type;
    }

    document.getElementById('edit-date-picker').value = date;
    document.getElementById('edit-info-date').value = date;
    document.getElementById('edit-info-index').value = index;
    document.getElementById('edit-notify-date').value = notiDate;
}

function hideEdit() {
    document.getElementById("edit-appointment-card").style.display = "none";
    document.getElementById("darkened-overlay").style.display = "none";
    event.preventDefault();
}

function editConfirm() {
    var input = document.getElementById("edit-custom-title").value;
    var type = document.getElementById("edit-appointment-type").value;
    if(input.length > 64){
        alert('Text too long, try using 64 characters or less.');
    }else if(type === 'custom' && /[!@#$%^&*()\-+={}[\]:;"'<>,.?\/|\\]/.test(input)){
        alert('Text contains special characters.');
    }else if(document.getElementById('edit-date-picker').value === "" || document.getElementById('edit-notify-date').value === "" || (type === 'custom' && input === "")){
        alert('Please fill in all forms.');
    }else{
        document.getElementById("edit-appointment-card").action = "/editAppointment";
        document.getElementById("edit-appointment-card").submit();
        hideImmunization();
    }
}

function deleteAppointment(){
    document.getElementById("edit-appointment-card").action = "/deleteAppointment"
    document.getElementById("edit-appointment-card").submit();
}


let appointmentType = document.getElementById('add-appointment-type');
let editAppointmentType = document.getElementById('edit-appointment-type');
appointmentType.addEventListener('change', function() {
    if(appointmentType.value === 'custom') {
        document.getElementById('custom-title').style.display = showType;
        document.getElementById('immunization-type').style.display = 'none';
        document.getElementById('cancer-type').style.display = 'none';
    } 
    else if(appointmentType.value === 'immunization') {
        document.getElementById('immunization-type').style.display = showType;
        document.getElementById('custom-title').style.display = 'none';
        document.getElementById('cancer-type').style.display = 'none';
    }else if(appointmentType.value === 'cancer') {
        document.getElementById('cancer-type').style.display = showType;
        document.getElementById('custom-title').style.display = 'none';
        document.getElementById('immunization-type').style.display = 'none';
    }else{
        document.getElementById('custom-title').style.display = 'none';
        document.getElementById('immunization-type').style.display = 'none';
        document.getElementById('cancer-type').style.display = 'none';
    }
});
editAppointmentType.addEventListener('change', function() {
    if(editAppointmentType.value === 'custom') {
        document.getElementById('edit-custom-title').style.display = showType;
        document.getElementById('edit-immunization-type').style.display = 'none';
        document.getElementById('edit-cancer-type').style.display = 'none';
    } 
    else if(editAppointmentType.value === 'immunization') {
        document.getElementById('edit-immunization-type').style.display = showType;
        document.getElementById('edit-custom-title').style.display = 'none';
        document.getElementById('edit-cancer-type').style.display = 'none';
    }else if(editAppointmentType.value === 'cancer') {
        document.getElementById('edit-cancer-type').style.display = showType;
        document.getElementById('edit-custom-title').style.display = 'none';
        document.getElementById('edit-immunization-type').style.display = 'none';
    }else{
        document.getElementById('edit-custom-title').style.display = 'none';
        document.getElementById('edit-immunization-type').style.display = 'none';
        document.getElementById('edit-cancer-type').style.display = 'none';
    }
});



document.getElementById('appointment-list').addEventListener('input', function(event) {
    const target = event.target;

    if (target && target.tagName === 'INPUT' && target.type === 'date') {
        const initialValue = target.defaultValue;
        
        if (target.value !== initialValue) {
            target.value = initialValue;
        }
    }
});
