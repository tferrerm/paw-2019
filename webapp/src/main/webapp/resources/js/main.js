$(document).ready(function() {

    $("#profilePictureButton").change(function() {
        $("#filenameDisplay").text(event.target.files[0].name)
    })

    $("#pitchPictureButton").change(function() {
        $("#filenameDisplay").text(event.target.files[0].name)
    })

});


