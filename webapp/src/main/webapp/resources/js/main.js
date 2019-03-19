$(document).ready(function(){



    var updateTable = function() {
        $.ajax({
            url: "http://localhost:8080/foo",
            context: document.body
        }).done(function(data) {
            alert(data);
        });
    };

    $("#ajaxTest").click(function () {
        updateTable();
    });

    $("#profilePictureButton").change(function(){
        $("#filenameDisplay").text(event.target.files[0].name)
    })


});


