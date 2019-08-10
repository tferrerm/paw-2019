$(document).ready(function() {

    $("#profilePictureButton").change(function() {
        $("#filenameDisplay").text(event.target.files[0].name)
    })

    $("#pitchPictureButton").change(function() {
        $("#filenameDisplay").text(event.target.files[0].name)
    })

    $("#eventStartHour").on("change", function() {
       var startVal = $("#eventStartHour").val();
       var endVal = $("#eventEndHour").val();
       if (startVal >= endVal) {
         var startIndex = $("#eventStartHour").prop('selectedIndex');
         $("#eventEndHour").prop('selectedIndex', startIndex);
       }
     })

   $("#eventEndHour").on("change", function() {
      var startVal = $("#eventStartHour").val();
      var endVal = $("#eventEndHour").val();
      if (startVal >= endVal) {
        var endIndex = $("#eventEndHour").prop('selectedIndex');
        $("#eventStartHour").prop('selectedIndex', endIndex);
      }
    })

});
