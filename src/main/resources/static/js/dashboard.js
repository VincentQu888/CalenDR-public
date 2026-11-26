function changeInfoRange(){
    document.getElementById("info-range-form").action = "/dashboard";
    document.getElementById("info-range-form").submit();
}


function remove(){
    let missedAppointment = document.getElementById("remove-id").innerText;
    document.getElementById("missed-appointment-input").value = missedAppointment;
    document.getElementById("missed-item").action = "/remove";
}
